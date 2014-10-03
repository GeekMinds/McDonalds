<%@ page language="java" %>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">

<title>Gestionar Cliente</title>
<link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/gestionarCliente.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/fancySelectGestionarC.css"/>
        <script src="js/libs/jquery-1.11.0.min.js"></script>
        <script src="js/libs/fancySelect.js"></script>
        <script src="js/libs/jquery.simplemodal.js"></script>
        <script src="js/functions/gestionarCliente.js"></script>
</head>
<body>
   <div id="wrapper">
            <div id="content">
                <div id="header">
                    <br style="clear: both"/>
                    <h1>industria de hamburguesas S.A.</h1>
                </div>
                <div class="ladoIzquierdo">
                <form name="formCliente" id="formCliente">
                     <input type="hidden" id="clienteGes" name="clienteGes" value="" />
                     <div class="icono">
                        <img src="media/imagenes/general/iconoTelefono.png"/>
                    </div>
                    <div class="tituloInformacion">
                        <label>tel&eacute;fono:</label>
                        <input type="text" value="" name="telefonoGest" id="ingresoTelefonoGes" placeholder="Teléfono del Cliente"/>
                    </div>
                     <div class="icono">
                        <img src="media/imagenes/general/iconoCliente.png"/>
                    </div>
                    <div class="tituloInformacion">
                        <div class="innerInfoLeft">
                            <span>*</span><label>Nombres</label>
                            <span style="top: 51px;">*</span><label>Apellidos</label>
                        </div>
                        <div class="innerInfoRight">
                            <input type="text" name="nombres" placeholder="Nombres"/>
                            <input type="text" name="apellidos" placeholder="Apellidos"/>
                        </div>
                    </div>
                    <div class="icono">
                        <img src="media/imagenes/gestionarCliente/iconoMail.png"/>
                    </div>
                    <div class="tituloInformacion">
                         <div class="innerInfoLeft">
                            <label>Email</label>
                            <label>Fecha de nacimiento</label>
                        </div>
                        <div class="innerInfoRight">
                            <input type="email" name="email" placeholder="email"/>
                            <input type="date" value="" name="fechaNacimiento" />
                        </div>
                    </div>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/gestionarCliente/datosFacturacion.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>Datos de facturación </h1>
                        </div>
                    </div>
                    <div class="contenedorFacturacion">
                        <div class="innerInfoLeft">
                            <label>Nombre facturación</label>
                            <label>NIT</label>
                        </div> 
                        <div class="innerInfoRight">
                            <input type="text" name="nombreFacturacion" placeholder="Nombre Facturación"/>
                            <input type="text" name="nit" placeholder="NIT"/>
                        </div>
                    </div>
                    </form>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoRestaurante.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>Datos del restaurante </h1>
                        </div>
                    </div>
                    <div class="contenedorDatosRestaurante">
                        <select name="restaurante_sugerido" id="direccionRestaurante">
                            <option class="default" value="00">Sin Asignar</option>
                        </select>
                        <label class="tiempoEntrega"></label>
                        <label class="info">información asociada al restaurante</label>
                        <textarea id="infoAsociadaRestaurante" name="infoAsociadaRestaurante"></textarea>
                    </div>
                </div>
                <div class="ladoDerecho">
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoDireccion.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>Direcciones asociadas</h1>
                        </div>
                    </div>
                    
                    <div style=" position: relative; width: 100%; overflow: hidden; z-index: 2; " id="carouselHorizontal" class="carouselHorizontal">
                    	<div style="width: 200%; z-index: 1; overflow: hidden; margin-left: 0px;" class="carouselHorizontalElemento">
                    
	                    </div>
                    </div>
                    <div style="display:none" id="nuevaDireccion" class="direccionesAsociadas">
                    <form class="active" name="direccion" >
                    <input type="hidden" value="" name="asignado" />
                    <input type="hidden" name="telefonoCliente" value="" id="telefonoCliente" />
                         <div class="separadorDirecciones">
                            <label>tipo de dirección</label>
                            <select name="tipoDireccion" class="direccionesCliente" id="tipoDireccion">
                                <option value="C">Casa</option>
                                <option value="A">Empresa</option>
                                <option value="O">Otro</option>
                            </select>
                        </div>
                         <div class="separadorDirecciones">
                            <label>dirección</label>
                            <input type="text" placeholder="Dirección" name="direccion" id="direccionCliente" />
                        </div>
                        <div class="separadorDirecciones">
                            <label>departamento</label>
                            <select name="departamento" class="direccionesCliente" id="departamento">
                            	<option class="default" value="">Elegir Una</option>
                                <c:forEach var="deptoList" items="${listaDeptos}">
	                                <c:forEach var="depto" items="${deptoList}">
	                                    <c:if test="${depto.key=='nombre'}">
	                                        <c:set var="nombreDepto" value="${depto.value}"/>
	                                    </c:if>
	                                    <c:if test="${depto.key=='cod_depto'}">
	                                        <c:set var="codDepto" value="${depto.value}"/>
	                                    </c:if>
	                                </c:forEach>
	                                <option value="${codDepto}">${nombreDepto}</option>
	                            </c:forEach>
                            </select>
                        </div>
                        <div class="separadorDirecciones">
                            <label>municipio</label>
                            <select name="municipio" class="direccionesCliente" id="municipio">
                                <option class="default" value="">Elegir Una</option>
                            </select>
                        </div>
                        <div class="separadorDirecciones">
                            <label>zona</label>
                            <select name="zona" class="direccionesCliente" id="zona">
                                <option class="default" value="">Elegir Una</option>
                            </select>
                        </div>
                        <div class="separadorDirecciones">
                            <label>colonia</label>
                            <select name="colonia" class="direccionesCliente" id="colonia">
                              <option class="default" value="">Elegir Una</option>
                            </select>
                        </div>
                        <div class="separadorDirecciones">
                            <label>referencia</label>
                            <input type="text" placeholder="Referencia" name="referencia" id="referencia" />
                        </div>
                        </form>
                    </div>
                    <div class="paginadorDirecciones">
                        <a style="display:none" href="javascript:void(0)" id="prevDir">
                            <img src="media/imagenes/gestionarCliente/prev.png"/>
                        </a>
                        <span style="display:none;" class="primero"></span>
                       <span style="display:none;"  class="segundo"></span>
                        <a style="display:none" href="javascript:void(0)" id="nextDir">
                            <img src="media/imagenes/gestionarCliente/next.png"/>
                        </a>
                    </div>
                    <a href="javascript:void(0)" id="cancelar">
                        <img src="media/imagenes/gestionarCliente/cancelar.png"/>
                    </a>
                    <a href="javascript:void(0)" id="guardar">
                        <img src="media/imagenes/gestionarCliente/guardar.png"/>
                    </a>
                </div>
                <br style="clear: both"/>
            </div>
        </div>
</body>
</html>