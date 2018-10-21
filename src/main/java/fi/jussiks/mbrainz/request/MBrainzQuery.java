package fi.jussiks.mbrainz.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;

import fi.jussiks.mbrainz.enums.Entity;
import fi.jussiks.mbrainz.enums.Inc;

/**
 * Provides methods for querying the MusicBrainz database.
 * 
 * @author jussi
 */
public class MBrainzQuery extends MBrainzRequestImpl {
	private Builder queryBuilder = new BooleanQuery.Builder();
	private Entity entity;
	private Inc[] incs;
	
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
	 * Adds new Lucene query parameter to the query.
	 * @param field		field used in query
	 * @param value		value of field
	 */
	public void addQuery(String field, String value) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Query query;
		try {
			query = new QueryParser(field, analyzer).parse(value);
			queryBuilder.add(query, BooleanClause.Occur.MUST);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Performs query on MusicBrainz entity. Throws NullPointerException if entity has
	 * not been set.
	 */
	public String doRequest() {		
		if (entity == null)
			throw new NullPointerException("Entity must be defined.");
		Query query = queryBuilder.build();
		return doRequest(entity, query, incs);
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
		return doRequest(entity, query.toString(), incs);
	}
	
	/**
	 * Performs query on MusicBrainz entity.
	 * @param entity	MusicBrainz entity
	 * @param query		Lucene formatted String 
	 * @return 			results as String
	 */
	public String doRequest(Entity entity, String params) {
		return doRequest(entity, params, (Inc[]) null);
	}
	
	/**
	 * Performs query on MusicBrainz entity.
	 * @param entity	MusicBrainz entity
	 * @param query		Lucene formatted String 
	 * @param incs		included subqueries
	 * @return 			results as String
	 */
	public String doRequest(Entity entity, String queryParams, Inc... incs) {
		String params = "";
		try {
			params = String.format("%s?query=%s", entity.toString(), URLEncoder.encode(queryParams, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doRequest(params, incs);
	}

	/**
	 * @return MusicBrainz entity used in query
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param entity 	 MusicBrainz entity used in query
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return subqueries included in query
	 */
	public Inc[] getIncs() {
		return incs;
	}

	/**
	 * @param incs	 subqueries included in query
	 */
	public void setIncs(Inc[] incs) {
		this.incs = incs;
	}

	/**
	 * @return query parameters as String
	 */
	public String getQueryParameters() {
		return queryBuilder.build().toString();
	}
}