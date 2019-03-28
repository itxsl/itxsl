var pageSize = 5;
var pageNumber = 1;
var totalSize =10;
var clearMessage = true;
$(function () {
    var E = window.wangEditor
    wangEditor = new E('#wEditor')
    wangEditor.customConfig.menus = [
        'fontName',  // 字体
        'italic',  // 斜体
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'quote',  // 引用
        'emoticon',  // 表情
        'undo',  // 撤销
        'redo'  // 重复
    ]
    wangEditor.create();
    $(".btn_tj").click(function () {
        $.ajax({
            type: 'get',
            url: '/is/login',
            success: function (isLogin) {
                if ("no" == isLogin) {
                    layer.msg('登录后才能评论哦！', {icon: 6});
                    return false;
                } else {
                    var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                    $.get("/is/email", function (result) {
                        if (wangEditor.txt.text().length<1){
                            layer.msg('请输入内容！', {icon: 6});
                            return false;
                        }
                        if (result) {
                            $.post("/api/message",
                                {
                                    'message': wangEditor.txt.html()
                                },
                                function (result) {
                                    layer.msg("留言成功！", {icon: 6});
                                    wangEditor.txt.html("");
                                    clearMessage = true;
                                    pageNumber=1;
                                    loadComment();
                                })
                        } else {
                            var email = $("#text_email").val();
                            if (email === "") { //输入不能为空
                                layer.msg('为了能及时告知您有回复，请填写邮箱！', {icon: 6});
                                return false;
                            } else if (!reg.test(email)) { //正则验证不通过，格式不对
                                layer.msg('邮箱格式有误，亲！', {icon: 6});
                                return false;
                            } else {
                                $.post("/api/message",
                                    {
                                        'email': email,
                                        'message': wangEditor.txt.html()
                                    },
                                    function (result) {
                                        layer.msg("留言成功！", {icon: 6});
                                        wangEditor.txt.html("");
                                        clearMessage = true;
                                        pageNumber=1;
                                        loadComment();
                                    })
                            }

                        }
                    });

                }
            }
        })


    })

})




function loadComment() {
    $.get("/api/message?pageSize="+pageSize+"&pageNumber="+pageNumber, function (result) {
        var data = result.data;
        if (clearMessage){
            $(".context_list").html("");
            clearMessage =false;
        }
        if (data == null || data.length < 1) {
            $(".message_em").text("0")
            $(".context_list").html('<div class="context_null">当前暂无任何留言！</div>')
        } else {
            totalSize = result.totalSize;
            $(".message_em").text("" + result.totalSize + "")
            var photo = ""
            for (var i = 0; i < data.length; i++) {
                str = "<ul class='context_li'><div class='context_liuyan clear' id='" + data[i].id + "_'><div class='liuyan_img'><img src='" + data[i].icon + "' /></div><div class='liuyan_con'><div class='liuyan_name'>" + data[i].nickname + "</div><div class='liuyan_con'>" + data[i].message + "</div> <div class='liuyan_time'><span>" + data[i].createTime + "</span><a role='button' class='btn_mess_hf'  id='" + data[i].id + "'>回复</a></div></div></div><div class='context_txt clear'><textarea class='context_textarea'></textarea><a role='button' class='btn_mess_tj'>提交</a><a role='button' class='btn_mess_sq'>收起</a><label class='hf_ts'></label></div></ul>";
                $(".context_list").append(str);
                str = ""
                if (data[i].childs != null) {
                    var sto = "";
                    for (var j = 0; j < data[i].childs.length; j++) {
                        sto += "<div class='context_reply clear' id='" + data[i].childs[j].id + "'><div class='reply_img'><img src='" + data[i].childs[j].icon + "' /></div><div class='reply_con'><div class='reply_name'>" + data[i].childs[j].nickname + "</div><div class='reply_con'>" + data[i].childs[j].message + "</div><div class='reply_time'><span>" + data[i].childs[j].createTime + "</span><a role='button' id='" + data[i].id + "'  class='btn_mess_hf'>回复</a></div></div></div>"
                    }
                    ;$("#" + data[i].id + "_").after(sto)
                }
                ;$('.btn_mess_hf').unbind('click');
                $(".btn_mess_hf").click(function () {
                    $(this).parent().parent().parent().siblings(".context_txt").css({"display": "block"});
                    var a = $(this).attr("id");
                    $('.btn_mess_tj').unbind('click');
                    $(".btn_mess_tj").click(function () {
                        var tare = $(this).prev(".context_textarea").val();
                        $.get("/is/login", function (result) {
                            if (result == "no") {
                                layer.msg('回复留言需要登录噢！', {icon: 1, offset: '36%'});
                                return false;
                            } else {
                                $.ajax({
                                    type: 'POST',
                                    url: '/api/message',
                                    dataType: "json",
                                    data: {
                                        'parentId': a,
                                        'message': tare
                                    },
                                    success: function (result) {
                                        if (result.code == 200) {
                                            layer.msg('回复成功！', {icon: 1, offset: '36%'});
                                            clearMessage = true;
                                            pageNumber=1;
                                            loadComment()
                                        } else {
                                            layer.msg(result.message, {icon: 5, offset: '36%'});
                                        }
                                    }
                                });
                                $(".context_txt").css({"display": "none"})
                            }
                        })

                    })
                    $(".btn_mess_sq").click(function () {
                        $(".context_txt").css({"display": "none"})
                    })
                })
            }
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
var isbool=true;//触发开关，防止多次调用事件
var ac = -1;
$(window).scroll(function() {
    if (($(this).scrollTop() + $(window).height()) >= $(document).height() && isbool==true) {
        if (totalSize>pageSize*(pageNumber-1)) {
            loadComment()
        }else {
            isbool=false;
        }
        pageNumber+=1
    }
});

