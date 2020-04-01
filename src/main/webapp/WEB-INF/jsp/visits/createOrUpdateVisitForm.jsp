<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="visits">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <h2>Update Visit</h2>
			<form:form modelAttribute="visit" class="form-horizontal" action="/visits/save/${visit.id}">
            <div class="form-group has-feedback">
           		<petclinic:inputField label="Description" name="description"/>	
                <petclinic:inputField label="Date of the Visit" name="date"/>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${visit.id}"/>
                   <!--! <input type="hidden" name="clinic" value="${visit.clinic.id}"/>
                    <input type="hidden" name="pet" value="${visit.pet.id}"/>-->
                    <button class="btn btn-default" type="submit">Save Visit</button>
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>
