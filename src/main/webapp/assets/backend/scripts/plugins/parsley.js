(function ($) {
    "use strict";

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
                method: 'POST'
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

    window.Parsley.addValidator('uniqueEmail', {
        validateString: function (value, requirement, instance) {

            var elementId = instance.$element.closest("form").find("[name=id]"), id = elementId.val();

            var xhr = $.ajax({
                url: requirement,
                data: {
                    value : value,
                    id: id
                },
                dataType: 'json',
                method: 'POST'
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
            en: 'Email of user is invalid, the email may be already exists!'
        },
        priority: 32
    });

    function initPar(){
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

        let pickElement = $("[data-target=#modal-image-gallery]");

        if(pickElement.length === 1){
            let galleryData = {},
                actionModalElement = $("[data-toggle=modal]");
            actionModalElement.on("click", function(){
                let dataInputPath = $(this).data("targetInput");
                galleryData = $("[ui-jp=galleryManager]").data("galleryManager");
                galleryData.inputElment = $("#"+dataInputPath);
            });
        }
    }
    initPar();
    $(document).on("pjaxEnd", function () {
        initPar();
    });

})(jQuery);
  