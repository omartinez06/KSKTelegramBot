package com.oscarmartinez.command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class StudentMenu {
	
	private static Logger logger = Logger.getLogger(StudentMenu.class);
	
	protected static final String URL_SERVICE = "http://localhost:9898/api";
	
	public static SendMessage sendMenuMessage(Long chatId, String message) {
		final String methodName = "sendMenuMessage()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		InlineKeyboardButton button = new InlineKeyboardButton();
		List<InlineKeyboardButton> buttonrow = new ArrayList<InlineKeyboardButton>();
		List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
		
		/*button.setText("Saldo Pendiente");
		button.setCallbackData("PENDING_BALANCE");
		buttonrow.add(button);
		rowList.add(buttonrow);*/
		
		button = new InlineKeyboardButton();
		buttonrow = new ArrayList<InlineKeyboardButton>();
		button.setText("Registro De Pago");
		button.setCallbackData("PAYMENT_RECORD");
		buttonrow.add(button);
		rowList.add(buttonrow);
		
		button = new InlineKeyboardButton();
		buttonrow = new ArrayList<InlineKeyboardButton>();
		button.setText("Consulta Proximos Eventos");
		button.setCallbackData("EVENT_QUERY");
		buttonrow.add(button);
		rowList.add(buttonrow);
		
		inlineKeyboardMarkup.setKeyboard(rowList);
		
		SendMessage resp = new SendMessage().builder().chatId(Long.toString(chatId)).text(message)
		.replyMarkup(inlineKeyboardMarkup).build();
		return resp;
	}

}
