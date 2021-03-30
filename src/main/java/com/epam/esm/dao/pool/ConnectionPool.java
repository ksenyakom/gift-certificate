package com.epam.esm.dao.pool;

import com.epam.esm.exception.PersistentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool {
    private static Logger logger = LogManager.getLogger(ConnectionPool.class);
    /**
     * Database properties.
     */
    private Properties properties;
    /**
     * Database url.
     */
    private String databaseUrl;
    /**
     * Max size of pool.
     */
    private int maxSize;
    /**
     * Timeout to check if connection is valid.
     */
    private int checkConnectionTimeout;

    /**
     * Collection of free connections.
     */
    private BlockingQueue<PooledConnection> freeConnections = new LinkedBlockingQueue<>();
    /**
     * Collection of used connections.
     */
    private Set<PooledConnection> usedConnections = new ConcurrentSkipListSet<>();

    private static final ReentrantLock lock = new ReentrantLock();

    private ConnectionPool() {
    }

    /**
     * Takes connection from pool,
     * if there is no free connections, then creates another one
     * if total number of connections is less then maxSize.
     *
     * @return connection from pool
     * @throws PersistentException - if number of connectrions is maxSize
     *                             or it is impossible to connect to a database.
     */
    public Connection getConnection() throws PersistentException {
        PooledConnection connection = null;
        while (connection == null) {
            try {
                if (!freeConnections.isEmpty()) {
                    connection = freeConnections.take();
                    if (!connection.isValid(checkConnectionTimeout)) {
                        try {
                            connection.getConnection().close();
                        } catch (SQLException e) {
                            logger.error("Unable to close invalid connection", e);
                        }
                        connection = null;
                    }
                } else if (usedConnections.size() < maxSize) {
                    connection = createConnection();
                } else {
                    logger.error("The limit of number of database connections is exceeded");
                    throw new PersistentException();
                }
            } catch (InterruptedException | SQLException e) {
                logger.error("It is impossible to connect to a database", e);
                throw new PersistentException(e);
            }
        }

        usedConnections.add(connection);
        logger.debug("Connection was received from pool. Current pool size:{} used connections; {} free connection", usedConnections.size(), freeConnections.size());
        return connection;
    }

    void freeConnection(PooledConnection connection) {
        try {
            if (connection.isValid(checkConnectionTimeout)) {
                connection.clearWarnings();
                connection.setAutoCommit(true);
                usedConnections.remove(connection);
                freeConnections.put(connection);
                logger.debug("Connection was returned into pool. Current pool size: {} used connections; {} free connection", usedConnections.size(), freeConnections.size());
            }
        } catch (SQLException | InterruptedException e1) {
            logger.warn("It is impossible to return by.ksu.database connection into pool", e1);
            try {
                connection.getConnection().close();
            } catch (SQLException e2) {
                logger.error("Unable to close connection", e2);

            }
        }
    }

    /**
     * Initializes pool of connections to database, using parameters.
     * All connections are put to collection freeConnections.
     *
     * @param properties             - properties for connection.
     * @param startSize              - start size of pool.
     * @param maxSize                - max size of pool.
     * @param checkConnectionTimeout - timeout to check if connection is valid.
     * @throws PersistentException - if is impossible to initialize connection pool.
     */
    public void init(Properties properties, int startSize, int maxSize, int checkConnectionTimeout) throws PersistentException {
        try {
            destroy();
            String driverName = (String) properties.get("driver");
            Class.forName(driverName);
            this.properties = properties;
            this.databaseUrl = (String) properties.get("db.url");
            this.maxSize = maxSize;
            this.checkConnectionTimeout = checkConnectionTimeout;
            for (int counter = 0; counter < startSize; counter++) {
                freeConnections.put(createConnection());
            }
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            logger.fatal("It is impossible to initialize connection pool", e);
            throw new PersistentException(e);
        }
    }

    private static ConnectionPool instance;

    public static ConnectionPool getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new ConnectionPool();
            }
            return instance;
        } finally {
            lock.unlock();
        }
    }


    private PooledConnection createConnection() throws SQLException {
        return new PooledConnection(DriverManager.getConnection(databaseUrl, properties));
    }

    public void destroy() {
        usedConnections.addAll(freeConnections);
        freeConnections.clear();
        for (PooledConnection connection : usedConnections) {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                logger.error("Unable to close connection", e);
            }
        }
        usedConnections.clear();
    }

}
