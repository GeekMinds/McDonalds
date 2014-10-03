<%@ page language="java" %>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
	<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Order Taking System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/login.css"/>
        <link rel="stylesheet" href="css/general.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/home.css"/>
        <link rel="stylesheet" href="css/screenPop.css"/>
        <link rel="stylesheet" href="css/easydropdown.flat.css"/>
        <script src="js/libs/jquery-1.11.0.min.js"></script>
        <script src="js/libs/jquery.easydropdown.js"></script>
        <script src="js/libs/jquery.simplemodal.js"></script>
        <script src="js/functions/loader.js"></script>
        
    </head>
    <body>
        <div id="wrapper">
            <!--<img src="media/imagenes/general/loader.gif" class="loader" height="100" width="100" />-->
           		<div id="content">
                	<br style="clear: both"/>
	                <div class="contenedorGeneral">
	                     <div class="header">
	                        <img src="media/imagenes/home/iconoHeader.png" alt="header"/>
	                        <h1>bienvenido al sistema ots</h1>
	                    </div>
	                    <form id="login" method="post">
	                        <select name="compania" class="tipoEmpresa dropdown">
	                            <c:forEach var="ciaList" items="${listaCompanias}">
	                                <c:forEach var="cia" items="${ciaList}">
	                                    <c:if test="${cia.key=='nombre'}">
	                                        <c:set var="nombreCompania" value="${cia.value}"/>
	                                    </c:if>
	                                    <c:if test="${cia.key=='cod_compania'}">
	                                        <c:set var="codCompania" value="${cia.value}"/>
	                                    </c:if>
	                                </c:forEach>
	                                <option value="${codCompania}">${nombreCompania}</option>
	                            </c:forEach>
	                        </select>
	                        <div class="izquierdo">
	                            <label>Código colaborador</label>
	                            <label>Contraseña</label>
	                        </div>
	                        <div class="derecho">
	                            <input type="text" name="user"/>
	                            <input type="password" name="pass"/>
	                        </div>
	                        <label class="mensajeError" style="color: red;position: relative;top: 15px;"></label>
	                        <input type="submit" name="aceptar" value="aceptar"/>
	                    </form>
	                </div>
	                <!--<br style="clear: both"/>-->
					
	        </div>
		</div>
    </body>
</html>