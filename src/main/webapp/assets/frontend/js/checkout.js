;(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('jquery'));
    } else {
        root.CheckOut = factory(root.jQuery);
    }
}(this, ($) => {
    let pluginName = "checkOut";

    Number.prototype.format = function(n, x, s, c) {
        let re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
            num = this.toFixed(Math.max(0, ~~n));

        return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
    };

    $.put = function(url, data, callback, type){

        if ( $.isFunction(data) ){
            type = type || callback,
                callback = data,
                data = {}
        }

        return $.ajax({
            url: url,
            type: 'PUT',
            success: callback,
            data: data,
            contentType: type
        });
    };

    $.post = function(url, data, callback, type){

        if ( $.isFunction(data) ){
            type = type || callback,
                callback = data,
                data = {}
        }

        return $.ajax({
            url: url,
            type: 'POST',
            dataType: 'json',
            success: callback,
            data: data,
            contentType: type
        });
    };

    function CheckOut(element, options){
        let defaultOption = {

        };

        this.element = element;
        this._element = $(element);
        this.options  = $.extend(true, defaultOption, options);
        this.childElement = {
            addressForm: this._element.find("#addressForm"),
            addressModal: this._element.find("#addAddress"),
            cartTable: this._element.find("#cartTable"),
            addressInputSelect: this._element.find("#address"),
            productRow: this._element.find("tr[class*=product-]")
        };

        this.pending = false;

        this.init();
        this.listen();
    }

    CheckOut.prototype.init = function (){

        this.processCartRow();
        this.childElement.addressForm.parsley();
        this.addAddress = this.addAddress.bind(this);
        this.updateProduct = this.updateProduct.bind(this);
        this.removeProduct = this.removeProduct.bind(this);
        this.OnAddressSelectChange = this.OnAddressSelectChange.bind(this);
        this.checkCode = this.checkCode.bind(this);
        this.removeCode = this.removeCode.bind(this);
        this.getAddressList = this.getAddressList.bind(this);

    };

    CheckOut.prototype.listen = function (){
        this.childElement.addressForm.on("submit", this.addAddress);
        this.childElement.cartTable.on("click", ".updateProduct", this.updateProduct);
        this.childElement.cartTable.on("change", ".productQuantity", this.updateProduct);
        this.childElement.cartTable.on("click", ".removeProduct", this.removeProduct);
        this.childElement.addressInputSelect.on("change", this.OnAddressSelectChange);

        this._element.on("click", ".refreshAddress", this.getAddressList);
        this._element.on("click", ".removeCode", this.removeCode);
        this._element.on("click", "#button-coupon", this.checkCode);
    };

    CheckOut.prototype.loadEvent = function(e){
        let listEvent = this._element.find(".listEvent");
        for(var i = 0; i < listEvent.length; i++){
            let el = $(listEvent[i]), id = el.data("id");

        }
    };

    CheckOut.prototype.OnAddressSelectChange = function (e) {
        let _this = this,
            target = $(e.target), id = target.val() || 0;
        if(!id) return;
        if(_this.pending){
            alert("Pending... please wait.")
            return;
        }
        _this.pending = true;
        $.get(PATH+"api/address",{addressId: id}, result => {
            if(!result.error && _this.totalCart.feeShip.length > 0 && result.list.length > 0){
                let fee = result.list[0].province.shippingFee || 0;
                _this.totalCart.feeShip.text(fee.format(0, 3, " "));
                _this.processCartRow();
            }else{
                alert("Address not found.");
                _this.pending = false;
                _this.getAddressList();
            }
        }).always(()=>{
            _this.pending = false;
        });
    };

    CheckOut.prototype.addAddress = function (e){
        e.preventDefault();
        let _this = this;
        if(_this.pending){
            alert("Pending... please wait.");
            return;
        }
        _this.pending = true;
        this.childElement.addressForm.find(".parsley-errors-list").remove();
        this.childElement.addressForm.find(".parsley-error").removeClass("parsley-error");
        if(this.childElement.addressForm.parsley().validate()){
            $.put(PATH + 'api/address', JSON.stringify(this.getFormData(this.childElement.addressForm)),(result) => {
                if(result.error){
                    if(result.message) alert(message);
                    else if(result.validationErrorDTO.fieldErrors && result.validationErrorDTO.fieldErrors.length > 0){
                        $.each(result.validationErrorDTO.fieldErrors,(index, value)=>{
                            let input = _this.childElement.find("[name='"+value.field+"']");
                            if(input.length > 0){
                                input.after("<ul class='parsley-errors-list'/>");
                                let ul = $intput.closest(".form-group").find("ul");
                                _this.childElement.find("[name='"+value.field+"']").addClass("parsley-error");
                                ul.append("<li>"+value.message+"</li>");
                            }
                        })
                    }
                }else {
                    _this.childElement.addressForm[0].reset();
                    _this.childElement.addressModal.modal("toggle");
                    alert("Add success!");

                    if(this.childElement.addressInputSelect.length === 0) return;
                    _this.pending = false;
                    _this.getAddressList();
                }
            } ,"application/json")
                .always(()=>{
                    _this.pending = false;
                });
        }
        return false
    };

    CheckOut.prototype.processCartRow = function (){
        let _this = this;
        this.cart = {};
        this.totalCart = {
            totalWeightAll : this.childElement.cartTable.find(".totalWeight"),
            totalPaid : this.childElement.cartTable.find(".totalPaidReal"),
            totalPaidShip : this.childElement.cartTable.find(".totalPaidShip"),
            feeShip : this.childElement.cartTable.find(".feeShip"),
            totalShip: this.childElement.cartTable.find(".totalShip"),
            sale: this.childElement.cartTable.find(".sale"),
            numbers:{
                totalWeightAll: 0,
                totalShip: 0,
                totalPaid: 0,
                totalPaidShip: 0,
                sale: 0,
                originalTotalPaid: 0
            }
        };
        _this.totalCart.numbers.provinceFeeShip = Number.parseInt(_this.totalCart.feeShip.text().replace(/ /g, ''));
        this.childElement.productRow = this._element.find("tr[class*=product-]");
        this.childElement.productRow.each((i, v) => {
            let el = $(v), id = el.attr("class").match(/product-(\d+)/)[1];

            _this.cart[id] = {
                id: id,
                el: el,
                unitPrice : el.find(".unitPrice.price-new"),
                unitPriceOld : el.find(".unitPrice.price-old"),
                totalPrice : el.find(".totalPrice.price-new"),
                totalPriceOld : el.find(".totalPrice.price-old"),
                unitWeight : el.next("tr").find(".unitWeight"),
                totalWeight: el.next("tr").find(".totalWeightRow"),
                quantity: el.find("input.productQuantity"),
                eventList: el.find(".eventList"),
                numbers: {}
            };
            _this.cart[id].numbers.quantity = Number.parseInt(_this.cart[id].quantity.val()) || 0;




            for(let key in _this.cart[id]){
                if(_this.cart[id].hasOwnProperty(key) && key.match(/(total|unit).+/) && Number.parseInt(_this.cart[id][key].text().replace(/ /g, "")))
                    _this.cart[id].numbers[key] = Number.parseInt(_this.cart[id][key].text().replace(/ /g, ""))
            }
            for(let key in _this.cart[id]){
                if(_this.cart[id].hasOwnProperty(key) && key.match(/total.+/)){
                    let unit = key.replace(/total/, 'unit');
                    _this.cart[id].numbers[key] = _this.cart[id].numbers[unit] * _this.cart[id].numbers.quantity;
                    _this.cart[id][key].text(_this.cart[id].numbers[key].format(0, 3, ' '))
                }
            }
            _this.totalCart.numbers.totalWeightAll = _this.totalCart.numbers.totalWeightAll + _this.cart[id].numbers.unitWeight * _this.cart[id].numbers.quantity;
            _this.totalCart.totalWeightAll.text(_this.totalCart.numbers.totalWeightAll.format(0, 3, ' '));

            let price = _this.cart[id].numbers.unitPriceOld || _this.cart[id].numbers.unitPrice;
            _this.totalCart.numbers.originalTotalPaid =  _this.totalCart.numbers.originalTotalPaid + price * _this.cart[id].numbers.quantity;

            _this.totalCart.numbers.totalPaid = _this.totalCart.numbers.totalPaid + _this.cart[id].numbers.unitPrice * _this.cart[id].numbers.quantity;
            _this.totalCart.totalPaid.text(_this.totalCart.numbers.totalPaid.format(0, 3, ' '));
            el.data(_this.cart[id]);
        });

        _this.totalCart.numbers.totalShip = Math.floor(_this.totalCart.numbers.totalWeightAll/1000 * _this.totalCart.numbers.provinceFeeShip);

        _this.totalCart.totalShip.text(_this.totalCart.numbers.totalShip.format(0, 3, ' '));

        if(_this.totalCart.sale.length > 0) _this.totalCart.numbers.sale = Number.parseInt(_this.totalCart.sale.text().replace(/ /g, ""));

        _this.totalCart.numbers.totalPaidShip = _this.totalCart.numbers.totalPaid + _this.totalCart.numbers.totalShip - _this.totalCart.numbers.sale;
        _this.totalCart.numbers.totalPaidShip = _this.totalCart.numbers.totalPaidShip > 0 ? _this.totalCart.numbers.totalPaidShip : 0;
        _this.totalCart.totalPaidShip.text(_this.totalCart.numbers.totalPaidShip.format(0, 3, ' '));
    };


    CheckOut.prototype.getAddressList = function (){
        let _this = this;
        if(_this.pending){
            alert("Pending... please wait.");
            return;
        }
        _this.pending = true;
        $.get(PATH+"api/address/list",{},(result)=>{
            if(result.list && result.list.length > 0){
                let options = '<option value="">Choice address...</option>';
                $.each(result.list, (index, value) => {
                    options += "<option value="+value.id+">"+value.address+" - "+value.province.name+"</option>"
                });
                _this.childElement.addressInputSelect.html(options);
                alert("Address lis is refreshed.")
            }
        }).always(()=>{
            _this.pending = false;
        });
    };

    CheckOut.prototype.getFormData = (form) => {
        let unIndexed_array = form.serializeArray(),
            indexed_array = {};

        $.map(unIndexed_array, function(n){
            let key = n['name'], value = n['value'], matcher = key.match(/(\w+)(\.\w+)?/);
            if(!isNaN(n['value']) && n['name'] !== 'phoneNumber') value = Number.parseInt(n['value']);

            if(matcher[2]){
                let childObject  = {};
                childObject[matcher[2].substring(1)] = value;
                Object.defineProperty(indexed_array, key,{
                    value: childObject,
                    writable: false
                })
            }else indexed_array[key] = value
        });

        return indexed_array;
    };

    CheckOut.prototype.updateProduct = function (e){
        let _this = this,
            target = $(e.target),
            productContainer = target.closest("tr");
        if(productContainer.length === 0) return;
        let productId = Number.parseInt(productContainer.data('id')) || false,
            quantity = Number.parseInt(productContainer.data('quantity').val());
        if(!productId) return;
        if(!quantity){
            quantity = 1;
            alert("The quantity must be lager than 0");
            productContainer.data('quantity').val(quantity);
        }
        if(_this.pending){
            alert("Pending... please wait.");
            return;
        }
        _this.pending = true;
        $.get(PATH+"api/product", {id: productId}, (result)=>{
            if((result.list && result.list.length === 0) || result.list[0].quantity === 0 || result.list[0].state === 1) {
                alert("This product not found or out of stock.");
                _this.removeProduct(false, productId);
                return;
            }else if(result.list && result.list[0].quantity < quantity && quantity > 10){
                alert("Your quantity is larger than this product quantity. That will be set it to the current value of product.");
                productContainer.find("input.productQuantity").val(result.list[0].quantity);
                quantity = result.list[0].quantity;
            }else if(quantity > 10){
                alert("You only buy up to 10 items each.");
                quantity = 10;
                productContainer.find("input.productQuantity").val(quantity);
            }
            simpleCart.find(productId) && simpleCart.find(productId).set("quantity", quantity);
            simpleCart.update();
            for(let i = 0; i < _this._element.find(".listEvent").length; i++){
                let el = $(_this._element.find(".listEvent")[0]), event = el.data("event");
                _this.removeEvent(event);
            }
            _this.processCartRow();
            for(let i = 0; i < _this._element.find(".listEvent").length; i++){
                let el = $(_this._element.find(".listEvent")[0]), event = el.data("event");
                _this.addEvent(event);
            }
            _this.processCartRow();
        }).always(()=>{
            _this.pending = false;
        });
    };

    CheckOut.prototype.removeProduct = function(e, productId) {
        let productContainer, _this = this;
        if(e){
            let target = $(e.target);
            productContainer = target.closest("tr");
            if(productContainer.length === 0) return;
            productId = Number.parseInt(productContainer.data('id')) || false;
        }else if(productId){
            productContainer = _this.childElement.cartTable.find('.product-'+productId);
        }
        if(!isNaN(productId))
            delete _this.cart[productId];
        simpleCart.find(productId) && simpleCart.find(productId).remove();
        simpleCart.update();
        productContainer.next("tr").remove();
        productContainer.remove();
        for(let i = 0; i < _this._element.find(".listEvent").length; i++){
            let el = $(_this._element.find(".listEvent")[0]), event = el.data("event");
            _this.removeEvent(event);
        }
        _this.processCartRow();
        for(let i = 0; i < _this._element.find(".listEvent").length; i++){
            let el = $(_this._element.find(".listEvent")[0]), event = el.data("event");
            _this.addEvent(event);
        }
        _this.processCartRow();
        if(simpleCart.quantity() === 0){
            alert("Cart is empty.");
            window.location.href = "/";
        }
    };

    CheckOut.prototype.checkCode = function (e){

        let _this = this, checkCode = false,
            productIds = [], el = _this._element.find("#input-coupon"), value = el.val(), containerInput = el.closest(".input-group"),
            container = el.closest(".form-group"),
            errorList = containerInput.next(".parsley-errors-list");
        if(value.length === 0){
            alert("Code must be fill...", "warning");
            return;
        }
        _this._element.find(".listEvent").each((i, v) =>{
            let el = $(v), code = el.find('input').val();
            if(value === code){
                alert("Your entered this code!", "warning");
                checkCode = true;
                return false;
            }
        });
        if(checkCode) return;
        if(errorList.length > 0) {
            errorList.remove();
            container.removeClass("parsley-error")
        }
        _this.childElement.productRow.each((index, value) => {
            productIds.push($(value).data("id"))
        });
        if(_this.pending){
            alert("Pending... please wait.");
            return;
        }
        _this.pending = true;
        if(productIds.length > 0) $.post(PATH+"api/event/check-code",JSON.stringify({
            code: value,
            productIds : productIds
        }), result => {
            if(result.error){
                if(result.message) alert(result.message);
                else if(result.validationErrorDTO.fieldErrors && result.validationErrorDTO.fieldErrors.length > 0){
                    containerInput.addClass("parsley-error");
                    containerInput.after("<ul class='parsley-errors-list'/>");
                    $.each(result.validationErrorDTO.fieldErrors, (i,v) =>{
                        containerInput.next(".parsley-errors-list").append("<li>"+v.message+"</li>")
                    })
                }
            }else{
                if(result.list && result.list.length > 0){
                    for(let key in result.list){
                        if(result.list.hasOwnProperty(key)){
                            let event = result.list[key];
                            if(_this.addEvent(event)){
                                let index = _this._element.find(".listEvent").length;
                                let html =
                                    "<div id='event-"+event.id+"' class='form-group listEvent'>" +
                                    "<label class='form-control-label col-sm-6'><a href='"+PATH+"event/"+event.id+"'>"+event.name+"</a></label>"+
                                    "<div class='col-sm-6'>" +
                                    "<div class='input-group'>" +
                                    "<input class='form-control' readonly='readonly' type='text' name='events["+index+"].code' value='"+event.code+"' />" +
                                    "<span class='input-group-btn'>" +
                                    "<a href='#' class='btn btn-danger removeCode'><i class='fa fa-close'></i></a>" +
                                    "</span>"+
                                    "</div>" +
                                    "</div>"+
                                    "</div>";
                                container.after(html);
                                $("#event-"+event.id).data('event', event);
                                _this.processCartRow();
                                alert("The code is valid, that is applied.")
                            }
                        }
                    }
                }
            }

        }, "application/json")
            .always(()=>{
                _this.pending = false;
            });
    };


    CheckOut.prototype.removeCode = function(e) {
        e.preventDefault();
        let el = $(e.currentTarget), container = el.closest(".listEvent"), data = container.data('event'), id = data ? data.id : container.data("id");
        if(!data){
            let _this = this,
                productIds = [], el = container.find("input"), value = el.val();
            _this.childElement.productRow.each((index, value) => {
                productIds.push($(value).data("id"))
            });
            if(_this.pending){
                alert("Pending... please wait.");
                return;
            }
            _this.pending = true;
            $.post(PATH+"api/event/check-code",JSON.stringify({
                code: value,
                productIds : productIds
            }), result => {
                if(!result.error){
                    if(result.list && result.list.length > 0){
                        for(let key in result.list){
                            if(result.list.hasOwnProperty(key)){
                                data = result.list[key];
                            }
                        }
                    }
                }
                _this.childElement.cartTable.find(".event-"+id).remove();
                _this.processEvent(data, true);
                container.remove();
                let listEvent = this._element.find('.listEvent');
                for(let i = 0; i < listEvent.length; i++ ){
                    let input = $(listEvent[i]).find('input'), name = input.attr("name");
                    name = name.replace(/\d+/, i);
                    input.attr("name", name)
                }
                alert("Delete event is success!")
            }, "application/json")
                .always(()=>{
                    _this.pending = false;
                });
        }else{
            this.childElement.cartTable.find(".event-"+id).remove();
            this.processEvent(data, true);
            container.remove();
            let listEvent = this._element.find('.listEvent');
            for(let i = 0; i < listEvent.length; i++ ){
                let input = $(listEvent[i]).find('input'), name = input.attr("name");
                name = name.replace(/\d+/, i);
                input.attr("name", name)
            }
            alert("Delete event is success!")
        }

    };

    CheckOut.prototype.addEvent = function(event) {
        let _this = this;

        if(!event.products || event.products.length === 0){
            if(_this.totalCart.numbers.totalPaid > event.maxPrice || _this.totalCart.numbers.totalPaid < event.minPrice){
                alert("Order value is not in the price range of the event.", "warning");
                return false;
            }
            if(_this.totalCart.sale.length === 0)
                _this.childElement.cartTable.find(".totalPaidShip").closest("tr").before("<tr><td class='text-right' colspan='2'>Sale</td><td class='text-right sale' colspan='3'></td></tr>");
            _this.totalCart.sale =  _this.childElement.cartTable.find(".sale");
            let saleNum = _this.totalCart.numbers.sale || 0;
            if(event.value) saleNum = saleNum + event.value;
            else if(event.percentValue)
                saleNum = saleNum + Math.floor(_this.totalCart.numbers.totalPaid/100 * event.percentValue);
            _this.totalCart.numbers.sale = saleNum;
            _this.totalCart.sale.text(saleNum.format(0, 3, ' '))
        }else{
            let count = 0;
            $.each(event.products, (i,v) => {
                let id = v.id;
                if(!_this.cart[id]) return;
                let originalPrice = v.price, quantity = _this.cart[id].numbers.quantity, unitPrice = _this.cart[id].numbers.unitPrice, totalPrice = _this.cart[id].numbers.totalPrice, originalTotalPrice = originalPrice * quantity;
                _this.cart[id].eventList.append("<li class='event-"+event.id+"'><a href='"+PATH+"event/"+event.id+"'>"+event.name+"</a></li>");
                if(_this.cart[id].unitPriceOld.length === 0){
                    _this.cart[id].unitPrice.before("<p class='price-old unitPrice'>"+originalPrice.format(0, 3, " ")+"</p>");
                    _this.cart[id].totalPrice.before("<p class='price-old totalPrice'>"+(originalPrice*quantity).format(0, 3, " ")+"</p>");
                    _this.cart[id].totalPriceOld = _this.cart[id].el.find('.price-old.totalPrice');
                    _this.cart[id].unitPriceOld = _this.cart[id].el.find('.price-old.unitPrice');
                }
                if(event.value){
                    _this.cart[id].unitPrice.html((unitPrice - event.value).format(0, 3, " "));
                    _this.cart[id].totalPrice.html((totalPrice - event.value * quantity).format(0, 3, " "));
                }else if(event.percentValue){
                    _this.cart[id].unitPrice.html((unitPrice - Math.floor(originalPrice/100 * event.percentValue)).format(0, 3, " "));
                    _this.cart[id].totalPrice.html((totalPrice  - Math.floor(originalPrice/100 * event.percentValue) * quantity).format(0, 3, " "));
                }
                count++;
            });
            if(count ===0){
                alert("There are no products that match '"+event.code+"'.", "warning");
                _this._element.find("#event-"+event.id).remove();
                return false;
            }
        }
        return true;
    };

    CheckOut.prototype.removeEvent = function(event) {
        let _this = this;
        if(!event.products || event.products.length === 0){
            if(_this.totalCart.sale.length === 0) return;
            if(event.value) _this.totalCart.numbers.sale = _this.totalCart.numbers.sale - event.value;
            else if(event.percentValue) {
                _this.totalCart.numbers.sale = _this.totalCart.numbers.sale - Math.floor(_this.totalCart.numbers.totalPaid/100 * event.percentValue);
            }
            if(_this.totalCart.numbers.sale > 0)
                _this.totalCart.sale.text(_this.totalCart.numbers.sale.format(0, 3, ' '));
            else _this.totalCart.sale.closest("tr").remove();
        }else{
            $.each(event.products, (i,v) => {
                let id = v.id;
                if(!_this.cart[id]) return;
                let unitPrice = _this.cart[id].numbers.unitPrice, totalPrice = _this.cart[id].numbers.totalPrice,
                    quantity =_this.cart[id].numbers.quantity,
                    originalPrice = v.price, originalTotalPrice = originalPrice * quantity;
                _this.cart[id].eventList.find(".event-"+event.id).remove();
                if(event.value){
                    _this.cart[id].numbers.unitPrice = unitPrice = unitPrice + event.value;
                    _this.cart[id].numbers.totalPrice = totalPrice = totalPrice + event.value * quantity;
                }
                else if(event.percentValue){
                    _this.cart[id].numbers.unitPrice  = unitPrice = unitPrice + Math.floor(originalPrice/100 * event.percentValue);
                    _this.cart[id].numbers.totalPrice = totalPrice = totalPrice + Math.floor(originalTotalPrice/100 * event.percentValue);
                }
                if(originalPrice === unitPrice){
                    _this.cart[id].unitPriceOld.remove();
                    _this.cart[id].totalPriceOld.remove();
                }

                _this.cart[id].unitPrice.html((unitPrice).format(0, 3, " "));
                _this.cart[id].totalPrice.html((totalPrice).format(0, 3, " "));

            })
        }
    };

    CheckOut.prototype.processEvent = function(event, remove) {
        if(!remove) this.addEvent(event);
        else this.removeEvent(event);
    };

    $.fn[pluginName] = function (options) {
        this.each(function () {
            if (!$.data(this, pluginName)) {
                $.data(this, pluginName, new CheckOut(this, options));
            }
        });

        return this;
    };

    return CheckOut;
}));
(function($){
    $("#content").checkOut();
}(jQuery));