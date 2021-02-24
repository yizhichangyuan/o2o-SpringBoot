$(function () {
    var getProductDetail = "/o2o/frontend/getproductdetail?productId=";
    var generateQRCode = "/o2o/frontend/generateproductqr?productId=";
    var productId = getQueryString("productId");
    // 打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-js-demo");
    });

    $.ajax({
        url: getProductDetail + productId,
        type: 'get',
        success: function (data) {
            if (data.success) {
                var product = data.product;
                var imgList = product.productImgList;
                $(".bar-nav .title").text(product.productName);
                $(".card-cover").attr("src", ".." + product.imgAddr);
                $(".lastEditTime").text(transfer(product.lastEditTime));
                if (product.point != undefined) {
                    $(".bonus").text("购买可得" + product.point + "积分");
                }
                $(".normal-price s").text("￥" + product.normalPrice);
                $(".promotion-price").text("￥" + product.promotionPrice);
                $(".product-desc").text(product.productDesc);
                var imgHtml = "";
                imgList.map(function (img, index) {
                    imgHtml += "<img alt='" + img.imgDesc + "' src='.." + img.imgAddr + "'/>";
                });
                // 如果需要二维码
                if(data.needQRcode){
                    imgHtml += "<img src='" + generateQRCode + productId + "'/>"
                }
                $(".img-list").html(imgHtml);
            }else {
                $.toast(data.errMsg);
            }
        }
    });

    function getQRCode(){

    }
});