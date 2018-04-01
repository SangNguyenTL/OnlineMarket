(function ($) {
    "use strict";

    var uniqueSlugElement = $('[data-parsley-unique-slug]');
    if(uniqueSlugElement.length > 0){
        var data = uniqueSlugElement.data();
        if(data.parsleyTarget){
            var target = $("[name="+data.parsleyTarget+"]");
            target.on("keyup",function (){
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

    if(pickElement.length === 1){
        var galleryData = {},
        actionModalElement = $("[data-toggle=modal]");
        actionModalElement.on("click", function(){
            var dataInputPath = $(this).data("targetInput");
            galleryData = $("[ui-jp=galleryManager]").data("galleryManager");
            galleryData.inputElment = $("#"+dataInputPath);
        });
        pickElement.on("click", function(){
            if(galleryData.inputElment)
            galleryData.inputElment.val(galleryData.imageLink);
            galleryData.inputElment.closest(".box-image").find("img").attr("src", window.location.origin+PATH+galleryData.imageLink);
        })
    }


    
    window.Parsley.addValidator('uniqueSlug', {
        validateString: function (value, requirement, instance) {
            
            var elementId = instance.$element.closest("form").find("[name=id]"), id = elementId.val();

            var xhr = $.ajax({
                url: requirement,
                data: {
                    value : value,
                    id: id
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
            },function(data){
                if(data.responseJSON.errors){
                    $.each(data.responseJSON.errors, function(k, val){
                        alert(val,"danger")
                    })
                }
                return false;
            });
        },
        messages: {
            en: 'Slug of item is invalid, the slug may be already exists!'
        },
        priority: 32
    });
  
  })(jQuery);
  