import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ConsultaServlet extends HttpServlet {
	// El método doGet() se ejecuta una vez por cada petición HTTP GET.
	private String userName;
	private String password;
	private String url;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Establecemos el tipo MIME del mensaje de respuesta
		response.setContentType("text/html");
		// Creamos un objeto para poder escribir la respuesta
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		Statement stmt = null;
		try {
			//Cargar el driver JDBC
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			//Conectarse a la Base de Datos utilizando la clase Connection
			//URL de la base de datos (equipo, puerto, base de datos)
			conn = DriverManager.getConnection(url, userName, password);
			
			//Crear sentencias SQL, utilizando objetos de tipo Statement
			stmt = conn.createStatement();
			
			//Ejecutar las sentencias SQL a través de los objetos Statement
			String sqlStr = "select * from libros where autor = "
					+ "'" + request.getParameter("autor") + "'";
			
			//Generar una página HTML como resultado de la consulta
			out.println("<html><head><title>Resultado de la consulta</title></head><body>");
			out.println("<h3>Gracias por su consulta.</h3>");
			out.println("<p>Tu consulta es: " + sqlStr + "</p>");
			ResultSet rset = stmt.executeQuery(sqlStr);
			
			//Paso 5: Procesar el conjunto de registros resultante utilizando ResultSet
			int count = 0;
			while(rset.next()) {
				out.println("<p>" + rset.getString("autor")
				+ ", " + rset.getString("titulo")
				+ ", " + rset.getDouble("precio") + "</p>");
				
				count++;
			}
			out.println("<p>=== " + count + " registros encontrados =====</p>");
			out.println("</body></html>");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			out.close();   // Cerramos el flujo de escritura
			try {
				// Cerramos el resto de los recursos
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		//Se leen los parámetros de inicialización del contexto
		//Estos parámetros podrán ser utilizados por otros Servlet o JSP de la aplicación
		ServletContext context = config.getServletContext();
		userName = context.getInitParameter("usuario");
		password = context.getInitParameter("password");
		url = context.getInitParameter("URLBaseDeDatos");
	}
}
