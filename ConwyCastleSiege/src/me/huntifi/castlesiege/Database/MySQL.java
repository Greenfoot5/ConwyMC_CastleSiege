package me.huntifi.castlesiege.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

public class MySQL {

	private String host = "mysql.apexhosting.gdn";
	private String port = "3306";
	private String database = "apexMC1238982";
	private String username = "apexMC1238982";
	private String password = "hyv6t&3IkydZ2nFD%Y$^p7dc";

	private Connection connection;

	public boolean isConnected() {

		return (connection == null ? false : true);

	}

	public void connect() throws ClassNotFoundException, SQLException {

		if (!isConnected()) {

			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
			Bukkit.getConsoleSender().sendMessage("Succesfully connected to the ConwyMC MySQL Database! ");

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
				Bukkit.getConsoleSender().sendMessage("Succesfully reconnected to the ConwyMC MySQL Database! ");
			} 
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}

		return connection;
	}

}


