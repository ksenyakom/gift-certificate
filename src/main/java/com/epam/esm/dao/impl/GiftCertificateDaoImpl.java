package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String CREATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, is_active) values(?,?,?,?,?,?)";
    private final static String READ = "SELECT * FROM gift_certificate WHERE id = ?";
    private final static String UPDATE = "UPDATE gift_certificate SET name = ?, description = ?,price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private final static String DELETE = "UPDATE gift_certificate SET is_active = false WHERE id = ?";
    private final static String READ_ALL = "SELECT * FROM gift_certificate";

    private final static String READ_TAG_BY_CERTIFICATE = "SELECT * FROM certificate_tag WHERE certificate_id = ?";
    private final static String CREATE_CERTIFICATE_TAG = "INSERT INTO  certificate_tag (certificate_id, tag_id) values (?,?)";
    private final static String DELETE_CERTIFICATE_TAG = "DELETE FROM certificate_tag WHERE certificate_id = ? AND tag_id = ?";
    private final static String ADD_CERTIFICATE_TAG = "INSERT INTO certificate_tag (certificate_id, tag_id) VALUES (?,?)";

    @Override
    public Integer create(GiftCertificate entity) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getDescription());
                ps.setBigDecimal(3, entity.getPrice());
                ps.setInt(4, entity.getDuration());
                LocalDateTime createDate = entity.getCreateDate();
                ps.setString(5, createDate == null ? null : createDate.toString());
                ps.setBoolean(6, true);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DaoException("Can not create new GiftCertificate. Name = " + entity.getName(), "01", e);
        }

        Integer certificateId = keyHolder.getKey() == null ? null : keyHolder.getKey().intValue();
        if (certificateId != null && entity.getTags() != null) {
            for (Tag tag : entity.getTags()) {
                jdbcTemplate.update(CREATE_CERTIFICATE_TAG, certificateId, tag.getId());
            }
        }
        return certificateId;
    }

    @Override
    public GiftCertificate read(Integer id) throws DaoException {
        try {
            GiftCertificate certificate = jdbcTemplate.queryForObject(READ, ROW_MAPPER, id);
            readTagsForCertificate(certificate);
            return certificate;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read GiftCertificate (id = " + id + ").", "02", e);
        }
    }

    @Override
    public void update(GiftCertificate certificate) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(), certificate.getDuration(), certificate.getLastUpdateDate(), certificate.getId());
            updateTags(certificate);
        } catch (DataAccessException e) {
            throw new DaoException("Can not update GiftCertificate (id = " + certificate.getId() + ").", "03", e);
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        try {
            jdbcTemplate.update(DELETE, id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not delete GiftCertificate (id = " + id + ").", "04", e);
        }
    }

    @Override
    public List<GiftCertificate> readAll() throws DaoException {
        try {
            List<GiftCertificate> certificates = jdbcTemplate.query(READ_ALL, ROW_MAPPER);
            if (!certificates.isEmpty()) {
                for (GiftCertificate certificate : certificates) {
                    readTagsForCertificate(certificate);
                }
            }
            return certificates;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all GiftCertificates", "05", e);
        }
    }

    private void readTagsForCertificate(GiftCertificate certificate) {
        if (certificate != null) {
            List<Tag> tags = jdbcTemplate.query(
                    READ_TAG_BY_CERTIFICATE, new BeanPropertyRowMapper<>(Tag.class), certificate.getId());
            certificate.setTags(tags);
        }
    }

    private void updateTags(GiftCertificate certificate) {
        if (certificate.getTags() != null) {
            List<Tag> newTagList = certificate.getTags();
            List<Tag> existingTags = jdbcTemplate.query(READ_TAG_BY_CERTIFICATE, new BeanPropertyRowMapper<>(Tag.class), certificate.getId());
            List<Tag> tagsToDelete = existingTags.stream().filter(tag -> !newTagList.contains(tag)).collect(Collectors.toList());
            List<Tag> tagsToAdd = newTagList.stream().filter(tag -> !existingTags.contains(tag)).collect(Collectors.toList());
            if (!tagsToDelete.isEmpty()) {
                for (Tag tag : tagsToDelete) {
                    jdbcTemplate.update(DELETE_CERTIFICATE_TAG, certificate.getId(), tag.getId());
                }
            }
            if (!tagsToAdd.isEmpty()) {
                for (Tag tag : tagsToAdd) {
                    jdbcTemplate.update(ADD_CERTIFICATE_TAG, certificate.getId(), tag.getId());
                }
            }
        }
    }
}
