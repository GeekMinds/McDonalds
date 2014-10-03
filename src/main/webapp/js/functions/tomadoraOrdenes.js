jQuery(document).ready(function($) {
    /*INIT*/
    var QueryString = function() {
        var query_string = {};
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            // If first entry with this name
            if (typeof query_string[pair[0]] === "undefined") {
                query_string[pair[0]] = pair[1];
                // If second entry with this name
            } else if (typeof query_string[pair[0]] === "string") {
                var arr = [query_string[pair[0]], pair[1]];
                query_string[pair[0]] = arr;
                // If third or later entry with this name
            } else {
                query_string[pair[0]].push(pair[1]);
            }
        }
        return query_string;
    }();
    var clienteID = QueryString.cliente;
    var dir = QueryString.dir;
    var res = QueryString.rest;
    var cod_pedido = QueryString.cod_pedido;
    if (clienteID !== undefined && clienteID !== 'undefined') {
        cliente(clienteID);
    }
    if (dir !== undefined && dir !== 'undefined') {
        direccion(dir, clienteID);
    }
    if (res !== undefined && res !== 'undefined') {
        infoRestaurante(res);
    }
    if (cod_pedido !== undefined && cod_pedido !== 'undefined') {
    		cargarOrdenParaModificar(cod_pedido);
    }
    if ($('a.on').length > 0) {
        var tipoH = $('a.on').data('tipo');
        clases(tipoH);
    }
    /*MODIFICAR*/
    function cargarOrdenParaModificar(cod_pedido){
    	var cod = "1109";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido=" + cod_pedido + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.cod === "0") {
                    console.log(r.data);
                }else{
                    alert(r.mensaje);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
    }
    
    /**
     * 
     * EVENTS
     * 
     */
    /*SWITCH*/

    $('.contenedorSwitch label, .contenedorSwitch span').click(function() {
        if (!$("#switch").hasClass('on')) {
            if (!$(this).hasClass("txOff")) {
                $("#switch").addClass('on');
            }
            slide(1, '1')
        } else {
            if (!$(this).hasClass("txOn")) {
                $("#switch").removeClass('on');
            }
            slide(1, '2')
        }

    });
    /*HORARIO*/
    $('#header .tipoProducto').click(function() {
        $('.on').removeClass('on');
        if ($(this).hasClass('iconoAlmuerzo') && !$(this).hasClass('almuerzoOn')) {
            $(this).removeClass('almuerzoInactivo').addClass('almuerzoOn').addClass('on');
            $('.iconoDesayuno').removeClass('desayunoOn').addClass('desayunoInactivo');

        } else if ($(this).hasClass('iconoDesayuno') && !$(this).hasClass('desayunoOn')) {
            $(this).removeClass('desayunoInactivo').addClass('desayunoOn').addClass('on');
            $('.iconoAlmuerzo').removeClass('almuerzoOn').addClass('almuerzoInactivo');

        }
        clases($(this).data('tipo'));
    });
    $('#fechaEnvio').datetimepicker({
        lang: 'es',
        timepicker: false,
        format: 'd/m/Y',
        step: 60,
        minDate: 0,
        minTime: '11:00',
        maxTime: '0:00',
        closeOnDateSelect: true
    });
    $('#horaEnvio').datetimepicker({
        datepicker: false,
        format: 'H:i',
        step: 60,
        allowTimes: [
            '09:00',
            '10:00',
            '11:00',
            '12:00',
            '13:00',
            '14:00',
            '15:00',
            '16:00',
            '17:00',
            '18:00',
            '19:00',
            '20:00',
            '21:00',
            '22:00',
        ],
        closeOnDateSelect: true
    });
    $("input[name=tipoOrden]:radio").change(function() {
        if ($(this).val() == 'A') {
            $('.fechaAnt').show();
        } else {
            $('.fechaAnt').hide();
        }
    });
    $("input[name=tipo_pago]:radio").change(function() {
        if ($(this).val() == 'EF') {
        	$(this).closest('form').find('.fancy-select').hide();
            $('.pagoEfectivo').show();
            $('input[name=monto_efectivo]').val($('#totalDeTodo').text());
            $('.pagoTarjeta').hide();
            $('input[name=monto_efectivo]').prop('readonly', true);
            $('input[name=anio_vence]').prop('required', false);
            $('input[name=mes_vence]').prop('required', false);
            $('input[name=no_tarjeta]').prop('required', false);
        } else if ($(this).val() == 'TA') {
        	$(this).closest('form').find('.fancy-select').show();
            $('.pagoTarjeta').show();
            $('.pagoEfectivo').hide();
            $('input[name=monto_tarjeta]').val($('#totalDeTodo').text());
            $('input[name=monto_tarjeta]').prop('readonly', true);
            $('input[name=no_tarjeta]').prop('required', true);
            $('input[name=anio_vence]').prop('required', true);
            $('input[name=mes_vence]').prop('required', true);
        } else if ($(this).val() == 'MI') {
        	$('.pagoTarjeta').show();
        	$(this).closest('form').find('.fancy-select').show();
        	$('input[name=monto_efectivo]').val($('#totalDeTodo').text());
        	$('input[name=monto_tarjeta]').val('0.00');
        	$('input[name=monto_efectivo]').show().prop('readonly', false);
        	$('input[name=monto_tarjeta]').show().prop('readonly', false);
        	$('input[name=no_tarjeta]').prop('required', true);
        	$('input[name=anio_vence]').prop('required', true);
            $('input[name=mes_vence]').prop('required', true);
        }
    });
    /*MODALES*/
    $('#next').click(function() {
    	
    	if($('#contenedorOrdenes').find('.producto_seleccionado').length > 0 ){
    		cargaDeOrdenes();
    		
    		var d = new Date();
    		var n = d.getFullYear();
    		for(var i = 0; i <= 5; i++){
    			na = n + i;
    			$('#anioExp').append('<option value="'+na+'">'+na+'</option>');
    		}
    	    $('#mexExp').fancySelect();
    	    $('#anioExp').fancySelect();
    	    $('#anioExp').closest('.fancy-select').hide();
    	    $('#mexExp').closest('.fancy-select').hide();
    	$('#pagoEfectivo').val($('#totalDeTodo').text());
    	$('input[name="totaltodo"]').val($('#totalDeTodo').text());
    	$('#observacionEntregaTxt').val($('#obsEntega').val());
        $('#modalFormaPago').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            minHeight: 570,
            minWidth: 700,
            containerId: "contenedor",
            appendTo: "#content .ladoDerecho",
            position: [120, 15]
        });
    	}
    	
    });
    $('#regresarBtn').click(function(){
    	$('#obsEntega').val($('#observacionEntregaTxt').val());
    });
    $(".fechaExpiracion").customSelect({customClass: 'selectFechaExp'});
    $('.iconoLista').click(function() {
    	
        $('#modalEditarMasivo').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            minHeight: 210,
            minWidth: 600,
            containerId: "contenedorModalMasivo",
            appendTo: "#content .ladoDerecho",
            position: [325, 65]
        });
    });
    $('#recetaMasivo').click(function() {
        $.modal.close();
        $('#modalRecetaAdicionales').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            minHeight: 570,
            minWidth: 700,
            containerId: "contenedorModalRecetas",
            appendTo: "#content .ladoDerecho",
            position: [50, 45]
        });
    });
    $('.opcionesTabla a').click(function() {
        $('.activo').removeClass('activo');
        if ($(this).hasClass('activo')) {
            $(this).removeClass('activo');
        } else {
            $(this).addClass('activo');
        }
    });
    $('#tamanoMasivo').click(function() {
        $.modal.close();
        $('#modalMasivoTamano').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            minHeight: 405,
            minWidth: 700,
            containerId: "contenedorTamanoMasivo",
            appendTo: "#content .ladoDerecho",
            position: [150, 45]
        });
    });
    $('.hEdit').click(function() {
        $('#modalRecetaAdicionalesCarta').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            minHeight: 570,
            minWidth: 700,
            containerId: "contenedorModalRecetasCarta",
            appendTo: "#content .ladoDerecho",
            position: [50, 45]
        });
    });
    $('body').on('click', '#clases li a', function() {


        $('#clases li.activo').removeClass('activo');
        $(this).closest('li').addClass('activo');

    		var tipo = '1';
            if ($('#switch').hasClass('on')){
            	tipo = '1';
            }else{
            	tipo = '2';
            }
                
                
        
        var clas = $(this).data('clase');
        
        items(clas, tipo);
    });
    $('body').on('mouseenter', '.productos', function() {
        if ($('#switch').hasClass('on')) {
            if (!$(this).find('.overlayProducto').hasClass('inactivo')) {
                $(this).find('.overlayProducto').find('label').text(parseFloat($(this).find('.overlayProducto').data('precio_carta')).toFixed(2));
            }
        } else {
            if (!$(this).find('.overlayProducto').hasClass('inactivo')) {
                $(this).find('.overlayProducto').find('label').text(parseFloat($(this).find('.overlayProducto').data('precio_menu')).toFixed(2));
            }
        }
    });

  
    $('body').on('click', '.overlayProducto', function() {
        if (!$(this).hasClass('inactivo')) {
            agregarALaOrden($(this), $('#switch').hasClass('on'));
        }
    });
    $('body').on('change', 'select[name="opcionestamanio"]', function() {
        if ($(this).val() != '') {
            var producto = $(this).closest('.producto_seleccionado');
            cambiarTamano($(this).closest('.ordenDetallada').attr('id'), $(this).find('option:selected').data('cod_producto'));

            var precioTotal = 0;
            $.each(producto.find('select[name="opcionestamanio"]'), function(i, v) {
                precioTotal = precioTotal + parseFloat($(v).find('option:selected').data('precio'));
            });
            producto.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));

            actTotal();
 
            $(this).closest('.menuElegido').find('.precioMenu').find('label').text(parseFloat($(this).find('option:selected').data('precio')).toFixed(2));
//           
            
            var opcs = cambioTamanioOpciones($(this).closest('.ordenDetallada').attr('id'));
                    var gruposOpciones = opcs.grupos;
//                    if(gruposOpciones.length > 0){
                        var nuevo = "";
                        var detalleOrden = "";
                        $.each(gruposOpciones, function(i, v) {
                            if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '<select name="opcionesbebidas" class="bebidas">';
                            }
                                var datosOpciones = opcs.opcionDatos;
                                
                                $.each(datosOpciones, function(j, w) {
                                    if(v.clase_opciones !== "PP" && v.clase_opciones == w.clase_opciones && w.elegido){
	                                    detalleOrden = detalleOrden + '<div class="opcionItem esOpcion" data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" >' +
						                            	'<label>' + w.cantidad + ' ' + w.nombre + '</label>' +
							                            '<span>' + w.precio + '</span>' +
							                            '</div>';
                                    }
                                    if (v.clase_opciones == "PP" && w.clase_opciones == "PP" && w.elegido) {
                                nuevo = nuevo + '<option selected="selected" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            } else if(v.clase_opciones == "PP" && w.clase_opciones == "PP") {
                                nuevo = nuevo + '<option value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            }
                                });
                                if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '</select>';
                            }
                            });
                        
                        
                        var ingrd = cambioTamanioIngredientes($(this).closest('.ordenDetallada').attr('id'));
                        $.each(ingrd.ingredientes, function(i,v){ 
                        	if(v.elegido){
//                        		toAppend = toAppend + '<div data-precio="-' + precio + '" data-clases="sin" data-grupo="' + grupo + '" data-cantidad="' + cantidad + '" class="opcionItem esIngrediente ' + grupo + '" data-ingrediente="' + ingrediente + '" >' +
//                                '<label>' + cantidad + ' ' + nombre + '</label>' +
//                                '<span>' + parseFloat(precio * cantidad).toFixed(2) + '</span>' +
//                                '</div>';
                        		if(v.IDtipo == "96"){
                        			detalleOrden = detalleOrden + '<div data-precio="-' + v.precio + '" data-clases="sin" data-grupo="' + v.grupo + '" data-cantidad="' + v.cantidad + '" class="opcionItem esIngrediente ' + v.grupo + '" data-ingrediente="' + v.ingrediente + '" >' +
                                	'<label>' + v.cantidad + ' ' + v.nombre + '</label>' +
    	                            '<span>' + parseFloat(vprecio * v.cantidad).toFixed(2) + '</span>' +
    	                            '</div>';
                        		}else if(v.IDtipo == "97" || v.IDtipo == "98"){
                        			detalleOrden = detalleOrden + '<div data-precio="-' + v.precio + '" data-clases="extra" data-grupo="' + v.grupo + '" data-cantidad="' + v.cantidad + '" class="opcionItem esIngrediente ' + v.grupo + '" data-ingrediente="' + v.ingrediente + '" >' +
                                	'<label>' + v.cantidad + ' ' + v.nombre + '</label>' +
    	                            '<span>' + parseFloat(vprecio * v.cantidad).toFixed(2) + '</span>' +
    	                            '</div>';
                        			
                        		}
                        		
                        	} 
                    	});
//                        }
//            
//                $(this).next('select[name="opcionesbebidas"]').remove();
//
//            
//                    $(this).after(nuevo);
//                     $(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').append(detalleOrden);

                //$(this).closest('.fancy-select').next('.fancy-select').remove();
            	//$(this).after(nuevo);
                $(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').empty();
            	$(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').append(detalleOrden + '<br style="clear:both;" />');
                $(this).closest('.fancy-select').next('.fancy-select').remove();
                $(this).closest('.fancy-select').after(nuevo);
            	$('body').find('.papasFritas').fancySelect().trigger('update');
            	$('body').find('.bebidas').fancySelect().trigger('update');
//                $('body').find('select').fancySelect().trigger('update');
                }
         
    });
    $('input[name="monto_tarjeta"]').on('copy paste cut drag drop', function (e) {
    	   e.preventDefault();
    });
    $('input[name="monto_tarjeta"]').on('mousedown', function (e) {
    	var isRight = (e.button) ? (e.button == 2) : (e.which == 3);
        if(isRight) {
            
            return false;
        }
        return true;

    });
        $('input[name="monto_tarjeta"]').on("keyup", function(e) {
//    	if(e.keyCode != 190 || $(this).val().indexOf('.') != -1){
//    		checkIsNumber(this);	
//    	}
        	
        	if(e.keyCode != 8){
        		format(this);
        	}
    	
    	
        var tt = parseFloat($('input[name="totaltodo"]').val());
        var tc = parseFloat($(this).val());
        if($(this).val() == ''){
        	tc = 0.00;
        }
      
        if(tc <= tt){
        	 $('input[name="monto_efectivo"]').val(parseFloat(tt-tc).toFixed(2));
        }else{
        	this.value = this.value.substring(0, (this.value.length - 1));
        	this.value = parseFloat(this.value).toFixed(2);
        }
    });
    $('input[name="monto_efectivo"]').on('copy paste cut drag drop', function (e) {
 	   e.preventDefault();
 });
    function format(input){
    	 var num = input.value;
    	  if(!isNaN(num)){
    	   if(num.indexOf('.') > -1){
    	   num = num.split('.');
    	   num[0] = num[0].toString().split('').reverse().join('').replace(/(?=\d*\.?)(\d{3})/g,'$1,').split('').reverse().join('').replace(/^[\,]/,'');
    	   if(num[1].length > 2){
    	  //  alert('You may only enter two decimals!');
    	    num[1] = num[1].substring(0,num[1].length-1);
    	   } input.value = num[0]+'.'+num[1];
    	  } else {
    	   //input.value = num.toString().split('').reverse().join('').replace(/(?=\d*\.?)(\d{3})/g,'$1,').split('').reverse().join('').replace(/^[\,]/,'')
    		  }
    	  } else {
    	//   alert('You may enter only numbers in this field!');
    	   input.value = input.value.substring(0,input.value.length-1);
    	  }
    	}
    $('input[name="monto_efectivo"]').on('mousedown', function (e) {
    	var isRight = (e.button) ? (e.button == 2) : (e.which == 3);
        

        if(isRight) {
            //alert('You are prompted to type this twice for a reason!');
            return false;
        }
        return true;

 });
    $('input[name="monto_efectivo"]').on("keyup", function(e) {
//        checkIsNumber(this);
    	if(e.keyCode != 8){
    		format(this);	
    	}
        var tt = parseFloat($('input[name="totaltodo"]').val());
        var ef = parseFloat($(this).val());
        if($(this).val() == ''){
        	ef = 0.00;
        }
       
        if(ef <= tt){
        	 $('input[name="monto_tarjeta"]').val(parseFloat(tt-ef).toFixed(2));
        }else{
        	this.value = this.value.substring(0, (this.value.length - 1));
        	this.value = parseFloat(this.value).toFixed(2);
        }
    });
    
    $('input[name="no_tarjeta"]').on("keyup", function() {
    	checkIsNumber(this);
    });
    
    $('body').on('change', 'select[name="opcionesbebidas"]', function() {
        
        var params = [];
        var opcs = {};
        opcs.cod_producto = $(this).val();
        opcs.cantidad = 1;
        params.push(opcs);
        
//        toAppend = toAppend + '<div class="opcionItem esOpcion" data-grupo="' + grupo + '" data-precio="' + precio + '" data-cantidad="' + cantidad + '" data-cod_producto="' + cod_producto + '" >' +
//                            '<label>' +  + '</label>' +
//                            '<span>' +  + '</span>' +
//                            '</div>';
        var bebida = $(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').find('.bebida');
        bebida.data('grupo', $(this).data('grupo'));
        bebida.data('precio', $(this).data('precio'));
        bebida.data('cantidad', $(this).data('cantidad'));
        bebida.data('cod_producto', $(this).data('cod_producto'));
        
        bebida.find('label').text($(this).data('cantidad') + ' ' + $(this).data('nombre'));
        bebida.find('span').html($(this).data('precio'));
        
        $.each($(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').find('.esOpcion').not('.bebida'), function(k,r){
           var opcs = {};
        opcs.cod_producto = $(r).data('cod_producto');
        opcs.cantidad = $(r).data('cantidad');
         
            
            params.push(opcs);
        });
        seleccionarOpcion($(this).closest('.ordenDetallada').attr('id'),params);
    });
    $('body').on('click', '.hSuma', function() {
        var inputC = $(this).parent().find('input[name="contador"]');
        if (checkIsNumber(inputC.get(0)) && parseInt(inputC.val()) < 20) {
            agregarALaOrden($(this).closest('.producto_seleccionado'), $(this).closest('.producto_seleccionado').data('switch'));
        }
    });
    $('body').on('click', '.borrarElegido', function() {
        var inputC = $(this).closest('.producto_seleccionado').find('input[name="contador"]');
        var codP = $(this).closest('.producto_seleccionado').attr('id');
        var idAElim = $(this).closest('.ordenDetallada').attr('id');
        var seg = eliminarSingular(idAElim);
        if(seg){
        if (checkIsNumber(inputC.get(0)) && parseInt(inputC.val()) > 1) {
            inputC.val(parseInt(inputC.val()) - 1);

            var idpadre = $(this).closest('.ordenDetallada').data('idpadre');
            
            $(this).closest('.ordenDetallada').next().remove();
            $(this).closest('.ordenDetallada').next().remove();
            $(this).closest('.ordenDetallada').remove();

            var producto = $('#contenedorOrdenes').find('div#' + idpadre);
            var precioTotal = 0;
            $.each(producto.find('.precioMenu'), function(i, v) {
                precioTotal = precioTotal + parseFloat($(v).find('label').text());
            });
            producto.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));
            actTotal();
            actCorrelativos(codP);
           
        } else if (checkIsNumber(inputC.get(0)) && parseInt(inputC.val()) == 1) {
            var r = confirm("Seguro de Eliminar?");
            if (r == true) {
            	var itemDl = $(this).closest('.producto_seleccionado').data('item');
            	var claseDl = $(this).closest('.producto_seleccionado').data('clase');
            	var seg = eliminarMultiple(claseDl, itemDl);
            	if(seg){
                	$(this).closest('.producto_seleccionado').remove();
                	actTotal();
                	actCorrelativos(codP);
            	}
            }
        }
    }
    });
    $('body').on('click', '.hCerrar', function() {
        var r = confirm("Seguro de Eliminar?");
        if (r == true) {
        	var itemDl = $(this).closest('.producto_seleccionado').data('item');
        	var claseDl = $(this).closest('.producto_seleccionado').data('clase');
        	var seg = eliminarMultiple(claseDl, itemDl);
        	if(seg){
            	$(this).closest('.producto_seleccionado').remove();
            	actTotal();
        	}
        }
    });
    $('body').on('click', '.hRest', function() {
        var inputC = $(this).parent().find('input[name="contador"]');
        var codP = $(this).closest('.producto_seleccionado').attr('id');
        if (checkIsNumber(inputC.get(0)) && parseInt(inputC.val()) > 1) {
        	var idAElim = $(this).closest('.producto_seleccionado').find('.ordenDetallada').last().attr('id');
            var seg = eliminarSingular(idAElim);
            if(seg){
	            inputC.val(parseInt(inputC.val()) - 1);
	            $(this).closest('.producto_seleccionado').find('br').last().remove();
	            $(this).closest('.producto_seleccionado').find('.pedidoEspecialDetalle').last().remove();
	            $(this).closest('.producto_seleccionado').find('.ordenDetallada').last().remove();
	            var producto = $(this).closest('.producto_seleccionado');
	            var precioTotal = 0;
	            $.each(producto.find('.precioMenu'), function(i, v) {
	                precioTotal = precioTotal + parseFloat($(v).find('label').text());
	            });
	            producto.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));
	            actTotal();
	            actCorrelativos(codP);
            }
        } else if (checkIsNumber(inputC.get(0)) && parseInt(inputC.val()) == 1) {
            var r = confirm("Seguro de Eliminar?");
            if (r == true) {
            	var itemDl = $(this).closest('.producto_seleccionado').data('item');
            	var claseDl = $(this).closest('.producto_seleccionado').data('clase');
            	var seg = eliminarMultiple(claseDl, itemDl);
            	if(seg){
                	$(this).closest('.producto_seleccionado').remove();
                	actTotal();
            	}
            }
        }
    });
    $('body').on('click', '.iconoLista', function() {
    	$.modal.close();
//        var produc = $(this).closest('.producto_seleccionado');
        var idBuscar = $(this).closest('.ordenDetallada').attr('id');
//        var claseT = produc.data('clase');
//        var itemT = produc.data('item');
        var opcs = opcionesDelItem(idBuscar, false);
        var gruposOpciones = opcs.grupos;
        var cs = 0;
        $.each(gruposOpciones, function(i, v) 
        {
        	if(v.clase_opciones != 'PP'){
        		cs++;
        	}
        
        });
        if(cs > 0){
        var opcionesDiv = '<div data-buscar="' + idBuscar + '" class="modalGenerico" id="modalCajitaFeliz">';
        opcionesDiv = opcionesDiv + '<a href="javascript:void(0)" class="btnModal guardarOpciones" >Guardar</a>' +
                '<div class="headerModal">' +
                '<h1>Opciones</h1>' +
                '</div>' +
                '<div class="tituloModal" style="background: #b0b0b0; font-size: 15px;">' +
                '<h1>' + $(this).parent().find('.nombreElegido').text() + '</h1>' +
                '</div>' +
                '<div class="contenedorTablaIngredientes">' +
                '<div class="headerTablaPostres">' +
                '<ul>';
//        
//        var arrayIDs = [];
//        var arrayCant = [];
        var tablasIngredientes = "";
        
        $.each(gruposOpciones, function(i, v) 
        {
            
        
//            var dikd = v.grupo_opciones + v.clase_opciones;
//            arrayIDs.push(dikd);
//            arrayCant.push(v.cantidad);
if(v.clase_opciones != "PP"){
            opcionesDiv = opcionesDiv + '<li>' +
                    '<a class="menuModal" data-grupo="' + v.grupo_opciones + '" data-cantidad="' + v.cantidad + '" id="' + v.grupo_opciones + v.clase_opciones + '" href="javascript:void(0)">' +
                    '<img src="'+v.imagen+'"/>' +
                    '<span>' + v.nombre + '</span>' +
                    '</a>' +
                    '</li>';
            tablasIngredientes = tablasIngredientes + '<div style="display:none;" id="' + v.grupo_opciones + v.clase_opciones + '" class="tablaIngredientes">';
            var datosOpciones = opcs.opcionDatos;
            $.each(datosOpciones, function(j, w) {
                if(v.clase_opciones == w.clase_opciones){
                    tablasIngredientes = tablasIngredientes + '<div data-grupo="' + w.grupo_opciones + '" data-nombre="' + w.nombre + '" data-precio="' + w.precio + '" data-cod_producto="' + w.cod_producto + '" class="ingrediente">' +
                        '<div class="cntImg">' +
                        '<img class="imgProdModal" src="' + w.imagen + '"/>' +
                        '</div>' +
                        '<label>' + w.nombre + '</label>' +
                        '<div class="contenedorAcciones">' +
                        '<a href="javascript:void(0)" class="add addOpcionesItem">' +
                        '<img src="media/imagenes/tomadoraOrdenes/modalMas.png"/>' +
                        '</a>' +
                        '<a href="javascript:void(0)" class="remove removeOpcionesItem">' +
                        '<img src="media/imagenes/tomadoraOrdenes/modalMenos.png"/>' +
                        '</a>' +
                        '</div>';
                    }
                    if(v.clase_opciones == w.clase_opciones && w.elegido){
                        tablasIngredientes = tablasIngredientes + '<input value="' + w.cantidad + '" type="text" id="ingredienteDefault" class="contadorExtras" readonly/></div>';
                    }else if(v.clase_opciones == w.clase_opciones){
                        tablasIngredientes = tablasIngredientes + '<input value="0" type="text" class="contadorExtras" readonly/></div>';
                    }
                });
                 tablasIngredientes = tablasIngredientes + '</div>';
             }
        });
        opcionesDiv = opcionesDiv + '</ul></div>';
        opcionesDiv = opcionesDiv + tablasIngredientes;
        
//        var c = 0;
//        
//
//            opcionesDiv = opcionesDiv + '';
//            $.each(v, function(s, w) {
//                
//                if (w.default == "S") {
//                    opcionesDiv = opcionesDiv + '<input value="' + arrayCant[c] + '" type="text" id="ingredienteDefault" class="contadorExtras" readonly/>';
//                } else {
//                    opcionesDiv = opcionesDiv + '<input value="0" type="text" class="contadorExtras" readonly/>';
//                }
//                opcionesDiv = opcionesDiv + '</div>';
//            });
//            opcionesDiv = opcionesDiv + '</div>';
//            c++;
//        });


        opcionesDiv = opcionesDiv + '</div>' +
                '<a href="javascript:void(0)" class="btnModal guardarOpciones" >Guardar</a>' +
                '</div>';
        $('body').find('.modalGenerico').remove();
        $('body').append(opcionesDiv);


        var opcionesYaSeleccionados = [];
        
        $.each($(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').find('.esOpcion'), function(k, s) {
        	var opcionY = {};
            opcionY.grupo = $(s).data('grupo');
            opcionY.cantidad = $(s).data('cantidad');
            opcionY.cod_producto = $(s).data('cod_producto');
            opcionesYaSeleccionados.push(opcionY);
        });

        $.each($('body').find('#modalCajitaFeliz').find('.ingrediente'), function(i, w) {
            var opcionItem = $(w);
            if (opcionesYaSeleccionados.length > 0) {
                
                $.each(opcionesYaSeleccionados, function(i, v) {
                    if (opcionItem.data('grupo') == v.grupo && v.cod_producto == opcionItem.data('cod_producto')) {
                        opcionItem.find('.contadorExtras').val(v.cantidad);
                        return false;
                    }else{
                    	
                        	opcionItem.find('.contadorExtras').val(0);
                        
                    }
                });
            }
        });


        $('body').find('#modalCajitaFeliz').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            containerId: "contenedorModalCajita",
            appendTo: "#content .ladoDerecho",
            position: [50, 25],
            escClose: false,
          //  autoPosition: true
        });
        $('body').find('#contenedorModalCajita').find('.headerTablaPostres').find('li').first().addClass('menuModalActivo');
        $('body').find('#contenedorModalCajita').find('div#' + $('body').find('#contenedorModalCajita').find('.headerTablaPostres').find('li').first().find('a').attr('id')).fadeIn();
    
        }else{
            alert('no hay opciones para este item');
        }
    });
    $('body').on('click', '.removeOpcionesItem', function() {
        var ingClick = $(this).closest('.ingrediente');
        var cantidadMaxima = $('a#' + $(this).closest('.tablaIngredientes').attr('id')).data('cantidad');
        var contadorItemReceta = $(this).closest('.ingrediente').find('.contadorExtras').val();
        var cantidadQueTieneElDefault = parseInt($(this).closest('.tablaIngredientes').find('#ingredienteDefault').val());
        var idDelClick = $(this).closest('.ingrediente').find('.contadorExtras').attr('id');
        var ingredientesSimilares = $(this).closest('.tablaIngredientes').find('.ingrediente');
//        var suma = 0;
//        $.each(ingredientesSimilares, function(is,ks){
//            var itCantidad = parseInt($(ks).find('.contadorExtras').val());
//            suma = suma + itCantidad;
//        });
        if (contadorItemReceta > 0) {
            $(this).closest('.ingrediente').find('.contadorExtras').val(parseInt(contadorItemReceta) - 1);
            var suma = 0;
            $.each(ingredientesSimilares, function(is, ks) {
                var itCantidad = parseInt($(ks).find('.contadorExtras').val());
                suma = suma + itCantidad;
            });
//            if(suma != cantidadMaxima){
//            if(idDelClick != 'ingredienteDefault'){
//                if(cantidadQueTieneElDefault != cantidadMaxima){
//                    $(this).closest('.tablaIngredientes').find('#ingredienteDefault').val(cantidadQueTieneElDefault + 1);
//                }
////                else{
////                    $.each(ingredientesSimilares, function(l, o){
////                       if($(o).data('opcion') != ingClick.data('opcion')){
////                           var itCantidad = parseInt($(o).find('.contadorExtras').val());
////                           if( itCantidad > 0){
////                               $(o).find('.contadorExtras').val(itCantidad - 1);
////                               return false;
////                           }
////                       }
////                    });
////                }
//            }
////            else{
////                $.each(ingredientesSimilares, function(l, o){
////                       if($(o).data('opcion') != ingClick.data('opcion')){
////                           var itCantidad = parseInt($(o).find('.contadorExtras').val());
////                           if( itCantidad > 0){
////                               $(o).find('.contadorExtras').val(itCantidad - 1);
////                               return false;
////                           }
////                       }
////                    });
////            }
//        }
//            $(this).closest('.ingrediente').find('.contadorExtras').val(parseInt(contadorItemReceta) - 1);
        }
    });
    $('body').on('click', '.addOpcionesItem', function() {
        var ingClick = $(this).closest('.ingrediente');
        var cantidadMaxima = $('a#' + $(this).closest('.tablaIngredientes').attr('id')).data('cantidad');
        var contadorItemReceta = $(this).closest('.ingrediente').find('.contadorExtras').val();
        var cantidadQueTieneElDefault = parseInt($(this).closest('.tablaIngredientes').find('#ingredienteDefault').val());
        var idDelClick = $(this).closest('.ingrediente').find('.contadorExtras').attr('id');
        var ingredientesSimilares = $(this).closest('.tablaIngredientes').find('.ingrediente');
        var suma = 0;
        $.each(ingredientesSimilares, function(is, ks) {
            var itCantidad = parseInt($(ks).find('.contadorExtras').val());
            suma = suma + itCantidad;
        });
        if (suma != cantidadMaxima) {
//        if(contadorItemReceta < cantidadMaxima){
//            if(suma == cantidadMaxima){
//            if(idDelClick != 'ingredienteDefault'){
//                if(cantidadQueTieneElDefault != 0){
//                    $(this).closest('.tablaIngredientes').find('#ingredienteDefault').val(cantidadQueTieneElDefault - 1);
//                }else{
//                    $.each(ingredientesSimilares, function(l, o){
//                       if($(o).data('opcion') != ingClick.data('opcion')){
//                           var itCantidad = parseInt($(o).find('.contadorExtras').val());
//                           if( itCantidad > 0){
//                               $(o).find('.contadorExtras').val(itCantidad - 1);
//                               return false;
//                           }
//                       }
//                    });
//                }
//            }else{
//                $.each(ingredientesSimilares, function(l, o){
//                       if($(o).data('opcion') != ingClick.data('opcion')){
//                           var itCantidad = parseInt($(o).find('.contadorExtras').val());
//                           if( itCantidad > 0){
//                               $(o).find('.contadorExtras').val(itCantidad - 1);
//                               return false;
//                           }
//                       }
//                    });
//            }
//        }
            $(this).closest('.ingrediente').find('.contadorExtras').val(parseInt(contadorItemReceta) + 1);

        }
    });
    $('body').on('click', '.menuModal', function() {
        $('body').find('.tablaIngredientes').fadeOut();
        var idTabla = $(this).attr('id');
        $('body').find('.menuModalActivo').removeClass('menuModalActivo');
        $(this).closest('li').addClass('menuModalActivo');
        $('body').find('.tablaIngredientes').filter('#' + idTabla).fadeIn();
    });
    $('body').on('click', '.menuReceta', function() {
        $('body').find('.tablaIngredientes').fadeOut();
        var idTabla = $(this).attr('id');
        $(this).closest('.tipoDeOpciones').find('.menuModalActivo').removeClass('menuModalActivo');
        $(this).closest('li').addClass('menuModalActivo');
        $('body').find('.tablaIngredientes').filter('#' + idTabla).fadeIn();
    });
    
    
    //NUEVO
    $('body').on('click', '.menuComboReceta', function(){
        $('body').find('.tipoDeOpciones').fadeOut();
        $('body').find('div#tipoDeOpciones'+$(this).attr('id')).fadeIn();
        $('body').find('div#tipoDeOpciones'+$(this).attr('id')).find('li').first().addClass('menuModalActivo');
        var idB = $('body').find('div#tipoDeOpciones'+$(this).attr('id')).find('li').first().find('a').attr('id');
        $('body').find('.tablaIngredientes').fadeOut();
         $(this).closest('.headerTablaPostres').find('.menuModalActivo').removeClass('menuModalActivo');
        $(this).closest('li').addClass('menuModalActivo');
        $('body').find('div#'+idB).fadeIn();
    });
    //CAMBIO
    $('body').on('click', '.iconoReceta', function() {
    	$.modal.close();
        var ingredientesYaSeleccionados = [];
        var adicionalesYaSeleccionados = [];
        $.each($(this).closest('.ordenDetallada').next('.pedidoEspecialDetalle').find('.esIngrediente'), function(k, s) {
            if ($(s).hasClass('receta')) {
                var ingredienteY = {};
                ingredienteY.clase = $(s).data('clases');
                ingredienteY.cantidad = $(s).data('cantidad');
                ingredienteY.ingrediente = $(s).data('ingrediente');
                ingredientesYaSeleccionados.push(ingredienteY);
            } else if ($(s).hasClass('adicional')) {
                var adicionalY = {};
                adicionalY.clase = $(s).data('clases');
                adicionalY.cantidad = $(s).data('cantidad');
                adicionalY.ingrediente = $(s).data('ingrediente');
                adicionalesYaSeleccionados.push(adicionalY);
            }
        });
        var idBuscar = $(this).closest('.ordenDetallada').attr('id');
        var itemsre = receta(idBuscar, true);
        
        
        
            var opcionesDiv = '<div data-buscar="' + idBuscar + '" class="modalGenerico" id="modalCajitaFeliz">';
        opcionesDiv = opcionesDiv + '<a href="javascript:void(0)" class="btnModal guardarReceta" >Guardar</a>' +
                '<div class="headerModal">' +
                '<h1>Receta</h1>' +
                '</div>' +
                '<div class="tituloModal" style="background: #b0b0b0; font-size: 15px;">' +
                '<h1>' + $(this).parent().find('.nombreElegido').text() + '</h1>' +
                '</div>'+
                '<div class="contenedorTablaIngredientes">' ;
        $('body').find('.modalGenerico').remove();
        $('body').append(opcionesDiv + itemsre + '</div>');
        
        if($('body').find('#menuCombos').length > 0){
            var idBuscarOpciones = $('body').find('#menuCombos').find('li').first().find('a').attr('id');
            $('body').find('#menuCombos').find('li').first().addClass('menuModalActivo');
            $('body').find('div#tipoDeOpciones'+idBuscarOpciones).fadeIn();
        }else{
            $('body').find('div#tipoDeOpciones').fadeIn();
        }
        var idBuscarTabla = "";
        if($('body').find('.tipoDeOpciones').length > 0){
            idBuscarTabla = $('body').find('.tipoDeOpciones').first().find('li').first().find('a').attr('id');
            $('body').find('.tipoDeOpciones').find('li').first().addClass('menuModalActivo');
        }
        $('body').find('div#'+idBuscarTabla).fadeIn();
        
        $.each($('body').find('#modalCajitaFeliz').find('.ingrediente'), function(i, v) {
            var ingredienteDeReceta = $(v);
            if (ingredientesYaSeleccionados.length > 0) {
                $.each(ingredientesYaSeleccionados, function(i, v) {
                    if (ingredienteDeReceta.data('grupo') == "receta") {
                        if (v.ingrediente == ingredienteDeReceta.data('ingrediente')) {
                            ingredienteDeReceta.addClass(v.clase);
                            if (ingredienteDeReceta.data('rcantidad') == "S") {
                                ingredienteDeReceta.find('.contadorExtras').val(v.cantidad);
                            }
                        }

                    }
                });
            }

            if (adicionalesYaSeleccionados.length > 0) {
                $.each(adicionalesYaSeleccionados, function(i, v) {
                    if (ingredienteDeReceta.data('grupo') == "adicional") {
                        if (v.ingrediente == ingredienteDeReceta.data('ingrediente')) {
                            ingredienteDeReceta.addClass(v.clase);
                            if (ingredienteDeReceta.data('rcantidad') == "S") {
                                ingredienteDeReceta.find('.contadorExtras').val(v.cantidad);
                            }
                        }

                    }
                });
            }
        });


        $('body').find('#modalCajitaFeliz').modal({
            overlayId: "overlayTomadora",
            overlayCss: {position: "absolute", width: "100%", height: "100%", backgroundColor: "#000"},
            containerId: "contenedorModalCajita",
            appendTo: "#content .ladoDerecho",
            position: [50, 25],
            escClose: false,
          //  autoPosition: true
        });
//        $('body').find('#contenedorModalCajita').find('.headerTablaPostres').find('li').first().addClass('menuModalActivo');
//        $('body').find('#contenedorModalCajita').find('div#' + abrir).fadeIn();
    });
    $('body').on('click', '.removeRecetaAdicional', function() {
        var reqCantidad = $(this).closest('.ingrediente').data('rcantidad');
        if (reqCantidad == 'S') {
            var contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
            if (contadorItemReceta > 0) {
                $(this).closest('.ingrediente').find('.contadorExtras').val(contadorItemReceta - 1);
                contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
                if (contadorItemReceta == 0) {
                    $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
                } else if (contadorItemReceta > 0) {
                    $(this).closest('.ingrediente').removeClass('sin').removeClass('extra').addClass('extra');
                }
            }
        } else {
            if ($(this).closest('.ingrediente').hasClass('extra')) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
            }
        }
    });
    $('body').on('click', '.removeReceta', function() {
        var reqCantidad = $(this).closest('.ingrediente').data('rcantidad');
        if (reqCantidad == 'S') {
            var contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
            if (contadorItemReceta > 0) {
                $(this).closest('.ingrediente').find('.contadorExtras').val(contadorItemReceta - 1);
                contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
                if (contadorItemReceta == 0) {
                    $(this).closest('.ingrediente').removeClass('sin').removeClass('extra').addClass('sin');
                } else if (contadorItemReceta == 1) {
                    $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
                }
            }
        } else {
            if (!$(this).closest('.ingrediente').hasClass('extra') && !$(this).closest('.ingrediente').hasClass('sin')) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra').addClass('sin');
            } else if ($(this).closest('.ingrediente').hasClass('extra')) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
            }
        }
    });
    $('body').on('click', '.addReceta', function() {
        var reqCantidad = $(this).closest('.ingrediente').data('rcantidad');
        if (reqCantidad == 'S') {
            var contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
            $(this).closest('.ingrediente').find('.contadorExtras').val(contadorItemReceta + 1);
            contadorItemReceta = parseInt($(this).closest('.ingrediente').find('.contadorExtras').val());
            if (contadorItemReceta > 1) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra').addClass('extra');
            } else if (contadorItemReceta == 1) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
            }
        } else {
            if ($(this).closest('.ingrediente').hasClass('sin')) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra');
            } else if (!$(this).closest('.ingrediente').hasClass('extra') && !$(this).closest('.ingrediente').hasClass('sin')) {
                $(this).closest('.ingrediente').removeClass('sin').removeClass('extra').addClass('extra');
            }
        }
    });
    $('body').on('click', '.sprite', function() {
        pa = parseInt($(this).data('pagina'));
        $('.sprite.activo').removeClass('activo');
        $(this).addClass('activo');
        var tipo = '1';
        if ($('#switch').hasClass('on')){
        	tipo = '1';
        }else{
        	tipo = '2';
        }
        slide(pa, tipo);
    });
    $('body').on('click', '.guardarReceta', function() {
        var toAppend = '';
        var params = [];
        var idBuscar = $(this).closest('#modalCajitaFeliz').data('buscar');
        $.each($(this).parent().find('.ingrediente'), function(i, v) {
            var reqCantidad = $(v).data('rcantidad');
            if (reqCantidad == 'S') {

                var precio = parseFloat($(v).data('precio'));
                var nombre = $(v).data('nombre');
                var ingrediente = $(v).data('ingrediente');
                var cantidad = 0;
                if(parseInt($(v).find('.contadorExtras').val()) > 0){
                	var cantidad = parseInt($(v).find('.contadorExtras').val()) - 1;	
                }
                
                var grupo = $(v).data('grupo');
                
                
                
                if ($(v).hasClass('extra')) {
                    toAppend = toAppend + '<div data-clases="extra" data-precio="' + precio + '" data-grupo="' + grupo + '" data-cantidad="' + cantidad + '" class="opcionItem esIngrediente ' + grupo + '" data-ingrediente="' + ingrediente + '" >' +
                            '<label>' + cantidad + ' ' + nombre + '</label>' +
                            '<span>' + parseFloat(precio * cantidad).toFixed(2) + '</span>' +
                            '</div>';
                    var opcs = {};
                    opcs.cod_producto = ingrediente;
                opcs.cantidad = cantidad;
                   opcs.especial = true;
                } else if ($(v).hasClass('sin')) {
                    toAppend = toAppend + '<div data-precio="-' + precio + '" data-clases="sin" data-grupo="' + grupo + '" data-cantidad="' + cantidad + '" class="opcionItem esIngrediente ' + grupo + '" data-ingrediente="' + ingrediente + '" >' +
                            '<label>' + cantidad + ' ' + nombre + '</label>' +
                            '<span>' + parseFloat(precio * cantidad).toFixed(2) + '</span>' +
                            '</div>';
                    var opcs = {};
                    opcs.cod_producto = ingrediente;
                    opcs.cantidad = cantidad;
                    opcs.especial = false;
                }
                        /*agregar la cantidad y el id del ingrediente, la opcion, off y on*/

//                       
//                        opcs.cantidad;
//                        opcs.cantidad = 0;
                        if(opcs != undefined && opcs != 'undefined'){
                            params.push(opcs);
                        }
            } else {
                var precio = parseFloat($(v).data('precio'));
                var nombre = $(v).data('nombre');
                var ingrediente = $(v).data('ingrediente');
                var grupo = $(v).data('grupo');
                
                
                
                if ($(v).hasClass('extra')) {
                   
                    toAppend = toAppend + '<div data-precio="' + precio + '"  data-clases="extra" data-cantidad="1" class="opcionItem esIngrediente ' + grupo + '" data-grupo="' + grupo + '" data-ingrediente="' + ingrediente + '" >' +
                            '<label> Extra ' + nombre + '</label>' +
                            '<span>' + precio.toFixed(2) + '</span>' +
                            '</div>';
                     var opcs = {};
                    opcs.cod_producto = ingrediente;
                    opcs.cantidad = 0;
                    opcs.especial = true;
                } else if ($(v).hasClass('sin')) {
                    
                    toAppend = toAppend + '<div data-precio="-' + precio + '" data-clases="sin" data-grupo="' + grupo + '" data-cantidad="0" class="opcionItem esIngrediente ' + grupo + '" data-grupo="' + grupo + '" data-ingrediente="' + ingrediente + '" >' +
                            '<label> Sin ' + nombre + '</label>' +
                            '<span>' + parseFloat(precio * 0).toFixed(2) + '</span>' +
                            '</div>';
                    var opcs = {};
                    opcs.cod_producto = ingrediente;
                    opcs.cantidad = 0;
                    opcs.especial = false;
                }
                        if(opcs != undefined && opcs != 'undefined'){
                            params.push(opcs);
                        }
            }

            
        });
        if(params.length > 0){
            seleccionarReceta(idBuscar,params);
            $('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente, br').remove();
            $('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').append(toAppend + '<br style="clear:both;" />');
        }else{
        	if($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esOpcion').length > 0){
        		$('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente').remove();
    		}else{
    			$('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente, br').remove();
    		}
        	
//            $('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').append(toAppend);
        }
        


        var totalO = 0;
        $.each($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esOpcion'), function(e, v) {
            var precio = parseFloat($(v).data('precio'));
            var cant = parseFloat($(v).data('cantidad'));
            totalO = totalO + (precio * cant);
        });

        var totalI = 0;
        $.each($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente'), function(e, v) {
            var precio = parseFloat($(v).data('precio'));
            var cant = parseFloat($(v).data('cantidad'));
            console.log(precio);
            totalI = totalI + (precio * cant);
        });

        var totalAnt = parseFloat($('#contenedorOrdenes').find('#' + idBuscar).find('select[name="opcionestamanio"]').find('option:selected').data('precio'));
        var tto = totalI + totalAnt + totalO;
        $('#contenedorOrdenes').find('#' + idBuscar).find('.precioMenu').find('label').text(tto.toFixed(2));

        var producto = $('#contenedorOrdenes').find('#' + idBuscar).closest('.producto_seleccionado');
        var precioTotal = 0;
        $.each(producto.find('.precioMenu'), function(i, v) {
            precioTotal = precioTotal + parseFloat($(v).find('label').text());
        });
        producto.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));

        actTotal();

        $.modal.close();
    });
    $('body').on('click', '.guardarOpciones', function() {
        var pasar = 0;
        var totalDeMenus = $(this).closest('#modalCajitaFeliz').find('.menuModal').length;
        var idBuscar = $(this).closest('#modalCajitaFeliz').data('buscar');
        $.each($(this).closest('#modalCajitaFeliz').find('.menuModal'), function(js, vs) {
            var cant = $(vs).data('cantidad');
            var idMenu = $(vs).attr('id');
            var totalSumado = 0;
            $.each($('body').find('div#' + idMenu).find('.ingrediente'), function(os, ws) {
                if (parseInt($(ws).find('.contadorExtras').val()) > 0) {
                    totalSumado += parseInt($(ws).find('.contadorExtras').val());
                }
            });
            if (totalSumado == cant) {
                pasar++;
            }
        });
        if (pasar == totalDeMenus) {
            var toAppend = '';
            var params = [];
            $.each($(this).parent().find('.ingrediente'), function(i, v) {
                if (parseInt($(v).find('.contadorExtras').val()) > 0) {
                     
                    var precio = $(v).data('precio');
                    var nombre = $(v).data('nombre');
                    var cod_producto = $(v).data('cod_producto');
                    var grupo = $(v).data('grupo');
                    var cantidad = parseInt($(v).find('.contadorExtras').val());
                    
                    var opcs = {};
                    opcs.cod_producto = cod_producto;
                    opcs.cantidad = cantidad;
                    params.push(opcs);
                    toAppend = toAppend + '<div class="opcionItem esOpcion" data-grupo="' + grupo + '" data-precio="' + precio + '" data-cantidad="' + cantidad + '" data-cod_producto="' + cod_producto + '" >' +
                            '<label>' + cantidad + ' ' + nombre + '</label>' +
                            '<span>' + parseFloat(precio).toFixed(2) + '</span>' +
                            '</div>';
                }
            });
            seleccionarOpcion(idBuscar,params);
            if($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente').length > 0){
            	$('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esOpcion').not('.bebida').remove();
                $('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').append(toAppend + '<br style="clear:both" />');
                
        		
    		}else{
    			
    			$('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esOpcion, br').not('.bebida').remove();
                $('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').append(toAppend + '<br style="clear:both" />');
                
    			
    		}
            

            var totalO = 0;
            $.each($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esOpcion'), function(e, v) {
                var precio = parseFloat($(v).data('precio'));
                var cant = parseFloat($(v).data('cantidad'));
                totalO = totalO + (precio * cant);
            });

            var totalI = 0;
            $.each($('#contenedorOrdenes').find('#' + idBuscar).next('.pedidoEspecialDetalle').find('.esIngrediente'), function(e, v) {
                var precio = parseFloat($(v).data('precio'));
                var cant = parseFloat($(v).data('cantidad'));
                console.log(precio);
                totalI = totalI + (precio * cant);
            });

            var totalAnt = parseFloat($('#contenedorOrdenes').find('#' + idBuscar).find('select[name="opcionestamanio"]').find('option:selected').data('precio'));
            var tss = totalI + totalAnt + totalO;
            $('#contenedorOrdenes').find('#' + idBuscar).find('.precioMenu').find('label').text(tss.toFixed(2));

            var producto = $('#contenedorOrdenes').find('#' + idBuscar).closest('.producto_seleccionado');
            var precioTotal = 0;
            $.each(producto.find('.precioMenu'), function(i, v) {
                precioTotal = precioTotal + parseFloat($(v).find('label').text());
            });
            producto.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));

            actTotal();



            $.modal.close();
        }
        else {
            alert('Faltan de seleccionar items');
        }
    });

    /**
     * 
     * FUNCTIONS
     * 
     * */
    function seleccionarOpcion(cod_u, arreglopcs){
        
        var codi = "6767";
        var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&opciones=" + JSON.stringify(arreglopcs) + "&codU="+cod_u+"&cod=" + codi,
            async: false,
            dataType: "json",
            success: function(r) {
                var x = JSON.stringify(r.orden);
                console.log(x);
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function seleccionarReceta(cod_u, arreglorec){
    	var cod_pedido = $('input[name=cod_pedido]').val();
        var codi = "4646";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&ingredientes=" + JSON.stringify(arreglorec) + "&codU="+cod_u+"&cod=" + codi,
            async: false,
            dataType: "json",
            success: function(r) {
                var x = JSON.stringify(r.orden);
                console.log(x);
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function cambiarTamano(cod_u, cod_ta) {
        var codi = "9797";
        var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&prod=" + cod_u + "&cod=" + codi + "&tam=" + cod_ta,
            async: false,
            dataType: "json",
            success: function(r) {
                var x = JSON.stringify(r.orden);
                console.log(x);
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function actCorrelativos(codP) {
        var ci = 1;
        $.each($('#contenedorOrdenes').find('div#' + codP).find('.ordenDetallada'), function(i, v) {
            $(v).attr('id', codP + 'P' + ci);
            ci++;
        });
    }
    function cliente(clienteID) {
        opCod = '1099';
        $.ajax({
            url: "Loader",
            type: "POST",
            data: 'cliente=' + clienteID + '&cod=' + opCod,
            async: false,
            success: function(response) {

                if (response.r == "1") {
                    clienteR = $.parseJSON(response.data);
                    $('#telCliente').text(clienteR.c_tel);
                    $('#nomCliente').text(clienteR.c_nombre + ' ' + clienteR.c_ape);
                    codP = '334';
                    $.ajax({
                        url: "Loader",
                        type: "POST",
                        data: 'cliente=' + clienteID + '&cod=' + codP,
                        async: false,
                        success: function(response) {
                            if (response.r == "1") {
                                tipoC = $.parseJSON(response.data);
                                $('#tipoC').html(tipoC.tipo);
                            }
                        },
                        error: function(a, b, c) {
                            alert(a.responseText);
                        },
                        dataType: "json"
                    });
                }
            },
            error: function(a, b, c) {
                alert(a.responseText);
            },
            dataType: "json"
        });

    }
    function direccion(dir, clienteID) {
        opCod = '9898';
        $
                .ajax({
                    url: "Loader",
                    type: "POST",
                    data: 'dir=' + dir + '&cod=' + opCod
                            + '&cliente=' + clienteID,
                    async: false,
                    success: function(response) {
                        // r = JSON.parse(r);
                        if (response.r == "1") {

                            dir = $.parseJSON(response.data);

                            $('#dirCliente').text(dir.direccion);

//                            infoRestaurante(dir.asignado);
                        }
                    },
                    error: function(a, b, c) {
                        alert(a.responseText);
                    },
                    dataType: "json"
                });
    }
    function infoRestaurante(tienda) {
        ra = tienda;
        opCod = "1992";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "ra=" + ra + "&cod=" + opCod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {

                    $('#restaurante').text(r.nombre);
                    $('.minutos').text(r.tiempo);

                }
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function clases(hr) {
        var cod = 4334;
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "horario=" + hr + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    var clasess = $.parseJSON(r.data);
//                    $('#clases').empty();
//                    $('#contenedorIconos').empty();
//                    $.each(clasess, function(id, va) {
//                            $('#clases').append('<li><a data-clase="' + va.no_clase + '" href="javascript:void(0)">' + va.nombre + '</a></li>');
//                            $('#contenedorIconos').append('<div class="icono"><img src="' + va.imagen + '"/></div>');
//                    });
                    
                    var sd = 1;
                    $('#clases').empty();
                    $('#contenedorIconos').empty();
                    $.each(clasess, function(id, va) {
                        if (sd < 7) {
                            
                                $('#clases').append('<li><a  data-clase="' + va.no_clase + '" href="javascript:void(0)">' + va.nombre + '</a></li>');
                           
                            $('#contenedorIconos').append('<div class="icono"><img src="' + va.imagen + '"/></div>');

                        }
                        sd++;
                    });
                    
                    $('#clases').find('li').first().addClass('activo');
                    if ($('#clases').find('.activo').length > 0) {
                    	var tipo = '1';
                        if ($('#switch').hasClass('on')){
                        	tipo = '1';
                        }else{
                        	tipo = '2';
                        }
                        items($('#clases').find('.activo').find('a').data('clase'), tipo);
                    }
                }
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    var itemsGlobal;
    function items(cl, p) {
        var cod = 8745;
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "clase=" + cl + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    var items = $.parseJSON(r.data);
                    itemsGlobal = items;
                    var cont = itemsGlobal.length;
                    var cm = 16;
                    var cpp = cont / cm;
                    var paginas = Math.ceil(cpp);
                    var sHtml = '';
                    for (var t = 1; t <= paginas; t++) {
                        if (t === 1) {
                            sHtml = sHtml + '<a class="sprite activo" data-pagina="' + t + '" href="javascript:void(0)"></a> ';
                        } else {
                            sHtml = sHtml + '<a class="sprite" data-pagina="' + t + '" href="javascript:void(0)"></a> ';
                        }
                    }

                    slide(1, p);

                    $('.paginadorProductos').fadeOut().html(sHtml).fadeIn();
                }
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function cargarNuevoALaOrden(crr, clase, item, esswitch) {
    	var cod_pedido = $('input[name=cod_pedido]').val();
    	
        var codi = "9696";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&clase=" + clase + "&item=" + item + "&prod=" + crr + "&cod=" + codi + "&alaCarta=" + esswitch,
            async: false,
            dataType: "json",
            success: function(r) {
                console.log(r);
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
    }
    function agregarALaOrden(producto, switchState) {

        if ($('#' + $(producto).data('clase') + $(producto).data('item') + '').length > 0) {
            //SI EXISTE UN PRODUCTO AGREGADO A LA ORDEN
            var productoExiste = $('#' + $(producto).data('clase') + $(producto).data('item') + '');
            var contadorProductos = $('#' + $(producto).data('clase') + $(producto).data('item') + '').find('.ordenDetallada').length + 1;

            var crr = $(producto).data('clase') + "" + $(producto).data('item') + "P" + contadorProductos;
            cargarNuevoALaOrden(crr, $(producto).data('clase'), $(producto).data('item'), switchState);

            var ordenNueva = '<div data-idpadre="' + $(producto).data('clase') + '' + $(producto).data('item') + '" id="' + crr + '" class="ordenDetallada">' +
                    '<div class="menuElegido">' +
                    '<a href="javascript:void(0)" class="borrarElegido">' +
                    '</a>' +
                    '<label class="nombreElegido">' + productoExiste.data('nombre') + '</label>';

            if (switchState) {
                //SI EL PRODUCTO PUEDE SER A LA CARTA
                ordenNueva = ordenNueva + '<select class="papasFritas" name="opcionestamanio">';
                ordenNueva = ordenNueva + '<option selected="selected" data-cod_producto="' + $(producto).data('cod_producto') + '" data-precio="' + $(producto).data('precio_carta') + '" value="' + $(producto).data('cod_producto') + '">A la carta</option>';
                if ($(producto).data('tipo') == "3") {
                    //SI EL PRODUCTO PUEDE SER MENU TAMBIEN
                    var lostamanios = tamanios($(producto).data('clase'), $(producto).data('item'), crr, switchState);
                    var defaultv = '';
                    if (lostamanios.length > 0) {
                        $.each(lostamanios, function(indes, vals) {
                            if (vals.def === "S") {
                                defaultv = vals.tamano;
                            }
                            ordenNueva = ordenNueva + '<option data-cod_producto="' + vals.cod_producto + '" data-precio="' + vals.precio + '" value="' + vals.cod_producto + '">' + vals.desc + '</option>';
                        });
                    }

                }
                ordenNueva = ordenNueva + '</select>';
                var opcs = opcionesDelItem(crr,true);
                    var gruposOpciones = opcs.grupos;
                    var en = 0;
                        var detalleOrden = "";
                        $.each(gruposOpciones, function(i, v) {
                            en++;
                            if(v.clase_opciones === "PP"){
                                ordenNueva = ordenNueva + '<select name="opcionesbebidas" class="bebidas">';
                            }
                                var datosOpciones = opcs.opcionDatos;
                                
                                $.each(datosOpciones, function(j, w) {
                                    if(v.clase_opciones == w.clase_opciones && w.elegido){
                                        if(v.clase_opciones != "PP"){
                                            detalleOrden = detalleOrden + '<div class="opcionItem esOpcion" data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" >' ;
                                        }
                            detalleOrden = detalleOrden + '<label>' + w.cantidad + ' ' + w.nombre + '</label>' +
                            '<span>' + parseFloat(w.precio).toFixed(2) + '</span>' +
                            '</div>';
                                    }
                                    if (v.clase_opciones == "PP" && w.clase_opciones == "PP" && w.elegido) {
                                ordenNueva = ordenNueva + '<option data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" data-nombre="' + w.nombre + '" selected="selected" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            } else if(v.clase_opciones == "PP" && w.clase_opciones == "PP") {
                                ordenNueva = ordenNueva + '<option data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" data-nombre="' + w.nombre + '" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            }
                                });
                                if(v.clase_opciones === "PP"){
                                ordenNueva = ordenNueva + '</select>';
                            }
                            });
                        
                
            }
            else {
                //SI EL PRODUCTO PUEDE SER MENU
                ordenNueva = ordenNueva + '<select class="papasFritas" name="opcionestamanio">';
                if ($(producto).data('tipo') == "3") {
                    var lostamanios = tamanios($(producto).data('clase'), $(producto).data('item'), crr, true);


                    //SI EL PRODUCTO PUEDE SER A LA CARTA PERO NO ES EL DEFAULT
                    ordenNueva = ordenNueva + '<option data-cod_producto="' + $(producto).data('cod_producto') + '" data-precio="' + $(producto).data('precio_carta') + '" value="' + $(producto).data('cod_producto') + '">A la carta</option>';
                } else {
                    var lostamanios = tamanios($(producto).data('clase'), $(producto).data('item'), crr, switchState);
                }
                
                if (lostamanios.length > 0) {
                    $.each(lostamanios, function(indes, vals) {
                        if (vals.def === "S") {
                            ordenNueva = ordenNueva + '<option data-cod_producto="' + vals.cod_producto + '" selected="selected"  data-precio="' + vals.precio + '" value="' + vals.tamano + '">' + vals.desc + '</option>';
                        } else {
                            ordenNueva = ordenNueva + '<option data-cod_producto="' + vals.cod_producto + '" data-precio="' + vals.precio + '" value="' + vals.tamano + '">' + vals.desc + '</option>';
                        }

                    });

                }
                ordenNueva = ordenNueva + '</select>';
                
                    var opcs = opcionesDelItem(crr,true);
                    var gruposOpciones = opcs.grupos;
                    
                        var detalleOrden = "";
                        $.each(gruposOpciones, function(i, v) {
                            if(v.clase_opciones === "PP"){
                                ordenNueva = ordenNueva + '<select name="opcionesbebidas" class="bebidas">';
                            }
                                var datosOpciones = opcs.opcionDatos;
                                
                                $.each(datosOpciones, function(j, w) {
                                    if(v.clase_opciones != "PP" && v.clase_opciones == w.clase_opciones && w.elegido){
                                    detalleOrden = detalleOrden + '<div class="opcionItem esOpcion" data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" >' +
                            '<label>' + w.cantidad + ' ' + w.nombre + '</label>' +
                            '<span>' + parseFloat(w.precio).toFixed(2) + '</span>' +
                            '</div>';
                                    }
                                    if (v.clase_opciones == "PP" && w.clase_opciones == "PP" && w.elegido) {
                                ordenNueva = ordenNueva + '<option selected="selected" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            } else if(v.clase_opciones == "PP" && w.clase_opciones == "PP") {
                                ordenNueva = ordenNueva + '<option value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            }
                                });
                                 if(v.clase_opciones === "PP"){
                                ordenNueva = ordenNueva + '</select>';
                            }
                            });
                        }
                    
//                    if (lasbebidas.length > 0) {
//                        
//                        $.each(lasbebidas, function(i, v) {
//                            
//                        });
//                        ordenNueva = ordenNueva + '</select>';
//                    }
                

            
            ordenNueva = ordenNueva + '<div class="iconoLista"></div>';
            
            if ($(producto).data('especial') == "S") {
                ordenNueva = ordenNueva + '<div class="iconoReceta"></div>';
            }
            //PRECIO DEL MENU
            //cod_producto
            ordenNueva = ordenNueva + '<div class="precioMenu">';
            var precioTotal = 0;
            $.each(productoExiste.find('select[name="opcionestamanio"]'), function(i, v) {
                precioTotal = precioTotal + parseFloat($(v).find('option:selected').data('precio'));
            });
            if (switchState) {
                ordenNueva = ordenNueva + '<label>' + $(producto).data('precio_carta') + '</label>';
                precioTotal = precioTotal + parseFloat($(producto).data('precio_carta'));
            } else {
                ordenNueva = ordenNueva + '<label>' + $(producto).data('precio_menu') + '</label>';
                precioTotal = precioTotal + parseFloat($(producto).data('precio_menu'));
            }


            productoExiste.find('.hCosto').find('label').text(parseFloat(precioTotal).toFixed(2));
            ordenNueva = ordenNueva + '</div>' +
                    '</div> ' +
                    '</div>' +
                    '<div class="pedidoEspecialDetalle">'+
            detalleOrden;
            if(detalleOrden != ''){
            	ordenNueva = ordenNueva +'<br style="clear: both"/>'
            }
            ordenNueva = ordenNueva + '</div><br style="clear: both"/></div>';
            productoExiste.append(ordenNueva);

            var inputC = productoExiste.find('input[name="contador"]');
            if (checkIsNumber(inputC.get(0)) && inputC.val().length < 20) {
                inputC.val(parseInt(inputC.val()) + 1);
            }
        }
        else {

            var crr = $(producto).data('clase') + "" + $(producto).data('item') + "P1";
            cargarNuevoALaOrden(crr, $(producto).data('clase'), $(producto).data('item'), switchState);

            if (switchState) {
                var nuevo = '<div class="producto_seleccionado" data-especial="' + $(producto).data('especial') + '" data-tipo="' + $(producto).data('tipo') + '" data-nombre="' + $(producto).data('nombre') + '" data-precio_menu="' + $(producto).data('precio_menu') + '" data-precio_carta="' + $(producto).data('precio_carta') + '" data-clase="' + $(producto).data('clase') + '" data-item="' + $(producto).data('item') + '" data-cod_producto="' + $(producto).data('cod_producto') + '" id="' + $(producto).data('clase') + $(producto).data('item') + '" data-switch="true" ><div class="headerOrden">';
            } else {
                var nuevo = '<div class="producto_seleccionado" data-especial="' + $(producto).data('especial') + '" data-tipo="' + $(producto).data('tipo') + '" data-nombre="' + $(producto).data('nombre') + '" data-precio_menu="' + $(producto).data('precio_menu') + '" data-precio_carta="' + $(producto).data('precio_carta') + '" data-clase="' + $(producto).data('clase') + '" data-item="' + $(producto).data('item') + '" data-cod_producto="' + $(producto).data('cod_producto') + '" id="' + $(producto).data('clase') + $(producto).data('item') + '" data-switch="false" ><div class="headerOrden">';
            }
            nuevo = nuevo + '<div class="hCerrar"></div>' +
                    '<div class="hMenuSeleccionado">' +
                    '<label>' + $(producto).data('nombre') + '</label>' +
                    '</div>' +
                    '<div class="hContador">' +
                    '<input type="text" name="contador" class="contador" readonly max-length="2" value="1"/>' +
                    '</div>' +
                    '<div class="hSuma">' +
                    '</div>' +
                    '<div class="hRest">' +
                    '</div>' +
                    '<div class="hEdit">' +
                    '</div>' +
                    '<div class="hCosto">';
            if (switchState) {
                nuevo = nuevo + '<label>' + parseFloat($(producto).data('precio_carta')).toFixed(2) + '</label>';
            }
            else {
                nuevo = nuevo + '<label>' + $(producto).data('precio_menu') + '</label>';
            }
            nuevo = nuevo + '</div>' +
                    '</div>' +
                    '<div data-idpadre="' + $(producto).data('clase') + '' + $(producto).data('item') + '" id="' + crr + '" class="ordenDetallada">' +
                    '<div class="menuElegido">' +
                    '<a href="javascript:void(0)" class="borrarElegido">' +
                    '</a>' +
                    '<label class="nombreElegido">' + $(producto).data('nombre') + '</label>';

            if (switchState) {
                nuevo = nuevo + '<select class="papasFritas" name="opcionestamanio">';
                nuevo = nuevo + '<option selected="selected" data-cod_producto="' + $(producto).data('cod_producto') + '" data-precio="' + $(producto).data('precio_carta') + '" value="' + $(producto).data('cod_producto') + '">A la carta</option>';
                if ($(producto).data('tipo') == "3") {

                    var lostamanios = tamanios($(producto).data('clase'), $(producto).data('item'), crr, switchState);
                    var defaultv = '';
                    if (lostamanios.length > 0) {
                        $.each(lostamanios, function(indes, vals) {
                            if (vals.def === "S") {
                                defaultv = vals.tamano;
                            }
                            nuevo = nuevo + '<option data-cod_producto="' + vals.cod_producto + '" data-precio="' + vals.precio + '" value="' + vals.cod_producto + '">' + vals.desc + '</option>';
                        });
                    }
                }
                nuevo = nuevo + '</select>';
                //nuevo = nuevo + '<select style="display:none;" name="opcionesbebidas" class="bebidas"></select>';
                var opcs = opcionesDelItem(crr,true);
                    var gruposOpciones = opcs.grupos;
                    
                        var detalleOrden = "";
                        $.each(gruposOpciones, function(i, v) {
                            if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '<select name="opcionesbebidas" class="bebidas">';
                            }
                                var datosOpciones = opcs.opcionDatos;
                                
                                $.each(datosOpciones, function(j, w) {
                                    if(v.clase_opciones != "PP" && v.clase_opciones == w.clase_opciones && w.elegido){
                                    detalleOrden = detalleOrden + '<div class="opcionItem esOpcion" data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" >' +
                            '<label>' + w.cantidad + ' ' + w.nombre + '</label>' +
                            '<span>' + w.precio + '</span>' +
                            '</div>';
                                    }
                                    if (v.clase_opciones == "PP" && w.clase_opciones == "PP" && w.elegido) {
                                nuevo = nuevo + '<option selected="selected" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            } else if(v.clase_opciones == "PP" && w.clase_opciones == "PP") {
                                nuevo = nuevo + '<option value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            }
                                });
                                if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '</select>';
                            }
                            });
                        
            } 
            else {
                var lostamanios = tamanios($(producto).data('clase'), $(producto).data('item'), crr, switchState);
                var defaultv = '';
                if (lostamanios.length > 0) {
                    nuevo = nuevo + '<select class="papasFritas" name="opcionestamanio">';
                    if ($(producto).data('tipo') == "3") {
                        nuevo = nuevo + '<option data-cod_producto="' + $(producto).data('cod_producto') + '" data-precio="' + $(producto).data('precio_carta') + '" value="' + $(producto).data('cod_producto') + '">A la carta</option>';
                    }
                    $.each(lostamanios, function(indes, vals) {
                        if (vals.def === "S") {
                            defaultv = vals.tamano;
                            nuevo = nuevo + '<option selected="selected" data-cod_producto="' + vals.cod_producto + '" data-precio="' + vals.precio + '" value="' + vals.tamano + '">' + vals.desc + '</option>';
                        } else {
                            nuevo = nuevo + '<option data-precio="' + vals.precio + '" data-cod_producto="' + vals.cod_producto + '" value="' + vals.tamano + '">' + vals.desc + '</option>';
                        }
                    });
                    nuevo = nuevo + '</select>';
                }
//                if (defaultv !== '') {
//                    var lasbebidas = opcionBebida(defaultv, $(producto).data('clase'), $(producto).data('item'));
//                    if (lasbebidas.length > 0) {
//                        nuevo = nuevo + '<select name="opcionesbebidas" class="bebidas">';
//                        $.each(lasbebidas, function(i, v) {
//                            if (v.default === "S") {
//                                nuevo = nuevo + '<option data-cod_producto="' + v.cod_producto + '" selected="selected" value="' + v.no_opcion + '">' + v.nombre + '</option>';
//                            } else {
//                                nuevo = nuevo + '<option data-cod_producto="' + v.cod_producto + '" value="' + v.no_opcion + '">' + v.nombre + '</option>';
//                            }
//                        });
//                        nuevo = nuevo + '</select>';
//                    }
//                }
var opcs = opcionesDelItem(crr,true);
                    var gruposOpciones = opcs.grupos;
                    
                        var detalleOrden = "";
                        $.each(gruposOpciones, function(i, v) {
                            if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '<select name="opcionesbebidas" class="bebidas">';
                            }
                                var datosOpciones = opcs.opcionDatos;
                                
                                $.each(datosOpciones, function(j, w) {
                                    if(v.clase_opciones !== "PP" && v.clase_opciones == w.clase_opciones && w.elegido){
                                    detalleOrden = detalleOrden + '<div class="opcionItem esOpcion" data-grupo="' + w.grupo_opciones + '" data-precio="' + w.precio + '" data-cantidad="' + w.cantidad + '" data-cod_producto="' + w.cod_producto + '" >' +
                            '<label>' + w.cantidad + ' ' + w.nombre + '</label>' +
                            '<span>' + parseFloat(w.precio).toFixed(2) + '</span>' +
                            '</div>';
                                    }
                                    if (v.clase_opciones == "PP" && w.clase_opciones == "PP" && w.elegido) {
                                nuevo = nuevo + '<option selected="selected" value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            } else if(v.clase_opciones == "PP" && w.clase_opciones == "PP") {
                                nuevo = nuevo + '<option value="' + w.cod_producto + '">' + w.nombre + '</option>';
                            }
                                });
                                if(v.clase_opciones === "PP"){
                                nuevo = nuevo + '</select>';
                            }
                            });
                        
            }
            nuevo = nuevo + '<div class="iconoLista"></div>';
            if ($(producto).data('especial') == "S") {
                nuevo = nuevo + '<div class="iconoReceta"></div>';
            }
            //PRECIO DEL MENU
            //cod_producto
            nuevo = nuevo + '<div class="precioMenu">';

            if (switchState) {
                nuevo = nuevo + '<label>' + parseFloat($(producto).data('precio_carta')).toFixed(2) + '</label>';
            } else {
                nuevo = nuevo + '<label>' + parseFloat($(producto).data('precio_menu')).toFixed(2) + '</label>';
            }
            nuevo = nuevo + '</div>' +
                    '</div> ' +
                    '</div>' +
                    '<div class="pedidoEspecialDetalle">' + detalleOrden;
    
            if(detalleOrden != ''){
            	nuevo = nuevo +'<br style="clear: both"/>';
            } 
            nuevo = nuevo +'</div><br style="clear: both"/></div>';
            $('#contenedorOrdenes').append(nuevo);
        }
        actTotal();
    }
    function actTotal() {
        var total = 0;
        $.each($('#contenedorOrdenes').find('.hCosto'), function(i, v) {
            total = total + parseFloat($(v).find('label').text())
        })

        $('#totalDeTodo').text(total.toFixed(2));
		
//		APLICAR FANCYSELECT
        $('body').find('.papasFritas').fancySelect().trigger('update');
//
        $('body').find('.bebidas').fancySelect().trigger('update');
    }
    function tamanios(clase, item, codU, switchState) {
        var tamanios = [];
        var cod_pedido = $('input[name=cod_pedido]').val();
        var cod = 3773;
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&alaCarta=" + switchState + "&codU=" + codU + "&clase=" + clase + "&item=" + item + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    tamanios = $.parseJSON(r.data);
                }
            },
            error: function(a, b, c) {
                alert(a.responseText);
            }
        });
        return tamanios;
    }

    function slide(page, tipo) {
        var cm = 16;
        if (page == 1) {
            inicio = page - 1;
        } else {
            inicio = (page - 1) * cm;
        }
        var html = "";
        for (i = inicio; i < inicio + cm; i++) {
            if (itemsGlobal[i] !== 'undefined' && itemsGlobal[i] !== undefined) {
//                console.log('ITEM ' + itemsGlobal[i].nombre);
                //imprimirOpciones(itemsGlobal[i].no_clase, itemsGlobal[i].no_item);
                if (itemsGlobal[i].dispo === 'A') {
                    if (itemsGlobal[i].tipo == tipo || itemsGlobal[i].tipo == "3") {
                        html = html + '<div class="productos"><div data-especial="' + itemsGlobal[i].especial + '" data-tipo="' + itemsGlobal[i].tipo + '" data-nombre="' + itemsGlobal[i].nombre + '" data-precio_menu="' + itemsGlobal[i].precio_default + '" data-precio_carta="' + itemsGlobal[i].precio_carta + '" data-clase="' + itemsGlobal[i].no_clase + '" data-item="' + itemsGlobal[i].no_item + '" data-cod_producto="' + itemsGlobal[i].cod_producto + '" class="overlayProducto">';
                        html = html + ' <h1>Precio</h1><label>' + itemsGlobal[i].precio_carta + '</label>';
                        html = html + '</div><img src="' + itemsGlobal[i].imagen + '"/><label class="nombreProducto">' + itemsGlobal[i].nombre + '</label></div>';
                    }
                } else {
                    html = html + '<div class="productos"><div data-especial="' + itemsGlobal[i].especial + '" data-nombre="' + itemsGlobal[i].nombre + '" data-precio="' + itemsGlobal[i].precio_carta + '" data-clase="' + itemsGlobal[i].no_clase + '" data-item="' + itemsGlobal[i].no_item + '" data-cod_producto="' + itemsGlobal[i].cod_producto + '" class="overlayProducto inactivo">';
                    html = html + ' <h1>NO DISPONIBLE</h1><label></label>';
                    html = html + '</div><img src="' + itemsGlobal[i].imagen + '"/><label class="nombreProducto">' + itemsGlobal[i].nombre + '</label></div>';
                }

            } else {
                break;
            }
        }
        $('.contenedorProductos').html(html);
    }
    function grupoOpc(clase, item, tamano, codU) {
        var arr = {};
        var cod = 98883;
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod=" + cod + "&tamano=" + tamano + "&clase=" + clase + "&item=" + item+"&`codU="+codU,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    arr = $.parseJSON(r.data);
                } else {
                    alert(r.data);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return arr;
    }
    function opcionesDelItem(codU, esdefault) {
        var arr = {};
        var cod = 3774;
        var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod=" + cod + "&esdefault=" + esdefault + "&codU="+codU,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    var arre = [];
                	arr = $.parseJSON(r.data);
                    $.each(arr.opcionDatos, function(i,v){
                        arre.push(v);
                    });
                 // Call Sort By Name
                    arre.sort(SortByOrdenamiento);
                    arr.opcionDatos = arre;
                } else {
                    alert(r.data);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return arr;
    }
    
    function cambioTamanioOpciones(codU) {
        var arr = {};
        var cod = 4614;
        var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod=" + cod + "&codU="+codU,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    var arre = [];
                	arr = $.parseJSON(r.data);
                    $.each(arr.opcionDatos, function(i,v){
                        arre.push(v);
                    });
                 // Call Sort By Name
                    arre.sort(SortByOrdenamiento);
                    arr.opcionDatos = arre;
                } else {
                    alert(r.data);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return arr;
    }
    
    function cambioTamanioIngredientes(codU) {
        var arr = {};
        var cod = 4615;
        var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod=" + cod + "&codU="+codU,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.r === "1") {
                    var arre = [];
                	arr = $.parseJSON(r.data);
//                    $.each(arr.opcionDatos, function(i,v){
//                        arre.push(v);
//                    });
//                 // Call Sort By Name
//                    arre.sort(SortByOrdenamiento);
//                    arr.opcionDatos = arre;
                } else {
                    alert(r.data);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return arr;
    }
    
    
    function receta(pro, men) {
        var cod = 2291;
        var cod_pedido = $('input[name=cod_pedido]').val();
        var arr = "";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod=" + cod + "&codU=" + pro + "&esdefault=" + men,
            async: false,
            success: function(r) {
                arr = r;
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return arr;
    }
    function countChar(val, lenght) {
        var len = val.value.length;

        if (len >= lenght) {
            val.value = val.value.substring(0, lenght);
        }
    }
    function checkIsNumber(thisObj) {
        var valL = thisObj.value.length;
        var valN = thisObj.value.slice(-1);

        if (!isNaN(valN) && valN != " ") {
            //si es Numero
            return true;
        } else {
            // Si es Letra o espacio
            //Eliminamos el caracter ingresado
            thisObj.value = thisObj.value.substring(0, (valL - 1));
            return thisObj.value;
        }
    }
    $('input[name="efectivo"]').on("change paste keyup", function() {
        if(checkIsNumber(this)){
        	if(parseFloat($(this).val()) <= parseFloat($('input[name="total"]').val())){
                alert('si');
            }else{
                alert('no');        
            }	
        }
    });
    function eliminarSingular(codU){
    	var seg = false;
    	var cod_pedido = $('input[name=cod_pedido]').val();
    	var cod = "23419";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod="+cod+"&codU=" + codU,
            async: false,
            dataType: "json",
            success: function(r) {
                console.log(r);
                seg = true;
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return seg;
    }
    function eliminarMultiple(clase, item){
    	var seg = false;
    	var cod = "23420";
    	var cod_pedido = $('input[name=cod_pedido]').val();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "cod_pedido="+cod_pedido+"&cod="+cod+"&claseDel=" + clase+"&itemDel=" + item,
            async: false,
            dataType: "json",
            success: function(r) {
                console.log(r);
                seg = true;
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
        return seg;
    }
    // ascending order
    function SortByID(x,y) {
      return x.ID - y.ID; 
    }


    function SortByOrdenamiento(x,y) {
      return ((x.ordenamiento == y.ordenamiento) ? 0 : ((x.ordenamiento > y.ordenamiento) ? 1 : -1 ));
    }
    
    function cargaDeOrdenes(){
    	
    	var cod = "25725";
    	var d = new Date();
    	var hora = d.getHours();
    	var fecha = d.getDate() + '/' + (d.getMonth() + 1) +'/'+ d.getFullYear();
        $.ajax({
            url: "Loader",
            type: "POST",
            data: "tienda=" + res + "&hora=" + hora +"&fecha=" + fecha + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.cod === "0") {
                    console.log(r.data);
                    $('.cuadroBlanco').html('<label>'+ r.data.ordenes_actuales +'</label>' + '<label>'+ r.data.limite +'</label>');
                }else{
                    alert(r.mensaje);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
    }
    
    $('.enviarOrdenBtn').click(function(){
         var cod = "666";
        $.ajax({
            url: "Loader",
            type: "POST",
            data: $('#enviarOrden').serialize() + "&cod=" + cod,
            async: false,
            dataType: "json",
            success: function(r) {
                if (r.codigo === "0") {
                    $('body').find('#modalFormaPago').find('#enviarOrden').remove();
                    $('#mensajeGrabado').find('h2').html('Orden #'+r.numero_orden);
                    $('#mensajeGrabado').fadeIn();
                }else{
                    alert(r.mensaje);
                }
            },
            error: function(a, b, c) {
                console.log(a.responseText);
            }
        });
    });
    
//    FANCYSELECT MODAL FORMAS DE PAGO\
    

});