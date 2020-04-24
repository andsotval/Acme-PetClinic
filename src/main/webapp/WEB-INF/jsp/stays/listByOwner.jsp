<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

		<h1>My Stays</h1>
		<h2>Accepted Stays</h2>
   		<c:if test="${not empty staysAccepted}">
        	<table id="staysTable" class="table table-striped">
        	<thead>
        	<tr>
        		<th>Pet Name</th>
            	<th>Start Date</th>
            	<th>Finish Date</th>
            	<th>Description</th>
            	<th>Status</th>
            	<th>Cancel</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${staysAccepted}" var="stayAccepted">
            		<tr>
                		<td width="15%">
                   			<c:out value="${stayAccepted.pet.name}"/>
                		</td>
                		<td width="15%">
                   			<c:out value="${stayAccepted.startDate}"/>
                		</td>
                		<td width="15%">
                   			<c:out value="${stayAccepted.finishDate}"/>
                		</td>
                		<td width="30%">
                    		<c:out value="${stayAccepted.description}"/>
                		</td>
                		<td width="15%">
                    		<c:out value="ACCEPTED"/>
                		</td>
                		<td width="10%">
                		<spring:url value="/stays/cancel/{stayId}" var="stayUrlCancel">
	                    	<spring:param name="stayId" value="${stayAccepted.id}"/>
	                	</spring:url>
	                	<a href="${fn:escapeXml(stayUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty staysAccepted}">
    		<h3>You don't have Accepted Stays by the moment</h3>
    	</c:if>

		<h2>Stays waiting for Acceptance</h2>
		<c:if test="${not empty staysPending}">
        	<table id="staysTable" class="table table-striped">
        	<thead>
        	<tr>
        		<th>Pet Name</th>
            	<th>Start Date</th>
            	<th>Finish Date</th>
            	<th>Description</th>
            	<th>Status</th>
            	<th>Cancel</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${staysPending}" var="stayPending">
            		<tr>
                		<td width="15%">
                   			<c:out value="${stayPending.pet.name}"/>
                		</td>
                		<td width="15%">
                   			<c:out value="${stayPending.startDate}"/>
                		</td>
                		<td width="15%">
                    		<c:out value="${stayPending.finishDate}"/>
                		</td>
                		<td width="30%">
                    		<c:out value="${stayPending.description}"/>
                		</td>
                		<td width="15%">
                    		<c:out value="PENDING"/>
                		</td>
                		<td width="10%">
                		<spring:url value="/stays/cancel/{stayId}" var="stayUrlCancel">
	                    	<spring:param name="stayId" value="${stayPending.id}"/>
	                	</spring:url>
	                	<a href="${fn:escapeXml(stayUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty staysPending}">
    		<h3>You don't have Stays waiting for being accepted by the moment</h3>
    	</c:if>
 
</petclinic:layout>