/*
Votifier-Addon (mc-ita) by Potassio
questo è un semplice plugin che sfrutta votifier (o meglio NuVotifier) per i voti provenienti da mc italia
*/

package dev.potassio.vaddon;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class votifierAddon extends JavaPlugin{

    private static votifierAddon instance;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("____   ____       __   .__   _____ .__                                           .__   __            \n" +
                "\\   \\ /   /____ _/  |_ |__|_/ ____\\|__|  ____ _______       _____    ____        |__|_/  |_ _____    \n" +
                " \\   Y   //  _ \\\\   __\\|  |\\   __\\ |  |_/ __ \\\\_  __ \\     /     \\ _/ ___\\ ______|  |\\   __\\\\__  \\   \n" +
                "  \\     /(  <_> )|  |  |  | |  |   |  |\\  ___/ |  | \\/    |  Y Y  \\\\  \\___/_____/|  | |  |   / __ \\_ \n" +
                "   \\___/  \\____/ |__|  |__| |__|   |__| \\___  >|__|       |__|_|  / \\___  >      |__| |__|  (____  / \n" +
                "                                            \\/                  \\/      \\/                       \\/  ");
        this.saveDefaultConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                DateFormat df = new SimpleDateFormat("HH.mm");
                String date = df.format(new Date());
                if (date.equalsIgnoreCase("00.10")) {
                    try {
                        for (String s : getUsers()) {
                            Bukkit.getScheduler().runTask(instance, () -> triggerVoteEvent(s));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.runTaskTimerAsynchronously(this, 0, 20 * 60);
    }

    @Override
    public void onDisable() {

    }

    public void triggerVoteEvent(String username) {
        Vote vote = new Vote();
        vote.setUsername(username);
        vote.setServiceName("Minecraft-Italia");
        vote.setAddress("<3");
        Bukkit.getPluginManager().callEvent(new VotifierEvent(vote));
    }

    public List<String> getUsers() throws IOException {
        ArrayList<String> users = new ArrayList<>();
        JSONObject json = readJsonFromUrl(getConfig().getString("apiUrl"));
        JSONArray usersArray = json.getJSONArray("userVotes");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (!user.get("minecraftNickname").toString().equals("null")) {
                users.add(user.get("minecraftNickname").toString());
            }
        }
        return users;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}
