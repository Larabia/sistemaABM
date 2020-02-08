package sistemaABM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminBD {

	public static Connection obtenerConexion() throws ClassNotFoundException, SQLException {
		Connection con = null;
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/schema_prueba", "root", "");
		return con;
	}

}