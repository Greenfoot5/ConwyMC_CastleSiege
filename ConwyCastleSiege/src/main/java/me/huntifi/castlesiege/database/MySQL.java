package me.huntifi.castlesiege.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.huntifi.castlesiege.Main;

public class MySQL {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;

	public MySQL(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	private Connection connection;

	public boolean isConnected() {
		return (connection != null);
	}

	public void connect() throws ClassNotFoundException, SQLException {

		if (!isConnected()) {

			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true", host, port, database), username, password);
			Main.instance.getLogger().info("Successfully connected to the ConwyMC MySQL Database!");

		}
	}

	public void disconnect() {

		if (isConnected()) {

			try {

				connection.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}


	}

	public Connection getConnection() {

		try {
			
			if (connection == null || connection.isClosed()) {
				
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
				Main.instance.getLogger().info("Successfully reconnected to the ConwyMC MySQL Database!");
			} 
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}

		return connection;
	}

}


