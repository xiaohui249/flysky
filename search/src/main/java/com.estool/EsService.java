package com.estool;

import com.estool.facets.result.FacetsResult;
import com.estool.query.MoreLikeThis;
import com.estool.utils.page.Page;
import com.estool.utils.page.Pageable;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;
import java.util.Map;

public interface EsService<T> {

	public Page<T> search(QueryBuilder query, Pageable page);

	public Page<T> search(QueryBuilder query, FilterBuilder filter, Pageable page);

	public Page<T> search(QueryBuilder query, Pageable page, String... highlightFields);

	public Page<T> search(QueryBuilder query, FilterBuilder filter, Pageable page, String... highlightFields);

	public List<T> search(QueryBuilder query, int size, List<SortBuilder> sorts);

	public List<T> search(QueryBuilder query, int start, int size);

	public List<T> search(QueryBuilder query, FilterBuilder filter, int start, int size);

	public List<T> search(QueryBuilder query, int start, int size, List<SortBuilder> sorts);

	public FacetsResult searchFacets(QueryBuilder query, String key, FacetBuilder facets, int start, int size, List<SortBuilder> sorts, String... highlightFields);

	public List<List<T>> multiSearch(SearchRequestBuilder[] requests);

	public long count(QueryBuilder query);

	public long count();

	public T get(String id);

	public Map<String, T> multiGetToMap(String[] ids);

	public List<T> multiGetToList(String[] ids);

	public List<T> moreLikeThisQuery(MoreLikeThis request);

	public void deleteById(String id);

	public boolean deleteByIds(List<String> ids);

	public boolean addByBean(T entity);

	public boolean addByBeans(List<T> entity);

}
