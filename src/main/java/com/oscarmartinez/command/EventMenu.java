package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class EventMenu {

	private static Logger logger = Logger.getLogger(EventMenu.class);

	protected static final String URL_SERVICE = "http://localhost:9898/api";

	public static SendMessage sendNextEvents(Long chatId, String license) {
		final String methodName = "sendNextEvents()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		resp.setChatId(Long.toString(chatId));
		try {
			URL url = new URL(URL_SERVICE + "/event/next/" + license);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			JSONParser parser = new JSONParser();
			JSONArray json = null;
			String messageText = "";
			while ((response = rd.readLine()) != null) {
				json = (JSONArray) parser.parse(response);
			}
			resp.setChatId(chatId.toString());
			if (json != null) {
				String message = "Los siguientes eventos son: \n";
				for (int x = 0; x < json.size(); x++) {
					JSONObject object = (JSONObject) json.get(x);
					message += "Evento: " + object.get("name") + "\n" + "Fecha: " + object.get("initialDate").toString().substring(0, object.get("initialDate").toString().indexOf("T"));
					message += "\n\n\n";
				}
				messageText = message;
			} else {
				messageText = "Carnet invalido ingreselo nuevamente";
			}
			InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
			InlineKeyboardButton button = new InlineKeyboardButton();
			List<InlineKeyboardButton> buttonrow = new ArrayList<InlineKeyboardButton>();
			List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
			button = new InlineKeyboardButton();
			buttonrow = new ArrayList<InlineKeyboardButton>();
			button.setText("Regresar Al Menu Principal");
			button.setCallbackData("RETURN");
			buttonrow.add(button);
			rowList.add(buttonrow);
			
			inlineKeyboardMarkup.setKeyboard(rowList);
			
			resp = SendMessage.builder().chatId(Long.toString(chatId)).text(messageText)
			.replyMarkup(inlineKeyboardMarkup).build();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

}
