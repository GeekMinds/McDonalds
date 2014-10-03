/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


jQuery(document).ready(function() {
    $('#login').submit(function(e) {
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

                    $('html').html($(r)).fadeIn('slow');
                }else{
                    //$('.mensajeError').text(response.data);
                    $('.mensajeError').text(r.data);
                }
            }
        });
        return false;
    });
    
});