<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Placed at the end of the document so the pages load faster --%>
<spring:url value="/webjars/jquery/2.2.4/jquery.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<%-- jquery-ui.js file is really big so we only load what we need instead of loading everything --%>
<spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.js" var="jQueryUiCore"/>
<script src="${jQueryUiCore}"></script>

<%-- Bootstrap --%>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<!-- Footer -->
<footer style="padding-bottom: 13px;padding-top: 13px;background-color: white;" class="page-footer font-small pt-4">

  <!-- Copyright -->
  <div class="footer-copyright text-center py-3">&#64; 2020 Copyright: DP2 - Grupo 8 - LAB F1.33
    <a href="https://github.com/andsotval/Acme-PetClinic"> Github</a>
  </div>
  <!-- Copyright -->

</footer>
<!-- Footer -->