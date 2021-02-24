$(function () {
    var registerAwardURL = "/o2o/shopadmin/addaward";
    var getAwardByIdURL = "/o2o/shopadmin/getawardbyid";
    var awardId = getQueryString("awardId");
    var modifyAwardURL = "/o2o/shopadmin/modifyaward";
    var urlHaveAwardId = awardId ? true : false;

    if (urlHaveAwardId) {
        getAwardById(awardId);
    }

    function getAwardById(awardId) {
        getAwardByIdURL += "?awardId=" + awardId;
        $.getJSON(getAwardByIdURL, function (data) {
            console.log(data);
            var tempHTML = "";
            if (data.success) {
                var award = data.award;
                $("#award-name").val(award.awardName);
                $("#priority").val(award.priority);
                $("#award-desc").val(award.awardDesc);
                $("#point").val(award.point);
            }else{
                $.toast(data.errMsg);
            }
        })

    }

    $('#submit').click(function () {
        var award = {};
        if (urlHaveAwardId) {
            award.awardId = awardId;
        }
        award.awardName = $("#award-name").val();
        award.awardDesc = $("#award-desc").val();
        award.priority = $("#priority").val();
        award.point = $("#point").val();
        if (!award.awardName.trim()) {
            $.toast("奖品名不能为空");
            return;
        }
        var awardImg;
        if($("#award-img")[0].files[0] != undefined){
            awardImg = $("#award-img")[0].files[0];
        }
        // 使用formData来构造表达数据
        var formData = new FormData();
        formData.append("awardStr", JSON.stringify(award));
        formData.append("awardImg", awardImg);
        formData.append("statusChange", "false"); //需要验证码校验
        var verifyCode = $("#j_kaptcha").val();
        if (!verifyCode) {
            $.toast("验证码为空");
            return;
        }
        formData.append("verifyCodeActual", verifyCode);
        $.ajax({
            url: (urlHaveAwardId ? modifyAwardURL : registerAwardURL),
            type: 'POST',
            data: formData,
            contentType: false, // 因为有文字和图片，所以为false
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功！");
                } else {
                    $.toast("提交失败！" + data.errMsg);
                }
                // 不管提交是否成功，都要改变一下验证码，因为点击事件连接着一个切换图片的事件
                $("#kaptcha-img").click();
            }
        });
    });
});