package sistemaABM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AppBaseABMPersona {

	public static void main(String[] args) {

		Connection conexion = null;
		try {
			conexion = AdminBD.obtenerConexion();
			Scanner sc = new Scanner(System.in);

			int opcion = mostrarMenu(sc);
			while (opcion != 0) {

				switch (opcion) {
				case 1:
					alta(conexion, sc);
					break;
				case 2:
					modificacion(conexion, sc);
					break;
				case 3:
					baja(conexion, sc);
					break;
				case 4:
					listado(conexion);
					break;
				case 0:

					break;

				default:
					break;
				}
				opcion = mostrarMenu(sc);
			}

			conexion.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Se produjo un error de coneccion con la base de datos.");
		}
	}

//---------------------------- 4.LISTADO (METODO)---------------------------------

	private static void listado(Connection conexion) {
		System.out.println();
		System.out.println("LISTADO--------------------");
		System.out.println("ID-NOMBRE-----EDAD-----F.NACIM---------");
		Statement stmt;
		try {

			stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERSONA");
			while (rs.next()) {
				Date fNac = rs.getDate(4);
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);
			}

			System.out.println("FIN LISTADO------------");
			System.out.println();
		} catch (SQLException e) {

		}

	}

//------------------------3. BAJA DE PERSONA (METODO)---------------------------------
	private static void baja(Connection conexion, Scanner sc) {

		System.out.println("BAJA DE PERSONA");
		System.out.println("---------------");
		System.out.println("Ingrese ID:");
		int id = sc.nextInt();

		Statement stmt;

		try {
			

			stmt = conexion.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");

			while (rs.next()) {
				Date fNac = rs.getDate(4);
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);

				System.out.println("Esta seguro de que desea borrar estos datos?");
				System.out.println("1.SI| 2.NO");
				int op = sc.nextInt();

				switch (op) {
				case 1:
					stmt = conexion.createStatement();
					String insert = "DELETE FROM persona WHERE ID = " + id + ";";
					stmt.executeUpdate(insert);

					System.out.println("Los datos fueron borrados exitosamente");
					listado(conexion);

					break;

				case 2:
					mostrarMenu(sc);
					break;

				default:
					break;
				}

			}

		} catch (SQLException e) {
			System.out.println("BAJA DE PERSONA produjo un error al cargar los datos en la base.");
		}

	}

//----------------------2.MODIFICACION DE PERSONA (METODO)---------------------------

	private static void modificacion(Connection conexion, Scanner sc) {

		System.out.println("MODIFICACION DE PERSONA");
		System.out.println("---------------");
		System.out.println("Ingrese ID:");
		int id = sc.nextInt();

		Statement stmt;
		try {
			
			stmt = conexion.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");

			while (rs.next()) {
				Date fNac = rs.getDate(4);
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + fNac);
			}

			System.out.println("ingrese la columna que desea modificar");
			System.out.println("1.NOMBRE| 2.EDAD| 3.FECHA_NACIMIENTO|4.Salir");
			int col = sc.nextInt();

			while (col == (1 | 2 | 3)) {

				switch (col) {
				case 1:

					System.out.println("ingrese nuevo nombre:");
					String nom = sc.next();
					stmt = conexion.createStatement();
					String insert = "UPDATE persona SET NOMBRE = '" + nom + "' WHERE ID=" + id + ";";

					stmt.executeUpdate(insert);

					break;

				case 2:
					System.out.println("ingrese nueva edad:");
					int ed = sc.nextInt();

					stmt = conexion.createStatement();
					insert = "UPDATE persona SET EDAD =" + ed + " WHERE ID=" + id + ";";

					stmt.executeUpdate(insert);

					break;

				case 3:
					System.out.println("Ingrese fecha nacimiento (aaaa-mm-dd):");
					String fe = sc.next();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date fechaNac = sdf.parse(fe);
					stmt = conexion.createStatement();
					insert = "UPDATE persona SET FECHA_NACIMIENTO ='" + fe + "' WHERE ID=" + id + ";";

					stmt.executeUpdate(insert);

					break;

				default:
					break;

				}

				rs = stmt.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");
				while (rs.next()) {
					System.out.println("El usuario ha sido modificado exitosamente");
					System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));
				}
				System.out.println("ingrese la columna que desea modificar");
				System.out.println("1.NOMBRE| 2.EDAD| 3.FECHA_NACIMIENTO|4.Salir");
				col = sc.nextInt();

				
			}
		} catch (SQLException e) {
			System.out.println("MODIFICACION DE PERSONA produjo un error al cargar los datos en la base.");
		} catch (ParseException e) {
			System.out.println("La fecha ingresada es incorrecta.");
			modificacion(conexion, sc);
		}

	}

//------------------------1.ALTA DE PERSONA (METODO)---------------------------------

	private static void alta(Connection conexion, Scanner sc) {

		System.out.println("ALTA DE PERSONA");
		System.out.println("---------------");
		System.out.println("Ingrese nombre:");
		String nombre = sc.next();
		System.out.println("Ingrese fecha nacimiento (aaaa-mm-dd):");
		String fNac = sc.next();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Statement stmt;
		try {
			Date fechaNac = sdf.parse(fNac);

			int edad = calcularEdad(fechaNac);

			
			stmt = conexion.createStatement();
			String insert = "INSERT INTO PERSONA (NOMBRE, EDAD, FECHA_NACIMIENTO) VALUES ('" + nombre + "', " + edad
					+ ", '" + fNac + "') ;";

			stmt.executeUpdate(insert);

			ResultSet rs = stmt.executeQuery("select * from persona");
			while (rs.next()) {
				int ID = rs.getInt(1);
				System.out.println(rs.getString(2) + "  " + rs.getInt(3) + "  " + rs.getDate(4));
			}

			conexion.close();

		} catch (SQLException e) {
			System.out.println("ALTA DE PERSONA produjo un error al cargar los datos en la base.");

		} catch (ParseException e) {
			System.out.println("La fecha ingresada es incorrecta.");
			alta(conexion, sc);
		}

	}

//------------------------CALCULAR EDAD (METODO)---------------------------------

	private static int calcularEdad(Date fechaNac) {

		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar hoy = new GregorianCalendar();
		gc.setTime(fechaNac);
		int anioActual = hoy.get(Calendar.YEAR);
		int anioNacim = gc.get(Calendar.YEAR);

		int mesActual = hoy.get(Calendar.MONTH);
		int mesNacim = gc.get(Calendar.MONTH);

		int diaActual = hoy.get(Calendar.DATE);
		int diaNacim = gc.get(Calendar.DATE);

		int dif = anioActual - anioNacim;

		if (mesActual < mesNacim) {
			dif = dif - 1;
		} else {
			if (mesActual == mesNacim && diaActual < diaNacim) {
				dif = dif - 1;
			}
		}

		return dif;
	}

//------------------------MOSTRAR MENU (METODO)---------------------------------

	private static int mostrarMenu(Scanner sc) {

		System.out.println("SISTEMA DE PERSONAS (ABM)");
		System.out.println("=========================");

		System.out.println("");
		System.out.println("MENU OPCIONES: ");
		System.out.println("");
		System.out.println("1: ALTA ");
		System.out.println("2: MODIFICACION ");
		System.out.println("3: BAJA");
		System.out.println("4: LISTADO");
		System.out.println("0: SALIR");
		int opcion = 0;
		opcion = sc.nextInt();
		return opcion;
	}
}
