package com.flexingstudios.flexinganarchy.listener

import com.flexingstudios.Common.player.Rank
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
        FlexingAnarchy.handledPlayers[player] = CombatHandle(player, FlexingAnarchy.instance)
        FlexingAnarchy.score = Scoreboard(FlexingAnarchy.instance, player)
        FlexingAnarchy.score.scoreboard.bind(player)
        if (!player.hasPlayedBefore()) {
            player.teleport(FlexingAnarchy.lobbyLocation)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        val player = event.player
        val flPlayer = FlexPlayer.get(player)
        if (!flPlayer.has(Rank.VADMIN) && FlexingAnarchy.handledPlayers[player]!!.isInCombat) {
            player.health = 0.0
            for (players in Bukkit.getOnlinePlayers()) Utilities.msg(
                players,
                Messages.PVP_LEAVE_BROADCAST.replace("{player}", players.name)
            )
        }
        if (FlexingAnarchy.handledPlayers.containsKey(player)) {
            FlexingAnarchy.handledPlayers[player]!!.cleanUp()
            FlexingAnarchy.handledPlayers.remove(player)
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val combatHandle = FlexingAnarchy.handledPlayers[event.entity.player]
        if (combatHandle!!.isInCombat) {
            combatHandle.reset()
        }
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        player.teleport(FlexingAnarchy.lobbyLocation)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (LobbyProtector.getWorld() == player.world && LobbyProtector.isExactNearLobby(player)) {
            player.teleport(FlexingAnarchy.lobbyLocation)
            Utilities.msg(player, "&6Вы пересекли границу спавна, и поэтому были телепортированы на точку спавна.")
        }
    }
}
