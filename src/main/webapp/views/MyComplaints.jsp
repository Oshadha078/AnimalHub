<!-- src/main/webapp/views/complaints/my-complaints.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.domesticanimalhub.model.Complaint" %>
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath()+"/views/login.jsp");
    return;
  }
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<Complaint> items = (List<Complaint>) request.getAttribute("items");
  if (items == null) items = Collections.emptyList();

  // Rename to avoid conflict with JSP implicit object "page"
  int pageNum = (request.getAttribute("page")==null)?1:(Integer)request.getAttribute("page");
  int size = (request.getAttribute("size")==null)?10:(Integer)request.getAttribute("size");
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>My Complaints — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg bg-white border-bottom">
  <div class="container">
    <a class="navbar-brand" href="<%=ctx%>/user/home.jsp"><i class="fas fa-paw me-2"></i>Domestic Animal Hub</a>
    <div class="d-flex">
      <a class="btn btn-outline-primary" href="<%=ctx%>/views/Complaint.jsp"><i class="fas fa-plus me-1"></i> New Complaint</a>
      <a class="btn btn-outline-primary" href="<%=ctx%>/views/home.jsp"><i class="fas fa-plus me-1"></i> Home</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <% String flash = (String) session.getAttribute("flashSuccess"); if (flash!=null) { %>
    <div class="alert alert-success"><i class="fas fa-check-circle me-1"></i><%=flash%></div>
    <% session.removeAttribute("flashSuccess"); %>
  <% } %>

  <div class="card shadow-sm">
    <div class="card-body">
      <h3 class="mb-3">My Complaints</h3>

      <% if (items.isEmpty()) { %>
        <div class="alert alert-info">You haven’t submitted any complaints yet.</div>
      <% } else { %>
        <div class="table-responsive">
          <table class="table align-middle">
            <thead>
              <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Status</th>
                <th>Created</th>
                <th>Updated</th>
              </tr>
            </thead>
            <tbody>
              <% for (Complaint c : items) { %>
                <tr>
                  <td><%=c.getComplaintId()%></td>
                  <td style="white-space: pre-wrap;"><%=c.getDescription()%></td>
                  <td><span class="badge bg-secondary"><%=c.getStatus()%></span></td>
                  <td><%=c.getCreatedAt()%></td>
                  <td><%=c.getUpdatedAt()==null?"—":c.getUpdatedAt()%></td>
                </tr>
              <% } %>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between">
          <a class="btn btn-outline-secondary <%= (pageNum<=1)?"disabled":"" %>" href="<%=ctx%>/complaints/my?page=<%=pageNum-1%>&size=<%=size%>">Previous</a>
          <a class="btn btn-outline-secondary" href="<%=ctx%>/complaints/my?page=<%=pageNum+1%>&size=<%=size%>">Next</a>
        </div>
      <% } %>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
