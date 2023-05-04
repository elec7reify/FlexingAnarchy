package com.flexingstudios.flexinganarchy

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork
import com.flexingstudios.flexinganarchy.AirDrop.AirDrop
import com.flexingstudios.flexinganarchy.AirDrop.protection.AirDropListener
import com.flexingstudios.flexinganarchy.Configuration.Config
import com.flexingstudios.flexinganarchy.Configuration.Function
import com.flexingstudios.flexinganarchy.PvPManager.CombatHandle
import com.flexingstudios.flexinganarchy.commands.AirDropCommand
import com.flexingstudios.flexinganarchy.commands.SpawnCommand
import com.flexingstudios.flexinganarchy.listener.PlayerListener
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

object FlexingAnarchy : JavaPlugin() {
    val instance: FlexingAnarchy = this
    lateinit var scheduledExecutorService: ScheduledExecutorService
    lateinit var score: Scoreboard
    lateinit var config: Config
    lateinit var lobbyLocation: Location
    val handledPlayers = HashMap<Player, CombatHandle>()

    override fun onLoad() {
    }

    override fun onEnable() {
        config = Config(this)
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        lobbyLocation = Location(
            config.getWorld(Function.LOBBY_WORLD),
            config.getDouble(Function.LOC_LOBBY_X),
            config.getDouble(Function.LOC_LOBBY_Y),
            config.getDouble(Function.LOC_LOBBY_Z),
            config.getFloat(Function.LOC_LOBBY_PITCH),
            config.getFloat(Function.LOC_LOBBY_YAW)
        )
        if (FlexingNetwork.isDevelopment()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, { AirDrop.start() }, 1000L)
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, { AirDrop.start() }, 864000L)
        }
        FlexingNetwork.features().CHANGE_CHAT.isEnabled = true
        CombatHandle.enableBar = true
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(Events(), this)
        server.pluginManager.registerEvents(AirDropListener(), this)
        //LobbyProtector.init(Anarchy.getInstance(), Anarchy.getLobbyLocation(), 100);
        getCommand("spawn").executor = SpawnCommand()
        getCommand("airdrop").executor = AirDropCommand()
    }

    override fun onDisable() {
        scheduledExecutorService.shutdownNow()
        AirDrop.getInstance().stop()
    }
}
