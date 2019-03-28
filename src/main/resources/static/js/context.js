var wangEditor;

$(function () {
    var contentType = $("#contentType").html();
    if (contentType != 1) {
        var EditormdView;
        EditormdView = editormd.markdownToHTML("editormd-view", {
            htmlDecode: "style,script,iframe",  // you can filter tags decode
            emoji: true,
            taskList: true,
            tex: true,  // 默认不解析
            flowChart: true,  // 默认不解析
            sequenceDiagram: true,  // 默认不解析
        });
    } else {

    }
    var E = window.wangEditor
    wangEditor = new E('#wEditor')
    wangEditor.customConfig.zIndex = 1
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
    wangEditor.create()
    loadComment();
    var music_id = $("#music_id").val();
    if (music_id!=undefined&&music_id!=null&&music_id!=""){
        $.get("/api/music/"+music_id,function (result) {
            if (result.code==200){
                var data = result.data;
                var ap3 = new APlayer({
                    element: document.getElementById('player3'),//样式1
                    narrow: false,
                    autoplay: true,
                    showlrc: false,
                    music: {
                        title: data.name,
                        author: data.singer,
                        url: data.url,
                        pic: data.cover
                    }
                });
                ap3.init();
            }else {
                $(".music_play").attr("hidden","hidden");
            }

        })
    }else {
        $(".music_play").attr("hidden","hidden");
    }
});

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
                    if (wangEditor.txt.text().length<3){
                        layer.msg('请输入内容，字数大于3！', {icon: 6});
                        return false;
                    }
                    if (result) {
                        $.post("/api/comment",
                            {
                                'belongId': $(".context_id").val(),
                                'content': wangEditor.txt.html()
                            },
                            function (result) {
                                layer.msg("评论成功！", {icon: 6});
                                $(".context_id").val("")
                                wangEditor.txt.html("")
                                loadComment()
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
                            $.post("/api/comment",
                                {
                                    'belongId': $(".context_id").val(),
                                    'email': email,
                                    'content': wangEditor.txt.html()
                                },
                                function (result) {
                                    layer.msg("评论成功！", {icon: 6});
                                    $(".context_id").val("")
                                    wangEditor.txt.html("")
                                    loadComment()
                                })
                        }

                    }
                });

            }
        }
    })


})


function loadComment() {
    $.get("/api/comment?belongId=" + $(".context_id").val(), function (result) {
        var data = result.data;
        $(".context_list").html("");
        if (data == null || data.length < 1) {
            $(".message_em").text("0")
            $(".context_list").html('<div class="context_null">当前文章暂无任何评论！</div>')
        } else {
            $(".number_em").text("" + result.totalSize + "")
            var photo = ""
            for (var i = 0; i < data.length; i++) {
                str = "<ul class='context_li'><div class='context_liuyan clear' id='" + data[i].id + "_'><div class='liuyan_img'><img src='" + data[i].icon + "' /></div><div class='liuyan_con'><div class='liuyan_name'>" + data[i].nickname + "</div><div class='liuyan_con'>" + data[i].content + "</div> <div class='liuyan_time'><span>" + data[i].createTime + "</span><a role='button' class='btn_mess_hf'  id='" + data[i].id + "'>回复</a></div></div></div><div class='context_txt clear'><textarea class='context_textarea'></textarea><a role='button' class='btn_mess_tj'>提交</a><a role='button' class='btn_mess_sq'>收起</a><label class='hf_ts'></label></div></ul>";
                $(".context_list").append(str);
                str = ""
                if (data[i].child != null) {
                    var sto = "";
                    for (var j = 0; j < data[i].child.length; j++) {
                        sto += "<div class='context_reply clear' id='" + data[i].child[j].id + "'><div class='reply_img'><img src='" + data[i].child[j].icon + "' /></div><div class='reply_con'><div class='reply_name'>" + data[i].child[j].nickname + "</div><div class='reply_con'>" + data[i].child[j].content + "</div><div class='reply_time'><span>" + data[i].child[j].createTime + "</span><a role='button' id='" + data[i].id + "'  class='btn_mess_hf'>回复</a></div></div></div>"
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
                                var href = window.location.href;
                                var last = href.lastIndexOf("=");
                                var id = href.substring(last + 1, href.length)
                                location.href = "/content/" + id;
                                return false;
                            } else {
                                $.ajax({
                                    type: 'POST',
                                    url: '/api/comment',
                                    dataType: "json",
                                    data: {
                                        'belongId': $(".context_id").val(),
                                        'parentId': a,
                                        'content': tare
                                    },
                                    success: function (result) {
                                        if (result.code == 200) {
                                            layer.msg('回复成功！', {icon: 1, offset: '36%'});
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
