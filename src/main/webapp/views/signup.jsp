<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Signup — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/assets/css/auth.css" rel="stylesheet">
</head>
<body>
<div class="auth-container">
  <div class="auth-card">
    <div class="card-header">
      <div class="brand-icon"><i class="fas fa-user-plus"></i></div>
      <h1 class="brand-title">Create Account</h1>
    </div>

    <div class="card-body">
      <% String err = (String) request.getAttribute("signupError"); if (err!=null) { %>
        <div class="alert alert-danger"><i class="fas fa-exclamation-circle me-2"></i><%=err%></div>
      <% } %>

      <form action="<%=request.getContextPath()%>/SignupServlet" method="post">
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-user me-2"></i>Full Name</label>
          <div class="position-relative">
            <i class="fas fa-user input-icon"></i>
            <input class="form-control form-control-icon" type="text" name="fullName" required>
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-envelope me-2"></i>Email</label>
          <div class="position-relative">
            <i class="fas fa-envelope input-icon"></i>
            <input class="form-control form-control-icon" type="email" name="email" required>
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-phone me-2"></i>Phone</label>
          <div class="position-relative">
            <i class="fas fa-phone input-icon"></i>
            <input class="form-control form-control-icon" type="text" name="phoneNumber">
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
          <div class="position-relative">
            <i class="fas fa-lock input-icon"></i>
            <input class="form-control form-control-icon" type="password" name="password" required>
          </div>
        </div>
        <button class="btn btn-success w-100 mb-3" type="submit"><i class="fas fa-user-plus me-2"></i>Sign up</button>
        <div class="text-center"><a href="<%=request.getContextPath()%>/views/login.jsp" class="text-decoration-none">Back to login</a></div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body></html>
