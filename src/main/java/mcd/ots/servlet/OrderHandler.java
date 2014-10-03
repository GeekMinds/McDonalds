package mcd.ots.servlet;


import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import java.util.Properties;

/**
 * Servlet implementation class OrderHandler
 *
@WebServlet("/OrderHandler")
*/
public class OrderHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Properties prop = new Properties();
	InputStream generalData = null;       
    
	/**
     * @see HttpServlet#HttpServlet()
     */
    public OrderHandler() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String wsResponse(WebTarget webTarget) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response responseWs = invocationBuilder.get();
        String wsResponseString = responseWs.readEntity(String.class);
        return wsResponseString;
    }

    private String wsResponse(WebTarget webTarget, Form form) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response responseWs = invocationBuilder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        String wsResponseString = responseWs.readEntity(String.class);
        return wsResponseString;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {

//    	String filename = "CommunData.properties";
//		generalData = OrderHandler.class.getClassLoader().getResourceAsStream(filename);
//		prop.load(generalData);
//		String ServerName = (String) prop.getProperty("ServerName");
    	
		
        RequestDispatcher dispatcher = null; //(request, response);
        HttpSession session = request.getSession(true);

        JSONObject jsonResponse = null; //JSON OBJECT respuesta del servicio
        JSONObject jsonResponseOrdenDetalle = null; //JSON OBJECT respuesta del servicio

        Client client = ClientBuilder.newClient(); //CLIENTE para el WS
        //WebTarget webTargetRoot = client.target(ServerName);
        WebTarget webTargetRoot = client.target("https://www.mcd.com.gt/wsots/");
        WebTarget webTarget = null; //WS especifico

        PrintWriter out = response.getWriter();
        JSONObject respuestaJson = new JSONObject();
        
        Integer codigo = Integer.parseInt(request.getParameter("cod")); //CODIGO DESDE FRONTEND
        String cod_pedido = "";
        Form form;
        String responseString = "";
        String nc = (String) session.getAttribute("no_cia");
        if (codigo != null) {
        	
        	switch (codigo) {
            case 19032:
            	
                webTarget = webTargetRoot.path("EnviarOrden");
                String numero_orden = request.getParameter("numero_orden");
                
                form = new Form();
                form.param("no_cia", nc);
                form.param("numero_orden", numero_orden);
                
                responseString = wsResponse(webTarget, form);
                try {
                    jsonResponse = new JSONObject(responseString);
                    
                    if (jsonResponse != null) {
                        String codigoRespuesta = jsonResponse.getString("codigo");
                        if (codigoRespuesta.equals("0")) {

                            respuestaJson.put("r", "1");
                            respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                        	out.print(respuestaJson.toString());
                        	
                        } else if (codigoRespuesta.equals("-1")) {
                            
                            respuestaJson.put("r", "-1");
                            respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                            out.print(respuestaJson.toString());
                        } else {

                        	respuestaJson.put("r", "0");
                            respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                            out.print(respuestaJson.toString());
                        }
                    }
                } catch (JSONException e) {

                }
                break;
                
            default:
                request.setAttribute("error", codigo);
                dispatcher = request.getRequestDispatcher("error.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }
    else 
    {
        request.setAttribute("error", "nulo");
        dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }
    }
    
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        try {
            // TODO Auto-generated method stub
           processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(Loader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

        try {
            // TODO Auto-generated method stub
            processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(Loader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
	}

}
