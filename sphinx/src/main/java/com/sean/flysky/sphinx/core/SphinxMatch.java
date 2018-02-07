/*
 * $Id: SphinxMatch.java 1172 2008-02-24 13:50:48Z shodan $
 */

package com.sean.flysky.sphinx.core;

import java.util.ArrayList;

/**
 * Matched document information, as in search result.
 */
public class SphinxMatch
{
	/** Matched document ID. */
	public long		docId;

	/** Matched document weight. */
	public int			weight;

	/** Matched document attribute values. */
	public ArrayList	attrValues;


	/** Trivial constructor. */
	public SphinxMatch ( long docId, int weight )
	{
		this.docId = docId;
		this.weight = weight;
		this.attrValues = new ArrayList();
	}
}

/*
 * $Id: SphinxMatch.java 1172 2008-02-24 13:50:48Z shodan $
 */
