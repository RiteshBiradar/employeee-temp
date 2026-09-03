<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Error" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="error-wrapper">
        <div class="error-code">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode != 0}">
                    ${pageContext.errorData.statusCode}
                </c:when>
                <c:otherwise>Error</c:otherwise>
            </c:choose>
        </div>
        <h2>Something went wrong</h2>
        <p>We encountered an unexpected error. Please try again or contact your administrator if the problem persists.</p>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Back to Home</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
