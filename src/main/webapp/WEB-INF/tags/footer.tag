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

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.js" integrity="sha256-gQzieXjKD85Ibbpg4l8GduIagpt4oUSQRYaDaLd+8sI=" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.css" integrity="sha256-AbZqn2w4KXugIvUu6QtV4nK4KlXj4nrIp6x/8S4Xg2U=" crossorigin="anonymous" />
<!-- Footer -->
<footer style="width:100%;position: fixed;bottom: 0; padding-bottom: 13px;padding-top: 13px; background-color: #38342c;" class="page-footer font-small pt-4">

  <!-- Copyright -->
  <div style="color:white" class="footer-copyright text-center py-3">&#64; 2020 Copyright: DP2 - Grupo 8 - LAB F1.33
   
  </div>
  
    <div style="color:white" class="footer-copyright text-center py-3">
    <a href="https://github.com/andsotval/Acme-PetClinic/wiki"> Link to our Repository in Github</a>
  </div>
  <!-- Copyright -->

</footer>
<!-- Footer -->