<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="stays">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
                $("#finishDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <c:if test="${stay.id != null}">
				<h2>Update Stay</h2>
        </c:if>
        
        <c:if test="${stay.id == null}">
				<h2>New Stay</h2>
        </c:if>
        
         <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${stay.pet.name}"/></td>
                <td><petclinic:localDate date="${stay.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${stay.pet.type.name}"/></td>
                <td><c:out value="${stay.pet.owner.firstName} ${stay.pet.owner.lastName}"/></td>
            </tr>
        </table>
        
			<form:form modelAttribute="stay" class="form-horizontal" action="/stays/save/${stay.id}">
            <div class="form-group has-feedback">
           		<petclinic:inputField label="Description" name="description"/>	
                <petclinic:inputField label="Start Date of the Stay" name="startDate"/>
                <petclinic:inputField label="Finish Date of the Stay" name="finishDate"/>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${stay.id}"/>
                    <%-- <input type="hidden" name="clinic" value="${stay.clinic.id}"/>
                    <input type="hidden" name="pet" value="${stay.pet}"/> --%>
                    <c:if test="${stay.id == null}">
                    	<input type="hidden" name="clinic" value="${clinicId}"/>
                    	<input type="hidden" name="pet" value="${stay.pet.id}"/>
						<button class="btn btn-default" type="submit" formaction="/stays/save">Save Stay</button> 
                     </c:if>
                     <c:if test="${stay.id != null}">
						<button class="btn btn-default" type="submit" formaction="/stays/save/${stay.id}">Save Stay</button> 
                     </c:if>
                </div>
            </div>
            
        </form:form>
        <c:if test="${stays != null}">
        <h2>Historical of Stays</h2>
        
        <table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Finish Date</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td>
                    <c:out value="${stay.startDate}"/>
                </td>
                <td>
                    <c:out value="${stay.finishDate}"/>
                </td>
                <td>
                    <c:out value="${stay.description} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </c:if>
    </jsp:body>
    
    
</petclinic:layout>
