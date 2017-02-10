package ru.lionzxy.telegramlist.handlers;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.lionzxy.telegramlist.MainLoop;
import ru.lionzxy.telegramlist.exception.PermissionException;
import ru.lionzxy.telegramlist.models.TMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionzxy on 10.02.17.
 */
public class HandlersManager {
    private final List<IMessageHandler> handlers = new ArrayList<>();
    private final List<TMessage> order = new ArrayList<>();
    private static final HandlersManager INSTANCE = new HandlersManager();

    public void notifiyAboutMessage(TMessage message) {
        synchronized (order) {
            order.add(message);
        }
    }

    public void subsribe(IMessageHandler handler) {
        synchronized (handlers) {
            if (handler != null && !handlers.contains(handler))
                handlers.add(handler);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        synchronized (order) {
            if (order.size() > 0)
                synchronized (handlers) {
                    for (TMessage message : order)
                        for (IMessageHandler handler : handlers)
                            try {
                                try {
                                    MainLoop.sendMessage(handler.onMessageReceive(message));
                                } catch (PermissionException e) {
                                    message.to = message.from;
                                    message.text = "У вас нет прав на использование этой комманды";
                                    MainLoop.sendMessage(message);
                                }
                            } catch (Exception e) {
                                StringBuilder text = new StringBuilder();
                                text.append("При обработке сообщения: ").append(message.toString()).append(" возникла ошибка\n");
                                text.append("Message: ").append(ExceptionUtils.getMessage(e)).append('\n');
                                text.append("Stacktrace: ").append(ExceptionUtils.getStackTrace(e));
                                message.to = message.from;
                                message.text = text.toString();
                                MainLoop.sendMessage(message);
                                FMLLog.warning(text.toString());
                            }
                    order.clear();
                }
        }
    }

    public static HandlersManager getInstance() {
        return INSTANCE;
    }
}
