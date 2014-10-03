/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($){
		$('.chosen-select').trigger('chosen:updated');
   		$('#PhoneNumer').bind('keypress', function(e){
	    if (e.keyCode == 13) {
	    	LoadScreen($(this).val());
	    }
	});
   		
	var params = {};
	params.no_orden = '';
	var getParams = window.location.search.substr(1);
	var x = getParams.split("&");
	for ( var i = 0; i < x.length; i++) {
        var tmparr = x[i].split("=");
        params.no_orden = tmparr[1];
    }

	if(params.no_orden == "" ){
		
	}
	else
	{
		
		params.cod = 911;
		
		var orden = []; 
	   
		$.ajax({
			url:'Loader',
			data: params,
			type:'POST',
			success: function (r){
				var TheOrd = JSON.parse(r.orden);
				orden['orden'] = TheOrd[0];
				ci = {};
				ci.cod = 1099;
				ci.cliente = TheOrd[0].no_cliente;
				$.ajax({
					type:"POST",
					url:"Loader",
					data:ci,
					success: function (r){
						if(r.r === "1"){
							res = JSON.parse(r.data);
							orden['cliente'] = res;
						}
					},
					error: function (a,b,c){
						console.log(a.responseText);
					},
					async:false,
					dataType: 'json'
				});
			},
			error: function (a,b,c){
				console.log(a.responseText);
			},
			async: false,
			dataType: 'json'
		});
		
		ClientDirecction(orden.cliente.no_cliente);
		
		
		params.cod = 98786;
		$.ajax({
			url:"Loader",
			type:"POST",
			data:params,
			success: function (r){
				//console.log(r);
				//$(".contenedorOrdenes").
				$(".contenedorOrdenes").append(r.detalles[0].detalle);
				$(".totalOrden").html("TOTAL DE LA ORDEN Q"+r.detalles[0].total);
				
				
			},
			error: function (a,b,c) {
				console.log(a.responseText);
			},
			dataType:"json",
			async:false
		});
	}
	
	
	
	$("#noOrdern").text(params.no_orden);
	$("#ciTel").text(orden.orden.telefono_origen);
	$("#ciNombre").text(orden.cliente.c_fac);
	
	$("#confirmarO").val(orden.orden.confirmar_texto);
	$("#observacionInterna").val(orden.orden.observaciones_orden);
	$("#observacionEntrega").val(orden.orden.observaciones_entrega);
	$("#ciTiempo").text(orden.orden.mensaje_tiempo);
	$(".fechaHora").text(orden.orden.fecha_orden);
	if(orden.orden.ordenes_asociadas != ""){
		var xOrdNum = orden.orden.ordenes_asociadas.split(",")
		var htmlOrd = "";
		xOrdNum.forEach(function(e)
		{
			if(e != ""){
				e.replace(' ','');
				htmlOrd = htmlOrd + "<a href='OrderSearch?no_orden="+e+"' target='_blank' style='text-decoration: none; color: black;'> Ver: "+e+" </a>";	
			}
			 
		});
		$(".ordenAsociada").html(htmlOrd);
	}

	if(orden.orden.no_emple == "0"){
		$(".contBlanco").find('label').text("El Cliente");
	}
	else
	{
		$.ajax({
			url:"Loader",
			type:"POST",
			data:"cod=1207&no_emp="+orden.orden.no_emple,
			dataType:"json",
			success: function(r){
				$(".contBlanco").find('label').text(r.empleado);
			},
			error: function(a,b,c){
				console.log(a.responseText);
			}
		})
		
	}
	
	
	
	if(orden.orden.confirmar_texto != ""){
		$("#confirmarOrden").prop('checked',true)
	}
	
	if(orden.orden.estado_1 === "S")
	{
		$("#opGrabada").prop('checked','true')
	}
	
	if(orden.orden.estado_2 === "S")
	{
		$("#opEnviada").prop('checked','true')
	}
	
	if(orden.orden.estado_3 === "S")
	{
		$("#opAnulada").prop('checked','true')
	}
	
	if(orden.orden.estado_4 === "S")
	{
		$("#opProduccion").prop('checked','true')
	}
	
	if(orden.orden.estado_5 === "S")
	{
		$("#opEnviada2").prop('checked','true')
	}
	
	if(orden.orden.estado_6 === "S")
	{
		$("#opEntregada").prop('checked','true')
	}
	
	
	
	
	$("#opGrabada").prop('checked','true')
	
	
	$.ajax({
		url:"Loader",
		data:"cod=9898&cliente="+orden.orden.no_cliente+"&dir="+orden.orden.telefono,
		type:"POST",
		success: function (r){
			//console.log(r);
			if(r.r == "1"){
				x = JSON.parse(r.data);
				$(".direccion").text(x.direccion);
				orden['asiganado'] = x.asignado;
			}
		},
		error: function (a,b,c){
			console.log(a.responseText);
		},
		dataType:'json',
		async: false
	});

	
	
	$.ajax({
		url:"Loader",
		type:"POST",
		data:"cod=1992&ra="+orden.asiganado,
		success: function (r){
			if(r.o === "1"){
				$('#restName').text(r.nombre);
				$('#ciTiempoRest').text(r.tiempo);
				h = "Observaciones de Promocion: " + r.observaciones_promocion + "\n";
				s = "Observaciones de Producto: " + r.observaciones_producto + "\n";
				t = "Observaciones de Servicio: " + r.observaciones_servicio + "\n";
				
				$("#restinfo").html(h+s+t);	
			}
			else if(r.o === "-1")
			{
				$("#restinfo").html(r.mensaje);
			}
			
		},
		error: function(a,b,c){
			console.log(a.responseText);
		},
		dataType:"json",
		async:false
	});
	
	
	$("#noOrdern").text(params.no_orden);
	$("#noOrdern").text(params.no_orden);

	   
	$("#SendThisOrder").click(function(){
		
		x = $("#noOrdern").text();
		var op1 = $("#opGrabada"); 
		var op2 = $("#opEnviada");
		var op3 = $("#opAnulada");
		var op4 = $("#opProduccion");
		var op5 = $("#opEnviada2");
		var op6 = $("#opEntregada");
		
		if(op1.is('checked') === true || op2.is('checked') === true || op3.is('checked') === true || op4.is('checked') === true || op5.is('checked') === true || op5.is('checked') === true || op5.is('checked') === true){
			return false;
		}
		
		$.ajax({
			url:"OrderHandler",
			data:"cod=19032&numero_orden=" + x,
			type:"POST",
			dataType:"json",
			success: function (r){
				alert(r.mensaje);
			},
			error: function(a,b,c){
				console.log(a.responseText);
			}
		});
		
	});
	
	
	
	
	
	
	
});





   function LoadScreen(telefono){
    	opCod = "2014";
    	tel = telefono;
    	$.ajax({
    		type:"POST",
    		data: 'telefono='+ telefono + '&cod=' + opCod,
    		async: false,
    		url:"Loader",
    		success: function (r){
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
    			}
    			$('.chosen-select').trigger('chosen:updated');
    		},
    		error:function (a,b,c){
    			alert(a.responseText);
    		},
    		dataType: "json",
    	});    	
    	$('.chosen-select').trigger('chosen:updated');
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
				$('.contenedorDirecciones').find('label').remove();

				if(r.v == "1"){
					$("#obs").text(r.obs);
					$("#typeC").text(r.tipo);
				}
			},
			error:function(a,b,c){
				alert(a.responseText);
			},
			dataType:"json"
		});
   }
   
   
   