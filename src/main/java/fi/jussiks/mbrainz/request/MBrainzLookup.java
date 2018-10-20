package fi.jussiks.mbrainz.request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;

/**
 * MBrainzLookup performs direct lookups using MusicBrainz ids of entities.
 * 
 * @author jussi
 */
public class MBrainzLookup extends MBrainzRequest {
	
	/**
	 * Initializes a new MBrainzLookup instance. Each request needs to include contact
	 * information so MusicBrainz can contact the application maintainers if necessary.
	 * 
	 * @param appName	name of the application
	 * @param version	version of the application
	 * @param contact	email or url for contacting application maintainers
	 */
	public MBrainzLookup(String appName, String version, String contact) {
		super(appName, version, contact);
	}
	
	/**
	 * Returns artist information and results for all possible artist subqueries.
	 * @param mbid	MusicBrainz id for artist
	 * @return 		String in JSON or XML format
	 */
	public String getArtist(String mbid) {
		return doLookup(Entity.ARTIST, mbid, 
				Inc.RECORDINGS, Inc.RELEASES, Inc.RELEASE_GROUPS, Inc.WORKS);
	}
	
	/**
	 * Returns collection information and results for all possible collection subqueries.
	 * @param mbid	MusicBrainz id for collection
	 * @return 		String in JSON or XML format
	 */
	public String getCollection(String mbid) {
		return doLookup(Entity.COLLECTION, mbid,
				Inc.USER_COLLECTIONS);
	}
	
	/**
	 * Returns label information and results for all possible label subqueries.
	 * @param mbid	MusicBrainz id for label
	 * @return 		String in JSON or XML format
	 */
	public String getLabel(String mbid) {
		return doLookup(Entity.LABEL, mbid,
				Inc.RELEASES);
	}
	
	/**
	 * Returns recording information and results for all possible recording subqueries.
	 * @param mbid	MusicBrainz id for recording
	 * @return 		String in JSON or XML format
	 */
	public String getRecording(String mbid) {
		return doLookup(Entity.RECORDING, mbid,
				Inc.ARTISTS, Inc.RELEASES);
	}
	
	/**
	 * Returns release information and results for all possible release subqueries.
	 * @param mbid	MusicBrainz id for release
	 * @return 		String in JSON or XML format
	 */
	public String getRelease(String mbid) {
		return doLookup(Entity.RELEASE, mbid, 
				Inc.ARTISTS, Inc.COLLECTIONS, Inc.LABELS, Inc.RECORDINGS, Inc.RELEASE_GROUPS);
	}
	
	/**
	 * Returns release-group information and results for all possible release-group subqueries.
	 * @param mbid	MusicBrainz id for release-group
	 * @return 		String in JSON or XML format
	 */
	public String getReleaseGroup(String mbid) {
		return doLookup(Entity.RELEASE_GROUP, mbid,
				Inc.ARTISTS, Inc.RELEASES);
	}

	/**
	 * Returns value for a MusicBrainz lookup.
	 * @param entity	Entity in MusicBrainz database
	 * @param mbid		MusicBrainz id for the entity
	 * @return 			String in JSON or XML format
	 */
	public String doLookup(Entity entity, String mbid) {
		return doLookup(entity, mbid, (Inc[]) null);
	}
	
	/**
	 * Returns value for MusicBrainz lookup.
	 * @param entity	Entity in MusicBrainz database
	 * @param mbid		MusicBrainz id for the entity
	 * @param incs		Subqueries included in the lookup
	 * @return 			String in JSON or XML format
	 */
	public String doLookup(Entity entity, String mbid, Inc... incs) {
		String paramString = String.format("%s/%s", entity.toString(), mbid);
		if (incs != null) {
			paramString += "?inc=";
			List<Inc> incList = Arrays.asList(incs);
			paramString += incList.stream()
					.map(Inc::toString)
					.collect(Collectors.joining("+"));
		}
		return doGETRequest(paramString);
	}
}
