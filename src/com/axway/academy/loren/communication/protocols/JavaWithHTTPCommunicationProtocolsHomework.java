package com.axway.academy.loren.communication.protocols;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author LorenIvanov
 *
 * 
 */
public class JavaWithHTTPCommunicationProtocolsHomework {

	/**
	 * Connecting to www.google.bg and getting response
	 */
	protected void connectingToGoogle() {
		HttpURLConnection connection = null;
		try {
			// initialize the resource
			URL googleUrl = new URL(
					"https://www.google.bg/?gfe_rd=cr&ei=Oe0ZWPzDGqyz8wekibiwCw");
			connection = (HttpURLConnection) googleUrl.openConnection();
			// set request method
			connection.setRequestMethod("GET");

			// execute the request and get status code and message
			int statusCode = connection.getResponseCode();
			writeToFile(statusCode, connection.getResponseMessage());
			System.out.println("The status code is " + statusCode);
			System.out.println("Response message is "
					+ connection.getResponseMessage());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Writes to file
	 * 
	 * @param statusCode
	 *            - status code
	 * @param responseMessage
	 *            - response message
	 */
	private void writeToFile(int statusCode, String responseMessage) {
		Path filePath = Paths.get("googleResponse.txt");
		String response = "" + statusCode;
		response = response.concat(" ").concat(responseMessage);
		try {
			Files.write(filePath, response.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void SSSLHandshake() {

		// Some SSL Handshake magic stuff happening here
		// This code segment makes connections over SSL possible
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {
		}
		// SSL handshake magic ends here
		// SSL handshake will be subject to future topics

		// Set to perform authentication
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("test", "123".toCharArray());
			}
		});
	}

	/**
	 * Creating an instance of JavaWithHTTPCommunicationProtocolsHomework class
	 * and working with its methods
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JavaWithHTTPCommunicationProtocolsHomework obj = new JavaWithHTTPCommunicationProtocolsHomework();
		obj.SSSLHandshake();
		obj.connectingToGoogle();
	}

}
