package me.sat7.dynamicshop.utilities;

import me.sat7.dynamicshop.files.CustomConfig;

public final class LayoutUtil
{
    public static CustomConfig ccLayout = new CustomConfig();

    private LayoutUtil()
    {

    }

    public static void Setup()
    {
        ccLayout.setup("Layout", null);
        ccLayout.get().options().header(
                "{Tag} : A predefined placeholder. Available for each item only." +
                "\n{\\nTag} : Line breaks when this item is displayed." +
                "\nSHOP.INFO: §f{ShopLore}{\\nPermission}{\\nTax}{\\nShopBalance}{\\nShopHour}{\\nShopPosition}" +
                "\nSHOP.ITEM_INFO: §f{\\nItemMetaLore}{\\nSell}{\\nBuy}{\\nStock}{\\nPricingType}\n{\\nTradeLore}" +
                "\nTRADE_VIEW.BUY: §f{Price}{\\nStock}{\\nDeliveryCharge}\n{\\nTradeLore}" +
                "\nTRADE_VIEW.SELL: §f{Price}{\\nStock}{\\nDeliveryCharge}\n{\\nTradeLore}" +
                "\nTRADE_VIEW.BALANCE: §f{PlayerBalance}{\\nShopBalance}"
        );

        ccLayout.get().addDefault("SHOP.INFO", "§f{ShopLore}{\\nPermission}{\\nTax}{\\nShopBalance}{\\nShopHour}{\\nShopPosition}");
        ccLayout.get().addDefault("SHOP.ITEM_INFO", "§f{\\nItemMetaLore}{\\nSell}{\\nBuy}{\\nStock}{\\nPricingType}\n{\\nTradeLore}");
        ccLayout.get().addDefault("TRADE_VIEW.BUY", "§f{Price}{\\nStock}{\\nDeliveryCharge}\n{\\nTradeLore}");
        ccLayout.get().addDefault("TRADE_VIEW.SELL", "§f{Price}{\\nStock}{\\nDeliveryCharge}\n{\\nTradeLore}");
        ccLayout.get().addDefault("TRADE_VIEW.BALANCE", "§f{PlayerBalance}{\\nShopBalance}");

        ccLayout.get().options().copyDefaults(true);
        ccLayout.save();
    }

    public static String l(String key)
    {
        return ccLayout.get().getString(key);
    }
}
