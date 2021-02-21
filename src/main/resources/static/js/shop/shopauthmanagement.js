$(function(){
    var getShopAuthListUrl = "/o2o/shopadmin/listshopauth?pageIndex=1&pageSize=100";
    var modifyShopAuth = "/o2o/shopadmin/modifyshopauthbyid";

    getShopAuthList();

    function getShopAuthList(){
        $.ajax({
            url: getShopAuthListUrl,
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data.success){
                    var tempHtml = "";
                    data.shopAuthList.map(function(shopAuth, index) {
                        tempHtml += "<div class='row row-shopauth'>" + "<div class='col-40 employee-name'>" + shopAuth.employee.name + "</div>"
                            + "<div class='col-40'>" + shopAuth.title + "</div>"
                            + "<div class='col-20'>" + operator(shopAuth) + "</div></div>";
                    });
                    $(".shopauth-wrap").html(tempHtml);
                }else{
                    $.toast(data.errMsg);
                }
            }
        });
    }

    $(".shopauth-wrap").on( /**/"click", "a", function(e){
        var target = $(e.currentTarget);
        var shopAuthId = $(this).data('shopauthid');
        if(target.hasClass('edit')){
            window.location.href = "/o2o/shopadmin/shopauthedit?shopAuthId=" + shopAuthId;
        }else if(target.hasClass('status')){
            var status = $(this).data('status');
            var titleFlag = $(this).data("titleflag");
            handleStatus(shopAuthId, status, titleFlag);
        }
    });

    function handleStatus(shopAuthId, status, titleFlag){
        var shopAuthStr = {};
        shopAuthStr.enableStatus = status;
        shopAuthStr.shopAuthId=shopAuthId;
        shopAuthStr.titleFlag = titleFlag;
        $.confirm("你确定么？", function(){
            $.ajax({
                url: modifyShopAuth,
                type: 'post',
                data: {
                    shopAuthMapStr: JSON.stringify(shopAuthStr),
                    verify: false
                },
                dataType: 'json',
                success: function(data){
                    if(data.success){
                        $.toast("操作成功");
                        getShopAuthList();
                    }else{
                        $.toast(errMsg);
                    }
                }
            })
        });

    }

    function opposite(enableStatus){
        if(enableStatus === 0) {
            return 1;
        }else{
            return 0;
        }
    }

    function operator(shopAuth){
        if(shopAuth.titleFlag === 0){
            return "不可操作";
        }else{
            var temp = "<a href='#' class='edit' data-shopauthid='" + shopAuth.shopAuthId + "' data-titleflag='"
                + shopAuth.titleFlag + "'>编辑</a>";
            if(shopAuth.enableStatus === 1){
                temp += "<a href='#' class='status' data-status='" + opposite(shopAuth.enableStatus) + "' data-shopauthid='"
                    + shopAuth.shopAuthId + "' data-titleflag='" + shopAuth.titleFlag + "'>删除</a>"
            }else{
                temp += "<a href='#' class='status' data-status='" + opposite(shopAuth.enableStatus) + "' data-shopauthid='"
                    + shopAuth.shopAuthId + "' data-titleflag='" + shopAuth.titleFlag + "'>恢复</a>"
        }
            return temp;
        }
    }



});