package com.flexingstudios.flexinganarchy;

import com.flexingstudios.FlexingNetwork.api.util.LobbyProtector;
import com.flexingstudios.flexinganarchy.Configuration.Function;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
                FlexingAnarchy.INSTANCE.getHandledPlayers().get(player).startCombat();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player && isSubject(event.getEntity().getType())) {
                Player damager = (Player) (((Projectile) event.getDamager()).getShooter());

                if (damager != null && !damager.hasPermission("antirelog.bypass"))
                    FlexingAnarchy.INSTANCE.getHandledPlayers().get(damager).startCombat();
            }
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("antirelog.bypass")) {
                FlexingAnarchy.INSTANCE.getHandledPlayers().get(player).startCombat();
            }
        }

    }

    @EventHandler
    public void onWorldInitialising(WorldLoadEvent event) {
        if (event.getWorld().getName().equals(Function.LOBBY_WORLD)) {
            LobbyProtector.init(FlexingAnarchy.INSTANCE.getInstance(), FlexingAnarchy.INSTANCE.getLobbyLocation(), 100);
            Logger.getGlobal().info("succesful");
        }
    }

    private boolean isSubject(EntityType entity) {
        for (String s : FlexingAnarchy.config.getStringList(Function.PVP_MANAGER_ACTIVATION_SUBJECTS)) {
            if (s.toUpperCase().equals(entity.name())) return true;
        }

        return false;
    }
}