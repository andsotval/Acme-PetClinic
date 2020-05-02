<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Placed at the end of the document so the pages load faster --%>
<spring:url value="/webjars/jquery/2.2.4/jquery.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<%-- jquery-ui.js file is really big so we only load what we need instead of loading everything --%>
<spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.js" var="jQueryUiCore"/>
<script src="${jQueryUiCore}"></script>

 <!-- Only datepicker is used -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> 

<%-- Bootstrap --%>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<!-- Footer -->
<footer style="width:100%; padding-bottom: 13px;padding-top: 13px; background-color: #38342c;" class="page-footer font-small pt-4">

  <!-- Copyright -->
  <div style="color:white" class="footer-copyright text-center py-3">&#64; 2020 Copyright: DP2 - Grupo 8 - LAB F1.33
   
  </div>
  
    <div style="color:white" class="footer-copyright text-center py-3">
    <a href="https://github.com/andsotval/Acme-PetClinic/wiki"> Link to our Repository in Github</a>
  </div>
  <!-- Copyright -->

</footer>
<!-- Footer -->