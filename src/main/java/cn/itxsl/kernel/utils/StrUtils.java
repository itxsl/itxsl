package cn.itxsl.kernel.utils;

import cn.itxsl.kernel.model.email.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 13:13
 * @description：字符串工具类
 */
public class StrUtils {

    public static String getPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public static Boolean NotEmpty(String obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        return true;

    }

    public static Boolean NotEmpty(Integer obj) {
        if (obj == null) {
            return false;
        }
        return true;

    }

    public static Boolean NotEmpty(Long obj) {
        if (obj == null) {
            return false;
        }
        return true;

    }

    public static String getIcon(String url) {
        if (NotEmpty(url)&&url.length()>5){
            if (!url.substring(0,5).equals("https")){
                return  "https:" + url.substring(5, url.length());
            }
        }
        return url;
    }

    /**
     * 截取图片名称及所在文件路径
     *
     * @param str
     * @return
     */
    public static String subStrUrl(String str) {
        return str.substring(str.lastIndexOf("images/"), str.length());
    }

    public static void main(String[] args) {
        System.out.println(getPassword("qweasd1234"));
    }

    public static String emailContent(String title, String nickname, String content, String article, Long articleId) {
        String template =
                "    <div class='qmbox qm_con_body_content qqmail_webmail_only' id='mailContentContainer' style=''>\n" +
                        "        <style type='text/css'>\n" +
                        "            .qmbox body {\n" +
                        "                margin: 0;\n" +
                        "                padding: 0;\n" +
                        "                background: #fff;\n" +
                        "                font-family: 'Verdana, Arial, Helvetica, sans-serif';\n" +
                        "                font-size: 14px;\n" +
                        "                line-height: 24px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                        "                margin: 0;\n" +
                        "                padding: 0;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox img {\n" +
                        "                border: none;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .contaner {\n" +
                        "                margin: 0 auto;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .title {\n" +
                        "                margin: 0 auto;\n" +
                        "                background: url() #CCC repeat-x;\n" +
                        "                height: 30px;\n" +
                        "                text-align: center;\n" +
                        "                font-weight: bold;\n" +
                        "                padding-top: 12px;\n" +
                        "                font-size: 16px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .content {\n" +
                        "                margin: 4px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .biaoti {\n" +
                        "                padding: 6px;\n" +
                        "                color: #000;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xtop, .qmbox .xbottom {\n" +
                        "                display: block;\n" +
                        "                font-size: 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                        "                display: block;\n" +
                        "                overflow: hidden;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                        "                height: 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                        "                border-left: 1px solid #BCBCBC;\n" +
                        "                border-right: 1px solid #BCBCBC;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1 {\n" +
                        "                margin: 0 5px;\n" +
                        "                background: #BCBCBC;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb2 {\n" +
                        "                margin: 0 3px;\n" +
                        "                border-width: 0 2px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb3 {\n" +
                        "                margin: 0 2px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb4 {\n" +
                        "                height: 2px;\n" +
                        "                margin: 0 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xboxcontent {\n" +
                        "                display: block;\n" +
                        "                border: 0 solid #BCBCBC;\n" +
                        "                border-width: 0 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .line {\n" +
                        "                margin-top: 6px;\n" +
                        "                border-top: 1px dashed #B9B9B9;\n" +
                        "                padding: 4px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .neirong {\n" +
                        "                padding: 6px;\n" +
                        "                color: #666666;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .foot {\n" +
                        "                padding: 6px;\n" +
                        "                color: #777;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_darkblue {\n" +
                        "                color: #006699;\n" +
                        "                font-weight: bold;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_lightblue {\n" +
                        "                color: #008BD1;\n" +
                        "                font-weight: bold;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_gray {\n" +
                        "                color: #888;\n" +
                        "                font-size: 12px;\n" +
                        "            }\n" +
                        "        </style>\n" +
                        "        <div class='contaner'>\n" +
                        "            <div class='title'>" + title + "</div>\n" +
                        "            <div class='content'>\n" +
                        "                <p class='biaoti'><b>亲爱的" + nickname + "，你好！</b></p>\n" +
                        "                <b class='xtop'><b class='xb1'></b><b class='xb2'></b><b class='xb3'></b><b class='xb4'></b></b>\n" +
                        "                <div class='xboxcontent'>\n" +
                        "                    <div class='neirong'>\n" +
                        "                        <p><b>”" + RunUtils.getCurrentUser().getNickname() + "“，回复了你的留言：</b>\n" +
                        "                        <p><b>回复内容:<span>" + content + "</span></p>\n" +
                        "                        <div class='line'>如有打扰敬请谅解！！！</div>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "                <b class='xbottom'><b class='xb4'></b><b class='xb3'></b><b class='xb2'></b><b class='xb1'></b></b>\n" +
                        "                <p class='foot'>留言回复相关文章：<a href='" + SpringContextUtils.getBean(Email.class).getRedirectUrl() + "/content?id=" + articleId + "'>" + article + "</a></p>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <style type='text/css'>\n" +
                        "            .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                        "                display: none !important;\n" +
                        "            }\n" +
                        "        </style>\n" +
                        "    </div>\n";

        return template;
    }
    public static String emailContent(String title, String nickname, String content) {
        String template =
                "    <div class='qmbox qm_con_body_content qqmail_webmail_only' id='mailContentContainer' style=''>\n" +
                        "        <style type='text/css'>\n" +
                        "            .qmbox body {\n" +
                        "                margin: 0;\n" +
                        "                padding: 0;\n" +
                        "                background: #fff;\n" +
                        "                font-family: 'Verdana, Arial, Helvetica, sans-serif';\n" +
                        "                font-size: 14px;\n" +
                        "                line-height: 24px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                        "                margin: 0;\n" +
                        "                padding: 0;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox img {\n" +
                        "                border: none;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .contaner {\n" +
                        "                margin: 0 auto;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .title {\n" +
                        "                margin: 0 auto;\n" +
                        "                background: url() #CCC repeat-x;\n" +
                        "                height: 30px;\n" +
                        "                text-align: center;\n" +
                        "                font-weight: bold;\n" +
                        "                padding-top: 12px;\n" +
                        "                font-size: 16px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .content {\n" +
                        "                margin: 4px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .biaoti {\n" +
                        "                padding: 6px;\n" +
                        "                color: #000;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xtop, .qmbox .xbottom {\n" +
                        "                display: block;\n" +
                        "                font-size: 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                        "                display: block;\n" +
                        "                overflow: hidden;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                        "                height: 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                        "                border-left: 1px solid #BCBCBC;\n" +
                        "                border-right: 1px solid #BCBCBC;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb1 {\n" +
                        "                margin: 0 5px;\n" +
                        "                background: #BCBCBC;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb2 {\n" +
                        "                margin: 0 3px;\n" +
                        "                border-width: 0 2px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb3 {\n" +
                        "                margin: 0 2px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xb4 {\n" +
                        "                height: 2px;\n" +
                        "                margin: 0 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .xboxcontent {\n" +
                        "                display: block;\n" +
                        "                border: 0 solid #BCBCBC;\n" +
                        "                border-width: 0 1px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .line {\n" +
                        "                margin-top: 6px;\n" +
                        "                border-top: 1px dashed #B9B9B9;\n" +
                        "                padding: 4px;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .neirong {\n" +
                        "                padding: 6px;\n" +
                        "                color: #666666;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .foot {\n" +
                        "                padding: 6px;\n" +
                        "                color: #777;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_darkblue {\n" +
                        "                color: #006699;\n" +
                        "                font-weight: bold;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_lightblue {\n" +
                        "                color: #008BD1;\n" +
                        "                font-weight: bold;\n" +
                        "            }\n" +
                        "\n" +
                        "            .qmbox .font_gray {\n" +
                        "                color: #888;\n" +
                        "                font-size: 12px;\n" +
                        "            }\n" +
                        "        </style>\n" +
                        "        <div class='contaner'>\n" +
                        "            <div class='title'>" + title + "</div>\n" +
                        "            <div class='content'>\n" +
                        "                <p class='biaoti'><b>亲爱的" + nickname + "，你好！</b></p>\n" +
                        "                <b class='xtop'><b class='xb1'></b><b class='xb2'></b><b class='xb3'></b><b class='xb4'></b></b>\n" +
                        "                <div class='xboxcontent'>\n" +
                        "                    <div class='neirong'>\n" +
                        "                        <p><b>”" + RunUtils.getCurrentUser().getNickname() + "“，回复了你的留言：</b>\n" +
                        "                        <p><b>回复内容:<span>" + content + "</span></p>\n" +
                        "                        <div class='line'>如有打扰敬请谅解！！！</div>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "                <b class='xbottom'><b class='xb4'></b><b class='xb3'></b><b class='xb2'></b><b class='xb1'></b></b>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <style type='text/css'>\n" +
                        "            .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                        "                display: none !important;\n" +
                        "            }\n" +
                        "        </style>\n" +
                        "    </div>\n";

        return template;
    }

}
