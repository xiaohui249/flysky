//package com.estool.query;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.index.query.BoolQueryBuilder;
////import org.elasticsearch.index.query.FieldQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.RangeQueryBuilder;
//import org.elasticsearch.index.query.TermQueryBuilder;
//import org.elasticsearch.index.query.TermsQueryBuilder;
//import org.elasticsearch.search.sort.SortBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//
//public class BoolQueryBuilders {
//
//	public static BoolQueryBuilder mustRangeQueryBuild(BoolQueryBuilder mustQuery, String field, int from, int to) {
//		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
//		if (from > to) {
//			return mustQuery;
//		} else if (from == 0 && to == 0) {
//			return mustQuery;
//		}
//		rangeQuery.from(from).includeLower(true);
//		if (to != 0) {
//			rangeQuery.to(to).includeUpper(true);
//		}
//		mustQuery.must(rangeQuery);
//		return mustQuery;
//	}
//
//	public static BoolQueryBuilder mustGreaterThanQueryBuild(BoolQueryBuilder mustQuery, String field, int from) {
//		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
//		rangeQuery.from(from).includeLower(false);
//		mustQuery.must(rangeQuery);
//		return mustQuery;
//	}
//
//	public static BoolQueryBuilder mustLessThanQueryBuild(BoolQueryBuilder mustQuery, String field, int to) {
//		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
//		rangeQuery.to(to).includeLower(false);
//		mustQuery.must(rangeQuery);
//		return mustQuery;
//	}
//
//	/**
//	 *
//	 * @param mustQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder mustFieldQueryBuild(BoolQueryBuilder mustQuery, String field, String query) {
//		if (StringUtils.isNotBlank(query)) {
//			FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
//			mustQuery.must(fieldQuery);
//		}
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder mustFieldQueryBuild(BoolQueryBuilder mustQuery, String field, boolean query) {
//		FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
//		mustQuery.must(fieldQuery);
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder mustFieldQueryBuild(BoolQueryBuilder mustQuery, String field, int query) {
//		FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
//		mustQuery.must(fieldQuery);
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param categoryId
//	 */
//	public static BoolQueryBuilder mustTermQueryBuild(BoolQueryBuilder mustQuery, String field, String categoryId) {
//		TermQueryBuilder termQuery = QueryBuilders.termQuery(field, categoryId);
//		mustQuery.must(termQuery);
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param categoryId
//	 */
//	public static BoolQueryBuilder mustNotTermQueryBuild(BoolQueryBuilder mustQuery, String field, String categoryId) {
//		TermQueryBuilder termQuery = QueryBuilders.termQuery(field, categoryId);
//		mustQuery.mustNot(termQuery);
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param values
//	 */
//	public static BoolQueryBuilder mustInQueryBuild(BoolQueryBuilder mustQuery, String field, String... values) {
//		TermsQueryBuilder termsQuery = QueryBuilders.inQuery(field, values);
//		mustQuery.must(termsQuery);
//		return mustQuery;
//	}
//
//	/**
//	 * @param mustQuery
//	 * @param field
//	 * @param values
//	 */
//	public static BoolQueryBuilder mustInQueryBuild(BoolQueryBuilder mustQuery, String field, int... values) {
//		TermsQueryBuilder termsQuery = QueryBuilders.inQuery(field, values);
//		mustQuery.must(termsQuery);
//		return mustQuery;
//	}
//
//	/**
//	 *
//	 * @param shouldQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder shouldFieldQueryBuild(BoolQueryBuilder shouldQuery, String field, String query) {
//		if (StringUtils.isNotBlank(query)) {
//			FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
//			shouldQuery.should(fieldQuery);
//		}
//		return shouldQuery;
//	}
//
//	/**
//	 *
//	 * @param shouldQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder shouldTermQueryBuild(BoolQueryBuilder shouldQuery, String field, String query) {
//		if (StringUtils.isNotBlank(query)) {
//			TermQueryBuilder fieldQuery = QueryBuilders.termQuery(field, query);
//			shouldQuery.should(fieldQuery);
//		}
//		return shouldQuery;
//	}
//
//	/**
//	 *
//	 * @param shouldQuery
//	 * @param field
//	 * @param query
//	 */
//	public static BoolQueryBuilder shouldFieldQueryBuild(BoolQueryBuilder shouldQuery, String field, int query) {
//		FieldQueryBuilder fieldQuery = QueryBuilders.fieldQuery(field, query);
//		shouldQuery.should(fieldQuery);
//		return shouldQuery;
//	}
//
//	/**
//	 *
//	 * @param sortField
//	 * @param order
//	 * @return
//	 */
//	public static List<SortBuilder> sortListBuild(String sortField, SortOrder order) {
//		List<SortBuilder> sorts = new ArrayList<SortBuilder>();
//		SortBuilder sort = SortBuilders.fieldSort(sortField).order(order);
//		sorts.add(sort);
//		return sorts;
//	}
//}
