(function ($) {
    "use strict";
    $('[ui-jp=summernote]').on('summernote.change', function(we, contents) {
        var element = $(we.currentTarget);
        contents = contents == "<p><br></p>" ? "" : contents;
        element.html(contents);
        element.find("form").parsley().reset();
    });
})(jQuery);