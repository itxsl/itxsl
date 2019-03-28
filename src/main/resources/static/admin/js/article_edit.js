var markdownEditor;
var wangEditor;
var flag = 1
var result_data = "";
var id = $("#article_id").val();
/*初始化wangeditor*/
var E = window.wangEditor
wangEditor = new E('#div3')
wangEditor.customConfig.uploadFileName = 'file'
wangEditor.customConfig.uploadImgServer = '/api/file'
wangEditor.create()
var timestamp=new Date().getTime();

$.get("/api/article/" + id, function (result) {
    result_data = result.data;
    var editor = "html";
    if (result_data.contentType == 1) {
        $("#editor_md").hide()
        $("#html-editor").show()
        $("#content_").html(result_data.content)
    }
    else {
        editor = "markdown";
        $("#html-editor").hide()
        $("#editor_md").show()
    }
    form.val('example', {
        "title": result_data.title,
        "synopsis": result_data.synopsis,
        "music": result_data.music,
        "type": result_data.type,
        "available": result_data.available == 1 ?true : false,
        "cover": function () {
            $("#cover").attr('src', result_data.cover)
        },
        "original": result_data.original,
        "content": result_data.content,
        "editor": editor
    })
    $.get("/api/music/"+result_data.music,function (result_music) {
        var data = result_music.data;
        console.log(data.name+"-"+data.singer);
        $("#checkMusic").text(data.name+"-"+data.singer)
        $("#music_id").val(data.id);
    })

    /*初始化markdown*/
    markdownEditor = editormd({
        id: "editor_md",
        markdown: result_data.content,
        width: "90%",
        height: 840,
        path: "/static/admin/editormd/lib/",
        codeFold: true,
        searchReplace: true,
        saveHTMLToTextarea: true,      // 保存 HTML 到 Textarea
        htmlDecode: "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
        emoji: true,
        taskList: true,
        tocm: true,          // Using [TOCM]
        tex: true,                      // 开启科学公式 TeX 语言支持，默认关闭
        flowChart: true,                // 疑似 Sea.js与 Raphael.js 有冲突，必须先加载 Raphael.js ，Editor.md 才能在 Sea.js 下正常进行；
        sequenceDiagram: true,          // 同上
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "icon"],
        imageUploadURL: "/api/upload/file",
        onload: function () {
            this.on('paste', function () {
                console.log(1);
            });
        }
    });
    /**
     * 咱贴上传图片0
     */
    $("#editor_md").on('paste', function (ev) {
        var data = ev.clipboardData;
        var items = (event.clipboardData || event.originalEvent.clipboardData).items;
        for (var index in items) {
            var item = items[index];
            if (item.kind === 'file') {
                var blob = item.getAsFile();
                var reader = new FileReader();
                reader.onload = function (event) {
                    var form = new FormData();
                    form.append('editormd-image-file', blob);

                    $.ajax({
                        url: "/api/"+timestamp+"/upload/file",
                        type: "post",
                        dataType: "json",
                        data: form,
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            if (data.success == 1) {
                                markdownEditor.insertValue("\n![](" + data.url + ")");
                            } else {
                                alert("上传失败！");
                            }
                            $(".data_img").attr("src", data.data)
                        },
                        error:function (result) {
                            if(result.status == 403){
                                layer.msg("权限不足!");
                            }else {
                                layer.msg(result.error);
                            }
                        }
                    });
                };
                var url = reader.readAsDataURL(blob);
            }
        }
    });
});
//表单初始赋值

layui.use(['form', 'layedit', 'laydate'], function () {
    form = layui.form, layer = layui.layer, layedit = layui.layedit, upload = layui.upload;
    //自定义验证规则
    form.verify({
        title: function (value) {
            if (value.length < 5) {
                return '标题至少得5个字符啊';
            }
        }
    });

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#cover'
        , url: '/api/'+timestamp+'/file'
        , done: function (res) {
            //如果上传失败
            if (res.errno > 0) {
                return layer.msg('上传失败');
            } else {
                console.log("================")
                $('#cover').attr('src', res.data[0]); //图片链接（base64）
            }
            //上传成功
        }

    });

    //监听指定开关
    form.on('switch(switchTest)', function (data) {
        layer.msg('开关checked：' + (this.checked ? 'true' : 'false'), {
            offset: '6px'
        });
        layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
    });

//监听提交
    form.on('submit(demo1)', function (data) {
        var editor = data.field.editor;
        var ava = data.field.available;
        var content = "-1";
        var contentType = -1;
        var available = 0;
        if (editor == "html") {
            content = wangEditor.txt.html();
            contentType = 1;

        } else if (editor == "markdown") {
            content = markdownEditor.getMarkdown();
            contentType = 0;
        }
        if (ava == "on") {
            available = 1;
        }
        $.ajax({
            url: "/api/article?timestamp="+timestamp,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                'id': result_data.id,
                'title': data.field.title,
                'type': data.field.type,
                'cover': $("#cover").attr('src'),
                'synopsis': data.field.synopsis,
                'original': data.field.original,
                'available': available,
                'music': data.field.music,
                'content': content,
                'contentType': contentType
            }),
            dataType: "json",
            success: function (result) {
                if (result.code == 200) {
                    layer.msg(result.message)
                    setTimeout(function () {
                        location.href = "/admin/article"
                    }, 1200);
                } else {
                    layer.msg(result.message)
                }
            }
        });
    return false;
});



    /*选择使用那个编辑器*/
    form.on('radio(editor)', function (data) {
        if (data.value == "html") {
            $("#editor_md").hide()
            $("#html-editor").show()
        } else {
            $("#html-editor").hide()
            $("#editor_md").show()
        }
    });

    $("#checkMusic").click(function() {
        layer.open({
            type: 2,
            title: '我的音乐',
            area: ['600px', '400px'],
            shade: 0.3,
            maxmin: true,
            content: '/admin/music/selected',
            btn: ['确定', '关闭'], //只是为了演示
            yes: function(obj) {
                var id = "layui-layer-iframe" + flag;
                console.log(id)
                var doc;
                if(document.all) { // IE
                    doc = document.frames[id].document;
                } else { // 标准
                    doc = document.getElementById(id).contentDocument;
                }
                var musicId = doc.getElementById("musicId").textContent;
                if(musicId == undefined || musicId == "") {
                    doc.getElementById("musicId").click()
                } else {
                    var str = musicId.split(",")
                    $("#checkMusic").text(str[1])
                    $("#music_id").val(str[0])
                    layer.closeAll();
                    flag = flag + 1;

                }

            },
            btn2: function() {
                layer.closeAll();
            },
            zIndex: layer.zIndex //重点1
            ,
            success: function(layero) {
                layer.setTop(layero); //重点2
            }

        })

    })
});

/*侧边栏*/
$(function () {
    $(".site-tree-mobile").click(function () {
        $("body").addClass("site-mobile");
        $(".site-mobile-shade").click(function () {
            $("body").removeClass("site-mobile");
        });
    });
});

/*第一次加载页面隐藏html编辑器*/
$(function () {
    $("#html-editor").hide()
})
