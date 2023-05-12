package com.flexingstudios.flexinganarchy.listener

import com.flexingstudios.Common.player.Rank
import com.flexingstudios.FlexingNetwork.api.event.PlayerLeaveEvent
import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector
import com.flexingstudios.FlexingNetwork.api.util.Utilities
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer
import com.flexingstudios.flexinganarchy.FlexingAnarchy
import com.flexingstudios.flexinganarchy.Configuration.Messages
import com.flexingstudios.flexinganarchy.PvPManager.CombatHandle
import com.flexingstudios.flexinganarchy.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        FlexingAnarchy.instance.handledPlayers[player] = CombatHandle(player, FlexingAnarchy.instance)
        FlexingAnarchy.instance.score = Scoreboard(FlexingAnarchy.instance, player)
        FlexingAnarchy.instance.score.scoreboard.bind(player)
        if (!player.hasPlayedBefore()) {
            player.teleport(FlexingAnarchy.instance.lobbyLocation)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        val player = event.player
        val flPlayer = FlexPlayer.get(player)
        if (FlexingAnarchy.instance.handledPlayers.containsKey(player)) {
            FlexingAnarchy.instance.handledPlayers[player]!!.cleanUp()
            FlexingAnarchy.instance.handledPlayers.remove(player)
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val combatHandle = FlexingAnarchy.instance.handledPlayers[event.entity.player]
        if (combatHandle!!.isInCombat) {
            combatHandle.reset()
        }
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        player.teleport(FlexingAnarchy.instance.lobbyLocation)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (LobbyProtector.getWorld() == player.world && LobbyProtector.isExactNearLobby(player)) {
            player.teleport(FlexingAnarchy.instance.lobbyLocation)
            Utilities.msg(player, "&6Вы пересекли границу спавна, и поэтому были телепортированы на точку спавна.")
        }
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerLeaveEvent) {
        if (event.isKick) {
            if (FlexingAnarchy.instance.handledPlayers[event.player]!!.isInCombat) {
                if (!event.networkPlayer.has(Rank.ADMIN)) {
                    event.player.health = 0.0
                    for (players in Bukkit.getOnlinePlayers()) Utilities.msg(
                        players,
                        Messages.PVP_LEAVE_BROADCAST.replace("{player}", players.name)
                    )
                }
            }
        }
    }
}
