(function ($) {
    "use strict";

    var uniqueSlugElement = $('[data-parsley-type=uniqueslug]');
    if(uniqueSlugElement.length > 0){
        var data = uniqueSlugElement.data();
        if(data.parsleyTarget){
            var target = $("[data-parsley-id="+data.parsleyTarget+"]");
            target.on("change",function (){
                var value = slug($(this).val(), {lower: true});
                uniqueSlugElement.val(value);
                uniqueSlugElement.trigger("change");
            });
        }

        uniqueSlugElement.on("change", function(){
            var val = uniqueSlugElement.val();
            uniqueSlugElement.val(slug(val));
        });
    }

    var pickElement = $("#modal-image-gallery [data-action=yes]");

    if(pickElement.length == 1){
        var galleryData = {},
        actionModalElment = $("[data-toggle=modal]");
        actionModalElment.on("click", function(){
            var dataInputPath = $(this).data("targetInput");
            galleryData = $("[ui-jp=galleryManager]").data("galleryManager");
            galleryData.inputElment = $("#"+dataInputPath);
        });
        pickElement.on("click", function(){
            if(galleryData.inputElment)
            galleryData.inputElment.val(galleryData.imageLink);
            galleryData.inputElment.closest(".box").find("img").attr("src", window.location.origin+PATH+galleryData.imageLink);
        })
    }


    
    window.Parsley.addValidator('uniqueslug', {
        validateString: function (value, requirement, instance) {
            var element = instance.element, data = element.data(),
            defaults = {
                parsleyUrl: false,

            }, setting = $.extend(true, data);

            xhr = $.ajax({
                url: setting.parsleyUrl,
                data: {
                    value : value
                },
                dataType: 'json',
                method: 'GET'
            });
    
            return xhr.then(function (data) {
                if (!data.error) {
                    return true;
                } else {
                    return $.Deferred().reject();
                }
            });
        },
        messages: {
            en: 'Slug of item already exists!'
        },
        priority: 32
    });
  
  })(jQuery);
  