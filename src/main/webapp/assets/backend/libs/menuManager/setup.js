;(function(root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('jquery'));
    } else {
        root.MyMenu = factory(root.jQuery);
    }
}(this, function($) {
    var pluginName = "myMenu";

    function MyMenu(element, options) {
        if (typeof $.fn.nestable != "function" && typeof $.fn.select2 != "function")
            return;
        var _this = this;
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

        this.form.on("submit", this.onSubmitForm);

        this.form.btnReset.on("click", this.onResetForm);

        this.generateListItem();

    }

    MyMenu.prototype.onChangeItem = function(e, b) {
        if (e.currentTarget.id != b.id)
            return;
        var _this = this
            , item = $(e.currentTarget)
            , ol = item.parent('.dd-list')
            , parent = ol.parent(".dd-item"), array = [];
        _this.oldData = Object.assign({}, item.data());
        if (_this.pending) {
            alert("Processing...");
            return;
        }

        if (parent.length > 0)
            item.data("parentId", parent.data("id"));
        else
            item.data("parentId", null);

        ol.children().each(function(i, v) {
            v = $(v);
            v.data("priority", i);
        });
        if (_this.equalDataItem(item.data()))
            return;
        ol.children().each(function(i, v) {
            v = $(v);
            array.push(_this.request("api/menu/update", JSON.stringify(v.data())));
        });
        $.when(array).then(function(){
            _this.generateListItem();
        })
    }
    ;

    MyMenu.prototype.equalDataItem = function(data) {
        var _this = this, flag = true;
        if (!this.oldData)
            return flag;
        $.each(_this.oldData, function(i, v) {
            if (data[i] != _this.oldData[i]){
                flag = false;
                return;
            }
        });
        return flag;
    }
    ;

    MyMenu.prototype.getFormData = function($form) {
        var unindexed_array = $form.serializeArray();
        var indexed_array = {};

        $.map(unindexed_array, function(n, i) {
            var value = n['value'];
            if (!isNaN(value))
                value = parseInt(value);
            if (value != '' && value != 0 && value != NaN && value != null) {
                indexed_array[n['name']] = value;
            }
        });

        return indexed_array;
    }
    ;

    MyMenu.prototype.onSubmitForm = function(e) {
        e.preventDefault();
        var form = $(e.currentTarget)
            , _this = this
            , data = _this.getFormData(form)
            , url = _this.form.id.val() != null && _this.form.id.val() > 0 ? 'api/menu/update' : 'api/menu/add';
        form.parsley().validate();
        if (form.parsley().isValid())
            _this.request(url, JSON.stringify(data)).then(function(reuslt) {
                _this.generateListItem()
            });
    }
    ;

    MyMenu.prototype.request = function(url, data) {
        var _this = this;
        if (_this.pending == true) {
            alert("Processing...");
        } else
            return $.ajax({
                url: PATH + url,
                type: "POST",
                data: data,
                contentType: 'application/json',
                error: function(xhr) {
                    var data = JSON.parse(xhr.responseText);
                    if (typeof data == "object" && data.fieldErrors != null) {
                        $.each(data.fieldErrors, function(i, v) {
                            alert(v.field + ": " + v.message)
                        })
                    } else if (typeof data == "string")
                        alert(data);
                }
            })
    }
    ;

    MyMenu.prototype.onResetForm = function(e) {
        var _this = this;
        _this.form.id.val(0);
        _this.form.parentId.val(0);
        _this.form.priority.val(0);
        _this.form.icon.val("").trigger("change");
    }
    ;

    MyMenu.prototype.buildControls = function() {
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

    MyMenu.prototype.buildControlItems = function() {
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

    MyMenu.prototype.removeItem = function(e) {
        var _this = this
            , item = $(e.currentTarget).closest(".list-group-item")
            , data = item.data()
            , menuId = data.id;
        _this.request("api/menu/remove/" + menuId).then(function(value) {
            _this.generateListItem()
        });
    }
    ;

    MyMenu.prototype.updateItem = function(e) {
        var _this = this;
        var _this = this
            , item = $(e.currentTarget).closest('.list-group-item')
            , data = item.data();
        _this.form.id.val(data.id);
        _this.form.menuGroupId.val(data.menuGroupId);
        _this.form.parentId.val(data.parentId);
        _this.form.name.val(data.name);
        _this.form.icon.val(data.icon).trigger("change");
        _this.form.priority
        _this.form.path.val(data.path);
        _this.form.description.val(data.description)
    }
    ;

    MyMenu.prototype.render = function(data, template, target, isAppend) {
        if (!Array.isArray(template))
            return false;
        var resultTemplate = template.join("\n");
        Mustache.parse(resultTemplate);
        var rendered = Mustache.render(resultTemplate, data);
        if (isAppend)
            target.append(rendered);
        else
            target.html(rendered);
    }
    ;

    MyMenu.prototype.generateListItem = function() {
        var _this = this
            , menuGroupId = this.form.menuGroupId.val();
        return $.ajax({
            url: PATH + "api/menu/load/menu-group/" + menuGroupId,
            type: "POST",
            contentType: 'application/json',
            success: function(result) {
                _this.nestableContainer.ol.html('');
                $.each(result, function(k, v) {
                    _this.renderItems(v, _this.nestableContainer.ol);
                });
                _this.generateNestable();
                _this.buildControlItems();
                _this.items.on("change", _this.onChangeItem);
            },
            error: function(xhr) {
                var data = JSON.parse(xhr.responseText);
                if (typeof data == "object" && data.fieldErrors != null) {
                    $.each(data.fieldErrors, function(i, v) {
                        alert(v.field + ": " + v.message)
                    })
                } else if (typeof data == "string")
                    alert(data);
            }
        })
    }
    ;

    MyMenu.prototype.renderItems = function(v, container) {
        var _this = this;
        if (v.icon)
            v.iconHtml = '<i class="fa ' + v.icon + '"></i>';
        _this.render(v, _this.templateItem, container, true);
        if (v.menus.length == 0)
            return;
        var newItem = container.find("#item-" + v.id)
            , childContainer = {};
        newItem.append('<ol class="dd-list"></ol>');
        childContainer = newItem.find(".dd-list");
        $.each(v.menus, function(i, vC) {
            vC.parentId = v.id;
            _this.renderItems(vC, childContainer)
        })
    }
    ;

    MyMenu.prototype.templateItem = ['<li id="item-{{id}}" class="m-b-sm dd-item list-group-item" data-id="{{id}}" data-name="{{name}}" data-path="{{path}}" data-description="{{description}}" data-icon="{{icon}}" data-menu-group-id="{{menuGroup.id}}" data-parent-id="{{parentId}}" data-priority="{{priority}}">', '<div class="dd-content"><div class="dd-handle"><i class="fa fa-reorder text-muted"></i></div> {{&iconHtml}} {{name}} <span class="pull-right"><button class="btn btn-icon btn-sm white updateItem"><i class="btn-success fa fa-refresh"></i></button><button class="btn btn-icon btn-sm white removeItem"><i class="btn-danger fa fa-remove"></i></button> </span></div>', '</li>'];

    MyMenu.prototype.generateNestable = function() {
        var _this = this;
        this.nestableContainer.nestable({
            maxDepth: _this.options.maxDepth
        })
    }
    ;

    MyMenu.prototype.generateIconData = function() {
        return $.get(this.options.pathFontAwesome);
    }
    ;

    MyMenu.prototype.buildSelectIcon = function() {
        var _this = this;
        this.generateIconData().done(function(result) {
            _this.form.icon.select2({
                placeholder: '--Choice icon --',
                allowClear: true,
                data: result.results,
                templateSelection: function(icon) {
                    return '<i class="fa ' + icon.id + '"></i> ' + icon.text.charAt(0).toUpperCase() + icon.text.slice(1);
                },
                templateResult: function(icon) {
                    return '<i class="fa ' + icon.id + '"></i> ' + icon.text.charAt(0).toUpperCase() + icon.text.slice(1);
                },
                escapeMarkup: function(text) {
                    return text;
                }
            })
        })
    }
    ;

    $.fn[pluginName] = function(options) {
        this.each(function() {
            if (!$.data(this, pluginName)) {
                $.data(this, pluginName, new MyMenu(this,options));
            }
        });

        return this;
    }
    ;

    return MyMenu;
}));
