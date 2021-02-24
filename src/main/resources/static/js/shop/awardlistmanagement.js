$(function(){
    var awardName = "";
    var url = "/o2o/shopadmin/getawardlistbyshop?";
    var pageIndex = 1;
    var pageSize = 10;
    var loading = false;

    getAwardList();

    function getAwardList(){
        url = url + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&awardName=" + awardName;
        loading = true;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data.success){
                    var tempHtml = "";
                    data.awardList.map(function(award, index){
                        tempHtml += "<div class='row row-award'>" + "<div class='col-40'>" + award.awardName + "</div>"
                            + "<div class='col-40'>" + award.point + "</div>"
                            + "<div class='col-20'>" + addOperation(award.awardId, award.enableStatus)  + "</div></div>";

                    });
                    $(".award-wrap").append(tempHtml);
                    var total = $('.award-wrap .row-award').length;
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

    function addOperation(awardId, enableStatus) {
        var pullText = "上架";
        if (enableStatus != 0) {
            pullText = "下架";
        }
        var editUrl = "<a href='/o2o/shopadmin/awardedit?awardId=" + awardId + "'>" + "编辑</a>";
        var pullUrl = "<a href='#' class='pulldown' id='" + awardId + "' data-status='" + enableStatus + "'>" + pullText + "</a>";
        var previewUrl = "<a href='#'>" + "预览</a>";
        return editUrl + pullUrl + previewUrl;
    }

    $(".pulldown").live("click", function () {
        var awardId = $(this).attr("id");
        console.log($(this).data('status'));
        var enableStatus = 1 - $(this).data("status");
        console.log(enableStatus);
        var award = {awardId: awardId, enableStatus: enableStatus};
        $.ajax({
            url: "/o2o/shopadmin/modifyaward",
            type: "post",
            data: {
                awardStr: JSON.stringify(award),
                statusChange: true
            },
            success: function (data) {
                if (data.success) {
                    if (enableStatus == 0) {
                        $.toast("下架成功");
                    } else {
                        $.toast("上架成功");
                    }
                    $(".award-wrap").empty();
                    getAwardList();
                } else {
                    $.toast("操作失败：" + data.errMsg);
                }
            }
        })
    });

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        getAwardList();
    });

    $("#search").on("change", function(){
        awardName = $(this).val();
        $(".award-wrap").empty();
        pageIndex = 1;
        getAwardList();
        $.init();
    });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});