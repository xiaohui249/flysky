package com.estool.utils.page;

import java.io.Serializable;

import com.estool.utils.page.Sort.Direction;

/**
 * front search paging, start from page 0, maximum page is 99
 * 
 **/
public class PageRequest implements Pageable, Serializable {

	private static final long serialVersionUID = 8280485938848398236L;

	private int p = 0;
	private int s = 10;
	private Sort sort;

	public PageRequest() {

	}

	/**
	 * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing
	 * 0 for {@code page} will return the first page.
	 * 
	 * @param size
	 * @param page
	 */
	public PageRequest(int page, int size) {

		this(page, size, null);
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 * @param size
	 * @param direction
	 * @param properties
	 */
	public PageRequest(int page, int size, Direction direction, String... properties) {

		this(page, size, new Sort(direction, properties));
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 * @param size
	 * @param sort
	 */
	public PageRequest(int page, int size, Sort sort) {

		if (0 > page) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}

		if (0 >= size) {
			throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
		}

		this.p = page;
		this.s = size;
		this.sort = sort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getPageSize()
	 */
	public int getPageSize() {

		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getPageNumber()
	 */
	public int getPageNumber() {

		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getFirstItem()
	 */
	public int getOffset() {

		return p * s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getSort()
	 */
	public Sort getSort() {

		return sort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		boolean pEqual = this.p == that.p;
		boolean sEqual = this.s == that.s;

		boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);

		return pEqual && sEqual && sortEqual;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + p;
		result = 31 * result + s;
		result = 31 * result + (null == sort ? 0 : sort.hashCode());

		return result;
	}

	public int getPage() {
		return p;
	}

	public void setPage(int p) {
		this.p = p < 1 ? 0 : p;
		this.p = p > 99 ? 99 : p;
	}

	public void setP(int p) {
		this.p = p < 1 ? 0 : p;
		this.p = p > 99 ? 99 : p;
	}

	public int getSize() {
		return s;
	}

	public void setSize(int s) {
		this.s = s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

}
