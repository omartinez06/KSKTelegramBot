package com.oscarmartinez.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.oscarmartinez.command.OptionMenu;

public class BotController extends TelegramLongPollingBot {

	public void onUpdateReceived(Update update) {
		System.out.println(update.getMessage().getText());
		try {
			execute(OptionMenu.sendPrincipalMessage(update.getMessage().getChatId()));
		} catch(TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public String getBotUsername() {
		return "ken_sei_kai";
	}

	@Override
	public String getBotToken() {
		return "5927781711:AAEaeTjljmqIc9hCfGzGQWWuypPWXSc1uE8";
	}

}
