<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.domesticanimalhub.model.Animal,com.domesticanimalhub.model.AnimalType" %>

<%
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<Animal> animals = (List<Animal>) request.getAttribute("animals");
  if (animals == null) animals = Collections.emptyList();

  @SuppressWarnings("unchecked")
  List<Animal> myPending = (List<Animal>) request.getAttribute("myPending");
  if (myPending == null) myPending = Collections.emptyList();

  String qType     = request.getParameter("type")==null?"":request.getParameter("type");
  String qBreed    = request.getParameter("breed")==null?"":request.getParameter("breed");
  String qLocation = request.getParameter("location")==null?"":request.getParameter("location");
  String qMax      = request.getParameter("max")==null?"":request.getParameter("max");
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Browse Animals — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>
    body{background:#f7f8fa}
    .card-img-top{height:200px;object-fit:cover}
    .section-h{font-weight:700}
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
        <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/animals">Browse</a></li>
      </ul>
      <a class="btn btn-primary" href="<%=ctx%>/animals/new"><i class="fas fa-plus me-2"></i>List Animal</a>
    </div>
  </div>
</nav>

<div class="container py-4">

  <% if (!myPending.isEmpty()) { %>
    <div class="mb-4">
      <h4 class="section-h mb-3">Your Pending Approvals</h4>
      <div class="row">
        <% for (Animal a : myPending) { %>
          <div class="col-md-6 col-lg-4">
            <div class="card mb-3 shadow-sm">
              <img class="card-img-top"
                   src="<%=ctx%>/image?animalId=<%=a.getAnimalId()%>"
                   alt="image"
                   onerror="this.src='https://via.placeholder.com/640x360?text=No+Image'">
              <div class="card-body">
                <h5 class="card-title"><%=a.getAnimalType()%> — <%= (a.getBreed()==null||a.getBreed().isBlank() ? "Unknown" : a.getBreed()) %></h5>
                <p class="card-text small text-muted mb-2"><i class="fas fa-map-marker-alt me-1"></i><%= (a.getLocation()==null||a.getLocation().isBlank() ? "N/A" : a.getLocation()) %></p>
                <div class="d-flex justify-content-between align-items-center">
                  <span class="fw-bold text-warning"><%= a.getListingStatus() %></span>
                  <div>
                    <a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/animals/edit?id=<%=a.getAnimalId()%>"><i class="fas fa-pen me-1"></i>Edit</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        <% } %>
      </div>
    </div>
  <% } %>

  <div class="d-flex align-items-center justify-content-between mb-3">
    <h4 class="section-h mb-0">Approved Listings</h4>
  </div>

  <!-- Search / filters -->
  <form class="row g-2 mb-4" method="get" action="<%=ctx%>/animals">
    <div class="col-md-3">
      <select class="form-select" name="type">
        <option value="">All Types</option>
        <% for (AnimalType t : AnimalType.values()) { %>
          <option value="<%=t.name()%>" <%= t.name().equals(qType) ? "selected" : "" %>><%= t.name() %></option>
        <% } %>
      </select>
    </div>
    <div class="col-md-3">
      <input class="form-control" name="breed" placeholder="Breed" value="<%=qBreed%>">
    </div>
    <div class="col-md-2">
      <input class="form-control" name="location" placeholder="Location" value="<%=qLocation%>">
    </div>
    <div class="col-md-2">
      <input class="form-control" name="max" placeholder="Max Price" value="<%=qMax%>">
    </div>
    <div class="col-md-2">
      <button class="btn btn-outline-secondary w-100"><i class="fas fa-search me-2"></i>Search</button>
    </div>
  </form>

  <div class="row">
    <% if (animals.isEmpty()) { %>
      <div class="col-12">
        <div class="alert alert-info"><i class="fas fa-info-circle me-2"></i>No animals found. Try adjusting your filters.</div>
      </div>
    <% } %>

    <% for (Animal a : animals) { %>
      <div class="col-md-4">
        <div class="card mb-4 shadow-sm">
          <img class="card-img-top"
               src="<%=ctx%>/image?animalId=<%=a.getAnimalId()%>"
               alt="image"
               onerror="this.src='https://via.placeholder.com/640x360?text=No+Image'">
          <div class="card-body">
            <h5 class="card-title"><%= a.getAnimalType() %> — <%= (a.getBreed()==null||a.getBreed().isBlank() ? "Unknown" : a.getBreed()) %></h5>
            <p class="card-text small text-muted mb-2"><i class="fas fa-map-marker-alt me-1"></i><%= (a.getLocation()==null||a.getLocation().isBlank() ? "N/A" : a.getLocation()) %></p>
            <div class="d-flex justify-content-between align-items-center">
              <span class="fw-bold text-success"><%= (a.getPrice()==null ? "-" : "$" + a.getPrice()) %></span>
              <span class="badge bg-secondary"><%= a.getListingStatus() %></span>
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
