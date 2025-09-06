<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="com.domesticanimalhub.model.User, com.domesticanimalhub.model.UserRole" %>
<%
    // If already logged in, send to role-appropriate home
    User u = (session == null) ? null : (User) session.getAttribute("user");
    if (u != null) {
        String dest = (u.getUserRole() == UserRole.ADMIN) ? "/admin/dashboard" : "/user/home.jsp";
        response.sendRedirect(request.getContextPath() + dest);
        return;
    }

    // determine target redirect after login
    String redirect = request.getParameter("redirect");
    if (redirect == null) redirect = "";

    // Read remember-me cookie to prefill email
    String rememberedEmail = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("rememberEmail".equals(c.getName())) {
                rememberedEmail = c.getValue();
                break;
            }
        }
    }

    // Which tab should be active? Priority: request attribute -> URL param -> default "login"
    String activeTab = (String) request.getAttribute("activeTab");
    if (activeTab == null) activeTab = request.getParameter("tab");
    if (activeTab == null || activeTab.isBlank()) activeTab = "login";
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Domestic Animal Hub — Login / Signup / Admin</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/assets/css/auth.css" rel="stylesheet">
</head>
<body>

<a href="<%=request.getContextPath()%>/index.jsp" class="back-to-home">
  <i class="fas fa-arrow-left me-2"></i>Back to Home
</a>

<div class="auth-container">
  <div class="auth-card">
    <div class="card-header">
      <div class="brand-icon"><i class="fas fa-paw"></i></div>
      <h1 class="brand-title">Domestic Animal Hub</h1>
      <p class="mb-0 opacity-75">Connect. Trade. Care.</p>

      <ul class="nav nav-tabs mt-3" id="authTabs" role="tablist">
        <li class="nav-item">
          <button class="nav-link <%= "login".equals(activeTab) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#login" type="button" role="tab">
            <i class="fas fa-sign-in-alt me-2"></i>User Login
          </button>
        </li>
        <li class="nav-item">
          <button class="nav-link <%= "signup".equals(activeTab) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#signup" type="button" role="tab">
            <i class="fas fa-user-plus me-2"></i>Signup
          </button>
        </li>
        <li class="nav-item">
          <button class="nav-link <%= "admin".equals(activeTab) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#admin" type="button" role="tab">
            <i class="fas fa-user-shield me-2"></i>Admin
          </button>
        </li>
      </ul>
    </div>

    <div class="card-body">
      <% String flash = (String) request.getAttribute("flashSuccess"); %>
      <% if (flash != null) { %>
        <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i><%= flash %></div>
      <% } %>

      <div class="tab-content">
        <!-- Login -->
        <div class="tab-pane fade <%= "login".equals(activeTab) ? "show active" : "" %>" id="login" role="tabpanel">
          <% String loginError = (String) request.getAttribute("error"); %>
          <% if (loginError != null) { %>
            <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%= loginError %></div>
          <% } %>

          <form action="<%= request.getContextPath() %>/LoginServlet" method="post" autocomplete="on" novalidate>
            <input type="hidden" name="redirect" value="<%= redirect %>">
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-envelope me-2"></i>Email Address</label>
              <div class="position-relative">
                <i class="fas fa-envelope input-icon"></i>
                <input type="email" class="form-control form-control-icon" name="email" value="<%= rememberedEmail %>" placeholder="Enter your email" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
              <div class="position-relative">
                <i class="fas fa-lock input-icon"></i>
                <input type="password" class="form-control form-control-icon" name="password" placeholder="Enter your password" required>
              </div>
            </div>
            <div class="mb-3 form-check">
              <input type="checkbox" class="form-check-input" id="remember" name="remember" <%= rememberedEmail.isEmpty() ? "" : "checked" %>>
              <label for="remember" class="form-check-label">Remember my email</label>
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3"><i class="fas fa-sign-in-alt me-2"></i>Login</button>
          </form>
        </div>

        <!-- Signup -->
        <div class="tab-pane fade <%= "signup".equals(activeTab) ? "show active" : "" %>" id="signup" role="tabpanel">
          <% String signErr = (String) request.getAttribute("signupError"); %>
          <% if (signErr != null) { %>
            <div class="alert alert-danger"><i class="fas fa-exclamation-circle me-2"></i><%= signErr %></div>
          <% } %>
          <form action="<%= request.getContextPath() %>/SignupServlet" method="post" autocomplete="on" novalidate>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-user me-2"></i>Full Name</label>
              <div class="position-relative">
                <i class="fas fa-user input-icon"></i>
                <input type="text" class="form-control form-control-icon" name="fullName" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-envelope me-2"></i>Email Address</label>
              <div class="position-relative">
                <i class="fas fa-envelope input-icon"></i>
                <input type="email" class="form-control form-control-icon" name="email" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-phone me-2"></i>Phone Number</label>
              <div class="position-relative">
                <i class="fas fa-phone input-icon"></i>
                <input type="text" class="form-control form-control-icon" name="phoneNumber">
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
              <div class="position-relative">
                <i class="fas fa-lock input-icon"></i>
                <input type="password" class="form-control form-control-icon" name="password" required>
              </div>
            </div>
            <button type="submit" class="btn btn-success w-100 mb-3"><i class="fas fa-user-plus me-2"></i>Create Account</button>
          </form>
        </div>

        <!-- Admin -->
        <div class="tab-pane fade <%= "admin".equals(activeTab) ? "show active" : "" %>" id="admin" role="tabpanel">
          <% String adminError = (String) request.getAttribute("adminError"); %>
          <% if (adminError != null) { %>
            <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%= adminError %></div>
          <% } %>
          <form action="<%= request.getContextPath() %>/AdminLoginServlet" method="post" novalidate>
            <input type="hidden" name="redirect" value="<%= redirect %>">
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-envelope me-2"></i>Admin Email</label>
              <div class="position-relative">
                <i class="fas fa-envelope input-icon"></i>
                <input type="email" class="form-control form-control-icon" name="email" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
              <div class="position-relative">
                <i class="fas fa-lock input-icon"></i>
                <input type="password" class="form-control form-control-icon" name="password" required>
              </div>
            </div>
            <button type="submit" class="btn btn-danger w-100 mb-3"><i class="fas fa-user-shield me-2"></i>Login as Admin</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/auth.js"></script>
</body>
</html>
