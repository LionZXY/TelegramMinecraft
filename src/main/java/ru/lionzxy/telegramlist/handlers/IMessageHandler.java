package ru.lionzxy.telegramlist.handlers;

import ru.lionzxy.telegramlist.exception.PermissionException;
import ru.lionzxy.telegramlist.models.TMessage;
import ru.lionzxy.telegramlist.models.TUser;

/**
 * Created by lionzxy on 10.02.17.
 */
public interface IMessageHandler {
    TMessage onMessageReceive(TMessage message) throws PermissionException;
}
