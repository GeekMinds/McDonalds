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
 * Servlet implementation class ScreenPop
 * 
 * @WebServlet(description = "ScreenPop Gestionador de Cliente Llamante",
 *                         urlPatterns = { "/ScreenPop" })
 */

public class ScreenPop extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScreenPop() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void loadPop(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null; // (request, response);

		dispatcher = request.getRequestDispatcher("ScreenPop.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;// (request, response);
		Object loginO = session.getAttribute("login");
		if (loginO != null) {
			int login = (Integer) loginO;
			if (login == 1) {
		loadPop(request, response);
			} else {
				dispatcher = request.getRequestDispatcher("Login");
				dispatcher.forward(request, response);
			}
		} else {
			dispatcher = request.getRequestDispatcher("Login");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;// (request, response);
		Object loginO = session.getAttribute("login");
		if (loginO != null) {
			int login = (Integer) loginO;
			if (login == 1) {
				// RESPONDER JSON CON HTML INTERNO

				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				Map<String, String> respuestaMap = new HashMap<String, String>();
				String html = "<div id=\"header\">\n" +
"                    <br style=\"clear: both\"/>\n" +
"                    <h1>industria de hamburguesas S.A.</h1>\n" +
"                </div>\n" +
"                <div class=\"ladoIzquierdo\">\n" +
"                    <span class=\"icono\">\n" +
"                        <img src=\"media/imagenes/general/iconoTelefono.png\"/>\n" +
"                    </span>\n" +
"                    <span class=\"tituloInformacion\">\n" +
"                        <label>tel&eacute;fono</label>\n" +
"                    </span>\n" +
"                    	<span class=\"textoDescripcion\">\n" +
"	                    	<input type=\"text\" id=\"PhoneNumer\" name=\"telefono\" style=\"margin-top: 10%;\" >\n" +
"                    	</span>\n" +
"                    <span class=\"icono\">\n" +
"                        <img src=\"media/imagenes/general/iconoCliente.png\"/>\n" +
"                    </span>\n" +
"                    <span class=\"tituloInformacion\">\n" +
"                        <label>Cliente</label>\n" +
"                    </span>\n" +
"                    <span class=\"textoDescripcion\">\n" +
"                        <select  id=\"listaClientes\" class=\"dropdown\">\n" +
"\n" +
"                        </select>\n" +
"                    </span>\n" +
"                    <div class=\"contenedorAcciones\">\n" +
"                        <a href=\"javascript:void(0)\" class=\"crear accion\">\n" +
"                            Crear cliente\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"editar accion\">\n" +
"                            editar cliente\n" +
"                        </a>\n" +
"                    </div>\n" +
"                    <div class=\"headerSeccion\">\n" +
"                        <div class=\"iconoHeader\">\n" +
"                            <img src=\"media/imagenes/general/iconoDireccion.png\"/>\n" +
"                        </div>\n" +
"                        <div class=\"textoHeader\">\n" +
"                            <h1>Direcciones asociadas</h1>\n" +
"                        </div>\n" +
"                    </div>\n" +
"                    <div class=\"contenedorDirecciones\">\n" +
"                        \n" +
"                    </div>\n" +
"                    <div class=\"contenedorAcciones\">\n" +
"                        <a href=\"javascript:void(0)\" class=\"crear accion\">\n" +
"                            Crear cliente\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"editar accion\">\n" +
"                            editar cliente\n" +
"                        </a>\n" +
"                    </div>\n" +
"                    <div class=\"headerSeccion\">\n" +
"                        <div class=\"iconoHeader\">\n" +
"                            <img src=\"media/imagenes/general/iconoRestaurante.png\"/>\n" +
"                        </div>\n" +
"                        <div class=\"textoHeader\">\n" +
"                            <h1>informaci&oacute;n asociada el restaurante</h1>\n" +
"                        </div>\n" +
"                    </div>\n" +
"                    <div class=\"contenedorInfoRestaurante\">\n" +
"                        <textarea placeholder=\"Observaciones asociadas al restaurante\"></textarea>\n" +
"                        <div class=\"r1\">\n" +
"                            <span class=\"restaurante\">\n" +
"                                Restaurante\n" +
"                            </span>\n" +
"                            <span class=\"lugarRestaurante\">\n" +
"                                Primma\n" +
"                            </span>\n" +
"                            <span class=\"tiempoEntrega\">\n" +
"                                Tiempo de entrega\n" +
"                            </span>\n" +
"                            <span class=\"estimado\">\n" +
"                                30 min.\n" +
"                            </span>\n" +
"                        </div>\n" +
"                    </div>\n" +
"                    <div class=\"tipologiaLlamada\">\n" +
"                        <a href=\"javascript:void(0)\">\n" +
"                            tipolog&iacute;a de llamada\n" +
"                        </a>\n" +
"                    </div>\n" +
"                </div>\n" +
"                <div class=\"ladoDerecho\">\n" +
"                    <div class=\"headerSeccion\">\n" +
"                        <div class=\"iconoHeader\">\n" +
"                            <img src=\"media/imagenes/general/iconoRestaurante.png\"/>\n" +
"                        </div>\n" +
"                        <div class=\"textoHeader\">\n" +
"                            <h1>informaci&oacute;n asociada el restaurante</h1>\n" +
"                        </div>\n" +
"                    </div>\n" +
"                    <div class=\"clasificacionCliente\">\n" +
"                        <div>\n" +
"                            <label class=\"tipoCliente\">tipo cliente</label>\n" +
"                            <label class=\"cliente\">Tipo <span id=\"typeC\"></span></label>\n" +
"                        </div>\n" +
"                    </div>\n" +
"                    <div class=\"clienteV\">\n" +
"                        <label id=\"obs\"></label>\n" +
"                    </div>\n" +
"                    <div class=\"seccionOrden\">\n" +
"                        <a href=\"javascript:void(0)\" class=\"ultimaOrden\">\n" +
"                            <span class=\"iconoOrden\">\n" +
"                                <img src=\"media/imagenes/general/iconoUltimaOrden.png\"/>\n" +
"                            </span>\n" +
"                            <span class=\"texto\">\n" +
"                                &uacute;ltima orden\n" +
"                            </span>\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"fav\">\n" +
"                            <span class=\"iconoOrden\">\n" +
"                                <img src=\"media/imagenes/general/iconoFavoritos.png\"/>\n" +
"                            </span>\n" +
"                            <span class=\"texto\">\n" +
"                                favoritos\n" +
"                            </span>\n" +
"                        </a>\n" +
"                    </div>\n" +
"                    <br style=\"clear: both\"/>\n" +
"                    <div class=\"contenedorOrdenes\">\n" +
"                        <label class=\"fechaHora\">Fecha: <span id=\"fechaHora\"></span></label>\n" +
"                        <h2 class=\"orden\">1 McMenú BigMac</h2>\n" +
"                        <label class=\"detalleOrden\">c/McPatatas</label>\n" +
"                        <label class=\"detalleOrden\">c/Coca-Cola</label>\n" +
"\n" +
"                        <h2 class=\"orden\">1 McFlury Oreo</h2>\n" +
"                    </div>\n<div style=\"display:none;\" class=\"contenedorFavoritos\">\n" +
"                        <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                         <a href=\"javascript:void(0)\" class=\"favorito\">\n" +
"                            <span class=\"textoFavorito\">Big Mac</span>\n" +
"                        </a>\n" +
"                    </div>" +
"                    <div class=\"contenedorTotalOrden\">\n" +
"                        <label class=\"totalOrden\"></label>\n" +
"                    </div>\n" +
"                    <div class=\"contenedorAcciones\">\n" +
"                        <a href=\"javascript:void(0)\" class=\"seleccionar accion\">\n" +
"                            seleccionar esta orden\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"nueva accion\">\n" +
"                            nueva orden\n" +
"                        </a>\n" +
"                        <a href=\"javascript:void(0)\" class=\"buscar accion\">\n" +
"                            buscar orden\n" +
"                        </a>\n" +
"                    </div>\n" +
"                </div>\n" +
"                <br style=\"clear: both\"/>";
				
				
				
				String modals = "<div id=\"modalTipologia\">\n" +
"            <div class=\"headerModal\">\n" +
"                <h1>Tipolog&iacute;a</h1>\n" +
"                <span></span>\n" +
"            </div>\n" +
"            <div class=\"contenedorInstrucciones\">\n" +
"                <label class=\"instrucciones\">Seleccione el tipo de llamada</label>\n" +
"                <select id=\"selectTipologia\">\n" +
"                    <option>Consulta</option>\n" +
"                    <option>Otros</option>\n" +
"                    <option>Otra Consulta</option>\n" +
"                </select>\n" +
"            </div>\n" +
"            <br style=\"clear: both\"/>\n" +
"            <textarea class=\"datosAdicionales\" placeholder=\"Datos adicionales de la llamada.\"></textarea>\n" +
"             <select id=\"selectSubTipologia\">\n" +
"                    <option>Sub categoría</option>\n" +
"                    <option>Otros</option>\n" +
"                    <option>Otra Consulta</option>\n" +
"                </select>\n" +
"            <a href=\"javascript:void(0)\" class=\"btnAceptar\">\n" +
"                aceptar\n" +
"            </a>\n" +
"        </div>" +
						"        <div id=\"modalBuscarOrden\">\n" +
						"            <div class=\"headerModal\">\n" +
						"                <h1>Buscar orden</h1>\n" +
						"                <span></span>\n" +
						"            </div>\n" +
						"            <div class=\"contenedorOrdenModal\">\n" +
						"                <div>\n" +
						"                    <label>teléfono</label>\n" +
						"                    <input type=\"text\" name=\"telefonoCliente\" id=\"telefonoCliente\"/>\n" +
						"                </div>\n" +
						"                <div>\n" +
						"                    <label>cliente</label>\n" +
						"                    <input type=\"text\" name=\"nombreCliente\" id=\"nombreCliente\" style=\"width: 181px;\"/>\n" +
						"                </div>\n" +
						"                <div>\n" +
						"                    <label>#rest.</label>\n" +
						"                    <input type=\"text\" name=\"numeroRestaurante\" id=\"numeroRestaurante\" style=\"width: 87px;\"/>\n" +
						"                </div>\n" +
						"                <div>\n" +
						"                    <label>fecha</label>\n" +
						"                    <input type=\"date\" name=\"fechaOrden\" id=\"fechaOrden\" />\n" +
						"                </div>\n" +
						"                <div>\n" +
						"                    <label>orden</label>\n" +
						"                    <input type=\"text\" name=\"numeroOrden\" id=\"numeroOrden\" style=\"width: 127px;\"/>\n" +
						"                </div>\n" +
						"                <div>\n" +
						"                    <label>origen</label>\n" +
						"                    <select id=\"origen\"> \n" +
						"                        <option>Web</option>\n" +
						"                        <option>App</option>\n" +
						"                    </select>\n" +
						"                </div>\n" +
						"                <a class=\"accionModal accionModalEdit\" href=\"javascript:void(0)\" style=\"border-right: 1px #fff solid;\">\n" +
						"                    <img src=\"media/imagenes/general/lapizModal.png\"/>\n" +
						"                </a>\n" +
						"                 <a class=\"accionModal accionModalSearch\" href=\"javascript:void(0)\">\n" +
						"                    <img src=\"media/imagenes/general/buscarOrdenModal.png\"/>\n" +
						"                </a>\n" +
						"            </div>\n" +
						"            <table>\n" +
						"                <tbody id=\"OrderList\">\n" +
						"                    \n" +
						"                </tbody>\n" +
						"            </table>\n" +
						"        </div>";
				respuestaMap.put("data", html);
				respuestaMap.put("modals", modals);
				respuestaMap.put("r", "0");
				JSONObject respuestaJson = new JSONObject(respuestaMap);
				out.print(respuestaJson.toString());

			} else {
				dispatcher = request.getRequestDispatcher("Login");
				dispatcher.forward(request, response);
			}
		} else {
			dispatcher = request.getRequestDispatcher("Login");
			dispatcher.forward(request, response);
		}

	}

}
