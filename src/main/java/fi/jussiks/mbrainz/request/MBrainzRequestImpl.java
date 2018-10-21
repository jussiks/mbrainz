package fi.jussiks.mbrainz.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fi.jussiks.mbrainz.enums.Inc;
import fi.jussiks.mbrainz.enums.ResultFormat;

public abstract class MBrainzRequestImpl implements MBrainzRequest {
	private static final String API_URL = "http://musicbrainz.org/ws/2";
	private String appName;
	private String version;
	private String contact;
	private ResultFormat resultFormat = ResultFormat.JSON; // use json as default
	
	/**
	 * Initializes a new MBrainzRequest object. Each request needs to include contact
	 * information so MusicBrainz can contact the application maintainers if necessary.
	 * 
	 * @param appName	name of the application
	 * @param version	version of the application
	 * @param contact	email or url for contacting application maintainers
	 */
	public MBrainzRequestImpl(String appName, String version, String contact) {
		this.appName = appName;
		this.version = version;
		this.contact = contact;
	}
	
	protected String doRequest(String params, Inc... incs) {
		if (incs != null) {
			params += "?inc=";
			List<Inc> incList = Arrays.asList(incs);
			params += incList.stream()
					.map(Inc::toString)
					.collect(Collectors.joining("+"));
		}
		return doGETRequest(params);
	}
	
	/**
	 * Makes a GET request to the MusicBrainz Web Service.
	 * @param paramString	path and parameters of the GET request
	 * @return service response as a String
	 */
	protected String doGETRequest(String paramString) {
		String urlString = String.format("%s/%s", API_URL, paramString);
		HttpURLConnection connection = null;
		System.out.println(urlString);
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", getUserAgent());
			connection.setRequestProperty("Accept", resultFormat.toString());
			
			BufferedReader in = null;
			int code = connection.getResponseCode();
			
			if (code < 300) {
				in = new BufferedReader(
						new InputStreamReader(connection.getInputStream(), "UTF-8"));
			} else {
				in = new BufferedReader(
						new InputStreamReader(connection.getErrorStream(), "UTF-8"));
			}

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (ProtocolException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	/**
	 * Returns the value of User-Agent header.
	 */
	public String getUserAgent() {
		return String.format("%s/%s { %s }", appName, version, contact);
	}

	/**
	 * Returns the selected result format (either ResultFormat.XML or ResultFormat.JSON).
	 */
	public ResultFormat getResultFormat() {
		return resultFormat;
	}

	/**
	 * Sets the format in which the results of the query should be returned.
	 * @param resultFormat	either ResultFormat.XML or ResultFormat.JSON
	 */
	public void setResultFormat(ResultFormat resultFormat) {
		this.resultFormat = resultFormat;
	}
}
