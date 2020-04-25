<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders">
	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>

	<jsp:body>
    	<h2>
        New Order
    </h2>
    <form:form modelAttribute="order" class="form-horizontal" id="create-order-form">
    
    	<input type="hidden" name="id" value="${order.id}" />
    	<input type="hidden" name="manager.id" value="${order.manager.id}" />
    	<input type="hidden" name="isAccepted" value="false" />
    	
        <div class="form-group has-feedback">
            <petclinic:inputField label="Date" name="date" />
            <petclinic:selectField label="Product" name="product" size="5" itemLabel="name" names="${products}"></petclinic:selectField>
            <!-- <form:select multiple="true" path="product" items="${products}" itemLabel="name" itemValue="id" /> -->
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Create Order</button>
            </div>
        </div>
    </form:form>
    </jsp:body>

</petclinic:layout>