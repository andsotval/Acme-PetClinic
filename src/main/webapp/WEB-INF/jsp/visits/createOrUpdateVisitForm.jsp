<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>


<petclinic:layout pageName="visitsByPet">    
	<jsp:body>
    	<c:if test="${visit.id != null}">
				<h2>Update Visit</h2>
        </c:if>
        
        <c:if test="${visit.id == null}">
				<h2>New Visit</h2>
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
                <td><c:out value="${visit.pet.name}" /></td>
                <td><petclinic:localDate
						date="${visit.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${visit.pet.type.name}" /></td>
                <td><c:out
						value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}" /></td>
            </tr>
        </table>
        
            <c:if test="${hasClinic eq true}">
				<form:form modelAttribute="visit" class="form-horizontal"
				action="/visits/save/${visit.id}">
		            <div class="form-group has-feedback">
		           		<petclinic:inputField label="Description"
							name="description" />	
		                <petclinic:inputField label="Date of the Visit"
							name="dateTime" />
		            </div>
		            
		            <div class="form-group">
		                <div class="col-sm-offset-2 col-sm-10">
		                	<sec:authorize access="hasAuthority('veterinarian')">
								<a class="btn btn-default"
								href='<spring:url value="/visits/listAllAccepted" htmlEscape="true"/>'>Back to list of pending visits</a>
		                	</sec:authorize>
		                	<sec:authorize access="hasAuthority('owner')">
		                	<a class="btn btn-default"
								href='<spring:url value="/pets/listMyPets" htmlEscape="true"/>'>Back to list of Pets</a>
		                	</sec:authorize>
		                    <input type="hidden" name="id" value="${visit.id}" />
		                     <c:if test="${visit.id == null}">
		                    	<input type="hidden" name="clinic"
									value="${visit.clinic.id}" />
		                    	<input type="hidden" name="pet"
									value="${visit.pet.id}" />
								<button class="btn btn-default" type="submit" name="authorized"
									formaction="/visits/save">Save Visit</button> 
		                     </c:if>
		                     <c:if test="${visit.id != null}">
								<button class="btn btn-default" type="submit" name="authorized"
									formaction="/visits/save/${visit.id}">Save Visit</button> 
		                     </c:if>
		                </div>
		            </div>
		        </form:form>
            </c:if>
             <c:if test="${hasClinic eq false}">
             	<h3 style="color: red;">**You cannot request visits if you are not in one Clinic</h3>
             </c:if>
       
       <c:if test="${visit.id == null}">
        	<h2>Historical of Visits</h2>
        	<c:if test="${not empty visits}">
        		<table id="visitsTable" class="table table-striped">
        		<thead>
        			<tr>
            			<th>Date</th>
            			<th>Description</th>
            			<th>Status</th>
            			<th>Clinic</th>
       				</tr>
       			</thead>
        		<tbody>
			        <c:forEach items="${visits}" var="visit">
			            <tr>
			                <td>
			                    <petclinic:localDateTime date="${visit.dateTime}" pattern="yyyy/MM/dd HH:mm:ss" />
			                </td>
			                <td>
			                    <c:out value="${visit.description}" />
			                </td>
			                <td>
			                <c:choose>
                			<c:when test="${visit.isAccepted == null}">
                				<c:out value="PENDING"/>
                			</c:when>
                			<c:when test="${visit.isAccepted eq true}">
                				<c:out value="ACCEPTED"/>
                			</c:when>
                			<c:otherwise>
                				<c:out value="REJECTED"/>
                			</c:otherwise>
                		</c:choose>
			                </td>
			                <td>
			                    <c:out value="${visit.clinic.name}" />
			                </td>
			            </tr>
			        </c:forEach>
       			</tbody>
    			</table>
    		</c:if>
    		<c:if test="${empty visits}">
    			<h3>This pet doesn't have any visit by the moment</h3>
    		</c:if>
    		
    </c:if>
    </jsp:body>

</petclinic:layout>
