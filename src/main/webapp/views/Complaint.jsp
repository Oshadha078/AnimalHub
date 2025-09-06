<!-- src/main/webapp/views/complaints/new.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.domesticanimalhub.model.User" %>
<%
  User current = (User) session.getAttribute("user");
  if (current == null) {
    // use the same auth page you use elsewhere
    response.sendRedirect(request.getContextPath()+"/views/auth.jsp");
    return;
  }
  String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Submit Complaint — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg bg-white border-bottom">
  <div class="container">
    <a class="navbar-brand" href="<%=ctx%>/user/home.jsp">
      <i class="fas fa-paw me-2"></i>Domestic Animal Hub
    </a>
  </div>
</nav>

<div class="container py-4">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card shadow-sm">
        <div class="card-body">
          <h3 class="mb-3">Submit a Complaint</h3>

          <% String err = (String) request.getAttribute("error"); if (err != null) { %>
            <div class="alert alert-danger"><%= err %></div>
          <% } %>

          <!-- POST to the servlet that creates the complaint -->
          <form action="<%=ctx%>/complaints/new" method="post" novalidate>
            <div class="mb-3">
              <label class="form-label">Describe your issue</label>
              <textarea class="form-control" name="description" rows="6"
                        placeholder="Explain what went wrong..." required></textarea>
            </div>

            <button class="btn btn-primary" type="submit">Submit Complaint</button>
            <!-- go to the list page handled by /complaints/my servlet -->
            <a class="btn btn-secondary ms-2" href="<%=ctx%>/complaints/my">My Complaints</a>
            <!-- back home -->
            <a class="btn btn-secondary ms-2" href="<%=ctx%>/user/home.jsp">Home</a>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
