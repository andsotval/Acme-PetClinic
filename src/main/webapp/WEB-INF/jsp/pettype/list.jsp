<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pettypes">
    <h2>Pet Type</h2>

    <table id="pettypeTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pettypes}" var="pettype">
            <tr>
                <td width="90%">
                	<c:out value="${pettype.name}"/>
                </td>
                <td width="10%">
                	<spring:url value="/pettype/edit/{pettypeId}" var="pettypeUpdateUrl">
                        <spring:param name="pettypeId" value="${pettype.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(pettypeUpdateUrl)}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
                    
	                <spring:url value="/pettype/delete/{pettypeId}" var="pettypeDeleteUrl">
	                    <spring:param name="pettypeId" value="${pettype.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(pettypeDeleteUrl)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <a class="btn btn-default" href='<spring:url value="/pettype/new" htmlEscape="true"/>'>Create a new Pet type</a>
    
</petclinic:layout>