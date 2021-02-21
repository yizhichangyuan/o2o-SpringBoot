$(function () {
    // 决定绑定完密码后是跳转前台展示系统还是后台商家管理页面，1是顾客，2是商家
    var userType = getQueryString("userType");
    var accountbind = "/o2o/local/bindlocalauth";

    $("#submit").on("click", function () {
        var userName = $("#userName").val();
        var password = $("#password").val();
        var verifyCode = $("#j_kaptcha").val();

        $.ajax({
            url: accountbind,
            type: 'post',
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                verifyCodeActual: verifyCode
            },
            success: function (data) {
                if (data.success) {
                    $.toast("账号绑定成功");
                    if (userType === "1") {
                        window.location.href = "/o2o/frontend/index";
                    } else {
                        window.location.href = "/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast(data.errMsg);
                }
            }
        });
    });

    $("#back").on("click", function () {
        if (userType === "1") {
            window.location.href = "/o2o/frontend/index";
        } else {
            window.location.href = "/o2o/shopadmin/shoplist";
        }
    });
});