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
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return Y + M + D;
}

//回退到上一页
function goBack() {
    history.go(-1);
}

$(".index").on("click", function () {
    window.location.href = "/o2o/frontend/index";
});