package com.estool.facets;

import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;

public class FacetsBuilders {

	public static FacetBuilder termFacets(String key, String field, int size) {
		return FacetBuilders.termsFacet(key).field(field).size(size);
	}

	public static FacetBuilder TermsStatsFacet(String name, String key, String field, int size) {
		return FacetBuilders.termsStatsFacet(name).keyField(key).valueField(field).size(size);
	}

}
