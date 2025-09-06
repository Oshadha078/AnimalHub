<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.domesticanimalhub.model.*" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath()+"/views/login.jsp?redirect="+request.getRequestURI());
    return;
  }
  String ctx = request.getContextPath();

  Animal edit = (Animal) request.getAttribute("editAnimal");
  @SuppressWarnings("unchecked")
  List<AnimalImage> editImages = (List<AnimalImage>) request.getAttribute("editImages");
  boolean editMode = (edit != null);

  // precompute values for safer output
  String breedVal = editMode ? (edit.getBreed()==null ? "" : edit.getBreed()) : "";
  String locVal   = editMode ? (edit.getLocation()==null ? "" : edit.getLocation()) : "";
  String descVal  = editMode ? (edit.getDescription()==null ? "" : edit.getDescription()) : "";
  String ageVal   = editMode && edit.getAgeYears()!=null ? String.valueOf(edit.getAgeYears()) : "";
  String priceVal = editMode && edit.getPrice()!=null ? String.valueOf(edit.getPrice()) : "";
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title><%= editMode ? "Edit Listing" : "New Listing" %></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
  <h2 class="mb-3"><%= editMode ? "Edit Listing" : "List an Animal" %></h2>

  <% String msg = (String) request.getAttribute("msg"); if (msg!=null) { %>
    <div class="alert alert-success"><%=msg%></div>
  <% } %>
  <% String err = (String) request.getAttribute("err"); if (err!=null) { %>
    <div class="alert alert-danger"><%=err%></div>
  <% } %>

  <form action="<%=ctx%>/animals<%= editMode ? "/update" : "" %>" method="post" enctype="multipart/form-data" class="row g-3">
    <% if (editMode) { %><input type="hidden" name="animalId" value="<%=edit.getAnimalId()%>"><% } %>

    <div class="col-md-4">
      <label class="form-label">Type</label>
      <select class="form-select" name="animalType" required>
        <% for (AnimalType t: AnimalType.values()) { %>
          <option value="<%=t.name()%>" <%= editMode && t==edit.getAnimalType() ? "selected" : "" %>><%=t.name()%></option>
        <% } %>
      </select>
    </div>
    <div class="col-md-4">
      <label class="form-label">Breed</label>
      <input class="form-control" name="breed" value="<%= breedVal %>">
    </div>
    <div class="col-md-4">
      <label class="form-label">Gender</label>
      <select class="form-select" name="gender" required>
        <option value="MALE"   <%= editMode && edit.getGender()==Gender.MALE   ? "selected":"" %>>MALE</option>
        <option value="FEMALE" <%= editMode && edit.getGender()==Gender.FEMALE ? "selected":"" %>>FEMALE</option>
      </select>
    </div>

    <div class="col-md-3">
      <label class="form-label">Age (years)</label>
      <input type="number" class="form-control" name="ageYears" min="0" value="<%= ageVal %>">
    </div>
    <div class="col-md-3">
      <label class="form-label">Price</label>
      <input type="number" step="0.01" class="form-control" name="price" value="<%= priceVal %>">
    </div>
    <div class="col-md-6">
      <label class="form-label">Location</label>
      <input class="form-control" name="location" required value="<%= locVal %>">
    </div>

    <div class="col-12">
      <label class="form-label">Description</label>
      <textarea class="form-control" name="description" rows="3"><%= descVal %></textarea>
    </div>

    <div class="col-md-3">
      <div class="form-check mt-4">
        <input class="form-check-input" type="checkbox" name="vaccinated" id="vaccinated"
               <%= editMode && edit.isVaccinated() ? "checked" : "" %>>
        <label class="form-check-label" for="vaccinated">Vaccinated</label>
      </div>
    </div>

    <% if (editMode && editImages != null && !editImages.isEmpty()) { %>
      <div class="col-12">
        <label class="form-label">Current Images</label>
        <div class="d-flex flex-wrap gap-3">
          <% for (AnimalImage im : editImages) { %>
            <div class="border p-2 rounded">
              <img src="<%=ctx%>/image?imageId=<%=im.getImageId()%>"
                   style="width:140px;height:90px;object-fit:cover"
                   onerror="this.src='https://via.placeholder.com/140x90?text=No+Image'">
              <div class="form-check mt-1">
                <input class="form-check-input" type="checkbox" name="removeImageId" value="<%=im.getImageId()%>" id="rm<%=im.getImageId()%>">
                <label class="form-check-label small" for="rm<%=im.getImageId()%>">Remove</label>
              </div>
            </div>
          <% } %>
        </div>
      </div>
    <% } %>

    <div class="col-12">
      <label class="form-label">Upload Images (<%= editMode ? "add more" : "up to 3" %>)</label>
      <input class="form-control" type="file" name="images" accept="image/*" multiple>
      <div class="form-text">JPG/PNG/WEBP, each ≤ 5MB.</div>
    </div>

    <div class="col-12">
      <button class="btn btn-primary" type="submit">
        <i class="fas <%= editMode ? "fa-save" : "fa-paper-plane" %> me-2"></i>
        <%= editMode ? "Save Changes" : "Submit for Approval" %>
      </button>
      <a class="btn btn-outline-secondary ms-2" href="<%=ctx%>/animals">Back</a>
    </div>
  </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
