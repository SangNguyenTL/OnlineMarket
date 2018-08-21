(function ($) {
    'use strict';

    window.app = {
        name: 'Flatkit',
        version: '1.1.3',
        // for chart colors
        color: {
            'primary':      '#0cc2aa',
            'accent':       '#a88add',
            'warn':         '#fcc100',
            'info':         '#6887ff',
            'success':      '#6cc788',
            'warning':      '#f77a99',
            'danger':       '#f44455',
            'white':        '#ffffff',
            'light':        '#f1f2f3',
            'dark':         '#2e3e4e',
            'black':        '#2a2b3c'
        },
        setting: {
            theme: {
                primary: 'primary',
                accent: 'accent',
                warn: 'warn'
            },
            color: {
                primary:      '#0cc2aa',
                accent:       '#a88add',
                warn:         '#fcc100'
            },
            folded: false,
            boxed: false,
            container: false,
            themeID: 1,
            bg: ''
        }
    };

    var setting = 'jqStorage-'+app.name+'-Setting',
        storage = $.localStorage;

    if( storage.isEmpty(setting) ){
        storage.set(setting, app.setting);
    }else{
        app.setting = storage.get(setting);
    }
    var v = window.location.search.substring(1).split('&');
    for (var i = 0; i < v.length; i++)
    {
        var n = v[i].split('=');
        app.setting[n[0]] = (n[1] == "true" || n[1]== "false") ? (n[1] == "true") : n[1];
        storage.set(setting, app.setting);
    }

    // init
    function setTheme(){

        $('body').removeClass($('body').attr('ui-class')).addClass(app.setting.bg).attr('ui-class', app.setting.bg);
        app.setting.folded ? $('#aside').addClass('folded') : $('#aside').removeClass('folded');
        app.setting.boxed ? $('body').addClass('container') : $('body').removeClass('container');

        $('.switcher input[value="'+app.setting.themeID+'"]').prop('checked', true);
        $('.switcher input[value="'+app.setting.bg+'"]').prop('checked', true);

        $('[data-target="folded"] input').prop('checked', app.setting.folded);
        $('[data-target="boxed"] input').prop('checked', app.setting.boxed);
    }

    // click to switch
    $(document).on('click.setting', '.switcher input', function(e){
        var $this = $(this), $target;
        $target = $this.parent().attr('data-target') ? $this.parent().attr('data-target') : $this.parent().parent().attr('data-target');
        app.setting[$target] = $this.is(':checkbox') ? $this.prop('checked') : $(this).val();
        ($(this).attr('name')=='color') && (app.setting.theme = eval('[' +  $(this).parent().attr('data-value') +']')[0]) && setColor();
        storage.set(setting, app.setting);
        setTheme(app.setting);
    });

    function setColor(){
        app.setting.color = {
            primary: getColor( app.setting.theme.primary ),
            accent: getColor( app.setting.theme.accent ),
            warn: getColor( app.setting.theme.warn )
        };
    };

    function getColor(name){
        return app.color[ name ] ? app.color[ name ] : palette.find(name);
    };

    function init(){
        $('[ui-jp]').uiJp();
        $('body').uiInclude();
        $('[data-toggle="popover"]').popover()
    }

    $(document).on('pjaxStart', function() {
        $('#aside').modal('hide');
        $('body').removeClass('modal-open').find('.modal-backdrop').remove();
        $('.navbar-toggleable-sm').collapse('hide');

    });

    $(document).on('pjaxEnd', function() {
        $('[data-toggle="popover"]').popover()
    });
    window.pending = false;

    $(document).on('click', "button.activate-rating", function(e) {
        var element = $(e.target), id = element.data("id"),
            container = element.closest("td");
        if(!id) return;
        if(window.pending){
            alert("Processing...", "warning");
            return;
        }
        $.ajax({
            type: 'POST',
            url: PATH + 'api/rating/modify-review-state',
            data: {
                id : id
            },
            beforeSend: function () {
                window.pending = true;
            },
            dataType: 'json',
            success: function (data) {
                if(data.error)
                    alert(data.message, "warning");
                else{
                    alert("Success!");
                    container.html("<span class='text-success'>Activated</span>")
                }
            },
            error: function () {
                alert("Unknown error", "danger");
            },
            complete: function () {
                window.pending = false;
            }

        })
    });

    $(document).on('click', "a.action-seen", function(e) {
        let element = $(this), id = element.data("id"), status = element.data("status");
        if(!id || (status != 0 && status != 1)) return;
        if(window.pending){
            alert("Processing...", "warning");
            return;
        }
        $.ajax({
            type: 'POST',
            url: PATH + 'api/notification/change-status',
            data: {
                id : id,
                status: status
            },
            beforeSend: function () {
                window.pending = true;
            },
            dataType: 'json',
            success: function (data) {
                if(data.error)
                    alert(data.message, "warning");
                else{
                    if(status === 1){
                        alert("Seen success!");
                        element.removeClass("b-success").addClass("b-accent");
                        element.closest(".sl-item").addClass("b-success");
                        element.data("status", 0)
                    }
                    else {
                        alert("Unread success!");
                        element.removeClass("b-accent").addClass("b-success");
                        element.closest(".sl-item").removeClass("b-success")
                        element.data("status", 1)
                    }
                }
            },
            error: function () {
                alert("Unknown error", "danger");
            },
            complete: function () {
                window.pending = false;
            }

        })
    });

    setInterval(function(){
        $.ajax({
            url: PATH + 'api/notification/count',
            success: function (data) {
                if(data.error)
                    if(data.message !== "User not found")
                        alert(data.message, "warning");
                    else window.location.href = PATH+"login?error=expired";
                else{
                    let notifyLi = $(".notification-li"), notifyA = $(".notification-a"),
                        count = Number.parseInt(data.message);
                    if(notifyLi.length > 0){
                        if(notifyLi.find(".nav-label .label").length>0){
                            if(count > 0)
                                notifyLi.find(".nav-label .label").html(data.message);
                            else notifyLi.find(".nav-label .label").remove();
                        }else {
                            if(count > 0)
                                notifyLi.find(".nav-icon").after('<span class="nav-label"><b class="label label-sm warn">'+data.message+'</b></span>')
                        }
                    }
                    if(notifyA.length > 0){
                        if(notifyA.find(".label").length>0){
                            if(count > 0)
                                notifyA.find(".label").html(data.message);
                            else notifyA.find(".label").remove();
                        }else {
                            if(count > 0)
                                notifyA.append('<span class="label label-sm up warn">'+data.message+'</span>')
                        }
                    }
                }
            }
        })
    }, (5 * 1000));

    $(document).on('click', "button.activate-comment", function(e) {
        var element = $(e.target), id = element.data("id"),
            container = element.closest("td");
        if(!id) return;
        if(window.pending){
            alert("Processing...", "warning");
            return;
        }
        $.ajax({
            type: 'POST',
            url: PATH + 'api/comment/modify-status',
            data: {
                id : id
            },
            beforeSend: function () {
                window.pending = true;
            },
            dataType: 'json',
            success: function (data, textStatus) {
                if(textStatus)
                    if(data.error)
                        alert(data.message, "warning");
                    else{
                        alert("Success!");
                        container.html("<span class='text-success'>Activated</span>")
                    }
            },
            error: function () {
                alert("Unknown error", "danger");
            },
            complete: function () {
                window.pending = false;
            }

        })
    });

    init();
    setTheme();
    !function(){

        $(document).on("click",".form-confirm button",function(e){
            var form = $(e.target).closest(".form-confirm");
            e.preventDefault();
            bootbox.confirm({
                message: "Are you ready to take this action?",
                buttons: {
                    confirm: {
                        label: 'Yes',
                        className: 'btn-success btn m-l-sm'
                    },
                    cancel: {
                        label: 'No',
                        className: 'btn-danger btn'
                    }
                },
                callback: function (result) {
                    if(result) form.trigger("submit");
                }
            });
        });

        if(typeof $.notify != 'function') return;
        window.alert = function(msg, type){
            type = type || "success";
            $.notify({
                message: msg
            },{
                type: type
            });
        }
    }();
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $('.generateCode').on('click', function (e) {
        let codeElement = $("#code"),
            text = "",
             possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (let i = 0; i < 12; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));

        codeElement.val(text);
    });
})(jQuery);
