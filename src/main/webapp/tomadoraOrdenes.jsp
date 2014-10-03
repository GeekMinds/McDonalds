<%@ page language="java" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
    <head>
       <title>OTS</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/basic.css"/>
        <link rel="stylesheet" href="css/easydropdown.flat.css"/>
        <link rel="stylesheet" href="css/tomadoraOrdenes.css"/>
        <link rel="stylesheet" href="css/fonts.css"/>
        <link rel="stylesheet" href="css/jquery.datetimepicker.css"/>
        <link rel="stylesheet" href="css/fancyTomadora.css"/>
        <script src="js/libs/jquery-1.11.0.min.js"></script>
        <script src="js/libs/fancySelect.js"></script>
        <script src='js/libs/jquery.customSelect.min.js'></script>
        <script src="js/libs/jquery.simplemodal.js"></script>
        <script src="js/libs/jquery.datetimepicker.js"></script>
        <script src="js/functions/tomadoraOrdenes.js"></script>
        <style>
            .fechaAnt, .pagoTarjeta, #pagoMixto{
                display:none;
            }
        </style>
    </head>
    <body>
        <div id="wrapper">
            <div id="content">
                <div id="header">
                    <br style="clear: both"/>
                    <h1>industria de hamburguesas S.A.</h1>
                    <span><label>Bienvenido, </label>Agente  <%= session.getAttribute( "fullName" ) %></span>
                    <div>
                        <c:if test="${horario=='DESAYUNO'}">
                        <a class="tipoProducto iconoDesayuno desayunoOn on" href="javascript:void(0)" data-tipo="DESAYUNO"></a>
                        <a class="tipoProducto iconoAlmuerzo almuerzoInactivo" href="javascript:void(0)" data-tipo="ALMUERZO"></a>
                          </c:if>   
                        
                        <c:if test="${horario=='ALMUERZO'}">
                            <a class="tipoProducto iconoDesayuno desayunoInactivo" href="javascript:void(0)" data-tipo="DESAYUNO"></a>
                        <a class="tipoProducto iconoAlmuerzo almuerzoOn on" href="javascript:void(0)" data-tipo="ALMUERZO"></a>
                        
                          </c:if>   
                    </div>
                </div>
                <div class="ladoIzquierdo">
                    <div class="contenedorDatosOrdenes">
                        <h1>datos de la orden</h1>
                        <div class="datosOrdenes">
                            <div class="innerLeft">
                                <span><label class="identificador"># orden</label><label class="descrip"></label></span>
                                <span><label class="identificador">cliente</label><label class="descrip" id="nomCliente"></label></span>
                                <span><label class="identificador">dirección</label><label id="dirCliente" class="descrip"></label></span>
                                <span><label class="identificador">restaurante</label><label id="restaurante" class="descrip"></label></span>
                                <span id="tipoC"></span>
                            </div>
                            <div class="innerRight">
                                <span><label class="identificador">asociada</label><label class="descrip">N/A</label></span>
                                <span style="border-bottom: 1px white solid;"><label class="identificador">teléfono</label><label id="telCliente" class="descrip"></label></span>
                                <div style="position: relative;">
                                    <img class="reloj" src="media/imagenes/tomadoraOrdenes/reloj.png"/>
                                    <label class="minutos"></label>
                                </div>
                            </div>
                            <br style="clear: both"/>
                        </div>
                    </div>
                    <div id="contenedorOrdenes" class="contenedorTablaOrden">
                        <h1 class="tituloDetalle">datos de la orden</h1>
                      
                    </div>
                    <div class="pagoTotalOrden">
                        <label class="total">total de la orden</label>
                        <label class="pago" id="totalDeTodo"></label>
                    </div>
                </div>
                <div class="ladoDerecho">
                    <div class="contenedorSwitch">
                        <label class="txOn">
                            A la Carta
                        </label>
                        <label class="txOff">
                            Menu
                        </label>
                        <span>
                            <a href="javascript:void(0)" class="off" id="switch">
                                <img src="media/imagenes/tomadoraOrdenes/circuloSwitch.png"/>
                            </a>
                        </span>
                        <a href="javascript:void(0)" id="siguienteMenu" class="siguienteMenu">
                        	<img src="media/imagenes/general/continuarMenu.png"/>
                        </a>
                         <a href="javascript:void(0)" id="regresarMenu" class="regresarMenu">
                        	<img src="media/imagenes/general/regresar.png"/>
                        </a>
                    </div>
                    <div id="contenedorIconos" class="contenedorIconos">
                        
                    </div>
                    <div  class="nav">
                        <ul id="clases">
                        </ul>
                    </div>
                    <div class="contenedorProductos">
                        
                    </div>
                    <div class="paginadorProductos">

                    </div>
                    <div class="contenedorObsevaciones">
                        <h1>Observaciones entrega</h1>
                        <textarea rows="" cols="" placeholder="Observaciones" id="obsEntega" name="obsEntega"></textarea>
                    </div>
                    <a href="javascript:void(0)" class="cancelar">
                        <img src="media/imagenes/gestionarCliente/cancelar.png"/>
                    </a>
                    <a href="javascript:void(0)" class="next" id='next'>
                        <img src="media/imagenes/tomadoraOrdenes/next.png"/>
                    </a>
                    <div class="overlay">

                    </div>
                    <!--MODAL FORMA DE PAGO-->
                    <div id="modalFormaPago">
                        <div class="headerModal">
                            <h1>Confirmar orden</h1>
                            <span></span>
                        </div>
                        <form id="enviarOrden">
                            <input type="hidden" name="cod_pedido" value="${cod_pedido}" />
                            <input type="hidden" name="totaltodo" value="0" />
                            
                        <div class="contenedorModal">
                            <div class="confirmar">
                                <input value="1" type="checkbox" name="confirmar" id="confirmarOrden" class="css-checkbox">
                                <label for="confirmarOrden" style="margin-top: -30px;" class="css-label txtConfirmar">confirmar orden</label>
                                <textarea name="confirmar_texto" placeholder="Confirmar Orden" id="txAreaConfirmar"></textarea>
                            </div>
                            
                            <div class="observaciones">
                                <h1>Observaciones Internas</h1>
                                <textarea name="observaciones_orden" placeholder="Observaciones" id="observacionInternaTxt"></textarea>
                                <br style="clear: both"/>
                            </div>
                            <div class="observaciones">
                                <h1>Observaciones de entrega</h1>
                                <textarea name="observaciones_entrega" placeholder="Observaciones" id="observacionEntregaTxt"></textarea>
                                <br style="clear: both"/>
                            </div>
                            <div class="tituloModal">
                                <h1>forma de pago</h1>
                            </div>
                            <div class="contenedorFormasPago">
                                <div class="clr">
                                    <input checked type="radio" value="EF" name="tipo_pago" id="cEfectivo" class="css-checkbox">
                                    <label for="cEfectivo" class="css-label txtPagos" style="top: 13px; position: absolute; left: 0px;"> efectivo</label>
                                    <input readonly type="text" class="pagoEfectivo" name="monto_efectivo" id="pagoEfectivo" style="position: absolute; right: 344px;"/>
                                    <input type="text" class="pagoTarjeta" name="no_tarjeta" id="pagoTCredito" style="float: right; margin-right: 29px;"/>        
                                    <label class="pagoTarjeta txtPagos" style="float: right; margin-top: 13px;"> tarjeta Crédito</label>
                                </div>
                                <div class="clr">
                                    <input type="radio" name="tipo_pago"  value="TA" id="cTarjeta" class="css-checkbox">
                                    <label for="cTarjeta" class="css-label txtPagos" style="position: absolute; top: 12px;">tarjeta crédito</label>
                                    <input class="pagoTarjeta" type="text" name="monto_tarjeta" id="pagoTarjeta" style="top: -15px; position: relative; margin-left: 200px"/>
                                    <label class="pagoTarjeta txtPagos" style="position: relative; top: -15px;">Fecha Expiración</label>
                                    <select name="mes_vence" class="pagoTarjeta" id="mexExp" class="fechaExpiracion">
                                        <option>01</option>
                                        <option>02</option>
                                        <option>03</option>
                                        <option>04</option>
                                        <option>05</option>
                                        <option>06</option>
                                        <option>07</option>
                                        <option>08</option>
                                        <option>09</option>
                                        <option>10</option>
                                        <option>11</option>
                                        <option>12</option>
                                    </select>
                                    <select name="anio_vence" class="pagoTarjeta" id="anioExp" class="fechaExpiracion">
                                        
                                    </select>
                                </div>
                                <div class="clr">
                                    <input type="radio" value="MI" name="tipo_pago" id="cMixto" class="css-checkbox">
                                    <label for="cMixto" class="css-label txtPagos" style="position: relative; top: 10px;">cobro mixto</label>
                                    <input type="text" name="pagoMixto" id="pagoMixto"/>
                                </div>
                                <div class="tituloModal">
                                    <h1>fecha entrega</h1>
                                </div>
                                <div class="clr">
                                    <input checked value="N" type="radio" name="tipoOrden" id="cAhora" class="css-checkbox">
                                    <label for="cAhora" class="css-label txtPagos"> Ahora</label>
                                    <label class="fechaAnt txtPagos" style="width: 40px;"> fecha</label>
                                    <input class="fechaAnt" type="text" name="fechaInAdvance" id="fechaEnvio" style="width: 130px;"/>
                                    <label class="fechaAnt txtPagos" style="width: 120px;"> ordenes asignadas</label>
                                    <span class="fechaAnt cuadroBlanco">
                                        
                                    </span>
                                </div>
                                <div class="clr">
                                    <input value="A" type="radio" name="tipoOrden" id="cHora" class="css-checkbox">
                                    <label for="cHora" class="css-label txtPagos" style="width: 150px;"> Anticipado</label>
                                    <label class="fechaAnt txtPagos" style="width: 40px;"> Hora</label>
                                    <input class="fechaAnt" type="text" name="horaInAdvance" id="horaEnvio" style="width: 129px;"/>       
                                </div>
                                <br style="clear: both;"/>
                            </div>
                            <div class="contenedorBotones">
                                <a href="javascript:void(0)" id="regresarBtn" class="btnModalPagos">
                                    regresar
                                </a>
                                <a href="javascript:void(0)" class="btnModalPagos">
                                    asociar
                                </a>
                                <a data-enviar="0" href="javascript:void(0)" class="btnModalPagos enviarOrdenBtn">
                                    grabar
                                </a>
                                <a data-enviar="1" href="javascript:void(0)" class="btnModalPagos enviarOrdenBtn">
                                    enviar
                                </a>
                            </div>
                        </div>
                        </form>
                        <div id="mensajeGrabado">
                        	<h1>La orden fue grabada con exito</h1>
                        	<h2></h2>
                        	<a class="mensajeGrabado" href="/mcd">
                        		Aceptar
                        	</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
