<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Leave Balance" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="page-header">
        <h1>Leave Balance</h1>
        <p>Your current leave balance overview</p>
    </div>

    <c:choose>
        <c:when test="${not empty balance}">
            <div class="balance-grid">
                <div class="balance-card casual">
                    <div class="balance-value"><fmt:formatNumber value="${balance.casualBalance}" maxFractionDigits="0" /></div>
                    <div class="balance-label">Casual Leave</div>
                    <div class="balance-sublabel">days remaining</div>
                </div>
                <div class="balance-card sick">
                    <div class="balance-value"><fmt:formatNumber value="${balance.sickBalance}" maxFractionDigits="0" /></div>
                    <div class="balance-label">Sick Leave</div>
                    <div class="balance-sublabel">days remaining</div>
                </div>
                <div class="balance-card earned">
                    <div class="balance-value"><fmt:formatNumber value="${balance.earnedBalance}" maxFractionDigits="0" /></div>
                    <div class="balance-label">Earned Leave</div>
                    <div class="balance-sublabel">days remaining</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">Balance Details</div>
                <div class="card-body">
                    <div class="table-container">
                        <table id="balance-details-table">
                            <thead>
                                <tr>
                                    <th>Leave Type</th>
                                    <th>Available Days</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Casual Leave</td>
                                    <td><fmt:formatNumber value="${balance.casualBalance}" maxFractionDigits="1" /></td>
                                </tr>
                                <tr>
                                    <td>Sick Leave</td>
                                    <td><fmt:formatNumber value="${balance.sickBalance}" maxFractionDigits="1" /></td>
                                </tr>
                                <tr>
                                    <td>Earned Leave</td>
                                    <td><fmt:formatNumber value="${balance.earnedBalance}" maxFractionDigits="1" /></td>
                                </tr>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td><strong><fmt:formatNumber value="${balance.casualBalance + balance.sickBalance + balance.earnedBalance}" maxFractionDigits="1" /></strong></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning">
                Leave balance information is not available. Please contact your administrator.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
