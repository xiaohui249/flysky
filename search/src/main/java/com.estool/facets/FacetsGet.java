package com.estool.facets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacetBuilder;

import com.estool.facets.result.FacetsResult;

public class FacetsGet {

	public FacetsResult getFacets(SearchResponse res, String key, FacetBuilder facets) {

		FacetsResult result = new FacetsResult();
		FacetsGet facetsGet = new FacetsGet();
		if (facets.getClass().equals(TermsFacetBuilder.class)) {
			result = facetsGet.termFacets(res, key);
		} else if (facets.getClass().equals(TermsStatsFacetBuilder.class)) {
			result = facetsGet.TermsStatsFacet(res, key);
		} else {
			result = null;
		}

		return result;
	}

	public FacetsResult termFacets(SearchResponse res, String key) {

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		FacetsResult result = new FacetsResult();
		TermsFacet f = (TermsFacet) res.getFacets().facetsAsMap().get(key);
		for (TermsFacet.Entry entry : f) {
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("term", entry.getTerm().toString());
			resultMap.put("count", String.valueOf(entry.getCount()));
			resultList.add(resultMap);
		}
		result.install(resultList, (int) f.getTotalCount());
		return result;
	}

	public FacetsResult TermsStatsFacet(SearchResponse res, String key) {

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		FacetsResult result = new FacetsResult();
		TermsStatsFacet f = (TermsStatsFacet) res.getFacets().facetsAsMap().get(key);
		int maxnum = 0;
		for (TermsStatsFacet.Entry entry : f) {
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("term", entry.getTerm().toString());
			resultMap.put("count", String.valueOf(entry.getCount()));
			resultList.add(resultMap);
			maxnum++;
		}
		result.install(resultList, maxnum);
		return result;
	}

}
