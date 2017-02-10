package ru.lionzxy.telegramlist.handlers.commands;

import net.minecraft.server.MinecraftServer;
import ru.lionzxy.telegramlist.Config;
import ru.lionzxy.telegramlist.exception.PermissionException;
import ru.lionzxy.telegramlist.handlers.IMessageHandler;
import ru.lionzxy.telegramlist.helpers.CommandSenderSave;
import ru.lionzxy.telegramlist.models.TMessage;

/**
 * Created by lionzxy on 10.02.17.
 */
public class Ban implements IMessageHandler {
    @Override
    public TMessage onMessageReceive(TMessage message) throws PermissionException {
        if (message.text.startsWith("/ban")) {
            StringBuilder response = new StringBuilder();
            if (!Config.isAdmin(message.from.username))
                throw new PermissionException();
            String[] args = message.text.split(" ");
            if (args.length < 2)
                response.append("Пример использования: /ban LionZXY %причина%");
            else {
                CommandSenderSave senderSave = new CommandSenderSave(MinecraftServer.getServer());
                MinecraftServer.getServer().getCommandManager().executeCommand(senderSave, message.text);
                response.append(senderSave.getText());
            }

            message.to = message.from;
            message.text = response.toString();
            return message;
        }
        return null;
    }
}
