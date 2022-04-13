package me.huntifi.castlesiege.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Main;

public class SQLGetter {

	public static void createTable() {

		PreparedStatement ps;
		try {
			ps = Main.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerstats " 
					+ "(NAME VARCHAR(100),UUID VARCHAR(100),RANK VARCHAR(100),STAFFRANK VARCHAR(100),RANKPOINTS DOUBLE,COINS INT(100),PLAYERTIME VARCHAR(100),JOINDATE VARCHAR(100),VOTES VARCHAR(100),PRIMARY KEY (NAME))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createPlayer(Player p) {

		if (Main.SQL.getConnection() != null) {
			try {
				UUID uuid = p.getUniqueId();

				if (!exists(uuid)) {
					PreparedStatement ps2 = Main.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerstats"
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
			PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT * FROM playerstats WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet results = ps.executeQuery();
			return results.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
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

	public static void addCoins(UUID uuid, int coins) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET COINS=? WHERE UUID=?");
				ps.setInt(1, (getCoins(uuid) + coins));
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

	public static void removeCoins(UUID uuid, int coins) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET COINS=? WHERE UUID=?");
				ps.setInt(1, (getCoins(uuid) - coins));
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

	public static void setCoins(UUID uuid, int coins) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET COINS=? WHERE UUID=?");
				ps.setInt(1, coins);
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


	public static int getCoins(UUID uuid) {

		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT COINS FROM playerstats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int coins;
				if (rs.next()) {
					coins = rs.getInt("COINS");
					CloseResultSet(rs);
					return coins;
				}
				ps.executeUpdate();
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


	public static void setRank(UUID uuid, String rank) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET RANK=? WHERE UUID=?");
				ps.setString(1, rank);
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

	public static void removeRank(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET RANK=? WHERE UUID=?");
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

	public static String getRank(UUID uuid) {
		String rank = "";
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT RANK FROM `playerstats` WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				rs.next();

				rank = rs.getString("RANK");
				CloseResultSet(rs);
				return rank;
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
		return rank;
	}


	public static void setStaffRank(UUID uuid, String rank) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET STAFFRANK=? WHERE UUID=?");
				ps.setString(1, rank);
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

	public static void removeStaffRank(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET STAFFRANK=? WHERE UUID=?");
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

	public static String getStaffRank(UUID uuid) {
		String Staffrank = "";
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT STAFFRANK FROM `playerstats` WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					Staffrank = rs.getString("STAFFRANK");
					CloseResultSet(rs);
					return Staffrank;
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
		return Staffrank;
	}


	public static void removeRankpoints(UUID uuid, int coins) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET RANKPOINTS=? WHERE UUID=?");
				ps.setInt(1, (getRankpoints(uuid) - coins));
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

	public static void setRankpoints(UUID uuid, int coins) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET RANKPOINTS=? WHERE UUID=?");
				ps.setInt(1, coins);
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

	public static int getRankpoints(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT RANKPOINTS FROM playerstats WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				int coins;

				if (rs.next()) {
					coins = rs.getInt("RANKPOINTS");
					CloseResultSet(rs);
					return coins;
				}
				ps.executeUpdate();
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
	
	public static void setVotes(UUID uuid, String vote) {

		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET VOTES=? WHERE UUID=?");
				ps.setString(1, vote);
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

	public static void removeVotes(UUID uuid) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET VOTES=? WHERE UUID=?");
				ps.setString(1, "NULL");
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
	
	public static void removeVote(UUID uuid, String vote) {
		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("UPDATE playerstats SET VOTES=? WHERE UUID=?");
				ps.setString(1, getVotes(uuid).replaceAll(vote, ""));
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

	public static String getVotes(UUID uuid) {
		String Votes = "";

		if (Main.SQL.getConnection() != null) {
			try {
				PreparedStatement ps = Main.SQL.getConnection().prepareStatement("SELECT VOTES FROM `playerstats` WHERE UUID=?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					Votes = rs.getString("VOTES");
					CloseResultSet(rs);
					return Votes;
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
		return Votes;
	}
}


