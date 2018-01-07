// lazyload config
var MODULE_CONFIG = {
	easyPieChart : [ PATH
			+ 'assets/backend/libs/jquery/jquery.easy-pie-chart/dist/jquery.easypiechart.fill.js' ],
	sparkline : [ PATH
			+ 'assets/backend/libs/jquery/jquery.sparkline/dist/jquery.sparkline.retina.js' ],
	plot : [ PATH + 'assets/backend/libs/jquery/flot/jquery.flot.js',
			PATH + 'assets/backend/libs/jquery/flot/jquery.flot.resize.js',
			PATH + 'assets/backend/libs/jquery/flot/jquery.flot.pie.js',
			PATH + 'assets/backend/libs/jquery/flot.tooltip/js/jquery.flot.tooltip.min.js',
			PATH + 'assets/backend/libs/jquery/flot-spline/js/jquery.flot.spline.min.js',
			PATH + 'assets/backend/libs/jquery/flot.orderbars/js/jquery.flot.orderBars.js' ],
	vectorMap : [
			PATH
					+ 'assets/backend/libs/jquery/bower-jvectormap/jquery-jvectormap-1.2.2.min.js',
			PATH + 'assets/backend/libs/jquery/bower-jvectormap/jquery-jvectormap.css',
			PATH
					+ 'assets/backend/libs/jquery/bower-jvectormap/jquery-jvectormap-world-mill-en.js',
			PATH
					+ 'assets/backend/libs/jquery/bower-jvectormap/jquery-jvectormap-us-aea-en.js' ],
	dataTable : [
			PATH + 'assets/backend/libs/jquery/datatables/media/js/jquery.dataTables.min.js',
			PATH
					+ 'assets/backend/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.js',
			PATH
					+ 'assets/backend/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.css' ],
	footable : [ PATH + 'assets/backend/libs/jquery/footable/dist/footable.all.min.js',
			PATH + 'assets/backend/libs/jquery/footable/css/footable.core.css' ],
	screenfull : [ PATH + 'assets/backend/libs/jquery/screenfull/dist/screenfull.min.js' ],
	sortable : [ PATH + 'assets/backend/libs/jquery/html.sortable/dist/html.sortable.min.js' ],
	nestable : [ PATH + 'assets/backend/libs/jquery/nestable/jquery.nestable.css',
			PATH + 'assets/backend/libs/jquery/nestable/jquery.nestable.js' ],
	summernote : [ PATH + 'assets/backend/libs/jquery/summernote/dist/summernote.css',
			PATH + 'assets/backend/libs/jquery/summernote/dist/summernote.js' ],
	parsley : [ PATH + 'assets/backend/libs/jquery/parsleyjs/dist/parsley.css',
			PATH + 'assets/backend/libs/jquery/parsleyjs/dist/parsley.min.js'],
	select2 : [
			PATH + 'assets/backend/libs/jquery/select2/dist/css/select2.min.css',
			PATH
					+ 'assets/backend/libs/jquery/select2-bootstrap-theme/dist/select2-bootstrap.min.css',
			PATH
					+ 'assets/backend/libs/jquery/select2-bootstrap-theme/dist/select2-bootstrap.4.css',
			PATH + 'assets/backend/libs/jquery/select2/dist/js/select2.min.js' ],
	datetimepicker : [
			PATH
					+ 'assets/backend/libs/jquery/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css',
			PATH
					+ 'assets/backend/libs/jquery/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.dark.css',
			PATH + 'assets/backend/libs/js/moment/moment.js',
			PATH
					+ 'assets/backend/libs/jquery/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js' ],
	chart : [ PATH + 'assets/backend/libs/js/echarts/build/dist/echarts-all.js',
			PATH + 'assets/backend/libs/js/echarts/build/dist/theme.js',
			PATH + 'assets/backend/libs/js/echarts/build/dist/jquery.echarts.js' ],
	bootstrapWizard : [ PATH
			+ 'assets/backend/libs/jquery/twitter-bootstrap-wizard/jquery.bootstrap.wizard.min.js' ],
	fullCalendar : [ PATH + 'assets/backend/libs/jquery/moment/moment.js',
			PATH + 'assets/backend/libs/jquery/fullcalendar/dist/fullcalendar.min.js',
			PATH + 'assets/backend/libs/jquery/fullcalendar/dist/fullcalendar.css',
			PATH + 'assets/backend/libs/jquery/fullcalendar/dist/fullcalendar.theme.css',
			'scripts/plugins/calendar.js' ],
	dropzone : [ PATH + 'assets/backend/libs/js/dropzone/dist/min/dropzone.min.js',
			PATH + 'assets/backend/libs/js/dropzone/dist/min/dropzone.min.css' ],
	dropify : [ PATH + 'assets/backend/libs/jquery/dropify/css/dropify.min.css',
			PATH + 'assets/backend/libs/jquery/dropify/js/dropify.min.js' ],
	mymap : [
			'https://maps.googleapis.com/maps/api/js?key=' + googleApiKey,
			PATH + 'assets/backend/libs/googleMap/setup.js'
	],
	fileupload: [
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/css/jquery.fileupload.css',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/css/jquery.fileupload-ui.css',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/vendor/jquery.ui.widget.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/tmpl.min.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/load-image.all.min.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/canvas-to-blob.min.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.blueimp-gallery.min.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.iframe-transport.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.fileupload.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.fileupload-process.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.fileupload-image.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.fileupload-validate.js',
		PATH + 'assets/backend/libs/jquery/jQuery-File-Upload/js/jquery.fileupload-ui.js'
	],
	galleryManager: [
		PATH + 'assets/backend/libs/jquery/mustache/mustache.js',
		PATH + 'assets/backend/libs/jquery/gallery/gallery.image.js',
	],
	twbsPagination: [
		PATH + 'assets/backend/libs/jquery/twbs-pagination/jquery.twbsPagination.min.js'
	],
	clipboard: [
		PATH + 'assets/backend/libs/js/clipboard/clipboard.min.js',
		PATH + 'assets/backend/libs/js/clipboard/plugin.js'
	]
};
