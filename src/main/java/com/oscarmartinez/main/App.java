package com.oscarmartinez.main;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.oscarmartinez.controller.BotController;

public class App {

	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String args[]) {
		logger.debug("Iniciando BOT process.");
		TelegramBotsApi telegramBotsApi;
		try {
			telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new BotController());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
