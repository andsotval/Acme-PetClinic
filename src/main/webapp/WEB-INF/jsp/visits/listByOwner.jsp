<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

		<h2>Accepted Visits</h2>
   		<c:if test="${not empty visitsAccepted}">
        	<table id="visitsTable" class="table table-striped">
        	<thead>
        	<tr>
            	<th>Date</th>
            	<th>Description</th>
            	<th>Status</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${visitsAccepted}" var="visitAccepted">
            		<tr>
                		<td>
                   			<c:out value="${visitAccepted.date}"/>
                		</td>
                		<td>
                    		<c:out value="${visitAccepted.description}"/>
                		</td>
                		<td width="15%">
                		<c:choose>
                			<c:when test="${visitAccepted.isAccepted == null}">
                				<c:out value="PENDING"/>
                			</c:when>
                			<c:when test="${visitAccepted.isAccepted eq true}">
                				<c:out value="ACCEPTED"/>
                			</c:when>
                			<c:otherwise>
                				<c:out value="REJECTED"/>
                			</c:otherwise>
                		</c:choose>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty visitsAccepted}">
    		<h3>You don't have Accepted Visits by the moment...</h3>
    	</c:if>

		<h2>Visits waiting for Acceptance</h2>
		<c:if test="${not empty visitsPending}">
        	<table id="visitsTable" class="table table-striped">
        	<thead>
        	<tr>
            	<th>Date</th>
            	<th>Description</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${visitsPending}" var="visitPending">
            		<tr>
                		<td>
                   			<c:out value="${visitPending.date}"/>
                		</td>
                		<td>
                    		<c:out value="${visitPending.description}"/>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty visitsPending}">
    		<h3>You don't have Visits waiting for being accepted by the moment...</h3>
    	</c:if>
    	
 
</petclinic:layout>