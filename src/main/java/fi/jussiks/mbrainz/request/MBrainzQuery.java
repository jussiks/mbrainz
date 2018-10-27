package fi.jussiks.mbrainz.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;
import fi.jussiks.mbrainz.util.LuceneQueryBuilder;

/**
 * Provides methods for querying the MusicBrainz database.
 * 
 * @author jussi
 */
public class MBrainzQuery extends MBrainzRequestImpl {
	private LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder();
	
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
	 * Initializes a new MBrainzQuery instance. Each request needs to include contact
	 * information so MusicBrainz can contact the application maintainers if necessary.
	 * 
	 * @param appName	name of the application
	 * @param version	version of the application
	 * @param contact	email or url for contacting application maintainers
	 * @param entity	entity that will be queried
	 */
	public MBrainzQuery(String appName, String version, String contact, Entity entity) {
		super(appName, version, contact, entity);
	}
	
	/**
	 * Initializes a new MBrainzQuery instance. Each request needs to include contact
	 * information so MusicBrainz can contact the application maintainers if necessary.
	 * 
	 * @param appName	name of the application
	 * @param version	version of the application
	 * @param contact	email or url for contacting application maintainers
	 * @param entity	entity that will be queried
	 * @param incs		subqueries to be included in the query
	 */
	public MBrainzQuery(String appName, String version, String contact, Entity entity, Inc...incs) {
		super(appName, version, contact, entity, incs);
	}
	
	/**
	 * Adds new Lucene query parameter to the query.
	 * @param field		field used in query
	 * @param value		value of field
	 */
	public void addQuery(String field, String value) {
		queryBuilder.addQuery(field, value);
	}
	
	/**
	 * Adds new Lucene query parameter to the query.
	 * @param field		field used in query
	 * @param value		value of field
	 * @param occur		boolean clause that determines how the search term should be
	 * 					added to the query (for example MUST or SHOULD)
	 */
	public void addQuery(String field, String value, BooleanClause.Occur occur) {
		queryBuilder.addQuery(field, value, occur);
	}
	
	/**
	 * Performs query on MusicBrainz entity. 
	 * @param entity	MusicBrainz entity
	 * @param query		Lucene query
	 * @return 			results as String
	 */
	public String doRequest(Entity entity, Query query) {
		return doRequest(entity, query, (Inc[]) null);
	}
	
	/**
	 * Performs query on MusicBrainz entity.
	 * @param entity	MusicBrainz entity
	 * @param query		Lucene query
	 * @param incs		included subqueries
	 * @return 			results as String
	 */
	public String doRequest(Entity entity, Query query, Inc... incs) {
		queryBuilder = new LuceneQueryBuilder();
		queryBuilder.addQuery(query);
		super.entity = entity;
		super.incs = incs;
		return super.doRequest();
	}
	
	/**
	 * Performs query on MusicBrainz entity.
	 * @param entity		MusicBrainz entity
	 * @param params		Lucene formatted String of query parameters
	 * @return 				results as String
	 * @throws ParseException 
	 */
	public String doRequest(Entity entity, String params) throws ParseException {
		return doRequest(entity, params, (Inc[]) null);
	}
	
	/**
	 * Performs query on MusicBrainz entity.
	 * @param entity	MusicBrainz entity
	 * @param query		Lucene formatted String of query parameters
	 * @param incs		included subqueries
	 * @return 			results as String
	 * @throws ParseException 
	 */
	public String doRequest(Entity entity, String query, Inc... incs) throws ParseException {
		super.entity = entity;
		super.incs = incs;

		queryBuilder = new LuceneQueryBuilder();	
		queryBuilder.addQuery(query);
		return super.doRequest();
	}

	/**
	 * @return query as String
	 */
	public String getQuery() {
		return queryBuilder.toString();
	}

	@Override
	/**
	 * @return query parameters in URL encoded String
	 */
	public String getParameters() {
		if (super.entity == null)
			return null;
		try {
			return String.format("%s?query=%s", super.entity.toString(), URLEncoder.encode(getQuery(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}