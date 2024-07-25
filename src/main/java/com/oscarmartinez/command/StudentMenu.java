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

	    // Crear la lista que contendrá las filas de botones
	    List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

	    // Primer botón: "Estado y Saldo Pendiente"
	    List<InlineKeyboardButton> buttonrow1 = new ArrayList<>();
	    InlineKeyboardButton button1 = new InlineKeyboardButton();
	    button1.setText("Estado y Saldo Pendiente");
	    button1.setCallbackData("PENDING_BALANCE");
	    buttonrow1.add(button1);
	    rowList.add(buttonrow1);

	    // Segundo botón: "Estado Cuenta"
	    List<InlineKeyboardButton> buttonrow2 = new ArrayList<>();
	    InlineKeyboardButton button2 = new InlineKeyboardButton();
	    button2.setText("Estado Cuenta");
	    button2.setCallbackData("ACCOUNT_STATUS");
	    buttonrow2.add(button2);
	    rowList.add(buttonrow2);

	    // Tercer botón: "Registro De Pago"
	    List<InlineKeyboardButton> buttonrow3 = new ArrayList<>();
	    InlineKeyboardButton button3 = new InlineKeyboardButton();
	    button3.setText("Registro De Pago");
	    button3.setCallbackData("PAYMENT_RECORD");
	    buttonrow3.add(button3);
	    rowList.add(buttonrow3);

	    // Cuarto botón: "Consulta Proximos Eventos"
	    List<InlineKeyboardButton> buttonrow4 = new ArrayList<>();
	    InlineKeyboardButton button4 = new InlineKeyboardButton();
	    button4.setText("Consulta Proximos Eventos");
	    button4.setCallbackData("EVENT_QUERY");
	    buttonrow4.add(button4);
	    rowList.add(buttonrow4);

	    // Quinto botón: "Consulta Token Activo"
	    List<InlineKeyboardButton> buttonrow5 = new ArrayList<>();
	    InlineKeyboardButton button5 = new InlineKeyboardButton();
	    button5.setText("Consulta Token Activo");
	    button5.setCallbackData("EVENT_TOKEN");
	    buttonrow5.add(button5);
	    rowList.add(buttonrow5);

	    // Configurar el teclado en línea
	    inlineKeyboardMarkup.setKeyboard(rowList);

	    // Crear y devolver el mensaje
	    SendMessage resp = new SendMessage();
	    resp.setChatId(Long.toString(chatId));
	    resp.setText(message);
	    resp.setReplyMarkup(inlineKeyboardMarkup);

	    return resp;
	}
	
	public static SendMessage sendMenuPayment(Long chatId, String message) {
		final String methodName = "sendMenuMessage()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		InlineKeyboardButton button = new InlineKeyboardButton();
		List<InlineKeyboardButton> buttonrow = new ArrayList<InlineKeyboardButton>();
		List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
		button = new InlineKeyboardButton();
		buttonrow = new ArrayList<InlineKeyboardButton>();
		button.setText("Registrar Pago");
		button.setCallbackData("PAYMENT_RECORD");
		buttonrow.add(button);
		rowList.add(buttonrow);
		
		button = new InlineKeyboardButton();
		buttonrow = new ArrayList<InlineKeyboardButton>();
		button.setText("Regresar Al Menu Principal");
		button.setCallbackData("RETURN");
		buttonrow.add(button);
		rowList.add(buttonrow);
		
		inlineKeyboardMarkup.setKeyboard(rowList);
		
		SendMessage resp = new SendMessage().builder().chatId(Long.toString(chatId)).text(message)
		.replyMarkup(inlineKeyboardMarkup).build();
		return resp;
	}

}
