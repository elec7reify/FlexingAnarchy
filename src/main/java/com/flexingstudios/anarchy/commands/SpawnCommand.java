package com.flexingstudios.anarchy.commands;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.anarchy.Anarchy;
import com.flexingstudios.anarchy.PvPManager.CombatHandle;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnCommand implements CommandExecutor {
    public static ScheduledExecutorService executor;
    public static Player player;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String @NotNull [] args) {
        Player sender = (Player) commandSender;
        player = sender;

        if (args.length == 0) {
            if (FlexingNetwork.hasRank(sender, Rank.TEAM, false)) {
                doTeleport();
                return true;
            }
            runScheduledTeleportation(sender);
        }

        if (args.length == 1) {
            String target = args[0];
            Player targetPlayer = Bukkit.getPlayerExact(args[0]);
            CombatHandle combatHandle = Anarchy.handledPlayers.get(targetPlayer);

            if (targetPlayer == sender) {
                if (FlexingNetwork.hasRank(sender, Rank.TEAM, false)) {
                    doTeleport();
                    return true;
                }
                runScheduledTeleportation(sender);
            } else if (FlexingNetwork.hasRank(sender, Rank.TEAM, true)) {
                if (targetPlayer == null) {
                    Utilities.msg(sender, "&cИгрок " + target + " не найден!");
                } else {
                    if (combatHandle.isInCombat() && !FlexingNetwork.hasRank(sender, Rank.VADMIN, true)) {
                        Utilities.msg(sender, "&cИгрок " + target + " находится в PvP режиме и не может быть телепортирован на точку спавна.");
                        return true;
                    }
                    targetPlayer.teleport(Anarchy.lobbyLocation);
                    Utilities.msg(targetPlayer, "&fВы были телепортированы на точку спавна игроком &3" + sender.getName() + "&f.");
                    Utilities.msg(sender, "&fВы телепортировали игрока &3" + target + " &fна точку спавна.");
                }

                return true;
            }
        }

        return false;
    }

    public void runScheduledTeleportation(Player player) {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> Utilities.msg(player, "&fОсталось &3пять секунд &fдо телепортации на точку спавна."), 0L, TimeUnit.SECONDS);
        executor.schedule(() -> Utilities.msg(player, "&fОсталось &3четыре секунды &fдо телепортации на точку спавна."), 1L, TimeUnit.SECONDS);
        executor.schedule(() -> Utilities.msg(player, "&fОсталось &3три секунды &fдо телепортации на точку спавна."), 2L, TimeUnit.SECONDS);
        executor.schedule(() -> Utilities.msg(player, "&fОсталось &3две секунды &fдо телепортации на точку спавна."), 3L, TimeUnit.SECONDS);
        executor.schedule(() -> Utilities.msg(player, "&fОсталась &3одна секунда &fдо телепортации на точку спавна."), 4L, TimeUnit.SECONDS);
        executor.schedule(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), SpawnCommand::doTeleport), 5L, TimeUnit.SECONDS);
    }

    private static void doTeleport() {
        player.teleport(Anarchy.lobbyLocation);
        Utilities.msg(player, "&fВы были телепортированы на точку спавна.");
    }
}
