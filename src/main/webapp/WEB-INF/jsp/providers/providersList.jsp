<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="/providers">
    <h2>Available providers</h2>

    <table id="providersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Mail</th>
            <th >Add provider</th>
 
            <th align = "center">See more</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${availableProviders}" var="avProvider">
            <tr>
                <td>
                    <c:out value="${avProvider.firstName} ${avProvider.lastName}"/>
                </td>
                <td>
                    <c:out value="${avProvider.mail}"/>
                </td>
                <td>
                	<div align = "center">
	                    <spring:url value="/providers/addProvider/{providerId}" var="providerUrl">                  
	                    	<spring:param name="providerId" value="${avProvider.id}"/>                      
	                    </spring:url>
	                    <a href="${fn:escapeXml(providerUrl)}"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
               		</div>
                </td>          
                <td>
	                <div align = "center">
						<spring:url value="/providers/listProductsByProvider/{providerId}" var="providerUrl">                  
	                    	<spring:param name="providerId" value="${avProvider.id}"/>                      
	                    </spring:url>
	                    <a href="${fn:escapeXml(providerUrl)}"><span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a>
	               </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
        <h2>Already added providers</h2>

    <table id="addedProvidersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Mail</th>
            <th>Telephone</th>
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${addedProviders}" var="addedProvider">
            <tr>
                <td>
                    <c:out value="${addedProvider.firstName} ${addedProvider.lastName}"/>
                </td>
                <td>
                    <c:out value="${addedProvider.mail}"/>
                </td>
                <td>                	
                    <c:out value="${addedProvider.telephone}"/>
                </td>                
            </tr>
        </c:forEach>       
        </tbody>
    </table>     

</petclinic:layout>
