package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


class GiftCertificateDaoImplTest {
    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    private GiftCertificateDao giftCertificateDao;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        giftCertificateDao = new GiftCertificateDaoImpl(jdbcTemplate);
    }

    @Test
    public void testReadAll() throws  DaoException {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        giftCertificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        Assert.assertNotNull(giftCertificateDao.readAll());
        Assert.assertEquals(3, giftCertificateDao.readAll().size());
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void create() {
    }

    @Test
    void read() {
    }

    @Test
    void readAll() {

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}