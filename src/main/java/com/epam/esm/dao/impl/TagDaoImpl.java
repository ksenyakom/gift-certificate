package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    private static Logger logger = LogManager.getLogger(TagDaoImpl.class);


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE = "INSERT INTO tag (name) values(?)";
    private static final String READ = "SELECT * FROM tag WHERE id = ?";
    private static final String READ_BY_NAME = "SELECT * FROM tag WHERE name  LIKE CONCAT('%', ?, '%')";
    private static final String READ_NAME = "SELECT name FROM tag WHERE id = ?";
    private static final String DELETE = "DELETE FROM tag WHERE id = ?";
    private static final String READ_ALL = "SELECT * FROM tag";
    private static final String READ_CERTIFICATES_BY_TAG = "SELECT * FROM certificate_tag where tag_id = ?";


    @Override
    public Integer create(Tag entity) throws DaoException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                return ps;
            }, keyHolder);

            Integer id = keyHolder.getKey() == null ? null : keyHolder.getKey().intValue();
            logger.debug("New tag created with id={}, name={}", id, entity.getName());

            return id;
        } catch (DataAccessException e) {
            throw new DaoException("Can not create new Tag. Name = " + entity.getName(), "11", e);
        }
    }

    @Override
    public Tag read(Integer id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(READ, new BeanPropertyRowMapper<>(Tag.class), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read Tag (id = " + id + ").", "12", e);
        }
    }

    @Override
    public void read(Tag tag) throws DaoException {
        try {
            String name = jdbcTemplate.queryForObject(READ_NAME, String.class, tag.getId());
            tag.setName(name);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read Tag name (id = " + tag.getId() + ").", "17", e);
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        try {
            jdbcTemplate.update(DELETE, id);
            logger.debug("Deleted tag with id={}", id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not delete Tag (id = " + id + ").", "14", e);
        }
    }

    @Override
    public List<Tag> readAll() throws DaoException {
        try {
            return jdbcTemplate.query(READ_ALL, new BeanPropertyRowMapper<>(Tag.class));
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all Tag", "15", e);
        }
    }

    @Override
    public List<GiftCertificate> readCertificateByTag(Integer id) throws DaoException {
        try {
            return jdbcTemplate.query(READ_CERTIFICATES_BY_TAG, new BeanPropertyRowMapper<>(GiftCertificate.class), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read certificates by tag", "16", e);
        }
    }

    @Override
    public List<Tag> readByName(String tagName) throws DaoException {
        try {
            return jdbcTemplate.query(READ_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), tagName);
        } catch (DataAccessException e) {
            throw new DaoException("No tags with name =" + tagName, "21", e);
        }
    }
}
