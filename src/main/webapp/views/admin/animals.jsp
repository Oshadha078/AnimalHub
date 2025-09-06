<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*,com.domesticanimalhub.model.Animal" %>
<%
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<Animal> pending = (List<Animal>) request.getAttribute("pending");
  @SuppressWarnings("unchecked")
  List<Animal> approved = (List<Animal>) request.getAttribute("approved");
  if (pending==null) pending = Collections.emptyList();
  if (approved==null) approved = Collections.emptyList();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Manage Animals — Admin</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>.card-img-top{height:160px;object-fit:cover}</style>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/admin/dashboard"><i class="fas fa-shield-dog me-2"></i>Admin</a>
    <ul class="navbar-nav me-auto">
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/dashboard">Dashboard</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/users">Users</a></li>
      <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/admin/animals">Animals</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/content">Educational Content</a></li>
    </ul>
    <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet">Logout</a>
  </div>
</nav>

<div class="container py-4">
  <h5 class="mb-3">Pending Approvals</h5>
  <div class="row">
    <% if (pending.isEmpty()) { %>
      <div class="col-12"><div class="alert alert-info">No pending animals.</div></div>
    <% } %>
    <% for (Animal a : pending) { %>
      <div class="col-md-4 col-lg-3">
        <div class="card shadow-sm mb-3">
          <img src="<%=ctx%>/image?animalId=<%=a.getAnimalId()%>" class="card-img-top"
               onerror="this.src='https://via.placeholder.com/320x180?text=No+Image'">
          <div class="card-body">
            <div class="fw-bold"><%=a.getAnimalType()%> — <%= a.getBreed()==null?"":a.getBreed() %></div>
            <div class="small text-muted"><%= a.getLocation()==null?"":a.getLocation() %></div>
            <div class="d-flex gap-2 mt-2">
              <form method="post" action="<%=ctx%>/admin/animals">
                <input type="hidden" name="action" value="approve">
                <input type="hidden" name="animalId" value="<%=a.getAnimalId()%>">
                <button class="btn btn-sm btn-success"><i class="fas fa-check me-1"></i>Approve</button>
              </form>
              <form method="post" action="<%=ctx%>/admin/animals" onsubmit="return confirm('Delete listing?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="animalId" value="<%=a.getAnimalId()%>">
                <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
              </form>
            </div>
          </div>
        </div>
      </div>
    <% } %>
  </div>

  <h5 class="mt-4 mb-3">Approved</h5>
  <div class="row">
    <% if (approved.isEmpty()) { %>
      <div class="col-12"><div class="alert alert-info">No approved animals.</div></div>
    <% } %>
    <% for (Animal a : approved) { %>
      <div class="col-md-4 col-lg-3">
        <div class="card shadow-sm mb-3">
          <img src="<%=ctx%>/image?animalId=<%=a.getAnimalId()%>" class="card-img-top"
               onerror="this.src='https://via.placeholder.com/320x180?text=No+Image'">
          <div class="card-body">
            <div class="fw-bold"><%=a.getAnimalType()%> — <%= a.getBreed()==null?"":a.getBreed() %></div>
            <div class="small text-muted"><%= a.getLocation()==null?"":a.getLocation() %></div>
            <div class="d-flex gap-2 mt-2">
              <form method="post" action="<%=ctx%>/admin/animals">
                <input type="hidden" name="action" value="sold">
                <input type="hidden" name="animalId" value="<%=a.getAnimalId()%>">
                <button class="btn btn-sm btn-outline-primary"><i class="fas fa-tag me-1"></i>Mark Sold</button>
              </form>
              <form method="post" action="<%=ctx%>/admin/animals" onsubmit="return confirm('Delete listing?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="animalId" value="<%=a.getAnimalId()%>">
                <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
              </form>
            </div>
          </div>
        </div>
      </div>
    <% } %>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
