package mcd.ots.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class Home
 *
@WebServlet("/Home") */
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		 RequestDispatcher dispatcher = null;//(request, response);
		Object loginO = session.getAttribute("login");
		if(loginO != null){
			int login = (Integer) loginO;
		if(login==1){
			dispatcher = request.getRequestDispatcher("home.jsp");
           dispatcher.forward(request, response);
		}else{
			dispatcher = request.getRequestDispatcher("Login");
           dispatcher.forward(request, response);
		}
		}else{
			dispatcher = request.getRequestDispatcher("Login");
           dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		 RequestDispatcher dispatcher = null;//(request, response);
		Object loginO = session.getAttribute("login");
		if(loginO != null){
			int login = (Integer) loginO;
		if(login==1){
			//RESPONDER JSON CON HTML INTERNO
			String nombreEmp = (String) session.getAttribute("fullName");
        	response.setContentType("application/json");
			PrintWriter out = response.getWriter();
            Map<String, String> respuestaMap = new HashMap<String, String>();
            String html = "<div class=\"contenedorGeneral\">\n"
                    + "                    <br style=\"clear: both\"/>\n"
                    + "                    <div class=\"header\">\n"
                    + "                        <img src=\"media/imagenes/home/iconoHeader.png\" alt=\"header\"/>\n"
                    + "                        <h1>bienvenido al sistema ots</h1>\n"
                    + "                        <h2>Bienvenido, Agente " + nombreEmp + "</h2>\n"
                    + "                    </div>\n"
                    + "                </div>\n"
                    + "                <div class=\"contenedorMenu\">\n"
                    + "                    <div class=\"menu\">\n"
                    + "                        <br style=\"clear: both\"/>\n"
                    + "                        <ul>\n"
                    + "                            <li>\n"
                    + "                                <a id=\"buscarOrdenes\" href=\"OrderSearch\">\n"
                    + "                                    <img src=\"media/imagenes/home/iconoBuscarOrdenes.png\" alt=\"buscar ordenes\"/>\n"
                    + "                                    <span>buscar ordenes</span>\n"
                    + "                                </a>\n"
                    + "                            </li>\n"
                    + "                            <li>\n"
                    + "                                <a id=\"gestionarClientes\" target=\"_blank\" href=\"GestionarCliente\">\n"
                    + "                                    <img src=\"media/imagenes/home/iconoGestionarClientes.png\" alt=\"gestionar clientes\"/>\n"
                    + "                                    <span>gestionar clientes</span>\n"
                    + "                                </a>\n"
                    + "                            </li>\n"
                    + "                            <li>\n"
                    + "                                <a id=\"lanzarScreenPoP\" target=\"_blank\" href=\"ScreenPop\">\n"
                    + "                                    <img  src=\"media/imagenes/home/iconoScreenPop.png\" alt=\"screen pop\"/>\n"
                    + "                                    <span>lanzar screen pop</span>\n"
                    + "                                </a>\n"
                    + "                            </li>\n"
                    + "                        </ul>\n"
                    + "                    </div>\n"
                    + "                    <br style=\"clear: both\"/>\n"
                    + "                </div>";
            
            respuestaMap.put("data", html);
            respuestaMap.put("r", "0");
            JSONObject respuestaJson = new JSONObject(respuestaMap);
            out.print(respuestaJson.toString());
		}else{
			dispatcher = request.getRequestDispatcher("Login");
            dispatcher.forward(request, response);
		}
		}else{
			dispatcher = request.getRequestDispatcher("Login");
            dispatcher.forward(request, response);
		}
		
	}

}

