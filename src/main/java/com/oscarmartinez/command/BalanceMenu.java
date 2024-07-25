package com.oscarmartinez.command;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class BalanceMenu {

	private static Logger logger = Logger.getLogger(BalanceMenu.class);

	protected static final String URL_SERVICE = "http://localhost:9898/api";

	public static SendMessage getPendingBalance(Long chatId, String license) {
		final String methodName = "getPendingBalance()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendMessage resp = new SendMessage();
		resp.setChatId(Long.toString(chatId));
		try {
			String message = "";
			URL url = new URL(URL_SERVICE + "/students/balance/" + license);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			while ((response = rd.readLine()) != null) {
				json = (JSONObject) parser.parse(response);
				message += "Su estado: " + json.get("status") + "\n";
				message += "Su saldo al dia de hoy: Q" + json.get("pendingBalance");
			}
			resp = StudentMenu.sendMenuPayment(chatId, message);

		} catch (Exception e) {
			e.printStackTrace();
			resp.setText("Hubo un error al obtener el saldo. Intente nuevamente.");
		}
		return resp;
	}

	private static byte[] fetchPdfFromService(String license) throws IOException {
		// Supongamos que esta es la implementación actual
	    URL url = new URL(URL_SERVICE + "/students/balance/accsts/" + license);
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    
	    InputStream inputStream = connection.getInputStream();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    
	    byte[] buffer = new byte[4096];
	    int bytesRead;
	    
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        outputStream.write(buffer, 0, bytesRead);
	    }
	    
	    outputStream.flush();
	    inputStream.close();
	    outputStream.close();
	    
	    byte[] pdfBytes = outputStream.toByteArray();
	    
	    // Verifica el tamaño del archivo
	    if (pdfBytes.length == 0) {
	        throw new IOException("El archivo PDF generado está vacío.");
	    }
	    
	    return pdfBytes;
	}

	public static SendDocument getAccountStatus(Long chatId, String license) {
		final String methodName = "getAccountStatus()";
		logger.debug(MessageFormat.format("{0} - Begin", methodName));
		SendDocument resp = new SendDocument();
		resp.setChatId(Long.toString(chatId));

		try {
			// Obtener el PDF del servicio
			byte[] pdfBytes = fetchPdfFromService(license);

			// Verificar que los bytes no están vacíos
			if (pdfBytes != null && pdfBytes.length > 0) {
				// Crear un ByteArrayInputStream para el archivo PDF
				InputStream pdfStream = new ByteArrayInputStream(pdfBytes);

				// (Opcional) Verificar el contenido del PDF antes de enviarlo
				// Puedes hacer una prueba simple como verificar el tamaño del archivo
				// y/o intentar leer una parte del contenido
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int read;
				while ((read = pdfStream.read(buffer)) != -1) {
					baos.write(buffer, 0, read);
				}
				pdfStream.close();

				// Verificar que el contenido se ha leído correctamente
				byte[] verifiedBytes = baos.toByteArray();
				if (verifiedBytes.length > 0) {
					InputFile inputFile = new InputFile(new ByteArrayInputStream(verifiedBytes), "Ultimos3Meses.pdf");
					resp.setDocument(inputFile);
				} else {
					logger.warn("El archivo PDF está vacío después de la verificación.");
				}
			} else {
				// Manejar caso en el que no se obtuvo un PDF válido
				logger.warn("No se pudo obtener el PDF o el archivo está vacío.");
			}
		} catch (Exception e) {
			logger.error("Error al obtener el estado de cuenta: ", e);
		}

		return resp;

	}

}
