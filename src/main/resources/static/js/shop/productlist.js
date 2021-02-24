$(function () {
    var getProductListURL = "/o2o/shopadmin/getproductlist";
    getProductList();

    function getProductList() {
        $.ajax({
            url: getProductListURL + "?pageIndex=1&pageSize=999",
            type: 'get',
            success: function (data) {
                if (data.success) {
                    var tempHtml = "";
                    // console.log(data.productList);
                    data.productList.map(function (term, index) {
                        // console.log(term);
                        tempHtml += "<div class='row row-product'>" + "<div class='col-30 product-name'>" + term.productName + "</div>"
                            + "<div class='col-20'>" + term.point + "</div>"
                            + "<div class='col-50'>" + addOperation(term.productId, term.enableStatus) + "</div></div>";
                    });
                    // console.log(tempHtml);
                    $(".product-wrap").html(tempHtml);
                } else {
                    $.toast(data.errMsg);
                }
            }
        })
    }

    function addOperation(productId, enableStatus) {
        var pullText = "上架";
        if (enableStatus != 0) {
            pullText = "下架";
        }
        var editUrl = "<a href='/o2o/shopadmin/productoperation?productId=" + productId + "'>" + "编辑</a>";
        var pullUrl = "<a href='#' class='pulldown' id='" + productId + "' data-status='" + enableStatus + "'>" + pullText + "</a>";
        var previewUrl = "<a href='/o2o/frontend/productdetail?productId=" + productId + "'>" + "预览</a>";
        return editUrl + pullUrl + previewUrl;
    }

    $(".pulldown").live("click", function () {
        var productId = $(this).attr("id");
        // console.log($(this).data('status'));
        var enableStatus = 1 - $(this).data("status");
        // console.log(enableStatus);
        var product = {productId: productId, enableStatus: enableStatus};
        $.ajax({
            url: "/o2o/shopadmin/modifyproduct",
            type: "post",
            data: {
                productStr: JSON.stringify(product),
                isPullDown: true,
            },
            success: function (data) {
                if (data.success) {
                    if (enableStatus == 0) {
                        $.toast("下架成功");
                    } else {
                        $.toast("上架成功");
                    }
                    getProductList();
                } else {
                    $.toast("操作失败：" + data.errMsg);
                }
            }
        })
    })
});