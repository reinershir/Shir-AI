<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/common/taglib.jsp" %>
<form class="toolbar" id="form${ClassName}Edit" method="post">
	<#if Autos??>
	<#list Autos as item>
	<#assign info=item?split("#")>
	<#if info[4]=="false">
		<input name="${info[1]}" type="hidden"/>
	</#if>
	</#list>
	</#if>
	<#if addMutilpart><input name="rows" id="${ClassName}AddDialogPageSize" type="hidden"/></#if>
	<table style="padding-left:5%">
	 	<#if Autos??>
	 	<#assign i=1>
		<#list Autos as item>
		<#assign info=item?split("#")>
		<#if info[5]=="true">
		<#if i==1>
		<tr height="40">
		</#if>
			<td>${info[2]}:</td>
			<td width="200"><input class="${info[7]}" name="${info[1]}" <#if info[3]=="true">required="true"</#if> data-options="prompt:'请输入 ${info[2]}...'"/></td>
		<#if i%2==0||!item_has_next>
		</tr>
		<#if item_has_next>
		<tr height="40">
		</#if>
		</#if>
		<#assign i=i+1>
		</#if>
		</#list>
		</#if>
    </table>
</form>
