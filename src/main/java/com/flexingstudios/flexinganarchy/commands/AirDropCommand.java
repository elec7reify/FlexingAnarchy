package com.flexingstudios.flexinganarchy.commands;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.command.SubCommand;
import com.flexingstudios.FlexingNetwork.api.command.SubCommandData;
import com.flexingstudios.FlexingNetwork.api.command.UpCommand;
import com.flexingstudios.FlexingNetwork.api.util.Notifications;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.flexinganarchy.AirDrop.AirDrop;
import com.flexingstudios.flexinganarchy.AirDrop.AirDropUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AirDropCommand extends UpCommand {
    @Override
    protected boolean main(CommandSender commandSender, Command command, String label, String[] args) {
        help(new SubCommandData(commandSender, label, "help", args));
        return true;
    }

    @SubCommand(name = "generateloot", aliases = "genloot", rank = Rank.ADMIN)
    private void generateLoot(SubCommandData data) {
        if (AirDrop.getInstance().getChestLocation().getBlock().getType() == Material.CHEST) {
            AirDropUtil.randomFillChest(AirDrop.getInstance().getChestLocation(), AirDrop.getInstance().getGenerator().loot());
            Utilities.Companion.msg(data.getPlayer(), Notifications.success("Анархия", "Лут успешно сгенерирован."));
        } else {
            Utilities.Companion.msg(data.getPlayer(), Notifications.error("Анархия", "Произошла ошибка. AirDrop сундук не найден."));
        }
    }

    @SubCommand(name = "help", rank = Rank.ADMIN, hidden = true)
    private void help(SubCommandData data) {
        Utilities.Companion.msg(data.getPlayer(), "/airdrop generateloot — сгенерировать лут в сундуке.");
    }
}
