/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mcd.ots.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author marvinajin
 */
public class TomadoraOrdenes extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		/* TODO output your page here. You may use following sample code. */
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet TomadoraOrdenes</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Servlet TomadoraOrdenes at "
				+ request.getContextPath() + "</h1>");
		out.println("</body>");
		out.println("</html>");
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;// (request, response);
		Object loginO = session.getAttribute("login");
		String ci = request.getParameter("cliente");
		String dir = request.getParameter("dir");
		String asociada = request.getParameter("asociada");
		String pedido = request.getParameter("cod_pedido");
		String no_orden = request.getParameter("no_orden");
		if (loginO != null) {
			if (ci != null && dir != null) {
				int login = (Integer) loginO;
				if (login == 1) {
					Map<String, Object> horariosMap = new HashMap<String, Object>();
					horariosMap.put("DESAYUNO", null);
					horariosMap.put("ALMUERZO", null);
					session.setAttribute("horarios", horariosMap);
					session.setAttribute("clienteDeOrden", ci);
					session.setAttribute("direccionDeOrden", dir);
					session.setAttribute("asociada", asociada);
					Client client = ClientBuilder.newClient();
					WebTarget webTarget = client
//					.target("http://192.168.100.60:8080/wscc/RecuperarHorario");
					.target("https://www.mcd.com.gt/wsots/RecuperarHorario");
					Invocation.Builder invocationBuilder = webTarget
							.request(MediaType.APPLICATION_JSON_TYPE);

					Form form = new Form();
					String nc = (String) session.getAttribute("no_cia");
					form.param("no_cia", nc);

					Response responseWs = invocationBuilder.post(Entity.entity(
							form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
					String wsResponseString = responseWs
							.readEntity(String.class);

					JSONObject jsonResponse = null;

					try {
						jsonResponse = new JSONObject(wsResponseString);
						if (jsonResponse != null) {
							String codigoRespuesta = jsonResponse
									.getString("codigo");
							if (codigoRespuesta.equals("0")) {
								String horario = jsonResponse
										.getString("horario");
								request.setAttribute("horario", horario);
								session.setAttribute("horario", horario);
								if(no_orden == null && pedido == null){
									WebTarget webTarget2 = client
	//										.target("http://192.168.100.60:8080/wscc/RecuperarCodPedido");
											.target("https://www.mcd.com.gt/wsots/RecuperarCodPedido");
									
									Invocation.Builder invocationBuilder2 = webTarget2
											.request(MediaType.APPLICATION_JSON_TYPE);
									form = new Form();
									form.param("no_cia", nc);
									Response responseCod = invocationBuilder2
											.post(Entity
													.entity(form,
															MediaType.APPLICATION_FORM_URLENCODED_TYPE));
									String wCString = responseCod
											.readEntity(String.class);
									//
									jsonResponse = new JSONObject(wCString);
									String codigoRespuestaC = jsonResponse
											.getString("codigo");
									if (codigoRespuestaC.equals("0")) {
										JSONArray jsonresp = jsonResponse
												.getJSONArray("items");
										JSONObject jssj = jsonresp.getJSONObject(0);
										String codigoPedido = jssj
												.getString("cod_pedido");
										request.setAttribute("cod_pedido",
												codigoPedido);
										HashMap<String, Object> ordenesEnSesion = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
										if(ordenesEnSesion == null){
											ordenesEnSesion = new HashMap<String, Object>();
										}
										
										ordenesEnSesion.put(codigoPedido,
												new HashMap<String, Object>());
										session.setAttribute("ordenesEnSesion",
												ordenesEnSesion);
										dispatcher = request
												.getRequestDispatcher("tomadoraOrdenes.jsp");
										dispatcher.forward(request, response);
									} else {
										String mensaje = jsonResponse
												.getString("mensaje");
										request.setAttribute("error", mensaje);
										dispatcher = request
												.getRequestDispatcher("error.jsp");
										dispatcher.forward(request, response);
									}
								}else{
									HashMap<String, Object> ordenesEnSesion = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
									if(ordenesEnSesion == null){
										ordenesEnSesion = new HashMap<String, Object>();
									}
									
									ordenesEnSesion.put(pedido,
											new HashMap<String, Object>());
									session.setAttribute("ordenesEnSesion",
											ordenesEnSesion);
									dispatcher = request
											.getRequestDispatcher("tomadoraOrdenes.jsp");
									dispatcher.forward(request, response);
								}
							} else {
								String mensaje = jsonResponse
										.getString("mensaje");
								request.setAttribute("error", mensaje);
								dispatcher = request
										.getRequestDispatcher("error.jsp");
								dispatcher.forward(request, response);
							}
						}
						// JSONPObject obj = new JS
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					dispatcher = request.getRequestDispatcher("Login");
					dispatcher.forward(request, response);
				}
			} else {
				dispatcher = request.getRequestDispatcher("Home");
				dispatcher.forward(request, response);
			}
		} else {
			dispatcher = request.getRequestDispatcher("Login");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
