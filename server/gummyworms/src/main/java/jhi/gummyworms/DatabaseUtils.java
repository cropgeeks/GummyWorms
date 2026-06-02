package jhi.gummyworms;

import java.sql.*;

import org.apache.commons.dbcp2.*;

import jakarta.servlet.ServletContext;

public class DatabaseUtils {
    private static BasicDataSource ds;

	public static void init(ServletContext context) {
		System.out.println("init");
        String username = GummyWorms.getConfigProperty("db.username");
		String password = GummyWorms.getConfigProperty("db.password");
		String url = GummyWorms.getConfigProperty("db.url");

		if (ds == null) {
			ds = new BasicDataSource();
			ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setUrl(url);
		}
	}

	public static Connection getConnection()
		throws SQLException {
		return ds.getConnection();
	}

	public static void close() {
		try {
			ds.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
