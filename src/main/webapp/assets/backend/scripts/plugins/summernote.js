(function ($) {
    "use strict";
    $(document).on('summernote.init', '[ui-jp=summernote]' ,function(e) {
        var element = $(e.currentTarget), outputElement = element.closest(".form-group").find("textarea");
        element.code(outputElement.val());
    });

    $(document).on('summernote.change', '[ui-jp=summernote]', function(e, content) {
        var element = $(e.currentTarget), outputElement = element.closest(".form-group").find("textarea");
        outputElement.html(content);
    });

    $(document).on('summernote.image.upload', '[ui-jp=summernote]', function(file) {
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