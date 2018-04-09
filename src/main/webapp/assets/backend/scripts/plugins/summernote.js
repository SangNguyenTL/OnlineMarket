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

    // $(document).on('summernote.image.upload', '[ui-jp=summernote]', function(file) {
    //     var element = $(this), url = element.data().url;
    //     sendFile(element, file)
    // });

})(jQuery);