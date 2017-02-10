package ru.lionzxy.telegramlist.handlers.commands;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.server.MinecraftServer;
import ru.lionzxy.telegramlist.Config;
import ru.lionzxy.telegramlist.exception.PermissionException;
import ru.lionzxy.telegramlist.handlers.IMessageHandler;
import ru.lionzxy.telegramlist.helpers.CommandSenderSave;
import ru.lionzxy.telegramlist.models.TMessage;
import ru.lionzxy.telegramlist.models.TUser;

/**
 * Created by lionzxy on 10.02.17.
 */
public class Exec implements IMessageHandler {
    @Override
    public TMessage onMessageReceive(TMessage message) throws PermissionException {
        if (message.text.startsWith("/exec")) {
            if (!canUseCommand(message.from))
                throw new PermissionException();
            StringBuilder response = new StringBuilder();
            if (message.text.length() > "/exec".length())
                response.append("Комманда не может быть пустой\n");
            else {
                String cmd = message.text.substring("/exec ".length());
                if (MinecraftServer.getServer() == null)
                    response.append("Мир еще не загружен\n");
                else {
                    response.append("Ответ на комманду \"").append(cmd).append("\"\n");
                    CommandSenderSave senderSave = new CommandSenderSave();
                    MinecraftServer.getServer().getCommandManager().executeCommand(senderSave, cmd);
                    response.append(senderSave.getText());
                }
            }
            message.to = message.from;
            message.text = response.toString();
            return message;
        }
        return null;
    }

    public boolean canUseCommand(TUser user) {
        return Config.isAdmin(user.username);
    }
}
