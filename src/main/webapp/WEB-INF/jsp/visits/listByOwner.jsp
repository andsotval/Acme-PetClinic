<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

   		<c:if test="${visitsAccepted != null}">
        	<h2>Accepted Visits</h2>
        
        	<table id="visitsTable" class="table table-striped">
        	<thead>
        	<tr>
            	<th>Date</th>
            	<th>Description</th>
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
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>

		<c:if test="${visitsPending != null}">
        	<h2>Visits waiting for Acceptance</h2>
        
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
    	
 
</petclinic:layout>