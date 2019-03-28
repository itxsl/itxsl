$(function () {
    loadComment()
})

function loadComment() {
    $.get("/api/link", function (result) {
        console.log(result)
        var data = result.data;
        $(".context_list").html("");
        if (data == null || data.length < 1) {
            $(".message_em").text("0")
            $(".context_list").html('<div class="context_null">当前暂无任何友情链接！</div>')
        } else {
            $(".number_em").text("" + result.totalSize + "")
            var str = ""
            for (var i = 0; i < data.length; i++) {
               str+= '<li class="apply_li"><div class="apply_liuyan clear" id="8"><div class="liuyan_img">' +
                '<img src="'+data[i].icon2+'">' +
                '</div><div class="liuyan_con"><div class="liuyan_name">'+data[i].nickname+'</div><div class="liuyan_con">' +
                '<p>~&nbsp;&nbsp;网站名称：'+data[i].title+'</p>' +
                '<p>~&nbsp;&nbsp;网站描述：'+data[i].synopsis+'</p>' +
                '<p>~&nbsp;&nbsp;网站链接：'+data[i].url+'</p>' +
                '<p>~&nbsp;&nbsp;网站头像：'+data[i].icon+'</p>' +
                '</div> <div class="liuyan_time"><span>'+data[i].createTime+'</span></div></li>'
            }
            $(".apply_list").append(str)
            if (result.totalSize == 0) {
                $(".context_list").css({
                    "display": "none"
                })
            } else {
                $(".context_list").css({
                    "display": "block"
                })
            }
        }

    })
}

$("#apply_link").click(function () {
    $.get("/is/login",function (isLogin) {
        if (isLogin){
            var email = $("#text_email").val();
            var title = $("#text_title").val();
            var url = $("#text_url").val();
            var icon = $("#text_icon").val();
            var synopsis = $("#text_synopsis").val();
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (IsEmpty(title)) {
                layer.msg("请检查网站名称！", {icon: 6})
                return false;
            }
            if (IsEmpty(url)) {
                layer.msg("请检查网站URL！", {icon: 6})
                return false;
            }
            if (IsEmpty(icon)) {
                layer.msg("请检查网站图标！", {icon: 6})
                return false;
            }
            if (IsEmpty(synopsis)) {
                layer.msg("请检查网站描述！", {icon: 6})
                return false;
            }
            $.get("/is/email", function (result) {
                if (!result) {
                    if (IsEmpty(email) || !reg.test(email)) {
                        layer.msg("请检查邮箱格式！", {icon: 6})
                        return false;
                    }
                }
            });
            $.post("/api/link",
                {
                    'title': title,
                    'url': url,
                    'icon': icon,
                    'synopsis': synopsis,
                    'email': email
                },
                function (result) {
                    if (result.code==200){
                        layer.msg("友联申请提交成功！", {icon: 6})
                    }else {
                        layer.msg("友联申请提交失败！", {icon: 5})
                    }
                })
        } else {
            layer.msg("请先登录！", {icon: 6})
        }
    })

})

function IsEmpty(str) {
    if (str == undefined || str == "" || str.length < 1) {
        return true;
    }
    return false;
}