<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar">
    <div class="navbar-inner">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">
            <span class="brand-icon">&#128197;</span>
            Leave Tracker
        </a>

        <c:if test="${not empty sessionScope.empId}">
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${sessionScope.role == 'EMPLOYEE'}">
                        <li><a href="${pageContext.request.contextPath}/employee/dashboard" id="nav-dashboard">Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/employee/apply-leave" id="nav-apply-leave">Apply Leave</a></li>
                        <li><a href="${pageContext.request.contextPath}/employee/leave-history" id="nav-leave-history">Leave History</a></li>
                        <li><a href="${pageContext.request.contextPath}/employee/leave-balance" id="nav-leave-balance">Leave Balance</a></li>
                    </c:when>
                    <c:when test="${sessionScope.role == 'MANAGER'}">
                        <li><a href="${pageContext.request.contextPath}/manager/dashboard" id="nav-manager-dashboard">Dashboard</a></li>
                    </c:when>
                </c:choose>
            </ul>

            <div class="navbar-user">
                <div class="user-info">
                    <div class="user-name"><c:out value="${sessionScope.empName}" /></div>
                    <div class="user-role"><c:out value="${sessionScope.role}" /></div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout" id="btn-logout">Logout</a>
            </div>
        </c:if>
    </div>
</nav>
