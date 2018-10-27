package fi.jussiks.mbrainz.util;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanQuery.Builder;

/**
 * Builds a Lucene query.
 * 
 * @author jussi
 * TODO: searching for exact phrase (for example "david bowie") only works when
 * 			query is added in String format. Needs to fixed
 */
public class LuceneQueryBuilder {
	private Builder queryBuilder;
	
	/**
	 * Initializes a new LuceneQueryBuilder.
	 */
	public LuceneQueryBuilder() {
		queryBuilder = new BooleanQuery.Builder();
	}
	
	/**
	 * Adds new query to the LuceneQueryBuilder.
	 * @param field		field to be queried
	 * @param value		value of the field
	 */
	public void addQuery(String field, String value) {
		addQuery(field, value, BooleanClause.Occur.MUST);
	}
	
	/**	 
	 * Adds new query to the LuceneQueryBuilder.
	 * @param field		field to be queried
	 * @param value		value of the field
	 * @param occur		boolean clause (for example MUST or SHOULD)
	 */
	public void addQuery(String field, String value, BooleanClause.Occur occur) {
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		String esc_field = QueryParser.escape(field);
		String esc_value = QueryParser.escape(value);

		System.out.println(esc_value);
		QueryParser parser = new QueryParser(esc_field, analyzer);
		parser.setSplitOnWhitespace(false);
		
		try {
			Query q = parser.parse(esc_value);
			addQuery(q, occur);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds new query to the LuceneQueryBuilder.
	 * @param query		Lucene formatted String
	 */
	public void addQuery(String query) {
		addQuery(query, BooleanClause.Occur.MUST);
	}
	
	/**
	 * Adds new query to the LuceneQueryBuilder.
	 * @param query		Lucene formatted String
	 * @param occur		boolean clause (for example MUST or SHOULD)
	 */
	public void addQuery(String query, BooleanClause.Occur occur) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		String esc_query = QueryParser.escape(query);
		
		try {
			Query q = new QueryParser(esc_query, analyzer).parse(esc_query);
			addQuery(q, occur);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds new query to the LuceneQueryBuilder.
	 * @param query		org.apache.lucene.search.Query object
	 */
	public void addQuery(Query query) {
		addQuery(query, BooleanClause.Occur.MUST);
	}
	
	/**
	 * Adds new query to the LuceneQueryBuilder.
	 * @param query		org.apache.lucene.search.Query object
	 * @param occur		boolean clause (for example MUST or SHOULD)
	 */
	public void addQuery(Query query, BooleanClause.Occur occur) {
		queryBuilder.add(query, occur);
	}
	
	/**
	 * @return query as a String
	 */
	public String toString() {
		return queryBuilder.build().toString();
	}
}
