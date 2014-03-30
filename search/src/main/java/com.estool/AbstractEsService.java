package com.estool;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.mlt.MoreLikeThisRequest;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estool.annotation.EsIndex;
import com.estool.annotation.IndexField;
import com.estool.exception.NotEsIndexException;
import com.estool.facets.FacetsGet;
import com.estool.facets.result.FacetsResult;
import com.estool.query.MoreLikeThis;
import com.estool.utils.HighlightUtils;
import com.estool.utils.page.Page;
import com.estool.utils.page.PageImpl;
import com.estool.utils.page.Pageable;
import com.estool.utils.page.Sort;
import com.estool.utils.page.Sort.Order;

/**
 * 对es搜索操作的主要封装类。主要功能是根据条件搜索es，然后把搜索结果转换成对应的实体bean
 */
public abstract class AbstractEsService<T> implements EsService<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractEsService.class);
	private static final String DEFAULT_ID_FIELD_NAME = "_id";
	private Class<T> clazz;
	private Field[] fields;

	private String indexName;
	private String[] indexTypes;

	protected Client client;

	public AbstractEsService() {
		init();

	}

	private void init() {
		try {
			// 获得T对应的class
			this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			;

			// 获得T里面的所有字段
			fields = clazz.getDeclaredFields();
			Field.setAccessible(fields, true);

			// 获得es索引注解，得到indexName和indexType
			EsIndex esIndex = clazz.getAnnotation(EsIndex.class);
			if (esIndex == null) {
				throw new NotEsIndexException();
			}
			// 获得T里面设置的索引名
			setIndexName(esIndex.indexName());
			// 获得T里面设置的索引类型
			setIndexTypes(esIndex.indexTypes());
		} catch (Exception e) {
			LOG.error("init error:", e);
		}
	}

	public SearchHits internalSearch(QueryBuilder query, int start, int size, List<SortBuilder> sorts, String... highlightFields) {
		return internalSearch(query, null, start, size, sorts, highlightFields);
	}

	public SearchHits internalSearch(QueryBuilder query, FilterBuilder filter, int start, int size, List<SortBuilder> sorts, String... highlightFields) {
		if (client == null) {
			LOG.error("Elasticsearch Client may not be null!");
		}
		SearchRequestBuilder searchRequest = client.prepareSearch(getIndexName()).setTypes(getIndexTypes());
		if (sorts != null) {
			for (SortBuilder sort : sorts) {
				searchRequest.addSort(sort);
			}
		}

		if (highlightFields != null) {
			for (String field : highlightFields) {
				searchRequest.addHighlightedField(field, 70).setHighlighterRequireFieldMatch(true);
			}
		}
		if (filter != null) {
			searchRequest.setPostFilter(filter);
		}
		searchRequest.setFrom(start).setSize(size).setQuery(query);
		SearchResponse searchResponse = searchRequest.execute().actionGet();
		SearchHits searchHits = searchResponse.getHits();
		return searchHits;
	}

	public FacetsResult searchFacets(QueryBuilder query, String key, FacetBuilder facets, int start, int size, List<SortBuilder> sorts, String... highlightFields) {
		FacetsResult result = new FacetsResult();
		if (client == null) {
			LOG.error("Elasticsearch Client may not be null!");
		}
		SearchRequestBuilder searchRequest = client.prepareSearch(getIndexName()).setTypes(getIndexTypes());
		if (sorts != null) {
			for (SortBuilder sort : sorts) {
				searchRequest.addSort(sort);
			}
		}
		if (highlightFields != null) {
			for (String field : highlightFields) {
				searchRequest.addHighlightedField(field, 70);
			}
		}
		searchRequest.setFrom(start).setSize(size).setQuery(query).addFacet(facets);
		SearchResponse searchResponse = searchRequest.execute().actionGet();
		result = new FacetsGet().getFacets(searchResponse, key, facets);
		return result;
	}

	public Page<T> search(QueryBuilder query, Pageable page) {
		return search(query, page, null);
	}

	public Page<T> search(QueryBuilder query, FilterBuilder filter, Pageable page) {
		return search(query, filter, page, null);
	}

	public Page<T> search(QueryBuilder query, Pageable page, String... highlightFields) {
		return search(query, null, page, highlightFields);
	}

	public Page<T> search(QueryBuilder query, FilterBuilder filter, Pageable page, String... highlightFields) {

		int start = page.getPageNumber();
		int size = page.getPageSize();
		List<SortBuilder> sorts = ParsePageSort(page.getSort());

		SearchHits searchHits = internalSearch(query, filter, start, size, sorts, highlightFields);

		SearchHit[] hits = searchHits.getHits();
		long total = searchHits.getTotalHits();
		Page<T> result = getPagingHits(hits, page, total);

		return result;
	}

	public List<T> search(QueryBuilder query, int size, List<SortBuilder> sorts) {
		SearchHits searchHits = internalSearch(query, 0, size, sorts);
		return getEntityList(searchHits.getHits());
	}

	public List<T> search(QueryBuilder query, int start, int size) {
		return search(query, null, start, size);
	}

	public List<T> search(QueryBuilder query, FilterBuilder filter, int start, int size) {
		SearchHits searchHits = internalSearch(query, filter, start, size, null);
		return getEntityList(searchHits.getHits());
	}

	public List<T> search(QueryBuilder query, int start, int size, List<SortBuilder> sorts) {
		SearchHits searchHits = internalSearch(query, start, size, sorts);
		return getEntityList(searchHits.getHits());
	}

	public List<T> search(QueryBuilder query, int start, int size, List<SortBuilder> sorts, String... highlight) {
		SearchHits searchHits = internalSearch(query, start, size, sorts, highlight);
		return getEntityList(searchHits.getHits());
	}

	public long count(QueryBuilder query) {
		CountResponse response = client.prepareCount(getIndexName()).setTypes(getIndexTypes()).setQuery(query).execute().actionGet();
		return response.getCount();
	}

	public long count() {
		CountResponse response = client.prepareCount(getIndexName()).setTypes(getIndexTypes()).execute().actionGet();
		return response.getCount();
	}

	public T get(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		GetResponse response = client.prepareGet(getIndexName(), getIndexType(), id).execute().actionGet();
		return constructEntity(response.getId(), response.getSource());
	}

	public List<List<T>> multiSearch(SearchRequestBuilder[] requests) {
		List<List<T>> result = new ArrayList<List<T>>();
		MultiSearchRequestBuilder builder = client.prepareMultiSearch();
		for (SearchRequestBuilder request : requests) {
			builder.add(request);
		}
		MultiSearchResponse response = builder.execute().actionGet();
		Item[] items = response.getResponses();
		for (Item item : items) {
			if (!item.isFailure()) {
				SearchResponse sResponse = item.getResponse();
				List<T> entities = getEntityList(sResponse.getHits().hits());
				result.add(entities);
			}
		}
		return result;
	}

	public Map<String, T> multiGetToMap(String[] ids) {
		Map<String, T> entities = new HashMap<String, T>();
		MultiGetResponse response = client.prepareMultiGet().add(getIndexName(), getIndexType(), ids).execute().actionGet();
		MultiGetItemResponse[] responses = response.getResponses();
		for (MultiGetItemResponse result : responses) {
			if (!result.isFailed()) {
				GetResponse getResponse = result.getResponse();

				String id = getResponse.getId();
				T entity = constructEntity(id, getResponse.getSource());
				entities.put(id, entity);
			}
		}
		return entities;
	}

	public List<T> multiGetToList(String[] ids) {
		List<T> entities = new ArrayList<T>();
		MultiGetResponse response = client.prepareMultiGet().add(getIndexName(), getIndexType(), ids).execute().actionGet();
		MultiGetItemResponse[] responses = response.getResponses();
		for (MultiGetItemResponse result : responses) {
			if (!result.isFailed()) {
				GetResponse getResponse = result.getResponse();

				String id = getResponse.getId();
				Map<String, Object> source = getResponse.getSource();
				if (source != null) {
					T entity = constructEntity(id, source);
					entities.add(entity);
				}
			}
		}
		return entities;
	}

	public List<T> moreLikeThisQuery(MoreLikeThis mlt) {
		MoreLikeThisRequest request = new MoreLikeThisRequest(mlt.index());
		request.boostTerms(mlt.boostTerms());
		request.fields(mlt.fields());
		request.id(mlt.id());
		request.type(mlt.type());
		request.maxDocFreq(mlt.maxDocFreq());
		request.maxQueryTerms(mlt.maxQueryTerms());
		request.maxWordLength(mlt.maxWordLen());
		request.minDocFreq(mlt.minDocFreq());
		request.minWordLength(mlt.maxWordLen());
		request.minTermFreq(mlt.minTermFreq());
		request.percentTermsToMatch(mlt.percentTermsToMatch());
		request.stopWords(mlt.stopWords());
		request.searchFrom(mlt.searchFrom());
		request.searchSize(mlt.searchSize());
		request.routing(mlt.routing());
		request.searchIndices(mlt.searchIndices());
		request.searchTypes(mlt.searchTypes());

		SearchResponse response = client.moreLikeThis(request).actionGet();
		SearchHits searchHits = response.getHits();
		return getEntityList(searchHits.getHits());
	}

	public void deleteById(String id) {
		DeleteResponse response = client.prepareDelete(getIndexName(), getIndexType(), id).execute().actionGet();
		LOG.info("Delete " + getIndexName() + " " + id);
	}

	public boolean deleteByIds(List<String> ids) {
		BulkRequestBuilder bulk = client.prepareBulk();
		for (String id : ids) {
			bulk.add(new DeleteRequest(getIndexName(), getIndexType(), id));
			LOG.info("Delete " + getIndexName() + " " + id);
		}
		if (bulk.numberOfActions() > 0) {
			BulkResponse response = bulk.execute().actionGet();
			if (response.hasFailures()) {
				LOG.error(response.buildFailureMessage());
				return false;
			}
		}
		return true;
	}

	public boolean addByBean(T bean) {
		return index(entityToMap(bean));
	}

	public boolean addByBeans(List<T> beans) {
		BulkRequestBuilder bulk = client.prepareBulk();
		for (T t : beans) {
			Map<String, Object> source = entityToMap(t);
			String id = (String) source.get("id");
			bulk.add(new IndexRequest(getIndexName(), getIndexType(), id).source(source));
		}
		if (bulk.numberOfActions() > 0) {
			BulkResponse response = bulk.execute().actionGet();
			if (response.hasFailures()) {
				LOG.error(response.buildFailureMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * index a document to elasticsearch
	 *
	 * @param doc
	 * @return
	 */
	public boolean index(Map<String, Object> doc) {
		try {
			String id = (String) doc.get("id");
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(getIndexName(), getIndexType(), id);
			indexRequestBuilder.setSource(doc);
			IndexResponse response = indexRequestBuilder.execute().actionGet();
			String indexedId = response.getId();
			if (StringUtils.isNotBlank(indexedId)) {
				return true;
			}
		} catch (Exception e) {
			LOG.error("add index error", e);
			return false;
		}
		return false;
	}

	public Map<String, Object> entityToMap(T entity) {
		Field[] superfields = clazz.getSuperclass().getDeclaredFields();
		Field[] fields = clazz.getDeclaredFields();

		Map<String, Object> doc = new HashMap<String, Object>();
		setValue(superfields, doc, entity);
		setValue(fields, doc, entity);
		return doc;
	}

	private void setValue(Field[] fields, Map<String, Object> doc, T entity) {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				IndexField field = fields[i].getAnnotation(IndexField.class);
				if (field != null) {
					String fieldName = field.value();
					Object value = fields[i].get(entity);
					if (value != null) {
						doc.put(fieldName, value);
					}
				}
			} catch (Exception e) {
				LOG.error("setValue errors:", e);

			}

		}

	}

	/**
	 * @param sort
	 * @return
	 */
	private List<SortBuilder> ParsePageSort(Sort sort) {
		List<SortBuilder> sorts = null;
		if (sort != null) {
			sorts = new ArrayList<SortBuilder>();
			Iterator<Order> orders = sort.iterator();
			while (orders.hasNext()) {
				Order order = orders.next();
				SortBuilder esSort = buildSort(order.getProperty(), order.getDirection().toString());
				sorts.add(esSort);
			}
		}
		return sorts;
	}

	private Page<T> getPagingHits(SearchHit[] hits, Pageable pageable, long total) {
		List<T> results = getEntityList(hits);
		Page<T> page = new PageImpl<T>(results, pageable, total);
		return page;
	}

	private List<T> getEntityList(SearchHit[] hits) {
		List<T> entityList = new ArrayList<T>();
		for (SearchHit hit : hits) {
			try {
				T entity = constructEntity(hit);
				entityList.add(entity);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entityList;

	}

	private T constructEntity(SearchHit hit) {
		return constructEntity(hit.getId(), hit.getSource(), hit.getHighlightFields());
	}

	private T constructEntity(String id, Map<String, Object> source) {
		return constructEntity(id, source, null);
	}

	/**
	 * @param id
	 * @param source
	 * @param highlightFields
	 */
	private T constructEntity(String id, Map<String, Object> source, Map<String, HighlightField> highlightFields) {
		try {
			Object obj = clazz.newInstance();

			for (int k = 0; k < fields.length; k++) {

				boolean isDefineField = fields[k].isAnnotationPresent(IndexField.class);
				if (isDefineField) {
					IndexField actualField = fields[k].getAnnotation(IndexField.class);
					String fieldName = actualField.value();

					HighlightField highlightField = null;

					if (highlightFields != null) {
						highlightField = highlightFields.get(fieldName);
					}

					Object value;
					if (highlightField != null) {
						value = HighlightUtils.textToString(highlightField.getFragments());
					} else {
						value = source.get(fieldName);
					}
					if (value == null) {
						value = "";
					}
					setEntityField(obj, fields[k], value.toString());
				}
			}
			return (T) obj;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("constructEntity error:", e);
			return null;
		}

	}

	private void setEntityField(Object obj, Field field, String value) {

		try {
			String fieldType = field.getType().toString();

			if (fieldType.equals("byte")) {
				field.setByte(obj, Byte.parseByte(value));
			} else if (fieldType.equals("boolean")) {
				field.setBoolean(obj, Boolean.parseBoolean(value));
			} else if (fieldType.equals("short")) {
				field.setShort(obj, Short.parseShort(value));
			} else if (fieldType.equals("int")) {
				field.setInt(obj, StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value));
			} else if (fieldType.equals("class java.lang.Integer")) {
				field.set(obj, StringUtils.isEmpty(value) ? Integer.valueOf(0) : Integer.valueOf(value));
			} else if (fieldType.equals("long")) {
				field.setLong(obj, Long.parseLong(value));
			} else if (fieldType.equals("float")) {
				field.setFloat(obj, Float.parseFloat(value));
			} else if (fieldType.equals("double")) {
				field.setDouble(obj, Double.parseDouble(value));
			} else if (fieldType.endsWith("Date")) {// change es datetime to
													// date
				DateTime dateTime = ISODateTimeFormat.dateOptionalTimeParser().parseDateTime(value.toString());
				long millis = dateTime.getMillis();
				Date date = new Date(millis);
				field.set(obj, date);
			} else {
				field.set(obj, value);
			}

		} catch (Exception e) {
			LOG.error("setEntityField error:", e);
		}

	}

	/**
	 * Note: if the order value not match asc or desc, desc will be use.
	 * 
	 * @param field
	 *            field name. specify the field to be sort
	 * @param order
	 *            asc or desc
	 * @return
	 */
	public static SortBuilder buildSort(String field, String order) {
		SortBuilder sort = SortBuilders.fieldSort(field);
		if ("asc".equalsIgnoreCase(order)) {
			sort.order(SortOrder.ASC);
		} else {
			sort.order(SortOrder.DESC);
		}
		return sort;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Client getClient() {
		return client;
	}

	public String getIndexName() {
		return indexName;
	}

	/**
	 * 默认返回第一个indextype，如果indextypes个数为零，返回indexName
	 * 
	 * @return
	 */
	public String getIndexType() {
		if (indexTypes.length > 0) {
			return indexTypes[0];
		}
		return getIndexName();
	}

	public String[] getIndexTypes() {
		return indexTypes;
	}

	protected void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	protected void setIndexTypes(String[] indexTypes) {
		this.indexTypes = indexTypes;
	}

	protected abstract void setClient(Client client);

}
