<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*,com.domesticanimalhub.model.User,com.domesticanimalhub.model.UserRole" %>
<%
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<User> users = (List<User>) request.getAttribute("users");
  if (users==null) users = Collections.emptyList();
  String q = (String) (request.getAttribute("q")==null?"":request.getAttribute("q"));
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Manage Users — Admin</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/admin/dashboard"><i class="fas fa-shield-dog me-2"></i>Admin</a>
    <ul class="navbar-nav me-auto">
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/dashboard">Dashboard</a></li>
      <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/admin/users">Users</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/animals">Animals</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/content">Educational Content</a></li>
    </ul>
    <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet">Logout</a>
  </div>
</nav>

<div class="container py-4">
  <form class="row g-2 mb-3" method="get">
    <div class="col-md-4">
      <input class="form-control" name="q" placeholder="Search name or email..." value="<%=q%>">
    </div>
    <div class="col-md-2">
      <button class="btn btn-outline-secondary w-100"><i class="fas fa-search me-2"></i>Search</button>
    </div>
  </form>

  <div class="table-responsive shadow-sm bg-white rounded">
    <table class="table align-middle mb-0">
      <thead class="table-light">
        <tr>
          <th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th>Verified</th><th style="width:220px">Actions</th>
        </tr>
      </thead>
      <tbody>
      <% if (users.isEmpty()) { %>
        <tr><td colspan="7" class="text-center text-muted py-4">No users</td></tr>
      <% } %>
      <% for (User u : users) { %>
        <tr>
          <td><%=u.getUserId()%></td>
          <td><%=u.getFullName()%></td>
          <td><%=u.getEmail()%></td>
          <td><%=u.getPhoneNumber()==null?"":u.getPhoneNumber()%></td>
          <td>
            <form class="d-inline" method="post" action="<%=ctx%>/admin/users">
              <input type="hidden" name="action" value="role">
              <input type="hidden" name="userId" value="<%=u.getUserId()%>">
              <select name="role" class="form-select form-select-sm d-inline w-auto me-2">
                <option value="CUSTOMER" <%= (u.getUserRole() == UserRole.CUSTOMER) ? "selected" : "" %>>CUSTOMER</option>
                <option value="ADMIN"    <%= (u.getUserRole() == UserRole.ADMIN)    ? "selected" : "" %>>ADMIN</option>
              </select>
              <button class="btn btn-sm btn-outline-primary">Update</button>
            </form>
          </td>
          <td>
            <form class="d-inline" method="post" action="<%=ctx%>/admin/users">
              <input type="hidden" name="action" value="verify">
              <input type="hidden" name="userId" value="<%=u.getUserId()%>">
              <input type="hidden" name="value" value="<%= u.isVerified()? "false":"true" %>">
              <button class="btn btn-sm <%= u.isVerified() ? "btn-success" : "btn-warning" %>">
                <%= u.isVerified() ? "Verified" : "Mark Verified" %>
              </button>
            </form>
          </td>
          <td>
            <form class="d-inline" method="post" action="<%=ctx%>/admin/users" onsubmit="return confirm('Delete user #<%=u.getUserId()%>?')">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="userId" value="<%=u.getUserId()%>">
              <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
            </form>
          </td>
        </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
