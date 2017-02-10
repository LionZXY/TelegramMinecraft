package ru.lionzxy.telegramlist;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lionzxy on 10.02.17.
 */
public class Config {
    public static Configuration config;
    private static List<String> admins;
    public static volatile boolean logMessage;
    public static volatile int update_interval;
    public static volatile String bot_token;

    public static void createConfig() {
        File configFile = new File(Loader.instance().getConfigDir() + "/TelegramList", "TelegramBot.cfg");
        config = new Configuration(configFile, "1.0.0");
        config.load();
        config.getCategory("bot_auth");
        bot_token = config.get("bot_auth", "bot_token", "Insert here").getString();
        config.getCategory("bot_update");
        update_interval = config.get("bot_update", "update_interval", 1000).getInt();
        logMessage = config.get("bot_update", "logMessage", true).getBoolean();
        config.getCategory("bot_command");
        String[] tmp = new String[]{"Test1", "Test2"};
        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null)
            tmp = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152603_m().func_152685_a();
        admins = Arrays.asList(config.get("bot_command", "admin_nickname", tmp).getStringList());
        config.save();
    }


    public static boolean isAdmin(String nickname) {
        synchronized (admins) {
            for (String admin : admins) {
                System.out.println(admin + " " + nickname);
                if (admin.equalsIgnoreCase(nickname))
                    return true;
            }
            return false;
        }
    }
}
