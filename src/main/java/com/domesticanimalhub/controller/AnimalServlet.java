package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.AnimalDao;
import com.domesticanimalhub.dao.AnimalImageDao;
import com.domesticanimalhub.model.Animal;
import com.domesticanimalhub.model.AnimalImage;
import com.domesticanimalhub.model.AnimalType;
import com.domesticanimalhub.model.Gender;
import com.domesticanimalhub.model.ListingStatus;
import com.domesticanimalhub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AnimalServlet extends HttpServlet {

    private final AnimalDao animalDao = new AnimalDao();
    private final AnimalImageDao imageDao = new AnimalImageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); // can be null

        // 1) Create form
        if ("/new".equals(path)) {
            if (requireLogin(req, resp)) return;
            req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);
            return;
        }

        // 2) Edit form (only before approval & owned by user)
        if ("/edit".equals(path)) {
            if (requireLogin(req, resp)) return;
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                User me = (User) req.getSession(false).getAttribute("user");
                Animal a = animalDao.findById(id);
                if (a == null || a.getUserId() != me.getUserId()) {
                    resp.sendError(404); return;
                }
                if (a.isApproved()) {
                    req.setAttribute("err", "This listing is already approved and cannot be edited.");
                }
                req.setAttribute("editAnimal", a);
                req.setAttribute("editImages", imageDao.listByAnimal(id));
                req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(500);
            }
            return;
        }

        // 3) Browse + show "Your Pending Approvals" if logged in
        try {
            String type = req.getParameter("type");
            String breed = req.getParameter("breed");
            String location = req.getParameter("location");
            String max = req.getParameter("max");

            AnimalType at = (type == null || type.isEmpty()) ? null : AnimalType.valueOf(type);
            BigDecimal maxPrice = (max == null || max.isEmpty()) ? null : new BigDecimal(max);

            List<Animal> animals = animalDao.search(true, at, breed, null, maxPrice, location, null, 24, 0);
            req.setAttribute("animals", animals);

            HttpSession s = req.getSession(false);
            if (s != null && s.getAttribute("user") != null) {
                int userId = ((User) s.getAttribute("user")).getUserId();
                req.setAttribute("myPending", animalDao.listByUserAndApproval(userId, false));
            }

            req.getRequestDispatcher("/views/animal/list.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, "Failed to load animals");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/update".equals(path)) {
            updateListing(req, resp);
            return;
        }
        createListing(req, resp);
    }

    /* ----------------- helpers ----------------- */

    /** @return true if redirected because not logged in */
    private boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?redirect=" + req.getRequestURI());
            return true;
        }
        return false;
    }

    private void createListing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?redirect=" + req.getRequestURI());
            return;
        }

        try {
            Animal a = parseAnimalFromRequest(req);
            a.setUserId(user.getUserId());
            a.setApproved(false);
            a.setListingStatus(ListingStatus.PENDING);

            int animalId = animalDao.create(a);
            if (animalId <= 0) throw new RuntimeException("Insert failed");

            saveUploadedImages(req, animalId, false, null);

            req.setAttribute("msg", "Listing submitted! Waiting for admin approval.");
            req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("err", "Could not create listing. Please try again.");
            req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);
        }
    }

    private void updateListing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?redirect=" + req.getRequestURI());
            return;
        }
        try {
            int id = Integer.parseInt(req.getParameter("animalId"));
            Animal existing = animalDao.findById(id);
            if (existing == null || existing.getUserId() != user.getUserId()) {
                resp.sendError(404); return;
            }
            if (existing.isApproved()) {
                req.setAttribute("err", "This listing is already approved and cannot be edited.");
                req.setAttribute("editAnimal", existing);
                req.setAttribute("editImages", imageDao.listByAnimal(id));
                req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);
                return;
            }

            Animal updated = parseAnimalFromRequest(req);
            updated.setAnimalId(id);

            boolean ok = animalDao.updateIfNotApproved(updated, user.getUserId());

            // Optional image removals
            String[] removeIds = req.getParameterValues("removeImageId");
            saveUploadedImages(req, id, true, removeIds);

            if (ok) req.setAttribute("msg", "Listing updated.");
            else    req.setAttribute("err", "Could not update listing.");

            Animal fresh = animalDao.findById(id);
            req.setAttribute("editAnimal", fresh);
            req.setAttribute("editImages", imageDao.listByAnimal(id));
            req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("err", "Failed to update listing.");
            req.getRequestDispatcher("/views/animal/form.jsp").forward(req, resp);
        }
    }

    private Animal parseAnimalFromRequest(HttpServletRequest req) {
        Animal a = new Animal();
        a.setAnimalType(AnimalType.valueOf(req.getParameter("animalType")));
        a.setBreed(req.getParameter("breed"));

        String age = req.getParameter("ageYears");
        if (age != null && !age.isBlank()) a.setAgeYears(Integer.parseInt(age));

        a.setGender(Gender.valueOf(req.getParameter("gender")));

        String price = req.getParameter("price");
        if (price != null && !price.isBlank()) a.setPrice(new BigDecimal(price));

        a.setLocation(req.getParameter("location"));
        a.setDescription(req.getParameter("description"));
        a.setVaccinated("on".equalsIgnoreCase(req.getParameter("vaccinated")));
        return a;
    }

    private void saveUploadedImages(HttpServletRequest req, int animalId, boolean allowDeletes, String[] removeIds) throws Exception {
        if (allowDeletes && removeIds != null) {
            for (String rid : removeIds) {
                try { imageDao.deleteById(Integer.parseInt(rid)); } catch (NumberFormatException ignored) {}
            }
        }

        List<Part> files = new ArrayList<>();
        for (Part p : req.getParts()) {
            if ("images".equals(p.getName()) && p.getSize() > 0) {
                String mt = p.getContentType();
                if (mt != null && mt.startsWith("image/")) files.add(p);
            }
        }
        for (Part p : files) {
            try (InputStream in = p.getInputStream()) {
                byte[] data = in.readAllBytes();
                imageDao.addImage(animalId, data);
            }
        }
    }
}
