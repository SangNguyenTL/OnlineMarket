(function ($) {
    "use strict";
    $('[ui-jp=summernote]').on('summernote.change', function(we, contents) {
        var element = $(we.currentTarget);
        element.html(contents).trigger('change');
    });
})(jQuery);