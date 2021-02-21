$(function () {
    var change = "/o2o/local/changepsd";
    var userType = getQueryString("userType");

    $("#modify").on('click', function () {
        var userName = $("#userName").val();
        var password = $("#oldPassword").val();
        var newPassword = $("#newPassword").val();
        var confirmPassword = $("#confirmPassword").val();
        var verifyCode = $("#j_kaptcha").val();
        if (newPassword !== confirmPassword) {
            $.toast("请确认两次密码输入正确");
            return;
        }
        if (!verifyCode) {
            $.toast("请输入验证码");
            return;
        }
        $.ajax({
            url: change,
            type: 'post',
            data: {
                userName: userName,
                password: password,
                newPassword: newPassword,
                verifyCodeActual: verifyCode,
            },
            success: function (data) {
                if (data.success) {
                    $.toast("修改成功");
                    if (userType === "1") {
                        window.location.href = "/o2o/frontend/index";
                    } else {
                        window.location.href = "/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast(data.errMsg);
                    $("#j_kaptcha").onclick();
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