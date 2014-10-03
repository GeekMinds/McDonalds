package mcd.ots.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Login
 *
@WebServlet(description = "Login Page", urlPatterns = { "/Login" })
*/
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = null;//(request, response);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("https://www.mcd.com.gt/wsots/RecuperarCompanias");
        
//        WebTarget webTarget = client.target("http://192.168.100.60:8080/wscc/RecuperarCompanias");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response responseWs = invocationBuilder.get();
        JSONObject jsonResponse = null;
        JSONArray companias = null;
        JSONObject objCompanias = null;
        List<HashMap<String,String>> listaCompanias = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> mapCompania = new HashMap<String, String>();
        String responseString = responseWs.readEntity(String.class);
        try {
            jsonResponse = new JSONObject(responseString);
            //JSONPObject obj = new JS
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            try {
                String codigoRespuesta = jsonResponse.getString("codigo");
                        if(codigoRespuesta.equals("0")){
                            companias = jsonResponse.getJSONArray("companias");
                        }else{
                            dispatcher = request.getRequestDispatcher("error.jsp");
                            dispatcher.forward(request, response);
                        }
                
            } catch (JSONException w) {
                w.printStackTrace();
            }
        }
        if(companias != null){
            int cont = companias.length();
            for(int i = 0; i < cont; i++ ){
                try{
                    mapCompania = new HashMap<String, String>();
                    objCompanias = (JSONObject)companias.get(i);
                    mapCompania.put("nombre", objCompanias.getString("nombre"));
                    mapCompania.put("cod_compania", objCompanias.getString("cod_compania"));
                    listaCompanias.add(mapCompania);
                }catch(JSONException z){
                    z.printStackTrace();
                }
            }
        }
        
        if(!listaCompanias.isEmpty()){
            request.setAttribute("listaCompanias", listaCompanias);
            dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }else{
            dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
