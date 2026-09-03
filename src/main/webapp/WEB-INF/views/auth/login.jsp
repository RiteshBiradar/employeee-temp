<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Login - Employee Leave & Attendance Tracker">
    <title>Login | Leave Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-wrapper">
    <div class="login-card">
        <div class="login-header">
            <div class="login-icon">&#128197;</div>
            <h1>Leave Tracker</h1>
            <p>Employee Leave &amp; Attendance Tracker</p>
        </div>

        <div class="login-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger" id="login-error">
                    <c:out value="${error}" />
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login" id="loginForm">
                <div class="form-group">
                    <label for="empId">Select Account <span class="required">*</span></label>
                    <select class="form-control" id="empId" name="empId" required>
                        <option value="">-- Select an employee --</option>
                        <c:forEach var="emp" items="${employees}">
                            <option value="${emp.empId}">
                                <c:out value="${emp.name}" /> (<c:out value="${emp.role}" />) &mdash; <c:out value="${emp.email}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary" id="btn-login">Sign In</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
