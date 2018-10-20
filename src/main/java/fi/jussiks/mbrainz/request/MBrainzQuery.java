package fi.jussiks.mbrainz.request;

import fi.jussiks.mbrainz.enums.Entity;

/**
 * Provides methods for querying the MusicBrainz database.
 * 
 * @author jussi
 */
public class MBrainzQuery extends MBrainzRequest {
	
	/**
	 * Initializes a new MBrainzQuery instance. Each request needs to include contact
	 * information so MusicBrainz can contact the application maintainers if necessary.
	 * 
	 * @param appName	name of the application
	 * @param version	version of the application
	 * @param contact	email or url for contacting application maintainers
	 */
	public MBrainzQuery(String appName, String version, String contact) {
		super(appName, version, contact);
	}
	
	/**
	 * Queries MusicBrainz to find albums that have the specified barcode.
	 * @param barcode	barcode of the album
	 * @return String 
	 */
	public String getAlbumsByBarcode(long barcode) {
		return doQuery(Entity.RELEASE, "barcode:" + barcode, "artist-credits+recordings+labels");
	}
	

	
	/**
	 * Queries the MusicBrainz database.
	 * 
	 * @param entity	determines which entities to query (for example
	 * 							artist, release, release-group)
	 * @param params	parameters of the query
	 * @param inc		extra fields to be included in the results
	 * @return String representing a list of query results in either xml
	 * 							or json format. 
	 */
	public String doQuery(Entity entity, String params, String inc) {
		String paramString = String.format("%s?query=%s", entity.toString(), params);
		paramString += inc.equals("") ? "" : "&inc=" + inc; 
		return doGETRequest(paramString);
	}
}