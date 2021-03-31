package com.epam.esm.dao;

import com.epam.esm.dao.Dao;
import com.epam.esm.dao.DaoException;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public interface GiftCertificateDao extends Dao<GiftCertificate> {

    RowMapper<GiftCertificate> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getInt("id"));
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        //TODO add all fields
//        giftCertificate.setCreateDate(resultSet.getTimestamp("create_date"));
//        giftCertificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date"));
//        giftCertificate.setPrice();
//        giftCertificate.setDuration();
//        giftCertificate.setIsActive();
        return giftCertificate;
    };

    List<GiftCertificate> readAll() throws DaoException;

}
