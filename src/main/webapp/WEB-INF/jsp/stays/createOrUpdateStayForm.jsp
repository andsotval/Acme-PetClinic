<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>


<petclinic:layout pageName="staysByPet">
	<jsp:attribute name="customScript">
        <script>
			$(function() {
				$("#startDate").datepicker({
					dateFormat : 'yy/mm/dd'
				});
				$("#finishDate").datepicker({
					dateFormat : 'yy/mm/dd'
				});
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
                <td><c:out value="${stay.pet.name}" /></td>
                <td><petclinic:localDate
						date="${stay.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${stay.pet.type.name}" /></td>
                <td><c:out
						value="${stay.pet.owner.firstName} ${stay.pet.owner.lastName}" /></td>
            </tr>
        </table>
        
            <c:if test="${hasClinic eq true}">
				<form:form modelAttribute="stay" class="form-horizontal"
				action="/stays/save/${stay.id}">
		            <div class="form-group has-feedback">
		           		<petclinic:inputField label="Description"
							name="description" />	
		                <petclinic:inputField label="Start Date of the Stay"
							name="startDate" />
		                <petclinic:inputField label="Finish Date of the Stay"
							name="finishDate" />
		            </div>
		            <div class="form-group">
		                <div class="col-sm-offset-2 col-sm-10">
	                     	<sec:authorize access="hasAuthority('veterinarian')">
								<a class="btn btn-default"
								href='<spring:url value="/stays/listAllAccepted" htmlEscape="true"/>'>Back to list of pending stays</a>
		                	</sec:authorize>
		                	<sec:authorize access="hasAuthority('owner')">
		                	<a class="btn btn-default"
								href='<spring:url value="/pets/listMyPets" htmlEscape="true"/>'>Back to list of Pets</a>
		                	</sec:authorize>
		                    <input type="hidden" name="id" value="${stay.id}" />
		                    <%-- <input type="hidden" name="clinic" value="${stay.clinic.id}"/>
		                    <input type="hidden" name="pet" value="${stay.pet}"/> --%>
		                    <c:if test="${stay.id == null}">
		                    	<input type="hidden" name="clinic"
										value="${stay.clinic.id}" />
		                    	<input type="hidden" name="pet"
										value="${stay.pet.id}" />
								<button class="btn btn-default" type="submit"
										formaction="/stays/save">Save Stay</button> 
		                     </c:if>
		                     <c:if test="${stay.id != null}">
								<button class="btn btn-default" type="submit"
										formaction="/stays/save/${stay.id}">Save Stay</button> 
		                     </c:if>
		                </div>
		            </div>
		        </form:form>
            </c:if>
            
             <c:if test="${hasClinic eq false}">
             	<h3 style="color: red;">**You cannot request stays if you are not in one Clinic</h3>
             </c:if>
            
        <c:if test="${stays != null}">
        <h2>Historical of Stays</h2>
        
        <c:if test="${not empty stays}">
        <table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Finish Date</th>
            <th>Description</th>
            <th>Status</th>
            <th>Clinic</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td>
                    <c:out value="${stay.startDate}" />
                </td>
                <td>
                    <c:out value="${stay.finishDate}" />
                </td>
                <td>
                    <c:out value="${stay.description} " />
                </td>
                <td>
			       <c:choose>
                		<c:when test="${stay.isAccepted == null}">
                			<c:out value="PENDING" />
                		</c:when>
                		<c:when test="${stay.isAccepted eq true}">
                			<c:out value="ACCEPTED" />
                		</c:when>
                		<c:otherwise>
                			<c:out value="REJECTED" />
                		</c:otherwise>
                	</c:choose>
			    </td>
			    <td>
			    	<c:out value="${stay.clinic.name}" />
			    </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
        	<c:if test="${empty stays}">
    			<h3>This pet doesn't have any stay by the moment</h3>
    		</c:if>
    
        </c:if>
    </jsp:body>


</petclinic:layout>
