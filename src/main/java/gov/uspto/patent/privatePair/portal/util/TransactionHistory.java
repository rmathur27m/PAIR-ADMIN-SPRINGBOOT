package gov.uspto.patent.privatePair.portal.util;

import java.util.List;

public class TransactionHistory {
	private Meta meta;
	private List<Results> results = null;
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public List<Results> getResults() {
		return results;
	}
	public void setResults(List<Results> results) {
		this.results = results;
	}
	
}
