;
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('jquery'));
    } else {
        root.GalleryManager = factory(root.jQuery);
    }
}(this, function ($) {
    var pluginName = "galleryManager";

    function GalleryManager(element, options) {
        if (!window.Mustache || !window.jQuery) return;
        var defaults = {
            title: false,
            filterType: [{
                    type: 'site',
                    name: 'Application'
                },
                {
                    type: 'user',
                    name: 'User'
                },
                {
                    type: 'event',
                    name: 'Event'
                },
                {
                    type: 'post',
                    name: 'Post'
                },
                {
                    type: 'product',
                    name: 'Product'
                },
                {
                    type: 'brand',
                    name: 'Brand'
                }
            ],
            actionUpload: false,
            actionDelete: false,
            actionLoadImage: false,
            maxFileSize: '5M',
            filter: {
                pageNumber: 1,
                pageSize: 12,
                uploadType: '',
                dateType: '',
                datetime: ''
            },
            totalPage: 0,
            modal: false
        },
        _this = this;
        this.element = $(element);
        this.settings = $.extend(true, defaults, options);
        this.render(this.settings, this.templateMain, this.element);
        this.selectTypeElement = this.element.find('#selectTypeDate');
        this.tabGalleryElement = $('[data-target=#gallery]');
        this.spinner = this.tabGalleryElement.find(".fa-spinner");
        this.datePickerElement = this.element.find('#datePicker');
        this.typeElement = this.element.find('.filterType');
        this.paginationElement = this.element.find('#pagination-image');
        this.containerImage = this.element.find('#gallery-container');
        this.init = this.init.bind(this);
        this.element.find('[ui-jp]').uiJp().then(this.init);
        this.tabGalleryElement.on("click", function(){
            if(!_this.spinner.hasClass("hide")) return;
            _this.loadImage();
        });
    }

    GalleryManager.prototype.init = function(){

        this.pending = false;
        this.loadImage();

        this.onSelectTypeDate = this.onSelectTypeDate.bind(this);
        this.onChangeDatePicker = this.onChangeDatePicker.bind(this);
        this.onChangeUploadType = this.onChangeUploadType.bind(this);
        this.listen();
    };

    GalleryManager.prototype.listen = function(){
        this.selectTypeElement.on('change', this.onSelectTypeDate);
        this.datePickerElement.on('dp.change', this.onChangeDatePicker);
        this.typeElement.on('change', this.onChangeUploadType);
    };

    GalleryManager.prototype.onChangeUploadType = function () {
        this.typeElement.off('change', this.onChangeUploadType);
        this.settings.filter.uploadType = this.element.find('.filterType:checked').map(function (_, e) {
            return $(e).val();
        }).get().join(',');
        this.loadImage();
    };

    GalleryManager.prototype.onChangeDatePicker = function (date) {
        if(date.date === date.oldDate) return;
        this.settings.filter.datetime = moment(date.date, "yyyy-MM-dd").format("YYYY-MM-DD");
        this.loadImage();
    };

    GalleryManager.prototype.onSelectTypeDate = function (e) {
        this.selectTypeElement.off('change', this.onSelectTypeDate);
        var element = $(e.currentTarget);
        switch (element.val()) {
            case 'day':
                this.datePickerElement.data('DateTimePicker').viewMode('days');
                break;
            case 'month':
                this.datePickerElement.data('DateTimePicker').viewMode('months');
                break;
            case 'year':
                this.datePickerElement.data('DateTimePicker').viewMode('years');
                break;
            default:
                break;
        }
        this.settings.datetime = this.datePickerElement.data('DateTimePicker').date();
        this.settings.filter.dateType = element.val();
        this.loadImage();
    };

    GalleryManager.prototype.onDelete = function (e) {
        this.actionDeleteElement.off('click', this.onDelete);
        if (!this.settings.actionDelete) return;
        var box = $(e.target).closest('.image-item');
        $.post(this.settings.actionDelete+'/'+ $(e.currentTarget).data('id'), {}, function (result) {
            if (!result.error) 
            box.remove();
            alert(result.message);
        });
        this.actionDeleteElement.on('click', this.onDelete);
    };

    GalleryManager.prototype.selectImageElement = function (e) {
        var element = $(e.currentTarget),
            box = element.find(".box");
        this.imageElement.off('click', this.selectImageElement);
        if (box.hasClass('info')) {
            box.removeClass("info");
            this.imageLink = "";
        } else {
            this.containerImage.find(".info").removeClass("info");
            box.addClass("info");
            this.imageLink = box.data("path");
        }
        this.imageElement.on('click', this.selectImageElement);
    };

    GalleryManager.prototype.loadImage = function () {
        var templateHtml = this.templateImage,
            render = this.render,
            _this = this;
        if(this.pending) return;
        this.pending = true;
        $.ajax({
            url: _this.settings.actionLoadImage,
            data: _this.settings.filter,
            method: 'POST',
            beforeSend: function(){
                _this.pending = true;
                if(_this.spinner.hasClass("hide")) _this.spinner.removeClass("hide");
            },
            success:  function (result) {
                if (typeof result !== 'object') return;
                _this.totalPage = Math.ceil(result.totalRow / _this.settings.filter.pageSize);
                _this.settings.filter.pageNumber = result.currentPage;
                _this.containerImage.html('');
                if(_this.totalPage === 1 && _this.settings.filter.pageNumber > 1 ) return;
                $.each(result.list, function (i, value) {
                    value.oldPath = value.path;
                    value.path = window.location.origin + PATH_IMAGE + value.path;
                    render(value, templateHtml, _this.containerImage, true);
                });
                _this.imageElement = _this.containerImage.find('.image-item');
                if(_this.totalPage > 0){
                    _this.paginationElement.twbsPagination('destroy');
                    _this.paginationElement.twbsPagination({
                        totalPages: _this.totalPage,
                        startPage: result.currentPage,
                        visiblePages: 6,
                        onPageClick: function (e, page) {
                            _this.settings.filter.pageNumber = page;
                            if(!_this.pending)
                            _this.loadImage();
                        }
                    });
                }
                if (_this.imageElement === undefined || _this.imageElement.length < 1) return;
                    _this.actionDeleteElement = _this.imageElement.find('.action-delete');
                    _this.onDelete = _this.onDelete.bind(_this);
                    _this.actionDeleteElement.on('click', _this.onDelete);

                    if (_this.settings.modal) {
                        _this.selectImageElement = _this.selectImageElement.bind(_this);
                        _this.imageElement.on('click', _this.selectImageElement);
                    }
            },
            complete: function(){
                if(_this.totalPage === 1 && _this.settings.filter.pageNumber > 1 ) {
                    _this.settings.filter.pageNumber = 1;
                    _this.pending = false;
                    _this.loadImage();
                    return;
                }
                _this.pending = false;
                if(!_this.spinner.hasClass("hide")) _this.spinner.addClass("hide");
                _this.listen();
                var clipboard = new Clipboard('.clipboard-btn');
                clipboard.on('success', function(e) {
                    alert('Link was saved to clipboard!');
                });
                
            }
            
            
        });
    }

    GalleryManager.prototype.render = function (data, template, target, isAppend) {
        if (!Array.isArray(template)) return false;
        var resultTemplate = template.join("\n");
        Mustache.parse(resultTemplate);
        var rendered = Mustache.render(resultTemplate, data);
        if (isAppend)
            target.append(rendered);
        else target.html(rendered);
    }

    GalleryManager.prototype.templateImage = [
        '<div class="col-sm-4 image-item" id="image-{{id}}">',
        '<div class="box" data-path="{{oldPath}}">',
        '<div class="box-header">',
        '<h3 class="text-truncate">{{name}}</h3>',
        '<small class="pull-left"><i class="fa-clock-o fa"></i> {{uploadDate}}</small><small class="pull-right"><i class="fa fa-user"></i> {{user.firstName}}</small>',
        '</div>',
        '<div class="box-tool">',
        '<ul class="nav">',
        '<li class="nav-item inline dropdown">',
        '<a class="nav-link" data-toggle="dropdown" aria-expanded="false">',
        '<i class="material-icons md-18"></i>',
        '</a>',
        '<div class="dropdown-menu pull-right">',
        '<a class="dropdown-item" href="{{path}}" download>Download</a>',
        '<a class="dropdown-item clipboard-btn" data-clipboard-text="{{path}}" href="#">Get link</a>',
        '<a class="dropdown-item action-delete" data-id="{{id}}" href="#">Delete</a>',
        '</div>',
        '</li>',
        '</ul>',
        '</div>',
        '<div class="box-body">',
        '<img src="{{path}}" class="img-responsive" alt="..." style="{height: 270px}"/>',
        '</div>',
        '</div>',
        '</div>',
    ]



    GalleryManager.prototype.templateMain = [
        '{{#title}}<h5 class="_300 margin">{{title}}</h5>{{/title}}',
        '<div class="b-b b-b-dark m-b-1 p-x">',
        	'<div class="row">',
        		'<div class="p-y-md clearfix nav-active-primary">',
			        '<ul class="nav nav-pills nav-sm">',
			        '<li class="nav-item active"><a class="nav-link" href="#"',
			        'data-toggle="tab" data-target="#gallery"><i class="fa fa-image"></i> Image collection <i class="fa fa-spinner"></i></a></li>',
			        '<li class="nav-item"><a class="nav-link" href="#" data-toggle="tab" data-target="#upload"><i class="fa fa-upload"></i> Upload</a></li>',
			        '</ul>',
		        '</div>',
	        '</div>',
        '</div>',
        '<div class="tab-content">',
        	'<div class="tab-pane p-v-sm active" id="gallery">',
        		'<div class="row">',
			        '<div class="col-md-4 push-md-8">',
			        '<div class="box">',
			        '<div class="box-header">Image Filter</div>',
			        '<div class="box-body">',
			        '<ul class="list-unstyled m-t-n-sm">',
			        '{{#filterType}}',
			        '<li class="checkbox"><label class="ui-check"> <input type="checkbox" name="filterType[]" class="filterType listen-event" value="{{type}}"><i></i> {{name}}',
			        '</label></li>',
			        '{{/filterType}}',
			        '</ul>',
			        '<div class="line line-dashed b-b m-b"></div>',
			        '<div class="form-group row">',
			        '<label for="inputPassword3" class="col-sm-2 form-control-label">Date</label>',
			        '<div class="col-sm-10">',
			        '<select class="form-control c-select" id="selectTypeDate">',
			        '<option value="">Pick type date filter</option>',
			        '<option value="day">Day</option>',
			        '<option value="week">Week</option>',
			        '<option value="month">Month</option>',
			        '<option value="year">Year</option>',
			        '</select>',
			        '</div>',
			        '</div>',
			        '<div class="form-group">',
			        '<div class="input-group date action-date" id="datePicker" ui-jp="datetimepicker" ui-options="{',
			        'inline: true,',
			        'sideBySide: true,',
			        'maxDate: new Date(),',
			        'format: \'DD/MM/YYYY\',',
			        'icons: {',
			        'time: \'fa fa-clock-o\',',
			        'date: \'fa fa-calendar\',',
			        'up: \'fa fa-chevron-up\',',
			        'down: \'fa fa-chevron-down\',',
			        'previous: \'fa fa-chevron-left\',',
			        'next: \'fa fa-chevron-right\',',
			        'today: \'fa fa-screenshot\',',
			        'clear: \'fa fa-trash\',',
			        'close: \'fa fa-remove\'',
			        '}',
			        '}">',
			        '</div>',
			        '</div>',
			        '</div>',
			        '</div>',
			        '</div>',
			        
			        '<div class="col-md-8 pull-md-4">',
        	        
        	        '<div class="row" id="gallery-container" ui-jp="clipboard" data-class="clipboard-btn"></div>',
        	        '<div ui-jp="twbsPagination" id="pagination-image" class="pagination"></div>',
    	        '</div>',
		        '</div>',
	        '</div>',
	        '<div class="tab-pane p-v-sm" id="upload">',
	        	'<form id="fileupload" ui-jp="fileupload" action="{{actionUpload}}" class="POST" enctype="multipart/form-data">',
	        	'<input ui-jp="dropify" type="file" name="files" class="dropify" multiple="multiple" ui-options="{maxFileSize:\'{{maxFileSize}}\'}" />',
		        '<div class="row fileupload-buttonbar padding">',
		        '<div class="col-lg-3">',
		        '<button type="submit" class="btn btn-primary start">',
		        '<i class="glyphicon glyphicon-upload"></i> <span>Upload</span>',
		        '</button>',
		        '<button type="reset" class="btn btn-warning cancel">',
		        '<i class="glyphicon glyphicon-ban-circle"></i> <span>Cancel</span>',
		        '</button>',
		        '<span class="fileupload-process"></span>',
		        '</div>',
		        '<div class="form-group row col-lg-4">',
		        '<label class="form-control-label">Select</label>',
		        '<div>',
		        '<select class="form-control input-c" name="uploadType">',
		        '{{#filterType}}',
		        '<option value="{{type}}"> {{name}}</option>',
		        '{{/filterType}}',
		        '</select>',
		        '</div>',
		        '</div>',
		        '<!-- The global progress state -->',
		        '<div class="col-lg-5 fileupload-progress fade">',
		        '<!-- The global progress bar -->',
		        '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">',
		        '<div class="progress-bar progress-bar-success" style="width: 0%;"></div>',
		        '</div>',
		        '<!-- The extended global progress state -->',
		        '<div class="progress-extended">&nbsp;</div>',
		        '</div>',
		        '<table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>',
		        '</div>',
		        '</form>',
	        '</div>',
        '</div>'
    ];

    $.fn[pluginName] = function (options) {
        this.each(function () {
            if (!$.data(this, pluginName)) {
                $.data(this, pluginName, new GalleryManager(this, options));
            }
        });

        return this;
    };
    return GalleryManager;
}));