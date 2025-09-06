<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*,com.domesticanimalhub.model.Animal,com.domesticanimalhub.model.User,com.domesticanimalhub.model.UserRole" %>
<%
  // Simple guard: only admins should view this page. (You may already enforce via filter.)
  User uDash = (session == null) ? null : (User) session.getAttribute("user");
  if (uDash == null || uDash.getUserRole() != UserRole.ADMIN) {
      response.sendRedirect(request.getContextPath() + "/views/auth.jsp?tab=admin");
      return;
  }

  String ctx = request.getContextPath();
  int totalUsers     = (request.getAttribute("totalUsers")==null)?0:(Integer)request.getAttribute("totalUsers");
  int pendingAnimals = (request.getAttribute("pendingAnimals")==null)?0:(Integer)request.getAttribute("pendingAnimals");
  int approvedAnimals= (request.getAttribute("approvedAnimals")==null)?0:(Integer)request.getAttribute("approvedAnimals");
  int totalContent   = (request.getAttribute("totalContent")==null)?0:(Integer)request.getAttribute("totalContent");
  @SuppressWarnings("unchecked")
  List<Animal> latestPending = (List<Animal>) request.getAttribute("latestPending");
  if (latestPending==null) latestPending = Collections.emptyList();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Admin Dashboard — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>
    body{background:#f7f8fa}
    .stat-card{border-radius:16px}
    .card-img-top{height:140px;object-fit:cover}
  </style>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/admin/dashboard"><i class="fas fa-shield-dog me-2"></i>Admin — Animal Hub</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav"><span class="navbar-toggler-icon"></span></button>
    <div id="nav" class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/admin/dashboard">Dashboard</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/users">Users</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/animals">Animals</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/content">Educational Content</a></li>
      <li class="nav-item"> <a class="nav-link" href="<%=request.getContextPath()%>/admin/complaints">Manage Complaints</a></li>

      </ul>
      <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet">Logout</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <div class="row g-3">
    <div class="col-md-3">
      <div class="card stat-card shadow-sm">
        <div class="card-body">
          <div class="text-muted small">Total Users</div>
          <div class="display-6 fw-bold"><%=totalUsers%></div>
        </div>
      </div>
    </div>
    <div class="col-md-3">
      <div class="card stat-card shadow-sm">
        <div class="card-body">
          <div class="text-muted small">Pending Animals</div>
          <div class="display-6 fw-bold text-warning"><%=pendingAnimals%></div>
        </div>
      </div>
    </div>
    <div class="col-md-3">
      <div class="card stat-card shadow-sm">
        <div class="card-body">
          <div class="text-muted small">Approved Animals</div>
          <div class="display-6 fw-bold text-success"><%=approvedAnimals%></div>
        </div>
      </div>
    </div>
    <div class="col-md-3">
      <div class="card stat-card shadow-sm">
        <div class="card-body">
          <div class="text-muted small">Educational Posts</div>
          <div class="display-6 fw-bold"><%=totalContent%></div>
        </div>
      </div>
    </div>
  </div>

  <h5 class="mt-4 mb-3">Latest Pending Animals</h5>
  <div class="row">
    <% if (latestPending.isEmpty()) { %>
      <div class="col-12"><div class="alert alert-info">No pending items now.</div></div>
    <% } %>
    <% for (Animal a : latestPending) { %>
      <div class="col-md-4 col-lg-3">
        <div class="card shadow-sm mb-3">
          <img src="<%=ctx%>/image?animalId=<%=a.getAnimalId()%>" class="card-img-top"
               onerror="this.src='https://via.placeholder.com/320x180?text=No+Image'">
          <div class="card-body">
            <div class="fw-bold"><%=a.getAnimalType()%> — <%= (a.getBreed()==null||a.getBreed().isBlank()?"Unknown":a.getBreed()) %></div>
            <div class="small text-muted"><i class="fas fa-map-marker-alt me-1"></i><%= a.getLocation()==null?"":a.getLocation() %></div>
            <a class="btn btn-sm btn-outline-primary mt-2" href="<%=ctx%>/admin/animals">Review</a>
          </div>
        </div>
      </div>
    <% } %>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
