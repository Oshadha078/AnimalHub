// src/main/java/com/domesticanimalhub/controller/admin/AdminContentServlet.java
package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.EducationalContentDao;
import com.domesticanimalhub.model.EducationalContent;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdminContentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final EducationalContentDao contentDao = new EducationalContentDao();

    private User ensureAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null) { resp.sendRedirect(req.getContextPath() + "/views/login.jsp?tab=admin"); return null; }
        Object u = s.getAttribute("user");
        if (!(u instanceof User) || ((User) u).getUserRole() != UserRole.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?tab=admin"); return null;
        }
        return (User) u;
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User admin = ensureAdmin(req, resp);
        if (admin == null) return;
        try {
            String q = req.getParameter("q");
            List<EducationalContent> items = contentDao.listAll(100, 0, q);
            req.setAttribute("items", items);
            req.setAttribute("q", q == null ? "" : q);
            req.getRequestDispatcher("/views/admin/content.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User admin = ensureAdmin(req, resp);
        if (admin == null) return;
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                EducationalContent c = new EducationalContent();
                c.setTitle(req.getParameter("title"));
                c.setDescription(req.getParameter("description"));

                byte[] img = null;
                Part part = req.getPart("image");
                if (part != null && part.getSize() > 0 &&
                    part.getContentType() != null && part.getContentType().startsWith("image/")) {
                    try (InputStream in = part.getInputStream()) { img = in.readAllBytes(); }
                }
                c.setImage(img);
                c.setPostedBy(admin.getUserId());          // <-- NOT NULL now
                contentDao.create(c);

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                EducationalContent c = new EducationalContent();
                c.setId(id);
                c.setTitle(req.getParameter("title"));
                c.setDescription(req.getParameter("description"));

                Part part = req.getPart("image");
                boolean hasNew = part != null && part.getSize() > 0 &&
                                 part.getContentType() != null && part.getContentType().startsWith("image/");
                if (hasNew) {
                    try (InputStream in = part.getInputStream()) { c.setImage(in.readAllBytes()); }
                    contentDao.update(c);
                } else {
                    contentDao.updateWithoutImage(c);
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                contentDao.delete(id);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/content");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
