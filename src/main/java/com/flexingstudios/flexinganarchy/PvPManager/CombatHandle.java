package com.flexingstudios.flexinganarchy.PvPManager;

import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.flexinganarchy.FlexingAnarchy;
import com.flexingstudios.flexinganarchy.Configuration.Function;
import com.flexingstudios.flexinganarchy.Configuration.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatHandle {
    public static boolean enableBar;
    private int combatTimeLeft;
    private final int combatTimeOut = FlexingAnarchy.Companion.getInstance().config.getInt(Function.PVP_MANAGER_COMBAT_DURATION);
    private final int vanishTimeOut = FlexingAnarchy.Companion.getInstance().config.getInt("vanish-timeout");
    private final String busyChat;
    private final String freeChat;
    private BossBar busyBar, freeBar;
    private final Player player;
    private final JavaPlugin plugin;
    private boolean inCombat;
    private int combatTickTask = -1;

    public CombatHandle(Player player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
        busyChat = Utilities.Companion.colored(Messages.PVP_BUSY_MESSAGE)
                .replaceAll("\\{displayname}", player.getDisplayName())
                .replaceAll("\\{username}", player.getName())
                .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                .replaceAll("\\{timeout}", String.valueOf(combatTimeOut));
        freeChat = Utilities.Companion.colored(Messages.PVP_FREE_MESSAGE)
                .replaceAll("\\{displayname}", player.getDisplayName())
                .replaceAll("\\{username}", player.getName())
                .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                .replaceAll("\\{timeout}", String.valueOf(combatTimeOut));

        if (enableBar) {
            busyBar = Bukkit.createBossBar(Utilities.Companion.colored(Messages.PVP_BUSY_MESSAGE)
                            .replaceAll("\\{displayname}", player.getDisplayName())
                            .replaceAll("\\{username}", player.getName())
                            .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                            .replaceAll("\\{timeout}", String.valueOf(combatTimeOut)),
                    BarColor.RED,
                    BarStyle.SEGMENTED_6);
            busyBar.addPlayer(player);
            busyBar.setVisible(false);

            freeBar = Bukkit.createBossBar(Utilities.Companion.colored(Messages.PVP_FREE_MESSAGE)
                    .replaceAll("\\{displayname}", player.getDisplayName())
                    .replaceAll("\\{username}", player.getName())
                    .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                    .replaceAll("\\{timeout}", String.valueOf(combatTimeOut)),
                    BarColor.GREEN,
                    BarStyle.SOLID);
            freeBar.addPlayer(player);
            freeBar.setVisible(false);
        }

        inCombat = false;
    }

    private String formatCombatChatMessage(String message, Player player) {
        return message
                .replaceAll("\\{displayname}", player.getDisplayName())
                .replaceAll("\\{username}", player.getName())
                .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                .replaceAll("\\{timeout}", String.valueOf(combatTimeOut));
    }


    public boolean isInCombat() {
        return inCombat;
        //return combatTimeLeft != 0;
    }

    public void startCombat() {
        if (enableBar) {
            busyBar.setTitle(Utilities.Companion.colored(Messages.PVP_BUSY_MESSAGE)
                    .replaceAll("\\{displayname}", player.getDisplayName())
                    .replaceAll("\\{username}", player.getName())
                    .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                    .replaceAll("\\{timeout}", String.valueOf(combatTimeOut)));
            busyBar.setVisible(true);
            busyBar.setProgress(1d);
            freeBar.setVisible(false);
        }
        if (!busyChat.isEmpty() && !inCombat)
            player.sendMessage(busyChat);
        combatTimeLeft = 30;
        if (combatTickTask != -1)
            plugin.getServer().getScheduler().cancelTask(combatTickTask);

        //player.sendTitle(Language.getMsg(player, Messages.PVP_PREFIX), Language.getMsg(player, Messages.PVP_ON), 20, 20, 20);

        combatTickTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (combatTimeLeft == 0) {
                CombatHandle.this.endCombat();
                return;
            }
            player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.C));
            if (enableBar) {
                busyBar.setProgress((double) combatTimeLeft / 30);
                // Update time in message
                busyBar.setTitle(Utilities.Companion.colored(Messages.PVP_BUSY_MESSAGE)
                        .replaceAll("\\{displayname}", player.getDisplayName())
                        .replaceAll("\\{username}", player.getName())
                        .replaceAll("\\{timeleft}", String.valueOf(combatTimeLeft))
                        .replaceAll("\\{timeout}", String.valueOf(combatTimeOut)));
            }

            combatTimeLeft--;
        }, 0, 20);
        //combatTickTask.timedRun(true);
        inCombat = true;
    }

    public void endCombat() {
        if (enableBar) {
            busyBar.setVisible(false);

            freeBar.setVisible(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> freeBar.setVisible(false), vanishTimeOut * 20L);
        }
        plugin.getServer().getScheduler().cancelTask(combatTickTask);
        combatTickTask = -1;
        player.sendTitle(Utilities.Companion.colored(Messages.PVP_PREFIX), Utilities.Companion.colored(Messages.PVP_OFF), 20, 20, 20);
        player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
        if (!freeChat.isEmpty())
            player.sendMessage(freeChat);

        inCombat = false;
    }

    // In case, if player re-joins
    public void cleanUp() {
        combatTickTask = -1;
        if (enableBar) {
            busyBar.removeAll();
            freeBar.removeAll();
        }
    }
    public void reset(){
        endCombat();
        combatTimeLeft = 0;
    }
}
