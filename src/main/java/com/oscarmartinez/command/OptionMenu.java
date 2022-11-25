package com.oscarmartinez.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class OptionMenu {
	
	private static Logger logger = Logger.getLogger(OptionMenu.class);
	
	public static SendMessage sendPrincipalMessage(Long chatId) {
		SendMessage resp = new SendMessage();
		resp.setChatId(chatId.toString());
		resp.setText("Ingrese su numero de carnet");
		return resp;
	}

}
