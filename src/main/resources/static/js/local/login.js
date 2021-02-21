$(function () {
    // 决定登陆后是前往前台展示页面还是后台
    var userType = getQueryString("userType");
    var login = "/o2o/local/logincheck";
    var count = 0;
    var verify = false;

    $("#login").on("click", function () {
        var userName = $("#userName").val();
        var password = $("#password").val();
        var verifyCode = $("#j_kaptcha").val();
        $.ajax({
            url: login,
            type: 'post',
            data: {
                userName: userName,
                password: password,
                verifyCodeActual: verifyCode,
                verify: verify,
            },
            success: function (data) {
                if (data.success) {
                    if (userType === "1") {
                        window.location.href = "/o2o/frontend/index";
                    } else {
                        window.location.href = "/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast("用户名密码错误");
                    count++;
                    if (count >= 3) {
                        $("#checkCode").show();
                        verify = true;
                    }
                }
            }
        })
    });
});