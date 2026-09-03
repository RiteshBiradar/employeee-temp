<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Manager Dashboard" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="page-header">
        <h1>Manager Dashboard</h1>
        <p>Pending leave requests from your direct reports</p>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" id="success-alert">
            <c:out value="${successMessage}" />
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" id="error-alert">
            <c:out value="${errorMessage}" />
        </div>
    </c:if>

    <div class="card">
        <div class="card-header">
            Pending Leave Requests
            <c:if test="${not empty pendingRequests}">
                <span class="badge badge-pending" style="margin-left: 0.5rem;">${pendingRequests.size()}</span>
            </c:if>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty pendingRequests}">
                    <div class="table-container">
                        <table id="pending-requests-table">
                            <thead>
                                <tr>
                                    <th>Employee</th>
                                    <th>Leave Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Working Days</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="req" items="${pendingRequests}">
                                    <tr>
                                        <td><c:out value="${req.employeeName}" /></td>
                                        <td><c:out value="${req.leaveType}" /></td>
                                        <td><c:out value="${req.startDate}" /></td>
                                        <td><c:out value="${req.endDate}" /></td>
                                        <td><c:out value="${req.workingDays}" /></td>
                                        <td><c:out value="${req.reason}" /></td>
                                        <td>
                                            <span class="badge badge-pending">
                                                <c:out value="${req.status}" />
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/manager/approve"
                                                      data-confirm="Are you sure you want to APPROVE this leave request?">
                                                    <input type="hidden" name="requestId" value="${req.requestId}">
                                                    <button type="submit" class="btn btn-success btn-sm" id="btn-approve-${req.requestId}">
                                                        Approve
                                                    </button>
                                                </form>
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/manager/reject"
                                                      data-confirm="Are you sure you want to REJECT this leave request?">
                                                    <input type="hidden" name="requestId" value="${req.requestId}">
                                                    <button type="submit" class="btn btn-danger btn-sm" id="btn-reject-${req.requestId}">
                                                        Reject
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">&#9989;</div>
                        <p>No pending leave requests from your team. All caught up!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
