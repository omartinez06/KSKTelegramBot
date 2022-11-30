package com.oscarmartinez.controller;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.oscarmartinez.command.OptionMenu;
import com.oscarmartinez.command.PaymentMenu;

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
				} else if(update.getMessage().getText().matches("[\\d][|][\\d]")){
					System.out.println("FUNCIONO!");
					String[] array = update.getMessage().getText().split("|");
					String ticket = array[0];
					int value = Integer.parseInt(array[1]);
					execute(PaymentMenu.sendMessagePaymentRegister(chatId, carnet, ticket, value));
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
