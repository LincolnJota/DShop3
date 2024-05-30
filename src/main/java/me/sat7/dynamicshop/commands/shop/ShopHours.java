package me.sat7.dynamicshop.commands.shop;

import me.sat7.dynamicshop.DynamicShop;
import me.sat7.dynamicshop.commands.DSCMD;
import me.sat7.dynamicshop.commands.Shop;
import me.sat7.dynamicshop.files.CustomConfig;
import me.sat7.dynamicshop.utilities.ShopUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sat7.dynamicshop.constants.Constants.P_ADMIN_SHOP_EDIT;
import static me.sat7.dynamicshop.utilities.LangUtil.t;
import static me.sat7.dynamicshop.utilities.MathUtil.Clamp;

public class ShopHours extends DSCMD
{
    public ShopHours()
    {
        inGameUseOnly = false;
        permission = P_ADMIN_SHOP_EDIT;
        validArgCount.add(5);
    }

    @Override
    public void SendHelpMessage(Player player)
    {
        player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "HELP.TITLE").replace("{command}", "shophours"));
        player.sendMessage(" - " + t(player, "HELP.USAGE") + ": ... shophours <open> <close>");

        player.sendMessage("");
    }

    @Override
    public void RunCMD(String[] args, CommandSender sender)
    {
        if(!CheckValid(args, sender))
            return;

        String shopName = Shop.GetShopName(args);
        CustomConfig shopData = ShopUtil.shopConfigFiles.get(shopName);

        try
        {
            int start = Clamp(Integer.parseInt(args[3]),1,24);
            int end = Clamp(Integer.parseInt(args[4]), 1, 24);

            if (start == end)
            {
                shopData.get().set("Options.shophours", null);
                shopData.save();

                sender.sendMessage(DynamicShop.dsPrefix(sender) + t(sender, "MESSAGE.CHANGES_APPLIED") + "Open 24 hours");
            } else
            {
                shopData.get().set("Options.shophours", start + "~" + end);
                shopData.save();

                sender.sendMessage(DynamicShop.dsPrefix(sender) + t(sender, "MESSAGE.CHANGES_APPLIED") + start + "~" + end);
            }
        } catch (Exception e)
        {
            sender.sendMessage(DynamicShop.dsPrefix(sender) + t(sender, "ERR.WRONG_DATATYPE"));
        }
    }
}
