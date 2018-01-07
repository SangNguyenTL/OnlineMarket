;(function($){
    if(typeof Clipboard != "function") return;
    $.fn.clipboard = function(options){
        var attrClass = $(this).data('class');
        new Clipboard(attrClass, options);
    }
}(jQuery));