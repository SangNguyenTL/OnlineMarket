;
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('jquery'));
    } else {
        root.HumanTime = factory(root.jQuery);
    }
}(this, function ($) {
    var pluginName = "humanTime";

    function HumanTime(element, options) {
        if (!window.jQuery || !window.moment) return;

        var date = new Date(), defaults = {
            date : date.toDateString(),
            format: undefined
        },
            _this = this;
        _this.element = $(element);
        _this.settings = $.extend(true, defaults, options);
        _this.settings.date != null && _this.settings.date !== 'null' &&
        _this.element.html(moment(_this.settings.date, _this.settings.format).fromNow());
    }

    $.fn[pluginName] = function (options) {
        this.each(function () {
            if (!$.data(this, pluginName)) {
                $.data(this, pluginName, new HumanTime(this, options));
            }
        });

        return this;
    };
    return HumanTime;
}));