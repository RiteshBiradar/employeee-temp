<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Leave History" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="page-header">
        <h1>Leave History</h1>
        <p>View all your leave requests</p>
    </div>

    <!-- Success Message -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" id="success-alert">
            <c:out value="${successMessage}" />
        </div>
    </c:if>

    <div class="card">
        <div class="card-header">Your Leave Requests</div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty leaveRequests}">
                    <div class="table-container">
                        <table id="leave-history-table">
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
                                <c:forEach var="req" items="${leaveRequests}">
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
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">&#128203;</div>
                        <p>No leave requests found.</p>
                        <a href="${pageContext.request.contextPath}/employee/apply-leave" class="btn btn-primary mt-2">Apply for Leave</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
