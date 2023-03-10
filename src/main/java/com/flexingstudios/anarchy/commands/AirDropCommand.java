package com.flexingstudios.anarchy.commands;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.command.CmdSub;
import com.flexingstudios.FlexingNetwork.api.command.UpCommand;
import com.flexingstudios.FlexingNetwork.api.command.dataCommand;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.anarchy.AirDrop.AirDrop;
import com.flexingstudios.anarchy.AirDrop.AirDropUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AirDropCommand extends UpCommand {
    private AirDrop airDrop;

    public AirDropCommand(AirDrop airDrop) {
        this.airDrop = airDrop;
    }

    @Override
    protected boolean main(CommandSender commandSender, Command command, String label, String[] args) {
        help(new dataCommand(commandSender, label, "help", args));
        return true;
    }

    @CmdSub(value = "generateloot", aliases = "genloot", rank = Rank.ADMIN)
    private void generateLoot(dataCommand data) {
        if (airDrop.getChestLocation().getBlock().getType() == Material.CHEST) {
            AirDropUtil.randomFillChest(airDrop.getChestLocation(), airDrop.getGenerator().loot());
            Utilities.msg(data.getPlayer(), T.success("Анархия", "Лут успешно сгенерирован."));
        } else {
            Utilities.msg(data.getPlayer(), T.error("Анархия", "Произошла ошибка. AirDrop сундук не найден."));
        }
    }

    @CmdSub(value = "help", rank = Rank.ADMIN, hidden = true)
    private void help(dataCommand data) {
        Utilities.msg(data.getPlayer(), "");
    }
}
