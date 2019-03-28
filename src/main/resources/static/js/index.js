$(function () {
    var bodyW = $(window).width();
    var bodyH = $(window).height();
    $(".part_1").height(bodyH);

    function addclass(e) {
        var win_h = $(window).height() * 1.4;
        var t = $(e).offset().top;
        var top = $(window).scrollTop();
        if (top > t - win_h) {
            $(e).addClass("on")
        }
    };$(window).scroll(function () {
        var top = $(document).scrollTop();
        if (top > 150) {
            $(".navbar-fixed-top").css({"background-Color": "#000", "transition": "background-Color ease-in-out 0.6s",})
        } else if (top < 150) {
            $(".navbar-fixed-top").css({
                "background-Color": "transparent",
                "transition": "background-Color ease-in-out 0.6s",
            })
        }
        ;addclass(".part_2 .major");
        addclass(".part_2 .div_p");
        addclass(".part_2 .div_url");
        addclass(".part_3 .major");
        addclass(".part_3 .row");
        addclass(".part_4 .major");
        addclass(".part_4 .row");
        addclass(".part_4 .div_url")
    })
})
$(function(){
    //根据不同浏览设备，加载不同图片
    pic_html();
    function pic_html(){
        if($(window).width()>1024){
            $(".part_1").attr("class","index_img part_1");
        }else{
            $(".part_1").attr("class","index_img2 part_1");
        }
    }
    //当调整浏览器窗口的大小时，发生 resize 事件
    $(window).resize(function(){
        pic_html();
    })
})

