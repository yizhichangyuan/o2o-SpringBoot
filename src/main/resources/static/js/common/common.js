// 点击切换图片就是再次发送同一个请求，加上一个两位的数字，就会重新更换前端的验证码图片
function changeVerifyCode(img) {
    img.src = "/o2o/Kaptcha?" + Math.floor(Math.random() * 100);
}

function handleClick(button) {
    var id = button.id;
    console.log(id);
}

//找出地址栏中是否有指定名字的参数，例如shopId
function getQueryString(parameter) {
    let reg = new RegExp("(^|&)" + parameter + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null;
}

function transfer(time) {
    var date = new Date(time);
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + '';
    s = date.getSeconds();
    return M + D + h + m;
}

//回退到上一页
function goBack() {
    history.go(-1);
}

$(".index").on("click", function () {
    window.location.href = "/o2o/frontend/index";
});

// 控制前台展示是登陆选项还是退出登录选项
function loginCheck(loginStatus){
    if(loginStatus){
        $("#logout").show();
        $("#login").attr("hidden", "hidden");
    }else{
        $("#login").show();
        $("#logout").attr("hidden", "hidden");
    }
}

$("#login").on("click", function(){
    var userType = $(this).data("usertype");
    var loginPage = "/o2o/local/login?userType=" + userType;
    window.location.href = loginPage;
});


$("#logout").on("click", function () {
    var userType = $(this).data("usertype");
    $.ajax({
        url: "/o2o/local/logout",
        type: 'get',
        success: function (data) {
            if (data.success) {
                // 决定登出后跳转到登录页面，如果登陆成功，地址栏中的userType决定是导向前台展示还是后台管理页面
                if(userType == 1){
                    window.location.href = "/o2o/frontend/index";
                }else{
                    // 跳转后台管理登陆页面
                    window.location.href = "/o2o/local/login?userType=" + 2;
                }
            }
        },
        error: function (data, error) {
            alert(error);
        }
    });
});

// 在注册店铺或者提交商品成功后，将信息清空防止重复提交，提交校验是否为空
function clearInput(){
    $("input").val("");
    $("textarea").val();
}