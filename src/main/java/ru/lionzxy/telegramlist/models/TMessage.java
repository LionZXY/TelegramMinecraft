package ru.lionzxy.telegramlist.models;

import cpw.mods.fml.common.FMLLog;

/**
 * Created by lionzxy on 10.02.17.
 */
public class TMessage {
    public int message_id, date, forward_from_message_id, edit_date;
    public TUser from, forward_from, to = null;
    public TMessage reply_to_message;
    public String text;

    public TMessage() {

    }

    @Override
    public String toString() {
        return "\"" + text + "\". От пользователя: " + (from == null ? "null" : (from.id + ":" + from.username));

    }
}
