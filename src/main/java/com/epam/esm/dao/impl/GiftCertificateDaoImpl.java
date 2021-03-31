package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE
            = "insert into gift_certificate (`name`, `description`, `price`, `duration`, `create_date`,`is_active`) values(?,?,?,?,?,?)";

    @Override
    public Integer create(GiftCertificate entity) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setBigDecimal(3, entity.getPrice());
            ps.setInt(4, entity.getDuration());
//TODO remove from here
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            ps.setString(5, nowAsISO);
            ps.setBoolean(6, true);
            return ps;
        }, keyHolder);

        return (Integer) keyHolder.getKey();
    }


    @Override
    public GiftCertificate read(Integer id) throws DaoException {
        GiftCertificate giftCertificate = null;
        try {
            giftCertificate = jdbcTemplate.queryForObject("select * from gift_certificate where id = ?", ROW_MAPPER, id);
        } catch (DataAccessException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return giftCertificate;
    }

    @Override
    public void update(GiftCertificate entity) throws DaoException {
        //TODO add all fields
        jdbcTemplate.update("update gift_certificate set name = ?2, description = ?3 where id = ?1", entity.getId(), entity.getName(), entity.getDescription());
    }

    @Override
    public void delete(Integer id) throws DaoException {
        jdbcTemplate.update("update gift_certificate set is_active = false where id = ?", id);
    }

    @Override
    public List<GiftCertificate> readAll() throws DaoException {
        return jdbcTemplate.query("select * from gift_certificate", ROW_MAPPER);
    }
}
