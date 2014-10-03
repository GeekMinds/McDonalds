/**
 * 
 */
jQuery(document).ready(function() {
$('#content').on('click', '#lanzarScreenPoP',function(e){
        e.preventDefault();
        $('#content').children().fadeOut('slow'); 
        $.ajax({
            type: "POST",
            url: 'ScreenPop',
            success: function(data)
            {
                $('html').html(data);
            }
        });
        return false;
    });
});
