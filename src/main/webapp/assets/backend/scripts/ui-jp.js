(function ($, MODULE_CONFIG) {
	"use strict";
  var promise = false,
	  deferred = $.Deferred();

  $.fn.uiJp = function(){

	  if(!promise)
		  promise = deferred.promise();

	  var lists  = this;

	  lists.each(function()
	  {
		  var self = $(this);
		  var options = eval('[' + self.attr('ui-options') + ']');
		  if ($.isPlainObject(options[0])) {
			  options[0] = $.extend({}, options[0]);
		  }

		  promise = uiLoad.load(MODULE_CONFIG[self.attr('ui-jp')]).then( function(){
			  self[self.attr('ui-jp')].apply(self, options);
		  });
	  });

	  deferred.resolve();

	  return promise;
  }

})(jQuery, MODULE_CONFIG);
