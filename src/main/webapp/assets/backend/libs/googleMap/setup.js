;$.fn.select2.amd.define('select2/data/googleAutocompleteAdapter', ['select2/data/array', 'select2/utils'],
    function (ArrayAdapter, Utils) {
        function GoogleAutocompleteDataAdapter ($element, options) {
            GoogleAutocompleteDataAdapter.__super__.constructor.call(this, $element, options);
        }

        Utils.Extend(GoogleAutocompleteDataAdapter, ArrayAdapter);

        GoogleAutocompleteDataAdapter.prototype.query = function (params, callback) {
            var returnSuggestions = function(predictions, status)
            {
                var data = {results: []};
                if (status != google.maps.places.PlacesServiceStatus.OK) {
                    callback(data);
                }
                if(predictions !=null && predictions.length > 0)
                for(var i = 0; i< predictions.length; i++)
                {
                    data.results.push({id:predictions[i].description, text: predictions[i].description});
                }else{
                    data.results.push({id:params.term, text:params.term})
				}
                data.results.push({id:' ', text: 'Powered by Google', disabled: true});
                callback(data);
            };

            if(params.term && params.term != '')
            {
                var service = new google.maps.places.AutocompleteService();
                service.getPlacePredictions({ input: params.term }, returnSuggestions);
            }
            else
            {
                var data = {results: []};
                data.results.push({id:' ', text: 'Powered by Google', disabled: true});
                callback(data);
            }
        };
        return GoogleAutocompleteDataAdapter;
    }
);
function formatRepo (repo) {
    if (repo.loading) {
        return repo.text;
    }

    var markup = "<div class='select2-result-repository clearfix'>" +
        "<div class='select2-result-title'>" + repo.text + "</div>";
    return markup;
}

function formatRepoSelection (repo) {
    return repo.text;
}

var googleAutocompleteAdapter = $.fn.select2.amd.require('select2/data/googleAutocompleteAdapter');

$('.adress-autocomplete').select2({
    width: '100%',
    dataAdapter: googleAutocompleteAdapter,
    placeholder: 'Search Adress',
    escapeMarkup: function (markup) { return markup; },
    minimumInputLength: 2,
    templateResult: formatRepo,
    templateSelection: formatRepoSelection
});

;
(function(root, factory) {
	if (typeof define === 'function' && define.amd) {
		define([ 'jquery' ], factory);
	} else if (typeof exports === 'object') {
		module.exports = factory(require('jquery'));
	} else {
		root.MyMap = factory(root.jQuery);
	}
}(this, function($) {
	var pluginName = "myMap";
	function MyMap(element, options) {
		if (!google.maps.Geocoder)
			return;

		var defaults = {
			coordinate : {
				lat : -34.397,
				lng : 150.644
			},
			zoom : 13,
			marker : {
				draggable : true
			},
			outId : {
				lat : '',
				lng : ''
			},
			search : ''
		};

		this.element = element;
		this.input = $(element);
		this.settings = $.extend(true, defaults, options, this.input
				.data(pluginName));
		this.geocoder = new google.maps.Geocoder();
		if (this.settings.outId.lat != '' && this.settings.outId.lng != '') {
			this.latInput = $('#' + this.settings.outId.lat);
			this.lngInput = $('#' + this.settings.outId.lng);
		}
		
		if(this.settings.coordinate.lat == 0 || this.settings.coordinate.lng == 0){
			this.settings.coordinate = defaults.coordinate;
		}

		this.onSearch = this.onSearch.bind(this);
		this.toggleBounce = this.toggleBounce.bind(this);

		this.initMap();

        if (this.settings.search != "") {
			this.searchInput = $("#" + this.settings.search);
			this.searchInput.select2(this.onSearch2);
			this.searchInput.on("change", this.onSearch);
		}

	}

	MyMap.prototype.initMap = function() {
		this.map = new google.maps.Map(this.element, {
			center : this.settings.coordinate,
			zoom : this.settings.zoom,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		});

		if (typeof this.settings.marker != "object")
			return;

		this.marker = new google.maps.Marker({
			map : this.map,
			draggable : this.settings.marker.draggable,
			animation : google.maps.Animation.DROP,
			position : this.settings.coordinate
		});

		this.marker.addListener('click', this.toggleBounce);

	};

	MyMap.prototype.onSearch2 = {
        width: '100%',
        dataAdapter: googleAutocompleteAdapter,
        placeholder: 'Search Adress',
        escapeMarkup: function (markup) { return markup; },
        minimumInputLength: 2,
        templateResult: formatRepo,
		theme: 'bootstrap',
        templateSelection: formatRepoSelection
    };

	MyMap.prototype.onSearch = function(e) {

		if (this.map == undefined || this.marker == undefined
				|| this.searchInput.val() < 5)
			return;
		var geocoder = new google.maps.Geocoder(), _this = this;
		geocoder.geocode({
			'address' : this.searchInput.val()
		}, function(results, status) {

			if (status == google.maps.GeocoderStatus.OK) {
                _this.settings.coordinate.lat = results[0].geometry.location
						.lat();
                _this.settings.coordinate.lng = results[0].geometry.location
						.lng();
                _this.latInput.val(_this.settings.coordinate.lat);
                _this.lngInput.val(_this.settings.coordinate.lng);
			}
            _this.initMap();
		});
	};

	MyMap.prototype.toggleBounce = function() {
		if (this.marker.getAnimation() !== null) {
			this.marker.setAnimation(null);
		} else {
			this.marker.setAnimation(google.maps.Animation.BOUNCE);
			this.latInput.val(this.marker.position.lat());
			this.lngInput.val(this.marker.position.lng());
		}
	};

	$.fn[pluginName] = function(options) {
		this.each(function() {
			if (!$.data(this, pluginName)) {
				$.data(this, pluginName, new MyMap(this, options));
			}
		});

		return this;
	};

	return MyMap;
}));