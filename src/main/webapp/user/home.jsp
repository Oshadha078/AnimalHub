<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.domesticanimalhub.model.User" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect(request.getContextPath()+"/views/auth.jsp");
    return;
  }
  User currentUser = (User) session.getAttribute("user");
  String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Home — Domestic Animal Hub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <link href="<%=ctx%>/assets/css/home.css" rel="stylesheet">
  <style>
    /* ---------- Global polish ---------- */
    body { background:#f7f8fb; }
    .navbar { box-shadow: 0 4px 16px rgba(0,0,0,.06); }
    .navbar .navbar-brand { font-weight: 800; letter-spacing:.2px; }
    .navbar .nav-link { font-weight: 600; }
    .dropdown-menu { border-radius: 12px; padding:.5rem; box-shadow:0 12px 30px rgba(0,0,0,.08); }
    .dropdown-item { border-radius:8px; }
    .dropdown-item:hover { background:#f0f4ff; }

    /* ---------- Hero ---------- */
    .hero-section {
      position: relative;
      z-index: 1;
      color:#fff;
      background:
        radial-gradient(1200px 500px at 80% 10%, rgba(255,255,255,.06), transparent 60%),
        linear-gradient(135deg, #1b7f5f, #0c5c8a 60%, #0c4d96);
    }
    .hero-section .container,
    .hero-section .row,
    .hero-section .col-lg-6 { position: relative; z-index: 2; }
    .hero-section::before, .hero-section::after { pointer-events: none; }

    .hero-section h1 { line-height:1.1; }
    .hero-section p.lead { color: rgba(255,255,255,.9); }

    .btn-outline-light { border-width:2px; }
    .btn-outline-light:hover { color:#0b2a3a; background:#fff; border-color:#fff; }
    .btn-light { color:#0b2a3a; }

    /* Extra CTA style if you reuse it */
    .btn-gradient {
      background: linear-gradient(90deg, #00c6ff, #0072ff);
      color: #fff !important;
      border: none;
      transition: transform .2s, box-shadow .2s;
    }
    .btn-gradient:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 18px rgba(0,114,255,.35);
    }

    /* ---------- Sections ---------- */
    .featured-section { padding:48px 0; }
    .section-title {
      font-weight:800;
      color:#1f2b3a;
      margin-bottom: 1rem;
    }

    /* Footer */
    .footer { background:#101b27; color:#9fb0c3; padding:18px 0; }
    .footer a { color:#9fb0c3; }
    .footer a:hover { color:#fff; }

    /* ---------- Chatbot UI ---------- */
    .chatbot-btn {
      position: fixed;
      right: 20px;
      bottom: 20px;
      background: linear-gradient(135deg, #1b7f5f, #0c5c8a);
      color: #fff;
      border: none;
      border-radius: 50%;
      width: 60px;
      height: 60px;
      font-size: 26px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 10px 28px rgba(0,0,0,.28);
      cursor: pointer;
      transition: transform .2s, box-shadow .2s, opacity .2s;
      z-index: 1055;
    }
    .chatbot-btn:hover {
      transform: translateY(-3px);
      box-shadow: 0 14px 30px rgba(0,0,0,.32);
    }

    .chatbot-window {
      position: fixed;
      right: 20px;
      bottom: 90px;
      width: 360px;
      max-width: calc(100% - 32px);
      height: 520px;
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 18px 48px rgba(0,0,0,.28);
      overflow: hidden;
      display: none; /* toggled via JS */
      flex-direction: column;
      z-index: 1055;
    }
    .chatbot-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 10px 12px;
      background: linear-gradient(135deg, #1b7f5f, #0c5c8a);
      color: #fff;
    }
    .chatbot-header .title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 700;
    }
    .chatbot-header button {
      border: 0;
      background: transparent;
      color: #fff;
      font-size: 18px;
      padding: 6px 8px;
      border-radius: 8px;
      transition: background .15s;
    }
    .chatbot-header button:hover { background: rgba(255,255,255,.18); }

    .chatbot-body { flex: 1; background: #f7f8fb; }
    .chatbot-iframe {
      border: 0;
      width: 100%;
      height: 100%;
      display: block;
      background: #fff;
    }

    @media (max-width: 576px) {
      .chatbot-window {
        right: 0;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 70vh;
        border-radius: 16px 16px 0 0;
      }
      .chatbot-btn { right: 16px; bottom: 16px; }
    }
  </style>
</head>
<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-light sticky-top bg-white border-bottom">
  <div class="container">
    <a class="navbar-brand" href="<%=ctx%>/user/home.jsp">
      <i class="fas fa-paw me-2 text-success"></i>Domestic Animal Hub
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="nav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link active" href="<%=ctx%>/user/home.jsp">Home</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/animals">Browse Animals</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/animals/new">List Animal</a></li>

        <!-- Complaint dropdown -->
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="complaintsMenu" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            Complaint
          </a>
          <ul class="dropdown-menu" aria-labelledby="complaintsMenu">
            <li>
               <a class="dropdown-item" href="<%=ctx%>/views/Complaint.jsp">
                <i class="fas fa-plus me-2"></i>New Complaint
              </a>
            </li>
            <li>
              <a class="dropdown-item" href="<%=ctx%>/complaints/my">
                <i class="fas fa-list me-2"></i>My Complaints
              </a>
            </li>
          </ul>
        </li>
      </ul>

      <!-- User dropdown -->
      <div class="dropdown">
        <button class="btn btn-outline-primary dropdown-toggle" data-bs-toggle="dropdown">
          <i class="fas fa-user me-1"></i> <%= currentUser.getFullName() %>
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="#"><i class="fas fa-user-circle me-1"></i>Profile</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><a class="dropdown-item" href="<%=ctx%>/LogoutServlet"><i class="fas fa-sign-out-alt me-1"></i>Logout</a></li>
        </ul>
      </div>
    </div>
  </div>
</nav>

<!-- HERO -->
<section class="hero-section py-5 text-white">
  <div class="container">
    <div class="row align-items-center">
      <div class="col-lg-6">
        <h1 class="display-5 fw-bold mb-3">Connect. Trade. Care.</h1>
        <p class="lead mb-4">Find approved listings or post your own animal safely.</p>
        <a class="btn btn-light btn-lg me-2" href="<%=ctx%>/animals">
          <i class="fas fa-search me-2"></i>Browse Animals
        </a>
        <a class="btn btn-outline-light btn-lg me-2" href="<%=ctx%>/animals/new">
          <i class="fas fa-plus me-2"></i>List Your Animal
        </a>
        <!-- UPDATED: points to /views/chatbot/index.html -->
        <a class="btn btn-outline-light btn-lg me-2" href="<%=ctx%>/views/AI/index.html">
          <i class="fas fa-robot me-2"></i>Animal AI
        </a>
      </div>
      <div class="col-lg-6 text-center">
        <i class="fas fa-dog display-1 text-white-50"></i>
        <i class="fas fa-cat display-1 text-white-50 ms-3"></i>
      </div>
    </div>
  </div>
</section>

<!-- FEATURED -->
<section class="featured-section">
  <div class="container">
    <h2 class="section-title">Latest Approved Listings</h2>
    <div class="row">
      <div class="col-12 text-center">
        <a href="<%=ctx%>/animals" class="btn btn-primary">See all</a>
      </div>
    </div>
  </div>
</section>

<!-- FOOTER -->
<footer class="footer">
  <div class="container d-flex justify-content-between">
    <span>&copy; 2024 Domestic Animal Hub</span>
    <span><a class="text-decoration-none" href="<%=ctx%>/animals/new">Post a listing</a></span>
  </div>
</footer>

<!-- CHATBOT: Floating button + popup window -->
<button id="chatbotToggle"
        class="chatbot-btn"
        type="button"
        aria-label="Open chat bot"
        title="Chat with Animal AI">
  <i class="fas fa-comments"></i>
</button>

<div id="chatbotWindow" class="chatbot-window" role="dialog" aria-modal="false" aria-labelledby="chatbotTitle">
  <div class="chatbot-header">
    <div class="title" id="chatbotTitle">
      <i class="fas fa-robot"></i>
      <span>Animal AI</span>
    </div>
    <div>
      <button type="button" id="chatbotOpenFull" title="Open full page" aria-label="Open full page">
        <i class="fas fa-external-link-alt"></i>
      </button>
      <button type="button" id="chatbotClose" title="Close" aria-label="Close chat">
        <i class="fas fa-times"></i>
      </button>
    </div>
  </div>
  <div class="chatbot-body">
    <!-- Loads your chatbot page (UPDATED path) -->
    <iframe class="chatbot-iframe" src="<%=ctx%>/views/chatbot/index.html" title="Animal AI Chat"></iframe>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  (function () {
    const toggleBtn = document.getElementById('chatbotToggle');
    const win = document.getElementById('chatbotWindow');
    const closeBtn = document.getElementById('chatbotClose');
    const openFullBtn = document.getElementById('chatbotOpenFull');
    const STORAGE_KEY = 'dah_chatbot_open';

    function setOpen(isOpen) {
      win.style.display = isOpen ? 'flex' : 'none';
      toggleBtn.setAttribute('aria-label', isOpen ? 'Close chat bot' : 'Open chat bot');
      localStorage.setItem(STORAGE_KEY, isOpen ? '1' : '0');
    }

    // Restore previous state
    try {
      const prev = localStorage.getItem(STORAGE_KEY);
      if (prev === '1') setOpen(true);
    } catch (e) {}

    toggleBtn.addEventListener('click', () => {
      const isOpen = win.style.display !== 'none' && win.style.display !== '';
      setOpen(!isOpen);
    });

    closeBtn.addEventListener('click', () => setOpen(false));

    // UPDATED: open full page to /views/chatbot/index.html
    openFullBtn.addEventListener('click', () => {
      window.location.href = "<%=ctx%>/views/chatbot/index.html";
    });

    // Close on ESC
    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') setOpen(false);
    });
  })();
</script>
</body>
</html>
