$(function () {
    getlist();

    function getlist(e) {
        $.ajax({
            url: "/o2o/shopadmin/getshoplist",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleShopList(data.shopList);
                    handleUser(data.user.name);
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    }

    function handleShopList(shopList) {
        var shopListHtml = "";
        shopList.map(function (shop, index) {
            shopListHtml += "<div class='row row-shop'>" + "<div class='col-40 shop-name'>" + shop.shopName + "</div>"
                + "<div class='col-40'>" + shopStatus(shop.enableStatus) + "</div>"
                + "<div class='col-20'>" + goShop(shop.enableStatus, shop.shopId) + "</div></div>"

        });
        $(".shop-wrap").html(shopListHtml);
    }

    function shopStatus(enableStatus) {
        if (enableStatus == 1) {
            return "审核通过";
        } else if (enableStatus == 0) {
            return "审核中"
        } else if (enableStatus == -1) {
            return "店铺非法";
        }
    }

    function goShop(enableStatus, shopId) {
        if (enableStatus == 1) {
            var goShopHtml = "<a href='" + "/o2o/shopadmin/shopmanagement?shopId=" + shopId + "'>" + "进入" + "</a>";
            return goShopHtml;
        }
        return "";
    }

    function handleUser(name) {
        $("#user-name").text(name);
    }
});