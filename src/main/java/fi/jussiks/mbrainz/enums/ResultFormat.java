package fi.jussiks.mbrainz.enums;

public enum ResultFormat {
	JSON, XML;
	
	public String toString() {
		String resultFormat = "";
		switch (this) {
		case JSON:
			resultFormat = "application/json";
			break;
		case XML:
			resultFormat = "application/xml";
			break;
		}
		return resultFormat;
	}
}