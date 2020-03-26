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
        <h2>Stay</h2>
			<form:form modelAttribute="stay" class="form-horizontal" action="/stays/save">
            <div class="form-group has-feedback">
           		<petclinic:inputField label="Description" name="description"/>	
                <petclinic:inputField label="Start Date of the Stay" name="startDate"/>
                <petclinic:inputField label="Finish Date of the Stay" name="finishDate"/>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${stay.id}"/>
                    <button class="btn btn-default" type="submit">Save Stay</button>
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>
