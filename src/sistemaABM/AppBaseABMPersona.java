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

		System.out.println("SISTEMA DE PERSONAS (ABM)");
		System.out.println("=========================");

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
				case 5:
					buscarXnombre(conexion, sc);
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

	/*------------------------1.ALTA DE PERSONA(METODO)---------------------------------*/

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

			System.out.println("Los datos se cargaron exitosamente:");

			stmt = conexion.createStatement();

			ResultSet rs = stmt.executeQuery(
					"SELECT ID FROM persona WHERE NOMBRE='" + nombre + "'AND FECHA_NACIMIENTO='" + fNac + "';");

			while (rs.next()) {
				int id = rs.getInt(1);
				mostrarPorID(conexion, sc, id);
			}
			conexion.close();

		} catch (SQLException e) {
			System.out.println("ALTA DE PERSONA produjo un error al cargar los datos en la base.");

		} catch (ParseException e) {
			System.out.println("La fecha ingresada es incorrecta.");
			alta(conexion, sc);
		}

	}

	/*----------------------2.MODIFICACION DE PERSONA(METODO)---------------------------*/

	private static void modificacion(Connection conexion, Scanner sc) {

		try {

			System.out.println("Ingrese el ID que desea modificar:");
			int id = sc.nextInt();
			mostrarPorID(conexion, sc, id);
			System.out.println("ingrese la columna que desea modificar");
			System.out.println("1.NOMBRE| 2.EDAD| 3.FECHA_NACIMIENTO|4.Salir");
			int col = sc.nextInt();

			while (col != 4) {

				switch (col) {
				case 1:

					System.out.println("ingrese nuevo nombre:");
					String nomNew = sc.next();
					Statement stmt = conexion.createStatement();
					String insert = "UPDATE persona SET NOMBRE = '" + nomNew + "' WHERE ID=" + id + ";";

					stmt.executeUpdate(insert);

					break;

				case 2:
					System.out.println("ingrese nueva edad:");
					int edNew = sc.nextInt();
					stmt = conexion.createStatement();
					insert = "UPDATE persona SET EDAD =" + edNew + " WHERE ID=" + id + ";";
					stmt.executeUpdate(insert);

					// verifica si la fecha de nacimiento coincide con la edad nueva
					ResultSet rs = stmt.executeQuery(
							"SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");
					Date fNac = rs.getDate(4);
					int edadBase = calcularEdad(fNac);

					if (edadBase != edNew) {
						System.out.println("Actualice la fecha de nacimiento (aaaa-mm-dd):");
						String fe = sc.next();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date fechaNac = sdf.parse(fe);
						int edad = calcularEdad(fechaNac);

						stmt = conexion.createStatement();
						insert = "UPDATE persona SET FECHA_NACIMIENTO ='" + fe + "', EDAD=" + edad + " WHERE ID=" + id
								+ ";";
						stmt.executeUpdate(insert);

					}

					break;

				case 3:
					System.out.println("Ingrese fecha nacimiento (aaaa-mm-dd):");
					String feNew = sc.next();
					// actualiza la edad en fucion de la nueva fecha de nacimiento
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date fechaNac = sdf.parse(feNew);
					int edad = calcularEdad(fechaNac);
					stmt = conexion.createStatement();
					insert = "UPDATE persona SET FECHA_NACIMIENTO ='" + feNew + "', EDAD=" + edad + " WHERE ID=" + id
							+ ";";
					stmt.executeUpdate(insert);

					break;

				default:
					break;

				}

				System.out.println("El usuario ha sido modificado exitosamente");
				mostrarPorID(conexion, sc, id);
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

	/* ------------------------3. BAJA DEPERSONA(METODO)---------------------------------*/

	private static void baja(Connection conexion, Scanner sc) {

		try {

			System.out.println("Ingrese el ID que desea borrar:");
			int id = sc.nextInt();
			mostrarPorID(conexion, sc, id);
			System.out.println("Esta seguro de que desea borrar estos datos?");
			System.out.println("1.SI| 2.NO");
			int op = sc.nextInt();

			if (op == 1) {

				Statement stmt = conexion.createStatement();
				String insert = "DELETE FROM persona WHERE ID = " + id + ";";
				stmt.executeUpdate(insert);

				System.out.println("Los datos fueron borrados exitosamente");
				listado(conexion);

			} else {
				mostrarMenu(sc);
			}

		} catch (SQLException e) {
			System.out.println("BAJA DE PERSONA produjo un error al cargar los datos en la base.");
		}
	}

	/*
	 * ----------------------------4.LISTADO(METODO)--------------------------------
	 * -
	 */

	private static void listado(Connection conexion) {
		try {

			System.out.println();
			System.out.println("-------Listado--------");
			System.out.println("ID|NOMBRE|EDAD|F.NACIM");
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERSONA");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));
			}

		} catch (SQLException e) {
			System.out.println("LISTADO produjo un error al cargar los datos en la base.");

		}

	}

	/*---------------------------- 5.BUSCAR POR NOMBRE(METODO)---------------------------------*/

	private static void buscarXnombre(Connection conexion, Scanner sc) {

		try {
			System.out.println();
			System.out.println("BUSQUEDA POR NOMBRE");
			System.out.println("-------------------");

			System.out.println("Ingrese el nombre o las primeras letras:");
			String busqueda = sc.next();

			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM persona " + "WHERE NOMBRE LIKE '" + busqueda + "%';");

			boolean sinResultados = true;
			boolean encabezado = false;

			while (rs.next()) {
				sinResultados = false;
				if (encabezado) {
					System.out.println("ID|NOMBRE|EDAD|F.NACIM");
					encabezado = true;
				}
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));
			}

			if (sinResultados) {
				System.out.println("No se encontraron resultados");
			}

			System.out.println();
		} catch (SQLException e) {
			System.out.println("BUSQUEDA POR NOMBRE produjo un error al cargar los datos en la base.");

		}

	}

	/*------------------------CALCULAR EDAD(METODO)---------------------------------*/

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

	/*------------------------MOSTRAR MENU(METODO)---------------------------------*/

	private static int mostrarMenu(Scanner sc) {

		System.out.println(
				"Menu opciones: 1- Alta |2- Modificacion |3- Baja |4- Listado |5- Buscar por nombre |0- Salir");

		int opcion = sc.nextInt();
		return opcion;
	}

	/*------------------------BUSQUEDA POR ID(METODO)-----------------------------------*/

	private static void mostrarPorID(Connection conexion, Scanner sc, int id) {

		try {

			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT ID, NOMBRE, EDAD, FECHA_NACIMIENTO FROM persona WHERE ID=" + id + ";");

			boolean sinResultados = true;
			boolean encabezado = false;

			while (rs.next()) {
				sinResultados = false;
				if (encabezado) {
					System.out.println("ID|NOMBRE|EDAD|F.NACIM");
					encabezado = true;
				}
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));
			}

			if (sinResultados) {
				System.out.println("No se encontraron resultados");
			}

		} catch (SQLException e) {
			System.out.println("El id ingresado es incorrecto");
		}
	}
}