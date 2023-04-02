package com.flexingstudios.anarchy;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector;
import com.flexingstudios.anarchy.AirDrop.AirDrop;
import com.flexingstudios.anarchy.AirDrop.AirDropUtil;
import com.flexingstudios.anarchy.AirDrop.loot.generators.StandardLootGenerator;
import com.flexingstudios.anarchy.Configuration.Config;
import com.flexingstudios.anarchy.Configuration.Function;
import com.flexingstudios.anarchy.PvPManager.CombatHandle;
import com.flexingstudios.anarchy.commands.AirDropCommand;
import com.flexingstudios.anarchy.commands.SpawnCommand;
import com.flexingstudios.anarchy.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Anarchy extends JavaPlugin {
    public static Anarchy instance;
    public static Config config;
    public ScheduledExecutorService scheduledExecutorService;
    private static Location lobbyLocation;

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

        Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), AirDrop::start, 24000L);
        FlexingNetwork.features().CHANGE_CHAT.setEnabled(true);

        CombatHandle.enableBar = true;
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        LobbyProtector.init(Anarchy.getInstance(), Anarchy.getLobbyLocation(), 100);

        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("airdrop").setExecutor(new AirDropCommand());
    }

    @Override
    public void onDisable() {
        scheduledExecutorService.shutdownNow();

        AirDrop.getInstance().stop();
    }

    public static Location getLobbyLocation() {
        return lobbyLocation;
    }

    public static Anarchy getInstance() {
        return instance;
    }
}
