package fi.jussiks.mbrainz.enums;

public enum Entity {
	AREA, ARTIST, EVENT, LABEL, PLACE, RECORDING, 
	RELEASE, RELEASE_GROUP, SERIES, URL, WORK,
	COLLECTION;
	
	public String toString() {
		return this.name().replaceAll("_", "-").toLowerCase();
	}
}

