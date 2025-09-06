<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.domesticanimalhub.model.User" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath()+"/views/login.jsp?redirect="+request.getRequestURI());
    return;
  }
  String ctx = request.getContextPath();
  User profile = (User) request.getAttribute("profile");
  if (profile == null) profile = (User) session.getAttribute("user");

  // Precompute safe values for inputs
  String phoneVal = (profile.getPhoneNumber() == null) ? "" : profile.getPhoneNumber();
  String addrVal  = (profile.getAddress() == null) ? "" : profile.getAddress();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>My Profile — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>
    body{background:#f7f8fa}
    .avatar{width:120px;height:120px;border-radius:50%;object-fit:cover;background:#e9ecef}
  </style>
</head>
<body>

<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/user/home.jsp"><i class="fas fa-paw me-2"></i>Domestic Animal Hub</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav"><span class="navbar-toggler-icon"></span></button>
    <div id="nav" class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/user/home.jsp">Home</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/animals">Browse</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/animals/new">List Animal</a></li>
      </ul>
      <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet"><i class="fas fa-sign-out-alt me-1"></i>Logout</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <div class="row g-4">
    <div class="col-lg-4">
      <div class="card shadow-sm">
        <div class="card-body text-center">
          <img class="avatar mb-3" src="<%=ctx%>/avatar?userId=<%=profile.getUserId()%>" onerror="this.src='https://via.placeholder.com/120?text=Avatar'">
          <h5 class="mb-0"><%=profile.getFullName()%></h5>
          <div class="text-muted small"><i class="fas fa-envelope me-1"></i><%=profile.getEmail()%></div>
          <div class="text-muted small"><i class="fas fa-id-badge me-1"></i><%=profile.getUserRole()%></div>
        </div>
      </div>
      <% String msg = (String) request.getAttribute("msg"); if (msg!=null) { %>
        <div class="alert alert-success mt-3"><i class="fas fa-check-circle me-2"></i><%=msg%></div>
      <% } %>
      <% String err = (String) request.getAttribute("err"); if (err!=null) { %>
        <div class="alert alert-danger mt-3"><i class="fas fa-exclamation-triangle me-2"></i><%=err%></div>
      <% } %>
    </div>

    <div class="col-lg-8">
      <div class="card shadow-sm mb-4">
        <div class="card-header bg-white"><strong>Edit Profile</strong></div>
        <div class="card-body">
          <form action="<%=ctx%>/user/profile" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="update">
            <div class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Full Name</label>
                <input class="form-control" name="fullName" value="<%=profile.getFullName()%>" required>
              </div>
              <div class="col-md-6">
                <label class="form-label">Email (read-only)</label>
                <input class="form-control" value="<%=profile.getEmail()%>" disabled>
              </div>
              <div class="col-md-6">
                <label class="form-label">Phone</label>
                <input class="form-control" name="phoneNumber" value="<%= phoneVal %>">
              </div>
              <div class="col-md-6">
                <label class="form-label">Address</label>
                <textarea class="form-control" name="address" rows="2"><%= addrVal %></textarea>
              </div>
              <div class="col-12">
                <label class="form-label">Profile Image</label>
                <input class="form-control" type="file" name="profileImage" accept="image/*">
                <div class="form-text">JPG/PNG/WEBP up to 5MB. Leave empty to keep current.</div>
              </div>
            </div>
            <div class="mt-3">
              <button class="btn btn-primary"><i class="fas fa-save me-2"></i>Save Changes</button>
            </div>
          </form>
        </div>
      </div>

      <div class="card shadow-sm">
        <div class="card-header bg-white"><strong>Change Password</strong></div>
        <div class="card-body">
          <% String msgPw = (String) request.getAttribute("msgPw"); if (msgPw!=null) { %>
            <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i><%=msgPw%></div>
          <% } %>
          <% String errPw = (String) request.getAttribute("errPw"); if (errPw!=null) { %>
            <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%=errPw%></div>
          <% } %>

          <form action="<%=ctx%>/user/profile" method="post">
            <input type="hidden" name="action" value="password">
            <div class="row g-3">
              <div class="col-md-4">
                <label class="form-label">Current Password</label>
                <input class="form-control" type="password" name="currentPassword" required>
              </div>
              <div class="col-md-4">
                <label class="form-label">New Password</label>
                <input class="form-control" type="password" name="newPassword" required>
              </div>
              <div class="col-md-4">
                <label class="form-label">Confirm New Password</label>
                <input class="form-control" type="password" name="confirmPassword" required>
              </div>
            </div>
            <div class="mt-3">
              <button class="btn btn-outline-primary"><i class="fas fa-key me-2"></i>Update Password</button>
            </div>
          </form>
        </div>
      </div>

    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
