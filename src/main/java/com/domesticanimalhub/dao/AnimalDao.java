package com.domesticanimalhub.dao;

import com.domesticanimalhub.model.*;
import com.domesticanimalhub.util.DB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDao {

    /* =========================
       CREATE
       ========================= */
    public int create(Animal a) throws SQLException {
        // default safety: if not set by caller
        if (a.getListingStatus() == null) {
            a.setListingStatus(ListingStatus.PENDING);
        }
        String sql = "INSERT INTO animals (" +
                "user_id, animal_type, breed, age_years, gender, price, location, description, health_record, " +
                "vaccinated, payment_slip, is_approved, listing_status" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getAnimalType().name());
            ps.setString(3, a.getBreed());
            if (a.getAgeYears() != null) ps.setInt(4, a.getAgeYears()); else ps.setNull(4, Types.INTEGER);
            ps.setString(5, a.getGender().name());
            if (a.getPrice() != null) ps.setBigDecimal(6, a.getPrice()); else ps.setNull(6, Types.DECIMAL);
            ps.setString(7, a.getLocation());
            ps.setString(8, a.getDescription());
            ps.setString(9, a.getHealthRecord());
            ps.setBoolean(10, a.isVaccinated());
            if (a.getPaymentSlip() != null) ps.setBytes(11, a.getPaymentSlip()); else ps.setNull(11, Types.BLOB);
            ps.setBoolean(12, a.isApproved());
            ps.setString(13, a.getListingStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    /* =========================
       READ
       ========================= */
    public Animal findById(int id) throws SQLException {
        String sql = "SELECT * FROM animals WHERE animal_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Animal> listByUser(int userId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM animals WHERE user_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Animal> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    /** List a user's animals filtered by approval flag (e.g., pending = false). */
    public List<Animal> listByUserAndApproval(int userId, boolean approved) throws SQLException {
        String sql = "SELECT * FROM animals WHERE user_id=? AND is_approved=? ORDER BY created_at DESC";
        List<Animal> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setBoolean(2, approved);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    /** Search for public/browse page. */
    public List<Animal> search(Boolean onlyApproved, AnimalType type, String breed,
                               BigDecimal minPrice, BigDecimal maxPrice, String location,
                               Boolean vaccinated, int limit, int offset) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM animals WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (onlyApproved != null && onlyApproved) {
            sb.append(" AND is_approved=TRUE AND listing_status='AVAILABLE' ");
        }
        if (type != null) { sb.append(" AND animal_type=? "); params.add(type.name()); }
        if (breed != null && !breed.isEmpty()) { sb.append(" AND breed LIKE ? "); params.add("%"+breed+"%"); }
        if (minPrice != null) { sb.append(" AND price>=? "); params.add(minPrice); }
        if (maxPrice != null) { sb.append(" AND price<=? "); params.add(maxPrice); }
        if (location != null && !location.isEmpty()) { sb.append(" AND location LIKE ? "); params.add("%"+location+"%"); }
        if (vaccinated != null) { sb.append(" AND vaccinated=? "); params.add(vaccinated); }

        sb.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<Animal> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof BigDecimal) ps.setBigDecimal(i + 1, (BigDecimal) p);
                else if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else if (p instanceof Boolean) ps.setBoolean(i + 1, (Boolean) p);
                else ps.setString(i + 1, String.valueOf(p));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public List<Animal> listPending(int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM animals " +
                "WHERE is_approved=FALSE OR listing_status='PENDING' " +
                "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Animal> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    /* =========================
       UPDATE
       ========================= */
    /** General update (owner-guarded). */
    public boolean update(Animal a) throws SQLException {
        String sql = "UPDATE animals SET " +
                "animal_type=?, breed=?, age_years=?, gender=?, price=?, location=?, " +
                "description=?, health_record=?, vaccinated=?, payment_slip=?, is_approved=?, listing_status=? " +
                "WHERE animal_id=? AND user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getAnimalType().name());
            ps.setString(2, a.getBreed());
            if (a.getAgeYears() != null) ps.setInt(3, a.getAgeYears()); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, a.getGender().name());
            if (a.getPrice() != null) ps.setBigDecimal(5, a.getPrice()); else ps.setNull(5, Types.DECIMAL);
            ps.setString(6, a.getLocation());
            ps.setString(7, a.getDescription());
            ps.setString(8, a.getHealthRecord());
            ps.setBoolean(9, a.isVaccinated());
            if (a.getPaymentSlip() != null) ps.setBytes(10, a.getPaymentSlip()); else ps.setNull(10, Types.BLOB);
            ps.setBoolean(11, a.isApproved());
            ps.setString(12, a.getListingStatus().name());
            ps.setInt(13, a.getAnimalId());
            ps.setInt(14, a.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Update fields ONLY if listing is NOT approved yet and belongs to the given user. */
    public boolean updateIfNotApproved(Animal a, int userId) throws SQLException {
        String sql = "UPDATE animals SET " +
                "animal_type=?, breed=?, age_years=?, gender=?, price=?, location=?, " +
                "description=?, vaccinated=? " +
                "WHERE animal_id=? AND user_id=? AND is_approved=FALSE";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getAnimalType().name());
            ps.setString(2, a.getBreed());
            if (a.getAgeYears() != null) ps.setInt(3, a.getAgeYears()); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, a.getGender().name());
            if (a.getPrice() != null) ps.setBigDecimal(5, a.getPrice()); else ps.setNull(5, Types.DECIMAL);
            ps.setString(6, a.getLocation());
            ps.setString(7, a.getDescription());
            ps.setBoolean(8, a.isVaccinated());
            ps.setInt(9, a.getAnimalId());
            ps.setInt(10, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Admin approve. */
    public boolean approve(int animalId) throws SQLException {
        String sql = "UPDATE animals SET is_approved=TRUE, listing_status='AVAILABLE' WHERE animal_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean markSold(int animalId) throws SQLException {
        String sql = "UPDATE animals SET listing_status='SOLD' WHERE animal_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            return ps.executeUpdate() > 0;
        }
    }

    /* =========================
       DELETE
       ========================= */
    public boolean delete(int animalId, int ownerUserId) throws SQLException {
        String sql = "DELETE FROM animals WHERE animal_id=? AND user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            ps.setInt(2, ownerUserId);
            return ps.executeUpdate() > 0;
        }
    }

    /* =========================
       MAPPER
       ========================= */
    private Animal map(ResultSet rs) throws SQLException {
        Animal a = new Animal();
        a.setAnimalId(rs.getInt("animal_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAnimalType(AnimalType.valueOf(rs.getString("animal_type")));
        a.setBreed(rs.getString("breed"));
        int age = rs.getInt("age_years");
        a.setAgeYears(rs.wasNull() ? null : age);
        a.setGender(Gender.valueOf(rs.getString("gender")));
        a.setPrice(rs.getBigDecimal("price"));
        a.setLocation(rs.getString("location"));
        a.setDescription(rs.getString("description"));
        a.setHealthRecord(rs.getString("health_record"));
        a.setVaccinated(rs.getBoolean("vaccinated"));
        a.setPaymentSlip(rs.getBytes("payment_slip"));
        a.setApproved(rs.getBoolean("is_approved"));
        a.setListingStatus(ListingStatus.valueOf(rs.getString("listing_status")));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        // updated_at may be null if you don't auto-manage it
        try { a.setUpdatedAt(rs.getTimestamp("updated_at")); } catch (SQLException ignore) {}
        return a;
    }
    
    public List<Animal> listApproved(int limit, int offset) throws Exception {
        String sql = "SELECT * FROM animals WHERE is_approved=TRUE ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit); ps.setInt(2, offset);
            List<Animal> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
            return out;
        }
    }

    public int countPending() throws Exception {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM animals WHERE is_approved=FALSE OR listing_status='PENDING'");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countApproved() throws Exception {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM animals WHERE is_approved=TRUE");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    

}
