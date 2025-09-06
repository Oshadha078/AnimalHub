package com.domesticanimalhub.dao;

import com.domesticanimalhub.model.EducationalContent;
import com.domesticanimalhub.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationalContentDao {

    private static final String TABLE = "educational_content";

    public int create(EducationalContent c) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (title, description, image, posted_by) VALUES (?,?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            if (c.getImage() != null) ps.setBytes(3, c.getImage()); else ps.setNull(3, Types.BLOB);
            if (c.getPostedBy() != null) ps.setInt(4, c.getPostedBy()); else ps.setNull(4, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean update(EducationalContent c) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET title=?, description=?, image=? WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            if (c.getImage() != null) ps.setBytes(3, c.getImage()); else ps.setNull(3, Types.BLOB);
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateWithoutImage(EducationalContent c) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET title=?, description=? WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public EducationalContent findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE + " WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<EducationalContent> listAll(int limit, int offset, String q) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + TABLE + " WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            sb.append(" AND (title LIKE ? OR description LIKE ?) ");
            params.add("%" + q + "%");
            params.add("%" + q + "%");
        }
        sb.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<EducationalContent> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else ps.setString(i + 1, String.valueOf(p));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private EducationalContent map(ResultSet rs) throws SQLException {
        EducationalContent c = new EducationalContent();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setImage(rs.getBytes("image"));
        int pb = rs.getInt("posted_by"); c.setPostedBy(rs.wasNull() ? null : pb);
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
