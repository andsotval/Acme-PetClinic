<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="visitsByPet">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#date").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>

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
        
			<form:form modelAttribute="visit" class="form-horizontal"
			action="/visits/save/${visit.id}">
            <div class="form-group has-feedback">
           		<petclinic:inputField label="Description"
					name="description" />	
                <petclinic:inputField label="Date of the Visit"
					name="date" />
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${visit.id}" />
                   <!--! <input type="hidden" name="clinic" value="${visit.clinic.id}"/>
                    <input type="hidden" name="pet" value="${visit.pet.id}"/>-->
                     <c:if test="${visit.id == null}">
                    	<input type="hidden" name="clinic"
							value="${clinicId}" />
                    	<input type="hidden" name="pet"
							value="${visit.pet.id}" />
						<button class="btn btn-default" type="submit"
							formaction="/visits/save">Save Visit</button> 
                     </c:if>
                     <c:if test="${visit.id != null}">
						<button class="btn btn-default" type="submit"
							formaction="/visits/save/${visit.id}">Save Visit</button> 
                     </c:if>
                    <!-- <button class="btn btn-default" type="submit">Save Visit</button> -->
                </div>
            </div>
        </form:form>
       
       <c:if test="${visit.id == null}">
        	<h2>Historical of Visits</h2>
        	<c:if test="${not empty visits}">
        		<table id="visitsTable" class="table table-striped">
        		<thead>
        			<tr>
            			<th>Date</th>
            			<th>Description</th>
       				</tr>
       			</thead>
        		<tbody>
			        <c:forEach items="${visits}" var="visit">
			            <tr>
			                <td>
			                    <c:out value="${visit.date}" />
			                </td>
			                <td>
			                    <c:out value="${visit.description}" />
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
