$(function(){

    var bp = document.createElement('script');
    var curProtocol = window.location.protocol.split(':')[0];
    if (curProtocol === 'https') {
        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
    }
    else {
        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
    }
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(bp, s);

	var bodyW = $(window).width();
	var bodyH = $(window).height();

	//菜单栏
    $(document).click(function(){
		$('.nav-list').removeClass('open')
	})

	$('.nav-menu,.nav-list').click(function(e){e.stopPropagation()})
	$('nav').find('.nav-menu').click(function(e){
		$('.nav-list').toggleClass('open')
	})

    $.ajax({
        type:'get',
        url:'/is/login',
        success:function (isLogin) {
            if ("no"!=isLogin){
                /*$("#login").attr("hidden","hidden");
                $("#logout").attr("hidden","");*/
                $("#logo").append(' <img src="'+isLogin+'">')
                $("#login").css('display','none');
                $("#logout").css('display','block');
            }else {
                $("#login").css('display','block');
                $("#logout").css('display','none');
            }
        }
    })

    /*$.ajax({
        type: "get",
        url: "/api/visit",
        beforeSend: function(XMLHttpRequest){
            //$("body").append(str);
        },
        success: function(data, textStatus){
            console.log(data)
            document.getElementsByClassName("number")[0].innerHTML = data;
        },
        complete: function(XMLHttpRequest, textStatus){
            setTimeout(function(){
                $(".loading").remove();
            },500)
        },
        error: function(){}
    });*/

    $(window).scroll(function(){
        var top = $(document).scrollTop();
        //动态设置置顶按钮的显示和影藏
        if(top > bodyH * 0.5){
            $(".fh").addClass("fh_left");
        }else if(top < bodyH){
            $(".fh").removeClass("fh_left");
        }
    });
    

    
    
    
    
    $(".btn_img").click(function(){
    	$(".head").css({
    		"display":"block"
    	})

    	var dynamic = $(document).scrollTop();
        //创建监听滚动条状态事件，并判断滚动条是否在运动。如果在，则关闭大图窗口
        $(window).scroll(function(){
            var top = $(document).scrollTop();
            if(top>dynamic||top<dynamic){
                $(".head").fadeOut(100);
            }
        })
    })
    
    $(".close_btn").click(function(){
    	$(".head").css({
    		"display":"none"
    	})
    })
   	

    setTimeout(function () {
        $(".page").css({
            "display":"block"
        })
    },1500);

    //返回顶部
    $(".fh").click(function(){
        $("body,html").stop().animate({scrollTop:0},300);
    });

});
