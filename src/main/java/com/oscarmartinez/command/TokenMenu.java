package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TokenMenu {
	
	private static Logger logger = Logger.getLogger(TokenMenu.class);
	
	protected static final String URL_SERVICE = "http://localhost:9898/api";
	
	public static SendMessage getTokenEvent(Long chatId, String license) {
		final String methodName = "getTokenEvent()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		try {
			URL url = new URL(URL_SERVICE + "/token/bot/" + license);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			String token = "";
			String messageText = "";
			while((response = rd.readLine()) != null) {
				token = response;
			}
			resp.setChatId(chatId.toString());
			if(!token.isEmpty()) {
				String message = "El token activo es: " + token;
				messageText = message;
			} else {
				messageText = "No hay token o evento activo.";
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
