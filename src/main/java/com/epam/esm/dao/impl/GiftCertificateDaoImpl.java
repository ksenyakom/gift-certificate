package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static Logger logger = LogManager.getLogger(GiftCertificateDaoImpl.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, is_active) values(?,?,?,?,?,?)";
    private static final String READ = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String UPDATE = "UPDATE gift_certificate SET name = ?, description = ?,price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private static final String DELETE = "UPDATE gift_certificate SET is_active = false WHERE id = ?";
    private static final String READ_ALL = "SELECT * FROM gift_certificate";
    private static final String READ_BY_NAME = "SELECT * FROM gift_certificate WHERE name LIKE CONCAT('%', ?, '%')";

    private static final String READ_BY_TAGID = "SELECT name, description, price, duration, create_date,  last_update_date, is_active, certificate_id as id FROM gift_certificate `c` join `certificate_tag` `t` on c.id = t.certificate_id  WHERE t.tag_id = ?";
    private static final String READ_BY_NAME_AND_TAGID = "SELECT name, description, price, duration, create_date,  last_update_date, is_active, certificate_id as id FROM gift_certificate `c` join `certificate_tag` `t` on c.id = t.certificate_id  WHERE c.name LIKE CONCAT('%', ?, '%') AND t.tag_id = ?";

    private static final String READ_TAG_BY_CERTIFICATE = "SELECT tag_id as id FROM certificate_tag WHERE certificate_id = ?";
    private static final String CREATE_CERTIFICATE_TAG = "INSERT INTO  certificate_tag (certificate_id, tag_id) values (?,?)";
    private static final String DELETE_CERTIFICATE_TAG = "DELETE FROM certificate_tag WHERE certificate_id = ? AND tag_id = ?";
    private static final String ADD_CERTIFICATE_TAG = "INSERT INTO certificate_tag (certificate_id, tag_id) VALUES (?,?)";

    @Override
    @NonNull
    public Integer create(@NonNull GiftCertificate certificate) throws DaoException {
        try {
            Integer certificateId = createCertificate(certificate);
            if (certificateId == null) {
                throw new DaoException("There is no autoincremented index after trying to add record into table `gift_certificate`", "31");
            }
            createCertificateTag(certificateId, certificate);
            logger.debug("New certificate created with id={}", certificateId);

            return certificateId;
        } catch (DataAccessException e) {
            throw new DaoException("Can not create new GiftCertificate. Name = " + certificate.getName(), "01", e);
        }
    }

    private Integer createCertificate(@NonNull GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            LocalDateTime createDate = certificate.getCreateDate();
            ps.setString(5, createDate == null ? null : createDate.toString());
            ps.setBoolean(6, true);
            return ps;
        }, keyHolder);
        Number number = keyHolder.getKey();
        return number == null ? null : number.intValue();
    }

    private void createCertificateTag(@Nullable Integer certificateId, @NonNull GiftCertificate certificate) {
        if (certificateId != null && certificate.getTags() != null) {
            for (Tag tag : certificate.getTags()) {
                jdbcTemplate.update(CREATE_CERTIFICATE_TAG, certificateId, tag.getId());
            }
        }
    }

    @Override
    @Nullable
    public GiftCertificate read(@NonNull Integer id) throws DaoException {
        try {
            GiftCertificate certificate = jdbcTemplate.queryForObject(READ, ROW_MAPPER, id);
            readTagsForCertificate(certificate);

            return certificate;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read GiftCertificate (id = " + id + ").", "02", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readAll() throws DaoException {
        try {
            List<GiftCertificate> certificates = jdbcTemplate.query(READ_ALL, ROW_MAPPER);
            if (!certificates.isEmpty()) {
                readTagsForCertificate(certificates);
            }

            return certificates;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all GiftCertificates", "05", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readByTags(@NonNull List<Tag> tags) throws DaoException {
        try {
            List<GiftCertificate> certificates = new ArrayList<>();
            for (Tag tag : tags) {
                certificates.addAll(jdbcTemplate.query(READ_BY_TAGID, ROW_MAPPER, tag.getId()));
            }
            certificates = certificates.stream().distinct().collect(Collectors.toList());
            readTagsForCertificate(certificates);

            return certificates;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read certificates by tags (tag = " + tags.toString() + ").", "22", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readByName(@NonNull String name) throws DaoException {
        try {
            List<GiftCertificate> certificates = jdbcTemplate.query(READ_BY_NAME, ROW_MAPPER, name);
            readTagsForCertificate(certificates);

            return certificates;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read certificates by name (name = " + name + ").", "23", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readByNameAndTagName(@NonNull String name, @NonNull List<Tag> tags) throws DaoException {
        try {
            List<GiftCertificate> certificates = new ArrayList<>();
            for (Tag tag : tags) {
                certificates.addAll(jdbcTemplate.query(READ_BY_NAME_AND_TAGID, ROW_MAPPER, name, tag.getId()));
            }
            certificates = certificates.stream().distinct().collect(Collectors.toList());
            readTagsForCertificate(certificates);

            return certificates;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read certificates by tags (tag = " + tags.toString() + ").", "22", e);
        }
    }

    @Override
    public void update(@NonNull GiftCertificate certificate) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(), certificate.getDuration(), certificate.getLastUpdateDate(), certificate.getId());
            updateCertificateTags(certificate);
            logger.debug("Certificate was updated with id={}", certificate.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can not update GiftCertificate (id = " + certificate.getId() + ").", "03", e);
        }
    }

    @Override
    public void delete(@NonNull Integer id) throws DaoException {
        try {
            jdbcTemplate.update(DELETE, id);
            logger.debug("Certificate was deleted(isActive=false) with id={}", id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not delete GiftCertificate (id = " + id + ").", "04", e);
        }
    }

    private void readTagsForCertificate(@Nullable List<GiftCertificate> certificates) {
        if (certificates != null) {
            Map<Integer, Tag> tagMap = new HashMap<>();
            Tag tag;
            List<Tag> tags;

            for (GiftCertificate certificate : certificates) {
                List<Integer> tagsId = jdbcTemplate.queryForList(READ_TAG_BY_CERTIFICATE, Integer.class, certificate.getId());
                tags = new ArrayList<>();
                certificate.setTags(tags);
                for (Integer id : tagsId) {
                    tag = tagMap.merge(id, new Tag(id), (oldValue, newValue) -> oldValue);
                    tags.add(tag);
                }
            }
        }
    }

    private void readTagsForCertificate(@Nullable GiftCertificate certificate) {
        if (certificate != null) {
            List<Tag> tags = jdbcTemplate.query(READ_TAG_BY_CERTIFICATE, new BeanPropertyRowMapper<>(Tag.class), certificate.getId());
            certificate.setTags(tags);
        }
    }

    private void updateCertificateTags(@NonNull GiftCertificate certificate) {
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
