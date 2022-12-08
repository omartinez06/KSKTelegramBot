package com.oscarmartinez.controller;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.oscarmartinez.command.EventMenu;
import com.oscarmartinez.command.OptionMenu;
import com.oscarmartinez.command.PaymentMenu;
import com.oscarmartinez.command.TokenMenu;

public class BotController extends TelegramLongPollingBot {
	
	private static Logger logger = Logger.getLogger(BotController.class);
	
	private static String userName = null;
	private static String userLast = null;
	private static String userId = null;
	private static long chatId = 0;
	private static String carnet = null;

	public void onUpdateReceived(Update update) {
		try {
			if(update.hasMessage()) {
				System.out.println(update.getMessage().getText());
				if(update.getMessage().getText().equals("/start")) {
					execute(OptionMenu.sendPrincipalMessage(update.getMessage().getChatId()));
				} else if(update.getMessage().getText().matches("\\d+[|]\\d+")){
					System.out.println("FUNCIONO!");
					String command = update.getMessage().getText();
					String[] array = command.split("\\|",2);
					String ticket = array[0];
					int value = Integer.parseInt(array[1]);
					execute(PaymentMenu.sendMessagePaymentRegister(chatId, carnet, ticket, value, userId+"-"+userName+"-"+userLast));
				} else {
					execute(OptionMenu.validateLicense(update.getMessage().getChatId(), update.getMessage().getText()));
					carnet = update.getMessage().getText();
				}
			} else if(update.hasCallbackQuery()) {
				userName = update.getCallbackQuery().getFrom().getFirstName();
				userLast = update.getCallbackQuery().getFrom().getLastName();
				userId = update.getCallbackQuery().getFrom().getUserName();
				chatId = update.getCallbackQuery().getMessage().getChatId();
				
				try {
					System.out.print(update.getCallbackQuery().getData());
					if(update.getCallbackQuery().getData().equals("PAYMENT_RECORD")) {
						execute(PaymentMenu.messageInsertPayment(chatId));
					} else if(update.getCallbackQuery().getData().equals("EVENT_QUERY")) {
						execute(EventMenu.sendNextEvents(chatId, carnet));
					} else if(update.getCallbackQuery().getData().equals("EVENT_TOKEN")) {
						execute(TokenMenu.getTokenEvent(chatId, carnet));
					}
				} catch (Exception ex) {
					logger.error("Error: ", ex);
				}
			}
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
