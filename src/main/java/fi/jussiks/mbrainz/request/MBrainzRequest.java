package fi.jussiks.mbrainz.request;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;

public interface MBrainzRequest {
	
	String doRequest();
	URL getURL() throws MalformedURLException, UnsupportedEncodingException;
	void setEntity(Entity entity);
	Entity getEntity();
	String getAppName();
	void setAppName(String appName);
	String getVersion();
	void setVersion(String version);
	String getContact();
	void setContact(String contact);
	String getParameters();
	Inc[] getIncs();
	void setIncs(Inc[] incs);

}
