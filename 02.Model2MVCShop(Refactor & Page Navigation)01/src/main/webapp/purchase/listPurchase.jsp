<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.util.*"  %>
<%@ page import="com.model2.mvc.service.domain.*" %>
<%@ page import="com.model2.mvc.common.*" %>
<%@ page import="com.model2.mvc.common.util.* "%>
<%
List<Purchase> list= (List<Purchase>)request.getAttribute("list");
Page resultPage=(Page)request.getAttribute("resultPage");

Search search = (Search)request.getAttribute("search");
//==> null �� ""(nullString)���� ����
String searchCondition = CommonUtil.null2str(search.getSearchCondition());
String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
%>




<html>
<head>
<title>���� �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetPurchaseList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">���� �����ȸ</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">��ü <%=resultPage.getTotalCount()%> �Ǽ�, ����<%=resultPage.getCurrentPage()%> ������</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ��ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��ȭ��ȣ</td>
		
		
		<td class="ct_line02"></td>
		<td class="ct_list_b">�����Ȳ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��������</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<%
	

	int no=list.size();
			for(int i=0; i<list.size(); i++) {
		Purchase purchase = (Purchase)list.get(i);
	%>	

	<tr class="ct_list_pop">
		<td align="center">
			<a href="/getPurchase.do?tranNo=<%=purchase.getTranNo()%>"><%=no--%></a>
		</td>
		<td></td>
		<td align="left">
			<a href="/getUser.do?userId=<%=purchase.getBuyer().getUserId()%>"><%=purchase.getBuyer().getUserId()%></a>
		</td>
		<td></td>
		<td align="left"><%= purchase.getReceiverName() %></td>
		<td></td>
		<td align="left"><%= purchase.getReceiverPhone() %></td>
			<td></td>
		<td align="left">����
				
			<% if(purchase.getTranCode().equals("000")){
			
			%>�Ǹ���
			
			<% }else if(purchase.getTranCode().equals("001")){ %>
			
			���ſϷ�
			
			<%} %>
			
			<% if(purchase.getTranCode().equals("002")){ %>
			
			�����
		
			<%}else if(purchase.getTranCode().equals("003")){ %>
			
			��ۿϷ�
			
			<%} %>
				���� �Դϴ�.</td>
		<td></td>
		<% if(purchase.getTranCode().equals("002")){ %>
		<td align="left">
		<a href="/updateTranCode.do?TranCode=<%= purchase.getTranCode() %>&tranNo=<%= purchase.getTranNo() %>">���ǵ���</a>
		</td>
		<%} %>	
		
		
	  <% if(purchase.getTranCode().equals("003")){ %>
			<td align="left">
			��ۿϷ�
			</td>
			<%} %>
		
		
		</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<%} %>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center"></td>
		<td>
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
			<% if( resultPage.getCurrentPage() <= resultPage.getPageUnit() ){ %>
					�� ����
			<% }else{ %>
					<a href="javascript:fncGetPurchaseList('<%=resultPage.getCurrentPage()-1%>')">�� ����</a>
			<% } %>

			<%	for(int i=resultPage.getBeginUnitPage();i<= resultPage.getEndUnitPage() ;i++){	%>
					<a href="javascript:fncGetPurchaseList('<%=i %>');"><%=i %></a>
			<% 	}  %>
	
			<% if( resultPage.getEndUnitPage() >= resultPage.getMaxPage() ){ %>
					���� ��
			<% }else{ %>
					<a href="javascript:fncGetPurchaseList('<%=resultPage.getEndUnitPage()+1%>')">���� ��</a>
			<% } %>
	
	
		</td>
		
	</tr>
</table>

<!--  ������ Navigator �� -->
</form>

</div>

</body>
</html>