$(function () {
    var logout = "/o2o/local/logout";
    $("#logout").on("click", function () {
        var userType = $(this).data("usertype");
        $.ajax({
            url: logout,
            type: 'get',
            success: function (data) {
                if (data.success) {
                    // 决定登出后跳转到登录页面，如果登陆成功，地址栏中的userType决定是导向前台展示还是后台管理页面
                    window.location.href = "/o2o/local/login?userType=" + userType;
                }
            },
            error: function (data, error) {
                alert(error);
            }
        });
    });
});