package com.domesticanimalhub.controller;

import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        String path = req.getServletPath();

        if (user == null) {
            String redirect = req.getContextPath() + "/views/auth.jsp?redirect=" + req.getRequestURI();
            resp.sendRedirect(redirect);
            return;
        }

        // If entering /admin/*, enforce ADMIN role
        if (path.startsWith("/admin/") && user.getUserRole() != UserRole.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only");
            return;
        }

        chain.doFilter(request, response);
    }
}
