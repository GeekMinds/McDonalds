<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order Taking System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/detalleOrden.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/chosen.css"/>
        
        <script src="js/libs/jquery-1.11.0.min.js"></script>
        <script src="js/libs/jquery.easydropdown.js"></script>
        <script src='js/libs/jquery.customSelect.min.js'></script>
        <script src="js/libs/jquery.simplemodal.js"></script>
       	<script src="js/functions/orderSearch.js"></script>
       	<script>
            var modalSearch = true;
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="content">
                <div id="header">
                    <br style="clear: both"/>
                    <h1>industria de hamburguesas S.A.</h1>
                    <span><label>Bienvenido, </label><%= session.getAttribute( "fullName" ) %></span>
                </div>
                <div class="ladoIzquierdo">
                    <span class="icono">
                        <img style="margin-top: 25px;" src="media/imagenes/general/iconoLista.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>Orden</label>
                    </span>
                    <span class="textoDescripcion">
                        <label id="noOrdern"></label>
                    </span>
                    <span class="icono">
                        <img  src="media/imagenes/general/iconoTelefono.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>teléfono</label>
                    </span>
                    <span class="textoDescripcion">
                        <label id="ciTel"></label>
                    </span>
                    <span class="icono">
                        <img style="margin-top: 25px;" src="media/imagenes/general/iconoPerfil.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>cliente</label>
                    </span>
                    <span class="textoDescripcion">
                        <label id="ciNombre"></label>
                    </span>
                    <span class="icono">
                        <img style="margin-top: 25px;" src="media/imagenes/general/iconoReloj.png"/>
                    </span>
                    <span class="tituloInformacion">
                        <label>tiempo de orden</label>
                    </span>
                    <span class="textoDescripcion">
                        <label id="ciTiempo"></label>
                    </span>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/iconoDireccion.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>Dirección registrada</h1>
                        </div>
                    </div>
                    <div class="contenedorDireccionesRegistradas">
                        <label class="direccion"></label>
                        <div class="subContenedor">
                            <label class="indicador">Restaurante</label>
                            <label class="descripcion" id="restName"></label>
                        </div>
                        <div class="subContenedor">
                            <label class="indicador">Tiempo de entrega</label>
                            <label class="descripcion" id="ciTiempoRest"></label>
                        </div>
                        <br style="clear:  both"/>
                        <div class="tituloDirecciones">
                            <h1>información asociada al restaurante</h1>
                        </div>
                        <textarea placeholder="Información asociada al restaurante" id="restinfo" disabled="disabled"></textarea>
                        <!--opciones 1-->
                        <input type="checkbox" name="opGrabada" id="opGrabada" class="css-checkbox" disabled="disabled">
                        <label for="opGrabada" class="css-label txtOpciones">grabada</label>
                         <input type="checkbox" name="opEnviada" id="opEnviada" class="css-checkbox" disabled="disabled">
                        <label for="opEnviada" class="css-label txtOpciones">enviada Rest.</label>
                         <input type="checkbox" name="opAnulada" id="opAnulada" class="css-checkbox" disabled="disabled">
                        <label for="opAnulada" class="css-label txtOpciones">anulada</label>
                        <!--opciones 2-->
                        <input type="checkbox" name="opProduccion" id="opProduccion" class="css-checkbox" disabled="disabled">
                        <label for="opProduccion" class="css-label txtOpciones">producción</label>
                         <input type="checkbox" name="opEnviada2" id="opEnviada2" class="css-checkbox" disabled="disabled">
                        <label for="opEnviada2" class="css-label txtOpciones">enviada Cliente</label>
                         <input type="checkbox" name="opEntregada" id="opEntregada" class="css-checkbox" disabled="disabled">
                        <label for="opEntregada" class="css-label txtOpciones">entregada</label>
                        <div class="tituloDirecciones" style="margin-top: 0px;">
                            <h1>ordenes asociadas</h1>
                        </div>
                        <label class="ordenAsociada"></label>
                    </div>
                    <div class="agregarOrden">
                        <a href="javascript:void(0)" id="agregarOrden">
                            agregar orden
                        </a>
                    </div>
                </div>
                <div class="ladoDerecho">
                    <div class="contenedorTitulos">
                        <h1>
                            orden ingresada por
                        </h1>
                    </div>
                    <div class="contBlanco">
                        <label></label>
                    </div>
                    <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/icono2.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>información asociada al cliente</h1>
                        </div>
                    </div>
                    <div class="descripcionTipoClienteTitulo">
                        <span>tipo cliente</span>
                        <label id="typeC"></label>
                    </div>
                    <div class="descripcionTipoCliente">
                        <label id="obs"></label>
                    </div>
                    <br style="clear: both"/>
                    <div class="contenedorObservaciones">
                        <div class="separadorContenedor">
                            <div class="gris">
                                <input type="checkbox" name="confirmarOrden" id="confirmarOrden" class="css-checkbox" disabled="disabled">
                                <label for="confirmarOrden" class="css-label txtConfirmaciones">confirmar orden</label>
                            </div>
                            <div class="blanco">
                                <input type="text" name="confirmarO" id="confirmarO" disabled="disabled"/>
                            </div>
                        </div>
                         <div class="separadorContenedor">
                            <div class="gris">
                                <label class="txtConfirmaciones">observaciones internas</label>
                            </div>
                            <div class="blanco">
                                <input type="text" name="observacionInterna" id="observacionInterna" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="separadorContenedor">
                            <div class="gris">
                                <label class="txtConfirmaciones">observaciones entrega</label>
                            </div>
                            <div class="blanco">
                                <input type="text" name="observacionEntrega" id="observacionEntrega" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                     <div class="headerSeccion">
                        <div class="iconoHeader">
                            <img src="media/imagenes/general/icnoLista2.png"/>
                        </div>
                        <div class="textoHeader">
                            <h1>detalle de orden</h1>
                        </div>
                    </div>
                    <div class="contenedorOrdenes" style="">
                        <label class="fechaHora"></label>
                 	</div>
                    <div class="contenedorTotalOrden">
                        <label class="totalOrden"></label>
                    </div>
                    <div class="contenedorAcciones">
                        <a href="javascript:void(0)" class="nueva accion">
                            nueva orden
                        </a>
                        <a href="javascript:void(0)" class="editar accion">
                            editar orden
                        </a>
                        <a href="javascript:void(0)" class="enviar accion" id="SendThisOrder">
                            enviar orden
                        </a>
                    </div>
                </div>
            </div>
            <script src="js/libs/chosen.jquery.js" type="text/javascript"></script>
  			<script src="js/libs/prism.js" type="text/javascript" charset="utf-8"></script>
			<div id="modalOrderSearch">
                       <div class="headerModal">
                        <h1>Tipología</h1>
                        <span></span>
                    </div>
                <span class="icono">
                    <img src="media/imagenes/general/iconoTelefono.png">
                </span>
                <span class="tituloInformacion">
                    <label>teléfono</label>
                </span>
                <span class="textoDescripcion">
                    <input type="text" id="PhoneNumer" name="telefono" style="border: none;font-family: 'Helvetica';font-size: 14px;height: 31px;margin-top: 25px;padding-left: 5px;width: 230px;" disabled="disabled">
                </span>
                <span class="icono">
                    <img src="media/imagenes/general/iconoCliente.png">
                </span>
                <span class="tituloInformacion">
                    <label>Cliente</label>
                </span>
                <span class="textoDescripcion">
                    <select  id="listaClientes" class="dropdown chosen-select" data-placeholder="Buscar Cliente..."style="width:250px;" tabindex="2">

                    </select>
                </span>
                <a href="javascript:void(0)" id="enviarSearch">
                    Enviar
                </a>
            </div>
        </div>
    </body>
</html>
