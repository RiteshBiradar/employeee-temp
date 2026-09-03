<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Apply for Leave" />
</jsp:include>

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="main-content">
    <div class="page-header">
        <h1>Apply for Leave</h1>
        <p>Submit a new leave request</p>
    </div>

    <!-- Current Balance Info -->
    <c:if test="${not empty balance}">
        <div class="balance-grid">
            <div class="balance-card casual">
                <div class="balance-value"><fmt:formatNumber value="${balance.casualBalance}" maxFractionDigits="0" /></div>
                <div class="balance-label">Casual</div>
            </div>
            <div class="balance-card sick">
                <div class="balance-value"><fmt:formatNumber value="${balance.sickBalance}" maxFractionDigits="0" /></div>
                <div class="balance-label">Sick</div>
            </div>
            <div class="balance-card earned">
                <div class="balance-value"><fmt:formatNumber value="${balance.earnedBalance}" maxFractionDigits="0" /></div>
                <div class="balance-label">Earned</div>
            </div>
        </div>
    </c:if>

    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger" id="apply-leave-error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <!-- Leave Application Form -->
    <div class="card">
        <div class="card-header">Leave Application Form</div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/employee/apply-leave" id="leaveForm">

                <div class="form-group">
                    <label for="leaveType">Leave Type <span class="required">*</span></label>
                    <select class="form-control" id="leaveType" name="leaveType" required>
                        <option value="">-- Select leave type --</option>
                        <option value="CASUAL" ${leaveType == 'CASUAL' ? 'selected' : ''}>Casual Leave</option>
                        <option value="SICK" ${leaveType == 'SICK' ? 'selected' : ''}>Sick Leave</option>
                        <option value="EARNED" ${leaveType == 'EARNED' ? 'selected' : ''}>Earned Leave</option>
                    </select>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="startDate">Start Date <span class="required">*</span></label>
                        <input type="date" class="form-control" id="startDate" name="startDate"
                               value="${startDate}" required>
                    </div>
                    <div class="form-group">
                        <label for="endDate">End Date <span class="required">*</span></label>
                        <input type="date" class="form-control" id="endDate" name="endDate"
                               value="${endDate}" required>
                    </div>
                </div>

                <div class="form-group">
                    <div id="workingDaysPreview" style="font-weight: 600; font-size: 0.9rem;"></div>
                </div>

                <div class="form-group">
                    <label for="reason">Reason <span class="required">*</span></label>
                    <textarea class="form-control" id="reason" name="reason" rows="4"
                              placeholder="Enter your reason for leave..." required><c:out value="${reason}" /></textarea>
                </div>

                <button type="submit" class="btn btn-primary" id="btn-submit-leave">Submit Leave Request</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
