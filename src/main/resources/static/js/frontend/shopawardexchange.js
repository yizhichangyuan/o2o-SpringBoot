$(function () {
    var shopId = getQueryString("shopId");
    var getPageInfo = "/o2o/frontend/getawardlistbyshop?shopId=" + shopId;
    var awardName = "";
    var pageIndex = 1;
    var pageSize = 3;
    var loading = false;
    var totalPoint = 0;
    var canProcessed = false;
    addListAwards(); //预先加载3条

    function addListAwards() {
        loading = true;
        $.ajax({
            url: getPageInfo + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&awardName=" + awardName,
            type: "GET",
            success: function (data) {
                if (data.success) {
                    var awardHtml = "";
                    data.awardList.map(function (award, index) {
                        awardHtml += "<div class=\"card\" data-awardId='" + award.awardId + "'>\n" +
                            "            <div class=\"card-header\">" + award.awardName + "<p class='point'>所需积分：" + award.point + "</p></div>\n" +
                            "            <div class=\"card-content\">\n" +
                            "                <div class=\"list-block media-list\">\n" +
                            "                    <ul>\n" +
                            "                        <li class=\"item-content\">\n" +
                            "                            <div class=\"item-media\">\n" +
                            "                                <img src='.." + award.awardImg + "' width=\"44\">\n" +
                            "                            </div>\n" +
                            "                            <div class=\"item-inner\">\n" +
                            "                                <div class=\"item-title-row\">\n" +
                            "                                    <div class=\"item-title\">" + award.awardDesc + "</div>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </li>\n" +
                            "                    </ul>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "            <div class=\"card-footer\">\n" +
                            "                <span class=\"last-edit-time\">" + transfer(award.lastEditTime) + "</span>\n" +
                            "                <span><a href=\"#\" class='exchange' data-awardId='" + award.awardId + "' data-point='" + award.point  + "'>点击领取</a></span>\n" +
                            "            </div>\n" +
                            "        </div>";
                    });
                    $(".shop-award-list").append(awardHtml);
                    totalPoint = data.totalPoint;
                    if(totalPoint != 0){
                        $(".my-point").text("总积分为：" + totalPoint);
                        canProcessed = true;
                    }
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

    $(".exchange").live("click", function () {
        var awardId = $(this).data("awardid");
        var point = $(this).data('point');
        $.confirm("确定兑换吗？", function(){
            if(point > totalPoint || !canProcessed){
                $.toast("积分不足或无权操作");
                return;
            }
            $.ajax({
                url: "/o2o/frontend/awardexchange?awardId=" + awardId + "&shopId=" + shopId,
                type: "get",
                success: function(data){
                    if(data.success) {
                        $.toast("兑换成功");
                        updatePoint();
                    }else{
                        $.toast(data.errMsg);
                    }
                }
            });
        });

    });

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addListAwards();
    });

    function updatePoint() {
        $.ajax({
            url: getPageInfo + "&pageIndex=" + 1 + "&pageSize=" + 1 + "&awardName=",
            type: "get",
            success: function(data){
                    if(data.success){
                        totalPoint = data.totalPoint;
                        $(".my-point").text("总积分为：" + totalPoint);
                    } else{
                        $.toast(data.errMsg);
                    }
            }
        });
    }

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