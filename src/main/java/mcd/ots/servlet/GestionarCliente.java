package mcd.ots.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
 * Servlet implementation class GestionarCliente
 *
@WebServlet("/GestionarCliente") */
public class GestionarCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionarCliente() {
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
	        Client client = ClientBuilder.newClient();
	        
	        ///CAMBIAR ROOT
//	        WebTarget webTargetRoot = client.target("http://192.168.100.60:8080/wscc");
//	        WebTarget webTarget = client.target("http://192.168.100.60:8080/wscc/RecuperarDepartamentos");
	        WebTarget webTarget = client.target("https://www.mcd.com.gt/wsots/RecuperarDepartamentos");
	        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
	        
	        Form form = new Form();
	        String nc = (String) session.getAttribute("no_cia");
            form.param("no_cia", nc);
	        
            Response responseWs = invocationBuilder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
            String wsResponseString = responseWs.readEntity(String.class);
            
	        JSONObject jsonResponse = null;
	        JSONArray departamentos = null;
	        JSONObject objDepartamentos = null;
	        List<HashMap<String,String>> listaDepartamentos = new ArrayList<HashMap<String,String>>();
	        HashMap<String, String> mapDepartamento = new HashMap<String, String>();
			
	        try {
	            jsonResponse = new JSONObject(wsResponseString);
	            //JSONPObject obj = new JS
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        if (jsonResponse != null) {
	            try {
	                String codigoRespuesta = jsonResponse.getString("codigo");
	                        if(codigoRespuesta.equals("0")){
	                            departamentos = jsonResponse.getJSONArray("departamentos");
	                        }else{
	                            dispatcher = request.getRequestDispatcher("error.jsp");
	                            dispatcher.forward(request, response);
	                        }
	                
	            } catch (JSONException w) {
	                w.printStackTrace();
	            }
	        }
	        if(departamentos != null){
	            int cont = departamentos.length();
	            for(int i = 0; i < cont; i++ ){
	                try{
	                	mapDepartamento = new HashMap<String, String>();
	                	objDepartamentos = (JSONObject)departamentos.get(i);
	                	mapDepartamento.put("nombre", objDepartamentos.getString("descripcion"));
	                	mapDepartamento.put("cod_depto", objDepartamentos.getString("cod_depto"));
	                	listaDepartamentos.add(mapDepartamento);
	                }catch(JSONException z){
	                    z.printStackTrace();
	                }
	            }
	        }
	        
	        if(!listaDepartamentos.isEmpty()){
	            request.setAttribute("listaDeptos", listaDepartamentos);
	            dispatcher = request.getRequestDispatcher("gestionarCliente.jsp");
	            dispatcher.forward(request, response);
	        }else{
	            dispatcher = request.getRequestDispatcher("error.jsp");
	            dispatcher.forward(request, response);
	        }
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
		Client client = ClientBuilder.newClient();
//		WebTarget webTarget = client.target("http://192.168.100.60:8080/wscc/RecuperarDepartamentos");
        WebTarget webTarget = client.target("https://www.mcd.com.gt/wsots/RecuperarDepartamentos");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;//(request, response);
        Form form = new Form();
        String nc = (String) session.getAttribute("no_cia");
        form.param("no_cia", nc);
        
        Response responseWs = invocationBuilder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        String wsResponseString = responseWs.readEntity(String.class);
        
        JSONObject jsonResponse = null;
        JSONArray departamentos = null;
        
        try {
            jsonResponse = new JSONObject(wsResponseString);
            //JSONPObject obj = new JS
        } catch (JSONException e) {
        	String errorMsj = jsonResponse.getString("mensaje");
            response.setContentType("application/json");
            	PrintWriter out = response.getWriter();
                Map<String, String> respuestaMap = new HashMap<String, String>();
                respuestaMap.put("data", errorMsj);
                respuestaMap.put("r", "-1");
                JSONObject respuestaJson = new JSONObject(respuestaMap);
                out.print(respuestaJson.toString());
        }
        if (jsonResponse != null) {
			try {
				String codigoRespuesta = jsonResponse.getString("codigo");
				if (codigoRespuesta.equals("0")) {
					departamentos = jsonResponse.getJSONArray("departamentos");
					if (departamentos != null) {
						response.setContentType("application/json");
						PrintWriter out = response.getWriter();
						Map<String, String> respuestaMap = new HashMap<String, String>();
						respuestaMap.put("data", departamentos.toString());
						respuestaMap.put("r", "1");
						JSONObject respuestaJson = new JSONObject(respuestaMap);
						out.print(respuestaJson.toString());
					}
				} else {
					String errorMsj = jsonResponse.getString("mensaje");
					response.setContentType("application/json");
					PrintWriter out = response.getWriter();
					Map<String, String> respuestaMap = new HashMap<String, String>();
					respuestaMap.put("data", errorMsj);
					respuestaMap.put("r", "-1");
					JSONObject respuestaJson = new JSONObject(respuestaMap);
					out.print(respuestaJson.toString());
				}
			} catch (JSONException w) {
				w.printStackTrace();
			}
		}
        	        
        	        
	}

}
