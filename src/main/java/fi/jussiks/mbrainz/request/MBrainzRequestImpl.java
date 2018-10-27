package fi.jussiks.mbrainz.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;
import fi.jussiks.mbrainz.enums.ResultFormat;

/**
 * Base class for lookups and queries.
 * 
 * @author jussi
 * TODO: method for setting credentials
 */
public abstract class MBrainzRequestImpl implements MBrainzRequest {
	private String appName;
	private String version;
	private String contact;
	
	private static final String API_URL = "http://musicbrainz.org/ws/2";
	private ResultFormat resultFormat = ResultFormat.JSON; // use json as default
	protected Entity entity;
	protected Inc[] incs;
	
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
	
	public MBrainzRequestImpl(String appName, String version, String contact, Entity entity) {
		this(appName, version, contact);
		this.entity = entity;
	}

	public MBrainzRequestImpl(String appName, String version, String contact, Entity entity, Inc[] incs) {
		this(appName, version, contact, entity);
		this.incs = incs;
		
	}

	@Override
	public String doRequest() {
		return doGETRequest();
	}
	
	/**
	 * Makes a GET request to the MusicBrainz Web Service.
	 * @param paramString	path and parameters of the GET request
	 * @return service response as a String
	 */
	private String doGETRequest() {
		HttpURLConnection connection = null;
		try {
			URL url = getURL();
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
	
	@Override
	public URL getURL() throws MalformedURLException, UnsupportedEncodingException {
		String urlString = String.format("%s/%s", API_URL, getParameters());
		if (incs != null) {
			urlString += "?inc=";
			List<Inc> incList = Arrays.asList(incs);
			urlString += incList.stream()
					.map(Inc::toString)
					.collect(Collectors.joining("+"));
		}
		return new URL(urlString);
	}

	@Override
	public String getAppName() {
		return appName;
	}

	@Override
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getContact() {
		return contact;
	}

	@Override
	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	/**
	 * @return subqueries included in lookup
	 */
	public Inc[] getIncs() {
		return incs;
	}

	@Override
	/**
	 * @param incs 	subqueries included in lookup
	 */
	public void setIncs(Inc[] incs) {
		this.incs = incs;
	}

	public static String getApiUrl() {
		return API_URL;
	}
}
