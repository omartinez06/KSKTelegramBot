package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

public class PaymentMenu {

	private static Logger logger = Logger.getLogger(PaymentMenu.class);

	protected static final String URL_SERVICE = "http://localhost:9898/api";
	
	public static SendMessage messageInsertPayment(Long chatId) {
		final String methodName = "messageInsertPayment()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		resp.setChatId(Long.toString(chatId));
		String message = "Ingrese los datos de su pago en el siguiente formato:" + "\n";
		message += "No.Boleta o Deposito|valor" + "\n";
		message += "\n";
		message += "\n";
		message += "Ejemplo:" + "\n";
		message += "123456789|450";
		resp.setText(message);
		return resp;
	}

	public static SendMessage sendMessagePaymentRegister(Long chatId, String license, String deposit, int value, String user) {
		final String methodName = "sendMessagePaymentRegister()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		try {
			URL url = new URL(URL_SERVICE + "/payment/paybot/");
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			JSONObject json = new JSONObject();
			json.put("depositTicket", deposit);
			json.put("value", value);
			json.put("studentLicense", license);
			json.put("insertedBy", user);

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = json.toJSONString().getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
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
			
			resp = SendMessage.builder().chatId(Long.toString(chatId)).text("Su pago ha sido registrado, al ser autorizado por el DOJO recibira su recibo por correo electronico.")
			.replyMarkup(inlineKeyboardMarkup).build();
		} catch (Exception e) {
			e.printStackTrace();
			resp.setText("Hubo un error al registrar su pago intente mas tarde");
		}
		return resp;
	}

}
