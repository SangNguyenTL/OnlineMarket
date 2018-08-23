(function ($) {

    let $formAddress = $('#addressForm'), $modalAddress = $("#addAddress"), cartTable = $("#cartTable"), selectAddress = $("#address");

    /**
     * Number.prototype.format(n, x, s, c)
     *
     * @param integer n: length of decimal
     * @param integer x: length of whole part
     * @param mixed   s: sections delimiter
     * @param mixed   c: decimal delimiter
     */
    Number.prototype.format = function(n, x, s, c) {
        let re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
            num = this.toFixed(Math.max(0, ~~n));

        return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
    };


    function calculateQuantity(container){
        if(container.length === 0) return;
        let input = container.find("input.productQuantity"), quantity = Number.parseInt(input.val()) || 0;
        if(input.length === 0) return;
        let data = {
            unitPrice : container.find(".unitPrice.price-new"),
            unitPriceOld : container.find(".unitPrice.price-old"),
            totalPrice : container.find(".totalPriceRow.price-new"),
            totalPriceOld : container.find(".totalPriceRow.price-old"),
            unitWeight : container.next("tr").find(".unitWeight"),
            totalWeight: container.next("tr").find(".totalWeightRow")
        }, dataNumber = {};
        for(let key in data){
            if(Number.parseInt(data[key].text().replace(/ /g, "")))
                dataNumber[key] = Number.parseInt(data[key].text().replace(/ /g, ""))
        }
        for(let key in data){
            if(key.match(/total.+/)){
                let unit = key.replace(/total/, 'unit');
                dataNumber[key] = dataNumber[unit] * quantity;
                data[key].text(dataNumber[key].format(0, 3, ' '))
            }
        }
        calculateAll();
    }

    function calculateAll(){
        let dataAll = {
            totalWeightAll : cartTable.find(".totalWeight"),
            totalPaidReal : cartTable.find(".totalPaidReal"),
            totalPriceAll : cartTable.find(".totalPaidAndShip"),
            feeShip : cartTable.find(".feeShip"),
            totalShip: cartTable.find(".totalShip")
        }, number = 0;
        cartTable.find(".totalWeightRow").each(function(){
            let element = $(this), localNumber = Number.parseInt(element.text().replace(/ /g, "")) || 0;
            number = number + localNumber;
            dataAll.totalWeightAll.text(number.format(0, 3, ' '))
        });
        let feeShipNumber = Number.parseInt(dataAll.feeShip.text().replace(/ /g, '')) || 0, totalShip = Math.floor(number/1000 * feeShipNumber);
        dataAll.totalShip.text(totalShip.format(0, 3, ' '));
        number = 0;
        cartTable.find(".totalPriceRow").each(function(){
            let element = $(this), localNumber = Number.parseInt(element.text().replace(/ /g, "")) || 0;
            number = number + localNumber;
            dataAll.totalPaidReal.text(number.format(0, 3, ' '));
            dataAll.totalPriceAll.text((number+totalShip).format(0, 3, ' '))
        });
    }

    function updateQuantity(e){
        let target = $(e.target),
            productContainer = target.closest("tr");
        if(productContainer.length === 0) return;
        let productId = Number.parseInt(productContainer.attr("class").match(/product-(\d+)/)[1]) || false,
            quantity = Number.parseInt(productContainer.find("input").val());
        if(!productId || !quantity) return;
        $.get(PATH+"api/product", {id: productId}, (result)=>{
            if((result.list && result.list.length === 0) || result.list[0].quantity === 0 || result.list[0].state === 1) {
                alert("This product not found or out of stock.");
                removeProduct(productContainer, productId);
                return;
            }else if(result.list && result.list[0].quantity < quantity){
                alert("Your quantity is larger than this product quantity. That will be set it to the current value of product.");
                productContainer.find("input.productQuantity").val(result.list[0].quantity)
                quantity = result.list[0].quantity;
            }
            simpleCart.find(productId) && simpleCart.find(productId).set("quantity", quantity);
            simpleCart.update();
            calculateQuantity(productContainer);
        });
    }

    function removeProduct(container, productId){
        simpleCart.find(productId) && simpleCart.find(productId).remove();
        simpleCart.update();
        container.next("tr").remove();
        container.remove();
        calculateAll();
        if(simpleCart.quantity() === 0) window.location.href = "/";

    }

    cartTable.on("click", ".updateProduct", (e) => updateQuantity(e));

    cartTable.on("click", ".removeProduct", (e) => {
        let target = $(e.target),
            productContainer = target.closest("tr");
        if(productContainer.length === 0) return;
        let productId = Number.parseInt(productContainer.attr("class").match(/product-(\d+)/)[1]) || false;
        if(!productId) return;
        removeProduct(productContainer, productId);
    });

    selectAddress.on("change", e => {
        let target = $(e.target), id = target.val() || 0, feeShipContainer = cartTable.find('.feeShip');
        if(!id) return;
        $.get(PATH+"api/address",{addressId: id}, result => {
            if(!result.error && feeShipContainer.length > 0 && result.list.length > 0){
                let feeShip = result.list[0].province.shippingFee || 0;
                feeShipContainer.text(feeShip);
                calculateAll();
            }else{
                alert("Address not found.");
                $.get(PATH+"api/address/list",{},(result)=>{
                    if(result.list && result.list.length > 0){
                        let options = '<option value="">Choice address...</option>';
                        $.each(result.list, (index, value) => {
                            options += "<option value="+value.id+">"+value.address+" - "+value.province.name+"</option>"
                        });
                        selectAddress.html(options);
                    }
                })
            }
        })
    });


    function getFormData($form){
        let unIndexed_array = $form.serializeArray(),
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
    }

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

    $formAddress.parsley();
    $formAddress.on('submit', function(e) {
        e.preventDefault();
        $formAddress.find(".parsley-errors-list").remove();
        $formAddress.find(".parsley-error").removeClass("parsley-error");
        if($formAddress.parsley().validate()){
            $.put(PATH + 'api/address', JSON.stringify(getFormData($formAddress)),(result) => {
                if(result.error){
                    if(result.message) alert(message);
                    else if(result.validationErrorDTO.fieldErrors && result.validationErrorDTO.fieldErrors.length > 0){

                        $.each(result.validationErrorDTO.fieldErrors,(index, value)=>{
                            let $intput = $formAddress.find("[name='"+value.field+"']");
                            if($intput.length > 0){
                                $intput.after("<ul class='parsley-errors-list'/>");
                                let $ulInput = $intput.closest(".form-group").find("ul");
                                $formAddress.find("[name='"+value.field+"']").addClass("parsley-error");
                                $ulInput.append("<li>"+value.message+"</li>");

                            }
                        })
                    }
                }else {
                    $formAddress[0].reset();
                    $modalAddress.modal("toggle");
                    alert("Add success!");

                    if(selectAddress.length === 0) return;
                    $.get(PATH+"api/address/list",{},(result)=>{
                        if(result.list && result.list.length > 0){
                            let options = '<option value="">Choice address...</option>';
                            $.each(result.list, (index, value) => {
                                options += "<option value="+value.id+">"+value.address+" - "+value.province.name+"</option>"
                            });
                            selectAddress.html(options);
                        }
                    })
                }
            } ,"application/json");

            $modalAddress.modal('close');
        }
        return false
    });


})(jQuery);