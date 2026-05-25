/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class DAO {
    protected static Connection con;

    public DAO() {
        connect();
    }

    protected final void connect() {
        if (con != null) {
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:3306/db_installment_payment?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
        String dbUsername = "root";
        String dbPassword = "123456";
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

    protected String getOptionalString(ResultSet rs, String columnName) throws SQLException {
        try {
            return rs.getString(columnName);
        } catch (SQLException ex) {
            return "";
        }
    }
}
