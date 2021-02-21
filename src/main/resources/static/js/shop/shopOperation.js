$(function () {
    var initURL = "/o2o/shopadmin/getshopinitinfo";
    var registerShopURL = "/o2o/shopadmin/registershop";
    var getShopByIdURL = "/o2o/shopadmin/getshopbyid";
    var shopId = getQueryString("shopId");
    var modifyShopURL = "/o2o/shopadmin/modifyshop";
    var urlHaveShopId = shopId ? true : false;

    if (urlHaveShopId) {
        getShopById(shopId);
    } else {
        getShopInitInfo();
    }

    function getShopById(shopId) {
        getShopByIdURL += "?shopId=" + shopId;
        $.getJSON(getShopByIdURL, function (data) {
            console.log(data);
            if (data.success) {
                var shop = data.shop;
                $("#shop-name").val(shop.shopName);
                $("#shop-addr").val(shop.shopAddr);
                $("#shop-desc").val(shop.shopDesc);
                $("#shop-phone").val(shop.phone);
                var shopCategoryHtml = "<option id='" + shop.shopCategory.shopCategoryId + "'>" +
                    shop.shopCategory.shopCategoryName + "</option>";
                var areaHtml = ""
                data.areaList.map(function (item, index) {
                    areaHtml += "<option id='" + item.areaId + "'>" + item.areaName + "</option>"
                })
                $("#shop-category").html(shopCategoryHtml);
                $("#shop-category").attr("disabled", "disabled");
                $("#area").html(areaHtml);
                $("#area option[id='" + shop.area.areaId + "']").attr("selected", "selected");
            }
        })

    }

    function getShopInitInfo() {
        $.getJSON(initURL, function (data) {
            var tempShopCategoryHTML = "";
            var tempAreaHTML = "";
            if (data.success) {
                data.shopCategoryList.map(function (item, index) {
                    tempShopCategoryHTML += "<option id='" + item.shopCategoryId + "'>" + item.shopCategoryName + "</option>";
                });
                data.areaList.map(function (item, index) {
                    tempAreaHTML += "<option id='" + item.areaId + "'>" + item.areaName + "</option>";
                });
                $("#shop-category").html(tempShopCategoryHTML);
                $("#area").html(tempAreaHTML);
            }
        });
    }

    $('#submit').click(function () {
        var shop = {};
        if (urlHaveShopId) {
            shop.shopId = shopId;
        }
        shop.shopName = $("#shop-name").val();
        shop.shopAddr = $("#shop-addr").val();
        shop.shopDesc = $("#shop-desc").val();
        shop.phone = $("#shop-phone").val();
        shop.shopCategory = {
            shopCategoryId: $("#shop-category").find("option").not(function () {
                return !this.selected;
            }).attr("id")
        };
        shop.area = {
            areaId: $("#area").find("option").not(function () {
                return !this.selected;
            }).attr('id')
        };
        if (!shop.shopName.trim() || !shop.shopAddr.trim()) {
            $.toast("店铺名或店铺地址不能为空");
            return;
        }
        var re = new RegExp(/^[0-9]{7}$/g, "");
        if (!re.test(shop.phone)) {
            $.toast("请输入正确的电话");
            return;
        }
        var shopImg = $("#shop-img")[0].files[0];
        // 使用formData来构造表达数据
        var formData = new FormData();
        formData.append("shopStr", JSON.stringify(shop));
        formData.append("shopImg", shopImg);
        var verifyCode = $("#j_kaptcha").val();
        if (!verifyCode) {
            $.toast("验证码为空");
            return;
        }
        formData.append("verifyCodeActual", verifyCode);
        $.ajax({
            url: (urlHaveShopId ? modifyShopURL : registerShopURL),
            type: 'POST',
            data: formData,
            contentType: false, // 因为有文字和图片，所以为false
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功！");
                } else {
                    $.toast("提交失败！" + data.errMsg);
                }
                // 不管提交是否成功，都要改变一下验证码，因为点击事件连接着一个切换图片的事件
                $("#kaptcha-img").click();
            }
        });
    });
});