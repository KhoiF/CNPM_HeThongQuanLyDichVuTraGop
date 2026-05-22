package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
    protected static Connection con;

    public DAO() {
        connect();
    }

    protected final void connect() {
        if (con != null) {
            return;
        }

        String dbUrl = getConfig("db.url", "DB_URL",
                "jdbc:mysql://localhost:3306/db_installment_payment?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh");
        String dbUsername = getConfig("db.user", "DB_USER", "root");
        String dbPassword = getConfig("db.password", "DB_PASSWORD", "123456");
        String dbClass = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("Kết nối database thành công!");
        } catch (Exception e) {
            System.out.println("Kết nối database thất bại!");
            e.printStackTrace();
        }
    }

    protected boolean hasConnection() {
        return con != null;
    }

    protected String getOptionalString(ResultSet rs, String columnName) throws SQLException {
        try {
            return rs.getString(columnName);
        } catch (SQLException ex) {
            return "";
        }
    }

    private String getConfig(String propertyName, String envName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            return value;
        }
        value = System.getenv(envName);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
