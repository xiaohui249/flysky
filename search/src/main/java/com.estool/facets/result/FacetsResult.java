package com.estool.facets.result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FacetsResult implements Serializable {

	private int totalHits;
	private List<Map<String, String>> results;

	public void install(List<Map<String, String>> result, int totalHits) {
		this.results = result;
		this.totalHits = totalHits;
	}

	public int getTotalHits() {
		if (totalHits < 0)
			totalHits = 0;
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public List<Map<String, String>> getResults() {
		return results;
	}

	public void setResults(List<Map<String, String>> results) {
		this.results = results;
	}

}
