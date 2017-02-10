package ru.lionzxy.telegramlist;

import com.google.gson.*;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.server.MinecraftServer;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import ru.lionzxy.telegramlist.handlers.HandlersManager;
import ru.lionzxy.telegramlist.models.TMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionzxy on 10.02.17.
 */
public class MainLoop extends Thread {
    private int lastUpdateId = 0;
    private static final List<TMessage> orderToSend = new ArrayList<>();

    @Override
    public void run() {
        super.run();
        HttpClient client = HttpClientBuilder.create().build();
        while (true) {
            try {
                sleep(Config.update_interval);
                String url = "https://api.telegram.org/bot" + Config.bot_token + "/";
                HttpPost getUpdates = new HttpPost(url + "getUpdates");

                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                urlParameters.add(new BasicNameValuePair("offset", String.valueOf(lastUpdateId + 1)));
                getUpdates.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = client.execute(getUpdates);

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                JsonObject element = new JsonParser().parse(rd).getAsJsonObject();
                if (element.get("ok").getAsBoolean()) {
                    JsonArray array = element.getAsJsonArray("result");
                    for (JsonElement updateElement : array) {
                        JsonObject updateObject = (JsonObject) updateElement;
                        if (updateObject.get("update_id").getAsInt() > lastUpdateId)
                            lastUpdateId = updateObject.get("update_id").getAsInt();
                        TMessage message = new Gson().fromJson(updateObject.get("message"), TMessage.class);
                        HandlersManager.getInstance().notifiyAboutMessage(message);
                    }
                } else FMLLog.warning(new Gson().toJson(element));

                synchronized (orderToSend) {
                    for (TMessage message : orderToSend)
                        try {
                            if (message != null && message.to != null) {
                                HttpPost sendMessage = new HttpPost(url + "sendMessage");
                                FMLLog.info(message.text);

                                urlParameters = new ArrayList<NameValuePair>();
                                urlParameters.add(new BasicNameValuePair("chat_id", String.valueOf(message.to.id)));
                                urlParameters.add(new BasicNameValuePair("text", message.text));
                                urlParameters.add(new BasicNameValuePair("parse_mode", "Markdown"));
                                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(urlParameters, Charset.forName("utf-8"));
                                sendMessage.setEntity(formEntity);

                                response = client.execute(sendMessage);

                                rd = new BufferedReader(
                                        new InputStreamReader(response.getEntity().getContent()));

                                StringBuilder result = new StringBuilder();
                                String line = "";
                                while ((line = rd.readLine()) != null) {
                                    result.append(line);
                                }

                                FMLLog.info("Сообщение отправленно. " + result);
                            } else
                                FMLLog.warning("Невозможно отправить сообщение без адресата. " + new Gson().toJson(message));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    orderToSend.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(TMessage message) {
        synchronized (orderToSend) {
            if (message != null && !orderToSend.contains(message) && message.text != null && message.text.length() > 0)
                orderToSend.add(message);
        }
    }
}
