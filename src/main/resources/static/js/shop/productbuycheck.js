$(function(){
   var productName = "";
   var pageIndex = 1;
   var pageSize = 10;
   var loading = false;

   getUserProductMapList();

   function getUserProductMapList(){
       var listuserproductbyshop = "/o2o/shopadmin/listuserproductmapbyshop?pageIndex=" + pageIndex + "&pageSize="
           + pageSize + "&productName=" + productName;
       var loading = true;
       $.ajax({
           url: listuserproductbyshop,
           type: 'get',
           dataType: 'json',
           success: function(data){
               if(data.success){
                   var tempHtml = "";
                   data.userProductMapList.map(function(userProduct, index){
                       tempHtml += "<div class='row row-productbuy'>" + "<div class='col-10'>" + userProduct.product.productName + "</div>"
                           + "<div class='col-40'>" + transfer(userProduct.createTime) + "</div>"
                           + "<div class='col-20'>" + userProduct.user.name + "</div>"
                           + "<div class='col-10'>" + userProduct.point + "</div>"
                           + "<div class='col-20'>" + userProduct.operator.name  + "</div></div>";

                   });
                   $(".productbuy-wrap").append(tempHtml);
                   var total = $('.productbuy-wrap .row-productbuy').length;
                   if (total >= data.count) {
                       // 加载完毕，则注销无限加载事件，以防不必要的加载
                       $.detachInfiniteScroll($('.infinite-scroll'));
                       // 删除加载提示符
                       $('.infinite-scroll-preloader').hide();
                   }
                   loading = false;
                   pageIndex += 1; //pageNum + 1，便于下一次无限滚动知道从什么位置开始
                   $.refreshScroller();
               } else {
                   $.toast(data.errMsg);
               }
           }
       });
   }

   var myChart = echarts.init(document.getElementById('chart'));

   $.ajax({
       url: "/o2o/shopadmin/productselldaily",
       type: 'get',
       dataType: 'json',
       success: function(data){
           if(data.success){
               option.series = data.series;
               option.xAxis = data.xData;
               option.legend = {data: data.legend};
               myChart.setOption(option);
           }else{
               $.toast(data.errMsg);
           }
       }
   });

   var option = {
       tooltip: {
           trigger: 'axis',
           axisPointer:{
               type: 'shadow'
           }

       },
       legend: {
           data: ['蒸发量', '降水量']
       },
       // toolbox: {
       //     show: true,
       //     feature: {
       //         dataView: {show: true, readOnly: false},
       //         magicType: {show: true, type: ['line', 'bar']},
       //         restore: {show: true},
       //         saveAsImage: {show: true}
       //     }
       // },
       calculable: true,
       xAxis: [
           {
               type: 'category',
               data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
           }
       ],
       yAxis: [
           {
               type: 'value'
           }
       ],
       series: [
           {
               name: '蒸发量',
               type: 'bar',
               data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
           },
           {
               name: '降水量',
               type: 'bar',
               data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
           }
       ]
   };

    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        getUserProductMapList();
    });

   $("#search").on("change", function(){
       productName = $(this).val();
       $(".productbuy-wrap").empty();
       pageIndex = 1;
       getUserProductMapList();
       $.init();
   });

    // 初始化page，并且同时class为page的元素需要添加一个class为page-current的属性，无限滚动才能运行！！！
    $.init();
});