package fi.jussiks.mbrainz.enums;

public enum Inc {
	RECORDINGS, RELEASES, RELEASE_GROUPS, WORKS,
	USER_COLLECTIONS,
	ARTISTS, COLLECTIONS, LABELS;
	
	public String toString() {
		return this.name().replaceAll("_", "-").toLowerCase();
	}
}
