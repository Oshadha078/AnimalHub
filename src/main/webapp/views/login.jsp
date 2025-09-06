<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%
  if (session != null && session.getAttribute("user") != null) {
    response.sendRedirect(request.getContextPath() + "/user/home.jsp");
    return;
  }
  String remembered = "";
  Cookie[] cookies = request.getCookies();
  if (cookies != null) for (Cookie c: cookies) if ("rememberEmail".equals(c.getName())) remembered = c.getValue();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Login — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/assets/css/auth.css" rel="stylesheet">
</head>
<body>
<div class="auth-container">
  <div class="auth-card">
    <div class="card-header">
      <div class="brand-icon"><i class="fas fa-paw"></i></div>
      <h1 class="brand-title">Domestic Animal Hub</h1>
      <p class="mb-0 opacity-75">Welcome back</p>
    </div>

    <div class="card-body">
      <% String error = (String) request.getAttribute("error"); if (error!=null) { %>
        <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%=error%></div>
      <% } %>

      <form action="<%=request.getContextPath()%>/LoginServlet" method="post">
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-envelope me-2"></i>Email</label>
          <div class="position-relative">
            <i class="fas fa-envelope input-icon"></i>
            <input class="form-control form-control-icon" type="email" name="email" value="<%=remembered%>" required>
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
          <div class="position-relative">
            <i class="fas fa-lock input-icon"></i>
            <input class="form-control form-control-icon" type="password" name="password" required>
          </div>
        </div>
        <div class="form-check mb-3">
          <input class="form-check-input" type="checkbox" id="remember" name="remember" <%=remembered.isEmpty()?"":"checked"%>>
          <label class="form-check-label" for="remember">Remember my email</label>
        </div>
        <button class="btn btn-primary w-100 mb-3" type="submit"><i class="fas fa-sign-in-alt me-2"></i>Login</button>
        <div class="text-center"><a href="<%=request.getContextPath()%>/views/signup.jsp" class="text-decoration-none">Create an account</a></div>
      </form>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body></html>
