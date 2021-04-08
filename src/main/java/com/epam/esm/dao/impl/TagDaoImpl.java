package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
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

@Component
public class TagDaoImpl implements TagDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE = "INSERT INTO tag (name) values(?)";
    private static final String READ = "SELECT * FROM tag WHERE id = ?";
    private static final String DELETE = "DELETE FROM tag WHERE id = ?";
    private static final String READ_ALL = "SELECT * FROM tag";


    @Override
    public Integer create(Tag entity) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DaoException("Can not create new Tag. Name = " + entity.getName(), "11", e);
        }

        return keyHolder.getKey() == null ? null : keyHolder.getKey().intValue();
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
    public void update(Tag entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) throws DaoException {
        try {
            jdbcTemplate.update(DELETE, id);
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
}
