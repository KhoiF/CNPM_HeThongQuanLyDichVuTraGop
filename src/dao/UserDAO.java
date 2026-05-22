package dao;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO extends DAO {
    public UserDAO() {
        super();
    }

    public boolean checkLogin(User user) {
        if (!hasConnection()) {
            return false;
        }

        String sql = "SELECT * FROM tblUser WHERE username = ? AND password = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("fullName"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setPosition(rs.getString("position"));
                    user.setTel(getOptionalString(rs, "tel"));
                    user.setEmail(getOptionalString(rs, "email"));
                    user.setAddress(getOptionalString(rs, "address"));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
