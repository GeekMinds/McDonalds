/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {
	$(".direccionesCliente").fancySelect();
					var direccionNueva = $('#nuevaDireccion').html();
					$('#nuevaDireccion').remove();
					var QueryString = function() {
						// This function is anonymous, is executed immediately
						// and
						// the return value is assigned to QueryString!
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
								var arr = [ query_string[pair[0]], pair[1] ];
								query_string[pair[0]] = arr;
								// If third or later entry with this name
							} else {
								query_string[pair[0]].push(pair[1]);
							}
						}
						return query_string;
					}();

					var clienteID = QueryString.cliente;
					var telefono = QueryString.telefono;
					
					
					//NEXT & PREV DIRECCION
					$("body")
							.on(
									"click",
									"#nextDir",
									function() {
										var nextCat = $('.actual').next(
												'div.direccionesAsociadas');
										var form;
										var dpdwRes = $('#direccionRestaurante');
										dpdwResN = dpdwRes.find('.default');
										dpdwRes.html(dpdwResN);
										if (nextCat.length !== 0) {
											var a = parseInt($(
													'.paginadorDirecciones .primero')
													.html());
											$('.paginadorDirecciones .primero')
													.html(a + 1);
											$('.carouselHorizontalElemento')
													.animate({
														'margin-left' : '-=640'
													});
											$('.actual').removeClass('actual');
											nextCat.addClass('actual');
											form = nextCat;
										} else {
											$('.paginadorDirecciones .primero')
													.html('1');
											$('.carouselHorizontalElemento')
													.animate({
														'margin-left' : '0'
													});
											$('.actual').removeClass('actual');
											$(
													'.carouselHorizontalElemento div.direccionesAsociadas')
													.first().addClass('actual');
											form = $(
													'.carouselHorizontalElemento div.direccionesAsociadas')
													.first();
										}
										restauranteSugerido(
												form
														.find(
																'select[name="zona"]')
														.val(),
												form
														.find(
																'select[name="colonia"]')
														.val(),
												form
														.find('select[name="colonia"]'));
										$('#direccionRestaurante').find(
												'option:selected').removeAttr(
												'selected');
										$('#direccionRestaurante').removeAttr(
												'selected');
										$('#direccionRestaurante')
												.find(
														'option[value="'
																+ form
																		.find(
																				'input[name="asignado"]')
																		.val()
																+ '"]').attr(
														'selected', true);
									});
					$("body")
							.on(
									"click",
									"#prevDir",
									function() {
										var prevCat = $('.actual').prev(
												'div.direccionesAsociadas');
										var form;
										var dpdwRes = $('#direccionRestaurante');
										dpdwResN = dpdwRes.find('.default');
										dpdwRes.html(dpdwResN);
										if (prevCat.length !== 0) {
											var a = parseInt($(
													'.paginadorDirecciones .primero')
													.html());
											$('.paginadorDirecciones .primero')
													.html(a - 1);
											$('.paginadorDirecciones .primero')
													.html();
											$('.carouselHorizontalElemento')
													.animate({
														'margin-left' : '+=640'
													});
											$('.actual').removeClass('actual');
											prevCat.addClass('actual');
											form = prevCat;
										} else {
											$('.paginadorDirecciones .primero')
													.html(
															$(
																	'.paginadorDirecciones .segundo')
																	.html());
											mrg = $(
													'.carouselHorizontalElemento')
													.width() - 640;
											$('.carouselHorizontalElemento')
													.animate(
															{
																'margin-left' : '-='
																		+ mrg
															});
											$('.actual').removeClass('actual');
											$(
													'.carouselHorizontalElemento div.direccionesAsociadas')
													.last().addClass('actual');
											form = $(
													'.carouselHorizontalElemento div.direccionesAsociadas')
													.last();
										}
										restauranteSugerido(
												form
														.find(
																'select[name="zona"]')
														.val(),
												form
														.find(
																'select[name="colonia"]')
														.val(),
												form
														.find('select[name="colonia"]'));
										$('#direccionRestaurante').find(
												'option:selected').removeAttr(
												'selected');
										$('#direccionRestaurante').removeAttr(
												'selected');
										$('#direccionRestaurante')
												.find(
														'option[value="'
																+ form
																		.find(
																				'input[name="asignado"]')
																		.val()
																+ '"]').attr(
														'selected', true);
									});
					//CAMBIOS EN EL RESTAURANTE ASIGNADO
					$('#direccionRestaurante').change(
							function() {
								asig = $('#direccionRestaurante').val();
								$('#content').find(
										'form[name="direccion"] .active').find(
										'input[name="asignado"]').val(asig);
								if (asig != '') {
									infoRestaurante(asig);
								}
							});
					//TELEFONO DESDE EL SCREENPOP
					if (telefono != undefined && telefono != 'undefined') {
						$('#ingresoTelefonoGes').val(telefono);
					}
					//	CAMBIOS EN LOS SELECT DE DIRECCION
					$('#content').on(
							'change',
							'select[name="departamento"]',
							function() {
								$(".direccionesCliente").fancySelect().trigger('update.fs');
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
							});
					$('#content').on(
							'change',
							'select[name="municipio"]',
							function() {
								$(".direccionesCliente").fancySelect().trigger('update.fs');
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
							});

					$('#content').on(
							'change',
							'select[name="zona"]',
							function() {
								$(".direccionesCliente").fancySelect().trigger('update.fs');
								idzona = $(this).val();
								var dpdwCol = $(this).closest('form').find(
										'select[name="colonia"]');
								dpdwColN = dpdwCol.find('.default');
								dpdwCol.html(dpdwColN);

								if (idzona != '') {
									colonias(idzona, $(this));
									var dpdwRes = $('#direccionRestaurante');
									dpdwResN = dpdwRes.find('.default');
									dpdwRes.html(dpdwResN);
									dpdwRes.change();
									restauranteSugerido(idzona, "", $(this));

								}
							});
					$('#content')
							.on(
									'change',
									'select[name="colonia"]',
									function() {
										$(".direccionesCliente").fancySelect().trigger('update.fs');
										idzona = $(this).closest('form').find(
												'select[name="zona"]').val();
										idcolonia = $(this).val();
										var dpdwRes = $('#direccionRestaurante'), dpdwResN = dpdwRes
												.find('.default');
										dpdwResN.attr('selected', true);
										dpdwRes.html(dpdwResN);
										dpdwRes.change();
										restauranteSugerido(idzona, idcolonia,
												$(this));
									});
					//RECUPERA RESTAURANTE SEGUN ZONA, COLONIA
					function restauranteSugerido(idzona, idcolonia, selecte) {
						opCod = "1013";
						idZona = idzona;
						idDepto = selecte.closest('form').find(
								'select[name="departamento"]').val();
						idMuni = selecte.closest('form').find(
								'select[name="municipio"]').val();
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
																$(
																		'#direccionRestaurante')
																		.append(
																				'<option value="'
																						+ value.tienda
																						+ '" >'
																						+ value.nombre
																						+ '</option>');
																$(".direccionesCliente").fancySelect().trigger('update.fs');
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
																$(".direccionesCliente").fancySelect().trigger('update.fs');
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
										$(".direccionesCliente").fancySelect().trigger('update.fs');
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
						idMuni = select.closest('form').find(
								'select[name="municipio"]').val();
						$
								.ajax({
									type : "POST",
									data : 'idDepto=' + idDepto + '&idMuni='
											+ idMuni + '&idZona=' + idZona
											+ '&cod=' + opCod,
									async : false,
									url : "Loader",
									success : function(response) {
										if (response.r === '1') {
											coloniasR = $
													.parseJSON(response.data);
											$
													.each(
															coloniasR,
															function(index,
																	value) {
																select
																		.closest(
																				'form')
																		.find(
																				'select[name="colonia"]')
																		.append(
																				'<option value="'
																						+ value.cod_colonia
																						+ '" >'
																						+ value.descripcion
																						+ '</option>');
																$(".direccionesCliente").fancySelect().trigger('update.fs');
															});
										} else {
											//alert(response.data);
										}
									},
									dataType : "json"
								});
					}

					if (clienteID != undefined && clienteID != 'undefined') {
						$('#clienteGes').val(clienteID);
						cliente(clienteID);
						direcciones(clienteID);
						$(".direccionesCliente").fancySelect().trigger('update.fs');
					}else{
						$('<div id="nuevaDireccion" class="direccionesAsociadas" >'+ direccionNueva +'</div>').insertAfter( "#carouselHorizontal" );
					}

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
								}
							},
							error : function(a, b, c) {
								alert(a.responseText);
							},
							dataType : "json"
						});
					}
					function infoRestaurante(tienda) {
						ra = tienda;
						opCod = "1992";
						$.ajax({
							url : "Loader",
							data : "ra=" + ra + "&cod=" + opCod,
							async : false,
							dataType : "json",
							success : function(r) {
								// r = JSON.parse(r);
								if (r.r === "1") {

									$('.tiempoEntrega').text(
											'tiempo de entrega - ' + r.tiempo);
									h = "Observaciones de Promocion: "
											+ r.observaciones_promocion + "\n";
									s = "Observaciones de Producto: "
											+ r.observaciones_producto + "\n";
									t = "Observaciones de Servicio: "
											+ r.observaciones_servicio + "\n";
									$(".contenedorDatosRestaurante").find(
											"textarea").html(h + s + t);
								}
							},
							error : function(a, b, c) {
								alert(a.responseText);
							}
						});
					}
					function direcciones(clienteID) {

						opCod = '1988';
						$.ajax({
							url : "Loader",
							type : "POST",
							data : 'ic=' + clienteID + '&cod=' + opCod,
							async : false,
							success : function(r) {
								// r = JSON.parse(r);
								if (r.r == "1") {
									xr = JSON.parse(r.data);
									cs = 1;
									xr.forEach(function(i) {
										direccion(i.telefono, clienteID, cs);
										cs++;
									});
									
									actualizarCarrousel();
								}else{
									$('<div id="nuevaDireccion" class="direccionesAsociadas" >'+ direccionNueva +'</div>').insertAfter( "#carouselHorizontal" );
								}
							},
							error : function(a, b, c) {
								alert(a.responseText);
							},
							dataType : "json"
						});
					}

					function direccion(dir, clienteID, cs) {
						opCod = '9898';
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
											var html = direccionNueva;
											var newDiv = $('<div style=" width: 640px; float: left; position: relative; " class="direccionesAsociadas"></div>');
											newDiv.append(html);
											$('.carouselHorizontalElemento')
													.append(newDiv);

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
											newDiv.find(
													'input[name="asignado"]')
													.val(dir.asignado);
											newDiv
													.find(
															'input[name="telefonoCliente"]')
													.val(dir.telefono);
											newDiv.find(
													'input[name="direccion"]')
													.val(dir.direccion);

											if (cs == 1) {
												$('#direccionRestaurante')
														.find('option:selected')
														.removeAttr('selected');
												$('#direccionRestaurante')
														.removeAttr('selected');
												$('#direccionRestaurante')
														.find(
																'option[value="'
																		+ dir.asignado
																		+ '"]')
														.attr('selected', true);
											}

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

					function actualizarCarrousel() {
						var wF = 0;
						var contador = 0;
						$
								.each(
										$('.carouselHorizontalElemento div.direccionesAsociadas'),
										function(i, e) {
											wF += $(e).width();
											console.log($(e).width());
											contador++;
										});
						$('.carouselHorizontalElemento').width(wF);
						$('.carouselHorizontalElemento').animate({
							'margin-left' : '0'
						});
						if (contador > 1) {
							$("#nextDir").show();
							$("#prevDir").show();
							$('.paginadorDirecciones .primero').show();
							$('.paginadorDirecciones .primero').html('1');
							$('.paginadorDirecciones .segundo').show();
							$('.paginadorDirecciones .segundo').html(contador);
						}
						$(
								'.carouselHorizontalElemento div.direccionesAsociadas')
								.first().addClass('actual');

					}
					$('#guardar').click(function()
						{save();});
					
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
									cliente(r.data);
									saveDir(r.data);
									alert('guardado correctamente');
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
					function saveDir(clienteID){
						opCod = '790';
						direcc = $('.direccionesAsociadas');
						
							$.each(direcc, function(index, value) {
							formDir = $(value).find('form');
						$.ajax({
							url : "Loader",
							type : "POST",
							data : formDir.serialize() + '&cliente='+clienteID+'&cod=' + opCod,
							async : false,
							success : function(r) {								
							},
							error : function(a, b, c) {
								alert(a.responseText);
							},
							dataType : "json"
						});
					});
						$('.carouselHorizontalElemento').empty();
						direcciones(clienteID);
					}
				});

//$("#cancelar").click(function(){$.ajax({url:"/ScreenPop", success: function(r){$("body").html("");$("body").html(r);}});});