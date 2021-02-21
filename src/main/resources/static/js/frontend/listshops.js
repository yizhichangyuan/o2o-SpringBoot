$(function () {
    var listshops = "/o2o/frontend/listshops";
    var getPageInfo = "/o2o/frontend/listshopspageinfo";
    var parentId = getQueryString("parentId");
    var isFromParent = false; // 此标志位控制后面button点击是发送parentId还是shopCategoryId
    if (parentId) {
        isFromParent = true;
    }
    var areaId = "";
    var shopName = "";
    var shopCategoryId = "";
    var pageNum = 1;
    var pageSize = 3;
    var loading = false;
    addListShops(pageNum, pageSize); //预先加载3条
    getSelectButton();


    function addListShops(pageIndex, pageSize) {
        conditionStr = "?" + "areaId=" + areaId + "&shopName=" + shopName + "&parentId=" + parentId + "&shopCategoryId="
            + shopCategoryId + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
        loading = true;
        $.ajax({
            url: listshops + conditionStr,
            type: "GET",
            success: function (data) {
                if (data.success) {
                    var shopCard = "";
                    data.shopList.map(function (shop, index) {
                        shopCard += "<div class=\"card\" data-shopId='" + shop.shopId + "'>\n" +
                            "            <div class=\"card-header\">" + shop.shopName + "</div>\n" +
                            "            <div class=\"card-content\">\n" +
                            "                <div class=\"list-block media-list\">\n" +
                            "                    <ul>\n" +
                            "                        <li class=\"item-content\">\n" +
                            "                            <div class=\"item-media\">\n" +
                            "                                <img src='.." + shop.shopImg + "' width=\"44\">\n" +
                            "                            </div>\n" +
                            "                            <div class=\"item-inner\">\n" +
                            "                                <div class=\"item-title-row\">\n" +
                            "                                    <div class=\"item-title\">" + shop.shopDesc + "</div>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </li>\n" +
                            "                    </ul>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "            <div class=\"card-footer\">\n" +
                            "                <span class=\"last-edit-time\">" + transfer(shop.lastEditTime) + "</span>\n" +
                            "                <span><a href=\"#\" data-shopId='" + shop.shopId + "'>点击查看</a></span>\n" +
                            "            </div>\n" +
                            "        </div>";
                    });
                    $(".shop-list").append(shopCard);
                    if (isFromParent) {
                        $(".shoplist-button-div").find("a").addClass("isFromParent");
                    }
                    var total = $('.list-div .card').length;
                    if (total >= data.count) {
                        // 加载完毕，则注销无限加载事件，以防不必要的加载
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        // 删除加载提示符
                        $('.infinite-scroll-preloader').hide();
                    }
                    loading = false;
                    pageNum += 1; //pageNum + 1，便于下一次无限滚动知道从什么位置开始
                    $.refreshScroller();
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    }

    $(".card").live("click", function () {
        var shopId = $(this).data("shopid");
        window.location.href = "/o2o/frontend/shopdetailpage?shopId=" + shopId;
    });

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addListShops(pageNum, pageSize);
    });

    // 打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-js-demo");
    });

    $(".close-panel").click(function () {
        $.closePanel("#panel-js-demo");
    });

    function getSelectButton() {
        $.ajax({
            url: getPageInfo + "?parentId=" + parentId,
            type: 'GET',
            success: function (data) {
                var selectButton = "<a href='#' class='button highlight' data-shopCategoryId=''>全部类别</a>";
                var areaHtml = "<option data-areaId=''>全部街道</option>";
                if (data.success) {
                    data.shopCategoryList.map(function (shopCategory, index) {
                        selectButton += "<a href=\"#\" class=\"button\" data-shopCategoryId='" + shopCategory.shopCategoryId
                            + "'>" + shopCategory.shopCategoryName + "</a>";
                    });
                    $(".shoplist-button-div").html(selectButton);

                    data.areaList.map(function (term, index) {
                        areaHtml += "<option data-AreaId='" + term.areaId + "'>" + term.areaName + "</option>";
                    });
                    $("#area-search").html(areaHtml);
                } else {
                    $.toast(data.errMsg);
                }
            }
        })
    }

    $("#search").on('input', function () {
        shopName = $(this).val(); // 将全局变量shopName设置为输入值，就可以配合其他条件共同搜索
        $(".list-div").empty(); // 将list-div下面的所有card清空，因为addListShops中是append元素，而不是html()元素
        pageNum = 1; // 将pageNum设置为1
        addListShops(pageNum, pageSize); //请求数据
        $.init(); //再次重新初始化页面，否则因为无限加载事件可能已经注销，所以需要重新初始化
    });

    $(".shoplist-button-div").on("click", "a", function () {
        var temp = $(this).data("shopcategoryid");
        // 需要判断当前应该填充到parentId还是shopCategoryId
        // 可以根据parentId是否为空，如果parentId不为空，说明是从一级大类进入的该页面，那么按钮二级分类，应该填充shopCategoryId
        // 如果parentId为空，那么说明当前是从全部商店进入，那么当前按钮应该都是一级大类按钮，那么应该填充parentId
        if (isFromParent) {
            shopCategoryId = temp;
        } else {
            parentId = temp;
        }
        // 旧的button背景颜色移除
        $($(".shoplist-button-div").find("a").not(function () {
            return !$(this).hasClass("highlight");
        })).removeClass("highlight");
        // 新的button添加高亮背景颜色
        $(this).addClass("highlight");
        $(".list-div").empty();
        pageNum = 1;
        addListShops(pageNum, pageSize);
        $.init();
    });

    $("#area-search").on("change", function () {
        areaId = $("#area-search").find("option").not(function () {
            return !this.selected;
        }).data("areaid");
        $(".list-div").empty();
        pageNum = 1;
        addListShops(pageNum, pageSize);
        $.init();
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});