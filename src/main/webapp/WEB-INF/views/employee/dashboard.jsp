<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Employee Dashboard" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="page-header">
        <h1>Welcome, <c:out value="${sessionScope.empName}" /></h1>
        <p>Here's an overview of your leave status</p>
    </div>

    <!-- Leave Balance Summary -->
    <c:if test="${not empty balance}">
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
    </c:if>

    <!-- Quick Actions -->
    <div class="quick-actions">
        <a href="${pageContext.request.contextPath}/employee/apply-leave" class="btn btn-primary" id="btn-apply-leave">
            &#10010; Apply for Leave
        </a>
        <a href="${pageContext.request.contextPath}/employee/leave-history" class="btn btn-primary" id="btn-view-history" style="background: var(--primary-light);">
            &#128196; View Leave History
        </a>
    </div>

    <!-- Recent Leave Requests -->
    <div class="card">
        <div class="card-header">Recent Leave Requests</div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty leaveRequests}">
                    <div class="table-container">
                        <table id="recent-leaves-table">
                            <thead>
                                <tr>
                                    <th>Leave Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Working Days</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="req" items="${leaveRequests}" end="4">
                                    <tr>
                                        <td><c:out value="${req.leaveType}" /></td>
                                        <td><c:out value="${req.startDate}" /></td>
                                        <td><c:out value="${req.endDate}" /></td>
                                        <td><c:out value="${dateUtil.calculateWorkingDays(req.startDate, req.endDate)}" /></td>
                                        <td><c:out value="${req.reason}" /></td>
                                        <td>
                                            <span class="badge badge-${req.status.toLowerCase()}">
                                                <c:out value="${req.status}" />
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <c:if test="${leaveRequests.size() > 5}">
                        <div class="text-center mt-2">
                            <a href="${pageContext.request.contextPath}/employee/leave-history" class="btn btn-primary btn-sm">View All</a>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">&#128203;</div>
                        <p>No leave requests yet. Apply for leave to get started.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
