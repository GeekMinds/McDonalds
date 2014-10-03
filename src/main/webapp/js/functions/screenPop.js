/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($){
	$('#cancelar').click(function(){ $.modal.close(); });
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
	$("#GestionarClienteAdd").click(function(){
		$(".ScreenPop").hide();
		$(".GestionarCliente").show();
	});
	
	$("#CancelarGestionar").click(function(){
		$(".ScreenPop").show();
		$(".GestionarCliente").hide();
	});
	
	$('#nuevaOrden').click(function(){
            if($('body').find('.borde').length > 0 && $('#listaClientes').val() != ''){
                window.open('TomadoraOrdenes?cliente='+$('#listaClientes').val()+'&dir='+$('body').find('.borde').data('dir')+'&rest='+$('body').find('.borde').data('id'));
            }
            else
        	{
            	alert("Debe seleccionar una direccion");
        	}
        });
	
    $(".contenedorAcciones .accion.buscar").click(function(){
        $("#modalBuscarOrden").modal();
        var selectModal = $('body').find('#origenSP');
        selectModal.fancySelect('update.fs'); 
        $("#telefonoCliente").val($("#PhoneNumer").val()); 
    });
    var direccionNueva = '';
    function deptos(){
    	$
		.ajax({
			url : "GestionarCliente",
			type : "POST",
			async : false,
			success : function(response) {
					direccionNueva = '<div class="divForm" id="modalCrearDireccion">'+
    	            '<div class="headerModal">'+
                    '<h1>Crear Dirección</h1>'+
                    '<span></span>'+
                '</div>'+
                '<div class="contenedorModalDirecciones">'+
                    '<form class="active" name="direccion" >'+
                    '<input type="hidden" name="telefonoCliente" value="" id="telefonoCliente" />'+
                        '<input type="hidden" name="cliente" value="" id="cliente_no" />'+
                    '<div>'+
                        '<label>tipo de dirección</label>'+
                        '<select name="tipoDireccion" class="direccionesCliente chosen-select" id="tipoDireccion">'+
                            '<option value="C">Casa</option>'+
                            '<option value="A">Empresa</option>'+
                            '<option value="O">Otro</option>'+
                        '</select>'+
                    '</div>'+
                    '<div>'+
                        '<label>dirección</label>'+
                        '<input type="text" placeholder="Dirección" name="direccion" id="direccionCliente" />'+
                    '</div>'+
                    '<div>'+
                        '<label>departamento</label>'+
                        '<select name="departamento" class="direccionesCliente chosen-select" id="departamento">'+
                        '<option class="default" value="">Elegir Una</option>';
					var dpt = $.parseJSON(response.data);
					$.each(dpt, function(index, value){
						 direccionNueva = direccionNueva + '<option value="'+value.cod_depto+'">'+value.descripcion+'</option>';
					});
					direccionNueva = direccionNueva + '</select>'+
					'</div>'+
                    '<div>'+
                        '<label>municipio</label>'+
                        '<select name="municipio" class="direccionesCliente" id="municipio">'+
                        '<option class="default" value="">Elegir Una</option>'+
                        '</select>'+
                    '</div>'+
                    '<div>'+
                        '<label>zona</label>'+
                        '<select  name="zona" class="direccionesCliente chosen-select" id="zona">'+
					'<option class="default" value="">Elegir Una</option>'+    
                        '</select>'+
                   '</div>'+
                    '<div>'+
                       ' <label>colonia</label>'+
                        '<select  name="colonia" class="direccionesCliente chosen-select" id="colonia">'+
                        '<option class="default" value="">Elegir Una</option>'+
                        '</select>'+
                    '</div>'+
                    '<div>'+
                        '<label>referencia</label>'+
                        '<input type="text" placeholder="Referencia" name="referencia" id="referencia" />'+
                    '</div>'+
                    '<div>'+
                    '<label>Restaurante Asignado</label>'+
                    '<select name="asignado" class="direccionesCliente chosen-select" id="asignado">'+
                    '</select>'+
                '</div>'+
                '</div></form>'+
                '<a href="javascript:void(0)" class="simplemodal-close" id="cancelar">'+
                    '<img src="media/imagenes/gestionarCliente/cancelar.png"/>'+
                '</a>'+
                '<a href="javascript:void(0)" id="guardar">'+
                    '<img src="media/imagenes/gestionarCliente/guardar.png"/>'+
                '</a>'+
                    '</div>';
					$('body').find('.divForm').remove();
					$('body').append(direccionNueva);
				},
				error : function(a, b, c) {
					alert(a.responseText);
				},
				dataType : "json"
			});
	        var selectModal = $("#modalCrearDireccion").find('select');
	        $.each(selectModal, function(i, v){
	            $(v).chosen();
	        });
    }
    
    
    
    function callDirectionForm(){
    	deptos();
    	$('body').find('#modalCrearDireccion').find('input[name="cliente"]').val($('#listaClientes').val());
    	direccionNueva = $('body').find('#modalCrearDireccion');
    	$.modal($(direccionNueva), {
            minHeight: 700,
            position:[152]
        });	
    }
    
    $('body').on('click', '#guardar', function(){
    	opCod = '790';
		
			formDir = $(this).parent().find('form');
		$.ajax({
			url : "Loader",
			type : "POST",
			data : formDir.serialize() + '&cod=' + opCod,
			async : false,
			success : function(r) {
				ClientDirecction($('#listaClientes').val());
				$.modal.close();
			},
			error : function(a, b, c) {
				alert(a.responseText);
			},
			dataType : "json"
	});
    });
    $('#dirClienteAdd').click(function (e) {
    	e.preventDefault();
    	if($('#listaClientes').val() != null && $('#listaClientes').val() != ''){
    		callDirectionForm();
    	}
    });
    $('#dirClienteEdit').click(function (e) {
    	e.preventDefault();
    	
    	if($('#listaClientes').val() != null && $('#listaClientes').val() != '' && $('body').find('.borde').length > 0){
    		deptos();
    		direccion($('body').find('.borde').data('dir'), $('#listaClientes').val());
    		direccionNueva = $('body').find('#modalCrearDireccion');
    		$.modal($(direccionNueva), {
                minHeight: 700,
                position:[152]
            });	
    	}
    });
    
    function direccion(dir) {
		opCod = '9898';
		clienteID = $('#listaClientes').val();
		$
				.ajax({
					
					url : "Loader",
					type : "POST",
					data : 'dir=' + dir + '&cod=' + opCod
							+ '&cliente=' + clienteID,
					async : false,
					success : function(response) {
						// r = JSON.parse(r);
						if (response.r == "1") {
							
							dir = $.parseJSON(response.data);
							deptos();
							var newDiv = $('body').find('#modalCrearDireccion');
							newDiv.find('input[name="cliente"]').val($('#listaClientes').val());
							newDiv
									.find(
											'select[name="tipoDireccion"]')
									.find('option:selected')
									.removeAttr('selected');
							newDiv
									.find(
											'select[name="tipoDireccion"]')
									.removeAttr('selected');
							newDiv
									.find(
											'select[name="tipoDireccion"]')
									.find(
											'option[value="'
													+ dir.tipo.toUpperCase()
													+ '"]')
									.attr('selected', true);

							newDiv
									.find(
											'select[name="departamento"]')
									.removeAttr('selected');
							newDiv
									.find(
											'select[name="departamento"]')
									.find('option:selected')
									.removeAttr('selected');
							newDiv
									.find(
											'select[name="departamento"]')
									.find(
											'option[value="'
													+ dir.depto
													+ '"]')
									.attr('selected', true);

							newDiv
									.find(
											'select[name="departamento"]')
									.change();

							newDiv.find(
									'select[name="municipio"]')
									.removeAttr('selected');

							newDiv.find(
									'select[name="municipio"]')
									.find('option:selected')
									.removeAttr('selected');
							newDiv.find(
									'select[name="municipio"]')
									.find(
											'option[value="'
													+ dir.muni
													+ '"]')
									.attr('selected', true);
							newDiv.find(
									'select[name="municipio"]')
									.change();
							newDiv.find('select[name="zona"]')
									.removeAttr('selected');
							newDiv.find('select[name="zona"]')
									.find('option:selected')
									.removeAttr('selected');
							newDiv.find('select[name="zona"]')
									.find(
											'option[value="'
													+ dir.zona
													+ '"]')
									.attr('selected', true);
							newDiv.find('select[name="zona"]')
									.change();
							newDiv.find(
									'select[name="colonia"]')
									.removeAttr('selected');
							newDiv.find(
									'select[name="colonia"]')
									.find('option:selected')
									.removeAttr('selected');
							newDiv.find(
									'select[name="colonia"]')
									.find(
											'option[value="'
													+ dir.col
													+ '"]')
									.attr('selected', true);
							newDiv.find(
									'select[name="colonia"]')
									.change();

							newDiv.find(
									'input[name="referencia"]')
									.val(dir.referencia);

							newDiv
									.find(
											'input[name="telefonoCliente"]')
									.val(dir.telefono);
							newDiv.find(
									'input[name="direccion"]')
									.val(dir.direccion);

							
							newDiv.find(
							'select[name="asignado"]').find('option:selected')
										.removeAttr('selected');
							newDiv.find(
							'select[name="asignado"]')
										.removeAttr('selected');
							newDiv.find(
							'select[name="asignado"]')
										.find(
												'option[value="'
														+ dir.asignado
														+ '"]')
										.attr('selected', true);
							

							//					newDiv.find('select[name="tipoDireccion"]').trigger('render');
							//					newDiv.find('select[name="departamento"]').trigger('render');
							//					newDiv.find('select[name="municipio"]').trigger('render');
							//					newDiv.find('select[name="zona"]').trigger('render');
							//					newDiv.find('select[name="colonia"]').trigger('render');
							//					$('#direccionRestaurante').trigger('render');
						}
					},
					error : function(a, b, c) {
						alert(a.responseText);
					},
					dataType : "json"
				});
	}
    
    //$('#selectTipologia').customSelect();
    
    //$('#origen').customSelect({customClass:'selectOrigen'});
    
    $('#listaClientes').change(function(){
    		//alert($(this).val());
//    		ic = $(this).val();
//    		ClientDirecction(ic);
//    		$('.lugarRestaurante').text("");
//    		$('.estimado').text("");
//    		$(".contenedorInfoRestaurante").find("textarea").html("");
	});

    $(".chosen-select").change(function(){
		ic = $(this).val();
		ClientDirecction(ic);
		$('.lugarRestaurante').text("");
		$('.estimado').text("");
		$(".contenedorInfoRestaurante").find("textarea").html("");    	
    });
    
    
    $('body').on('click','.direccion',function(){
    	ra = $(this).data('id');
    	$('.borde').removeClass('borde'); 
    	$(this).addClass('borde');
    	opCod = "1992";
    	$(".contenedorInfoRestaurante").find("textarea").html('');
    	$('.lugarRestaurante').text('');
		$('.estimado').text('');
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
    	$('.direccionActiva').removeClass('direccionActiva');
    	$(this).addClass('direccionActiva');
    });
    $('.ladoDerecho .seccionOrden a').click(function(){
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
    
    $("#actModal").click(function(){
        $("#modalCrearDireccion").modal({
            minHeight: 700,
            position:[152]
        });
    });
    
    
    var selecTipologia =$('#selectTipologia').fancySelect();
    
    $(".tipologiaLlamada").click(function(){
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
                    selecTipologia.trigger('update.fs');
				}
				else{
					
				}
			},
			error: function (a,b,c){
				
			}
    	});
    	$("#modalTipologia").modal();
	});
    $('#PhoneNumer').bind('keypress', function(e){
    	    if (e.keyCode == 13) {
    	    	LoadScreen($(this).val());
    	    }
	});

    $(".accionModalSearch").click(function(){
    	RecOrd();
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
        					fullName = i.primer_nombre + ' ' + i.segundo_nombre + ' ' + i.primer_apellido +' '+ i.segundo_apellido;   
        					xHtml = xHtml + '<option value="'+i.no_cliente +'">' + fullName + '</option>';  
        				});
    				$('#listaClientes').html(xHtml);
    				ic = $( "#listaClientes option:selected" ).val();
    				$("#PhoneNumer").val(r.tele);
        			ClientDirecction(ic);
        			$('.lugarRestaurante').text("");
        			$('.estimado').text("");
        			$(".contenedorInfoRestaurante").find("textarea").html("");    	
    			}
    			else{
    				if($("#PhoneNumer").val().length > 0){
    					alert('No se encontro Cliente');	
    				}
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
				if(r.r == "1"){
					xr = JSON.parse(r.data);
					
					var xHtml = '' ;
    				xr.forEach(function(i){ 
    								xHtml = xHtml + '<label class="direccion" data-dir="'+i.telefono+'" data-id="'+ i.restaurante_asignado +'">' +  i.direccion_completa + '</label>';
    							});
    				$('.contenedorDirecciones').html(xHtml);
					$("#obs").text("");
					$("#typeC").text("");
				};
				if(r.v == "1"){
					$("#obs").text(r.obs);
					$("#typeC").text(r.tipo);
				}
				$("#fechaHora").text('');
				$('.newData').find('*').not('.fechaHora').remove();
				if(r.l == "1"){
					
					
					$("#fechaHora").text(r.lFecha);
                                        if(r.lDetalle !== ""){
					var htmlSplit = r.lDetalle.split("<h1>");
					$.each(htmlSplit, function(is,cs){
                                            if(is > 0){
                                              var splitInside = cs.split("<h2>");
                                              var menu = splitInside[0];
                                              $('<h1 class="orden">'+menu+'</h1>').appendTo('.newData');
                                              var splitExtra = splitInside[1].split("<br>");
                                              $.each(splitExtra, function(ji,ka){
                                                  if(ji > 0){
							$('<label class="detalleOrden">'+ka+'</label>').appendTo('.newData');
                                                    }
						});
                                            }
                                        });
                                    }
					$(".totalOrden").text("TOTAL DE LA ORDEN Q.: "+ r.lTotal);
				}
				$('.contenedorFavoritos').empty();
				if(r.f == "1"){
					favors = $.parseJSON(r.favs);
					$.each(favors, function(iy, vz){
						$('<a href="javascript:void(0)" class="favorito">'+
                            '<span class="textoFavorito">'+ vz.nombre +'</span>'+
                        '</a>').appendTo('.contenedorFavoritos');
					});
				}
			},
			error:function(a,b,c){
				alert(a.responseText);
			},
			dataType:"json"
		});
    }
    $(".buscar").click(function (){
    	$("#telefonoCliente").val($("#PhoneNumer").val());
    	$("#nombreCliente").val();
    	RecOrd();
    });
    
    function RecOrd(){
		opCod = 2011;
		var tel, cli, rest, fech, ord, org = '';
		//$("#telefonoCliente").val($("#PhoneNumer").val());
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
	                        '<td>'+i.total+'</td>'+
	                        '<td>'+i.estado +'</td>'+
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
//    function RecOrd(){
//    	opCod = 2011;
//    	var tel, cli, rest, fech, ord, org = '';
//    	tel = $("#PhoneNumer").val($("#telefonoCliente").val());
//    	cli = $("#nombreCliente").val();
//    	rest = $("#numeroRestaurante").val();
//    	fech = $("#fechaOrden").val();
//    	fech = fech.replace("-","").replace("-","");
//    	ord = $("#numeroOrden").val();
//    	org = $("#origen").val();
//    	$.ajax({
//    		url:"Loader",
//    		data:"tel="+tel+"&cli="+cli+"&rest="+rest+"&fecha="+fech+"&ord="+ord+"&org="+org+"&cod="+opCod,
//    		type:"POST",
//    		dataType:"json",
//    		async:false,
//    		success:function(r){
//    			if(r.ord =="1"){
//    				yHtml = '';
//    				r.data = JSON.parse(r.data);
//    				r.data.forEach(function(i){
//    					yHtml = yHtml +'<tr>'+
//	                        '<td style="background: url(media/imagenes/general/iconoWeb.png) no-repeat left center;"><img src=""> <span>'+i.version+'</span></td>'+
//	                        '<td>'+i.numero_orden+'</td>'+
//	                        '<td>'+i.nombre_cliente+'</td>'+
//	                        '<td>'+i.total+'</td>'+
//	                        '<td>'+ r.data.forEach(function(i){ ((i.estado === 'E')? 'Entregado': 'Grabado' );}) +'</td>'+
//                        '</tr>';
//    				});
//    				$("#OrderList").html(yHtml);
//    			}
//    		},
//    		error: function(a,b,c){
//    			alert(a.responseText);
//    		}
//    	});
//    }
    LoadScreen(params.telefono);
    $('body').on(
			'change',
			'select[name="departamento"]',
			function() {
				idepto = $(this).val();
				var dpdwMuni = $(this).closest('form').find(
						'select[name="municipio"]');
				dpdwMuniN = dpdwMuni.find('.default');
				dpdwMuni.html(dpdwMuniN);
				var dpdwZona = $(this).closest('form').find(
						'select[name="zona"]');
				dpdwZonaN = dpdwZona.find('.default');
				dpdwZona.html(dpdwZonaN);
				var dpdwCol = $(this).closest('form').find(
						'select[name="colonia"]');
				dpdwColN = dpdwCol.find('.default');
				dpdwCol.html(dpdwColN);
				if (idepto != '') {
					municipios(idepto, $(this));
				}
                var selectModal = $("#modalCrearDireccion").find('select');
                $.each(selectModal, function(i, v){
                    $(v).trigger('chosen:updated');
                });
			});
    
	$('body').on(
			'change',
			'select[name="municipio"]',
			function() {
				idmuni = $(this).val();
				var dpdwZona = $(this).closest('form').find(
						'select[name="zona"]');
				dpdwZonaN = dpdwZona.find('.default');
				dpdwZona.html(dpdwZonaN);
				var dpdwCol = $(this).closest('form').find(
						'select[name="colonia"]');
				dpdwColN = dpdwCol.find('.default');
				dpdwCol.html(dpdwColN);
				if (idmuni != '') {
					zonas(idmuni, $(this));
				}
                var selectModal = $("#modalCrearDireccion").find('select');
                $.each(selectModal, function(i, v){
                    $(v).trigger('chosen:updated');
                });				
			});
	

	$('body').on(
			'change',
			'select[name="zona"]',
			function() {
				idzona = $(this).val();
				var dpdwCol = $(this).closest('form').find(
						'select[name="colonia"]');
				dpdwColN = dpdwCol.find('.default');
				dpdwCol.html(dpdwColN);

				if (idzona != '') {
					colonias(idzona, $(this));
					var dpdwRes = $(this).closest('form').find(
					'select[name="asignado"]');;
					dpdwResN = dpdwRes.find('.default');
					dpdwRes.html(dpdwResN);
					dpdwRes.change();
					restauranteSugerido(idzona, "", $(this));

				}
                var selectModal = $("#modalCrearDireccion").find('select');
                $.each(selectModal, function(i, v){
                    $(v).trigger('chosen:updated');
                });	
			});
	$('body')
			.on(
					'change',
					'select[name="colonia"]',
					function() {
						idzona = $(this).closest('form').find(
								'select[name="zona"]').val();
						idcolonia = $(this).val();
						var dpdwRes =  $(this).closest('form').find(
						'select[name="asignado"]'), dpdwResN = dpdwRes
								.find('.default');
						dpdwResN.attr('selected', true);
						dpdwRes.html(dpdwResN);
						dpdwRes.change();
						restauranteSugerido(idzona, idcolonia,
								$(this));
		                var selectModal = $("#modalCrearDireccion").find('select');
		                $.each(selectModal, function(i, v){
		                    $(v).trigger('chosen:updated');
		                });	
					});
	//RECUPERA RESTAURANTE SEGUN ZONA, COLONIA
	function restauranteSugerido(idzona, idcolonia, selecte) {
		opCod = "1013";
		idZona = idzona;
		idDepto = selecte.closest('form').find(
				'select[name="departamento"]').val();
		idMuni = selecte.closest('form').find(
				'select[name="municipio"]').val();
		asig = selecte.closest('form').find(
		'select[name="asignado"]');
		idCol = idcolonia;
		$('.tiempoEntrega').text(
				'tiempo de entrega - ');
		$
				.ajax({
					type : "POST",
					data : 'idDepto=' + idDepto + '&idMuni='
							+ idMuni + '&idZona=' + idZona
							+ '&idCol=' + idCol + '&cod='
							+ opCod,
					async : false,
					url : "Loader",
					success : function(response) {
						if (response.r === '1') {
							restaurantes = $
									.parseJSON(response.data);

							$
									.each(
											restaurantes,
											function(index,
													value) {
												asig
														.append(
																'<option value="'
																		+ value.tienda
																		+ '" >'
																		+ value.nombre
																		+ '</option>');
											});

						} else {
							alert(response.data);
						}
					},
					dataType : "json"
				});
	}

	function municipios(idepto, sele) {
		opCod = "1010";
		idDepto = idepto;
		$
				.ajax({
					type : "POST",
					data : 'idDepto=' + idDepto + '&cod='
							+ opCod,
					async : false,
					url : "Loader",
					success : function(response) {
						if (response.r === '1') {
							municipiosw = $
									.parseJSON(response.data);
							$
									.each(
											municipiosw,
											function(index,
													value) {
												sele
														.closest(
																'form')
														.find(
																'select[name="municipio"]')
														.append(
																'<option value="'
																		+ value.cod_municipio
																		+ '" >'
																		+ value.descripcion
																		+ '</option>');
											});
						} else {
							alert(response.data);
						}
					},
					dataType : "json"
				});
	}

	function zonas(idmuni, selec) {
		opCod = "1011";
		idMuni = idmuni;
		idDepto = selec.closest('form').find(
				'select[name="departamento"]').val();
		$.ajax({
			type : "POST",
			data : 'idDepto=' + idDepto + '&idMuni=' + idMuni
					+ '&cod=' + opCod,
			async : false,
			url : "Loader",
			success : function(response) {
				if (response.r === '1') {
					zonasR = $.parseJSON(response.data);
					$.each(zonasR, function(index, value) {
						selec.closest('form').find(
								'select[name="zona"]').append(
								'<option value="'
										+ value.cod_zona
										+ '" >'
										+ value.descripcion
										+ '</option>');
					});
				} else {
					alert(response.data);
				}
			},
			dataType : "json"
		});
	}

	function colonias(idzona, select) {
		opCod = "1012";
		idZona = idzona;
		idDepto = select.closest('form').find(
				'select[name="departamento"]').val();
		idMuni = select.closest('form').find('select[name="municipio"]').val();
		$.ajax({
					type : "POST",
					data : 'idDepto=' + idDepto + '&idMuni='
							+ idMuni + '&idZona=' + idZona
							+ '&cod=' + opCod,
					async : false,
					url : "Loader",
					success : function(response) {
						if (response.r === '1') {
							coloniasR = $.parseJSON(response.data);
							$.each(coloniasR,function(index,value) {
								select.closest('form').find('select[name="colonia"]').append('<option value="'+ value.cod_colonia+ '" >'+ value.descripcion+ '</option>');
							});
						} else {
							//alert(response.data);
						}
					},
					dataType : "json"
				});
	}
    $('#crearCliente').click(function(){
        $('#modalCrearCliente').modal({
            overlayId :"overlayCliente",
            overlayCss:{position:"absolute", width: "100%", height: "100%", backgroundColor:"#000"},
            minHeight:570,
            minWidth: 700,
            containerId: "contenedorModalCrearCliente"
        });
        var elTel = $("#PhoneNumer").val();
        $("#ingresoTelefonoGes").val(elTel)
    });
    
	$('#guardarCliente').click(function(){
		save();
	});
    
	$('#selectTipologia').fancySelect();
    
    function save(){
		opCod = '710';
		$.ajax({
			url : "Loader",
			type : "POST",
			data : $("#formCliente").serialize() + '&cod=' + opCod,
			async : false,
			success : function(r) {
				
				// r = JSON.parse(r);
				if (r.r == "1") {
					$('.contenedorDirecciones').find('label').remove();
					$('#listaClientes').html('<option selected="selected" value="'+r.data+'">'+document.getElementsByName('nombres')[0].value +' '+ document.getElementsByName('apellidos')[0].value+'</option>');
					ClientDirecction(r.data);
					$('.chosen-select').trigger('chosen:updated');
					$.modal.close();
					$('.lugarRestaurante').text("");
					$('.estimado').text("");
					$(".contenedorInfoRestaurante").find("textarea").html("");    	
					if($('#clienteGes').val().length === 0){
						$('#dirClienteAdd').click();
					}
				}else{
					alert(r.data);	
				}
				
			},
			error : function(a, b, c) {
				alert(a.responseText);
			},
			dataType : "json"
		});
	}
    
    
		$('#selectTipologia').fancySelect();
	    
	    
	    $('#crearClienteEdit').click(function(){
			if ($('#PhoneNumer').val().length > 0 ){
				if($('#listaClientes').val().length > 0){
					
					console.log(1);
					cliente($('#listaClientes').val());
					
					var telefonoEdiar = $('#PhoneNumer').val();
					$('#telCrearCliente').val(telefonoEdiar);
				}
			}
	    });
    
		function cliente(clienteID) {
			opCod = '1099';
			$.ajax({
				url : "Loader",
				type : "POST",
				data : 'cliente=' + clienteID + '&cod=' + opCod,
				async : false,
				success : function(response) {

					if (response.r == "1") {
						clienteR = $.parseJSON(response.data);
						$('#clienteGes').val(clienteID);
						$('input[name="telefonoGest"]').val(
								clienteR.c_tel);
						$('input[name="nombres"]').val(
								clienteR.c_nombre);
						$('input[name="email"]')
								.val(clienteR.c_mail);
						$('input[name="apellidos"]').val(
								clienteR.c_ape);
						$('input[name="nombreFacturacion"]').val(
								clienteR.c_fac);
						$('input[name="nit"]').val(clienteR.c_nit);
						$('input[name="fechaNacimiento"]').val(
								clienteR.c_fecha);
						$('#modalCrearCliente').modal({
							overlayId :"overlayCliente",
							overlayCss:{position:"absolute", width: "100%", height: "100%", backgroundColor:"#000"},
							minHeight:570,
							minWidth: 700,
							containerId: "contenedorModalCrearCliente"
						});
					}
				},
				error : function(a, b, c) {
					alert(a.responseText);
				},
				dataType : "json"
			});
		}
    
		$('.btnAceptar').click(function(){
			params = {};
			params.cod  = 1984;
			params.call = $('#selectTipologia').val();
			params.text = $('.datosAdicionales').val();
			params.telefono = $('#PhoneNumer').val();
			$.ajax({
				url:'Loader',
				data:params,
				type:'POST',
				success: function (r){
					$.modal.close();
					alert('Registro Llamado');
				},
				error: function (a,b,c){
					console.log(a);
				}
			});
		});
	   $('body').on('click','#laOrdenABuscar',function(){
		   window.open("OrderSearch?no_orden="+$(this).data('id'));
	   });
});