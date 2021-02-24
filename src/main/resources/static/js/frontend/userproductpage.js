$(function () {
    var getPageInfo = "/o2o/frontend/listuserproductbyuser";
    var productName = "";
    var pageIndex = 1;
    var pageSize = 3;
    var loading = false;
    addListProducts(); //预先加载3条

    function addListProducts() {
        loading = true;
        $.ajax({
            url: getPageInfo + "?pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&productName=" + productName,
            type: "GET",
            success: function (data) {
                if (data.success) {
                    var userProductHtml = "";
                    data.userProductMapList.map(function (userProduct, index) {
                        userProductHtml += "<div class=\"card\" data-productId='" + userProduct.product.productId + "'>\n" +
                            "            <div class=\"card-header\">" + userProduct.product.productName + "<p class='point'>获得积分：" + userProduct.point + "</p></div>\n" +
                            "            <div class=\"card-content\">\n" +
                            "                <div class=\"list-block media-list\">\n" +
                            "                    <ul>\n" +
                            "                        <li class=\"item-content\">\n" +
                            "                            <div class=\"item-media\">\n" +
                            "                                <img src='.." + userProduct.product.imgAddr + "' width=\"44\">\n" +
                            "                            </div>\n" +
                            "                            <div class=\"item-inner\">\n" +
                            "                                <div class=\"item-title-row\">\n" +
                            "                                    <div class=\"item-title\">" + userProduct.product.productDesc + "</div>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </li>\n" +
                            "                    </ul>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "            <div class=\"card-footer\">\n" +
                            "                <span class=\"last-edit-time\">" + transfer(userProduct.createTime) + "</span>\n" +
                            "                <span><a href=\"#\" class='exchange' data-userProductId='" + userProduct.userProductId + "'>已消费</a></span>\n" +
                            "            </div>\n" +
                            "        </div>";
                    });
                    $(".user-product-list").append(userProductHtml);
                    var total = $('.list-div .card').length;
                    if (total >= data.count) {
                        // 加载完毕，则注销无限加载事件，以防不必要的加载
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        // 删除加载提示符
                        $('.infinite-scroll-preloader').hide();
                    }
                    loading = false;
                    pageIndex += 1; //pageIndex + 1，便于下一次无限滚动知道从什么位置开始
                    $.refreshScroller();
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    }

    $(".card").live("click", function () {
        var productId = $(this).data("productid");
        window.location.href = "/o2o/frontend/productdetail?productId=" + productId + "&fromUserProduct=true";
    });

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addListProducts();
    });

    // 打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-js-demo");
    });

    $(".close-panel").click(function () {
        $.closePanel("#panel-js-demo");
    });


    $("#search").on('input', function () {
        awardName = $(this).val(); // 将全局变量shopName设置为输入值，就可以配合其他条件共同搜索
        $(".list-div").empty(); // 将list-div下面的所有card清空，因为addListShops中是append元素，而不是html()元素
        pageIndex = 1; // 将pageIndex设置为1
        addListAwards(); //请求数据
        $.init(); //再次重新初始化页面，否则因为无限加载事件可能已经注销，所以需要重新初始化
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});