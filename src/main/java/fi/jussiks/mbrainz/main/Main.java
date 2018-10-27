package fi.jussiks.mbrainz.main;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;
import fi.jussiks.mbrainz.enums.ResultFormat;
import fi.jussiks.mbrainz.request.MBrainzLookup;
import fi.jussiks.mbrainz.request.MBrainzQuery;

public class Main {
	private static String[] albumIds = {
			"5d227193-8f7e-4f6b-b9d1-2faa3624a5a6",
			"f6573119-4305-4af1-a99c-6286fa7876d0",
			"0859fb47-a805-40f0-917b-199425e80721",
			"927e2755-860d-47aa-8b75-bfd600c7040f",
			"a35f18e1-1435-4eb8-b344-ed1e88218436",
			"64d16be8-0039-4c74-86ed-4d16b61ed59b",
			"b4fffcb0-4723-4c91-8e09-bd76876d7e9a",
			"b3b992ab-9313-31f0-91ea-e1b8599830c5",
			"5be001e7-f75e-344c-8973-8c5fb1753caf",
			"1b105601-d2d3-4da2-a7d9-114f981b1766",
			"8094543e-de11-4f33-9c10-fb90056210a9",
			"f6010e25-d302-4848-9b07-53f715c363bb",
			"2c244bca-f6b0-408c-aa9c-0e3a693d8f5e",
			"6136bc33-1e9c-4e76-8c9f-2c71fcf1c82b"
	};
	private static String[] artistIds = {
			"5b11f4ce-a62d-471e-81fc-a69a8278c7da",
			"ed46b78e-dae1-4534-bc91-341066f35599",
			"ce6b102e-63f9-4618-a9cb-9731b5e37c4b",
			"2b798105-7746-4d2a-bbe4-531859bfa1a8",
			"f26c72d3-e52c-467b-b651-679c73d8e1a7"
	};
	private static Long[] barcodes = {
			853895007018L,
			6418547015533L,
			074646574720L,
			5099765157123L
	};
	
	/**
	 * For testing purposes.
	 * @param args
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws ParseException, MalformedURLException, UnsupportedEncodingException {
		JsonObject res = new JsonObject();
		JsonArray resArr = new JsonArray();
		JsonParser parser = new JsonParser();
		
		MBrainzLookup lookup = new MBrainzLookup("mbrainzclient", "0.1", "https://github.com/jussiks/mbrainz");
		lookup.setEntity(Entity.ARTIST);
		lookup.setMbid(artistIds[1]);
		lookup.setResultFormat(ResultFormat.JSON);
		//System.out.println(lookup.doRequest());
		
		/*
		MBrainzQuery mbq = new MBrainzQuery("mbrainzclient", "0.1", "https://github.com/jussiks/mbrainz");
		mbq.addQuery("artist", "rytmihäiriö");
		mbq.addQuery("country", "FI");
		mbq.setEntity(Entity.ARTIST);
		System.out.println(mbq.doRequest());
		*/
		
		//TODO: escape lucene chars
		MBrainzQuery mbq = new MBrainzQuery("mbrainzclient", "0.1", "https://github.com/jussiks/mbrainz");
		//System.out.println(mbq.doRequest(Entity.ARTIST, "artist:rytmihäiriö OR artist:\"kakka-hätä 77\"", Inc.RECORDINGS));
		mbq.setEntity(Entity.ARTIST);
		
		//TODO: this does not work
		mbq.addQuery("artist", "kakka-hätä 77");
		System.out.println(mbq.getParameters());
		System.out.println(mbq.getQuery());
		//System.out.println(mbq.doRequest());
		
		mbq = new MBrainzQuery(
				"mbrainzclient", "0.1", "https://github.com/jussiks/mbrainz",
				Entity.ARTIST);
		//System.out.println(mbq.doRequest(Entity.ARTIST, "artist:rytmihäiriö OR artist:\"kakka-hätä 77\"", Inc.RECORDINGS));
		
		//TODO: this does not work
		mbq.addQuery("artist", "queen", BooleanClause.Occur.SHOULD);
		mbq.addQuery("artist", "nirvana", BooleanClause.Occur.MUST);
		System.out.println(mbq.getParameters());
		System.out.println(mbq.getQuery());
		res = parser.parse(mbq.doRequest()).getAsJsonObject();
		resArr = res.get("artists").getAsJsonArray();
		
		for (JsonElement elem : resArr)
			System.out.println(elem.getAsJsonObject().get("name").getAsString());
		

	}
}