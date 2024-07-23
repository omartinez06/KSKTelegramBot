package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BalanceMenu {
	
	private static Logger logger = Logger.getLogger(BalanceMenu.class);
	
	protected static final String URL_SERVICE = "http://localhost:9898/api";
	
	public static SendMessage getPendingBalance(Long chatId, String license) {
		final String methodName = "getPendingBalance()";
	    logger.debug(MessageFormat.format("{0} - Begin", methodName));
	    SendMessage resp = new SendMessage();
	    resp.setChatId(Long.toString(chatId));
	    try {
	        URL url = new URL(URL_SERVICE + "/students/balance/" + license);
	        System.out.println(url);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        
	        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuilder responseBuilder = new StringBuilder();
	        String responseLine;
	        
	        while ((responseLine = rd.readLine()) != null) {
	            responseBuilder.append(responseLine);
	        }
	        
	        rd.close();
	        connection.disconnect();
	        
	        int balance = Integer.parseInt(responseBuilder.toString());
	        
	        String message = "Su saldo pendiente al día de hoy es de: Q" + balance + ".";
	        resp = StudentMenu.sendMenuPayment(chatId, message);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        resp.setText("Hubo un error al obtener el saldo. Intente nuevamente.");
	    }
	    return resp;
	}

}
