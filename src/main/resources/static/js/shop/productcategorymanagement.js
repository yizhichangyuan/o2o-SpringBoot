$(function () {
    var getProductCategoryListURL = "/o2o/shopadmin/getproductcategorylist";
    getList();

    function getList() {
        $.ajax({
            url: getProductCategoryListURL,
            type: 'get',
            cache: false,
            success: function (data) {
                if (data.success) {
                    var tempHtml = "";
                    $(".productCategory-wrap").html("");
                    data.productCategoryList.map(function (term, index) {
                        tempHtml += "<div class='row row-productCategory'>" +
                            "<div class='col-33 productCategory-name'>" + term.productCategoryName + "</div>" +
                            "<div class='col-33'>" + term.priority + "</div>" +
                            "<div class='col-33'>" + "<a href='#' class='button deleteProductCategory' id='" + term.productCategoryId + "'>删除</a></div></div>"
                    });
                    $(".productCategory-wrap").html(tempHtml);
                }
            }
        });
    }

    $(".deleteProductCategory").live('click', function () {
        var productCategoryId = $(this).attr('id');
        $.confirm("确定删除吗？", function () {
            $.ajax({
                url: '/o2o/shopadmin/deleteproductcategory?productCategoryId=' + productCategoryId,
                type: 'GET',
                success: function (data) {
                    if (data.success) {
                        getList();
                        $.toast("删除成功");
                    } else {
                        $.toast(data.errMsg);
                    }
                }
            });
        });
    })

    $("#newAdd").click(function () {
        var tempHtml = "<div class='row row-productCategory new-add-productCategory'>" +
            "<div class='col-33 productCategory-name'>" + "<input type='text' class='category-input' id='productCategoryName' placeholder='新增类别名'/>" + "</div>" +
            "<div class='col-33'>" + "<input type='text' class='category-input' id='priority' placeholder='新增权重'/>" + "</div>" +
            "<div class='col-33'>" + "<a href='#' class='button deleteNewAddProductCategory'>删除</a></div></div>";
        $(".productCategory-wrap").append(tempHtml);
    });

    $(".deleteNewAddProductCategory").live("click", function () {
        var parent = $(this).parent().parent();
        $(parent).find("*").map(function (index, term) {
            $(term).remove();
        }); //找到所有直接或非直接后代
        $(parent).remove();
    });


    $("#submit").click(function () {
        var categoryList = [];
        $(".new-add-productCategory").map(function (index, term) {
            console.log(term);
            var temp = {};
            temp.productCategoryName = $(term).find("#productCategoryName").val();
            temp.priority = $(term).find("#priority").val();
            categoryList.push(temp);
        });
        console.log(categoryList);
        $.ajax({
            url: "/o2o/shopadmin/addbatchproductcategory",
            type: "POST",
            data: JSON.stringify(categoryList),
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    // window.location.href='/o2o/shopadmin/getproductcategorylist;
                    $.toast("新增成功");
                    getList();
                } else {
                    $.toast("errMsg：" + "添加类别失败");
                }
            }
        })
    });
});