package com.flexingstudios.anarchy;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Lobby;
import com.flexingstudios.FlexingNetwork.api.score.Record;
import com.flexingstudios.FlexingNetwork.api.score.SideScoreboard;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Scoreboard {
    private final Anarchy plugin;
    public SideScoreboard scoreboard;
    public Player player;
    private Record date;

    public Scoreboard(Anarchy plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        scoreboard = new SideScoreboard(Utilities.colored("&c&lАНАРХИЯ"));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            scoreboard.reset();
            scoreboard.create(Utilities.colored("&7%date% &8%server%"
                    .replace("%date%", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())))
                    .replace("%server%", FlexingNetwork.lobby().getServerId())), 4).update();
            scoreboard.create("", 3).update();
            scoreboard.create("Ник: " + player.getName(), 2).update();
            scoreboard.create(Utilities.colored("&3&lFlexingWorld.net"), 1).update();
        }, 20L, 20L);
    }
}
