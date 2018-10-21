package fi.jussiks.mbrainz.request;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;

/**
 * MBrainzLookup performs direct lookups using MusicBrainz ids of entities.
 * 
 * @author jussi
 */
public class MBrainzLookup extends MBrainzRequestImpl {
	private Entity entity;
	private String mbid;
	private Inc[] incs;
	
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
	
	@Override
	/**
	 * Performs a MusicBrainz lookup. Throws NullPointer if entity or MusicBrainz id 
	 * have not been set.
	 * @return 		result as String
	 */
	public String doRequest() {
		if (entity == null || mbid == null)
			throw new NullPointerException("Entity and mbid must be defined.");
		return doRequest(entity, mbid, incs);
	}
	
	/**
	 * Performs lookup on an artist. Includes all possible subqueries for artist.
	 * @param mbid	MusicBrainz id for artist
	 * @return 		result as String
	 */
	public String getArtist(String mbid) {
		return doRequest(Entity.ARTIST, mbid, 
				Inc.RECORDINGS, Inc.RELEASES, Inc.RELEASE_GROUPS, Inc.WORKS);
	}
	
	/**
	 * Performs lookup on a collection. Includes all possible subqueries for collection.
	 * @param mbid	MusicBrainz id for collection
	 * @return 		result as String
	 */
	public String getCollection(String mbid) {
		return doRequest(Entity.COLLECTION, mbid,
				Inc.USER_COLLECTIONS);
	}
	
	/**
	 * Performs lookup on a label. Includes all possible subqueries for label.
	 * @param mbid	MusicBrainz id for label
	 * @return 		result as String
	 */
	public String getLabel(String mbid) {
		return doRequest(Entity.LABEL, mbid,
				Inc.RELEASES);
	}
	
	/**
	 * Performs lookup on a recording. Includes all possible subqueries for recording.
	 * @param mbid	MusicBrainz id for recording
	 * @return 		result as String
	 */
	public String getRecording(String mbid) {
		return doRequest(Entity.RECORDING, mbid,
				Inc.ARTISTS, Inc.RELEASES);
	}
	
	/**
	 * Performs lookup on a release. Includes all possible subqueries for release.
	 * @param mbid	MusicBrainz id for release
	 * @return 		result as String
	 */
	public String getRelease(String mbid) {
		return doRequest(Entity.RELEASE, mbid, 
				Inc.ARTISTS, Inc.COLLECTIONS, Inc.LABELS, Inc.RECORDINGS, Inc.RELEASE_GROUPS);
	}
	
	/**
	 * Performs lookup on a release-group. Includes all possible subqueries for release-group.
	 * @param mbid	MusicBrainz id for release-group
	 * @return 		result as String
	 */
	public String getReleaseGroup(String mbid) {
		return doRequest(Entity.RELEASE_GROUP, mbid,
				Inc.ARTISTS, Inc.RELEASES);
	}

	/**
	 * Performs MusicBrainz lookup on specified entity.
	 * @param entity	entity in MusicBrainz database
	 * @param mbid		MusicBrainz id for the entity
	 * @return 			result as String
	 */
	public String doRequest(Entity entity, String mbid) {
		return doRequest(entity, mbid, (Inc[]) null);
	}
	
	/**
	 * Performs MusicBrainz lookup on specified entity.
	 * @param entity	entity in MusicBrainz database
	 * @param mbid		MusicBrainz id for the entity
	 * @param incs		subqueries included in the lookup
	 * @return 			result as String
	 */
	public String doRequest(Entity entity, String mbid, Inc... incs) {
		String params = String.format("%s/%s", entity.toString(), mbid);
		return super.doRequest(params, incs);
	}

	/**
	 * @return value of entity used in lookup.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param entity 	value of entity used in lookup.
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return entity's id in MusicBrainz.
	 */
	public String getMbid() {
		return mbid;
	}

	/**
	 * @param mbid	entity's id in MusicBrainz
	 */
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	/**
	 * @return subqueries included in lookup
	 */
	public Inc[] getIncs() {
		return incs;
	}

	/**
	 * @param incs 	subqueries included in lookup
	 */
	public void setIncs(Inc[] incs) {
		this.incs = incs;
	}
}
