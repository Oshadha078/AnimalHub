package com.domesticanimalhub.dao;

import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import com.domesticanimalhub.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // CREATE
    public int create(User u) throws SQLException {
        String sql = "INSERT INTO users(full_name,email,password_hash,phone_number,address,user_role,is_verified,profile_image) " +
                     "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getPhoneNumber());
            ps.setString(5, u.getAddress());
            ps.setString(6, u.getUserRole().name());
            ps.setBoolean(7, u.isVerified());
            if (u.getProfileImage() != null) ps.setBytes(8, u.getProfileImage()); else ps.setNull(8, Types.BLOB);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // READ
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public User findByEmail(String email) throws Exception {
        String sql = "SELECT user_id, full_name, email, password_hash, phone_number, address, " +
                     "user_role, is_verified, profile_image, created_at, updated_at FROM users WHERE email = ?";
        try (var con = com.domesticanimalhub.util.DB.getConnection();
             var ps  = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setPhoneNumber(rs.getString("phone_number"));
                    u.setAddress(rs.getString("address"));
                    String roleStr = rs.getString("user_role");
                    u.setUserRole(roleStr == null ? com.domesticanimalhub.model.UserRole.CUSTOMER
                                                  : com.domesticanimalhub.model.UserRole.valueOf(roleStr));
                    u.setVerified(rs.getBoolean("is_verified"));
                    u.setProfileImage(rs.getBytes("profile_image"));
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    u.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return u;
                }
            }
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<User> listAll(int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<User> out = new ArrayList<>();
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

    // UPDATE
    public boolean update(User u) throws SQLException {
        String sql = "UPDATE users SET full_name=?, phone_number=?, address=?, user_role=?, is_verified=?, profile_image=? WHERE user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getPhoneNumber());
            ps.setString(3, u.getAddress());
            ps.setString(4, u.getUserRole().name());
            ps.setBoolean(5, u.isVerified());
            if (u.getProfileImage() != null) ps.setBytes(6, u.getProfileImage()); else ps.setNull(6, Types.BLOB);
            ps.setInt(7, u.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(int userId, String passwordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, passwordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean setVerified(int userId, boolean verified) throws SQLException {
        String sql = "UPDATE users SET is_verified=? WHERE user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, verified);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setPhoneNumber(rs.getString("phone_number"));
        u.setAddress(rs.getString("address"));
        u.setUserRole(UserRole.valueOf(rs.getString("user_role")));
        u.setVerified(rs.getBoolean("is_verified"));
        u.setProfileImage(rs.getBytes("profile_image"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
    
    public int countAll() throws Exception {
        try (var con = com.domesticanimalhub.util.DB.getConnection();
             var ps  = con.prepareStatement("SELECT COUNT(*) FROM users");
             var rs  = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    public boolean updateRole(int userId, UserRole role) throws Exception {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET user_role=? WHERE user_id=?")) {
            ps.setString(1, role.name());   // store as 'ADMIN' or 'CUSTOMER'
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Optional convenience overload if you ever pass raw strings elsewhere */
    public boolean updateRole(int userId, String role) throws Exception {
        return updateRole(userId, UserRole.valueOf(role.toUpperCase()));
    }
    
    public List<User> listAll(int limit, int offset, String q) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT user_id, full_name, email, password_hash, phone_number, address, " +
            "user_role, is_verified, profile_image, created_at, updated_at " +
            "FROM users"
        );
        List<Object> params = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            sql.append(" WHERE full_name LIKE ? OR email LIKE ?");
            params.add("%" + q + "%");
            params.add("%" + q + "%");
        }
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else ps.setString(i + 1, String.valueOf(p));
            }

            List<User> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setPhoneNumber(rs.getString("phone_number"));
                    u.setAddress(rs.getString("address"));
                    String roleStr = rs.getString("user_role");
                    u.setUserRole(roleStr == null ? UserRole.CUSTOMER : UserRole.valueOf(roleStr));
                    u.setVerified(rs.getBoolean("is_verified"));
                    u.setProfileImage(rs.getBytes("profile_image"));
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    u.setUpdatedAt(rs.getTimestamp("updated_at"));
                    out.add(u);
                }
            }
            return out;
        }
    }
    
}
