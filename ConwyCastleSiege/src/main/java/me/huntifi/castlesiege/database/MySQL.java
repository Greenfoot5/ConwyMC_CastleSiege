package me.huntifi.castlesiege.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.huntifi.castlesiege.Main;

public class MySQL {

	private final String host = "mysql.apexhosting.gdn";
	private final String port = "3306";
	private final String database = "apexMC1238982";
	private final String username = "apexMC1238982";
	private final String password = "hyv6t&3IkydZ2nFD%Y$^p7dc";

	private Connection connection;

	public boolean isConnected() {

		return (connection != null);

	}

	public void connect() throws ClassNotFoundException, SQLException {

		if (!isConnected()) {

			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
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


