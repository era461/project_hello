<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function linkPage(index){
		alert(index);
		document.frm.action="/paging.do";
		document.frm.pageIndex.value=index;
		document.frm.submit();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form name="frm">
		<input type="hidden" name="pageIndex" value="${searchVO.pageIndex}">
	</form>
	<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/>
</body>
</html>