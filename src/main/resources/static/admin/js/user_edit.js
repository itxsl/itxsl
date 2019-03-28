var result_data = "";
var id = $("#user_id").val();
var timestamp = new Date().getTime();
layui.use(['form', 'layedit', 'laydate'], function () {

    var form = layui.form,
        layer = layui.layer,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;

    //日期
    laydate.render({
        elem: '#date'
    });
    laydate.render({
        elem: '#date1'
    });


    //监听指定开关
    form.on('switch(switchTest)', function (data) {
        layer.msg('开关checked：' + (this.checked ? 'true' : 'false'), {
            offset: '6px'
        });
        layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
    });

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#icon'
        , url: '/api/' + timestamp + '/file'
        , done: function (res) {
            //如果上传失败
            if (res.errno > 0) {
                return layer.msg('上传失败');
            } else {
                console.log("================")
                $('#icon').attr('src', res.data[0]); //图片链接（base64）
            }
            //上传成功
        }

    });


    $.get("/api/role", function (result) {
        var data = result.data;
        var o = "";
        for (index in data) {
            o += "<option value='" + data[index].id + "'>" + data[index].explain + "</option>"
        }
        console.log(o)
        $("#role").append(o)
        $.get("/api/user/" + id, function (result) {
            result_data = result.data;
            roleId = result_data.authorities[0].id;
            console.log(result_data.authorities[0].createTime)
            var options = $("#role")[0].options;
            console.log(options)
            for (index in options) {
                if (options[index].value == roleId) {
                    $("#role")[0].options[index].selected = true
                }
            }

            //表单初始赋值
            form.val('example', {
                "username": result_data.username,
                "nickname": result_data.nickname,
                "type": result_data.type,
                "isAccountNonLocked": result_data.isAccountNonLocked,
                "sex": result_data.sex,
                "email": result_data.email,
                "phone": result_data.phone,
                "createTime": result_data.createTime,
                "icon": function () {
                    $("#icon").attr("src", result_data.icon)
                }

            })

        })

    })


    //监听提交
    form.on('submit(demo1)', function (data) {
        $.ajax({
            url: '/api/user?timestamp='+timestamp+"&roleId="+data.field.role,
            type: 'put',
            contentType: "application/json",
            data: JSON.stringify({
                "nickname": data.field.nickname,
                "type": data.field.type,
                "isAccountNonLocked": data.field.isAccountNonLocked,
                "sex": data.field.sex,
                "email": data.field.email,
                "icon": $("#icon").attr("src"),
                "id": result_data.id
            }),
            success: function (result) {
                if (result.code == 200) {
                    layer.msg("更新用户成功！")
                } else {
                    layer.msg(result.message)
                }
            },
            error: function () {
                layer.msg("更新用户失败，请联系管理员！")
            }
        })

        return false;
    });


});
$(function () {
    $(".site-tree-mobile").click(function () {
        $("body").addClass("site-mobile");
        $(".site-mobile-shade").click(function () {
            $("body").removeClass("site-mobile");
        });
    });
})