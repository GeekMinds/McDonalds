<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/screenPop.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/chosen.css"/>
        <link rel="stylesheet" href="css/fancySelect.css"/>
        <link rel="stylesheet" href="css/fancySelectGestionarC.css"/>
        <link rel="stylesheet" href="css/fancySelect.css"/>
        <link rel="stylesheet" href="css/fancySelectTipologia.css"/>
        
        <script src="js/libs/jquery-1.11.0.min.js"></script>
		<script src='js/libs/jquery.customSelect.min.js'></script>       
		<script src="js/functions/screenPop.js"></script>        
        <script src="js/libs/jquery.simplemodal.js"></script>
        <script src="js/libs/fancySelect.js"></script>
		<title>Order Taking System</title>
	</head>
	<body>
		<div id="wrapper">
            <div id="content" class="ScreenPop">
                <div id="header">
                    <br style="clear: both"/>
                    <h1>industria de hamburguesas S.A.</h1>
                    <span><label>Bienvenido, </label>Agente  <%= session.getAttribute( "fullName" ) %></span>
                </div>
                <div class="ladoIzquierdo">
                    <span class="icono">
                        <img src="media/imagenes/general/iconoTelefono.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>teléfono</label>
                    </span>
                    	<span class="textoDescripcion">
	                    	<input type="text" id="PhoneNumer" name="telefono" style="border: none;font-family: 'Helvetica';font-size: 14px;height: 31px;margin-top: 25px;padding-left: 15px;width: 220px;" >
                    	</span>
                    <span class="icono">
                        <img src="media/imagenes/general/iconoCliente.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>Cliente</label>
                    </span>
                    <span class="textoDescripcion">
                        <select  id="listaClientes" class="dropdown chosen-select" data-placeholder="Buscar Cliente..."style="width:350px;" tabindex="2">

                        </select>
                    </span>
                    <div class="contenedorAcciones">
                        <a href="javascript:void(0)" class="crear accion" id="crearCliente">
                            Crear cliente
                        </a>
                        <a  href="javascript:void(0)" class="editar accion" id="crearClienteEdit">
                            Editar cliente
                        </a>
                    </div>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoDireccion.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>Direcciones asociadas</h1>
                        </div>
                    </div>
                    <div class="contenedorDirecciones">
                        
                    </div>
                    <div class="contenedorAcciones">
                        <a href="javascript:void(0)" id="dirClienteAdd" class="crear accion">
                            Crear Dirección
                        </a>
                        <a href="javascript:void(0)" id="dirClienteEdit" class="editar accion">
                            Editar Dirección
                        </a>
                    </div>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoRestaurante.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>información asociada el restaurante</h1>
                        </div>
                    </div>
                    <div class="contenedorInfoRestaurante">
                        <textarea placeholder="Observaciones asociadas al restaurante"></textarea>
                        <div class="r1">
                            <span class="restaurante">
                                Restaurante
                            </span>
                            <span class="lugarRestaurante">
                                Primma
                            </span>
                            <span class="tiempoEntrega">
                                Tiempo de entrega
                            </span>
                            <span class="estimado">
                                30 min.
                            </span>
                        </div>
                    </div>
                    <div class="tipologiaLlamada">
                        <a href="javascript:void(0)">
                            tipología de llamada
                        </a>
                    </div>
                </div>
                <div class="ladoDerecho">
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoRestaurante.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>información asociada al cliente</h1>
                        </div>
                    </div>
                    <div class="clasificacionCliente">
                        <div>
                            <label class="tipoCliente">tipo cliente</label>
                            <label class="cliente"><span id="typeC"></span></label>
                        </div>
                    </div>
                    <div class="clienteV">
                        <label id="obs"></label>
                    </div>
                    <div class="seccionOrden">
                        <a href="javascript:void(0)" class="ultimaOrden">
                            <span class="iconoOrden">
                                <img src="media/imagenes/general/iconoUltimaOrden.png"/>
                            </span>
                            <span class="texto">
                                &uacute;ltima orden
                            </span>
                        </a>
                        <a href="javascript:void(0)" class=" fav activo">
                            <span class="iconoOrden">
                                <img src="media/imagenes/general/iconoFavoritos.png"/>
                            </span>
                            <span class="texto">
                                favoritos
                            </span>
                        </a>
                    </div>
                    <div class="contenedorOrdenes">
                        <label class="fechaHora">Fecha: <span id="fechaHora"></span></label>
                        <div class='newData'>
                        
                        </div>
                    </div>
                    <div class="contenedorFavoritos" style="display: none">
                        
                    </div>
                    <div class="contenedorTotalOrden">
                        <label class="totalOrden"></label>
                    </div>
                    <div class="contenedorAcciones">
                        <a href="javascript:void(0)" class="seleccionar accion">
                            seleccionar esta orden
                        </a>
                        <a href="javascript:void(0)" class="nueva accion" id="nuevaOrden">
                            nueva orden
                        </a>
                        <a href="javascript:void(0)" class="buscar accion">
                            buscar orden
                        </a>
                    </div>
                </div>
                <br style="clear: both"/>
                <div id="modalTipologia">
		            <div class="headerModal">
		                <h1>Tipología</h1>
		                <span></span>
		            </div>
		            <div class="contenedorInstrucciones">
		                <label class="instrucciones">Seleccione el tipo de llamada</label>
		                <select id="selectTipologia">
		
		                </select>
		            </div>
		            <br style="clear: both"/>
		            <textarea class="datosAdicionales" placeholder="Datos adicionales de la llamada."></textarea>
		            <a href="javascript:void(0)" class="btnAceptar">
		                aceptar
		            </a>
		            <div id="modalBuscarOrden" style="overflow: scroll;">
			            <div class="headerModal">
			                <h1>Buscar orden</h1>
			                <span></span>
			            </div>
			            
			            <div class="contenedorOrdenModal">
			                <div>
			                    <label>teléfono</label>
			                    <input type="text" name="telefonoCliente" id="telefonoCliente"/>
			                </div>
			                <div>
			                    <label>cliente</label>
			                    <input type="text" name="nombreCliente" id="nombreCliente" style="width: 181px;"/>
			                </div>
			                <div>
			                    <label>#rest.</label>
			                    <input type="text" name="numeroRestaurante" id="numeroRestaurante" style="width: 87px;"/>
			                </div>
			                <div>
			                    <label>fecha</label>
			                    <input type="date" name="fechaOrden" id="fechaOrden" />
			                </div>
			                <div>
			                    <label>orden</label>
			                    <input type="text" name="numeroOrden" id="numeroOrden" style="width: 127px;"/>
			                </div>
			                <div>
			                    <label>origen</label>
			                    <select id="origenSP"> 
			                        <option>Web</option>
			                        <option>App</option>
			                    </select>
			                </div>
			                 <a class="accionModalSearch" href="javascript:void(0)">
			                    <img src="media/imagenes/general/buscarOrdenModal.png"/>
			                </a>
			            </div>
			            <table>
			                <tbody id="OrderList" >
			                    
			                </tbody>
			            </table>
			        </div>
		        </div>
       		</div>
			
  
  <script src="js/libs/chosen.jquery.js" type="text/javascript"></script>
  <script src="js/libs/prism.js" type="text/javascript" charset="utf-8"></script>

	<script type="text/javascript">
	    var config = {
	      '.chosen-select'           : {},
	      '.chosen-select-deselect'  : {allow_single_deselect:true},
	      '.chosen-select-no-single' : {disable_search_threshold:10},
	      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
	      '.chosen-select-width'     : {width:"95%"}
	    }
	    for (var selector in config) {
	      $(selector).chosen(config[selector]);
	    }
	    $('.chosen-select').trigger('chosen:updated');
	</script>

        <div id='modalCrearCliente'>
            <div class="headerModal">
                <h1>Crear Cliente</h1>
                <span></span>
            </div>
            <form id="formCliente" method="post" action="">
                <div class="separador">
                	<input type="hidden" id="clienteGes" name="clienteGes" value="" />
                    <label>Teléfono</label>
                    <input type="text"  name="telefonoGest" id="ingresoTelefonoGes" placeholder="Teléfono del Cliente" />
                    <label>Nombre</label>
                    <input type="text" name="nombres" placeholder="Nombres" />
                    <label>Apellidos</label>
                    <input type="text" name="apellidos" placeholder="Apellidos" />
                </div>
                <div class="separador">
                    <label>Email</label>
                    <input type="text"  type="email" name="email" placeholder="email" />
                </div>
                <div class="separador">
                    <label>Fecha Nacimiento</label>
                    <input type="date"name="fechaNacimiento"  id='fechaNacimiento' />
                </div>
               	<div class="contenedorFacturacion" style="border-bottom: 1px #ccc solid; display: block; padding: 10px 20px 10px 20px;">			
	                <div class="innerInfoLeft">
	                    <label style="color: #1e1e1e;display: block;font-family: 'Helvetica';font-size: 15px;font-weight: normal;margin-top: 10px;padding-bottom: 5px;text-transform: uppercase;">Nombre facturación</label>
	                    <input type="text" name="nombreFacturacion" placeholder="Nombre Facturación" style="background: #fff; border: none; display: block; font-family: 'Helvetica'; font-size: 13px; font-weight: bold; height: 40px; margin-bottom: 10px; width: 100%;"/>
	                    <label  style="color: #1e1e1e;display: block;font-family: 'Helvetica';font-size: 15px;font-weight: normal;margin-top: 10px;padding-bottom: 5px;text-transform: uppercase;"> NIT</label>
	                    <input type="text" name="nit" placeholder="NIT" style="background: #fff; border: none; display: block; font-family: 'Helvetica'; font-size: 13px; font-weight: bold; height: 40px; margin-bottom: 10px; width: 100%;"/>
	                </div>
               	</div>
                <a id="guardarCliente" href="javascript:void(0)">
                    Guardar
                </a>
            </form>
        </div>
</body>
</html>

