$(function () {
    addUrl();

    function addUrl() {
        var shopId = getQueryString("shopId");
        $("#shopinfo").attr("href", "/o2o/shopadmin/shopoperation?shopId=" + shopId);
        $.ajax({
            url: "/o2o/shopadmin/getshopmanagementinfo?shopId=" + shopId,
            type: 'get',
            success: function (data) {
                if (data.redirect) {
                    window.location.href = data.url;
                }
            }
        });
    }
});