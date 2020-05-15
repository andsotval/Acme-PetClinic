<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="specialty">
    <h2>
        <c:if test="${specialty['new']}">New </c:if>Specialty
    </h2>
    <form:form modelAttribute="specialty" class="form-horizontal" id="create-specialty-form">
    
    	<input type="hidden" name="id" value="${specialty.id}"/>
    	<input type="hidden" name="available" value="${specialty.available}"/>
    	
        <petclinic:inputField label="Name" name="name"/>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                    <c:when test="${specialty.available eq true}">
						<a class="btn btn-default" href='<spring:url value="/specialty/listAvailable" htmlEscape="true"/>'>Back to Specialty Available List</a> 
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-default" href='<spring:url value="/specialty/listNotAvailable" htmlEscape="true"/>'>Back to Specialty Not Available List</a> 
                    </c:otherwise>
                </c:choose>
            	<c:choose>
                    <c:when test="${specialty['new']}">
						<button class="btn btn-default" type="submit" formaction="/specialty/new">Create Specialty</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit" formaction="/specialty/edit/${specialtyId}">Update Specialty</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>