package ru.lionzxy.telegramlist.handlers.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import ru.lionzxy.telegramlist.MainLoop;
import ru.lionzxy.telegramlist.handlers.IMessageHandler;
import ru.lionzxy.telegramlist.helpers.CommandSenderSave;
import ru.lionzxy.telegramlist.models.TMessage;
import ru.lionzxy.telegramlist.models.TUser;

import java.util.List;

/**
 * Created by lionzxy on 10.02.17.
 */
public class Info implements IMessageHandler {

    @Override
    public TMessage onMessageReceive(TMessage message) {
        if (message.text.startsWith("/info")) {
            StringBuilder response = new StringBuilder();
            if (MinecraftServer.getServer() == null)
                response.append("Мир еще не загружен");
            else {
                List<EntityPlayer> list = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
                response.append("Количество игроков: ").append(MinecraftServer.getServer().getCurrentPlayerCount()).append("/").append(MinecraftServer.getServer().getMaxPlayers()).append("\n");
                for (EntityPlayer playerMP : list)
                    response.append(playerMP.getGameProfile().getName()).append('\n');
                response.append("Ответ на комманду /forge tps:\n");
                CommandSenderSave senderSave = new CommandSenderSave(MinecraftServer.getServer());
                MinecraftServer.getServer().getCommandManager().executeCommand(senderSave, "/forge tps");
                response.append(senderSave.getText());
            }
            message.to = message.from;
            message.text = response.toString();
            return message;
        }
        return null;
    }

    public boolean canUseCommand(TUser user) {
        return true;
    }
}
