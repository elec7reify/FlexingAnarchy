package com.flexingstudios.flexinganarchy;

import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector;
import com.flexingstudios.flexinganarchy.Configuration.Function;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.logging.Logger;

public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCombat(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        if (event.getDamager() instanceof Player && isSubject(event.getEntity().getType())) {
            Player player = (Player) event.getDamager();
            if (!player.hasPermission("antirelog.bypass"))
                FlexingAnarchy.Companion.getInstance().getHandledPlayers().get(player).startCombat();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player && isSubject(event.getEntity().getType())) {
                Player damager = (Player) (((Projectile) event.getDamager()).getShooter());

                if (damager != null && !damager.hasPermission("antirelog.bypass"))
                    FlexingAnarchy.Companion.getInstance().getHandledPlayers().get(damager).startCombat();
            }
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("antirelog.bypass")) {
                FlexingAnarchy.Companion.getInstance().getHandledPlayers().get(player).startCombat();
            }
        }

    }

    @EventHandler
    public void onWorldInitialising(WorldInitEvent event) {
        if (event.getWorld().getName().equals(FlexingAnarchy.Companion.getInstance().config.getWorld(Function.LOBBY_WORLD))) {
            LobbyProtector.init(FlexingAnarchy.Companion.getInstance(), FlexingAnarchy.Companion.getInstance().getLobbyLocation(), 100);
        }
    }

    private boolean isSubject(EntityType entity) {
        for (String s : FlexingAnarchy.Companion.getInstance().config.getStringList(Function.PVP_MANAGER_ACTIVATION_SUBJECTS)) {
            if (s.toUpperCase().equals(entity.name())) return true;
        }

        return false;
    }
}
