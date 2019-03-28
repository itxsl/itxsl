$(function(){
    $.ajax({
        url: "/api/link",
        type: "get",
        dataType: "json",
        success: function (result) {
           if (result.code==200){
               data = result.data
               $("#totalSize").text(result.totalSize)
			   var str=""
               for(var i=0;i<data.length;i++){
                   str += "<div class='col-sm-4 col-md-3 blog' style=''>"+
                       "<a href='"+data[i].url+"' target='_Blank' class='link_blog'>"+
                       "<div class='thumbnail thumbnail_link thumbnail-btn--border-line'>"+
                       "<img class='blog_img' src="+data[i].icon+">"+
                       "<div class='caption'>"+
                       "<h3>"+data[i].title+"</h3>"+
                       "<p>"+data[i].synopsis+"</p>"+
                       "</div>"+
                       "</div>"+
                       "</a>"+
                       "</div>";
			   }
               $(".link_list .row").append(str);
           }
        },
        error: function (err) {
            // layer.msg("服务繁忙，请稍后再试！",{icon: 1});
        },
        complete: function () {
        }
    });
/*	var str = ""
	
	for(var i=0;i<21;i++){
        str += "<div class='col-sm-4 col-md-3 blog' style=''>"+
					"<a href='http://www.itxsl.cn' target='_Blank' class='link_blog'>"+
						"<div class='thumbnail thumbnail_link thumbnail-btn--border-line'>"+
				      		"<img class='blog_img' src=\"http://qzapp.qlogo.cn/qzapp/101500524/E2A2D692EF9E63B6D0C7A0A764E39358/100\">"+
				      		"<div class='caption'>"+
				        		"<h3>"+"七年"+"</h3>"+
				        		"<p>"+"数据测试"+"</p>"+
				     		"</div>"+
				   		"</div>"+
					"</a>"+				    
			  	"</div>";
    }
    $(".link_list .row").append(str);*/
})
