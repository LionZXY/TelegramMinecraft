package ru.lionzxy.telegramlist;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import ru.lionzxy.telegramlist.handlers.HandlersManager;
import ru.lionzxy.telegramlist.handlers.commands.Exec;
import ru.lionzxy.telegramlist.handlers.commands.Info;
import ru.lionzxy.telegramlist.handlers.LogAboutMessage;

@Mod(name = TelegramListMod.NAME, modid = TelegramListMod.MODID, version = TelegramListMod.VERSION)
public class TelegramListMod {
    public static final String NAME = "Telegram WhiteList Mod";
    public static final String MODID = "telegramlist";
    public static final String VERSION = "1.0";
    private static MainLoop mainLoop;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLLog.info("Telegram module loaded...");
        Config.createConfig();

        if (Config.logMessage)
            HandlersManager.getInstance().subsribe(new LogAboutMessage());
        HandlersManager.getInstance().subsribe(new Info());
        HandlersManager.getInstance().subsribe(new Exec());
        FMLCommonHandler.instance().bus().register(HandlersManager.getInstance());

        if (!"Insert here".equals(Config.bot_token)) {
            FMLLog.info("Запускается MainLoop...");
            mainLoop = new MainLoop();
            mainLoop.start();
        } else FMLLog.warning("Отсутвует bot_token");
    }
}
