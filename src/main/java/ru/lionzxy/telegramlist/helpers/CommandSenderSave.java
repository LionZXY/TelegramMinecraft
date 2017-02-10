package ru.lionzxy.telegramlist.helpers;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * Created by lionzxy on 10.02.17.
 */
public class CommandSenderSave implements ICommandSender {
    private StringBuilder stringBuilder = new StringBuilder();
    private ICommandSender commandSender;

    public CommandSenderSave() {
        this(MinecraftServer.getServer());
    }

    public CommandSenderSave(ICommandSender realCommandSender) {
        this.commandSender = realCommandSender;
    }

    @Override
    public String getCommandSenderName() {
        return "Telegram";
    }

    @Override
    public IChatComponent func_145748_c_() {
        return commandSender.func_145748_c_();
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {
        if (p_145747_1_ instanceof ChatComponentStyle)
            stringBuilder.append(((ChatComponentStyle) p_145747_1_).getUnformattedTextForChat()).append("\n");
        commandSender.addChatMessage(p_145747_1_);
    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return true;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return commandSender.getPlayerCoordinates();
    }

    @Override
    public World getEntityWorld() {
        return commandSender.getEntityWorld();
    }

    public String getText() {
        return stringBuilder.toString();
    }
}
