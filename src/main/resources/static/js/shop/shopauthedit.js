$(function(){
    var shopAuthId = getQueryString("shopAuthId");
    var getShopAuthById = "/o2o/shopadmin/getshopauthbyid?shopAuthId=" + shopAuthId;
    var modifyShopAuth = "/o2o/shopadmin/modifyshopauthbyid";
    var titleFlag = 1;

    getShopAuth();

    function getShopAuth(){
        if(!shopAuthId){
            $.toast("empty shopAuthId");
            window.location.href = "/o2o/shopadmin/shopauthmanagement";
        }else{
            $.ajax({
                url: getShopAuthById,
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    if(data.success){
                        var shopAuthMap = data.shopAuthMap;
                        $("#employee-name").val(shopAuthMap.employee.name);
                        $("#title").val(shopAuthMap.title);
                        titleFlag = shopAuthMap.titleFlag;
                    }else{
                        $.toast(data.errMsg);
                    }
                }
            })
        }
    }
    $("#submit").on("click", function(){
        var shopAuth = {};
        shopAuth.shopAuthId = shopAuthId;
        shopAuth.title = $("#title").val();
        shopAuth.titleFlag = titleFlag;
        var employee = {};
        employee.name = $("#employee-name").val();
        shopAuth.employee=employee;
        var verifyCodeActual = $("#j_kaptcha").val();
        $.ajax({
            url: modifyShopAuth,
            type: 'post',
            data: {
                verify: true,
                verifyCodeActual: verifyCodeActual,
                shopAuthMapStr: JSON.stringify(shopAuth)
            },
            dataType: 'json',
            success: function(data){
                if(data.success){
                    $.toast("修改成功");
                }else{
                    $.toast(data.errMsg);
                }
            }
        });
    })
});