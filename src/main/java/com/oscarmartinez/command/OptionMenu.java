package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class OptionMenu {

	private static Logger logger = Logger.getLogger(OptionMenu.class);

	protected static final String URL_SERVICE = "http://localhost:9898/api";

	public static SendMessage sendPrincipalMessage(Long chatId) {
		final String methodName = "sendPrincipalMessage()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		resp.setChatId(chatId.toString());
		resp.setText("Ingrese su numero de carnet");
		return resp;
	}

	public static SendMessage validateLicense(Long chatId, String license) {
		final String methodName = "validateLicense()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		try {
			URL url = new URL(URL_SERVICE + "/students/license/" + license);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			while((response = rd.readLine()) != null) {
				json = (JSONObject) parser.parse(response);
			}
			resp.setChatId(chatId.toString());
			if(json != null) {
				String message = MessageFormat.format("Bienvenid@ {0} {1}", json.get("name"), json.get("lastName")) + "\n" + "Seleccione una opcion del menu";
				resp = StudentMenu.sendMenuMessage(chatId, message);
			} else {
				resp.setText("Carnet invalido ingreselo nuevamente");
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

}
