package com.joe.springracing.utils.io.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.joe.springracing.SpringRacingServices;

public class HTMLReaderIO {
	
	public static String getHTML(String urlToRead) throws Exception {
		 try {
		    @SuppressWarnings("deprecation")
		    ConnectionSocketFactory sslConnectionFactory = new SSLSocketFactory(new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					return true;
				}
			 }, new AllowAllHostnameVerifier());
			 Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
		         .register("https", sslConnectionFactory)
		         .register("http", PlainConnectionSocketFactory.getSocketFactory())
		         .build();
		//         CredentialsProvider credsProvider = new BasicCredentialsProvider();
		//         credsProvider.setCredentials(new AuthScope(host, port, MANAGEMENT_REALM, AuthSchemes.DIGEST),
		//                 new UsernamePasswordCredentials(username, password));
			 PoolingHttpClientConnectionManager connectionPool = new PoolingHttpClientConnectionManager(registry);
			 HttpClientBuilder.create().setConnectionManager(connectionPool).build();
			 CloseableHttpClient httpClient = HttpClientBuilder.create()
		         .setConnectionManager(connectionPool)
		         .setRetryHandler(new StandardHttpRequestRetryHandler(5, true))
		//                 .setDefaultCredentialsProvider(credsProvider)
		             .build();
		     return doGET(httpClient, urlToRead);
		 } catch (Exception e) {
		     throw new RuntimeException(e);
		 }
	}
	
	public static String _getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		
		HttpURLConnection conn;
		if (SpringRacingServices.useHTTPProxy()) {
			conn = (HttpURLConnection) url.openConnection(SpringRacingServices.getHTTPProxy());			
		    Authenticator.setDefault(SpringRacingServices.getHTTPAuthenticator());
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		
		conn.setRequestMethod("GET");
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
			   result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
	private static String doGET(CloseableHttpClient httpClient, String url) throws IOException {
		try {
			HttpGet httpGet = new HttpGet(url);
//			httpGet.addHeader("User-Agent", USER_AGENT);
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
	
//			System.out.println("GET Response Status:: "
//					+ httpResponse.getStatusLine().getStatusCode());
//	
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
	
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			reader.close();
			return response.toString();
		} finally {
			httpClient.close();
		}
	}

}
