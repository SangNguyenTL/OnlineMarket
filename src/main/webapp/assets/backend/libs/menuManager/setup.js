;(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('jquery'));
    } else {
        root.MyMenu = factory(root.jQuery);
    }
}(this, function ($) {
    let pluginName = "myMenu";

    function MyMenu(element, options) {
        if (typeof $.fn.nestable !== "function" && typeof $.fn.select2 !== "function")
            return;
        this.element = element;
        this._element = $(element);
        this.pending = false;

        this.options = {
            pathFontAwesome: PATH_ICON,
            maxDepth: 3
        };

        this.options = $.extend(true, this.options, options, this._element.data(pluginName));

        this.buildControls();

        this.buildSelectIcon();

        this.buildControlItems = this.buildControlItems.bind(this);

        this.onResetForm = this.onResetForm.bind(this);

        this.onSubmitForm = this.onSubmitForm.bind(this);

        this.onChangeItem = this.onChangeItem.bind(this);

        this.equalDataItem = this.equalDataItem.bind(this);

        this.beforeSend = this.beforeSend.bind(this);

        this.complete = this.complete.bind(this);

        this.form.on("submit", this.onSubmitForm);

        this.form.btnReset.on("click", this.onResetForm);

        this.generateListItem();

    }

    MyMenu.prototype.onChangeItem = function (e, b) {
        if (e.currentTarget.id !== b.id)
            return;
        let _this = this
            , item = $(e.currentTarget)
            , ol = item.parent('.dd-list')
            , parent = ol.parent(".dd-item");
        _this.oldData = Object.assign({}, item.data());
        if (_this.pending) {
            alert("Processing...");
            return;
        }

        if (parent.length > 0)
            item.data("parentId", parent.data("id"));
        else
            item.data("parentId", null);

        ol.children().each(function (i, v) {
            v = $(v);
            v.data("priority", i);
        });
        if (_this.equalDataItem(item.data()))
            return;
        _this.beforeSend();

        let requestUpdate = function(){
            let promise = false,
                deferred = new $.Deferred();
            ol.children().each(function (i, v) {
                v = $(v);
                if(!promise) promise = deferred.promise();
                promise = promise.then(function () {
                    for(let key in v.data()){
                        if(v.data(key) === "")
                            v.data(key,null)
                    }
                    return _this.request("api/menu/update", JSON.stringify(v.data()))
                });
            });
            deferred.resolve();
            return promise;
        };
        requestUpdate().then(function () {
            _this.complete();
            _this.generateListItem();
        })
    };

    MyMenu.prototype.equalDataItem = function (data) {
        let _this = this, flag = true;
        if (!this.oldData)
            return flag;
        $.each(_this.oldData, function (i, v) {
            if (data[i] !== _this.oldData[i]) {
                flag = false;
            }
        });
        return flag;
    }
    ;

    MyMenu.prototype.getFormData = function ($form) {
        let unIndexed_array = $form.serializeArray(),
            indexed_array = {};

        $.map(unIndexed_array, function (n, i) {
            let value = n['value'];
            if (!isNaN(value))
                value = parseInt(value);
            if (value !== '' && value !== 0 && value != null) {
                indexed_array[n['name']] = value;
            }
        });

        return indexed_array;
    }
    ;

    MyMenu.prototype.onSubmitForm = function (e) {
        e.preventDefault();
        let form = $(e.currentTarget)
            , _this = this
            , data = _this.getFormData(form)
            , url = _this.form.id.val() != null && _this.form.id.val() > 0 ? 'api/menu/update' : 'api/menu/add';
        form.parsley().validate();
        if (form.parsley().isValid())
            _this.beforeSend();
            _this.request(url, JSON.stringify(data)).then(function () {
                _this.complete();
                _this.generateListItem()
            });
    }
    ;

    MyMenu.prototype.beforeSend = function () {
        this.pending = true;
        if($("#overlay-box").length === 0){
            let container = $.parseHTML("<div class='black-overlay item-bg item' id='overlay-box'/>");
            $(container).append('<div class="center text-center"><i class="fa fa-5x fa-spin fa-spinner text-white"></i></div>').css("z-index", "1200");
            $("body").prepend(container);
        }
    };

    MyMenu.prototype.complete = function () {
        let _this = this;
        _this.pending = false;
        $("#overlay-box").remove();
    };

    MyMenu.prototype.request = function (url, data) {
        return $.ajax({
            url: PATH + url,
            type: "POST",
            data: data,
            contentType: 'application/json',
            error: function (xhr) {
                let data = JSON.parse(xhr.responseText);
                if (typeof data === "object" && data.fieldErrors != null) {
                    $.each(data.fieldErrors, function (i, v) {
                        alert(v.field + ": " + v.message)
                    })
                } else if (typeof data === "string")
                    alert(data);
            }
        })
    };

    MyMenu.prototype.onResetForm = function () {
        let _this = this;
        _this.form.id.val(0);
        _this.form.parentId.val(0);
        _this.form.priority.val(0);
        _this.form.icon.val("").trigger("change");
    }
    ;

    MyMenu.prototype.buildControls = function () {
        this.form = this._element.find("form");
        this.form.id = this.form.find("#id");
        this.form.icon = this.form.find("#icon");
        this.form.menuGroupId = this.form.find("#menuGroupId");
        this.form.parentId = this.form.find("#parentId");
        this.form.name = this.form.find("#name");
        this.form.path = this.form.find("#path");
        this.form.description = this.form.find("#description");
        this.form.priority = this.form.find("#priority");
        this.form.btnReset = this.form.find("#btnReset");
        this.nestableContainer = this._element.find(".nestable");
        this.nestableContainer.ol = this.nestableContainer.find(".dd-list-handle");
    }
    ;

    MyMenu.prototype.buildControlItems = function () {
        this.items = this.nestableContainer.find('.list-group-item');
        this.items.updateBtn = this.items.find('.updateItem');
        this.items.removeBtn = this.items.find('.removeItem');

        this.removeItem = this.removeItem.bind(this);
        this.updateItem = this.updateItem.bind(this);
        this.renderItems = this.renderItems.bind(this);

        this.items.updateBtn.off('click', this.updateItem);
        this.items.updateBtn.on('click', this.updateItem);
        this.items.removeBtn.off('click', this.removeItem);
        this.items.removeBtn.on('click', this.removeItem);
    }
    ;

    MyMenu.prototype.removeItem = function (e) {
        let _this = this
            , item = $(e.currentTarget).closest(".list-group-item")
            , data = item.data()
            , menuId = data.id;
        _this.request("api/menu/remove/" + menuId).then(function () {
            _this.generateListItem()
        });
    }
    ;

    MyMenu.prototype.updateItem = function (e) {
        let _this = this
            , item = $(e.currentTarget).closest('.list-group-item')
            , data = item.data();
        _this.form.id.val(data.id);
        _this.form.menuGroupId.val(data.menuGroupId);
        _this.form.parentId.val(data.parentId);
        _this.form.name.val(data.name);
        _this.form.icon.val(data.icon).trigger("change");
        _this.form.priority.val(data.priority);
        _this.form.path.val(data.path);
        _this.form.description.val(data.description)
    }
    ;

    MyMenu.prototype.render = function (data, template, target, isAppend) {
        if (!Array.isArray(template))
            return false;
        let resultTemplate = template.join("\n");
        Mustache.parse(resultTemplate);
        let rendered = Mustache.render(resultTemplate, data);
        if (isAppend)
            target.append(rendered);
        else
            target.html(rendered);
    }
    ;

    MyMenu.prototype.generateListItem = function () {
        let _this = this
            , menuGroupId = this.form.menuGroupId.val();
        if(_this.pending) alert("Processing...", "warning");
        else
        return $.ajax({
            url: PATH + "api/menu/load/menu-group/" + menuGroupId,
            type: "POST",
            contentType: 'application/json',
            beforeSend: _this.beforeSend,
            complete: _this.complete,
            success: function (result) {
                _this.nestableContainer.ol.html('');
                $.each(result, function (k, v) {
                    _this.renderItems(v, _this.nestableContainer.ol);
                });
                _this.generateNestable();
                _this.buildControlItems();
                _this.items.on("change", _this.onChangeItem);
            },
            error: function (xhr) {
                let data = JSON.parse(xhr.responseText);
                if (typeof data === "object" && data.fieldErrors != null) {
                    $.each(data.fieldErrors, function (i, v) {
                        alert(v.field + ": " + v.message)
                    })
                } else if (typeof data === "string")
                    alert(data);
            }
        })
    }
    ;

    MyMenu.prototype.renderItems = function (v, container) {
        let _this = this;
        if (v.icon)
            v.iconHtml = '<i class="fa ' + v.icon + '"></i>';
        _this.render(v, _this.templateItem, container, true);
        if (v.menus.length === 0)
            return;
        let newItem = container.find("#item-" + v.id)
            , childContainer = {};
        newItem.append('<ol class="dd-list"></ol>');
        childContainer = newItem.find(".dd-list");
        $.each(v.menus, function (i, vC) {
            vC.parentId = v.id;
            _this.renderItems(vC, childContainer)
        })
    }
    ;

    MyMenu.prototype.templateItem = ['<li id="item-{{id}}" class="m-b-sm dd-item list-group-item" data-id="{{id}}" data-name="{{name}}" data-path="{{path}}" data-description="{{description}}" data-icon="{{icon}}" data-menu-group-id="{{menuGroup.id}}" data-parent-id="{{parentId}}" data-priority="{{priority}}">', '<div class="dd-content"><div class="dd-handle"><i class="fa fa-reorder text-muted"></i></div> {{&iconHtml}} {{name}} <span class="pull-right"><button class="btn btn-icon btn-sm white updateItem"><i class="btn-success fa fa-refresh"></i></button><button class="btn btn-icon btn-sm white removeItem"><i class="btn-danger fa fa-remove"></i></button> </span></div>', '</li>'];

    MyMenu.prototype.generateNestable = function () {
        let _this = this;
        this.nestableContainer.nestable({
            maxDepth: _this.options.maxDepth
        })
    }
    ;

    MyMenu.prototype.generateIconData = function () {
        return $.get(this.options.pathFontAwesome);
    }
    ;

    MyMenu.prototype.buildSelectIcon = function () {
        let _this = this;
        this.generateIconData().done(function (result) {
            _this.form.icon.select2({
                placeholder: '--Choice icon --',
                theme: 'bootstrap',
                allowClear: true,
                data: result.results,
                templateSelection: function (icon) {
                    return '<i class="fa ' + icon.id + '"></i> ' + icon.text.charAt(0).toUpperCase() + icon.text.slice(1);
                },
                templateResult: function (icon) {
                    return '<i class="fa ' + icon.id + '"></i> ' + icon.text.charAt(0).toUpperCase() + icon.text.slice(1);
                },
                escapeMarkup: function (text) {
                    return text;
                }
            })
        })
    }
    ;

    $.fn[pluginName] = function (options) {
        this.each(function () {
            if (!$.data(this, pluginName)) {
                $.data(this, pluginName, new MyMenu(this, options));
            }
        });

        return this;
    }
    ;

    return MyMenu;
}));
