package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
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
        giftCertificate.setPrice(resultSet.getBigDecimal("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setIsActive(resultSet.getBoolean("is_active"));
        return giftCertificate;
    };

    List<GiftCertificate> readAll() ;

}
