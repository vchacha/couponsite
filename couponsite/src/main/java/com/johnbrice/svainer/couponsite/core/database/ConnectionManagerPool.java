package com.johnbrice.svainer.couponsite.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;

/**
 * Singleton Class with fixed number of connection to database Signing into
 * database with a user name and password If no connection are available, the
 * system does not allow additional connection
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 * 
 */

public class ConnectionManagerPool implements ConnectionManager {

	private static final int CONNECTIONS_NUMBER = 1;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/test1?autoReconnect=true&useSSL=false&verifyServerCertificate=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	private static ConnectionManager connectionManager = null;
	private static Connection[] connections = new Connection[CONNECTIONS_NUMBER];

	private ConnectionManagerPool() {
	}

	/**
	 * returns instance of {@code ConnectionManagerPool} Promises to return same
	 * instance for each call.
	 * 
	 * @throws DAOException
	 *             if cannot establish connection
	 * @return instance of {@code ConnectionManager}
	 */
	public static ConnectionManager getInstance() {
		if (connectionManager == null) {
			try {
				for (int i = 0; i < CONNECTIONS_NUMBER; i++) {
					Class.forName("com.mysql.jdbc.Driver");
					connections[i] = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
				}
				connectionManager = new ConnectionManagerPool();
			} catch (SQLException | ClassNotFoundException e) {
				throw new DAOException("cannot establish connection");
			}
		}
		return connectionManager;
	}

	/**
	 * returns connection
	 * 
	 * @return connection
	 */
	@Override
	public Connection returnConnection() {
		return connections[CONNECTIONS_NUMBER - 1];
	}

	/**
	 * close all connections
	 * 
	 * @throws DAOException
	 *             if cannot close connection
	 */
	@Override
	public void closeAllConnection() {
		try {
			for (int i = 0; i < CONNECTIONS_NUMBER; i++) {
				if (connections[i] != null) {
					connections[i].close();
				}
			}
		} catch (SQLException e) {
			throw new DAOException("cannot close connection");
		}
	}

	/**
	 * if no connection are available, the method lock connection
	 * 
	 */
	@Override
	public void lockConnection() {

	}

}
