var  pageNumber =1;
var pageSize=5;
var pageTotal = 0;
var articleType = "";
var count =0
var title = "";
$(function(){

   $(".input_search").click(function () {
       title = $(".input_title").val()
       $(".muen_list").html("");
       pageNumber=1;
       list()
   })
    $(".screen_ul>li").click(function(){

        $(this)
            .addClass('selected')
            .siblings().removeClass('selected')
            .end()
            .find(':radio').attr('cheked',true)

        var id = $(this).attr("id");
        if (id==count) {
            return
        }
        $(".relevant_list").each(function () {
            var uId = $(this).attr("id");

            if(id!=0){
                count=id
                if(id==1){
                    articleType=1;
                }else if (id==2)  {
                    articleType=2;
                }else{
                    articleType=3;
                }
            }else {
                count=id
                articleType="";
            }
            $(this).css({
                "display":"none"
            });
        });
        pageNumber=1;
        title = null;
        list()
    })


    //监听输入框事件
    $("#search").click(function(){
        title = $("#search_text").val();
       //  $(".muen_list").html("");
        list()
    });


    $(".btn_loading").click(function(){
        var id = parseInt($(this).attr("id"))
        if(id>pageSize-1){
            layer.msg("加载完毕！", {icon: 6})
            return
        }else{
            var c = ++id
            pageNumber+=1;
            if (pageTotal==pageNumber||pageTotal<pageNumber) {
                layer.msg("加载完毕！", {icon: 6})
                return false;
            }
            $(".btn_loading").attr("id",c)
            list()
        }
    })
    list()
    function list(){
        $.ajax({
            type:"get",
            url:"/api/article",
            data:{
                pageSize: pageSize,
                type:articleType,
                pageNumber:pageNumber,
                title:title,
                available:0
            },
            async:true,
            dataType:"json",
            success:function(data){
                $(".wenzhang").html(data.totalSize);
                $(".commentTotal").html(data.totalCount);
                if (data.totalSize%pageSize==0){
                    pageTotal = data.totalSize/pageSize;
                } {
                    pageTotal = (data.totalSize/pageSize)+1;
                }
                var data = data.data;
                if(data!=undefined&&data!=null){
                    var str = "";
                    for(var i=0;i<data.length;i++){
                        var con = "";
                        if(data[i].introduction==""){
                            con = "博主很懒，没有留下摘要"
                        }else{
                            con=data[i].synopsis
                        }
                        var type = "";
                        if (data[i].type=="1"){
                            type="技术分享"
                        }else if (data[i].type=="2"){
                            type="生活点滴"

                        }  else {
                            type="更新公告"

                        }
                        str += "<div class='relevant_list row clear'  id='"+type+"'>" +
                            "<div class='list_img col-sm-4 col-md-4'>" +
                            "<img src='"+data[i].cover+"'/>" +
                            "</div>" +
                            "<div class='list_context col-xs-12 col-sm-8 col-md-8'>" +
                            "<div class='list_title'>" +
                            "<a href='/content?id="+data[i].id+"' class='a_title'>" +
                            "<em style='color: orangered;font-size: 16px;'>『"+data[i].original+"』</em>"+data[i].title+"</a>" +
                            "</div>" +
                            "<div class='list_txt'>"+con+"</div>" +
                            "<div class='list_other clear'>" +
                            "<div class='other_time'>" +
                            "<img src='/static/img/shijian_riji.png'/>" +
                            "<span>"+data[i].createTime+"</span>" +
                            "</div>" +
                            "<div class='other_visit'>" +
                            "<img src='/static/img/fangwen_riji.png'/>" +
                            "<span>"+data[i].click+"</span>" +
                            "</div>" +
                            "<div class='other_type'>" +
                            "<img src='/static/img/fenlei_riji.png'/>" +
                            "<span>"+type+"</span>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>"
                    }

                    $(".muen_list").append(str);
                }
            },
            error:function(err){
               // layer.msg("服务繁忙，请稍后再试！",{icon: 1});
            }
        });
    }


})
