package com.axway.academy.loren.communication.protocols;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

import org.apache.commons.io.IOUtils;

/**
 * 
 * Connecting to google server and getting response
 * 
 * @author LorenIvanov
 * 
 */
public class JavaWithHTTPCommunicationProtocolsHomework {

	private final static String URL_ADDRESS = "https://www.google.bg/?gfe_rd=cr&ei=Oe0ZWPzDGqyz8wekibiwCw";
	private static URL GOOGLE_URL = null;
	private final static Path FILE_NAME = Paths.get("googleResponse.txt");

	/**
	 * Connecting to www.google.bg and getting response
	 */
	protected void connectingToGoogle() {
		HttpURLConnection connection = null;
		try {
			// initialize the resource
			GOOGLE_URL = new URL(URL_ADDRESS);
			connection = (HttpURLConnection) GOOGLE_URL.openConnection();
			// set request method
			connection.setRequestMethod("GET");

			// execute the request and get status code and message
			int statusCode = connection.getResponseCode();
			writeToFile(statusCode, connection);
			System.out.println("The status code is " + statusCode);
			System.out.println("Response message is "
					+ connection.getResponseMessage());

		} catch (MalformedURLException e) {
			System.out.println("Handeled MalformedURLException!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Handeled IOException!");
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
	 * @param connection
	 *            - gets connection
	 * @throws IOException
	 */
	private void writeToFile(int statusCode, HttpURLConnection connection)
			throws IOException {
		String body = getBody(connection);
		String response = "" + statusCode;
		response = response.concat(" ");
		response = response.concat(connection.getResponseMessage());
		response = response.concat("\r\n");
		response = response.concat(body);
		try {
			Files.write(FILE_NAME, response.getBytes());
		} catch (IOException e) {
			System.out.println("Handeled IOException!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Handeled Exception!");
			e.printStackTrace();
		}
	}

	/**
	 * Generates response to string
	 * 
	 * @param connection
	 *            - gets connection
	 * @return content
	 * @throws IOException
	 */
	private String getBody(HttpURLConnection connection) throws IOException {
		InputStream is = connection.getInputStream();
		String encoding = connection.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(is, encoding);
		if (is != null) {
			is.close();
		}
		return body;
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
			System.out.println("Handeled Exception.");
			e.printStackTrace();
		}
		// SSL handshake magic ends here
		// SSL handshake will be subject to future topics

		// Set to perform authentication
		// Authenticator.setDefault(new Authenticator() {
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("test", "123".toCharArray());
		// }
		// });
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
