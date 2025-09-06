<!-- src/main/webapp/views/admin/complaints.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.domesticanimalhub.model.Complaint, com.domesticanimalhub.model.ComplaintStatus, com.domesticanimalhub.model.User, com.domesticanimalhub.model.UserRole" %>
<%
  User u = (User) session.getAttribute("user");
  if (u == null || u.getUserRole() != UserRole.ADMIN) {
    response.sendRedirect(request.getContextPath()+"/views/auth.jsp?tab=admin");
    return;
  }
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<Complaint> items = (List<Complaint>) request.getAttribute("items");
  if (items==null) items = Collections.emptyList();
  @SuppressWarnings("unchecked")
  Map<String,Integer> counts = (Map<String,Integer>) request.getAttribute("counts");

  // Rename to avoid conflict with JSP implicit object "page"
  int pageNum = (request.getAttribute("page")==null)?1:(Integer)request.getAttribute("page");
  int size = (request.getAttribute("size")==null)?20:(Integer)request.getAttribute("size");
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Admin — Complaints</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/admin/dashboard"><i class="fas fa-shield-dog me-2"></i>Admin — Animal Hub</a>
    <div class="ms-auto">
      <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet">Logout</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <% String flash = (String) session.getAttribute("flashSuccess"); if (flash!=null) { %>
    <div class="alert alert-success"><i class="fas fa-check-circle me-1"></i><%=flash%></div>
    <% session.removeAttribute("flashSuccess"); %>
  <% } %>

  <div class="row g-3 mb-3">
    <% if (counts != null) {
         String[] keys = {"OPEN","IN_PROGRESS","RESOLVED","REJECTED"};
         String[] classes = {"warning","info","success","danger"};
         for (int i=0;i<keys.length;i++) {
           String k = keys[i];
           Integer v = counts.getOrDefault(k, 0);
    %>
      <div class="col-md-3">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="text-muted small"><%=k.replace("_"," ")%></div>
            <div class="display-6 fw-bold text-<%=classes[i]%>"><%=v%></div>
          </div>
        </div>
      </div>
    <% }} %>
  </div>

  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="mb-3">All Complaints</h4>
      <% if (items.isEmpty()) { %>
        <div class="alert alert-info">No complaints found.</div>
      <% } else { %>
        <div class="table-responsive">
          <table class="table align-middle">
            <thead><tr>
              <th>ID</th><th>User ID</th><th>Description</th><th>Status</th><th>Created</th><th>Updated</th><th>Actions</th>
            </tr></thead>
            <tbody>
              <% for (Complaint c : items) { %>
                <tr>
                  <td><%=c.getComplaintId()%></td>
                  <td><%=c.getUserId()%></td>
                  <td style="white-space: pre-wrap; max-width: 520px;"><%=c.getDescription()%></td>
                  <td><span class="badge bg-secondary"><%=c.getStatus()%></span></td>
                  <td><%=c.getCreatedAt()%></td>
                  <td><%=c.getUpdatedAt()==null?"—":c.getUpdatedAt()%></td>
                  <td>
                    <form class="d-flex gap-2" action="<%=ctx%>/admin/complaints/update" method="post">
                      <input type="hidden" name="complaintId" value="<%=c.getComplaintId()%>">
                      <select class="form-select form-select-sm" name="status" required>
                        <% for (ComplaintStatus st : ComplaintStatus.values()) { %>
                          <option value="<%=st.name()%>" <%= (st==c.getStatus()?"selected":"") %>><%=st.name()%></option>
                        <% } %>
                      </select>
                      <button class="btn btn-sm btn-primary" type="submit">Update</button>
                    </form>
                  </td>
                </tr>
              <% } %>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between">
          <a class="btn btn-outline-secondary <%= (pageNum<=1)?"disabled":"" %>"
             href="<%=ctx%>/admin/complaints?page=<%=pageNum-1%>&size=<%=size%>">Previous</a>
          <a class="btn btn-outline-secondary"
             href="<%=ctx%>/admin/complaints?page=<%=pageNum+1%>&size=<%=size%>">Next</a>
        </div>
      <% } %>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
