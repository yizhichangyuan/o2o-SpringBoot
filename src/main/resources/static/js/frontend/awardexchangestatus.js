$(function () {
    var userAwardId = getQueryString("userAwardId");
    var getPageInfo = "/o2o/frontend/getuserawardbyid?userAwardId=" + userAwardId;
    addAwards(); //预先加载3条

    function addAwards() {
        $.ajax({
            url: getPageInfo,
            type: "GET",
            success: function (data) {
                if (data.success) {
                    var userAwardMap = data.userAwardMap;
                    var awardHtml = "<div class=\"card\">\n" +
                            "            <div class=\"card-header\">" + userAwardMap.award.awardName + "<p class='point'>消耗积分：" + userAwardMap.point + "</p></div>\n" +
                            "            <div class=\"card-content\">\n" +
                            "                <div class=\"list-block media-list\">\n" +
                            "                    <ul>\n" +
                            "                        <li class=\"item-content\">\n" +
                            "                            <div class=\"item-media\">\n" +
                            "                                <img src='.." + userAwardMap.award.awardImg + "' width=\"44\">\n" +
                            "                            </div>\n" +
                            "                            <div class=\"item-inner\">\n" +
                            "                                <div class=\"item-title-row\">\n" +
                            "                                    <div class=\"item-title\">" + userAwardMap.award.awardDesc + "</div>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </li>\n" +
                            "                    </ul>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "            <div class=\"card-footer\">\n" +
                            "                <span class=\"last-edit-time\">" + transfer(userAwardMap.lastEditTime) + "</span>\n" +
                            "                <span><a href=\"#\" class='exchange' data-awardId='" + userAwardMap.award.awardId + "'></a></span>\n" +
                            "            </div>\n" +
                            "        </div>";
                    $(".shop-award-list").html(awardHtml);
                    if(userAwardMap.usedStatus == 0){
                        $("#exchange-img").attr("src", "/o2o/frontend/generateawardexchange?userAwardId=" + userAwardMap.userAwardId);
                    }else{
                        $("#exchange-img").attr("hidden", "hidden");
                    }
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    }
});