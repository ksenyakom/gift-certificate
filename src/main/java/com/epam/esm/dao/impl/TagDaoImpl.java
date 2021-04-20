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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    private static Logger logger = LogManager.getLogger(TagDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    @NotNull
    public Integer create(@NotNull Tag entity) throws DaoException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                return ps;
            }, keyHolder);
            Number number =  keyHolder.getKey();
            Integer id = number == null ? null : number.intValue();
            if (id == null) {
                throw new DaoException("There is no autoincremented index after trying to add record into table `tag`", "30");
            }
            logger.debug("New tag created with id={}, name={}", id, entity.getName());

            return id;
        } catch (DataAccessException e) {
            throw new DaoException("Can not create new Tag. Name = " + entity.getName(), "11", e);
        }
    }

    @Override
    @Nullable
    public Tag read(@NotNull Integer id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(READ, new BeanPropertyRowMapper<>(Tag.class), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read Tag (id = " + id + ").", "12", e);
        }
    }

    @Override
    public void read(@NotNull Tag tag) throws DaoException {
        try {
            String name = jdbcTemplate.queryForObject(READ_NAME, String.class, tag.getId());
            tag.setName(name);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read Tag name (id = " + tag.getId() + ").", "17", e);
        }
    }

    @Override
    public void delete(@NotNull Integer id) throws DaoException {
        try {
            jdbcTemplate.update(DELETE, id);
            logger.debug("Deleted tag with id={}", id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not delete Tag (id = " + id + ").", "14", e);
        }
    }

    @Override
    @Nullable
    public List<Tag> readAll() throws DaoException {
        try {
            return jdbcTemplate.query(READ_ALL, new BeanPropertyRowMapper<>(Tag.class));
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all Tag", "15", e);
        }
    }

    @Override
    @Nullable
    public List<GiftCertificate> readCertificateByTag(@NotNull Integer id) throws DaoException {
        try {
            return jdbcTemplate.query(READ_CERTIFICATES_BY_TAG, new BeanPropertyRowMapper<>(GiftCertificate.class), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read certificates by tag", "16", e);
        }
    }

    @Override
    @Nullable
    public List<Tag> readByName(@NotNull String tagName) throws DaoException {
        try {
            return jdbcTemplate.query(READ_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), tagName);
        } catch (DataAccessException e) {
            throw new DaoException("No tags with name =" + tagName, "21", e);
        }
    }
}
