;(function(root, factory){
    if(typeof define === 'function' && define.amd){
        define(['jquery'],factory);
    } else if (typeof exports === 'object') {
     module.exports = factory(require('jquery'));
   } else {
     root.GalleryManager = factory(root.jQuery);
   }
 }(this, function ($){
     var pluginName = "galleryManager";
 
     function GalleryManager(element, options){
         if(!window.Mustache || !window.jQuery) return;
         var defaults = {
             title: 'Quản lý hình ảnh',
             filterType: [
                 {type : 'site', name: 'Application'},
                 {type : 'user', name: 'User'},
                 {type : 'event', name: 'Event'},
                 {type : 'post', name: 'Post'},
                 {type : 'product', name: 'Product'}
             ],
             actionUpload: false,
             actionLoadImage: false,
             maxFileSize: '5M',
             filter: {
                 pageNumber: 1,
                 pageSize: 12,
                 uploadType: '',
                 datetime: new Date()
             }
         }
         this._this = this;
         this.element = $(element);
         this.settings = $.extend(true, defaults, options);
         this.render(this.settings, this.templateMain, this.element);
         this.loadImage();
         this.element.find('[ui-jp]').uiJp();
     }
 
     GalleryManager.prototype.loadImage = function(){
         var templateHtml = this.templateImage,
             container = this.element.find('#gallery-container'),
             render = this.render, _this = this._this;
 
         $.get(_this.settings.actionLoadImage, _this.settings.filter,function (result){
             if(typeof result != 'object') return;
             _this.settings.pageSize = result.currentPage;
             _this.settings.pageNumber = result.currentRow + 1;
             $.each(result.list, function(i, value){
                 value.path = PATH+value.path;
                 render(value, templateHtml, container, true);
             });
         })
     }
 
     GalleryManager.prototype.render = function(data, template, target, isAppend){
         if(!Array.isArray(template)) return false;
         var resultTemplate = template.join("\n");
         Mustache.parse(resultTemplate);
         var rendered = Mustache.render(resultTemplate, data);
         if(isAppend)
             target.append(rendered);
         else target.html(rendered);
     }
 
     GalleryManager.prototype.templateImage = [
     '<div class="col-sm-4" id="image-{{id}}">',
       '<div class="box">',
         '<div class="box-header">',
           '<h3>{{name}}</h3>',
           '<small><i class="fa-clock-o fa"></i> {{uploadDate}}</small><small class="pull-right"><i class="fa fa-user"></i> {{user.firstName}}</small>',
         '</div>',
         '<div class="box-tool">',
           '<ul class="nav">',
             '<li class="nav-item inline dropdown">',
               '<a class="nav-link" data-toggle="dropdown" aria-expanded="false">',
                 '<i class="material-icons md-18"></i>',
               '</a>',
               '<div class="dropdown-menu pull-right">',
                 '<a class="dropdown-item action-download" href="#">Download</a>',
                 '<a class="dropdown-item action-getLink" href="#">Get link</a>',
                 '<a class="dropdown-item action-delete" href="#">Delete</a>',
               '</div>',
             '</li>',
           '</ul>',
         '</div>',
         '<div class="box-body">',
             '<img src="{{path}}" alt="" class="img-responsive" />',
         '</div>',
       '</div>',
     '</div>',
     ]
 
 
 
     GalleryManager.prototype.templateMain = [
         '<h5 class="_300 margin">{{title}}n</h5>',
             '<div class="row">',
                 '<div class="col-md-3 push-md-9">',
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
                                 '<select class="form-control c-select listen-event">',
                                     '<option value="">Pick type date filter</option>',
                                     '<option value="day">Day</option>',
                                     '<option value="week">Week</option>',
                                     '<option value="month">Month</option>',
                                     '<option value="year">Year</option>',
                                 '</select>',
                                 '</div>',
                             '</div>',
                             '<div class="form-group">',
                                 '<div class="input-group date action-date" ui-jp="datetimepicker" ui-options="{',
 '                                    inline: true,',
 '                                    sideBySide: true,',
 '                                    format: \'DD/MM/YYYY\',',
 '                                    icons: {',
 '                                        time: \'fa fa-clock-o\',',
 '                                        date: \'fa fa-calendar\',',
 '                                        up: \'fa fa-chevron-up\',',
 '                                        down: \'fa fa-chevron-down\',',
 '                                        previous: \'fa fa-chevron-left\',',
 '                                        next: \'fa fa-chevron-right\',',
 '                                        today: \'fa fa-screenshot\',',
 '                                        clear: \'fa fa-trash\',',
 '                                        close: \'fa fa-remove\'',
 '                                    }',
 '                                }">',
                                 '</div>',
                             '</div>',
                         '</div>',
                     '</div>',
                 '</div>',
                 '<div class="col-md-9 pull-md-3">',
                     '<div class="b-b b-b-dark m-b-1 p-x">',
                         '<div class="row">',
                             '<div class="p-y-md clearfix nav-active-primary">',
                                 '<ul class="nav nav-pills nav-sm">',
                                     '<li class="nav-item active"><a class="nav-link" href="#"',
                                     'data-toggle="tab" data-target="#gallery"><i',
                                     'class="fa fa-image"></i> Bộ ảnh</a></li>',
                                     '<li class="nav-item"><a class="nav-link" href="#"',
                                     'data-toggle="tab" data-target="#upload"><i',
                                     'class="fa fa-upload"></i> Tải lên</a></li>',
                                 '</ul>',
                             '</div>',
                         '</div>',
                     '</div>',
                     '<div class="tab-content">',
                         '<div class="tab-pane p-v-sm active" id="gallery">',
                             '<div class="row" id="gallery-container"></div>',
                             '<div id="pagination-image" class="pagination">',
                                 
                             '</div>',
                         '</div>',
                         '<div class="tab-pane p-v-sm" id="upload">',
                             '<form id="fileupload" ui-jp="fileupload" action="{{actionUpload}}" class="POST" enctype="multipart/form-data">',
                                 '<input ui-jp="dropify" type="file" name="files" class="dropify" multiple="multiple" ui-options="{maxFileSize:\'{{maxFileSize}}\'}" />',
                                 '<div class="row fileupload-buttonbar padding">',
                                     '<div class="col-lg-3">',
                                         '<button type="submit" class="btn btn-primary start">',
                                             '<i class="glyphicon glyphicon-upload"></i> <span>Tải lên</span>',
                                         '</button>',
                                         '<button type="reset" class="btn btn-warning cancel">',
                                             '<i class="glyphicon glyphicon-ban-circle"></i> <span>Hủy</span>',
                                         '</button>',
                                         '<span class="fileupload-process"></span>',
                                     '</div>',
                                     '<div class="form-group row col-lg-4">',
                                         '<label class="col-sm-2 form-control-label">Select</label>',
                                         '<div class="col-sm-10">',
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
                     '</div>',
                 '</div>',
             '</div>'
 ];
 
 $.fn[pluginName] = function(options) {
     this.each(function() {
         if (!$.data(this, pluginName)) {
             $.data(this, pluginName, new GalleryManager(this, options));
         }
     });
 
     return this;
 };
     return GalleryManager;
 }));