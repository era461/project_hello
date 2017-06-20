package com.project.hello.common;

/**
 * 4DEPTH
 * @author YOONHWAN
 *
 */
public class DefaultVO {
	/** 현제 페이지 번호 */
	private int pageIndex = 1;
	
	/**  한 블럭 페이지 사이즈 */
	private int pageSize = 10;
	
	/** 첫번째 인덱스 */
	private int fisrtIndex = 1;
	
	/** 마지막 인덱스 */
	private int lastIndex = 1;
	
	/** 한 페이지에 게시되는 게시물 건수 */
	private int recordCountPerPage = 10;
	
	/** 검색조건 */
	private String searchCondition = "";

	/** 검색조건2 */
	private String searchCondition2 = "";

	/** 검색조건3 */
	private String searchCondition3 = "";
	
	/** 검색 키워드 */
	private String searchKeyword = "";
	
	/** 검색 키워드 */
	private String searchKeyword2 = "";
	
	/** 검색 키워드 */
	private String searchKeyword3 = "";
	
	/** 조회기간 시작일 */
	private String sCalendar = "";
	
	/** 조회기간 종료일 */
	private String eCalendar = "";

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFisrtIndex() {
		return fisrtIndex;
	}

	public void setFisrtIndex(int fisrtIndex) {
		this.fisrtIndex = fisrtIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchCondition2() {
		return searchCondition2;
	}

	public void setSearchCondition2(String searchCondition2) {
		this.searchCondition2 = searchCondition2;
	}

	public String getSearchCondition3() {
		return searchCondition3;
	}

	public void setSearchCondition3(String searchCondition3) {
		this.searchCondition3 = searchCondition3;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchKeyword2() {
		return searchKeyword2;
	}

	public void setSearchKeyword2(String searchKeyword2) {
		this.searchKeyword2 = searchKeyword2;
	}

	public String getSearchKeyword3() {
		return searchKeyword3;
	}

	public void setSearchKeyword3(String searchKeyword3) {
		this.searchKeyword3 = searchKeyword3;
	}

	public String getsCalendar() {
		return sCalendar;
	}

	public void setsCalendar(String sCalendar) {
		this.sCalendar = sCalendar;
	}

	public String geteCalendar() {
		return eCalendar;
	}

	public void seteCalendar(String eCalendar) {
		this.eCalendar = eCalendar;
	}
}