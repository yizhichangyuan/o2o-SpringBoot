$(function(){
    var awardName = "";
    var url = "/o2o/shopadmin/getuserawardmaplist?";
    var pageIndex = 1;
    var pageSize = 10;
    var loading = false;

    getUserAwardList();

    function getUserAwardList(){
        url = url + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&awardName=" + awardName;
        loading = true;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data.success){
                    var tempHtml = "";
                    data.userAwardMapList.map(function(userAward, index){
                        tempHtml += "<div class='row row-useraward'>" + "<div class='col-15'>" + userAward.award.awardName + "</div>"
                            + "<div class='col-30'>" + transfer(userAward.lastEditTime) + "</div>"
                            + "<div class='col-16'>" + checkUsed(userAward.usedStatus)  + "</div>"
                            + "<div class='col-15'>" + userAward.user.name + "</div>"
                            + "<div class='col-15'>" + userAward.point + "</div>"
                            + "<div class='col-15'>" + checkOperator(userAward) + "</div></div>";
                    });
                    $(".useraward-wrap").append(tempHtml);
                    var total = $('.useraward-wrap .row-useraward').length;
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

    function checkOperator(userAward){
        if(userAward.operator != null){
            return userAward.operator.name;
        }else{
            return "无";
        }
    }

    function checkUsed(usedStatus){
        if(usedStatus == 0){
            return "未领取";
        }else{
            return "已领取"
        }
    }

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        getUserAwardList();
    });

    $("#search").on("change", function(){
        awardName = $(this).val();
        $(".award-wrap").empty();
        pageIndex = 1;
        getUserAwardList();
        $.init();
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});