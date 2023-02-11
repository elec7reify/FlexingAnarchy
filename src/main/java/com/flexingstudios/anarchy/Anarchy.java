package com.flexingstudios.anarchy;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector;
import com.flexingstudios.anarchy.Configuration.Config;
import com.flexingstudios.anarchy.Configuration.Function;
import com.flexingstudios.anarchy.PvPManager.CombatHandle;
import com.flexingstudios.anarchy.commands.SpawnCommand;
import com.flexingstudios.anarchy.listener.PlayerListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Anarchy extends JavaPlugin {
    public static Anarchy instance;
    public static Config config;
    public ScheduledExecutorService scheduledExecutorService;
    public static Location lobbyLocation;

    public static final HashMap<Player, CombatHandle> handledPlayers = new HashMap<>();
    public Scoreboard score;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        config = new Config(this);

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        lobbyLocation = new Location(
                Anarchy.config.getWorld(Function.LOBBY_WORLD),
                Anarchy.config.getDouble(Function.LOC_LOBBY_X),
                Anarchy.config.getDouble(Function.LOC_LOBBY_Y),
                Anarchy.config.getDouble(Function.LOC_LOBBY_Z),
                Anarchy.config.getFloat(Function.LOC_LOBBY_PITCH),
                Anarchy.config.getFloat(Function.LOC_LOBBY_YAW));

        FlexingNetwork.features().CHANGE_CHAT.setEnabled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                LobbyProtector.init(Anarchy.getInstance(), lobbyLocation, 75);
            }
        }.runTaskLater(this, 200);
        CombatHandle.enableBar = true;
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);

        getCommand("spawn").setExecutor(new SpawnCommand());
    }

    @Override
    public void onDisable() {
        scheduledExecutorService.shutdownNow();
    }

    public static Anarchy getInstance() {
        return instance;
    }
}
