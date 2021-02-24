$(function(){
    var userName = "";
    var url = "/o2o/shopadmin/getusershoplistbyshop?";
    var pageIndex = 1;
    var pageSize = 10;
    var loading = false;

    getUserShopMapList();

    function getUserShopMapList(){
        var listusershopbyshop = url + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&userName=" + userName;
        loading = true;
        $.ajax({
            url: listusershopbyshop,
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data.success){
                    var tempHtml = "";
                    data.userShopMapList.map(function(userShop, index){
                        tempHtml += "<div class='row row-shopmember'>" + "<div class='col-40'>" + transfer(userShop.createTime) + "</div>"
                            + "<div class='col-40'>" + userShop.user.name + "</div>"
                            + "<div class='col-20'>" + userShop.point  + "</div></div>";

                    });
                    $(".shopmember-wrap").append(tempHtml);
                    var total = $('.shopmember-wrap .row-shopmember').length;
                    if (total >= data.count) {
                        // 加载完毕，则注销无限加载事件，以防不必要的加载
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        // 删除加载提示符
                        $('.infinite-scroll-preloader').hide();
                    }
                    loading = false;
                    pageIndex += 1; //pageNum + 1，便于下一次无限滚动知道从什么位置开始
                    $.refreshScroller();
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    }

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        getUserShopMapList();
    });

    $("#search").on("change", function(){
        userName = $(this).val();
        $(".shopmember-wrap").empty();
        pageIndex = 1;
        getUserShopMapList();
        $.init();
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});