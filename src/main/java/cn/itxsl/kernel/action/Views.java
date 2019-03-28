package cn.itxsl.kernel.action;

import cn.itxsl.kernel.model.dto.ArticleDTO;
import cn.itxsl.kernel.model.mapped.ITAccessMonitoring;
import cn.itxsl.kernel.model.mapped.ITArticle;
import cn.itxsl.kernel.model.mapped.ITCommon;
import cn.itxsl.kernel.model.oauth.OAuth2;
import cn.itxsl.kernel.model.oauth.QQ;
import cn.itxsl.kernel.repository.ArticleRepository;
import cn.itxsl.kernel.repository.CommonRepository;
import cn.itxsl.kernel.repository.LinkRepository;
import cn.itxsl.kernel.repository.UserRepository;
import cn.itxsl.kernel.utils.RunUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.itxsl.kernel.utils.CndUtils.cnd;
import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

@Controller
@SuppressWarnings("all")
public class Views {

    @Value("${site-url}")
    private String siteUrl;


    @Autowired
    UserRepository userRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommonRepository commonRepository;


    public static ITCommon common = null;


    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("sites", getCommon());
        Cnd cnd = Cnd.where("available", "=", 1);
        cnd.desc("create_time");
        model.addAttribute("links", linkRepository.gets(cnd, new Pager(1, 6)));
        List<ITArticle> articles = articleRepository.gets(cnd, new Pager(1, 3));
        ArrayList<ArticleDTO> articleDTOS = new ArrayList<>();
        for (ITArticle article : articles) {
            articleDTOS.add(new ArticleDTO("content?id=" + article.getId(), article.getTitle(), article.getCover()));
        }
        model.addAttribute("articles", articleDTOS);
        return "index.html";
    }

    @GetMapping("/article")
    public String article(Model model) {
        model.addAttribute("sites", getCommon());
        model.addAttribute("title", "日记本");
        return "article.html";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("sites", getCommon());
        model.addAttribute("title", "登录");
        return "login.html";
    }


    @GetMapping("/content")
    public String content(Model model, Long id) {
        if (!NotEmpty(id)) {
            return "redirect:/article";
        }
        model.addAttribute("sites", getCommon());
        ITArticle article = articleRepository.get(id);
        article.setClick(article.getClick() + 1);
        articleRepository.put(article);
        Integer contentType = article.getContentType();
        if (contentType == null || contentType != 1) {
            article.setContent("<textarea   style=\"display:none;\" >" + article.getContent() + "</textarea>");
        }
        model.addAttribute("article", article);
        return "content.html";
    }

    @GetMapping("/content/{id}")
    public String contentId(Model model, @PathVariable Long id) {
        model.addAttribute("sites", getCommon());
        ITArticle article = articleRepository.get(id);
        Integer contentType = article.getContentType();
        if (contentType == null || contentType != 1) {
            article.setContent("<textarea   style=\"display:none;\" >" + article.getContent() + "</textarea>");
        }
        model.addAttribute("article", article);
        return "content.html";
    }


    @GetMapping("/message")
    public String message(Model model) {
        model.addAttribute("sites", getCommon());
        return "message.html";
    }


    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("sites", getCommon());
        return "link.html";
    }

    @GetMapping("/link/add")
    public String linkAdd(Model model) {
        ITCommon common = getCommon();
        String indexContent = common.getIndexTitleContent().replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("\n", "，") + "。";
        model.addAttribute("sites", common);
        model.addAttribute("siteUrl", siteUrl);
        model.addAttribute("indexContent", indexContent);
        return "link_add.html";
    }


    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("sites", getCommon());
        return "about.html";
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping({"/admin/index", "/admin", "/admin/"})
    public String adminIndex(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/index.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/article")
    public String adminArticle(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/article.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/article/add")
    public String adminArticlePost(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/article_add.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/article/edit")
    public String adminArticlePut(Model model, Long id) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        model.addAttribute("id", id);
        return "admin/article_edit.html";
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/user")
    public String adminUserGet(Model model, Long id) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/user.html";
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/user/edit")
    public String adminUserPut(Model model, Long id) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        model.addAttribute("id", id);
        return "admin/user_edit.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/music")
    public String music(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/music.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/message")
    public String adminMessage(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/message.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/link")
    public String adminLink(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/link.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/comment")
    public String adminComment(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/comment.html";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/music/selected")
    public String musicSelected(Model model) {
        model.addAttribute("user", RunUtils.getCurrentUser());
        model.addAttribute("sites", getCommon());
        return "admin/music_selected.html";
    }

    @GetMapping("/love")
    public String love(Model model){
        model.addAttribute("sites", getCommon());
        model.addAttribute("time", new Date());
        return "love.html";
    }

    @ResponseBody
    @GetMapping("/is/login")
    public String isLogin() {
        if (RunUtils.isLogin()) {
            return RunUtils.getCurrentUser().getIcon();
        }
        return "no";
    }


    @ResponseBody
    @GetMapping("/is/email")
    public Boolean isEmail() {
        if (!NotEmpty(RunUtils.getCurrentUser().getEmail())) {
            String email = userRepository.get(RunUtils.getCurrentUser().getId()).getEmail();
            if (!NotEmpty(email)) {
                return false;
            }
            RunUtils.getCurrentUser().setEmail(email);
        }
        return true;
    }

    @Autowired
    OAuth2 auth;

    //QQ登陆对外接口，只需将该接口放置html的a标签href中即可
    @GetMapping("/login/qq")
    public void loginQQ(HttpServletResponse response) {
        QQ qq = auth.getQq();
        try {
            response.sendRedirect(qq.getCode_uri() + //获取code码地址
                    "?client_id=" + qq.getClient_id()//appid
                    + "&state=" + UUID.randomUUID() + //这个说是防攻击的，就给个随机uuid吧
                    "&redirect_uri=" + qq.getRedirect_uri() +//这个很重要，这个是回调地址，即就收腾讯返回的code码
                    "&response_type=code");//授权模式，授权码模式
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI
    @GetMapping("/login/weibo")
    public void loginWeibo(HttpServletResponse response) {
        try {
            response.sendRedirect("https://api.weibo.com/oauth2/authorize?client_id=" + auth.getWeibo().getClientId()
                    + "&response_type=code&redirect_uri=" + auth.getWeibo().getRedirectUri()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Autowired
    Dao dao;

    @Autowired
    HttpServletRequest request;


    private ITCommon getCommon() {
        if (common == null) {
            common = commonRepository.get(cnd());
            if (common == null) {
                common = new ITCommon();
                common.setLogo("/static/img/emoticon/beanbag/01.gif");
                common.setLogoName("秋意凉。");
                common.setRecordInfo("");
                common.setVisitorVolume(Long.valueOf(dao.count(ITAccessMonitoring.class)));
                common.setUpdateDate(new Date(1546272000));
            }
        }
        String remoteHost = getRemortIP(request);
        if (!"0:0:0:0:0:0:0:1".equals(remoteHost) && dao.fetch(ITAccessMonitoring.class, Cnd.where("host", "=", remoteHost)) == null) {
            dao.insert(new ITAccessMonitoring(remoteHost, new Date()));
            common.setVisitorVolume(Long.valueOf(dao.count(ITAccessMonitoring.class)));
        }
        return common;
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

}

