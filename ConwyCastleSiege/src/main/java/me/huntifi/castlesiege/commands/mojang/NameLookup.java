package me.huntifi.castlesiege.commands.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class NameLookup {

    /**
     * The URL from Mojang API that provides the JSON String in response.
     */
    private static final String LOOKUP_URL = "https://api.mojang.com/user/profiles/%s/names";

    /**
     * The URL from Mojang API to resolve the UUID of a player from their name.
     */
    private static final String GET_UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?t=0";
    private static final Gson JSON_PARSER = new Gson();

    /**
     * <h1>NOTE: Avoid running this method <i>Synchronously</i> with the main thread!It blocks while attempting to get a response from Mojang servers!</h1>
     * @param player The UUID of the player to be looked up.
     * @return Returns an array of {@link PreviousPlayerNameEntry} objects, or null if the response couldn't be interpreted.
     * @throws IOException {@link #getPlayerPreviousNames(String)}
     */
    public static PreviousPlayerNameEntry[] getPlayerPreviousNames(UUID player) throws IOException {
        return getPlayerPreviousNames(player.toString());
    }

    /**
     * <h1>NOTE: Avoid running this method <i>Synchronously</i> with the main thread! It blocks while attempting to get a response from Mojang servers!</h1>
     * Alternative method accepting an 'OfflinePlayer' (and therefore 'Player') objects as parameter.
     * @param player The OfflinePlayer object to obtain the UUID from.
     * @return Returns an array of {@link PreviousPlayerNameEntry} objects, or null if the response couldn't be interpreted.
     * @throws IOException {@link #getPlayerPreviousNames(UUID)}
     */
    public static PreviousPlayerNameEntry[] getPlayerPreviousNames(OfflinePlayer player) throws IOException {
        return getPlayerPreviousNames(player.getUniqueId());
    }

    /**
     * <h1>NOTE: Avoid running this method <i>Synchronously</i> with the main thread! It blocks while attempting to get a response from Mojang servers!</h1>
     * Alternative method accepting an {@link OfflinePlayer} (and therefore {@link Player}) objects as parameter.
     * @param uuid The UUID String to lookup
     * @return Returns an array of {@link PreviousPlayerNameEntry} objects, or null if the response couldn't be interpreted.
     * @throws IOException
     */
    public static PreviousPlayerNameEntry[] getPlayerPreviousNames(String uuid) throws IOException {
        if (uuid == null || uuid.isEmpty())
            return null;
        String response = getRawJsonResponse(new URL(String.format(LOOKUP_URL, uuid)));
        PreviousPlayerNameEntry[] names = JSON_PARSER.fromJson(response, PreviousPlayerNameEntry[].class);
        return names;
    }

    /**
     * If you don't have the UUID of a player, this method will resolve it for you.<br>
     * The output of this method may be used directly with {@link #getPlayerPreviousNames(String)}.<br>
     * <b>NOTE: as with the rest, this method opens a connection with a remote server, so running it synchronously will block the main thread which will lead to server lag.</b>
     * @param name The name of the player to lookup.
     * @return A String which represents the player's UUID. <b>Note: the uuid cannot be parsed to a UUID object directly, as it doesnt contain dashes. This feature will be implemented later</b>
     * @throws IOException Inherited by {@link BufferedReader#readLine()}, {@link BufferedReader#close()}, {@link URL}, {@link HttpURLConnection#getInputStream()}
     */
    public static String getPlayerUUID(String name) throws IOException {
        String response = getRawJsonResponse(new URL(String.format(GET_UUID_URL, name)));
        JsonObject o = JSON_PARSER.fromJson(response, JsonObject.class);
        if (o == null)
            return null;
        return o.get("id") == null ? null : o.get("id").toString();
    }

    /**
     * This is a helper method used to read the response of Mojang's API webservers.
     * @param u the URL to connect to
     * @return a String with the data read.
     * @throws IOException Inherited by {@link BufferedReader#readLine()}, {@link BufferedReader#close()}, {@link URL}, {@link HttpURLConnection#getInputStream()}
     */
    private static String getRawJsonResponse(URL u) throws IOException {
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setDoInput(true);
        con.setConnectTimeout(2000);
        con.setReadTimeout(2000);
        con.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.readLine();
        in.close();
        return response;
    }
}
