$(function () {
    var shopId = getQueryString("shopId");
    var getPageInfo = "/o2o/frontend/shopdetailpageinfo?shopId=";
    var getProduct = "/o2o/frontend/getproductlist";
    var productDetail = "/o2o/frontend/productdetail?productId=";
    var pageNum = 1;
    var pageSize = 4;
    var productCategoryId = "";
    var productName = "";
    var remove = false;

    $.ajax({
        url: getPageInfo + shopId,
        type: 'get',
        success: function (data) {
            if (data.success) {
                var buttonHtml = "<a href='#' class='button highlight' data-productCategoryId=''>全部类别</a>";
                data.productCategoryList.map(function (productCategory, index) {
                    buttonHtml += "<a href='#' class='button' data-productCategoryId='" + productCategory.productCategoryId
                        + "'>" + productCategory.productCategoryName + "</a>"
                });
                $(".product-category-button").html(buttonHtml);
                var shop = data.shop;
                $(".demo-card-header-pic div img").attr("src", ".." + shop.shopImg);
                $(".bar-nav .title").text(shop.shopName);
                $(".lastEditTime").text(transfer(shop.lastEditTime));
                $(".shop-desc").text(shop.shopDesc);
                $(".shop-addr").text(shop.shopAddr);
                $(".phone").text(shop.phone);
            }
        }
    });

    $("#me").click(function () {
        $.openPanel("#panel-js-demo");
    });

    function addListProduct(pageIndex, pageSize) {
        var conditionStr = "?shopId=" + shopId + "&productName=" + productName + "&productCategoryId=" + productCategoryId
            + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
        loading = false;
        $.ajax({
            url: getProduct + conditionStr,
            type: 'get',
            success: function (data) {
                if (data.success) {
                    var productList = "";
                    data.productList.map(function (product, index) {
                        productList += "              <div class=\"card product\" data-productId='" + product.productId + "'>\n" +
                            "                            <div class=\"card-header\">" + product.productName + "</div>\n" +
                            "                            <div class=\"card-content\">\n" +
                            "                                <div class=\"list-block media-list\">\n" +
                            "                                    <ul>\n" +
                            "                                        <li class=\"item-content\">\n" +
                            "                                            <div class=\"item-media\">\n" +
                            "                                                <img src='.." + product.imgAddr + "' width=\"44\">\n" +
                            "                                            </div>\n" +
                            "                                            <div class=\"item-inner\">\n" +
                            "                                                <div class=\"item-title-row\">\n" +
                            "                                                    <div class=\"item-title\">" + product.productDesc + "</div>\n" +
                            "                                                </div>\n" +
                            "                                            </div>\n" +
                            "                                        </li>\n" +
                            "                                    </ul>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                            <div class=\"card-footer\">\n" +
                            "                                <span id=\"last-edit-time\">" + transfer(product.lastEditTime)+ "</span>\n" +
                            "                                <span><a href=\"#\">点击查看</a></span>\n" +
                            "                            </div>\n" +
                            "                        </div>";
                    });
                    var total = $('.product-list .card').length;
                    // append前，首先查看.card是否达到data.count，如果达到则不再append
                    // 此目的在于防止切换button时，滚动条正好在下面，这样会造成两者同时都是pageNum = 1来请求数据两次，造成两次append重复数据
                    if (total < data.count) {
                        $(".product-list").append(productList);
                    }
                    // append后的元素数量
                    total = $('.product-list .card').length;
                    if (total >= data.count) {
                        // 加载完毕，则注销无限加载事件，以防不必要的加载
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        // 删除加载提示符
                        $('.infinite-scroll-preloader').remove();
                        remove = true;
                    }
                    loading = false;
                    pageNum += 1; //pageNum + 1，便于下一次无限滚动知道从什么位置开始
                    $.refreshScroller();
                } else {
                    $.toast(data.errMsg);
                }
            }
        })
    }

    addListProduct(pageNum, pageSize);

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        console.log("由scroll 加载");
        addListProduct(pageNum, pageSize);
    });

    $("#search").on('change', function () {
        productName = $(this).val(); // 将全局变量shopName设置为输入值，就可以配合其他条件共同搜索
        $(".product-list").empty(); // 将list-div下面的所有card清空，因为addListShops中是append元素，而不是html()元素
        pageNum = 1; // 将pageNum设置为1
        addListProduct(pageNum, pageSize); //请求数据
        $.init();
    });

    $(".product-category-button").on("click", "a", function () {
        // 旧的button背景颜色移除
        $($(".product-category-button").find("a").not(function () {
            return !$(this).hasClass("highlight");
        })).removeClass("highlight");
        // 新的button添加高亮背景颜色
        $(this).addClass("highlight");
        productCategoryId = $(this).data("productcategoryid");
        $(".product-list").empty();
        pageNum = 1;
        console.log("由button加载");
        addListProduct(pageNum, pageSize);
        $.init(); //事件注销了，需要重新将事件初始化回来
    });

    $(".product").live("click", function () {
        var productId = $(this).data("productid");
        window.location.href = productDetail + productId;
    });

    $(".exchange-award").on("click", function(){
        window.location.href = "/o2o/frontend/shopawardexchange?shopId=" + shopId;
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});