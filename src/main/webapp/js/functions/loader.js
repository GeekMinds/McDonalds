/**
 * 
 */
jQuery(document).ready(function() {

	$('#content').on('submit','#login', function(e) {
	    e.preventDefault();
	    $('#content').children().fadeOut('slow');
	    var cod = '1923';
	    $.ajax({
	        type: "POST",
	        url: 'Loader',
	        data: $("#login").serialize() + '&cod=' + cod,
	        success: function(r)
	        {
	            if(r.r === "0"){
	            	window.location.href = "Home";     
	            }else{
	                //$('.mensajeError').text(response.data);
	                $('.mensajeError').text(r.data);
	                $('#content').children().fadeIn('slow');
	            }
	        }
	    });
	    return false;
	});

	
	$('#content').on('click' , '.ladoDerecho .seccionOrden a', function(){
	    $('.activo').removeClass('activo');
	   if($(this).hasClass('activo')){
	       $(this).removeClass('activo');
	   }else{
	       $(this).addClass('activo');
	       if($(this).hasClass('fav')){
	           $('.contenedorOrdenes').hide("fast");
	           $('.contenedorFavoritos').show("fast");
	       }else{
	           $('.contenedorFavoritos').hide("fast");
	           $('.contenedorOrdenes').show("fast");
	       }
	   }
	});

	var params = {};
	params.telefono = '';
	var getParams = window.location.search.substr(1);
	var x = getParams.split("&");
	for ( var i = 0; i < x.length; i++) {
	    var tmparr = x[i].split("=");
	    params.telefono = tmparr[1];
	}
	$('.lugarRestaurante').text("");
	$('.estimado').text("");
	$(".totalOrden").text("");
	$('#fechaHora').text("");
	$(".totalOrden").text("");
	function limpiar(){
		$('.lugarRestaurante').text("");
		$('.estimado').text("");
		$(".totalOrden").text("");
		$('#fechaHora').text("");
		$(".totalOrden").text("");
	}

	$("#content").on('click', '.contenedorAcciones .accion.buscar', function(){
	    $("#modalBuscarOrden").modal();
	});
	
	$('#content').on('change', '#listaClientes', function(){
			//alert($(this).val());
			ic = $(this).val();
			ClientDirecction(ic);
			$('.lugarRestaurante').text("");
			$('.estimado').text("");
			$(".contenedorInfoRestaurante").find("textarea").html("");
	});
	
	$('body').on('click','#direccion',function(){
		ra = $(this).data('id');
		opCod = "1992";
		$.ajax({
			url:"Loader",
			data: "ra="+ra+"&cod="+opCod,
			async:false,
			dataType:"json",
			success: function (r){
				//r = JSON.parse(r);
				if(r.r === "1"){
					$('.lugarRestaurante').text(r.nombre);
					$('.estimado').text(r.tiempo);
					h = "Observaciones de Promocion: " + r.observaciones_promocion + "\n";
					s = "Observaciones de Producto: " + r.observaciones_producto + "\n";
					t = "Observaciones de Servicio: " + r.observaciones_servicio + "\n";
					$(".contenedorInfoRestaurante").find("textarea").html(h+s+t);
				}
			},
			error: function (a,b,c){
				alert(a.responseText);
			}
		});
	});

	
	$("#content").on('click', '.tipologiaLlamada', function(){
		opCod = 1960;
		$.ajax({
			url:"Loader",
			type:"POST",
			data: 'cod=' + opCod,
			async: false,
			dataType:"json",
			success: function(r){
				if(r.t == "1"){
					var xHtml = '';
					var t = JSON.parse(r.data);
					t.forEach(function(i){
						xHtml = xHtml + '<option value = "'+ i.codigo +'">'+ i.descripcion +'</option>';
					});
					$('#selectTipologia').html(xHtml);
				}
			},
			error: function (a,b,c){
				
			}
		});
		$("#modalTipologia").modal();
	});
	$('#content').on('keypress', '#PhoneNumer',function(e){
		    if (e.keyCode == 13) {
		    	LoadScreen($(this).val());
		    }
	});
	
	$("#content").on('.accionModalSearch', 'click', function(){
		RecOrd();
	});

	
	function LoadScreen(telefono){
		opCod = "2014";
		$.ajax({
			type:"POST",
			data: 'telefono='+ telefono + '&cod=' + opCod,
			async: false,
			url:"Loader",
			success: function (r){
				//alert(r);
				if(r.r==="1"){
					x = JSON.parse(r.data);
					xHtml = '';
	    			x.forEach(function (i){ 
	    					fullName = i.primer_nombre + ' ' + i.segundo_nombre + ' ' + i.primer_apellido + i.segundo_apellido;   
	    					xHtml = xHtml + '<option value="'+i.no_cliente +'">' + fullName + '</option>';  
	    				});
					$('#listaClientes').html(xHtml);
					ic = $( "#listaClientes option:selected" ).val();
					$("#PhoneNumer").val(r.tele);
	    			ClientDirecction(ic);
				};
			},
			error:function (a,b,c){
				alert(a.responseText);
			},
			dataType: "json",
		});    	
	}
	
	function ClientDirecction(ic){
		opCod = '1988';
		$.ajax({
			url:"Loader",
			type:"POST",
			data: 'ic='+ ic + '&cod=' + opCod,
			async: false,
			success: function(r){
				//r = JSON.parse(r);
				if(r.r == "1"){
					xr = JSON.parse(r.data);
					$('.contenedorDirecciones').find('label').remove();
					var xHtml = '' ;
					xr.forEach(function(i){ 
									xHtml = xHtml + '<label id="direccion" data-id="'+ i.restaurante_asignado +'">' +  i.direccion + '</label>';
								});
					$('.contenedorDirecciones').html(xHtml);
					$("#obs").text("");
					$("#typeC").text("");
				};
				if(r.v == "1"){
					$("#obs").text(r.obs);
					$("#typeC").text(r.tipo);
				}
				if(r.l == "1"){
					$("#fechaHora").text(r.lFecha);
					$(".totalOrden").text("TOTAL DE LA ORDEN Q.: "+ r.lTotal);
				}
				if(r.f == "1"){
					
				}
			},
			error:function(a,b,c){
				alert(a.responseText);
			},
			dataType:"json"
		});
	}
	$("#content").on('click','.buscar', function (){
		$("#telefonoCliente").val($("#PhoneNumer").val());
		$("#nombreCliente").val();
		RecOrd();
	});
	function RecOrd(){
		opCod = 2011;
		var tel, cli, rest, fech, ord, org = '';
		tel = $("#telefonoCliente").val();
		cli = $("#nombreCliente").val();
		rest = $("#numeroRestaurante").val();
		fech = $("#fechaOrden").val();
		fech = fech.replace("-","").replace("-","");
		ord = $("#numeroOrden").val();
		org = $("#origen").val();
		$.ajax({
			url:"Loader",
			data:"tel="+tel+"&cli="+cli+"&rest="+rest+"&fecha="+fech+"&ord="+ord+"&org="+org+"&cod="+opCod,
			type:"POST",
			dataType:"json",
			async:false,
			success:function(r){
				if(r.ord =="1"){
					yHtml = '';
					r.data = JSON.parse(r.data);
					r.data.forEach(function(i){
						var tip = ""
						yHtml = yHtml +'<tr>'+
	                        '<td style="background: url(media/imagenes/general/iconoWeb.png) no-repeat left center;"><img src=""> <span>'+i.version+'</span></td>'+
	                        '<td>'+i.numero_orden+'</td>'+
	                        '<td>'+i.nombre_cliente+'</td>'+
	                        '<td>'+i.total+'</td>'+
	                        '<td>'+ i.estado  +'</td>'+
	                    '</tr>';
					});
					$("#OrderList").html(yHtml);
				}
			},
			error: function(a,b,c){
				alert(a.responseText);
			}
		});
	}
	if(params.telefono != ''){
		LoadScreen(params.telefono);
	}

	$("#buscarOrdenes").click(function(){
		var elSelect = $('html').find('#modalBuscarOrden #origen');
		$("#modalBuscarOrden").modal();
		elSelect.fancySelect('update.fs');
	});

	   
   $("body").on("click" , ".accionModalSearch",function (){

	   	opCod = 2011;
	   	var tel, cli, rest, fech, ord, org = '';
	   	tel = $("#telefonoCliente").val();
	   	cli = $("#nombreCliente").val();
	   	rest = $("#numeroRestaurante").val();
	   	fech = $("#fechaOrden").val();
	   	fech = fech.replace("-","").replace("-","");
	   	ord = $("#numeroOrden").val();
	   	org = $("#origen").val();
	   	$.ajax({
	   		url:"Loader",
	   		data:"tel="+tel+"&cli="+cli+"&rest="+rest+"&fecha="+fech+"&ord="+ord+"&org="+org+"&cod="+opCod,
	   		type:"POST",
	   		dataType:"json",
	   		async:false,
	   		success:function(r){
	   			if(r.ord =="1"){
	   				yHtml = '';
	   				r.data = JSON.parse(r.data);
	   				r.data.forEach(function(i){
	   					yHtml = yHtml +'<tr data-id="' + i.numero_orden  +'" id="laOrdenABuscar">'+
		                        '<td style="background: url(media/imagenes/general/iconoWeb.png) no-repeat left center;"><img src=""> <span>'+i.version+'</span></td>'+
		                        '<td>'+i.numero_orden+'</td>'+
		                        '<td>'+i.nombre_cliente+'</td>'+
		                        '<td> Q.'+i.total+'</td>'+
		                        '<td>'+ i.estado +'</td>'+
	                       '</tr>';
	   				});
	   				$("#OrderList").html(yHtml);
	   			}
	   		},
	   		error: function(a,b,c){
	   			alert(a.responseText);
	   		}
	   	});
   });
   
   
   $('body').on('click','#laOrdenABuscar',function(){
	   window.open("OrderSearch?no_orden="+$(this).data('id'));
   });
	   

	
	
});     

function xLoad(){
    var x = '<div id="modalBuscarOrden" style="overflow: scroll;"> <div class="headerModal"> <h1>Buscar orden</h1> <span></span> </div> <div class="contenedorOrdenModal"> <div> <label>teléfono</label> <input type="text" name="telefonoCliente" id="telefonoCliente"/> </div> <div> <label>cliente</label> <input type="text" name="nombreCliente" id="nombreCliente" style="width: 181px;"/> </div> <div> <label>#rest.</label> <input type="text" name="numeroRestaurante" id="numeroRestaurante" style="width: 87px;"/> </div> <div> <label>fecha</label> <input type="date" name="fechaOrden" id="fechaOrden" /> </div> <div> <label>orden</label> <input type="text" name="numeroOrden" id="numeroOrden" style="width: 127px;"/> </div> <div> <label>origen</label> <select id="origen"> <option>Web</option> <option>App</option> <option>Call Center</option></select> </div>  <a class="accionModalSearch" href="javascript:void(0)"> <img src="media/imagenes/general/buscarOrdenModal.png"/> </a> </div> <table> <tbody id="OrderList" > </tbody> </table> </div>';
    $('html').append(x);
    $('body').find('#modalBuscarOrden #origen').fancySelect();
}

xLoad();



