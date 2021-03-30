package com.epam.esm.dao.database;

import com.epam.esm.dao.DaoException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.PersistentException;
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
        // Создадим базу данных для тестирования
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()// Добавляем скрипты schema.sql и data.sql
                .setType(EmbeddedDatabaseType.H2)// Используем базу H2
                .build();

        // Создадим JdbcTemplate
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        giftCertificateDao = new GiftCertificateDaoImpl(jdbcTemplate);
    }

    @Test
    public void testReadAll() throws  DaoException {
        // Создадим базу данных для тестирования
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()// Добавляем скрипты schema.sql и data.sql
                .setType(EmbeddedDatabaseType.H2)// Используем базу H2
                .build();

        // Создадим JdbcTemplate
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