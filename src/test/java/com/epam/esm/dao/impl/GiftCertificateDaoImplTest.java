package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateDaoImplTest {
    private static EmbeddedDatabase embeddedDatabase;

    private static GiftCertificateDao giftCertificateDao;

    @BeforeAll
    public static void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        giftCertificateDao = new GiftCertificateDaoImpl(jdbcTemplate);
    }

    @AfterAll
    public static void tearDown() {
        embeddedDatabase.shutdown();
    }

    @ParameterizedTest
    @CsvSource({
            "Visiting CosmoCaixa, Scientific museum in Barcelona, 49, 90"
    })
    void createRead(ArgumentsAccessor arguments) throws DaoException {
        GiftCertificate giftCertificate = new GiftCertificate(arguments.getString(0),
                arguments.getString(1),
                arguments.get(2, BigDecimal.class),
                arguments.getInteger(3));
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        giftCertificate.setCreateDate(localDateTime);

        int id = giftCertificateDao.create(giftCertificate);
        GiftCertificate actual = giftCertificateDao.read(id);

        assertAll("GiftCertificates should be equal",
                () -> {
                    assertNotNull(actual);
                    assertEquals(giftCertificate.getName(), actual.getName());
                    assertEquals(giftCertificate.getDescription(), actual.getDescription());
                    assertEquals(0, giftCertificate.getPrice().compareTo(actual.getPrice()));
                    assertEquals(giftCertificate.getDuration(), actual.getDuration());
                    assertEquals(localDateTime, actual.getCreateDate());
                    assertTrue(actual.getIsActive());
                    assertNull(actual.getLastUpdateDate());
                });
        giftCertificateDao.delete(id);
    }

    @Test
    void readByName() throws DaoException {
        String name = "cut";

        assertEquals(1, giftCertificateDao.readByName(name).size());
    }

    @Test
    void readByTags() throws DaoException {
        Tag tag = new Tag(1);

        assertEquals(2, giftCertificateDao.readByTags(Collections.singletonList(tag)).size());
    }

    @Test
    void readException() {
        int notExistingId = 100;

        assertThrows(DaoException.class, () -> giftCertificateDao.read(notExistingId));
    }

    @Test
    void createException() {
        GiftCertificate emptyCertificate = new GiftCertificate();

        assertThrows(DaoException.class, () -> giftCertificateDao.create(emptyCertificate));
    }

    @Test
    void update() throws DaoException {
        int id = 1;
        GiftCertificate giftCertificate = giftCertificateDao.read(id);
        assert giftCertificate != null;
        giftCertificate.setName("New name");
        giftCertificate.setDescription("New description");
        giftCertificate.setPrice(BigDecimal.ONE);
        giftCertificate.setDuration(1);
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        giftCertificate.setLastUpdateDate(localDateTime);

        giftCertificateDao.update(giftCertificate);
        GiftCertificate actual = giftCertificateDao.read(id);

        assertAll("GiftCertificates should be equal except last_update_date field",
                () -> {
                    assertNotNull(actual);
                    assertEquals(giftCertificate.getName(), actual.getName());
                    assertEquals(giftCertificate.getDescription(), actual.getDescription());
                    assertEquals(0, giftCertificate.getPrice().compareTo(actual.getPrice()));
                    assertEquals(giftCertificate.getDuration(), actual.getDuration());
                    assertEquals(giftCertificate.getCreateDate(), actual.getCreateDate());
                    assertNotNull(actual.getLastUpdateDate());
                    assertEquals(giftCertificate.getLastUpdateDate(), actual.getLastUpdateDate());
                    assertTrue(actual.getIsActive());
                });
    }

    @Test
    void updateException() {
        GiftCertificate giftCertificate = new GiftCertificate();
        int id = 1;
        giftCertificate.setId(id);

        assertThrows(DaoException.class, () -> giftCertificateDao.update(giftCertificate));
    }

    @Test
    void delete() throws DaoException {
        int id = 2;
        giftCertificateDao.delete(id);
        GiftCertificate actual = giftCertificateDao.read(id);

        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.getIsActive());
        });
    }

    @Test
    void testReadAll() {
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(giftCertificateDao.readAll());
                    assertEquals(3, giftCertificateDao.readAll().size());
                });
    }
}