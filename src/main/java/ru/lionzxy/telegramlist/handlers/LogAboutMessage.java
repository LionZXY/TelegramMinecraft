package ru.lionzxy.telegramlist.handlers;

import cpw.mods.fml.common.FMLLog;
import ru.lionzxy.telegramlist.handlers.IMessageHandler;
import ru.lionzxy.telegramlist.models.TMessage;
import ru.lionzxy.telegramlist.models.TUser;

/**
 * Created by lionzxy on 10.02.17.
 */
public class LogAboutMessage implements IMessageHandler {
    @Override
    public TMessage onMessageReceive(TMessage message) {
        FMLLog.info("Полученно сообщение: \"" + message.text + "\". От пользователя: " + (message.from == null ? "null" : (message.from.id + ":" + message.from.username)));
        return null;
    }
}
