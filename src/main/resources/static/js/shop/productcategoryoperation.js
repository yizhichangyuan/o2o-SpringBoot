$(function () {
    $("#submit").click(function () {
        var productCategoryName = $("#productCategoryName").val();
        var priority = $("#priority").val();
        var checkCode = $("#j_kaptcha").val();
        if (!productCategoryName.trim()) {
            $.toast("商品类别名为空");
            return;
        }
        if (!priority.match(new RegExp("\\d+"))) {
            $.toast("请确认权重输入为数字");
            return;
        }
        var productCategory = {};
        productCategory.productCategoryName = productCategoryName;
        productCategory.priority = priority;
        var formData = new FormData();
        formData.append("productCategory", JSON.stringify(productCategory));
        formData.append("verifyCodeActual", checkCode);
        $.ajax({
            url: "/o2o/shopadmin/registerproductcategory",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,  // jQuery不要去设置Content-Type请求头
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功！");
                } else {
                    $.toast("提交失败！" + data.errMsg);
                }
                // 不管提交是否成功，都要改变一下验证码，因为点击事件连接着一个切换图片的事件
                $("#kaptcha_img").click();
            }
        });
    });
});