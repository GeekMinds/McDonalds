package mcd.ots.servlet;

import java.io.FileWriter;
import java.io.IOException;
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

public class Loader extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Map<String, String> meses;

    static {
        meses = new HashMap<String, String>();
        meses.put("ENE", "01");
        meses.put("FEB", "02");
        meses.put("MAR", "03");
        meses.put("APR", "04");
        meses.put("MAY", "05");
        meses.put("JUN", "06");
        meses.put("JUL", "07");
        meses.put("AUG", "08");
        meses.put("SEP", "09");
        meses.put("OCT", "10");
        meses.put("NOV", "11");
        meses.put("DIC", "12");
    }

    private Object ordenEnSesionA;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Loader() {
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

    private String parsearFecha(String fecha) {
        if (!fecha.equals("")) {
            String[] fechaP = fecha.split("/", 0);

            String fechaR = fechaP[2] + "-" + fechaP[1] + "-" + fechaP[0];
            return fechaR;

        }
        return "";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {

        RequestDispatcher dispatcher = null; //(request, response);
        HttpSession session = request.getSession(true);

        JSONObject jsonResponse = null; //JSON OBJECT respuesta del servicio
        JSONObject jsonResponseOrdenDetalle = null; //JSON OBJECT respuesta del servicio

        Client client = ClientBuilder.newClient(); //CLIENTE para el WS
        WebTarget webTargetRoot = client.target("https://www.mcd.com.gt/wsots/");
//        WebTarget webTargetRoot = client.target("http://192.168.100.60:8080/wscc/");
        WebTarget webTarget = null; //WS especifico

        Integer codigo = Integer.parseInt(request.getParameter("cod")); //CODIGO DESDE FRONTEND
        String cod_pedido = "";
        Form form;
        String responseString = "";
        String nc = (String) session.getAttribute("no_cia");
        if (codigo != null) {
            ArrayList<Integer> _cod_pro = null;
            ArrayList<Integer> _clase = null;
            ArrayList<Integer> _item = null;
            ArrayList<Integer> _linea = null;
            ArrayList<String> _orden = null;
            ArrayList<Integer> _cantidad = null;
            ArrayList<String> _es_postre = null;
            ArrayList<String> _tiene_agregado = null;
            ArrayList<String> _agregado_de = null;
            ArrayList<String> _ingrediente_de = null;
            ArrayList<String> _tipo = null;
            ArrayList<String> _subtipo = null;
            ArrayList<Integer> _cantidad_ingrediente = null;
            switch (codigo) {
                case 1923:
                    webTarget = webTargetRoot.path("Login");
                    String no_cia = request.getParameter("compania");
                    String no_emple = request.getParameter("user");
                    String password = request.getParameter("pass");
                    form = new Form();
                    form.param("no_cia", no_cia);
                    form.param("no_emple", no_emple);
                    form.param("password", password);
                    responseString = wsResponse(webTarget, form);
                    try {
                        jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                        if (jsonResponse != null) {
                            String codigoRespuesta = jsonResponse.getString("codigo");
                            if (codigoRespuesta.equals("0")) {
                                String nombreEmp = jsonResponse.getString("nombre");
                                session.setAttribute("login", new Integer(1));
                                session.setAttribute("fullName", nombreEmp);
                                session.setAttribute("no_cia", no_cia);
                                session.setAttribute("OpId", no_emple);
                                dispatcher = request.getRequestDispatcher("Home");
                                dispatcher.forward(request, response);
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
                        }
                    } catch (JSONException e) {

                    }
                    break;
                case 2014:
                    //JSONObject jsonResponse = null; //JSON OBJECT respuesta del servicio
                    String telefono = request.getParameter("telefono"); // Obetener el numero de telefono
                    if (telefono != null) {
                        JSONArray clientes;
                        JSONObject objCliente;
                        form = new Form();
                        webTarget = webTargetRoot.path("BuscarClientePorTelefono"); // Web Service a Utilizar
                        form.param("telefono", telefono);
                        form.param("grupo", "03");
                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString);
                            if (jsonResponse != null) {
                                PrintWriter out = response.getWriter();
                                response.setContentType("application/json");
                                Map<String, String> respuestaMap = new HashMap<String, String>();
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    //response.setContentType("text/plain;charset=UTF-8");
                                    response.setCharacterEncoding("UTF-8");
                                    clientes = jsonResponse.getJSONArray("clientes");
                                    respuestaMap.put("data", clientes.toString());
                                    respuestaMap.put("r", "1");
                                    respuestaMap.put("tele", telefono);
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                } else {
                                    String wsError = jsonResponse.getString("mensaje");
                                    respuestaMap.put("data", wsError);
                                    respuestaMap.put("r", "-1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                }
                            }
                        } catch (JSONException e) {

                        }
                    } else {
                        PrintWriter out = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", "No se puede leer #telefono");
                        respuestaMap.put("r", "-1");
                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                        out.print(respuestaJson.toString());
                    }
                    break;
                case 1988:
                    //BuscarDirecccionesPorCliente
                    String elCliente = request.getParameter("ic");
                    if (elCliente != null) {
                        JSONArray clientDirecctions = null;
                        webTarget = webTargetRoot.path("BuscarDireccionesPorCliente"); // Web Service a Utilizar    
                        form = new Form();
                        form.param("no_cliente", elCliente);
                        form.param("grupo", "03");
                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString);

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    clientDirecctions = jsonResponse.getJSONArray("direcciones");
                                    respuestaMap.put("data", clientDirecctions.toString());
                                    respuestaMap.put("r", "1");

                                    webTarget = webTargetRoot.path("EsVip");
                                    responseString = wsResponse(webTarget, form);
                                    jsonResponse = new JSONObject(responseString);

                                    if (jsonResponse != null) {
                                        String vCod = jsonResponse.getString("codigo");
                                        if (vCod.equals("0")) {
                                            String vObs = jsonResponse.getString("observacion");
                                            String vStatus = jsonResponse.getString("status");
                                            String vMensaje = jsonResponse.getString("mensaje");
                                            String vTipo = jsonResponse.getString("tipo_cliente");
                                            respuestaMap.put("obs", vObs.toString());
                                            respuestaMap.put("status", vStatus.toString());
                                            respuestaMap.put("mensaje", vMensaje.toString());
                                            respuestaMap.put("tipo", vTipo.toString());
                                            respuestaMap.put("v", "1");
                                        } else {
                                            String wsError = jsonResponse.getString("mensaje");
                                            respuestaMap.put("v-data", wsError);
                                            respuestaMap.put("v", "2");
                                        }
                                    }

//                                    webTarget = webTargetRoot.path("RecuperarUltimaOrden");
//                                    responseString = wsResponse(webTarget, form);
//                                    jsonResponse = new JSONObject(responseString);
//                                    if (jsonResponse != null) {
//                                        String vCod = jsonResponse.getString("codigo");
//                                        if (vCod.equals("0")) {
//                                            respuestaMap.put("lFecha", jsonResponse.getString("fecha"));
//                                            String htmlDetalle = jsonResponse.getString("detalle");
//                                            respuestaMap.put("lDetalle", htmlDetalle);
//
//                                            respuestaMap.put("lFecha", jsonResponse.getString("fecha"));
//                                            respuestaMap.put("lTotal", jsonResponse.getString("total"));
//
//                                            respuestaMap.put("l", "1");
//
//                                        } else {
//                                            String wsError = jsonResponse.getString("mensaje");
//                                            respuestaMap.put("l-data", wsError);
//                                            respuestaMap.put("l", "2");
//                                        }
//                                    }
//                                    form = new Form();
//                                    form.param("no_cliente", elCliente);
//                                    form.param("no_cia", nc);
//                                    webTarget = webTargetRoot.path("RecuperaFavoritos");
//
//                                    responseString = wsResponse(webTarget, form);
//                                    jsonResponse = new JSONObject(responseString);
//                                    String codFavs = jsonResponse.getString("codigo");
//                                    if (codFavs.equals("0")) {
//                                        respuestaMap.put("f", "1");
//                                        JSONArray codigosFav = jsonResponse.getJSONArray("items");
//                                        respuestaMap.put("favs", codigosFav.toString());
//                                    } else {
//                                        String wsError = jsonResponse.getString("mensaje");
//                                        respuestaMap.put("f-data", wsError);
//                                        respuestaMap.put("f", "2");
//                                    }
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                } else {
                                    PrintWriter out = response.getWriter();
                                    response.setContentType("application/json");
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    String wsError = jsonResponse.getString("mensaje");
                                    respuestaMap.put("r-data", wsError);
                                    respuestaMap.put("r", "-1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }
                    break;
                case 334:
                    form = new Form();
                    String cod_clienteV = request.getParameter("cliente");
                    if (cod_clienteV.equals("")) {
                        form.param("no_cliente", cod_clienteV);
                        form.param("grupo", "03");
                        form.param("no_cia", nc);
                        webTarget = webTargetRoot.path("EsVip");
                        responseString = wsResponse(webTarget, form);
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            String vCod = jsonResponse.getString("codigo");
                            if (vCod.equals("0")) {
                                String vObs = jsonResponse.getString("observacion");
                                String vStatus = jsonResponse.getString("status");
                                String vMensaje = jsonResponse.getString("mensaje");
                                String vTipo = jsonResponse.getString("tipo_cliente");
                                Map<String, String> respuestaMap = new HashMap<String, String>();
                                respuestaMap.put("obs", vObs);
                                respuestaMap.put("status", vStatus);
                                respuestaMap.put("mensaje", vMensaje);
                                respuestaMap.put("tipo", vTipo);
                                PrintWriter out = response.getWriter();
                                respuestaMap.put("r", "1");
                                JSONObject respuestaJson = new JSONObject(respuestaMap);
                                out.print(respuestaJson.toString());
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
                        }
                    } else {
                        response.setContentType("application/json");
                        PrintWriter out = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", "Los datos proporcionados no contienen datos");
                        respuestaMap.put("r", "-1");
                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                        out.print(respuestaJson.toString());
                    }
                    break;
                case 1992:
                    //Buscar Restaurante para Direccion
                    JSONArray RestaurantAs = null;
                    JSONObject objRA = null;
                    HashMap<String, String> mapRA = new HashMap<String, String>();
                    client = ClientBuilder.newClient(); //CLIENTE para el WS
                    webTarget = webTargetRoot.path("TiempoDeServicio"); // Web Service a Utilizar
                    String restAs = request.getParameter("ra");

                    responseString = "";

                    if (restAs != null) {
                        form = new Form();
                        form.param("tienda", restAs);
                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString);
                            PrintWriter out = response.getWriter();
                            Map<String, String> respuestaMap = new HashMap<String, String>();

                            if (jsonResponse != null) {
                                response.setContentType("application/json");
                                String codRes = jsonResponse.getString("codigo");
                                if (codRes.equals("0")) {
                                    String NombreResta = jsonResponse.getString("nombre_restaurante");
                                    String TiempoServ = jsonResponse.getString("tiempo_servicio");
                                    respuestaMap.put("nombre", NombreResta.toString());
                                    respuestaMap.put("tiempo", TiempoServ);
                                    respuestaMap.put("r", "1");
                                } else {
                                    String wsError = jsonResponse.getString("mensaje");
                                    respuestaMap.put("r-data", wsError);
                                    respuestaMap.put("r", "-1");
                                }
                            }
                            webTarget = webTargetRoot.path("RecuperarObservacionesDelRestaurante");
                            form = new Form();
                            form.param("tienda", restAs);

                            form.param("no_cia", nc);
                            responseString = wsResponse(webTarget, form);

                            jsonResponse = new JSONObject(responseString);

                            if (jsonResponse != null) {
                                String codRes2 = jsonResponse.getString("codigo");
                                if (codRes2.equalsIgnoreCase("0")) {
                                    String observaciones_promocion = jsonResponse.getString("observaciones_promocion");
                                    String observaciones_servicio = jsonResponse.getString("observaciones_servicio");
                                    String observaciones_producto = jsonResponse.getString("observaciones_producto");
                                    respuestaMap.put("observaciones_promocion", observaciones_promocion);
                                    respuestaMap.put("observaciones_servicio", observaciones_servicio);
                                    respuestaMap.put("observaciones_producto", observaciones_producto);
                                    respuestaMap.put("o", "1");
                                } else {
                                    String wsError = jsonResponse.getString("mensaje");
                                    respuestaMap.put("o-data", wsError);
                                    respuestaMap.put("o", "-1");
                                }
                            }

                            JSONObject respuestaJson = new JSONObject(respuestaMap);
                            out.print(respuestaJson.toString());
                        } catch (JSONException e) {

                        }
                    }

                    break;
                case 1960:
                    //Buscar Tipologias
                    JSONArray Tipologias = null;
                    JSONObject ObjTipologias = null;
                    client = ClientBuilder.newClient(); //CLIENTE para el WS
                    webTarget = webTargetRoot.path("RecuperarTipologias"); // Web Service a Utilizar
                    form = new Form();
                    responseString = "";

                    form.param("no_cia", nc);
                    responseString = wsResponse(webTarget, form);
                    try {
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            response.setContentType("application/json");
                            PrintWriter out = response.getWriter();
                            Map<String, String> respuestaMap = new HashMap<String, String>();
                            String codTip = jsonResponse.getString("codigo");
                            if (codTip.equals("0")) {
                                Tipologias = jsonResponse.getJSONArray("tipologias");
                                respuestaMap.put("data", Tipologias.toString());
                                respuestaMap.put("t", "1");
                            } else {
                                String errMsj = jsonResponse.getString("mensaje");
                                respuestaMap.put("data", errMsj.toString());
                                respuestaMap.put("t", "-1");
                            }
                            JSONObject respuestaJson = new JSONObject(respuestaMap);
                            out.print(respuestaJson.toString());
                        }
                    } catch (JSONException e) {

                    }
                    break;
                case 2011:
                    JSONArray Ordenes = null;
                    client = ClientBuilder.newClient(); //CLIENTE para el WS
                    webTarget = webTargetRoot.path("RecuperarOrdenes"); // Web Service a Utilizar
                    form = new Form();

                    responseString = "";

                    String vTel = request.getParameter("tel");
                    String vCli = request.getParameter("cli");
                    String nRes = request.getParameter("rest");
                    String vDate = request.getParameter("fecha");
                    String nOrd = request.getParameter("ord");
                    String nOrg = request.getParameter("org");

                    form.param("telefono", vTel);
                    form.param("nombre_cliente", vCli);
                    form.param("tienda", nRes);
                    form.param("fecha", vDate);
                    form.param("orden", nOrd);
                    form.param("origen", nOrg);

                    form.param("no_cia", nc);
                    responseString = wsResponse(webTarget, form);
                    try {
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            response.setContentType("application/json");
                            PrintWriter out = response.getWriter();
                            Map<String, String> respuestaMap = new HashMap<String, String>();
                            String codTip = jsonResponse.getString("codigo");
                            if (codTip.equals("0")) {

                                Ordenes = jsonResponse.getJSONArray("ordenes");
                                respuestaMap.put("data", Ordenes.toString());
                                respuestaMap.put("ord", "1");
                            } else {
                                String errMsj = jsonResponse.getString("mensaje");
                                respuestaMap.put("data", errMsj.toString());
                                respuestaMap.put("ord", "-1");
                            }
                            JSONObject respuestaJson = new JSONObject(respuestaMap);
                            out.print(respuestaJson.toString());
                        }
                    } catch (JSONException e) {

                    }
                    break;
                case 1010:
                    //RecuperarMunicipios por Departamento
                    webTarget = webTargetRoot.path("RecuperarMunicipios");
                    form = new Form();
                    JSONArray municipios = null;
                    String cod_depto = request.getParameter("idDepto");
                    if (cod_depto != null) {
                        form.param("cod_depto", cod_depto);

                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    municipios = jsonResponse.getJSONArray("municipios");
                                    if (municipios != null) {
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", municipios.toString());
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
                            }
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
                    }
                    break;
                case 1011:
                    //RecuperarZonas por Municipio
                    webTarget = webTargetRoot.path("RecuperarZonas");
                    form = new Form();
                    JSONArray zonas = null;
                    String cod_deptoZ = request.getParameter("idDepto");
                    String cod_municipio = request.getParameter("idMuni");
                    if (cod_municipio != null) {
                        form.param("cod_depto", cod_deptoZ);
                        form.param("cod_municipio", cod_municipio);

                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    zonas = jsonResponse.getJSONArray("zonas");
                                    if (zonas != null) {
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", zonas.toString());
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
                            }
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
                    }
                    break;
                case 1012:
                    //RecuperarColonias por Zona
                    webTarget = webTargetRoot.path("RecuperarColonias");
                    form = new Form();
                    JSONArray colonias = null;
                    String cod_deptoC = request.getParameter("idDepto");
                    String cod_municipioC = request.getParameter("idMuni");
                    String cod_zona = request.getParameter("idZona");
                    if (cod_zona != null && cod_deptoC != null && cod_municipioC != null) {
                        form.param("cod_depto", cod_deptoC);
                        form.param("cod_municipio", cod_municipioC);
                        form.param("cod_zona", cod_zona);

                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    colonias = jsonResponse.getJSONArray("colonias");
                                    if (colonias != null) {
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", colonias.toString());
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
                            }
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
                    }
                    break;
                case 1013:
                    //RecuperarRestauranteAsignado por direccion, dpto, muni, zona y/o col
                    webTarget = webTargetRoot.path("RecuperarRestauranteAsignado");
                    form = new Form();
                    JSONArray restaurantes = null;
                    String cod_deptoR = request.getParameter("idDepto");
                    String cod_municipioR = request.getParameter("idMuni");
                    String cod_zonaR = request.getParameter("idZona");
                    String cod_colonia = request.getParameter("idCol");
                    if (cod_colonia != null && cod_zonaR != null && cod_deptoR != null && cod_municipioR != null) {
                        form.param("cod_depto", cod_deptoR);
                        form.param("cod_municipio", cod_municipioR);
                        form.param("cod_zona", cod_zonaR);
                        form.param("cod_colonia", cod_colonia);

                        form.param("no_cia", nc);
                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    restaurantes = jsonResponse.getJSONArray("restaurantes");
                                    if (restaurantes != null) {
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", restaurantes.toString());
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
                            }
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
                    }
                    break;
                case 9898:
                    //RecuperarDireccion por su ID
                    webTarget = webTargetRoot.path("RecuperarDireccion");
                    form = new Form();
                    JSONObject direccion = null;
                    String cod_clienteD = request.getParameter("cliente");
                    String telefonoD = request.getParameter("dir");
                    if (telefonoD != null && cod_clienteD != null) {
                        form.param("no_cliente", cod_clienteD);
                        form.param("telefono", telefonoD);

                        form.param("no_cia", nc);
                        form.param("grupo", "03");

                        responseString = wsResponse(webTarget, form);
                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    direccion = new JSONObject();
                                    direccion.put("direccion", jsonResponse.getString("direccion_completa"));
                                    direccion.put("referencia", jsonResponse.getString("referencia"));
                                    direccion.put("tipo", jsonResponse.getString("tipo_telefono"));
                                    direccion.put("depto", jsonResponse.getString("cod_depto"));
                                    direccion.put("muni", jsonResponse.getString("cod_municipio"));
                                    direccion.put("zona", jsonResponse.getString("cod_zona"));
                                    direccion.put("col", jsonResponse.getString("cod_colonia"));
                                    direccion.put("asignado", jsonResponse.getString("restaurante_asignado"));
                                    direccion.put("telefono", telefonoD);
                                    response.setContentType("application/json");
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    respuestaMap.put("data", direccion.toString());
                                    respuestaMap.put("r", "1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
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
                            }
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
                    }

                    break;
                case 1099:
                    //RecuperarCliente por su ID
                    webTarget = webTargetRoot.path("RecuperarCliente");
                    form = new Form();
                    JSONObject cliente = null;
                    String cod_cliente = request.getParameter("cliente");
                    if (cod_cliente != null) {

                        form.param("no_cliente", cod_cliente);

                        form.param("no_cia", nc);
                        form.param("grupo", "03");

                        responseString = wsResponse(webTarget, form);

                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    cliente = new JSONObject();
                                    cliente.put("c_nombre", jsonResponse.getString("primer_nombre") + " " + jsonResponse.getString("segundo_nombre"));
                                    cliente.put("c_ape", jsonResponse.getString("primer_apellido") + " " + jsonResponse.getString("segundo_apellido"));
                                    cliente.put("c_mail", jsonResponse.getString("email"));
                                    cliente.put("c_fac", jsonResponse.getString("nombre_factura"));
                                    cliente.put("c_nit", jsonResponse.getString("nit_factura"));
                                    cliente.put("c_tel", jsonResponse.getString("telefono"));
                                    String fechaParse = parsearFecha(jsonResponse.getString("fecha_nacimiento"));
                                    if (!fechaParse.equals("")) {
                                        cliente.put("c_fecha", fechaParse);
                                    } else {
                                        cliente.put("c_fecha", "2000-01-01");
                                    }
                                    response.setContentType("application/json");
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    respuestaMap.put("data", cliente.toString());
                                    respuestaMap.put("r", "1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
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
                            }
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
                    }

                    break;
                case 710:
                    //InsertarCliente nuevo o existente
                    webTarget = webTargetRoot.path("InsertarCliente");
                    form = new Form();
                    String clienteID = "";
                    String cod_clienteI = request.getParameter("clienteGes");
                    String nombresC = request.getParameter("nombres");
                    String apellidosC = request.getParameter("apellidos");
                    String telefonoC = request.getParameter("telefonoGest");
                    String nombreFacturacion = request.getParameter("nombreFacturacion");
                    String nit = request.getParameter("nit");
                    String emailC = request.getParameter("email");
                    String fechaNacimiento = request.getParameter("fechaNacimiento");
                    if (fechaNacimiento != null && !fechaNacimiento.equalsIgnoreCase("")) {
                        String[] f = fechaNacimiento.split("-");
                        fechaNacimiento = f[2] + f[1] + f[0];
                    } else {
                        fechaNacimiento = "";
                    }
                    String[] nombres = nombresC.split(" ", 0);
                    String[] apellidos = apellidosC.split(" ", 0);
                    String nom = "";
                    String nom2 = "";
                    String ape = "";
                    String ape2 = "";
                    for (int u = 0; u < nombres.length; u++) {
                        if (u == 0) {
                            nom = nombres[u];
                        } else {
                            nom2 = nom2 + " " + nombres[u];
                        }
                    }
                    for (int t = 0; t < apellidos.length; t++) {
                        if (t == 0) {
                            ape = apellidos[t];
                        } else {
                            ape2 = ape2 + " " + apellidos[t];
                        }
                    }
                    if (!nom.equalsIgnoreCase("") && !ape.equalsIgnoreCase("") && !telefonoC.equalsIgnoreCase("")) {

                        form.param("no_cia", nc);
                        form.param("no_grupo", "03");
                        form.param("no_cliente", cod_clienteI);
                        form.param("primer_nombre", nom);
                        form.param("segundo_nombre", nom2);
                        form.param("primer_apellido", ape);
                        form.param("segundo_apellido", ape2);
                        form.param("telefono", telefonoC);
                        form.param("nombre_factura", nombreFacturacion);
                        form.param("nit_factura", nit);
                        form.param("email", emailC);
                        form.param("fecha_nacimiento", fechaNacimiento);
                        responseString = wsResponse(webTarget, form);

                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    clienteID = jsonResponse.getString("no_cliente");
                                    response.setContentType("application/json");
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    respuestaMap.put("data", clienteID);
                                    respuestaMap.put("r", "1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                } else {
                                    String errorMsj = jsonResponse.getString("mensaje");
                                    response.setContentType("application/json");
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    respuestaMap.put("data", responseString + " " + cod_clienteI + " "
                                            + nom + " " + nom2 + " " + ape + " " + ape2 + " " + telefonoC + " " + nombreFacturacion + " " + nit + " " + emailC + " " + fechaNacimiento);
                                    respuestaMap.put("r", "-1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
                                }
                            }
                        } catch (JSONException e) {
                            String errorMsj = jsonResponse.getString("mensaje");
                            response.setContentType("application/json");
                            PrintWriter out = response.getWriter();
                            Map<String, String> respuestaMap = new HashMap<String, String>();
                            respuestaMap.put("data", responseString + " " + cod_clienteI + " "
                                    + nom + " " + nom2 + " " + ape + " " + ape2 + " " + telefonoC + " " + nombreFacturacion + " " + nit + " " + emailC + " " + fechaNacimiento);
                            respuestaMap.put("r", "-1");
                            JSONObject respuestaJson = new JSONObject(respuestaMap);
                            out.print(respuestaJson.toString());
                        }
                    } else {
                        response.setContentType("application/json");
                        PrintWriter out = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", "Los Campos Obligatorios Deben Llenarse");
                        respuestaMap.put("r", "-1");
                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                        out.print(respuestaJson.toString());
                    }

                    break;
                case 790:
                    //InsertarDireccion nuevo o existente
                    webTarget = webTargetRoot.path("InsertarDireccion");
                    form = new Form();

                    String cod_clienteID = request.getParameter("cliente");
                    String telefonoid = request.getParameter("telefonoCliente");
                    String direccionI = request.getParameter("direccion");
                    String referencia = request.getParameter("referencia");
                    String tipoDireccion = request.getParameter("tipoDireccion");
                    String restaurante_asignado = request.getParameter("asignado");
                    String cod_deptoI = request.getParameter("departamento");
                    String cod_municipioI = request.getParameter("municipio");
                    String cod_zonaI = request.getParameter("zona");
                    String cod_coloniaI = request.getParameter("colonia");

                    if (!cod_clienteID.equalsIgnoreCase("")
                            && !direccionI.equalsIgnoreCase("")
                            && !tipoDireccion.equalsIgnoreCase("")
                            && !cod_deptoI.equalsIgnoreCase("") && !cod_municipioI.equalsIgnoreCase("")
                            && !cod_zonaI.equalsIgnoreCase("")) {

                        form.param("no_cia", nc);
                        form.param("grupo", "03");
                        form.param("no_cliente", cod_clienteID);
                        form.param("telefono", telefonoid);
                        form.param("direccion", direccionI);
                        form.param("referencia", referencia);
                        form.param("tipo_telefono", tipoDireccion);
                        form.param("restaurante_asignado", restaurante_asignado);
                        form.param("cod_depto", cod_deptoI);
                        form.param("cod_municipio", cod_municipioI);
                        form.param("cod_zona", cod_zonaI);
                        form.param("cod_colonia", cod_coloniaI);
                        responseString = wsResponse(webTarget, form);

                        try {
                            jsonResponse = new JSONObject(responseString); //PROCESAR LA RESPUESTA EN STRING QUE DEVUELVE EL WS

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    String telefonoID = jsonResponse.getString("telefono");
                                    response.setContentType("application/json");
                                    PrintWriter out = response.getWriter();
                                    Map<String, String> respuestaMap = new HashMap<String, String>();
                                    respuestaMap.put("data", telefonoID);
                                    respuestaMap.put("r", "1");
                                    JSONObject respuestaJson = new JSONObject(respuestaMap);
                                    out.print(respuestaJson.toString());
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
                            }
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
                    } else {
                        response.setContentType("application/json");
                        PrintWriter out = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", "Los Campos Obligatorios Deben Llenarse");
                        respuestaMap.put("r", "-1");
                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                        out.print(respuestaJson.toString());
                    }

                    break;
                case 4334:

                    //RECUPERAR CLASES POR HORARIO
                    Map<String, Object> clasesEnSesion = new HashMap<String, Object>();
                    webTarget = webTargetRoot.path("RecuperarClases");
                    String horario = request.getParameter("horario");
                    session.setAttribute("horario", horario);
                    if (horario != null) {
                        form = new Form();
                        form.param("no_cia", nc);
                        form.param("app_destino", "CC");
                        form.param("horario", horario);
                        responseString = wsResponse(webTarget, form);
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            String codigoRespuesta = jsonResponse.getString("codigo");
                            if (codigoRespuesta.equals("0")) {
                                JSONObject cla;
                                PrintWriter out = response.getWriter();
                                JSONArray clasesWs = jsonResponse.getJSONArray("clases");
                                JSONArray clasesPorHorario = new JSONArray();
                                int cont = clasesWs.length();
                                for (int i = 0; i < cont; i++) {
                                    JSONObject cws = clasesWs.getJSONObject(i);
                                    cla = new JSONObject();
                                    cla.put("nombre", cws.getString("nombre"));
                                    cla.put("imagen", cws.getString("imagen1"));
                                    cla.put("no_clase", cws.getString("clase"));
                                    clasesEnSesion.put(cws.getString("clase"), null);
                                    clasesPorHorario.put(cla);
                                }

                                Map<String, Object> horariosEnSesion = (Map<String, Object>) session.getAttribute("horarios");
                                horariosEnSesion.put(horario, clasesEnSesion);
                                session.setAttribute("horarios", horariosEnSesion);

                                Map<String, String> respuestaMap = new HashMap<String, String>();
                                respuestaMap.put("data", clasesPorHorario.toString());
                                respuestaMap.put("r", "1");
                                JSONObject respuestaJson = new JSONObject(respuestaMap);
                                out.print(respuestaJson.toString());
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
                        }
                    }
                    break;
                case 8745:
                    webTarget = webTargetRoot.path("RecuperarItems");
                    String clase = request.getParameter("clase");
                    if (clase != null) {
                        form = new Form();
                        form.param("no_cia", nc);
                        form.param("app_destino", "CC");
                        form.param("clase", clase);
                        responseString = wsResponse(webTarget, form);
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            String codigoRespuesta = jsonResponse.getString("codigo");
                            if (codigoRespuesta.equals("0")) {
                                JSONObject item;
                                HashMap<String, Object> mapaDeItems = new HashMap<String, Object>();
                                HashMap<String, Object> itemMap = null;
                                PrintWriter out = response.getWriter();
                                JSONArray itemsWs = jsonResponse.getJSONArray("items");
                                JSONArray itemsArray = new JSONArray();
                                int cont = itemsWs.length();
                                for (int i = 0; i < cont; i++) {
                                    JSONObject cws = itemsWs.getJSONObject(i);
                                    item = new JSONObject();
                                    itemMap = new HashMap<String, Object>();
                                    item.put("nombre", cws.getString("nombre"));
                                    item.put("imagen", cws.getString("imagen1"));
                                    item.put("no_item", cws.getString("item"));
                                    item.put("no_clase", cws.getString("clase"));
                                    item.put("dispo", cws.getString("estado"));
                                    item.put("especial", cws.getString("usa_especial"));
                                    item.put("precio_carta", cws.getString("precio"));
                                    item.put("cod_producto", cws.getString("cod_producto"));
                                    item.put("tipo", cws.getString("tipo"));
                                    item.put("es_postre", cws.getString("es_postre"));

                                    itemMap.put("nombre", cws.getString("nombre"));
                                    itemMap.put("imagen", cws.getString("imagen1"));
                                    itemMap.put("no_item", cws.getString("item"));
                                    itemMap.put("no_clase", cws.getString("clase"));
                                    itemMap.put("dispo", cws.getString("estado"));
                                    itemMap.put("especial", cws.getString("usa_especial"));
                                    itemMap.put("precio_carta", cws.getString("precio"));
                                    itemMap.put("cod_producto", cws.getString("cod_producto"));
                                    itemMap.put("tipo", cws.getString("tipo"));
                                    itemMap.put("es_postre", cws.getString("es_postre"));

                                    if (cws.getString("tipo").equalsIgnoreCase("3") || cws.getString("tipo").equalsIgnoreCase("2")) {
                                        webTarget = webTargetRoot.path("RecuperarTamanos");
                                        form = new Form();
                                        form.param("no_cia", nc);
                                        form.param("app_destino", "CC");
                                        form.param("clase", cws.getString("clase"));
                                        form.param("item", cws.getString("item"));
                                        responseString = wsResponse(webTarget, form);
                                        jsonResponse = new JSONObject(responseString);
                                        String codigoRespuestaT = jsonResponse.getString("codigo");
//                                        itemsArray.put(jsonResponse);
                                        if (codigoRespuestaT.equals("0")) {
                                            JSONArray tamanosWs = jsonResponse.getJSONArray("tamanos");
                                            int cont2 = tamanosWs.length();
                                            for (int j = 0; j < cont2; j++) {
                                                JSONObject tws = tamanosWs.getJSONObject(j);
//                                              //DISPONIBILIDAD tws.getString("valida_disponibilidad").equalsIgnoreCase("S") &&
                                                if (tws.getString("valor_default").equalsIgnoreCase("S")) {
                                                    item.put("tamano_default", tws.getString("tamano"));
                                                    item.put("precio_default", tws.getString("precio"));

                                                    itemMap.put("cod_tamano", tws.getString("cod_producto"));
                                                    itemMap.put("precio_tamano", tws.getString("precio"));
                                                    itemMap.put("dtamano", tws.getString("tamano"));
                                                    item.put("dtamano", tws.getString("tamano"));
                                                    itemsArray.put(item);
                                                    mapaDeItems.put(cws.getString("item"), itemMap);
                                                    break;
                                                }
                                            }
                                        } else {
                                            item.put("tamano_default", "N");
                                            item.put("precio_default", "N");
                                            itemsArray.put(item);
                                            mapaDeItems.put(cws.getString("item"), mapaDeItems);
                                        }
                                    } else {
                                        item.put("tipo", cws.getString("tipo"));
                                        item.put("tamano_default", "N");
                                        item.put("precio_default", "N");
                                        itemsArray.put(item);
                                        mapaDeItems.put(cws.getString("item"), itemMap);
                                    }
                                }

                                Map<String, Object> horariosEnSesion = (Map<String, Object>) session.getAttribute("horarios");
                                Map<String, Object> clasesEnHorario = (Map<String, Object>) horariosEnSesion.get((String) session.getAttribute("horario"));
                                clasesEnHorario.put(clase, mapaDeItems);
                                horariosEnSesion.put((String) session.getAttribute("horario"), clasesEnHorario);
                                session.setAttribute("horarios", horariosEnSesion);

                                Map<String, String> respuestaMap = new HashMap<String, String>();
                                respuestaMap.put("data", itemsArray.toString());
                                respuestaMap.put("r", "1");
                                JSONObject respuestaJson = new JSONObject(respuestaMap);
                                out.print(respuestaJson.toString());
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
                        }
                    }
                    break;
                case 3773:
                    webTarget = webTargetRoot.path("RecuperarTamanos");
                    String claseT = request.getParameter("clase");
                    String itemT = request.getParameter("item");
                    String codU = request.getParameter("codU");
                    cod_pedido = request.getParameter("cod_pedido");
                    boolean alaCarta = Boolean.parseBoolean(request.getParameter("alaCarta"));
                    if (claseT != null && itemT != null) {
                        form = new Form();
                        form.param("no_cia", nc);
                        form.param("app_destino", "CC");
                        form.param("clase", claseT);
                        form.param("item", itemT);
                        responseString = wsResponse(webTarget, form);
                        jsonResponse = new JSONObject(responseString);

                        if (jsonResponse != null) {
                            String codigoRespuesta = jsonResponse.getString("codigo");
                            if (codigoRespuesta.equals("0")) {
                                HashMap<String, Object> ordenesEnSesion = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                                HashMap<String, Object> ordenEnSesion = (HashMap<String, Object>) ordenesEnSesion.get(cod_pedido);
                                JSONObject item;
                                PrintWriter out = response.getWriter();
                                JSONArray tamanosWs = jsonResponse.getJSONArray("tamanos");
                                JSONArray itemsArray = new JSONArray();
                                int cont = tamanosWs.length();
                                HashMap<String, Object> itemEnSesionCT = (HashMap<String, Object>) ordenEnSesion.get(codU);
                                if (itemEnSesionCT != null) {
                                    Map<String, Object> listaTamanosDelItem = null;
                                    listaTamanosDelItem = (Map<String, Object>) itemEnSesionCT.get("tamano");
                                    for (int i = 0; i < cont; i++) {
                                        JSONObject cws = tamanosWs.getJSONObject(i);
                                        if (cws.getString("valida_disponibilidad").equalsIgnoreCase("S")) {
                                            Map<String, Object> tamanoItem = new HashMap<String, Object>();
                                            item = new JSONObject();
                                            item.put("desc", cws.getString("descripcion2"));
                                            item.put("precio", cws.getString("precio"));
                                            item.put("def", cws.getString("valor_default"));
                                            item.put("tamano", cws.getString("tamano"));
                                            item.put("cod_producto", cws.getString("cod_producto"));
//                                        tamanoItem.put("codigo", cws.getString("cod_producto"));
                                            tamanoItem.put("elegido", false);
                                            tamanoItem.put("precio", cws.getString("precio"));
                                            tamanoItem.put("tamano", cws.getString("tamano"));
                                            itemsArray.put(item);
                                            if (listaTamanosDelItem.get(cws.getString("cod_producto")) == null) {
                                                listaTamanosDelItem.put(cws.getString("cod_producto"), tamanoItem);
                                            }
                                        }
                                    }
                                    itemEnSesionCT.put("tamano", listaTamanosDelItem);
                                    ordenEnSesion.put(codU, itemEnSesionCT);
                                    ordenesEnSesion.put(cod_pedido, ordenEnSesion);
                                    session.setAttribute("ordenesEnSesion", ordenesEnSesion);
                                }
                                Map<String, String> respuestaMap = new HashMap<String, String>();
                                respuestaMap.put("data", itemsArray.toString());
                                respuestaMap.put("r", "1");
                                JSONObject respuestaJson = new JSONObject(respuestaMap);
                                out.print(respuestaJson.toString());
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
                        }
                    }
                    break;
                case 98883:
//                    webTarget = webTargetRoot.path("RecuperarGruposOpciones");
//                    String claseO = request.getParameter("clase");
//                    String itemO = request.getParameter("item");
//                    String tamanoG = request.getParameter("tamano");
//                    form = new Form();
//                    form.param("no_cia", nc);
//                    form.param("app_destino", "CC");
//                    form.param("clase", claseO);
//                    form.param("item", itemO);
//                    responseString = wsResponse(webTarget, form);
//                    jsonResponse = new JSONObject(responseString);
//                    JSONArray itemsOpciones;
//                    JSONArray itemsOpcionesArray;
//                    JSONArray itemsGrupos;
//                    if (jsonResponse != null) {
//                        String codigoRespuesta = jsonResponse.getString("codigo");
//                        if (codigoRespuesta.equals("0")) {
//
//                            JSONArray gruposopcionesws = jsonResponse.getJSONArray("items");
//                            itemsGrupos = new JSONArray();
//                            itemsOpcionesArray = new JSONArray();
//                            JSONObject respuestaJson = new JSONObject();
//                            itemsOpciones = new JSONArray();
//
//                            int conta = gruposopcionesws.length();
//                            for (int o = 0; o < conta; o++) {
//
//                                JSONObject grupodeopciones = gruposopcionesws.getJSONObject(o);
//
//                                itemsOpciones = new JSONArray();
//
//                                JSONObject itemGrupo = new JSONObject();
//                                itemGrupo.put("nombre", grupodeopciones.getString("nombre"));
//                                itemGrupo.put("grupo_opciones", grupodeopciones.getString("grupo_opciones"));
//                                itemGrupo.put("clase_opciones", grupodeopciones.getString("clase"));
//
//                                itemGrupo.put("cantidad", grupodeopciones.getString("cantidad"));
//                                itemGrupo.put("imagen", grupodeopciones.getString("imagen1"));
//                                itemsGrupos.put(itemGrupo);
//                                webTarget = webTargetRoot.path("RecuperarOpciones");
//
//                                form = new Form();
//                                form.param("no_cia", nc);
//                                form.param("app_destino", "CC");
//                                form.param("grupo_opciones", grupodeopciones.getString("grupo_opciones"));
//                                form.param("tamano", tamanoG);
//                                responseString = wsResponse(webTarget, form);
//                                jsonResponse = new JSONObject(responseString);
//
//                                if (jsonResponse != null) {
//                                    String codigoRespuestaB = jsonResponse.getString("codigo");
//                                    if (codigoRespuestaB.equals("0")) {
//
//                                        JSONArray opcionesws = jsonResponse.getJSONArray("items");
//
//                                        int cnt = opcionesws.length();
//                                        for (int j = 0; j < cnt; j++) {
//                                              JSONObject opcionws = opcionesws.getJSONObject(j);
//                                            if (opcionws.getString("disponible").equalsIgnoreCase("A")) {
//                                                JSONObject itemOpcion = new JSONObject();
//                                                itemOpcion.put("nombre", opcionws.getString("nombre"));
//                                                itemOpcion.put("default", opcionws.getString("valor_default"));
//                                                itemOpcion.put("no_opcion", opcionws.getString("opcion"));
//                                                itemOpcion.put("imagen", opcionws.getString("imagen1"));
//                                                itemOpcion.put("precio", opcionws.getString("precio"));
//                                                itemOpcion.put("grupo_opciones", opcionws.getString("grupo_opciones"));
//                                                itemOpcion.put("cod_producto", opcionws.getString("cod_producto"));
//                                                itemsOpciones.put(itemOpcion);
//                                            }
//                                        }
//                                        itemsOpcionesArray.put(itemsOpciones);
//                                    }
//                                }
//                            }
//                        }
//                        JSONObject respuestaJson = new JSONObject();
//                        respuestaJson.put("grupos", itemsGrupos.toString());
//                        respuestaJson.put("opcionDatos", itemsOpcionesArray.toString());
//                        PrintWriter out = response.getWriter();
//                        Map<String, String> respuestaMap = new HashMap<String, String>();
//                        respuestaMap.put("data", respuestaJson.toString());
//                        respuestaMap.put("r", "1");
//                        JSONObject respuestaJsonO = new JSONObject(respuestaMap);
//                        out.print(respuestaJsonO.toString());
//                    } else {
//                        String errorMsj = jsonResponse.getString("mensaje");
//                        response.setContentType("application/json");
//                        PrintWriter out = response.getWriter();
//                        Map<String, String> respuestaMap = new HashMap<String, String>();
//                        respuestaMap.put("data", errorMsj);
//                        respuestaMap.put("r", "-1");
//                        JSONObject respuestaJson = new JSONObject(respuestaMap);
//                        out.print(respuestaJson.toString());
//                    }
//            }

                    break;

                case 3774:

                    String codUn = request.getParameter("codU");
                    cod_pedido = request.getParameter("cod_pedido");
                    boolean esdefault = Boolean.parseBoolean(request.getParameter("esdefault"));
                    //SETEAR TODAS SUS OPCIONES, DEL ITEM EN CUESTION, CON SUS
                    //PROPIEDADES DE CLASE, ITEM, TAMANO, SETEAR LAS DEFAULT Y
                    //REGRESARLAS PARA LA VISTA
                    HashMap<String, Object> ordenesEnSesionRO = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionRO = (HashMap<String, Object>) ordenesEnSesionRO.get(cod_pedido);
                    Map<String, Object> itemEnSesionRO = (HashMap<String, Object>) ordenEnSesionRO.get(codUn);
                    if (itemEnSesionRO != null) {
                        Map<String, Object> propDelItem = (Map<String, Object>) itemEnSesionRO.get("propiedades");
                        if (esdefault) {
                            webTarget = webTargetRoot.path("RecuperarGruposOpciones");
                            form = new Form();
                            form.param("no_cia", nc);
                            form.param("app_destino", "CC");
                            form.param("clase", (String) propDelItem.get("no_clase"));
                            form.param("item", (String) propDelItem.get("no_item"));
                            responseString = wsResponse(webTarget, form);
                            jsonResponse = new JSONObject(responseString);
                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {

                                    String tamano = "";
                                    Map<String, Object> listamanosDelItem = (Map<String, Object>) itemEnSesionRO.get("tamano");
                                    for (String codTam : listamanosDelItem.keySet()) {
                                        HashMap<String, Object> tamanoDelIterado = (HashMap<String, Object>) listamanosDelItem.get(codTam);
                                        if ((Boolean) tamanoDelIterado.get("elegido") == true) {
                                            tamano = (String) tamanoDelIterado.get("tamano");
                                        }
                                    }
                                    JSONArray gruposopcionesws = jsonResponse.getJSONArray("items");
                                    Map<String, Object> itemsGrupos = new HashMap<String, Object>();

                                    JSONObject itemsOpciones;
                                    int cont = gruposopcionesws.length();
                                    Map<String, Object> listaDeOpciones = new HashMap<String, Object>();
                                    for (int i = 0; i < cont; i++) {
                                        JSONObject grupodeopciones = gruposopcionesws.getJSONObject(i);
                                        Map<String, Object> itemGrupo = new HashMap<String, Object>();
                                        itemGrupo.put("nombre", grupodeopciones.getString("nombre"));
                                        itemGrupo.put("grupo_opciones", grupodeopciones.getString("grupo_opciones"));
                                        itemGrupo.put("clase_opciones", grupodeopciones.getString("clase"));
                                        itemGrupo.put("cantidad", grupodeopciones.getString("cantidad"));
                                        itemGrupo.put("imagen", grupodeopciones.getString("imagen1"));
                                        itemsGrupos.put(grupodeopciones.getString("grupo_opciones"), itemGrupo);

                                        //OPCIONES DE UN GRUPO DE OPCIONES
                                        webTarget = webTargetRoot.path("RecuperarOpciones");
                                        form = new Form();
                                        form.param("no_cia", nc);
                                        form.param("app_destino", "CC");
                                        form.param("grupo_opciones", grupodeopciones.getString("grupo_opciones"));
                                        form.param("tamano", tamano);
                                        int cantidadDelGrupo = Integer.parseInt(grupodeopciones.getString("cantidad"));
                                        responseString = wsResponse(webTarget, form);
                                        jsonResponse = new JSONObject(responseString);

                                        if (jsonResponse != null) {
                                            String codigoRespuestaB = jsonResponse.getString("codigo");
                                            if (codigoRespuestaB.equals("0")) {
//                                                JSONObject item;
//                                                PrintWriter out = response.getWriter();
//                                                JSONArray bebidasArray = new JSONArray();
//                                                JSONArray bebidasws = jsonResponse.getJSONArray("items");
//
//                                                int cnt = bebidasws.length();
//                                                for (int j = 0; j < cnt; j++) {
                                                JSONArray opcionesws = jsonResponse.getJSONArray("items");

                                                int cnt = opcionesws.length();
                                                boolean def = false;
                                                for (int j = 0; j < cnt; j++) {
                                                    JSONObject opcionws = opcionesws.getJSONObject(j);
                                                    if (opcionws.getString("disponible").equalsIgnoreCase("A")) {
                                                        Map<String, Object> itemOpcion = new HashMap<String, Object>();
                                                        itemOpcion.put("nombre", opcionws.getString("nombre"));

                                                        itemOpcion.put("ordenamiento", opcionws.getString("ordenamiento"));
                                                        itemOpcion.put("clase_opciones", grupodeopciones.getString("clase"));
                                                        if (opcionws.getString("valor_default").equals("S")) {
                                                            itemOpcion.put("elegido", true);
                                                            itemOpcion.put("cantidad", cantidadDelGrupo);
                                                            def = true;
                                                        } else {
                                                            itemOpcion.put("elegido", false);
                                                            itemOpcion.put("cantidad", 0);
                                                        }

                                                        itemOpcion.put("imagen", opcionws.getString("imagen1"));
                                                        itemOpcion.put("precio", opcionws.getString("precio"));
                                                        itemOpcion.put("grupo_opciones", opcionws.getString("grupo_opciones"));
                                                        itemOpcion.put("cod_producto", opcionws.getString("cod_producto"));
                                                        listaDeOpciones.put(opcionws.getString("cod_producto"), itemOpcion);
                                                    }
                                                }
                                                if (!def) {
                                                    for (String cod_opcion : listaDeOpciones.keySet()) {
                                                        Map<String, Object> opcionIt = (Map<String, Object>) listaDeOpciones.get(cod_opcion);
                                                        String suGrupo = (String) opcionIt.get("grupo_opciones");
                                                        String ord = (String) opcionIt.get("ordenamiento");
                                                        if (suGrupo.equalsIgnoreCase(grupodeopciones.getString("grupo_opciones")) && ord.equalsIgnoreCase("1")) {
                                                            opcionIt.put("elegido", true);
                                                            listaDeOpciones.put(cod_opcion, opcionIt);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        itemEnSesionRO.put("grupos", itemsGrupos);
                                        itemEnSesionRO.put("opciones", listaDeOpciones);
                                    }
                                }
                            }
                        }
                        //REGRESAR EL GRUPO DE OPCIONES Y LAS OPCIONES
                        ordenEnSesionRO.put(codUn, itemEnSesionRO);
//                        session.setAttribute("orden", ordenEnSesionRO);
                        ordenesEnSesionRO.put(cod_pedido, ordenEnSesionRO);
                        session.setAttribute("ordenesEnSesion", ordenesEnSesionRO);
                        JSONObject respuestaJson = new JSONObject();
//                                    Map<String, Object> mapaG = (HashMap<String, Object>)itemEnSesionRO.get("grupos");
//                                    JSONArray itemsOpcionesArray = new JSONArray((Map<String, Object>)itemEnSesionRO.get("opciones"));
                        respuestaJson.put("grupos", (Map<String, Object>) itemEnSesionRO.get("grupos"));
                        Map<String, Object> mapaDeOpciones = (Map<String, Object>) itemEnSesionRO.get("opciones");

                        respuestaJson.put("opcionDatos", mapaDeOpciones);

                        respuestaJson.put("sesion", ordenesEnSesionRO);
                        PrintWriter out = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", respuestaJson.toString());
                        respuestaMap.put("r", "1");
                        JSONObject respuestaJsonO = new JSONObject(respuestaMap);
                        out.print(respuestaJsonO.toString());

                    }

                    break;

                //NUEVO SERVICIO 15/05
                case 2291:
                    String codUnR = request.getParameter("codU");
                    boolean esdefaultR = Boolean.parseBoolean(request.getParameter("esdefault"));
                    String html = "";
                    String header = "";
                    String header2 = "";
                    String divIng = "";
                    String divAd = "";
                    cod_pedido = request.getParameter("cod_pedido");

                    HashMap<String, Object> ordenesEnSesionRe = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
//                                HashMap<String, Object> ordenEnSesion = (HashMap<String, Object>) ordenesEnSesion.get(cod_pedido);
                    Map<String, Object> ordenEnSesionRe = (HashMap<String, Object>) ordenesEnSesionRe.get(cod_pedido);
                    Map<String, Object> itemEnSesionRe = (HashMap<String, Object>) ordenEnSesionRe.get(codUnR);
                    if (itemEnSesionRe != null) {
//                        Map<String, Object> propDelItem = (Map<String, Object>) itemEnSesionRe.get("propiedades");
//                        JSONArray itemsAdicionales = new JSONArray();
//
//                        JSONArray itemsReceta = new JSONArray();
//                        JSONArray itemsHeader = new JSONArray();
//                        JSONArray listaDeIngredientesReturn = new JSONArray();
                        if (esdefaultR) {
                            String cod_productoReceta = "";
                            String tamanoReceta = "";
                            Map<String, Object> listamanosDelItem = (Map<String, Object>) itemEnSesionRe.get("tamano");
                            for (String codTam : listamanosDelItem.keySet()) {
                                HashMap<String, Object> tamanoDelIterado = (HashMap<String, Object>) listamanosDelItem.get(codTam);
                                if ((Boolean) tamanoDelIterado.get("elegido") == true) {
                                    cod_productoReceta = codTam;

                                    tamanoReceta = (String) tamanoDelIterado.get("tamano");
                                }
                            }

                            webTarget = webTargetRoot.path("RecuperarReceta");

                            form = new Form();
                            form.param("no_cia", nc);
                            form.param("app_destino", "CC");
                            form.param("cod_producto", cod_productoReceta);
                            if (Integer.parseInt(tamanoReceta) == 0) {
                                form.param("es_menu", "N");
                                responseString = wsResponse(webTarget, form);
                                jsonResponse = new JSONObject(responseString);

                                if (jsonResponse != null) {
                                    String codigoRespuesta = jsonResponse.getString("codigo");
                                    if (codigoRespuesta.equals("0")) {
                                        Map<String, Object> listaDeIngredientes = new HashMap<String, Object>();
                                        JSONArray recetaitemsws = jsonResponse.getJSONArray("items");
                                        header2 += "<div style=\"display:none;\" id=\"tipoDeOpciones\" class=\"headerTablaPostres tipoDeOpciones\">\n"
                                                + "                                <ul>";
                                        boolean haying = false;
                                        boolean hayadic = false;
                                        int conta = recetaitemsws.length();
                                        for (int o = 0; o < conta; o++) {
                                            JSONObject recetaitem = recetaitemsws.getJSONObject(o);

                                            Map<String, Object> itemIngrediente = new HashMap<String, Object>();
                                            itemIngrediente.put("cod_producto", recetaitem.getString("cod_ingrediente"));
                                            itemIngrediente.put("precio", recetaitem.getString("precio"));
                                            itemIngrediente.put("elegido", false);
                                            itemIngrediente.put("requiere_cantidad", recetaitem.getString("requiere_cantidad"));
                                            itemIngrediente.put("imagen1", recetaitem.getString("imagen1"));
                                            itemIngrediente.put("nombre", recetaitem.getString("nombre"));
                                            itemIngrediente.put("cantidad", 0);
                                            itemIngrediente.put("tipo", recetaitem.getString("tipo"));
                                            itemIngrediente.put("IDtipo", "");
                                            listaDeIngredientes.put(recetaitem.getString("cod_ingrediente"), itemIngrediente);
                                            if (recetaitem.getString("tipo").equalsIgnoreCase("I")) {
                                                if (!haying) {
                                                    haying = true;

                                                    header2 += "<li>\n"
                                                            + "                                        <a id=\"itemsReceta" + recetaitem.getString("cod_ingrediente") + "\" class=\"menuReceta\" href=\"javascript:void(0)\">\n"
                                                            + "                                            <span>Ingrediente</span>\n"
                                                            + "                                        </a>\n"
                                                            + "                                    </li>";
                                                    divIng += "<div style=\"display:none;\" id=\"itemsReceta" + recetaitem.getString("cod_ingrediente") + "\" class=\"tablaIngredientes\"> ";
                                                }
                                                divIng += "<div data-nombre=" + recetaitem.getString("nombre") + " data-grupo=\"receta\" data-ingrediente=" + recetaitem.getString("cod_ingrediente") + " data-precio=" + recetaitem.getString("precio")
                                                        + " data-rcantidad=" + recetaitem.getString("requiere_cantidad") + " class=\"ingrediente\">";

                                                divIng += "<div class=\"cntImg\">'"
                                                        + "<img class=\"imgProdModal\" src=" + recetaitem.getString("imagen1") + " />"
                                                        + "</div>"
                                                        + "<label>" + recetaitem.getString("nombre") + "</label>"
                                                        + "<div class=\"overayIngrediente\"></div><div class=\"contenedorAcciones\">"
                                                        + "<a href=\"javascript:void(0)\" class=\"add addReceta\">"
                                                        + "<img src=\"media/imagenes/tomadoraOrdenes/modalMas.png \"/>"
                                                        + "</a>"
                                                        + "<a href=\"javascript:void(0)\" class=\"remove removeReceta\">"
                                                        + "<img src=\"media/imagenes/tomadoraOrdenes/modalMenos.png\"/>"
                                                        + "</a>"
                                                        + "</div>";
                                                if (recetaitem.getString("requiere_cantidad").equalsIgnoreCase("S")) {
                                                    divIng = divIng + "<input type=\"text\" value=\"1\" class=\"contadorExtras\" readonly/>";
                                                }

                                                divIng += "</div>";
                                            } else if (recetaitem.getString("tipo").equalsIgnoreCase("A")) {
                                                if (!hayadic) {
                                                    hayadic = true;
                                                    header2 += "<li>\n"
                                                            + "                                        <a id=\"itemsAdicionales" + recetaitem.getString("cod_ingrediente") + "\" class=\"menuReceta\" href=\"javascript:void(0)\">\n"
                                                            + "                                            <span>Adicional</span>\n"
                                                            + "                                        </a>\n"
                                                            + "                                    </li>";
                                                    divAd += "<div style=\"display:none;\" id=\"itemsAdicionales" + recetaitem.getString("cod_ingrediente") + "\"  class=\"tablaIngredientes\"> ";
                                                }
                                                divAd += "<div data-nombre=" + recetaitem.getString("nombre") + " data-grupo=\"adicional\" data-ingrediente=" + recetaitem.getString("cod_ingrediente") + " data-precio=" + recetaitem.getString("precio")
                                                        + " data-rcantidad=" + recetaitem.getString("requiere_cantidad") + " class=\"ingrediente\">";

                                                divAd += "<div class=\"cntImg\">'"
                                                        + "<img class=\"imgProdModal\" src=" + recetaitem.getString("imagen1") + " />"
                                                        + "</div>"
                                                        + "<label>" + recetaitem.getString("nombre") + "</label>"
                                                        + "<div class=\"overayIngrediente\"></div><div class=\"contenedorAcciones\">"
                                                        + "<a href=\"javascript:void(0)\" class=\"add addReceta\">"
                                                        + "<img src=\"media/imagenes/tomadoraOrdenes/modalMas.png \"/>"
                                                        + "</a>"
                                                        + "<a href=\"javascript:void(0)\" class=\"remove removeRecetaAdicional\">"
                                                        + "<img src=\"media/imagenes/tomadoraOrdenes/modalMenos.png\"/>"
                                                        + "</a>"
                                                        + "</div>";
                                                if (recetaitem.getString("requiere_cantidad").equalsIgnoreCase("S")) {
                                                    divAd = divAd + "<input type=\"text\" value=\"0\" class=\"contadorExtras\" readonly/>";
                                                }

                                                divAd += "</div>";
                                            }
                                        }
                                        divIng += "</div>";
                                        divAd += "</div>";
                                        header2 += "</ul>\n"
                                                + "                            </div>";
                                        itemEnSesionRe.put("receta", listaDeIngredientes);
                                        ordenEnSesionRe.put(codUnR, itemEnSesionRe);
                                    } else {
                                        String errorMsj = jsonResponse.getString("mensaje");
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", cod_productoReceta);
                                        respuestaMap.put("data2", tamanoReceta);
                                        respuestaMap.put("r", "-1");
                                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                                        out.print(respuestaJson.toString());
                                    }
                                }
                            } else {
                                form.param("es_menu", "1");
                                responseString = wsResponse(webTarget, form);
                                jsonResponse = new JSONObject(responseString);

                                if (jsonResponse != null) {
                                    String codigoRespuesta = jsonResponse.getString("codigo");
                                    if (codigoRespuesta.equals("0")) {
                                        Map<String, Object> listaDeIngredientes = new HashMap<String, Object>();

                                        JSONArray recetaitemsws = jsonResponse.getJSONArray("items");

                                        int conta = recetaitemsws.length();
                                        boolean he = false;
                                        for (int o = 0; o < conta; o++) {
                                            JSONObject recetaitem = recetaitemsws.getJSONObject(o);
                                            if (!he) {
                                                header += "<div id=\"menuCombos\" class=\"headerTablaPostres\">\n"
                                                        + "                                <ul>\n";
                                                he = true;
                                            }

                                            header += "<li>";

                                            header += "                                     <a id=" + recetaitem.getString("cod_ingrediente") + " class=\"menuComboReceta\" href=\"javascript:void(0)\">\n"
                                                    + "                                            <span>" + recetaitem.getString("nombre") + "</span>\n"
                                                    + "                                        </a>\n"
                                                    + "                                    </li>";
                                            if ((conta - 1) == o) {
                                                header += "</ul>\n"
                                                        + "</div>";
                                            }
                                            Map<String, Object> itemIngrediente = new HashMap<String, Object>();
                                            itemIngrediente.put("cod_producto", recetaitem.getString("cod_ingrediente"));
                                            itemIngrediente.put("precio", recetaitem.getString("precio"));
                                            itemIngrediente.put("elegido", false);
                                            itemIngrediente.put("requiere_cantidad", recetaitem.getString("requiere_cantidad"));
                                            itemIngrediente.put("imagen1", recetaitem.getString("imagen1"));
                                            itemIngrediente.put("nombre", recetaitem.getString("nombre"));
                                            itemIngrediente.put("cantidad", 0);
                                            itemIngrediente.put("tipo", "P");
                                            itemIngrediente.put("IDtipo", "");
                                            recetaitem.put("tipo", "P");
                                            listaDeIngredientes.put(recetaitem.getString("cod_ingrediente"), itemIngrediente);
                                            form = new Form();
                                            form.param("no_cia", nc);
                                            form.param("app_destino", "CC");
                                            form.param("cod_producto", recetaitem.getString("cod_ingrediente"));
                                            form.param("es_menu", "N");
                                            responseString = wsResponse(webTarget, form);
                                            jsonResponse = new JSONObject(responseString);
                                            String codigoRespuesta2 = jsonResponse.getString("codigo");
                                            if (codigoRespuesta2.equals("0")) {
                                                Map<String, Object> listaDeIngredientes2 = new HashMap<String, Object>();
                                                JSONArray recetaitemsws2 = jsonResponse.getJSONArray("items");
                                                int conta2 = recetaitemsws2.length();
                                                header2 += "<div style=\"display:none;\" id=\"tipoDeOpciones" + recetaitem.getString("cod_ingrediente") + "\" class=\"headerTablaPostres tipoDeOpciones\">\n"
                                                        + "                                <ul>";
                                                boolean haying = false;
                                                boolean hayadic = false;
                                                for (int p = 0; p < conta2; p++) {
                                                    JSONObject recetaitem2 = recetaitemsws2.getJSONObject(p);

                                                    Map<String, Object> itemIngrediente2 = new HashMap<String, Object>();
                                                    itemIngrediente2.put("cod_producto", recetaitem2.getString("cod_ingrediente"));
                                                    itemIngrediente2.put("precio", recetaitem2.getString("precio"));
                                                    itemIngrediente2.put("elegido", false);
                                                    itemIngrediente2.put("requiere_cantidad", recetaitem2.getString("requiere_cantidad"));
                                                    itemIngrediente2.put("imagen1", recetaitem2.getString("imagen1"));
                                                    itemIngrediente2.put("nombre", recetaitem2.getString("nombre"));
                                                    itemIngrediente2.put("cantidad", 0);
                                                    itemIngrediente2.put("ingredienteDe", recetaitem.getString("cod_ingrediente"));
                                                    itemIngrediente2.put("tipo", recetaitem2.getString("tipo"));
                                                    itemIngrediente2.put("IDtipo", "");
                                                    listaDeIngredientes.put(recetaitem2.getString("cod_ingrediente"), itemIngrediente2);
                                                    if (recetaitem2.getString("tipo").equalsIgnoreCase("I")) {
                                                        if (!haying) {
                                                            haying = true;

                                                            header2 += "<li>\n"
                                                                    + "                                        <a id=\"itemsReceta" + recetaitem.getString("cod_ingrediente") + "\" class=\"menuReceta\" href=\"javascript:void(0)\">\n"
                                                                    + "                                            <span>Ingrediente</span>\n"
                                                                    + "                                        </a>\n"
                                                                    + "                                    </li>";
                                                            divIng += "<div style=\"display:none;\" id=\"itemsReceta" + recetaitem.getString("cod_ingrediente") + "\" class=\"tablaIngredientes\"> ";
                                                        }
                                                        divIng += "<div data-grupo=\"receta\" data-ingrediente=" + recetaitem2.getString("cod_ingrediente") + " data-nombre=" + recetaitem2.getString("nombre") + " data-precio=" + recetaitem2.getString("precio")
                                                                + " data-rcantidad=" + recetaitem2.getString("requiere_cantidad") + " class=\"ingrediente\">";

                                                        divIng += "<div class=\"cntImg\">'"
                                                                + "<img class=\"imgProdModal\" src=" + recetaitem2.getString("imagen1") + " />"
                                                                + "</div>"
                                                                + "<label>" + recetaitem2.getString("nombre") + "</label>"
                                                                + "<div class=\"overayIngrediente\"></div><div class=\"contenedorAcciones\">"
                                                                + "<a href=\"javascript:void(0)\" class=\"add addReceta\">"
                                                                + "<img src=\"media/imagenes/tomadoraOrdenes/modalMas.png \"/>"
                                                                + "</a>"
                                                                + "<a href=\"javascript:void(0)\" class=\"remove removeReceta\">"
                                                                + "<img src=\"media/imagenes/tomadoraOrdenes/modalMenos.png\"/>"
                                                                + "</a>"
                                                                + "</div>";
                                                        if (recetaitem2.getString("requiere_cantidad").equalsIgnoreCase("S")) {
                                                            divIng = divIng + "<input type=\"text\" value=\"1\" class=\"contadorExtras\" readonly/>";
                                                        }

                                                        divIng += "</div>";
                                                    } else if (recetaitem2.getString("tipo").equalsIgnoreCase("A")) {
                                                        if (!hayadic) {
                                                            hayadic = true;
                                                            header2 += "<li>\n"
                                                                    + "                                        <a id=\"itemsAdicionales" + recetaitem.getString("cod_ingrediente") + "\" class=\"menuReceta\" href=\"javascript:void(0)\">\n"
                                                                    + "                                            <span>Adicional</span>\n"
                                                                    + "                                        </a>\n"
                                                                    + "                                    </li>";
                                                            divAd += "<div style=\"display:none;\" id=\"itemsAdicionales" + recetaitem.getString("cod_ingrediente") + "\"  class=\"tablaIngredientes\"> ";
                                                        }
                                                        divAd += "<div data-grupo=\"adicional\" data-ingrediente=" + recetaitem2.getString("cod_ingrediente") + " data-nombre=" + recetaitem2.getString("nombre") + " data-precio=" + recetaitem2.getString("precio")
                                                                + " data-rcantidad=" + recetaitem2.getString("requiere_cantidad") + " class=\"ingrediente\">";

                                                        divAd += "<div class=\"cntImg\">'"
                                                                + "<img class=\"imgProdModal\" src=" + recetaitem2.getString("imagen1") + " />"
                                                                + "</div>"
                                                                + "<label>" + recetaitem2.getString("nombre") + "</label>"
                                                                + "<div class=\"overayIngrediente\"></div><div class=\"contenedorAcciones\">"
                                                                + "<a href=\"javascript:void(0)\" class=\"add addReceta\">"
                                                                + "<img src=\"media/imagenes/tomadoraOrdenes/modalMas.png \"/>"
                                                                + "</a>"
                                                                + "<a href=\"javascript:void(0)\" class=\"remove removeRecetaAdicional\">"
                                                                + "<img src=\"media/imagenes/tomadoraOrdenes/modalMenos.png\"/>"
                                                                + "</a>"
                                                                + "</div>";
                                                        if (recetaitem2.getString("requiere_cantidad").equalsIgnoreCase("S")) {
                                                            divAd = divAd + "<input type=\"text\" value=\"0\" class=\"contadorExtras\" readonly/>";
                                                        }

                                                        divAd += "</div>";
                                                    }
                                                }
                                                divIng += "</div>";
                                                divAd += "</div>";
                                                header2 += "</ul>\n"
                                                        + "                            </div>";
                                            }
//                                    
//
//                                    
//                                }

                                        }
                                        itemEnSesionRe.put("receta", listaDeIngredientes);
                                        ordenEnSesionRe.put(codUnR, itemEnSesionRe);
                                    } else {
                                        String errorMsj = jsonResponse.getString("mensaje");
                                        response.setContentType("application/json");
                                        PrintWriter out = response.getWriter();
                                        Map<String, String> respuestaMap = new HashMap<String, String>();
                                        respuestaMap.put("data", cod_productoReceta);
                                        respuestaMap.put("data2", tamanoReceta);
                                        respuestaMap.put("r", "-1");
                                        JSONObject respuestaJson = new JSONObject(respuestaMap);
                                        out.print(respuestaJson.toString());
                                    }
                                }
                            }

                        }
                        Map<String, Object> listaDeIngredientes = (Map<String, Object>) itemEnSesionRe.get("receta");

                        ordenesEnSesionRe.put(cod_pedido, ordenEnSesionRe);
                        session.setAttribute("ordenesEnSesion", ordenesEnSesionRe);
//                        session.setAttribute("orden", ordenEnSesionRe);

//                        for (String cod_ingrediente : listaDeIngredientes.keySet()) {
//                            Map<String, Object> recetaitem = (Map<String, Object>) listaDeIngredientes.get(cod_ingrediente);
//                            String tipo = (String) recetaitem.get("tipo");
//                            if (tipo.equalsIgnoreCase("I")) {
//                                itemsReceta.put(new JSONObject(recetaitem));
//                            } else if (tipo.equalsIgnoreCase("A")) {
//                                itemsAdicionales.put(recetaitem);
//                            } else if (tipo.equalsIgnoreCase("P")) {
//                                itemsHeader.put(recetaitem);
//                            }
//                        }
                        JSONObject respuestaJson = new JSONObject();
                        respuestaJson.put("receta", header + header2);
                        respuestaJson.put("xD", listaDeIngredientes);
//                        respuestaJson.put("header", itemsHeader.toString());
//                        respuestaJson.put("adicionales", itemsAdicionales.toString());
//                        respuestaJson.put("receta",listaDeIngredientesReturn);
                        PrintWriter out = response.getWriter();
                        response.setContentType("text/html;charset=UTF-8");
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", respuestaJson.toString());
                        respuestaMap.put("r", "1");
                        JSONObject respuestaJsonO = new JSONObject(respuestaMap);
                        out.print(header + header2 + divIng + divAd);

                    }
                    break;
                //NUEVO SERVICIO
                case 9696:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesion = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    HashMap<String, Object> ordenEnSesion = (HashMap<String, Object>) ordenesEnSesion.get(cod_pedido);

//                    HashMap<String, Object> ordenEnSesion = (HashMap<String, Object>) session.getAttribute("orden");
                    if (ordenEnSesion == null) {
                        ordenEnSesion = new HashMap<String, Object>();
                    }
                    Map<String, Object> itemDeLaOrden = new HashMap<String, Object>();
                    String codUnico = (String) request.getParameter("prod");
                    String claseNuevo = (String) request.getParameter("clase");
                    String itemNuevo = (String) request.getParameter("item");
                    boolean alacarta = Boolean.parseBoolean(request.getParameter("alaCarta"));
                    Map<String, Object> horariosEnSesion = (Map<String, Object>) session.getAttribute("horarios");
                    Map<String, Object> clasesEnHorario = (Map<String, Object>) horariosEnSesion.get((String) session.getAttribute("horario"));
                    Map<String, Object> itemsEnClase = (Map<String, Object>) clasesEnHorario.get(claseNuevo);
                    if (itemsEnClase != null) {
                        Map<String, Object> itemSeleccionado = (Map<String, Object>) itemsEnClase.get(itemNuevo);
                        if (itemSeleccionado != null) {
                            Map<String, Object> propiedadesItem = new HashMap<String, Object>();
                            Map<String, Object> listaDeTamanos = new HashMap<String, Object>();
                            Map<String, Object> tamanoItem = new HashMap<String, Object>();
                            propiedadesItem.put("no_clase", itemSeleccionado.get("no_clase"));
                            propiedadesItem.put("no_item", itemSeleccionado.get("no_item"));
                            propiedadesItem.put("tipo", itemSeleccionado.get("tipo"));
                            propiedadesItem.put("es_postre", itemSeleccionado.get("es_postre"));
                            if (alacarta) {
                                String cod = (String) itemSeleccionado.get("cod_producto");
                                String precio = (String) itemSeleccionado.get("precio_carta");
                                tamanoItem.put("elegido", true);
                                tamanoItem.put("precio", precio);
                                tamanoItem.put("tamano", "0");
                                listaDeTamanos.put(cod, tamanoItem);
                            } else {
                                String cod = (String) itemSeleccionado.get("cod_tamano");
                                String precio = (String) itemSeleccionado.get("precio_tamano");
                                String tamanoD = (String) itemSeleccionado.get("dtamano");
                                tamanoItem.put("elegido", true);
                                tamanoItem.put("precio", precio);
                                tamanoItem.put("tamano", tamanoD);
                                listaDeTamanos.put(cod, tamanoItem);
                                String itemTipo = (String) itemSeleccionado.get("tipo");
                                if (itemTipo.equalsIgnoreCase("3")) {
                                    Map<String, Object> tamanoItemC = new HashMap<String, Object>();
                                    String codC = (String) itemSeleccionado.get("cod_producto");
                                    String precioC = (String) itemSeleccionado.get("precio_carta");
//                                    tamanoItemC.put("codigo", cod);
                                    tamanoItemC.put("elegido", false);
                                    tamanoItemC.put("precio", precioC);
                                    tamanoItemC.put("tamano", "0");
                                    listaDeTamanos.put(codC, tamanoItemC);
                                }

                            }
                            itemDeLaOrden.put("propiedades", propiedadesItem);

                            itemDeLaOrden.put("tamano", listaDeTamanos);
                            itemDeLaOrden.put("opciones", new HashMap<String, Object>());
                            itemDeLaOrden.put("receta", new HashMap<String, Object>());
                            ordenEnSesion.put(codUnico, itemDeLaOrden);
//                            session.setAttribute("orden", ordenEnSesion);

                            ordenesEnSesion.put(cod_pedido, ordenEnSesion);
                            session.setAttribute("ordenesEnSesion", ordenesEnSesion);
                            PrintWriter out = response.getWriter();

                            response.setContentType("application/json");
                            JSONObject respuestaJson = new JSONObject();
                            respuestaJson.put("orden", ordenEnSesion.toString());
                            out.print(respuestaJson.toString());
                        }
                    }
                    break;

                case 9797:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesionCT = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionCT = (HashMap<String, Object>) ordenesEnSesionCT.get(cod_pedido);
                    String codUT = request.getParameter("prod");
                    String codT = request.getParameter("tam");
                    if (codUT != null && codT != null) {
                        Map<String, Object> itemEnSesionCT = (HashMap<String, Object>) ordenEnSesionCT.get(codUT);
                        if (itemEnSesionCT != null) {
                            Map<String, Object> listaTamanosDelItem = (Map<String, Object>) itemEnSesionCT.get("tamano");
                            for (String codTam : listaTamanosDelItem.keySet()) {
                                HashMap<String, Object> tamanoDelIterado = (HashMap<String, Object>) listaTamanosDelItem.get(codTam);
                                if (codTam.equalsIgnoreCase(codT)) {
                                    tamanoDelIterado.put("elegido", true);
                                } else {
                                    tamanoDelIterado.put("elegido", false);
                                }
                                listaTamanosDelItem.put(codTam, tamanoDelIterado);
                            }
                            itemEnSesionCT.put("tamano", listaTamanosDelItem);
                        }
                        ordenEnSesionCT.put(codUT, itemEnSesionCT);

                        ordenesEnSesionCT.put(cod_pedido, ordenEnSesionCT);
                        session.setAttribute("ordenesEnSesion", ordenesEnSesionCT);
                        PrintWriter out = response.getWriter();

                        response.setContentType("application/json");
                        JSONObject respuestaJson = new JSONObject();
                        respuestaJson.put("orden", ordenesEnSesionCT.toString());
                        out.print(respuestaJson.toString());
                    }
                    break;
                case 6767:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesionO = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionO = (HashMap<String, Object>) ordenesEnSesionO.get(cod_pedido);
                    String codUO = request.getParameter("codU");
                    String opciones = request.getParameter("opciones");

                    if (opciones != null && codUO != null) {

                        Map<String, Object> itemEnSesionO = (HashMap<String, Object>) ordenEnSesionO.get(codUO);
                        if (itemEnSesionO != null) {
                            JSONArray opcionesSeleccionadas = new JSONArray(opciones);
                            Map<String, Object> listaOpcsDelItem = (Map<String, Object>) itemEnSesionO.get("opciones");
                            Map<String, Object> listaGOpcsDelItem = (Map<String, Object>) itemEnSesionO.get("grupos");
                            for (String grupoOpciones : listaGOpcsDelItem.keySet()) {
                                HashMap<String, Object> gOpcionDelIterado = (HashMap<String, Object>) listaGOpcsDelItem.get(grupoOpciones);
                                int cantG = Integer.parseInt((String) gOpcionDelIterado.get("cantidad"));

                                for (String codOpcion : listaOpcsDelItem.keySet()) {
                                    Map<String, Object> opcionDelItem = (Map<String, Object>) listaOpcsDelItem.get(codOpcion);
                                    String grupoDelItemIterado = (String) opcionDelItem.get("grupo_opciones");
                                    if (grupoDelItemIterado.equalsIgnoreCase(grupoOpciones)) {
                                        opcionDelItem.put("cantidad", 0);
                                        opcionDelItem.put("elegido", false);
                                        int cselec = opcionesSeleccionadas.length();
                                        for (int i = 0; i < cselec; i++) {
                                            JSONObject opcionSeleccionada = opcionesSeleccionadas.getJSONObject(i);
                                            int cantidadSeleccionada = opcionSeleccionada.getInt("cantidad");
                                            int codigoint = Integer.parseInt(codOpcion);
                                            if (codigoint == opcionSeleccionada.getInt("cod_producto")) {

                                                int cantT = 0;
                                                for (String codOpSumar : listaOpcsDelItem.keySet()) {
                                                    HashMap<String, Object> opcionDelIterado = (HashMap<String, Object>) listaOpcsDelItem.get(codOpSumar);
                                                    String gIt = (String) opcionDelIterado.get("grupo_opciones");
                                                    if (gIt.equalsIgnoreCase(grupoOpciones)) {

                                                        int cantI = (Integer) opcionDelIterado.get("cantidad");
                                                        cantT += cantI;
                                                    }
                                                }
                                                if (cantT < cantG) {
                                                    if ((cantT + cantidadSeleccionada) > cantG) {
                                                        cantidadSeleccionada = cantG - cantT;
                                                    }

                                                    opcionDelItem.put("cantidad", cantidadSeleccionada);
                                                    opcionDelItem.put("elegido", true);

                                                }
                                            }
                                        }
                                        listaOpcsDelItem.put(codOpcion, opcionDelItem);
                                    }
                                }
                            }
                            itemEnSesionO.put("opciones", listaOpcsDelItem);
                            ordenEnSesionO.put(codUO, itemEnSesionO);
                            ordenesEnSesionO.put(cod_pedido, ordenEnSesionO);
                            session.setAttribute("ordenesEnSesion", ordenesEnSesionO);
                            PrintWriter out = response.getWriter();

                            response.setContentType("application/json");
                            JSONObject respuestaJson = new JSONObject();
                            respuestaJson.put("orden", ordenesEnSesionO.toString());
                            out.print(respuestaJson.toString());
                        } else {
                            PrintWriter out = response.getWriter();

                            response.setContentType("application/json");
                            JSONObject respuestaJson = new JSONObject();
                            respuestaJson.put("data", "es el item noencuentra");
                            out.print(respuestaJson.toString());
                        }

//                        itemEnSesionO.put("opciones", listaOpcsDelItem);
//                        ordenEnSesionO.put(codUO, itemEnSesionO);
//                        session.setAttribute("orden", ordenEnSesionO);
                    } else {
                        PrintWriter out = response.getWriter();

                        response.setContentType("application/json");
                        JSONObject respuestaJson = new JSONObject();
                        respuestaJson.put("data", "es el request");
                        out.print(respuestaJson.toString());
                    }
                    //OBTENER EL ITEM SEGUN EL ENVIADO
                    //OBTENER SU GRUPO DE OPCIONES
                    //ITERAR EN SUS OPCIONES
                    //SI ES IGUAL SU GRUPO DE OPCIONES ITERADO CON EL GRUPO DE OPCIONES DE LA
                    //OPCION SACADA DEL CODIGO Y SI ES IGUAL AL CODIGO ENVIADO
                    //CAMBIAR CANTIDAD Y ELEGIDO
                    //DE OTRA FORMA SI SOLO SON IGUALES LOS GRUPOS DE OPCIONES
                    //SETEAR CANTIDAD 0
                    //
//                            String cantidadO = request.getParameter("cantidad");
//                            
//                            HashMap<String, Object> opcionDelSelec = (HashMap<String, Object>) listaOpcsDelItem.get(codO);
//                            String gSlec = (String)opcionDelSelec.get("grupo_opciones");
//                            HashMap<String, Object> gOpcionDelIterado = (HashMap<String, Object>) listaGOpcsDelItem.get(gSlec);
//                            int cantG = Integer.parseInt((String) gOpcionDelIterado.get("cantidad"));
//                            
//                            for (String codOp : listaOpcsDelItem.keySet()) {
//                                
//                                
//                                
//                                HashMap<String, Object> opcionDelIterado = (HashMap<String, Object>) listaOpcsDelItem.get(codOp);
//                                String gIt = (String) opcionDelIterado.get("grupo_opciones");
//                                if(gIt.equalsIgnoreCase(gSlec)){
//                                    int cantI = Integer.parseInt((String) opcionDelIterado.get("cantidad"));
//                                    cantT += cantI;
//                                }
//                            }
//                            if(cantT < cantG && (cantT + cantS) <= cantG){
//                                    if (codOp.equalsIgnoreCase(codO)) {
//                                        opcionDelIterado.put("elegido", true);
//                            itemEnSesionO.put("opciones", listaOpcsDelItem);        }
//                                    listaOpcsDelItem.put(codOp, opcionDelIterado);
//                                }
//                            itemEnSesionO.put("opciones", listaOpcsDelItem);
//                        }

                    break;
                case 4646:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesionR = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");

                    Map<String, Object> ordenEnSesionR = (HashMap<String, Object>) ordenesEnSesionR.get(cod_pedido);
                    String codUR = request.getParameter("codU");
                    String ingredientes = request.getParameter("ingredientes");

                    if (ingredientes != null && codUR != null) {

                        Map<String, Object> itemEnSesionR = (HashMap<String, Object>) ordenEnSesionR.get(codUR);
                        if (itemEnSesionR != null) {
                            JSONArray ingredientesSeleccionados = new JSONArray(ingredientes);
                            Map<String, Object> listaIngDelItem = (Map<String, Object>) itemEnSesionR.get("receta");
                            for (String codIngr : listaIngDelItem.keySet()) {
                                Map<String, Object> ingredienteDelItem = (Map<String, Object>) listaIngDelItem.get(codIngr);
                                ingredienteDelItem.put("elegido", false);
                                ingredienteDelItem.put("cantidad", 0);
                                int cselec = ingredientesSeleccionados.length();
                                String tipoDelIngredienteParaP = (String) ingredienteDelItem.get("tipo");
                                
                                for (int i = 0; i < cselec; i++) {
                                    JSONObject ingredienteSeleccionado = ingredientesSeleccionados.getJSONObject(i);
                                    int codigoint = Integer.parseInt(codIngr);
                                    if (codigoint == ingredienteSeleccionado.getInt("cod_producto")) {
                                        String tipoDelIngredienteIterado = (String) ingredienteDelItem.get("tipo");
                                        String reqDelIngredienteIterado = (String) ingredienteDelItem.get("requiere_cantidad");
                                        if (tipoDelIngredienteIterado.equalsIgnoreCase("A") && ingredienteSeleccionado.getBoolean("especial")) {
                                            if (reqDelIngredienteIterado.equalsIgnoreCase("S")) {
                                                ingredienteDelItem.put("cantidad", ingredienteSeleccionado.getInt("cantidad"));
                                            }
                                            ingredienteDelItem.put("IDtipo", 96);
                                            ingredienteDelItem.put("elegido", true);

                                        } else if (tipoDelIngredienteIterado.equalsIgnoreCase("I")) {
                                            if (reqDelIngredienteIterado.equalsIgnoreCase("S")) {
                                                ingredienteDelItem.put("cantidad", ingredienteSeleccionado.getInt("cantidad"));
                                            }
                                            if (ingredienteSeleccionado.getBoolean("especial")) {
                                                ingredienteDelItem.put("IDtipo", 97);
                                                ingredienteDelItem.put("elegido", true);
                                            } else {
                                                ingredienteDelItem.put("IDtipo", 98);
                                                ingredienteDelItem.put("elegido", true);
                                            }
                                        }

                                    }
                                }
                                listaIngDelItem.put(codIngr, ingredienteDelItem);
                            }
                            itemEnSesionR.put("receta", listaIngDelItem);
                            ordenEnSesionR.put(codUR, itemEnSesionR);
                            ordenesEnSesionR.put(cod_pedido, ordenEnSesionR);
                            session.setAttribute("ordenesEnSesion", ordenesEnSesionR);
                            PrintWriter out = response.getWriter();

                            response.setContentType("application/json");
                            JSONObject respuestaJson = new JSONObject();
                            respuestaJson.put("orden", ordenesEnSesionR.toString());
                            out.print(respuestaJson.toString());

                        } else {
                            PrintWriter out = response.getWriter();
                            response.setContentType("application/json");
                            JSONObject respuestaJson = new JSONObject();
                            respuestaJson.put("data", "es el item noencuentra");
                            out.print(respuestaJson.toString());
                        }

//                        itemEnSesionO.put("opciones", listaOpcsDelItem);
//                        ordenEnSesionO.put(codUO, itemEnSesionO);
//                        session.setAttribute("orden", ordenEnSesionO);
                    } else {
                        PrintWriter out = response.getWriter();

                        response.setContentType("application/json");
                        JSONObject respuestaJson = new JSONObject();
                        respuestaJson.put("data", "es el request");
                        out.print(respuestaJson.toString());
                    }
                    break;
                case 666:
                    //INSERTAR LA ORDEN

                    String cod_pedidoInsertarPedido = request.getParameter("cod_pedido");
                    String grupoInsertarPedido = "03";
                    String no_clienteInsertarPedido = (String) session.getAttribute("clienteDeOrden");
                    String no_empleado = (String) session.getAttribute("OpId");
                    String telefonoInsertarPedido = (String) session.getAttribute("direccionDeOrden");
                    String horarioInsertarPedido = (String) session.getAttribute("horario");
                    String observaciones_entrega = (String) request.getParameter("observaciones_entrega");
                    String observaciones_orden = (String) request.getParameter("observaciones_orden");
                    String tipo_pago = (String) request.getParameter("tipo_pago");
                    String monto_tarjeta = "";
                    String monto_efectivo = "";
                    String tarjeta = request.getParameter("no_tarjeta");
                    String mes_vence = request.getParameter("mes_vence");
                    String anio_vence = request.getParameter("anio_vence");
                    String tipoOrden = (String) request.getParameter("tipoOrden");
                    String fechaInAdvance = (String) request.getParameter("fechaInAdvance");
                    String horaInAdvance = (String) request.getParameter("horaInAdvance");
                    
                    String confirmar = (String) request.getParameter("confirmar");
                    String confirmar_texto = (String) request.getParameter("confirmar_texto");
                    String orden_original = "";
                    if ((String) session.getAttribute("asociada") != null) {
                        orden_original = (String) session.getAttribute("asociada");
                    }
                    if (confirmar == null || confirmar.equalsIgnoreCase("")) {
                        confirmar = "0";
                    }

                    HashMap<String, Object> ordenesEnSesionEnviar = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    HashMap<String, Object> ordenParaEnviar = (HashMap<String, Object>) ordenesEnSesionEnviar.get(cod_pedidoInsertarPedido);

                    _cod_pro = new ArrayList<Integer>();
                    _clase = new ArrayList<Integer>();
                    _item = new ArrayList<Integer>();
                    _linea = new ArrayList<Integer>();
                    _orden = new ArrayList<String>();
                    _cantidad = new ArrayList<Integer>();
                    _es_postre = new ArrayList<String>();
                    _tiene_agregado = new ArrayList<String>();
                    _agregado_de = new ArrayList<String>();
                    _ingrediente_de = new ArrayList<String>();
                    _tipo = new ArrayList<String>();
                    _subtipo = new ArrayList<String>();
                    _cantidad_ingrediente = new ArrayList<Integer>();
                    ArrayList<Map<String, Object>> _listasIngredientes = new ArrayList<Map<String, Object>>();
                    int numeroLinea = 0;
                    int lineaQueda = 0;
                    DecimalFormat format = new DecimalFormat("#00");
                    float montoTotal = 0;
                    for (String codProducto : ordenParaEnviar.keySet()) {
                        numeroLinea = numeroLinea + 1;
                        lineaQueda = numeroLinea;
                        Map<String, Object> itemEnLaOrden = (Map<String, Object>) ordenParaEnviar.get(codProducto);
                        Map<String, Object> opcionesDelItem = (Map<String, Object>) itemEnLaOrden.get("opciones");
                        Map<String, Object> ingredientesDelItem = (Map<String, Object>) itemEnLaOrden.get("receta");
                        _listasIngredientes.add(ingredientesDelItem);
                        Map<String, Object> tamanos = (Map<String, Object>) itemEnLaOrden.get("tamano");
                        Map<String, Object> propiedadesDelItem = (Map<String, Object>) itemEnLaOrden.get("propiedades");
                        String clas = (String) propiedadesDelItem.get("no_clase");
                        String item = (String) propiedadesDelItem.get("no_item");
                        String espostre = (String) propiedadesDelItem.get("es_postre");
                        for (String codProductoT : tamanos.keySet()) {
                            Map<String, Object> tamanoIterado = (Map<String, Object>) tamanos.get(codProductoT);
                            boolean eleg = (Boolean) tamanoIterado.get("elegido");
                            if (eleg) {
                                String precioT = (String) tamanoIterado.get("precio");
                                float precioF = Float.parseFloat(precioT);
                                montoTotal = montoTotal + precioF;
                                _cod_pro.add(Integer.parseInt(codProductoT));
                                break;
                            }
                        }

                        _clase.add(Integer.parseInt(clas));
                        _item.add(Integer.parseInt(item));
                        _linea.add(numeroLinea);
                        _orden.add("" + format.format(lineaQueda) + format.format(numeroLinea));
                        _cantidad.add(1);
                        _es_postre.add(espostre);
                        if (ingredientesDelItem.size() > 0) {
                            _tiene_agregado.add("S");
                        } else {
                            _tiene_agregado.add("N");
                        }
                        _agregado_de.add("0");
                        _ingrediente_de.add("0");
                        _tipo.add("M");
                        _subtipo.add("0");
                        _cantidad_ingrediente.add(1);
                        ArrayList<Integer> listaHeaders = new ArrayList<Integer>();
                        ArrayList<Boolean> listaSiAgregar = new ArrayList<Boolean>();
                        int lineaDeP = 0;
                        
                        for (String codProductoIngrediente : ingredientesDelItem.keySet()) {

                            Map<String, Object> ingredienteDelItem = (Map<String, Object>) ingredientesDelItem.get(codProductoIngrediente);

                            String tipoIng = (String) ingredienteDelItem.get("tipo");
                            if (tipoIng.equalsIgnoreCase("P")) {
                            	listaHeaders.add(Integer.parseInt(codProductoIngrediente));
                            }
                        }
                        if(listaHeaders.size() > 0){
                        	for(int i = 0; i<listaHeaders.size(); i++){
                        		listaSiAgregar.add(i, false);
                    			int idP = listaHeaders.get(i);
	                        	for (String codProductoIngrediente : ingredientesDelItem.keySet()) {
	                                Map<String, Object> ingredienteDelItem = (Map<String, Object>) ingredientesDelItem.get(codProductoIngrediente);
	                                String tipoIng = (String) ingredienteDelItem.get("tipo");
	                                boolean eleg = (Boolean) ingredienteDelItem.get("elegido");
	                                String ingDe = (String) ingredienteDelItem.get("ingredienteDe");
	                                if (eleg && ingDe.equalsIgnoreCase(Integer.toString(idP))) {
	                                	listaSiAgregar.add(i, true);
	                                	break;
	                                }
	                        	}
                        	}
                        	for(int i = 0; i<listaHeaders.size(); i++){
                        		if(listaSiAgregar.get(i)){
                    			int idP = listaHeaders.get(i);
                        		 numeroLinea = numeroLinea + 1;
                                 lineaDeP = numeroLinea;
                                 _cod_pro.add(idP);
                                 _clase.add(Integer.parseInt(clas));
                                 _item.add(Integer.parseInt(item));
                                 _linea.add(numeroLinea);
                                 _orden.add("" + format.format(lineaQueda) + format.format(numeroLinea));
                                 _cantidad.add(1);
                                 _es_postre.add("N");
                                 _tiene_agregado.add("S");
                                 _agregado_de.add("" + lineaQueda);
                                 _ingrediente_de.add("");
                                 _tipo.add("P");
                                 _subtipo.add("");
                                 _cantidad_ingrediente.add(1);
                        		
                                 for (String codProductoIngrediente : ingredientesDelItem.keySet()) {

                                    Map<String, Object> ingredienteDelItem = (Map<String, Object>) ingredientesDelItem.get(codProductoIngrediente);

                                    String tipoIng = (String) ingredienteDelItem.get("tipo");
//                                    
                                      
                                            boolean eleg = (Boolean) ingredienteDelItem.get("elegido");
                                            String ingDe = (String) ingredienteDelItem.get("ingredienteDe");
                                            if (eleg && ingDe.equalsIgnoreCase(Integer.toString(idP))) {
                                            	
                                                numeroLinea = numeroLinea + 1;
                                                _cod_pro.add(Integer.parseInt(codProductoIngrediente));
                                                _clase.add(Integer.parseInt(clas));
                                                _item.add(Integer.parseInt(item));
                                                _linea.add(numeroLinea);
                                                _orden.add("" + format.format(lineaQueda) + format.format(numeroLinea));
                                                _cantidad.add(1);
                                                _es_postre.add("N");
                                                _tiene_agregado.add("N");
                                                _agregado_de.add("" + lineaQueda);
                                                
                                                	_ingrediente_de.add("" + lineaDeP);
                                                _tipo.add("I");
                                                int subtipo = (Integer) ingredienteDelItem.get("IDtipo");
                                                _subtipo.add("" + subtipo);
                                                String reqCant = (String) ingredienteDelItem.get("requiere_cantidad");
                                                if (reqCant.equalsIgnoreCase("S")) {

                                                    int cantidadI = (Integer) ingredienteDelItem.get("cantidad");;
                                                    _cantidad_ingrediente.add(cantidadI);

                                                    String precioI = (String) ingredienteDelItem.get("precio");
                                                    float precioF = Float.parseFloat(precioI) * cantidadI;
                                                    montoTotal = montoTotal + precioF;
                                                } else {
                                                    String precioI = (String) ingredienteDelItem.get("precio");
                                                    float precioF = Float.parseFloat(precioI);
                                                    montoTotal = montoTotal + precioF;
                                                    _cantidad_ingrediente.add(1);
                                                }
                                            }

                                        
                                    }
                        		}
                                }
                        	}else{
                        		for (String codProductoIngrediente : ingredientesDelItem.keySet()) {

                                    Map<String, Object> ingredienteDelItem = (Map<String, Object>) ingredientesDelItem.get(codProductoIngrediente);
                        		boolean eleg = (Boolean) ingredienteDelItem.get("elegido");
                                if (eleg) {
                                	
                                    numeroLinea = numeroLinea + 1;
                                    _cod_pro.add(Integer.parseInt(codProductoIngrediente));
                                    _clase.add(Integer.parseInt(clas));
                                    _item.add(Integer.parseInt(item));
                                    _linea.add(numeroLinea);
                                    _orden.add("" + format.format(lineaQueda) + format.format(numeroLinea));
                                    _cantidad.add(1);
                                    _es_postre.add("N");
                                    _tiene_agregado.add("N");
                                    _agregado_de.add("" + lineaQueda);
                                    
                                    	_ingrediente_de.add("" + lineaQueda);
                                    
                                    _tipo.add("I");
                                    int subtipo = (Integer) ingredienteDelItem.get("IDtipo");
                                    _subtipo.add("" + subtipo);
                                    String reqCant = (String) ingredienteDelItem.get("requiere_cantidad");
                                    if (reqCant.equalsIgnoreCase("S")) {

                                        int cantidadI = (Integer) ingredienteDelItem.get("cantidad");;
                                        _cantidad_ingrediente.add(cantidadI);

                                        String precioI = (String) ingredienteDelItem.get("precio");
                                        float precioF = Float.parseFloat(precioI) * cantidadI;
                                        montoTotal = montoTotal + precioF;
                                    } else {
                                        String precioI = (String) ingredienteDelItem.get("precio");
                                        float precioF = Float.parseFloat(precioI);
                                        montoTotal = montoTotal + precioF;
                                        _cantidad_ingrediente.add(1);
                                    }
                                }
                                
                        		}
                        	}
                        
                        
                        for (String codProductoOpcion : opcionesDelItem.keySet()) {

                            Map<String, Object> opcionDelItem = (Map<String, Object>) opcionesDelItem.get(codProductoOpcion);
                            boolean elegO = (Boolean) opcionDelItem.get("elegido");
                            if (elegO) {
                                numeroLinea = numeroLinea + 1;
                                String precioO = (String) opcionDelItem.get("precio");
                                float precioF = Float.parseFloat(precioO);
                                montoTotal = montoTotal + precioF;
                                _cod_pro.add(Integer.parseInt(codProductoOpcion));
                                _clase.add(Integer.parseInt(clas));
                                _item.add(Integer.parseInt(item));
                                _linea.add(numeroLinea);
                                _orden.add("" + format.format(lineaQueda) + format.format(numeroLinea));
                                _cantidad.add(1);
                                _es_postre.add("N");
                                _tiene_agregado.add("N");
                                _agregado_de.add("" + lineaQueda);
                                _ingrediente_de.add("0");
                                _tipo.add("M");
                                _subtipo.add("0");
                                _cantidad_ingrediente.add(1);
                            }
                        }
                    }
//                                      
//                                    total   
//TIPO DE PAGO

                    webTarget = webTargetRoot.path("InsertarOrden");
                    JSONObject respuestaJson = new JSONObject();
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("cod_pedido", cod_pedidoInsertarPedido);
                    form.param("grupo", grupoInsertarPedido);
                    form.param("no_emple", no_empleado);

                    form.param("no_cliente", no_clienteInsertarPedido);
                    form.param("telefono", telefonoInsertarPedido);
                    form.param("horario", horarioInsertarPedido);
                    form.param("tipo_pago", tipo_pago);
                    String montoString = Float.toString(montoTotal);

                    if (tipo_pago == null) {
                        tipo_pago = "EF";
                    }

                    if (tipo_pago.equalsIgnoreCase("EF")) {
                        monto_efectivo = montoString;
                    } else if (tipo_pago.equalsIgnoreCase("TA")) {
                        monto_tarjeta = montoString;
                    } else if ((tipo_pago.equalsIgnoreCase("MI"))) {
                        String strTar = request.getParameter("monto_tarjeta");
                        String strEf = request.getParameter("monto_efectivo");
                        try {
                            float montEf = Float.parseFloat(strEf);
                            float montTar = Float.parseFloat(strTar);

                            if (montEf < montoTotal) {
                                monto_efectivo = Float.toString(montEf);
                                float resto = montoTotal - montEf;
                                monto_tarjeta = Float.toString(resto);
                            } else {
                                if (montTar < montoTotal) {
                                    monto_tarjeta = Float.toString(montTar);
                                    float resto = montoTotal - montTar;
                                    monto_efectivo = Float.toString(resto);
                                } else {
                                    PrintWriter out = response.getWriter();
                                    response.setContentType("application/json");
                                    respuestaJson.put("codigo", "-1");
                                    respuestaJson.put("mensaje", "Los montos no coinciden con el total");
                                }
                            }
                        } catch (Exception f) {
                            PrintWriter out = response.getWriter();
                            response.setContentType("application/json");
                            respuestaJson.put("codigo", "-1");
                            respuestaJson.put("mensaje", f.getMessage());
                        }
                    }
                    form.param("monto_efectivo", monto_efectivo);
                    form.param("monto_tarjeta", monto_tarjeta);

                    form.param("total", montoString);
                    form.param("tipo_orden", tipoOrden);
                    form.param("fechaInAdvance", fechaInAdvance);
                    form.param("horaInAdvance", horaInAdvance);
                    form.param("orden_original", orden_original);
                    form.param("observaciones_orden", observaciones_orden);
                    form.param("observaciones_entrega", observaciones_entrega);
                    form.param("confirmar", confirmar);
                    form.param("confirmar_texto", confirmar_texto);
                    form.param("cod_producto", _cod_pro.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("clase", _clase.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("item", _item.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("linea", _linea.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("orden", _orden.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("cantidad", _cantidad.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("es_postre", _es_postre.toString().replace("[", "").replace("]", "").replace(" ", ""));

                    form.param("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
                    form.param("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("tipo", _tipo.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("subtipo", _subtipo.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    form.param("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", "").replace(" ", ""));

                    responseString = wsResponse(webTarget, form);

                    jsonResponse = new JSONObject(responseString);
                    String codigoRespuestaInsert = jsonResponse.getString("codigo");

                    if (codigoRespuestaInsert.equals("0")) {

                        if (tipo_pago.equalsIgnoreCase("TA") || tipo_pago.equalsIgnoreCase("MI")) {

                            form = new Form();
                            form.param("no_cia", nc);
                            form.param("cod_pedido", cod_pedidoInsertarPedido);
                            form.param("tarjeta", tarjeta);
                            form.param("anio_vence", anio_vence);
                            form.param("mes_vence", mes_vence);
                            form.param("monto_tarjeta", monto_tarjeta);
                            webTarget = webTargetRoot.path("InsertarTarjeta");
                            responseString = wsResponse(webTarget, form);
                            jsonResponse = new JSONObject(responseString);
                            String codigoRespuestaInsertarTarjeta = jsonResponse.getString("codigo");

                            if (codigoRespuestaInsertarTarjeta.equals("0")) {
//                            	if(confirmar.equalsIgnoreCase("1")){
//                            		form = new Form();
//                                    form.param("no_cia", nc);
//                                    form.param("cod_pedido", cod_pedidoInsertarPedido);
//                                    form.param("tarjeta", tarjeta);
//                                    form.param("anio_vence", anio_vence);
//                                    form.param("mes_vence", mes_vence);
//                                    form.param("monto_tarjeta", monto_tarjeta);
//                                    webTarget = webTargetRoot.path("InsertarTarjeta");
//                                    responseString = wsResponse(webTarget, form);
//                                    jsonResponse = new JSONObject(responseString);
//                                    String codigoRespuestaInsertarTarjeta = jsonResponse.getString("codigo");
//
//                                    if (codigoRespuestaInsertarTarjeta.equals("0")) {
//                                    	
//                                    }
//                            	}
                                PrintWriter out = response.getWriter();
                                response.setContentType("application/json");
                                
                                respuestaJson.put("listaingredientes", _listasIngredientes.toString());
                                respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                                respuestaJson.put("numero_orden", jsonResponse.getString("numero_orden"));
                                respuestaJson.put("codigo", jsonResponse.getString("codigo"));
                                respuestaJson.put("no_cia", nc);
                                respuestaJson.put("cod_pedido", cod_pedidoInsertarPedido);
                                respuestaJson.put("grupo", grupoInsertarPedido);
                                respuestaJson.put("no_cliente", no_clienteInsertarPedido);
                                respuestaJson.put("telefono", telefonoInsertarPedido);
                                respuestaJson.put("horario", horarioInsertarPedido);
                                respuestaJson.put("tipo_pago", tipo_pago);
                                respuestaJson.put("monto_efectivo", montoString);
                                respuestaJson.put("monto_tarjeta", monto_tarjeta);
                                respuestaJson.put("total", montoString);
                                respuestaJson.put("tipo_orden", tipoOrden);
                                respuestaJson.put("fechaInAdvance", fechaInAdvance);
                                respuestaJson.put("horaInAdvance", horaInAdvance);
                                respuestaJson.put("orden_original", orden_original);
                                respuestaJson.put("observaciones_orden", observaciones_orden);
                                respuestaJson.put("observaciones_entrega", observaciones_entrega);
                                respuestaJson.put("confirmar", confirmar);
                                respuestaJson.put("confirmar_texto", confirmar_texto);
                                respuestaJson.put("cod_producto", _cod_pro.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("clase", _clase.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("item", _item.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("linea", _linea.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("orden", _orden.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("cantidad", _cantidad.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("es_postre", _es_postre.toString().replace("[", "").replace("]", ""));
        
                                respuestaJson.put("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("tipo", _tipo.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("subtipo", _subtipo.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", ""));
                                out.print(respuestaJson.toString());
                                
                            } else {
                                PrintWriter out = response.getWriter();
                                response.setContentType("application/json");
                                respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                                respuestaJson.put("codigo", jsonResponse.getString("codigo"));
                                respuestaJson.put("no_cia", nc);
                                respuestaJson.put("cod_pedido", cod_pedidoInsertarPedido);
                                respuestaJson.put("grupo", grupoInsertarPedido);
                                respuestaJson.put("no_cliente", no_clienteInsertarPedido);
                                respuestaJson.put("telefono", telefonoInsertarPedido);
                                respuestaJson.put("horario", horarioInsertarPedido);
                                respuestaJson.put("tipo_pago", tipo_pago);
                                respuestaJson.put("monto_efectivo", montoString);
                                respuestaJson.put("monto_tarjeta", monto_tarjeta);
                                respuestaJson.put("total", montoString);
                                respuestaJson.put("tipo_orden", tipoOrden);
                                respuestaJson.put("fechaInAdvance", fechaInAdvance);
                                respuestaJson.put("horaInAdvance", horaInAdvance);
                                respuestaJson.put("orden_original", orden_original);
                                respuestaJson.put("observaciones_orden", observaciones_orden);
                                respuestaJson.put("observaciones_entrega", observaciones_entrega);
                                respuestaJson.put("confirmar", confirmar);
                                respuestaJson.put("confirmar_texto", confirmar_texto);
                                respuestaJson.put("cod_producto", _cod_pro.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("clase", _clase.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("item", _item.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("linea", _linea.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("orden", _orden.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("cantidad", _cantidad.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("es_postre", _es_postre.toString().replace("[", "").replace("]", ""));
        
                                respuestaJson.put("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("tipo", _tipo.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("subtipo", _subtipo.toString().replace("[", "").replace("]", ""));
                                respuestaJson.put("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", ""));
                                out.print(respuestaJson.toString());
                            }
                        } else {
                            
                        	PrintWriter out = response.getWriter();
                            response.setContentType("application/json");
                            respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
                            respuestaJson.put("numero_orden", jsonResponse.getString("numero_orden"));
                            respuestaJson.put("listaingredientes", _listasIngredientes.toString());
                            respuestaJson.put("codigo", jsonResponse.getString("codigo"));
                            respuestaJson.put("no_cia", nc);
                          respuestaJson.put("cod_pedido", cod_pedidoInsertarPedido);
                          respuestaJson.put("grupo", grupoInsertarPedido);
                          respuestaJson.put("no_cliente", no_clienteInsertarPedido);
                          respuestaJson.put("telefono", telefonoInsertarPedido);
                          respuestaJson.put("horario", horarioInsertarPedido);
                          respuestaJson.put("tipo_pago", tipo_pago);
                          respuestaJson.put("monto_efectivo", montoString);
                          respuestaJson.put("monto_tarjeta", monto_tarjeta);
                          respuestaJson.put("total", montoString);
                          respuestaJson.put("tipo_orden", tipoOrden);
                          respuestaJson.put("fechaInAdvance", fechaInAdvance);
                          respuestaJson.put("horaInAdvance", horaInAdvance);
                          respuestaJson.put("orden_original", orden_original);
                          respuestaJson.put("observaciones_orden", observaciones_orden);
                          respuestaJson.put("observaciones_entrega", observaciones_entrega);
                          respuestaJson.put("confirmar", confirmar);
                          respuestaJson.put("confirmar_texto", confirmar_texto);
                          respuestaJson.put("cod_producto", _cod_pro.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("clase", _clase.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("item", _item.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("linea", _linea.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("orden", _orden.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("cantidad", _cantidad.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("es_postre", _es_postre.toString().replace("[", "").replace("]", ""));
  
                          respuestaJson.put("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("tipo", _tipo.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("subtipo", _subtipo.toString().replace("[", "").replace("]", ""));
                          respuestaJson.put("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", ""));
                            out.print(respuestaJson.toString());
                            
                        }

//                                respuestaJson.put("mensaje", "Si funciona");
//                        respuestaJson.put("codigo", codigoRespuestaInsert);
//                        respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
//                        respuestaJson.put("numero_orden", jsonResponse.getString("numero_orden"));
//                        respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
//                        respuestaJson.put("no_cia", nc);
//                        respuestaJson.put("cod_pedido", cod_pedidoInsertarPedido);
//                        respuestaJson.put("grupo", grupoInsertarPedido);
//                        respuestaJson.put("no_cliente", no_clienteInsertarPedido);
//                        respuestaJson.put("telefono", telefonoInsertarPedido);
//                        respuestaJson.put("horario", horarioInsertarPedido);
//                        respuestaJson.put("tipo_pago", tipo_pago);
//                        respuestaJson.put("monto_efectivo", montoString);
//                        respuestaJson.put("monto_tarjeta", monto_tarjeta);
//                        respuestaJson.put("total", montoString);
//                        respuestaJson.put("tipo_orden", tipoOrden);
//                        respuestaJson.put("fechaInAdvance", fechaInAdvance);
//                        respuestaJson.put("horaInAdvance", horaInAdvance);
//                        respuestaJson.put("orden_original", orden_original);
//                        respuestaJson.put("observaciones_orden", observaciones_orden);
//                        respuestaJson.put("observaciones_entrega", observaciones_entrega);
//                        respuestaJson.put("confirmar", confirmar);
//                        respuestaJson.put("confirmar_texto", confirmar_texto);
//                        respuestaJson.put("cod_producto", _cod_pro.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("clase", _clase.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("item", _item.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("linea", _linea.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("orden", _orden.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("cantidad", _cantidad.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("es_postre", _es_postre.toString().replace("[", "").replace("]", ""));
//
//                        respuestaJson.put("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("tipo", _tipo.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("subtipo", _subtipo.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", ""));
                    } else {
                        PrintWriter out = response.getWriter();
                        response.setContentType("application/json");
                        respuestaJson.put("mensaje", jsonResponse.getString("mensaje"));
//                        respuestaJson.put("no_cia", nc);
//                        respuestaJson.put("cod_pedido", cod_pedidoInsertarPedido);
//                        respuestaJson.put("grupo", grupoInsertarPedido);
//                        respuestaJson.put("no_cliente", no_clienteInsertarPedido);
//                        respuestaJson.put("telefono", telefonoInsertarPedido);
//                        respuestaJson.put("horario", horarioInsertarPedido);
//                        respuestaJson.put("tipo_pago", tipo_pago);
//                        respuestaJson.put("monto_efectivo", montoString);
//                        respuestaJson.put("monto_tarjeta", monto_tarjeta);
//                        respuestaJson.put("total", montoString);
//                        respuestaJson.put("tipo_orden", tipoOrden);
//                        respuestaJson.put("fechaInAdvance", fechaInAdvance);
//                        respuestaJson.put("horaInAdvance", horaInAdvance);
//                        respuestaJson.put("orden_original", orden_original);
//                        respuestaJson.put("observaciones_orden", observaciones_orden);
//                        respuestaJson.put("observaciones_entrega", observaciones_entrega);
//                        respuestaJson.put("confirmar", confirmar);
//                        respuestaJson.put("confirmar_texto", confirmar_texto);
//                        respuestaJson.put("cod_producto", _cod_pro.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("clase", _clase.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("item", _item.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("linea", _linea.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("orden", _orden.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("cantidad", _cantidad.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("es_postre", _es_postre.toString().replace("[", "").replace("]", ""));
//
//                        respuestaJson.put("tiene_agregado", _tiene_agregado.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("agregado_de", _agregado_de.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("ingrediente_de", _ingrediente_de.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("tipo", _tipo.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("subtipo", _subtipo.toString().replace("[", "").replace("]", ""));
//                        respuestaJson.put("cantidad_ingrediente", _cantidad_ingrediente.toString().replace("[", "").replace("]", ""));
                        out.print(respuestaJson.toString());
                    }

                    break;

                // Gestionar Orden
                case 1984:
                    String op = (String) session.getAttribute("OpId");
                    webTarget = webTargetRoot.path("InsertarLlamada");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("telefono", request.getParameter("telefono"));
                    form.param("cod_tipologia", request.getParameter("call"));
                    form.param("comentarios", request.getParameter("text"));
                    form.param("operador", op);
                    responseString = wsResponse(webTarget, form);
                    PrintWriter out = response.getWriter();
                    try {
                        jsonResponse = new JSONObject(responseString);
                        String codigoRespuesta = jsonResponse.getString("codigo");
                        if (codigoRespuesta.equals("0")) {

                            JSONObject respuestaJson2 = new JSONObject();
                            respuestaJson2.put("funciona", "SI");
                            out.print(respuestaJson2.toString());
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson2 = new JSONObject();
                        respuestaJson2.put("funciona", "No por");
                        out.print(respuestaJson2.toString());
                    }
                    break;

                case 911:
                    String UltimaOrdenCode = request.getParameter("no_orden");
                    webTarget = webTargetRoot.path("RecuperarDatosOrden");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("numero_orden", UltimaOrdenCode);
                    responseString = wsResponse(webTarget, form);
                    PrintWriter out2 = response.getWriter();
                    try {
                        jsonResponse = new JSONObject(responseString);
                        JSONObject respuestaJson2 = new JSONObject();
                        String CodRes = jsonResponse.getString("codigo");

                        if (CodRes.equals("0")) {
                        	JSONObject t = jsonResponse.getJSONArray("items").getJSONObject(0);
                        		if(t.getString("estado_1").equalsIgnoreCase("S") && t.getString("estado_2").equalsIgnoreCase("N") 
                        				&& t.getString("estado_3").equalsIgnoreCase("N") && t.getString("estado_4").equalsIgnoreCase("N")
                        				&& t.getString("estado_5").equalsIgnoreCase("N")){
                        			List<String> listadoParaModificar =  (ArrayList<String>) session.getAttribute("listadoParaModificar");
									if(listadoParaModificar == null){
										listadoParaModificar = new ArrayList<String>();
									}
									listadoParaModificar.add(t.getString(UltimaOrdenCode));
                        		}
                        	
                            respuestaJson2.put("orden", jsonResponse.getJSONArray("items").toString());
                            out2.print(respuestaJson2.toString());
                        } else {
                            response.setContentType("application/json");
                            respuestaJson2.put("mensaje", jsonResponse.getString("mensaje"));
                            out2.print(respuestaJson2.toString());
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson2 = new JSONObject();
                        respuestaJson2.put("funciona", "No por 1");
                        out2.print(respuestaJson2.toString());
                    }
                    break;
                case 102313:

                    String RestAsig = request.getParameter("asig");
                    webTarget = webTargetRoot.path("RecuperarObservacionesDelRestaurante");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("tienda", RestAsig);
                    responseString = wsResponse(webTarget, form);
                    PrintWriter out3 = response.getWriter();

                    try {
                        jsonResponse = new JSONObject(responseString);
                        JSONObject respuestaJson2 = new JSONObject();

                        String CodRes = jsonResponse.getString("codigo");
                        if (CodRes.equals("0")) {
                            String observaciones_promocion = jsonResponse.getString("observaciones_promocion");
                            String observaciones_servicio = jsonResponse.getString("observaciones_servicio");
                            String observaciones_producto = jsonResponse.getString("observaciones_producto");
                            respuestaJson2.put("observaciones_promocion", observaciones_promocion);
                            respuestaJson2.put("observaciones_servicio", observaciones_servicio);
                            respuestaJson2.put("observaciones_producto", observaciones_producto);
                            respuestaJson2.put("o", "1");
                            out3.print(respuestaJson2.toString());
                        } else if (CodRes.equals("-1")) {
                            response.setContentType("application/json");
                            respuestaJson2.put("o", "-1");
                            respuestaJson2.put("mensaje", jsonResponse.getString("mensaje"));
                            out3.print(respuestaJson2.toString());
                        }
                        {
                            response.setContentType("application/json");
                            respuestaJson2.put("mensaje", jsonResponse.getString("mensaje"));
                            out3.print(respuestaJson2.toString());
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson2 = new JSONObject();
                        respuestaJson2.put("funciona", "No por 1");
                        out3.print(respuestaJson2.toString());
                    }

                    break;
                case 98786:

                    String OrdNum = request.getParameter("no_orden");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("numero_orden", OrdNum);
                    webTarget = webTargetRoot.path("RecuperarDetalle");
                    responseString = wsResponse(webTarget, form);
                    PrintWriter NewOut = response.getWriter();
                    try {
                        jsonResponse = new JSONObject(responseString);
                        JSONObject respuestaJson2 = new JSONObject();
                        String CodRes = jsonResponse.getString("codigo");
                        if (CodRes.equals("0")) {
                            respuestaJson2.put("detalles", jsonResponse.getJSONArray("items"));
                            NewOut.print(respuestaJson2.toString());;
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson2 = new JSONObject();
                        respuestaJson2.put("funciona", e.toString());
                        NewOut.print(respuestaJson2.toString());
                    }

                    break;
                case 23419:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesionE = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");

                    Map<String, Object> ordenEnSesionE = (HashMap<String, Object>) ordenesEnSesionE.get(cod_pedido);
                    String codUE = request.getParameter("codU");

                    List listaActualizacion = new ArrayList();
                    if (ordenEnSesionE != null) {
                        Map<String, Object> itemEli = (HashMap<String, Object>) ordenEnSesionE.get(codUE);
                        if (itemEli != null) {
                            Map<String, Object> prop = (HashMap<String, Object>) itemEli.get("propiedades");
                            String claseElim = (String) prop.get("no_clase");
                            String itemElim = (String) prop.get("no_item");

//                            ordenEnSesionE.remove(codUE);

                            Iterator<Map.Entry<String, Object>> iter = ordenEnSesionE.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, Object> entry = iter.next();
                                String productoEnOrden = (String) entry.getKey();
                                if (productoEnOrden.equalsIgnoreCase(codUE)) {
                                    iter.remove();
                                }
                            }
                            while (iter.hasNext()) {
                                Map.Entry<String, Object> entry = iter.next();

                                Map<String, Object> productoEnOrden = (HashMap<String, Object>) entry.getValue();
                                Map<String, Object> propied = (HashMap<String, Object>) productoEnOrden.get("propiedades");
                                String claseIt = (String) propied.get("no_clase");
                                String itemIt = (String) propied.get("no_item");

                                if (claseIt.equalsIgnoreCase(claseElim) && itemIt.equalsIgnoreCase(itemElim)) {
                                    listaActualizacion.add(entry.getValue());
                                    iter.remove();
                                }
                            }

                            int contadorProducto = 1;
                            for (int a = 0; a < listaActualizacion.size(); a++) {
                                String newKey = claseElim + itemElim + "P" + contadorProducto;
                                ordenEnSesionE.put(newKey, listaActualizacion.get(a));
                                contadorProducto++;
                            }

//                    		for(String codProducto : ordenEnSesionE.keySet()){
//                    			Map<String, Object> productoEnOrden = (Map<String, Object>) ordenEnSesionE.get(codProducto);
//                    			Map<String, Object> propied  = (HashMap<String, Object>) productoEnOrden.get("propiedades");
//                    			String claseIt = (String) prop.get("no_clase");
//                        		String itemIt = (String) prop.get("no_item");
//                        		if(claseIt.equalsIgnoreCase(claseElim) && itemIt.equalsIgnoreCase(itemElim)){
//                        			listaActualizacion.add(ordenEnSesionE.remove(codProducto));
//                        			
//                        		}
//                    		}
                            //ordenEnSesionE.put(newKey, );
                        }
                    }

                    ordenesEnSesionE.put(cod_pedido, ordenEnSesionE);
                    session.setAttribute("ordenesEnSesion", ordenesEnSesionE);
                    PrintWriter outE = response.getWriter();

                    response.setContentType("application/json");
                    JSONObject respuestaJson2 = new JSONObject();
                    respuestaJson2.put("orden", ordenesEnSesionE.toString());
                    outE.print(respuestaJson2.toString());

                    break;
                case 25725:
                    String tienda = request.getParameter("tienda");
                    String fecha = request.getParameter("fecha");
                    String hora = request.getParameter("hora");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("tienda", tienda);
                    form.param("fecha", fecha);
                    form.param("hora", hora);
                    webTarget = webTargetRoot.path("RecuperarCargaPorRestYHora");
                    responseString = wsResponse(webTarget, form);
                    PrintWriter outw = response.getWriter();
                    try {
                        jsonResponse = new JSONObject(responseString);
                        JSONObject respuestaJson3 = new JSONObject();
                        String CodRes = jsonResponse.getString("codigo");
                        if (CodRes.equals("0")) {
                            respuestaJson3.put("cod", "0");
                            respuestaJson3.put("data", jsonResponse);
                            outw.print(respuestaJson3.toString());;
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson3 = new JSONObject();
                        respuestaJson3.put("cod", "-1");
                        respuestaJson3.put("mensaje", e.toString());
                        outw.print(respuestaJson3.toString());
                    }

                    break;
                case 23420:
                    cod_pedido = request.getParameter("cod_pedido");
                    HashMap<String, Object> ordenesEnSesionA = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionA = (HashMap<String, Object>) ordenesEnSesionA.get(cod_pedido);
                    String claseDel = request.getParameter("claseDel");
                    String itemDel = request.getParameter("itemDel");

                    if (ordenEnSesionA != null) {
//                    		for(String codProducto : ordenEnSesionA.keySet()){
//                    			Map<String, Object> productoEnOrdenA = (Map<String, Object>) ordenEnSesionA.get(codProducto);
//                    			Map<String, Object> propieda  = (HashMap<String, Object>) productoEnOrdenA.get("propiedades");
//                    			String claseIt = (String) propieda.get("no_clase");
//                        		String itemIt = (String) propieda.get("no_item");
//                        		if(claseIt.equalsIgnoreCase(claseDel) && itemIt.equalsIgnoreCase(itemDel)){
//                        			ordenEnSesionA.remove(codProducto);
//                        		}
//                    		}
                        Iterator<Map.Entry<String, Object>> iter = ordenEnSesionA.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();

                            Map<String, Object> productoEnOrden = (HashMap<String, Object>) entry.getValue();
                            Map<String, Object> propied = (HashMap<String, Object>) productoEnOrden.get("propiedades");
                            String claseIt = (String) propied.get("no_clase");
                            String itemIt = (String) propied.get("no_item");

                            if (claseIt.equalsIgnoreCase(claseDel) && itemIt.equalsIgnoreCase(itemDel)) {
                                iter.remove();
                            }
                        }
                    }
                    ordenesEnSesionA.put(cod_pedido, ordenEnSesionA);
                    session.setAttribute("ordenesEnSesion", ordenesEnSesionA);

                    PrintWriter outA = response.getWriter();

                    response.setContentType("application/json");
                    JSONObject respuestaJson3 = new JSONObject();
                    respuestaJson3.put("orden", ordenEnSesionA.toString());
                    outA.print(respuestaJson3.toString());

                    break;
                case 1207:
                    String EmpCode = request.getParameter("no_emp");
                    form = new Form();
                    form.param("no_cia", nc);
                    form.param("no_emple", EmpCode);
                    webTarget = webTargetRoot.path("RecuperarOperador");
                    responseString = wsResponse(webTarget, form);
                    PrintWriter NewOut2 = response.getWriter();
                    try {
                        jsonResponse = new JSONObject(responseString);
                        JSONObject respuestaJson4 = new JSONObject();
                        String CodRes = jsonResponse.getString("codigo");
                        if (CodRes.equals("0")) {
                            respuestaJson4.put("empleado", jsonResponse.getString("nombre_empleado"));
                            NewOut2.print(respuestaJson4.toString());;
                        }
                    } catch (JSONException e) {
                        response.setContentType("application/json");
                        JSONObject respuestaJson4 = new JSONObject();
                        respuestaJson4.put("funciona", e.toString());
                        NewOut2.print(respuestaJson4.toString());
                    }

                    break;

                case 1109:
                	 String no_orden = request.getParameter("cod_pedido");
                    
                    ArrayList<String> listadoParaModificar = (ArrayList<String>) session.getAttribute("listadoParaModificar");
                    if(listadoParaModificar.contains(listadoParaModificar)){
                    	form = new Form();
                        form.param("no_cia", nc);
                        form.param("no_orden", no_orden);
                        webTarget = webTargetRoot.path("RecuperarOrdenParaModificar");

                        responseString = wsResponse(webTarget, form);

                        jsonResponse = new JSONObject(responseString);

                        JSONArray itemsArray = jsonResponse.getJSONArray("items");

                        int howM = itemsArray.length();

                        _cod_pro = new ArrayList<Integer>();
                        _clase = new ArrayList<Integer>();
                        _item = new ArrayList<Integer>();
                        _linea = new ArrayList<Integer>();
                        _orden = new ArrayList<String>();
                        _cantidad = new ArrayList<Integer>();
                        _es_postre = new ArrayList<String>();
                        _tiene_agregado = new ArrayList<String>();
                        _agregado_de = new ArrayList<String>();
                        _ingrediente_de = new ArrayList<String>();
                        _tipo = new ArrayList<String>();
                        _subtipo = new ArrayList<String>();
                        _cantidad_ingrediente = new ArrayList<Integer>();

                        for (int k = 0; k < howM; k++) {
                            JSONObject newItems = itemsArray.getJSONObject(k);
                            _cod_pro.add(Integer.parseInt(newItems.getString("cod_producto")));
                            _clase.add(Integer.parseInt(newItems.getString("clase")));
                            _item.add(Integer.parseInt(newItems.getString("item")));
                            _linea.add(Integer.parseInt(newItems.getString("linea")));
                            _orden.add("" + newItems.getString(""));
                            _cantidad.add(Integer.parseInt(newItems.getString("cantidad")));
                            _es_postre.add(newItems.getString("es_postre"));
                            _tiene_agregado.add(newItems.getString("tiene_agregado"));
                            _agregado_de.add(newItems.getString("agregado_de"));
                            _ingrediente_de.add(newItems.getString("ingrediente_de"));
                            _tipo = new ArrayList<String>();
                            _subtipo = new ArrayList<String>();
                            _cantidad_ingrediente.add(Integer.parseInt(newItems.getString("cantidad_ingrediente")));
                        }
                    }
                    
                    
                    break;
                    //NUEVO SERVICIO 04/06
                case 4615:
                	//CAMBIO DE TAMANIO OPCIONES
                	cod_pedido = request.getParameter("cod_pedido");
                    Map<String, Object> ordenesEnSesionRm = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionRm = (HashMap<String, Object>) ordenesEnSesionRm.get(cod_pedido);
                    String codURm = request.getParameter("codU");
                        
                    Map<String, Object> itemEnSesionRm = (HashMap<String, Object>) ordenEnSesionRm.get(codURm);
                   
                    String tamanoR = "";
                    String cod_productoReceta = "";
                    Map<String, Object> listrtamanosDelItem = (Map<String, Object>) itemEnSesionRm.get("tamano");
                    for (String codTam : listrtamanosDelItem.keySet()) {
                        HashMap<String, Object> tamanoDelIterado = (HashMap<String, Object>) listrtamanosDelItem.get(codTam);
                        if ((Boolean) tamanoDelIterado.get("elegido") == true) {
                        	tamanoR = (String) tamanoDelIterado.get("tamano");
                        	cod_productoReceta = codTam;
                            break;
                        }
                    }
                    List<String> listaElegidos = new ArrayList<String>();
                    List<String> subtipos = new ArrayList<String>();
                    List<Integer> cantidadesI = new ArrayList<Integer>();
                    if (itemEnSesionRm != null) {
                        Map<String, Object> listaIngredDelItem = (Map<String, Object>) itemEnSesionRm.get("receta");
                        for (String codIngr : listaIngredDelItem.keySet()) {
                            Map<String, Object> ingredienteDelItem = (Map<String, Object>) listaIngredDelItem.get(codIngr);
                            String tipoDelIngredienteParaP = (String) ingredienteDelItem.get("IDtipo");
                            boolean ele = (Boolean) ingredienteDelItem.get("elegido");
                            if(ele){
                            	String subt = (String) ingredienteDelItem.get("subtipo");
                            	int cantE = (Integer) ingredienteDelItem.get("cantidad");
                            	listaElegidos.add(codIngr);
                            	subtipos.add(subt);
                            	cantidadesI.add(cantE);
                            }
                        }
                        webTarget = webTargetRoot.path("RecuperarReceta");

                        form = new Form();
                        form.param("no_cia", nc);
                        form.param("app_destino", "CC");
                        form.param("cod_producto", cod_productoReceta);
                        if (Integer.parseInt(tamanoR) == 0) {
                            form.param("es_menu", "N");
                            responseString = wsResponse(webTarget, form);
                            jsonResponse = new JSONObject(responseString);

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    Map<String, Object> listaDeIngredientes = new HashMap<String, Object>();
                                    JSONArray recetaitemsws = jsonResponse.getJSONArray("items");
                                    boolean haying = false;
                                    boolean hayadic = false;
                                    int conta = recetaitemsws.length();
                                    for (int o = 0; o < conta; o++) {
                                        JSONObject recetaitem = recetaitemsws.getJSONObject(o);
                                        Map<String, Object> itemIngrediente = new HashMap<String, Object>();
                                        itemIngrediente.put("cod_producto", recetaitem.getString("cod_ingrediente"));
                                        itemIngrediente.put("precio", recetaitem.getString("precio"));
                                        itemIngrediente.put("elegido", false);
                                        itemIngrediente.put("cantidad", 0);
                                        itemIngrediente.put("IDtipo", "");
                                        if(listaElegidos.contains(recetaitem.getString("cod_ingrediente"))){
                                        	int idn = listaElegidos.indexOf(recetaitem.getString("cod_ingrediente"));
	                                        itemIngrediente.put("elegido", true);
	                                        itemIngrediente.put("cantidad", cantidadesI.get(idn));
	                                        itemIngrediente.put("IDtipo", subtipos.get(idn));
                                        }
                                        itemIngrediente.put("requiere_cantidad", recetaitem.getString("requiere_cantidad"));
                                        itemIngrediente.put("imagen1", recetaitem.getString("imagen1"));
                                        itemIngrediente.put("nombre", recetaitem.getString("nombre"));
                                        
                                        itemIngrediente.put("tipo", recetaitem.getString("tipo"));
                                        
                                        listaDeIngredientes.put(recetaitem.getString("cod_ingrediente"), itemIngrediente);
                                    }
                                    itemEnSesionRm.put("receta", listaDeIngredientes);
                                    ordenEnSesionRm.put(codURm, itemEnSesionRm);
                                }
                            }
                        } else {
                            form.param("es_menu", "1");
                            responseString = wsResponse(webTarget, form);
                            jsonResponse = new JSONObject(responseString);

                            if (jsonResponse != null) {
                                String codigoRespuesta = jsonResponse.getString("codigo");
                                if (codigoRespuesta.equals("0")) {
                                    Map<String, Object> listaDeIngredientes = new HashMap<String, Object>();

                                    JSONArray recetaitemsws = jsonResponse.getJSONArray("items");

                                    int conta = recetaitemsws.length();
                                    boolean he = false;
                                    for (int o = 0; o < conta; o++) {
                                        JSONObject recetaitem = recetaitemsws.getJSONObject(o);
                                        Map<String, Object> itemIngrediente = new HashMap<String, Object>();
                                        itemIngrediente.put("cod_producto", recetaitem.getString("cod_ingrediente"));
                                        itemIngrediente.put("precio", recetaitem.getString("precio"));
                                        itemIngrediente.put("elegido", false);
                                        itemIngrediente.put("requiere_cantidad", recetaitem.getString("requiere_cantidad"));
                                        itemIngrediente.put("imagen1", recetaitem.getString("imagen1"));
                                        itemIngrediente.put("nombre", recetaitem.getString("nombre"));
                                        itemIngrediente.put("cantidad", 0);
                                        itemIngrediente.put("tipo", "P");
                                        itemIngrediente.put("IDtipo", "");
                                        recetaitem.put("tipo", "P");
                                        listaDeIngredientes.put(recetaitem.getString("cod_ingrediente"), itemIngrediente);
                                        form = new Form();
                                        form.param("no_cia", nc);
                                        form.param("app_destino", "CC");
                                        form.param("cod_producto", recetaitem.getString("cod_ingrediente"));
                                        form.param("es_menu", "N");
                                        responseString = wsResponse(webTarget, form);
                                        jsonResponse = new JSONObject(responseString);
                                        String codigoRespuesta2 = jsonResponse.getString("codigo");
                                        if (codigoRespuesta2.equals("0")) {
                                            Map<String, Object> listaDeIngredientes2 = new HashMap<String, Object>();
                                            JSONArray recetaitemsws2 = jsonResponse.getJSONArray("items");
                                            int conta2 = recetaitemsws2.length();
                                           
                                            boolean haying = false;
                                            boolean hayadic = false;
                                            for (int p = 0; p < conta2; p++) {
                                                JSONObject recetaitem2 = recetaitemsws2.getJSONObject(p);
                                                Map<String, Object> itemIngrediente2 = new HashMap<String, Object>();
                                                itemIngrediente2.put("cod_producto", recetaitem2.getString("cod_ingrediente"));
                                                itemIngrediente2.put("precio", recetaitem2.getString("precio"));
                                                itemIngrediente2.put("elegido", false);
                                                itemIngrediente2.put("requiere_cantidad", recetaitem2.getString("requiere_cantidad"));
                                                itemIngrediente2.put("imagen1", recetaitem2.getString("imagen1"));
                                                itemIngrediente2.put("nombre", recetaitem2.getString("nombre"));
                                                itemIngrediente2.put("cantidad", 0);
                                                itemIngrediente2.put("ingredienteDe", recetaitem.getString("cod_ingrediente"));
                                                itemIngrediente2.put("tipo", recetaitem2.getString("tipo"));
                                                itemIngrediente2.put("IDtipo", "");
                                                if(listaElegidos.contains(recetaitem2.getString("cod_ingrediente"))){
                                                	int idn = listaElegidos.indexOf(recetaitem2.getString("cod_ingrediente"));
                                                	itemIngrediente2.put("elegido", true);
        	                                        itemIngrediente2.put("cantidad", cantidadesI.get(idn));
        	                                        itemIngrediente2.put("IDtipo", subtipos.get(idn));
                                                }
                                                listaDeIngredientes.put(recetaitem2.getString("cod_ingrediente"), itemIngrediente2);
                                            }
                                        }
                                    }
                                    itemEnSesionRm.put("receta", listaDeIngredientes);
                                    ordenEnSesionRm.put(codURm, itemEnSesionRm);
                                } 
                            }
                        }
                        Map<String, Object> listaDeIngredientes = (Map<String, Object>) itemEnSesionRm.get("receta");
                        ordenesEnSesionRm.put(cod_pedido, ordenEnSesionRm);
                        session.setAttribute("ordenesEnSesion", ordenesEnSesionRm);
                        JSONObject respuestaJsonR = new JSONObject();
                        respuestaJsonR.put("ingredientes", listaDeIngredientes);
                        respuestaJsonR.put("ingredientesAntes", listaElegidos);
                        
                        PrintWriter outR = response.getWriter();
                        response.setContentType("text/html;charset=UTF-8");
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", respuestaJsonR.toString());
                        respuestaMap.put("r", "1");
                        JSONObject respuestaJsonO = new JSONObject(respuestaMap);
                        outR.print(respuestaJsonO.toString());
                    }
                    break;
                case 4614:
                	//CAMBIO DE TAMANIO OPCIONES
                	cod_pedido = request.getParameter("cod_pedido");
                    Map<String, Object> ordenesEnSesionCm = (HashMap<String, Object>) session.getAttribute("ordenesEnSesion");
                    Map<String, Object> ordenEnSesionCm = (HashMap<String, Object>) ordenesEnSesionCm.get(cod_pedido);
                    String codUCm = request.getParameter("codU");
                        
                    Map<String, Object> itemEnSesionCm = (HashMap<String, Object>) ordenEnSesionCm.get(codUCm);
                    ArrayList<String> ordenamientos = new ArrayList<String>();
                    ArrayList<String> grupos = new ArrayList<String>();
                    ArrayList<Integer> cantidades = new ArrayList<Integer>();
                    String tamano = "";
                    Map<String, Object> listamanosDelItem = (Map<String, Object>) itemEnSesionCm.get("tamano");
                    for (String codTam : listamanosDelItem.keySet()) {
                        HashMap<String, Object> tamanoDelIterado = (HashMap<String, Object>) listamanosDelItem.get(codTam);
                        if ((Boolean) tamanoDelIterado.get("elegido") == true) {
                            tamano = (String) tamanoDelIterado.get("tamano");
                            break;
                        }
                    }
                    
                    if (itemEnSesionCm != null) {
                        Map<String, Object> listaOpcsDelItem = (Map<String, Object>) itemEnSesionCm.get("opciones");
                        Map<String, Object> listaGOpcsDelItem = (Map<String, Object>) itemEnSesionCm.get("grupos");
                        for (String grupoOpciones : listaGOpcsDelItem.keySet()) {
                            HashMap<String, Object> gOpcionDelIterado = (HashMap<String, Object>) listaGOpcsDelItem.get(grupoOpciones);
                            int cantG = Integer.parseInt((String) gOpcionDelIterado.get("cantidad"));

                            for (String codOpcion : listaOpcsDelItem.keySet()) {
                                Map<String, Object> opcionDelItem = (Map<String, Object>) listaOpcsDelItem.get(codOpcion);
                                if((Boolean) opcionDelItem.get("elegido")){
                                String grupoDelItemIterado = (String) opcionDelItem.get("grupo_opciones");
                                int cantidadIt = (Integer) opcionDelItem.get("cantidad");
                                String ordenamientoIt = (String) opcionDelItem.get("ordenamiento");
                                ordenamientos.add(ordenamientoIt);
                                grupos.add(grupoDelItemIterado);
                                cantidades.add(cantidadIt);
                                }
                            }
                            JSONObject itemsOpciones;
                            
                            Map<String, Object> listaDeOpciones = new HashMap<String, Object>();
                            for (String grup : listaGOpcsDelItem.keySet()) {
                        		Map<String, Object> grupodeopciones = (HashMap<String, Object>) listaGOpcsDelItem.get(grup);
                        		webTarget = webTargetRoot.path("RecuperarOpciones");
                                form = new Form();
                                form.param("no_cia", nc);
                                form.param("app_destino", "CC");
                                form.param("grupo_opciones", grup);
                                form.param("tamano", tamano);
                                 int cantidadDelGrupo = Integer.parseInt((String) grupodeopciones.get("cantidad"));
                                 responseString = wsResponse(webTarget, form);
                                 jsonResponse = new JSONObject(responseString);

                                 if (jsonResponse != null) {
                                     String codigoRespuestaB = jsonResponse.getString("codigo");
                                     if (codigoRespuestaB.equals("0")) {
                                         if(grupos.contains(grup)){
                                    	 JSONArray opcionesws = jsonResponse.getJSONArray("items");

                                         int cnt = opcionesws.length();
                                         for (int j = 0; j < cnt; j++) {
                                             JSONObject opcionws = opcionesws.getJSONObject(j);
                                             if (opcionws.getString("disponible").equalsIgnoreCase("A")) {
                                                 Map<String, Object> itemOpcion = new HashMap<String, Object>();
                                                 itemOpcion.put("nombre", opcionws.getString("nombre"));

                                                 itemOpcion.put("ordenamiento", opcionws.getString("ordenamiento"));
                                                 itemOpcion.put("clase_opciones", (String)grupodeopciones.get("clase_opciones"));
                                                 itemOpcion.put("elegido", false);
                                                  itemOpcion.put("cantidad", 0);
                                                 for(int u = 0; u < ordenamientos.size(); u++){
                                                     String ordenamiento = ordenamientos.get(u);
                                                     int cantidad = cantidades.get(u);
                                                     String grupo = grupos.get(u);
                                                     if(opcionws.getString("ordenamiento").equalsIgnoreCase(ordenamiento) &&  
                                                    		 opcionws.getString("grupo_opciones").equalsIgnoreCase(grupo)){
                                                         itemOpcion.put("elegido", true);
                                                         itemOpcion.put("cantidad", cantidad);
                                                     }
                                                  }
                                                 
                                                 itemOpcion.put("imagen", opcionws.getString("imagen1"));
                                                 itemOpcion.put("precio", opcionws.getString("precio"));
                                                 itemOpcion.put("grupo_opciones", opcionws.getString("grupo_opciones"));
                                                 itemOpcion.put("cod_producto", opcionws.getString("cod_producto"));
                                                 listaDeOpciones.put(opcionws.getString("cod_producto"), itemOpcion);
                                             }
                                             
                                         }
                                     }else{
                                    	 JSONArray opcionesws = jsonResponse.getJSONArray("items");

                                         int cnt = opcionesws.length();
                                         boolean def = false;
                                         for (int j = 0; j < cnt; j++) {
                                             JSONObject opcionws = opcionesws.getJSONObject(j);
                                             if (opcionws.getString("disponible").equalsIgnoreCase("A")) {
                                                 Map<String, Object> itemOpcion = new HashMap<String, Object>();
                                                 itemOpcion.put("nombre", opcionws.getString("nombre"));

                                                 itemOpcion.put("ordenamiento", opcionws.getString("ordenamiento"));
                                                 itemOpcion.put("clase_opciones",(String)grupodeopciones.get("clase_opciones"));
                                                 if (opcionws.getString("valor_default").equals("S")) {
                                                     itemOpcion.put("elegido", true);
                                                     itemOpcion.put("cantidad", cantidadDelGrupo);
                                                     def = true;
                                                 } else {
                                                     itemOpcion.put("elegido", false);
                                                     itemOpcion.put("cantidad", 0);
                                                 }

                                                 itemOpcion.put("imagen", opcionws.getString("imagen1"));
                                                 itemOpcion.put("precio", opcionws.getString("precio"));
                                                 itemOpcion.put("grupo_opciones", opcionws.getString("grupo_opciones"));
                                                 itemOpcion.put("cod_producto", opcionws.getString("cod_producto"));
                                                 listaDeOpciones.put(opcionws.getString("cod_producto"), itemOpcion);
                                             }
                                         }
                                         if (!def) {
                                             for (String cod_opcion : listaDeOpciones.keySet()) {
                                                 Map<String, Object> opcionIt = (Map<String, Object>) listaDeOpciones.get(cod_opcion);
                                                 String suGrupo = (String) opcionIt.get("grupo_opciones");
                                                 String ord = (String) opcionIt.get("ordenamiento");
                                                 if (suGrupo.equalsIgnoreCase((String)grupodeopciones.get("grupo_opciones")) && ord.equalsIgnoreCase("1")) {
                                                     opcionIt.put("elegido", true);
                                                     listaDeOpciones.put(cod_opcion, opcionIt);
                                                     break;
                                                 }
                                             }
                                         }
                                     }
                                     
                                 }
                             }
                            }
                            itemEnSesionCm.put("opciones", listaDeOpciones);
                        }
                        ordenEnSesionCm.put(codUCm, itemEnSesionCm);
                        ordenesEnSesionCm.put(cod_pedido, ordenEnSesionCm);
                        session.setAttribute("ordenesEnSesion", ordenesEnSesionCm);
                        JSONObject miRespuestaJson = new JSONObject();
                        miRespuestaJson.put("grupos", (Map<String, Object>) itemEnSesionCm.get("grupos"));
                        Map<String, Object> mapaDeOpciones = (Map<String, Object>) itemEnSesionCm.get("opciones");
                        miRespuestaJson.put("opcionDatos", mapaDeOpciones);
                        miRespuestaJson.put("sesion", ordenesEnSesionCm);
                        PrintWriter outCm = response.getWriter();
                        Map<String, String> respuestaMap = new HashMap<String, String>();
                        respuestaMap.put("data", miRespuestaJson.toString());
                        respuestaMap.put("r", "1");
                        JSONObject respuestaJsonO = new JSONObject(respuestaMap);
                        outCm.print(respuestaJsonO.toString());
                    }
                
                	break;

                default:
                    request.setAttribute("error", codigo);
                    dispatcher = request.getRequestDispatcher("error.jsp");
                    dispatcher.forward(request, response);
                    break;
            }
        } else {
            request.setAttribute("error", "nulo");
            dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // TODO Auto-generated method stub
            processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(Loader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // TODO Auto-generated method stub
            processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(Loader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

