package me.sat7.dynamicshop.commands.shop;

import me.sat7.dynamicshop.DynamicShop;
import me.sat7.dynamicshop.commands.DSCMD;
import me.sat7.dynamicshop.commands.Shop;
import me.sat7.dynamicshop.models.DSItem;
import me.sat7.dynamicshop.utilities.ItemsUtil;
import me.sat7.dynamicshop.utilities.ShopUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sat7.dynamicshop.constants.Constants.P_ADMIN_SHOP_EDIT;
import static me.sat7.dynamicshop.utilities.LangUtil.t;

public class AddHand extends DSCMD
{
    public AddHand()
    {
        permission = P_ADMIN_SHOP_EDIT;
        validArgCount.add(6);
        validArgCount.add(8);
    }

    @Override
    public void SendHelpMessage(Player player)
    {
        player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "HELP.TITLE").replace("{command}", "addhand"));
        player.sendMessage(" - " + t(player, "HELP.USAGE") + ": ... addhand <value> <median> <stock>");
        player.sendMessage(" - " + t(player, "HELP.USAGE") + ": ... addhand <value> <min value> <max value> <median> <stock>");
        player.sendMessage(" - " + t(player, "HELP.SHOP_ADD_HAND"));
        player.sendMessage(" - " + t(player, "HELP.PRICE"));
        player.sendMessage(" - " + t(player, "HELP.INF_STATIC"));
    }

    @Override
    public void RunCMD(String[] args, CommandSender sender)
    {
        if(!CheckValid(args, sender))
            return;

        Player player = (Player) sender;

        String shopName = Shop.GetShopName(args);
        double buyValue;
        double valueMin = 0.0001;
        double valueMax = -1;
        int median;
        int stock;

        try
        {
            if (args.length == 6)
            {
                buyValue = Double.parseDouble(args[3]);
                median = Integer.parseInt(args[4]);
                stock = Integer.parseInt(args[5]);
            } else
            {
                buyValue = Double.parseDouble(args[3]);
                valueMin = Double.parseDouble(args[4]);
                valueMax = Double.parseDouble(args[5]);
                median = Integer.parseInt(args[6]);
                stock = Integer.parseInt(args[7]);

                // 유효성 검사
                if (valueMax > 0 && valueMin > 0 && valueMin >= valueMax)
                {
                    player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.MAX_LOWER_THAN_MIN"));
                    return;
                }
                if (valueMax > 0 && buyValue > valueMax)
                {
                    player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.DEFAULT_VALUE_OUT_OF_RANGE"));
                    return;
                }
                if (valueMin > 0 && buyValue < valueMin)
                {
                    player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.DEFAULT_VALUE_OUT_OF_RANGE"));
                    return;
                }
            }

            if (buyValue < 0.0001 || median == 0 || stock == 0)
            {
                player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.VALUE_ZERO"));
                return;
            }
        } catch (Exception e)
        {
            player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.WRONG_DATATYPE"));
            return;
        }

        // 손에 뭔가 들고있는지 확인
        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
        {
            player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.HAND_EMPTY"));
            return;
        }

        // 금지품목
        if (Material.getMaterial(player.getInventory().getItemInMainHand().getType().toString()) == Material.AIR)
        {
            player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.ITEM_FORBIDDEN"));
            return;
        }

        DSItem temp = new DSItem(player.getInventory().getItemInMainHand(), buyValue, buyValue, valueMin, valueMax, median, stock);

        // 상점에서 같은 아이탬 찾기
        int idx = ShopUtil.findItemFromShop(shopName, player.getInventory().getItemInMainHand());
        // 상점에 새 아이탬 추가
        if (idx == -1)
        {
            idx = ShopUtil.findEmptyShopSlot(shopName, 1, true);
            if (idx == -1)
            {
                player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "ERR.NO_EMPTY_SLOT"));
            } else if (ShopUtil.addItemToShop(shopName, idx, temp)) // 아이탬 추가
            {
                player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "MESSAGE.ITEM_ADDED"));
                ItemsUtil.sendItemInfo(player, shopName, idx, "HELP.ITEM_INFO");
            }
        }
        // 기존 아이탬 수정
        else
        {
            ShopUtil.editShopItem(shopName, idx, temp);
            player.sendMessage(DynamicShop.dsPrefix(player) + t(player, "MESSAGE.ITEM_UPDATED"));
            ItemsUtil.sendItemInfo(player, shopName, idx, "HELP.ITEM_INFO");
        }
    }
}
