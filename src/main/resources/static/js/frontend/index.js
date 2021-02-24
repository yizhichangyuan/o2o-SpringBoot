$(function () {
    var mainPageInfoUrl = "/o2o/frontend/listmainpageinfo";
    var listShopsPageUrl = "/o2o/frontend/listshopspage?parentId=";
    getMainPageInfo();

    function getMainPageInfo() {
        $.ajax({
            url: mainPageInfoUrl,
            type: 'get',
            success: function (data) {
                if (data.success) {
                    var headLineHtml = "";
                    data.headLineList.map(function (headLine, index) {
                        headLineHtml += "<div class='swiper-slide img-wrap'><img class='banner-img' src='.." +
                            headLine.lineImg + "' alt='" + headLine.lineName + "'></div>"
                    });
                    $(".swiper-wrapper").html(headLineHtml);
                    $(".swiper-container").swiper({
                        autoplay: 1000,
                        autoplayDisableOnInteraction: false
                    });
                    var shopParentCategoryHtml = "";
                    // console.log(data.shopCategoryList);
                    data.shopCategoryList.map(function (shopCategory, index) {
                        shopParentCategoryHtml += "<div class='shop-classify col-50' data-shopCategryId='" + shopCategory.shopCategoryId + "'>"
                            + "<div class='word'>" +
                            "<div class='shop-info'>" + "<div class='shop-name'>" + shopCategory.shopCategoryName + "</div>" +
                            "</div>" + "<div class='shop-desc'>" + shopCategory.shopCategoryDesc + "</div>" +
                            "</div>" + "<div class='shop-classify-img-warp'><img src='.." + shopCategory.shopCategoryImg +
                            "' style='width: 2.2rem;'></div>" + "</div>"
                    });
                    $(".list-block").html(shopParentCategoryHtml);
                    loginCheck(data.loginStatus);
                }
            }
        })
    }

    $("#me").click(function () {
        $.openPanel("#panel-js-demo");
    });

    $(".list-block").on("click", ".shop-classify", function () {
        var parentId = $(this).data("shopcategryid");
        window.location.href = listShopsPageUrl + parentId;
    });
});