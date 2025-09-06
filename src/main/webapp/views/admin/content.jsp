<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*,com.domesticanimalhub.model.EducationalContent" %>
<%
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<EducationalContent> items = (List<EducationalContent>) request.getAttribute("items");
  if (items == null) items = Collections.emptyList();
  String q = (String) (request.getAttribute("q") == null ? "" : request.getAttribute("q"));
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Educational Content — Admin</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>.thumb{width:120px;height:80px;object-fit:cover;background:#eee}</style>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-white border-bottom sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="<%=ctx%>/admin/dashboard">Admin</a>
    <ul class="navbar-nav me-auto">
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/dashboard">Dashboard</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/users">Users</a></li>
      <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin/animals">Animals</a></li>
      <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/admin/content">Educational Content</a></li>
    </ul>
    <a class="btn btn-outline-secondary" href="<%=ctx%>/LogoutServlet">Logout</a>
  </div>
</nav>

<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <form class="d-flex" method="get">
      <input class="form-control me-2" name="q" placeholder="Search title/description..." value="<%=q%>">
      <button class="btn btn-outline-secondary">Search</button>
    </form>
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#newModal">
      <i class="fas fa-plus me-2"></i>Add New
    </button>
  </div>

  <div class="table-responsive shadow-sm bg-white rounded">
    <table class="table align-middle mb-0">
      <thead class="table-light">
        <tr>
          <th>Image</th><th>Title</th><th>Description</th><th>Posted</th><th style="width:200px">Actions</th>
        </tr>
      </thead>
      <tbody>
      <% if (items.isEmpty()) { %>
        <tr><td colspan="5" class="text-center text-muted py-4">No content</td></tr>
      <% } %>
      <% for (EducationalContent c : items) { %>
        <tr>
          <td>
            <img class="thumb" src="<%=ctx%>/content-image?id=<%=c.getId()%>"
                 onerror="this.src='https://via.placeholder.com/120x80?text=No+Img'">
          </td>
          <td class="fw-bold"><%=c.getTitle()%></td>
          <td><div class="small text-muted" style="max-width:420px"><%=c.getDescription()%></div></td>
          <td class="small text-muted"><%=c.getCreatedAt()%></td>
          <td>
            <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal"
                    data-bs-target="#editModal<%=c.getId()%>"><i class="fas fa-pen"></i></button>
            <form class="d-inline" action="<%=ctx%>/admin/content" method="post"
                  onsubmit="return confirm('Delete this post?')">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="<%=c.getId()%>">
              <button class="btn btn-sm btn-outline-danger"><i class="fas fa-trash"></i></button>
            </form>
          </td>
        </tr>

        <!-- Edit Modal -->
        <div class="modal fade" id="editModal<%=c.getId()%>" tabindex="-1">
          <div class="modal-dialog modal-lg">
            <form class="modal-content" method="post" action="<%=ctx%>/admin/content" enctype="multipart/form-data">
              <input type="hidden" name="action" value="update">
              <input type="hidden" name="id" value="<%=c.getId()%>">
              <div class="modal-header">
                <h5 class="modal-title">Edit content</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
              </div>
              <div class="modal-body">
                <div class="mb-3">
                  <label class="form-label">Title</label>
                  <input class="form-control" name="title" value="<%=c.getTitle()%>" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Description</label>
                  <textarea class="form-control" name="description" rows="4" required><%=c.getDescription()%></textarea>
                </div>
                <div class="mb-3">
                  <label class="form-label">Replace Image (optional)</label>
                  <input class="form-control" type="file" name="image" accept="image/*">
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn btn-primary">Save</button>
              </div>
            </form>
          </div>
        </div>
      <% } %>
      </tbody>
    </table>
  </div>
</div>

<!-- New Modal -->
<div class="modal fade" id="newModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <form class="modal-content" method="post" action="<%=ctx%>/admin/content" enctype="multipart/form-data">
      <input type="hidden" name="action" value="create">
      <div class="modal-header">
        <h5 class="modal-title">Add Educational Content</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="mb-3">
          <label class="form-label">Title</label>
          <input class="form-control" name="title" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Description</label>
          <textarea class="form-control" name="description" rows="4" required></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">Image (optional)</label>
          <input class="form-control" type="file" name="image" accept="image/*">
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary">Create</button>
      </div>
    </form>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
