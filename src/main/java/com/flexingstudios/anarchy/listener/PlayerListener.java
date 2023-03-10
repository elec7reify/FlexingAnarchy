package com.flexingstudios.anarchy.listener;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import com.flexingstudios.anarchy.AirDrop.AirDrop;
import com.flexingstudios.anarchy.Anarchy;
import com.flexingstudios.anarchy.Configuration.Messages;
import com.flexingstudios.anarchy.PvPManager.CombatHandle;
import com.flexingstudios.anarchy.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Anarchy.handledPlayers.put(player, new CombatHandle(player, Anarchy.getInstance()));
        Anarchy.getInstance().score = new Scoreboard(Anarchy.getInstance(), player);
        Anarchy.getInstance().score.scoreboard.bind(player);
        if (!player.hasPlayedBefore()) {
            player.teleport(Anarchy.getLobbyLocation());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        FlexPlayer flPlayer = FlexPlayer.get(player);
        if (!flPlayer.has(Rank.VADMIN) && Anarchy.handledPlayers.get(player).isInCombat()) {
            player.setHealth(0.0);
            for (Player players : Bukkit.getOnlinePlayers())
                Utilities.msg(players, Messages.PVP_LEAVE_BROADCAST.replace("{player}", players.getName()));
        }

        if (Anarchy.handledPlayers.containsKey(player)) {
            Anarchy.handledPlayers.get(player).cleanUp();
            Anarchy.handledPlayers.remove(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final CombatHandle combatHandle = Anarchy.handledPlayers.get(event.getEntity().getPlayer());
        if (combatHandle.isInCombat()){
            combatHandle.reset();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.teleport(Anarchy.getLobbyLocation());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (LobbyProtector.getWorld() == player.getWorld() && LobbyProtector.isExactNearLobby(player)) {
            player.teleport(Anarchy.getLobbyLocation());
            Utilities.msg(player, "&6Вы пересекли границу спавна, и поэтому были телепортированы на точку спавна.");
        }
    }

}
