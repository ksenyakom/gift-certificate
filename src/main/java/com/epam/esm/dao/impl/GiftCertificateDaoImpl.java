package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDao {
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

        return keyHolder.getKey() == null ? null : keyHolder.getKey().intValue();
    }


    @Override
    public GiftCertificate read(Integer id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(READ, ROW_MAPPER, id);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read GiftCertificate (id = " + id + ").", "02", e);
        }
    }

    @Override
    public void update(GiftCertificate entity) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE,
                    entity.getName(), entity.getDescription(), entity.getPrice(), entity.getDuration(), entity.getLastUpdateDate(), entity.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can not update GiftCertificate (id = " + entity.getId() + ").", "03", e);
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
            return jdbcTemplate.query(READ_ALL, ROW_MAPPER);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all GiftCertificates", "05", e);
        }
    }
}
