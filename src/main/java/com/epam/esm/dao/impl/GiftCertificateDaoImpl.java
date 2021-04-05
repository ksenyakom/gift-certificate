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

import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final String UPDATE = "UPDATE gift_certificate SET name = ?2, description = ?3 WHERE id = ?1";
    private static final String DELETE = "UPDATE gift_certificate SET is_active = false WHERE id = ?";
    private static final String READ_ALL = "SELECT * FROM gift_certificate";

    @Override
    public Integer create(GiftCertificate entity) {
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
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            ps.setString(5, nowAsISO);
            ps.setBoolean(6, true);
            return ps;
        }, keyHolder);

        return (Integer) keyHolder.getKey();
    }


    @Override
    public GiftCertificate read(Integer id)  {
        GiftCertificate giftCertificate = null;
            giftCertificate = jdbcTemplate.queryForObject(READ, ROW_MAPPER, id);
//        } catch (DataAccessException e) {
//            throw new DaoException(e.getMessage(), e);
//        }
        return giftCertificate;
    }

    @Override
    public void update(GiftCertificate entity) {
        //TODO add all fields
        jdbcTemplate.update(UPDATE, entity.getId(), entity.getName(), entity.getDescription());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public List<GiftCertificate> readAll()  {
        return jdbcTemplate.query(READ_ALL, ROW_MAPPER);
    }
}
