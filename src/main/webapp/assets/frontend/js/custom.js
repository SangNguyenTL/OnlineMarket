(function ($) {
    "use strict";

    /*----------------------------
        Slideshow
    ------------------------------ */
    $('.slideshow').owlCarousel({
        items: 6,
        autoPlay: 3000,
        singleItem: true,
        navigation: true,
        navigationText: ['<i class="fa fa-chevron-left"></i>', '<i class="fa fa-chevron-right"></i>'],
        pagination: true
    });

    /*---------------------------------------------------
        Banner Slider (with Fade in Fade Out effect)
    ----------------------------------------------------- */
    $('.banner').owlCarousel({
        items: 6,
        autoPlay: 3000,
        singleItem: true,
        navigation: false,
        pagination: false,
        transitionStyle: 'fade'
    });

    /*---------------------------------------------------
         Product Slider (with owl-carousel)
    ----------------------------------------------------- */
    $(".owl-carousel.product_carousel, .owl-carousel.latest_category_carousel, .owl-carousel.latest_brands_carousel, .owl-carousel.related_pro").owlCarousel({
        itemsCustom : [[320, 1],[600, 2],[768, 3],[992, 5],[1199, 5]],
        lazyLoad : true,
        navigation : true,
        navigationText: ['<i class="fa fa-angle-left"></i>', '<i class="fa fa-angle-right"></i>'],
        scrollPerPage : true
    });

    /*---------------------------------------------------
         Product Carousel Slider with Tab
    ----------------------------------------------------- */
    $("#latest_category .owl-carousel.latest_category_tabs").owlCarousel({
        itemsCustom : [[320, 1],[600, 2],[768, 3],[992, 5],[1199, 5]],
        lazyLoad : true,
        navigation : true,
        navigationText: ['<i class="fa fa-angle-left"></i>', '<i class="fa fa-angle-right"></i>'],
        scrollPerPage : true,
    });
    $("#latest_category .tab_content").addClass("deactive");
    $("#latest_category .tab_content:first").show();
    //Default Action
    $("#latest_category ul#sub-cat li:first").addClass("active").show(); //Activate first tab
    //On Click Event
    $("#latest_category ul#sub-cat li").on("click", function() {
        $("#latest_category ul#sub-cat li").removeClass("active"); //Remove any "active" class
        $(this).addClass("active"); //Add "active" class to selected tab
        $("#latest_category .tab_content").hide();
        var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
        $(activeTab).fadeIn(); //Fade in the active content
        return false;
    });

    /*---------------------------------------------------
         Brand Slider (Default Owl Carousel)
    ----------------------------------------------------- */
    $('#carousel').owlCarousel({
        items: 6,
        autoPlay: 3000,
        lazyLoad : true,
        navigation: true,
        navigationText: ['<i class="fa fa-angle-left"></i>', '<i class="fa fa-angle-right"></i>'],
        pagination: true
    });

    /*---------------------------------------------------
         Product Tab Carousel Slider(Featured,Latest,specila,etc..)
    ----------------------------------------------------- */
    $("#product-tab .product_carousel_tab").owlCarousel({
        itemsCustom : [[320, 1],[600, 2],[768, 3],[992, 5],[1199, 5]],
        lazyLoad : true,
        navigation : true,
        navigationText: ['<i class="fa fa-angle-left"></i>', '<i class="fa fa-angle-right"></i>'],
        scrollPerPage : true
    });
    $("#product-tab .tab_content").addClass("deactive");
    $("#product-tab .tab_content:first").show();
    //Default Action
    $("ul#tabs li:first").addClass("active").show(); //Activate first tab
    //On Click Event
    $("ul#tabs li").on("click", function() {
        $("ul#tabs li").removeClass("active"); //Remove any "active" class
        $(this).addClass("active"); //Add "active" class to selected tab
        $("#product-tab .tab_content").hide();
        var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
        $(activeTab).fadeIn(); //Fade in the active content
        return false;
    });

    /*---------------------------------------------------
        Categories Accordion
    ----------------------------------------------------- */
    $('#cat_accordion').cutomAccordion({
        saveState: false,
        autoExpand: true
    });

    /*---------------------------------------------------
        Main Menu
    ----------------------------------------------------- */
    $('#menu .nav > li > .dropdown-menu').each(function() {
        var menu = $('#menu').offset();
        var dropdown = $(this).parent().offset();

        var i = (dropdown.left + $(this).outerWidth()) - (menu.left + $('#menu').outerWidth());

        if (i > 0) {
            $(this).css('margin-left', '-' + (i + 5) + 'px');
        }
    });

    var $screensize = $(window).width();
    $('#menu .nav > li, #header .links > ul > li').on("mouseover", function() {

        if ($screensize > 991) {
            $(this).find('> .dropdown-menu').stop(true, true).slideDown('fast');
        }
        $(this).bind('mouseleave', function() {

            if ($screensize > 991) {
                $(this).find('> .dropdown-menu').stop(true, true).css('display', 'none');
            }
        });});
    $('#menu .nav > li div > ul > li').on("mouseover", function() {
        if ($screensize > 991) {
            $(this).find('> div').css('display', 'block');
        }
        $(this).bind('mouseleave', function() {
            if ($screensize > 991) {
                $(this).find('> div').css('display', 'none');
            }
        });});
    $('#menu .nav > li > .dropdown-menu').closest("li").addClass('sub');

// Clearfix for sub Menu column
    $( document ).ready(function() {
        $screensize = $(window).width();
        if ($screensize > 1199) {
            $('#menu .nav > li.mega-menu > div > .column:nth-child(6n)').after('<div class="clearfix visible-lg-block"></div>');
        }
        if ($screensize < 1199) {
            $('#menu .nav > li.mega-menu > div > .column:nth-child(4n)').after('<div class="clearfix visible-lg-block visible-md-block"></div>');
        }
    });
    $( window ).resize(function() {
        $screensize = $(window).width();
        if ($screensize > 1199) {
            $("#menu .nav > li.mega-menu > div .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.mega-menu > div > .column:nth-child(6n)').after('<div class="clearfix visible-lg-block"></div>');
        }
        if ($screensize < 1199) {
            $("#menu .nav > li.mega-menu > div .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.mega-menu > div > .column:nth-child(4n)').after('<div class="clearfix visible-lg-block visible-md-block"></div>');
        }
    });

// Clearfix for Brand Menu column
    $( document ).ready(function() {
        $screensize = $(window).width();
        if ($screensize > 1199) {
            $('#menu .nav > li.menu_brands > div > div:nth-child(12n)').after('<div class="clearfix visible-lg-block"></div>');
        }
        if ($screensize < 1199) {
            $('#menu .nav > li.menu_brands > div > div:nth-child(6n)').after('<div class="clearfix visible-lg-block visible-md-block"></div>');
        }
        if ($screensize < 991) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(4n)').after('<div class="clearfix visible-lg-block visible-sm-block"></div>');
            $('#menu .nav > li.menu_brands > div > div:last-child').after('<div class="clearfix visible-lg-block visible-sm-block"></div>');
        }
        if ($screensize < 767) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(2n)').after('<div class="clearfix visible-lg-block visible-xs-block"></div>');
            $('#menu .nav > li.menu_brands > div > div:last-child').after('<div class="clearfix visible-lg-block visible-xs-block"></div>');
        }
    });
    $( window ).resize(function() {
        $screensize = $(window).width();
        if ($screensize > 1199) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(12n)').after('<div class="clearfix visible-lg-block"></div>');
        }
        if ($screensize < 1199) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(6n)').after('<div class="clearfix visible-lg-block visible-md-block"></div>');
        }
        if ($screensize < 991) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(4n)').after('<div class="clearfix visible-lg-block visible-sm-block"></div>');
            $('#menu .nav > li.menu_brands > div > div:last-child').after('<div class="clearfix visible-lg-block visible-sm-block"></div>');
        }
        if ($screensize < 767) {
            $("#menu .nav > li.menu_brands > div > .clearfix.visible-lg-block").remove();
            $('#menu .nav > li.menu_brands > div > div:nth-child(4n)').after('<div class="clearfix visible-lg-block visible-xs-block"></div>');
            $('#menu .nav > li.menu_brands > div > div:last-child').after('<div class="clearfix visible-lg-block visible-xs-block"></div>');
        }
    });

    /*---------------------------------------------------
        Language and Currency Dropdowns
    ----------------------------------------------------- */
    $('#nav-user').hover(function() {
        $(this).find('ul').stop(true, true).slideDown('fast');
    },function() {
        $(this).find('ul').stop(true, true).css('display', 'none');
    });

    /*---------------------------------------------------
        Mobile Main Menu
    ----------------------------------------------------- */
    $('#menu .navbar-header > span').on("click", function() {
        $(this).toggleClass("active");
        $("#menu .navbar-collapse").slideToggle('medium');
        return false;
    });

//mobile sub menu plus/mines button
    $('#menu .nav > li > div > .column > div, .submenu, #menu .nav > li .dropdown-menu').before('<span class="submore"></span>');

//mobile sub menu click function
    $('span.submore').on("click", function() {
        $(this).next().slideToggle('fast');
        $(this).toggleClass('plus');
        return false;
    });
//mobile top link click
    $('.drop-icon').on("click", function() {
        $('#header .htop').find('.left-top').slideToggle('fast');
        return false;
    });

    /*---------------------------------------------------
        Increase and Decrease Button Quantity for Product Page
    ----------------------------------------------------- */
    $(".qtyBtn").on("click", function() {
        if($(this).hasClass("plus")){
            var qty = $(".qty #input-quantity").val();
            qty++;
            $(".qty #input-quantity").val(qty);
        }else{
            var qty = $(".qty #input-quantity").val();
            qty--;
            if(qty>0){
                $(".qty #input-quantity").val(qty);
            }
        }
        return false;
    });

    /*---------------------------------------------------
        Product List
    ----------------------------------------------------- */
    $('#list-view').on("click", function() {
        $(".products-category > .clearfix.visible-lg-block").remove();
        $('#content .product-layout').attr('class', 'product-layout product-list col-xs-12');
        localStorage.setItem('display', 'list');
        $('.btn-group').find('#list-view').addClass('selected');
        $('.btn-group').find('#grid-view').removeClass('selected');
        return false;
    });

    /*---------------------------------------------------
       Product Grid
    ----------------------------------------------------- */
    $(document).on('click', '#grid-view', function(e){
        $('#content .product-layout').attr('class', 'product-layout product-grid col-lg-3 col-md-3 col-sm-4 col-xs-12');

        $screensize = $(window).width();
        if ($screensize > 1199) {
            $(".products-category > .clearfix").remove();
            $('.product-grid:nth-child(4n)').after('<span class="clearfix visible-lg-block"></span>');
        }
        if ($screensize < 1199) {
            $(".products-category > .clearfix").remove();
            $('.product-grid:nth-child(4n)').after('<span class="clearfix visible-lg-block visible-md-block"></span>');
        }
        if ($screensize < 991) {
            $(".products-category > .clearfix").remove();
            $('.product-grid:nth-child(3n)').after('<span class="clearfix visible-lg-block visible-sm-block"></span>');
        }
        $( window ).resize(function() {
            $screensize = $(window).width();
            if ($screensize > 1199) {
                $(".products-category > .clearfix").remove();
                $('.product-grid:nth-child(4n)').after('<span class="clearfix visible-lg-block"></span>');
            }
            if ($screensize < 1199) {
                $(".products-category > .clearfix").remove();
                $('.product-grid:nth-child(4n)').after('<span class="clearfix visible-lg-block visible-md-block"></span>');
            }
            if ($screensize < 991) {
                $(".products-category > .clearfix").remove();
                $('.product-grid:nth-child(3n)').after('<span class="clearfix visible-lg-block visible-sm-block"></span>');
            }
            if ($screensize < 767) {
                $(".products-category > .clearfix").remove();
            }
        });
        localStorage.setItem('display', 'grid');
        $('.btn-group').find('#grid-view').addClass('selected');
        $('.btn-group').find('#list-view').removeClass('selected');
    });
    if (localStorage.getItem('display') == 'list') {
        $('#list-view').trigger('click');
    } else {
        $('#grid-view').trigger('click');
    }

    /*---------------------------------------------------
       tooltips
    ----------------------------------------------------- */
    $('[data-toggle=\'tooltip\']').tooltip({container: 'body'});

    /*---------------------------------------------------
       Scroll to top
    ----------------------------------------------------- */
    $(function () {
        $(window).scroll(function () {
            if ($(this).scrollTop() > 150) {
                $('#back-top').fadeIn();
            } else {
                $('#back-top').fadeOut();
            }
        });
    });
    $('#back-top').on("click", function() {
        $('html, body').animate({scrollTop:0}, 'slow');
        return false;
    });

    /*---------------------------------------------------
       Facebook Side Block
    ----------------------------------------------------- */
    $(function(){
        $("#facebook.fb-left").hover(function(){
                $(this).stop(true, false).animate({left: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({left: "-241px" }, 800, 'easeInQuint' );
            },1000);
    });
    $(function(){
        $("#facebook.fb-right").hover(function(){
                $(this).stop(true, false).animate({right: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({right: "-241px" }, 800, 'easeInQuint' );
            },1000);
    });

    /*---------------------------------------------------
       Twitter Side Block
    ----------------------------------------------------- */
    $(function(){
        $("#twitter_footer.twit-left").hover(function(){
                $(this).stop(true, false).animate({left: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({left: "-215px" }, 800, 'easeInQuint' );
            },1000);
    });
    $(function(){
        $("#twitter_footer.twit-right").hover(function(){
                $(this).stop(true, false).animate({right: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({right: "-215px" }, 800, 'easeInQuint' );
            },1000);
    });

    /*---------------------------------------------------
       Video Side Block
    ----------------------------------------------------- */
    $(function(){
        $("#video_box.vb-left").hover(function(){
                $(this).stop(true, false).animate({left: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({left: "-566px" }, 800, 'easeInQuint' );
            },1000);
    });
    $(function(){
        $("#video_box.vb-right").hover(function(){
                $(this).stop(true, false).animate({right: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({right: "-566px" }, 800, 'easeInQuint' );
            },1000);
    });

    /*---------------------------------------------------
       Custom Side Block
    ----------------------------------------------------- */
    $(function(){
        $('#custom_side_block.custom_side_block_left').hover(function(){
                $(this).stop(true, false).animate({left: '0' }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({left: '-215px' }, 800, 'easeInQuint' );
            },1000);
    });
    $(function(){
        $("#custom_side_block.custom_side_block_right").hover(function(){
                $(this).stop(true, false).animate({right: "0" }, 800, 'easeOutQuint' );
            },
            function(){
                $(this).stop(true, false).animate({right: "-215px" }, 800, 'easeInQuint' );
            },1000);
    });

    if(typeof $.notify !== 'function') return;
    window.alert = function(msg, type){
        type = type || "success";
        $.notify({
            message: msg
        },{
            type: type
        });
    };
    $.cookie.json = true;
    $(".compare-product").on("click", function(e){
        let element = $(e.currentTarget),
            compareList = $.cookie("compareList") || {},
            id= Number.parseInt(element.data("id")),
            cateId= Number.parseInt(element.data("cateId"));
        if(typeof id === "number"  && typeof cateId === "number"){
            if(typeof compareList[cateId] === "object" && compareList[cateId].filter(function(item){return item === id}).length > 0){
                compareList[cateId] = compareList[cateId].filter(function(item){return item !== id});
                if(compareList[cateId].length === 0) delete (compareList[cateId]);
                alert("Removed product to compare factory", "warning")
            }else{
                compareList[cateId] = compareList[cateId] || [];
                compareList[cateId].push(id);
                if(compareList[cateId].length > 3)
                    compareList[cateId] = compareList[cateId].slice(-3);
                alert("Added product to compare factory")
            }
            $.cookie("compareList", compareList, {path:"/"});
        }
    });
    $(".remove-compare").on("click", function (e) {
        let element = $(e.currentTarget),
            compareList = $.cookie("compareList") || {},
            id= Number.parseInt(element.data("id")),
            cateId= Number.parseInt(element.data("cateId"));
        if(typeof id === "number" && typeof cateId === "number"){
            if(typeof compareList[cateId] === "object" && compareList[cateId].filter(function(item){return item === id}).length > 0){
                compareList[cateId] = compareList[cateId].filter(function(item){return item !== id});
                if(compareList[cateId].length === 0) delete compareList[cateId];
                alert("Removed product to compare factory", "warning")
            }
            $.cookie("compareList", compareList, {path:"/"});
        }
        location.reload();
    });

    simpleCart.currency({
        code: "VND" ,
        symbol: "&#x20AB;" ,
        name: "Viet nam dong",
        accuracy: 0,
        delimiter: " ",
        decimal: ".",
        after: true
    });

    simpleCart({
        // array representing the format and columns of the cart,
        // see the cart columns documentation
        cartColumns: [
            { attr: "image", label: "Image", view: 'image'},
            {
                label: "Name",
                view: function(item, column){
                    let link = item.get("pagelink");
                    link = link ? link : "#";
                    return '<a href="'+link+'">'+item.get("name")+'</a>';
                }
            },
            { view: "decrement" , label: false , text: "-" } ,
            { attr: "quantity", label: "Qty"},
            { view: "increment" , label: false , text: "+" } ,
            { view: "currency", attr: "total", label: "Sub Total" },
            { view: "remove", text: "<i class='fa fa-close text-info'></i>", label: false}
        ],
        // "div" or "table" - builds the cart as a
        // table or collection of divs
        cartStyle: "table",
        // how simpleCart should checkout, see the
        // checkout reference for more info
        checkout: {
            type: "SendForm" ,

            url: PATH+"check-out",

            // http method for form, "POST" or "GET", default is "POST"
            method: "GET"
        },
        // set the currency, see the currency
        // reference for more info
        currency: "VND",
        // collection of arbitrary data you may want to store
        // with the cart, such as customer info
        data: {},
        // set the cart langauge
        // (may be used for checkout)
        language: "english-us"
    });

    simpleCart.bind( "afterAdd" , function( item ){
        alert( item.get("name") + " was added to the cart!" );
    });

    simpleCart.bind( 'beforeRemove' , function( item ){
        alert( item.get('name') + " was removed from the cart" , "warning");
    });

    simpleCart.bind( 'update' , function(){
        let cart = JSON.parse(window.localStorage.getItem("simpleCart_items")) || {};
        if(typeof cart === "object"){
            let cartCookies = $.cookie("cart");
            if(window.location.search.match(/check-out=success/)){
                window.localStorage.setItem("simpleCart_items", "{}");
                $.cookie("cart", [], {path:"/"});
            }else{
                cartCookies = [];
                for(let key in cart){
                    if(cart.hasOwnProperty(key) && cart[key].hasOwnProperty("quantity"))
                        cartCookies.push({
                            id: key,
                            quantity: cart[key].quantity
                        })
                }
                $.cookie("cart", cartCookies, {path:"/"});
            }
        }
    });

    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr) {
        xhr.setRequestHeader(header, token);
    });

    setInterval(function(){
        if(userLogged)
            $.ajax({
                url: PATH + 'api/notification/count',
                success: function (data) {
                    if(data.error)
                        if(data.message !== "User not found")
                            alert(data.message, "warning");
                        else {
                            alert("Session invalid", "warning");
                            window.location.reload()
                        }
                    else{
                        let notifyA = $(".notification-a"),
                            count = Number.parseInt(data.message);
                        if(notifyA.length > 0){
                            if(notifyA.find(".count").length>0){
                                if(count > 0)
                                    notifyA.find(".count").html(data.message);
                                else notifyA.find(".count").remove();
                            }else {
                                if(count > 0)
                                    notifyA.append('<span class="count">'+data.message+'</span>')
                            }
                        }
                    }
                }
            })
    }, (5 * 1000));

    window.searchProduct = {
        width: "100%",
        placeholder: 'Search product...',
        minimumInputLength: 2,
        theme: 'bootstrap',
        ajax: {
            delay: 250,
            url: PATH+'api/product/search',
            dataType: 'json',
            data: function (params) {
                let query = {
                    q: params.term,
                    type: 'public',
                    page: params.page || 1
                };

                // Query parameters will be ?search=[term]&type=public
                return query;
            },
            processResults: function (data) {
                data.results =  $.map(data.results, function (obj) {
                    obj.text = obj.name;
                    obj.id = window.location.origin+PATH+"product/"+obj.slug;
                    return obj;
                });
                return data;
            }
        },
        escapeMarkup: function(m) {
            // Do not escape HTML in the select options text
            return m;
        },
        templateResult: productTemplate,
        templateSelection: productTemplate,
        cache: true
    };

    function productTemplate(product){
        if (product.loading) return product.text;
        if(!product.slug) return product.text+"<i class='fa fa-search pull-right'></i>";
        return "<a class='text-black' href='"+window.location.origin+PATH+"product/"+product.slug+"'>"+product.name+"</a>";
    }

    $(document).find("select.searchProduct").select2(window.searchProduct);
    $(document).on('click', '#cart .dropdown-menu', function (e) {
        e.stopPropagation();
    });
})(jQuery);