package com.project.hello.common.pagination;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

public class ImagePaginationRenderer extends AbstractPaginationRenderer{
	
	/**
	 * 2017. 06. 19
	 * YOONHWAN
	 * 페이징 UI 영역 onclick{0} 자바스크립트 함수명
	 * 				  onclick{1} 자바스크립트 매개인자
	 * 페이징 로직 변경 필요시 AbstractPaginationRenderer클래스의 renderPagination메서드 오버라이딩
	 * 버튼 이미지 변경 필요시 easycompany/images/bt_first.gif 수정
	 */
	public ImagePaginationRenderer() {
		firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\"><image src=\"/easycompany/images/bt_first.gif\" border=0/></a>&#160;"; 
		previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\"><image src=\"/easycompany/images/bt_prev.gif\" border=0/></a>&#160;";
		currentPageLabel = "<strong>{0}</strong>&#160;";
		otherPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
		nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\"><image src=\"/easycompany/images/bt_next.gif\" border=0/></a>&#160;";
		lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\"><image src=\"/easycompany/images/bt_last.gif\" border=0/></a>&#160;";
	}
}
