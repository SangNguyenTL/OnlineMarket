(function ($) {
    "use strict";
    window.Parsley.addValidator('uniqueCode', {
        validateString: function (value, requirement, instance) {

            let elementId = instance.$element.closest("form").find("[name=id]"), id = elementId.val();

            let xhr = $.ajax({
                url: requirement,
                data: {
                    code : value,
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
            en: 'Code of item is invalid, the code may be already exists!'
        },
        priority: 32
    });

    window.Parsley.addValidator('uniqueSlug', {
        validateString: function (value, requirement, instance) {

            let elementId = instance.$element.closest("form").find("[name=id]"), id = elementId.val();

            let xhr = $.ajax({
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

            let elementId = instance.$element.closest("form").find("[name=id]"), id = elementId.val();

            let xhr = $.ajax({
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
        let uniqueSlugElement = $('[data-parsley-unique-slug]');
        if(uniqueSlugElement.length > 0){
            let data = uniqueSlugElement.data();
            if(data.parsleyTarget){
                let target = $("[name="+data.parsleyTarget+"]");
                target.on("keyup",function (){
                    let value = slug($(this).val(), {lower: true});
                    uniqueSlugElement.val(value);
                    uniqueSlugElement.trigger("change");
                });
            }

            uniqueSlugElement.on("change", function(){
                let val = uniqueSlugElement.val();
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
  