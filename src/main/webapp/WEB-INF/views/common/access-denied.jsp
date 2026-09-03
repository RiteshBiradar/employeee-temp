<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Access Denied" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="error-wrapper">
        <div class="error-code">403</div>
        <h2>Access Denied</h2>
        <p>You do not have permission to access this page. Please contact your administrator if you believe this is an error.</p>
        <c:choose>
            <c:when test="${sessionScope.role == 'EMPLOYEE'}">
                <a href="${pageContext.request.contextPath}/employee/dashboard" class="btn btn-primary">Go to Dashboard</a>
            </c:when>
            <c:when test="${sessionScope.role == 'MANAGER'}">
                <a href="${pageContext.request.contextPath}/manager/dashboard" class="btn btn-primary">Go to Dashboard</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Back to Login</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
