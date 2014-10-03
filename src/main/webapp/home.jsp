<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/login.css"/>
        <link rel="stylesheet" href="css/general.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/home.css"/>
        <link rel="stylesheet" href="css/screenPop.css"/>
        <link rel="stylesheet" href="css/fancySelect.css"/>
        <script src="js/libs/jquery-1.11.0.min.js"></script>
        <script src="js/libs/jquery.simplemodal.js"></script>
        <script src="js/libs/fancySelect.js"></script>
        <script src="js/functions/loader.js"></script>
		<title>Order Taking System</title>
	</head>
<body>
<div id="wrapper">
            <div id="content">
                <br style="clear: both"/>
	<div class="contenedorGeneral">
                    <br style="clear: both"/>
                    <div class="header">
                        <img src="media/imagenes/home/iconoHeader.png" alt="header"/>
                        <h1>bienvenido al sistema ots</h1>
                        <h2>Bienvenido, <%= session.getAttribute( "fullName" ) %></h2>
                    </div>
                    <a id="cerrarSesion" href="LogOut">
                    	Cerrar Sesión
                    	<span>
                    		
                    	</span>
                    </a>
     </div>
                <div class="contenedorMenu">
                    <div class="menu">
                        <br style="clear: both"/>
                        <ul>
                            <li>
                                <a id="buscarOrdenes" href="javascript:void(0)">
                                    <img src="media/imagenes/home/iconoBuscarOrdenes.png" alt="buscar ordenes"/>
                                    <span>buscar ordenes</span>
                                </a>
                            </li>
                            <li style="display:none">
                                <a id="gestionarClientes" href="javascript:void(0)">
                                    <img src="media/imagenes/home/iconoGestionarClientes.png" alt="gestionar clientes"/>
                                    <span>gestionar clientes</span>
                                </a>
                            </li>
                            <li>
                                <a id="lanzarScreenPoP" href="ScreenPop" target="_blank">
                                    <img  src="media/imagenes/home/iconoScreenPop.png" alt="screen pop"/>
                                    <span>lanzar screen pop</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <br style="clear: both"/>
                </div>
                </div>
                </div>
</body>
</html>