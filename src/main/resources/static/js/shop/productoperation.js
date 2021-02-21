$(function () {
    var getProductCategoryURL = "/o2o/shopadmin/getproductcategorylist";
    var getProductByIdURL = "/o2o/shopadmin/getproductbyid?productId="

    var productId = getQueryString("productId");
    var isAdd = false;
    if (!productId) {
        isAdd = true;
        getProductCategory();
    } else {
        getProductCategory();
        getProductById();
    }

    function getProductById() {
        $.ajax({
            url: getProductByIdURL + productId,
            type: 'GET',
            success: function (data) {
                if (data.success) {
                    getProductCategory();
                    var product = data.product;
                    $("#product-name").val(product.productName);
                    $("#product-desc").val(product.productDesc);
                    $("#normal-price").val(product.normalPrice);
                    $("#promotion-price").val(product.promotionPrice);
                    $("#priority").val(product.priority);
                    $("#point").val(product.point);
                    $("#product-category option[id='" + product.productId + "']").attr('selected', "selected");
                } else {
                    console.log("getproductbyid fail");
                }
            }
        });
    }

    $(".detail-input").on("change", "input:last-child", function () {
        if ($(".detail-img").length < 6) {
            $(".detail-input").append("<input type=\"file\" class=\"detail-img\" placeholder=\"商品详情图\"/>");
        }
    });

    $("#submit").on("click", function () {
        var product = {};
        var formData = new FormData();
        var chooseUrl = isAdd === true ? "/o2o/shopadmin/addproduct" : "/o2o/shopadmin/modifyproduct";
        var checkCode = $("#j_kaptcha").val();
        formData.append("verifyCodeActual", checkCode);

        product.productName = $("#product-name").val();
        product.productDesc = $("#product-desc").val();
        product.normalPrice = $("#normal-price").val();
        product.promotionPrice = $("#promotion-price").val();
        product.priority = $("#priority").val();
        product.productCategory = {
            productCategoryId: $("#product-category").find("option").not(function () {
                return !this.selected;
            }).attr('id')
        };
        product.point = $("#point").val();
        if (!isAdd) {
            product.productId = productId;
        }
        formData.append("productStr", JSON.stringify(product));

        var thumbnail = $("#thumbnail")[0].files[0];
        formData.append("thumbnail", thumbnail);

        $(".detail-img").map(function (index, term) {
            if ($('.detail-img')[index].files.length > 0) {
                formData.append("productDetailImg-" + index, $(".detail-img")[index].files[0]);
            }
        });

        $.ajax({
            url: chooseUrl,
            type: 'post',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功");
                } else {
                    $.toast("提交失败：" + data.errMsg);
                }
            }
        });
    });


    function getProductCategory() {
        $.ajax({
            url: getProductCategoryURL,
            type: 'get',
            success: function (data) {
                if (data.success) {
                    var tempHtml = "";
                    data.productCategoryList.map(function (term, index) {
                        tempHtml += "<option id='" + term.productCategoryId + "'>" + term.productCategoryName + "</option>"
                    });
                    $("#product-category").html(tempHtml);
                }
            }
        });
    }
});