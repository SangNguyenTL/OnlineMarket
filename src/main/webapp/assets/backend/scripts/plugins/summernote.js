(function ($) {
    "use strict";
    $('[ui-jp=summernote]').on('summernote.init', function(e) {
        var elment = $(e.currentTarget), outputElement = elment.closest(".form-group").find("textarea");
        elment.code(outputElement.val());
    });

    $('[ui-jp=summernote]').on('summernote.change', function(e, content) {
        var elment = $(e.currentTarget), outputElement = elment.closest(".form-group").find("textarea");
        outputElement.html(content);
    });

    $('[ui-jp=summernote]').on('summernote.image.upload', function(file) {
        var element = $(this), url = element.data().url;
        sendFile(element, file)
    });
    
    function sendFile(element, file) {
        var data = new FormData();
        data.append("files", file);
        data.append("uploadType", "product");
        $.ajax({
            data: data,
            type: "POST",
            url: PATH + 'api/image/upload',
            cache: false,
            contentType: false,
            processData: false,
            success: function(result) {
                var path = window.location.origin + PATH + result[0].path;
                element.summernote('editor.insertImage', path);
            }
        });
    }
})(jQuery);