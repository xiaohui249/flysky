/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.estool.query;

import java.io.Serializable;

import org.elasticsearch.common.Required;

public class MoreLikeThis implements Serializable {

	private static final long serialVersionUID = -7414615958567184841L;

	private String index;

	private String type;

	private String id;

	private String routing;

	private String[] fields;

	private float percentTermsToMatch = -1;
	private int minTermFreq = -1;
	private int maxQueryTerms = -1;
	private String[] stopWords = null;
	private int minDocFreq = -1;
	private int maxDocFreq = -1;
	private int minWordLen = -1;
	private int maxWordLen = -1;
	private float boostTerms = -1;

	private int searchSize = 0;
	private int searchFrom = 0;
	private String[] searchIndices;
	private String[] searchTypes;

	MoreLikeThis() {
	}

	/**
	 * Constructs a new more like this request for a document that will be fetch
	 * from the provided index. Use {@link #type(String)} and
	 * {@link #id(String)} to specify the document to load.
	 */
	public MoreLikeThis(String index) {
		this.index = index;
	}

	/**
	 * The index to load the document from which the "like" query will run with.
	 */
	public String index() {
		return index;
	}

	/**
	 * The type of document to load from which the "like" query will rutn with.
	 */
	public String type() {
		return type;
	}

	void index(String index) {
		this.index = index;
	}

	/**
	 * The type of document to load from which the "like" query will execute
	 * with.
	 */
	@Required
	public MoreLikeThis type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * The id of document to load from which the "like" query will execute with.
	 */
	public String id() {
		return id;
	}

	/**
	 * The id of document to load from which the "like" query will execute with.
	 */
	@Required
	public MoreLikeThis id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * @return The routing for this request. This used for the `get` part of the
	 *         mlt request.
	 */
	public String routing() {
		return routing;
	}

	public void routing(String routing) {
		this.routing = routing;
	}

	/**
	 * The fields of the document to use in order to find documents "like" this
	 * one. Defaults to run against all the document fields.
	 */
	public String[] fields() {
		return this.fields;
	}

	/**
	 * The fields of the document to use in order to find documents "like" this
	 * one. Defaults to run against all the document fields.
	 */
	public MoreLikeThis fields(String... fields) {
		this.fields = fields;
		return this;
	}

	/**
	 * The percent of the terms to match for each field. Defaults to
	 * <tt>0.3f</tt>.
	 */
	public MoreLikeThis percentTermsToMatch(float percentTermsToMatch) {
		this.percentTermsToMatch = percentTermsToMatch;
		return this;
	}

	/**
	 * The percent of the terms to match for each field. Defaults to
	 * <tt>0.3f</tt>.
	 */
	public float percentTermsToMatch() {
		return this.percentTermsToMatch;
	}

	/**
	 * The frequency below which terms will be ignored in the source doc.
	 * Defaults to <tt>2</tt>.
	 */
	public MoreLikeThis minTermFreq(int minTermFreq) {
		this.minTermFreq = minTermFreq;
		return this;
	}

	/**
	 * The frequency below which terms will be ignored in the source doc.
	 * Defaults to <tt>2</tt>.
	 */
	public int minTermFreq() {
		return this.minTermFreq;
	}

	/**
	 * The maximum number of query terms that will be included in any generated
	 * query. Defaults to <tt>25</tt>.
	 */
	public MoreLikeThis maxQueryTerms(int maxQueryTerms) {
		this.maxQueryTerms = maxQueryTerms;
		return this;
	}

	/**
	 * The maximum number of query terms that will be included in any generated
	 * query. Defaults to <tt>25</tt>.
	 */
	public int maxQueryTerms() {
		return this.maxQueryTerms;
	}

	/**
	 * Any word in this set is considered "uninteresting" and ignored.
	 * <p/>
	 * <p>
	 * Even if your Analyzer allows stopwords, you might want to tell the
	 * MoreLikeThis code to ignore them, as for the purposes of document
	 * similarity it seems reasonable to assume that
	 * "a stop word is never interesting".
	 * <p/>
	 * <p>
	 * Defaults to no stop words.
	 */
	public MoreLikeThis stopWords(String... stopWords) {
		this.stopWords = stopWords;
		return this;
	}

	/**
	 * Any word in this set is considered "uninteresting" and ignored.
	 * <p/>
	 * <p>
	 * Even if your Analyzer allows stopwords, you might want to tell the
	 * MoreLikeThis code to ignore them, as for the purposes of document
	 * similarity it seems reasonable to assume that
	 * "a stop word is never interesting".
	 * <p/>
	 * <p>
	 * Defaults to no stop words.
	 */
	public String[] stopWords() {
		return this.stopWords;
	}

	/**
	 * The frequency at which words will be ignored which do not occur in at
	 * least this many docs. Defaults to <tt>5</tt>.
	 */
	public MoreLikeThis minDocFreq(int minDocFreq) {
		this.minDocFreq = minDocFreq;
		return this;
	}

	/**
	 * The frequency at which words will be ignored which do not occur in at
	 * least this many docs. Defaults to <tt>5</tt>.
	 */
	public int minDocFreq() {
		return this.minDocFreq;
	}

	/**
	 * The maximum frequency in which words may still appear. Words that appear
	 * in more than this many docs will be ignored. Defaults to unbounded.
	 */
	public MoreLikeThis maxDocFreq(int maxDocFreq) {
		this.maxDocFreq = maxDocFreq;
		return this;
	}

	/**
	 * The maximum frequency in which words may still appear. Words that appear
	 * in more than this many docs will be ignored. Defaults to unbounded.
	 */
	public int maxDocFreq() {
		return this.maxDocFreq;
	}

	/**
	 * The minimum word length below which words will be ignored. Defaults to
	 * <tt>0</tt>.
	 */
	public MoreLikeThis minWordLen(int minWordLen) {
		this.minWordLen = minWordLen;
		return this;
	}

	/**
	 * The minimum word length below which words will be ignored. Defaults to
	 * <tt>0</tt>.
	 */
	public int minWordLen() {
		return this.minWordLen;
	}

	/**
	 * The maximum word length above which words will be ignored. Defaults to
	 * unbounded.
	 */
	public MoreLikeThis maxWordLen(int maxWordLen) {
		this.maxWordLen = maxWordLen;
		return this;
	}

	/**
	 * The maximum word length above which words will be ignored. Defaults to
	 * unbounded.
	 */
	public int maxWordLen() {
		return this.maxWordLen;
	}

	/**
	 * The boost factor to use when boosting terms. Defaults to <tt>1</tt>.
	 */
	public MoreLikeThis boostTerms(float boostTerms) {
		this.boostTerms = boostTerms;
		return this;
	}

	/**
	 * The boost factor to use when boosting terms. Defaults to <tt>1</tt>.
	 */
	public float boostTerms() {
		return this.boostTerms;
	}

	/**
	 * The indices the resulting mlt query will run against. If not set, will
	 * run against the index the document was fetched from.
	 */
	public MoreLikeThis searchIndices(String... searchIndices) {
		this.searchIndices = searchIndices;
		return this;
	}

	/**
	 * The indices the resulting mlt query will run against. If not set, will
	 * run against the index the document was fetched from.
	 */
	public String[] searchIndices() {
		return this.searchIndices;
	}

	/**
	 * The types the resulting mlt query will run against. If not set, will run
	 * against the type of the document fetched.
	 */
	public MoreLikeThis searchTypes(String... searchTypes) {
		this.searchTypes = searchTypes;
		return this;
	}

	/**
	 * The types the resulting mlt query will run against. If not set, will run
	 * against the type of the document fetched.
	 */
	public String[] searchTypes() {
		return this.searchTypes;
	}

	/**
	 * The number of documents to return, defaults to 10.
	 */
	public MoreLikeThis searchSize(int size) {
		this.searchSize = size;
		return this;
	}

	public int searchSize() {
		return this.searchSize;
	}

	/**
	 * From which search result set to return.
	 */
	public MoreLikeThis searchFrom(int from) {
		this.searchFrom = from;
		return this;
	}

	public int searchFrom() {
		return this.searchFrom;
	}
}
