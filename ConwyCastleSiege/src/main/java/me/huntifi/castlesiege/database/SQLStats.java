package me.huntifi.castlesiege.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Main;

public class SQLStats {
	public static void createTable() {
		PreparedStatement ps;
		try {
			ps = Main.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS castlesiegestats " 
					+ "(NAME VARCHAR(100),UUID VARCHAR(100),SCORE DOUBLE,KILLS DOUBLE,DEATHS DOUBLE,CAPTURES DOUBLE,ASSISTS DOUBLE"
					+ ",HEALS DOUBLE,SUPPORTS DOUBLE,MVPS INT(100),SECRETS INT(100),LEVEL INT(100),KIT VARCHAR(100),KILLSTREAK INT(100),PRIMARY KEY (NAME))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createPlayer(Player p) {
		if (Main.SQL.getConnection() != null) {
			try {
				UUID uuid = p.getUniqueId();

				if (exists(uuid)) {
					PreparedStatement ps2 = Main.SQL.getConnection().prepareStatement("INSERT IGNORE INTO castlesiegestats"
							+ " (NAME,UUID) VALUES (?,?)");
					ps2.setString(1, p.getName());
					ps2.setString(2, uuid.toString());
					ps2.executeUpdate();
					ClosePreparedStatement(ps2);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static boolean exists(UUID uuid) {
		try {
			PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT * FROM castlesiegestats WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet results = ps.executeQuery();
			return results.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void CloseResultSet(ResultSet s) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (s != null) {
					try {
						s.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 100);
	}

	public static void ClosePreparedStatement(PreparedStatement s) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (s != null) {
					try {
						s.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 100);
	}

	//SCORE
	public static void addScore(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SCORE=? WHERE UUID=?");
				ps.setDouble(1, (getScore(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeScore(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SCORE=? WHERE UUID=?");
				ps.setDouble(1, (getScore(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setScore(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SCORE=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Double getScore(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT SCORE FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("SCORE");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//KILLS
	public static void addKills(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLS=? WHERE UUID=?");
				ps.setDouble(1, (getKills(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeKills(UUID uuid, Double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLS=? WHERE UUID=?");
				ps.setDouble(1, (getKills(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setKills(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLS=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static double getKills(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT KILLS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("KILLS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//DEATHS
	public static void addDeaths(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET DEATHS=? WHERE UUID=?");
				ps.setDouble(1, (getDeaths(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeDeaths(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET DEATHS=? WHERE UUID=?");
				ps.setDouble(1, (getDeaths(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setDeaths(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET DEATHS=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static double getDeaths(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT DEATHS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("DEATHS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//CAPTURES
	public static void addCaptures(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET CAPTURES=? WHERE UUID=?");
				ps.setDouble(1, (getCaptures(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeCaptures(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET CAPTURES=? WHERE UUID=?");
				ps.setDouble(1, (getCaptures(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setCaptures(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET CAPTURES=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Double getCaptures(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT CAPTURES FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("CAPTURES");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//ASSISTS
	public static void addAssists(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET ASSISTS=? WHERE UUID=?");
				ps.setDouble(1, (getAssists(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeAssists(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET ASSISTS=? WHERE UUID=?");
				ps.setDouble(1, (getAssists(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setAssists(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET ASSISTS=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Double getAssists(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT ASSISTS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("ASSISTS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//HEALS
	public static void addHeals(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET HEALS=? WHERE UUID=?");
				ps.setDouble(1, (getHeals(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeHeals(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET HEALS=? WHERE UUID=?");
				ps.setDouble(1, (getHeals(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setHeals(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET HEALS=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static double getHeals(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT HEALS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("HEALS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}

	//SUPPORTS
	public static void addSupports(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SUPPORTS=? WHERE UUID=?");
				ps.setDouble(1, (getSupports(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeSupports(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SUPPORTS=? WHERE UUID=?");
				ps.setDouble(1, (getSupports(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setSupports(UUID uuid, double value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SUPPORTS=? WHERE UUID=?");
				ps.setDouble(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static double getSupports(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT SUPPORTS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				double value;

				if (rs.next()) {
					value = rs.getDouble("SUPPORTS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		return 0.0;
	}

	//SECRETS
	public static void addSecrets(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SECRETS=? WHERE UUID=?");
				ps.setInt(1, (getSecrets(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeSecrets(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SECRETS=? WHERE UUID=?");
				ps.setInt(1, (getSecrets(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setSecrets(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET SECRETS=? WHERE UUID=?");
				ps.setInt(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getSecrets(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT SECRETS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int value;

				if (rs.next()) {
					value = rs.getInt("SECRETS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	//MVPS
	public static void addMvps(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET MVPS=? WHERE UUID=?");
				ps.setInt(1, (getMvps(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeMvps(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET MVPS=? WHERE UUID=?");
				ps.setInt(1, (getMvps(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setMvps(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET MVPS=? WHERE UUID=?");
				ps.setInt(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getMvps(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT MVPS FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int value;

				if (rs.next()) {
					value = rs.getInt("MVPS");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	//LEVEL
	public static void addLevel(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET LEVEL=? WHERE UUID=?");
				ps.setInt(1, (getLevel(uuid) + value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeLevel(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET LEVEL=? WHERE UUID=?");
				ps.setInt(1, (getLevel(uuid) - value));
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setLevel(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET LEVEL=? WHERE UUID=?");
				ps.setInt(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getLevel(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT LEVEL FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int value;

				if (rs.next()) {
					value = rs.getInt("LEVEL");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	//KILLSTREAKS
	public static void addKillstreak(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLSTREAK=? WHERE UUID=?");
				ps.setInt(1, getKillstreak(uuid) + value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeKillstreak(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLSTREAK=? WHERE UUID=?");
				ps.setInt(1, getKillstreak(uuid) - value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setKillstreak(UUID uuid, int value) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KILLSTREAK=? WHERE UUID=?");
				ps.setInt(1, value);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getKillstreak(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT KILLSTREAK FROM castlesiegestats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int value;

				if (rs.next()) {
					value = rs.getInt("KILLSTREAK");
					return value;
				}
				ps.executeUpdate();
				ClosePreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	//KIT
	public static void setKit(UUID uuid, String kit) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KIT=? WHERE UUID=?");
				ps.setString(1, kit);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeKit(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE castlesiegestats SET KIT=? WHERE UUID=?");
				ps.setString(1, "None");
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
				ClosePreparedStatement(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getKit(UUID uuid) {
		String kit = "Swordsman";
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT KIT FROM `castlesiegestats` WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				rs.next();
				kit = rs.getString("KIT");
				CloseResultSet(rs);
				return kit;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Main.SQL.connect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return kit;
	}
}
