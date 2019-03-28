package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITArticle;
import cn.itxsl.kernel.model.mapped.ITFileLog;
import cn.itxsl.kernel.repository.ArticleRepository;
import cn.itxsl.kernel.repository.FileLogRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static cn.itxsl.kernel.utils.OSSUtils.delFile;
import static cn.itxsl.kernel.utils.OSSUtils.updateFileLog;
import static cn.itxsl.kernel.utils.RunUtils.getCurrentUser;
import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;


/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 12:54
 * @description：日记本api
 */
@RestController
@RequestMapping("/api/article")
public class ITArticleAPI {


    @Autowired
    ArticleRepository repository;

    @Autowired
    FileLogRepository fileLog;

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result post(@Valid ITArticle article, @RequestParam Long timestamp) {
        article.setNickname(getCurrentUser().getNickname());
        article.setIcon(getCurrentUser().getIcon());
        article.setClick(0L);
        ITArticle post = repository.post(article);
        updateFileLog(timestamp, post.getId(), ITXSL.ARTICLE);
        return ResultUtils.post(post);
    }


    @PutMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result put(@RequestBody ITArticle article, @RequestParam Long timestamp) {
        ITArticle put = repository.put(article);
        updateFileLog(timestamp, put.getId(), ITXSL.ARTICLE);
        return ResultUtils.put(put);
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable(name = "id") Long id) {
        return ResultUtils.get(repository.get(id));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Result del(@PathVariable(name = "id") Long id) {
        if (repository.del(id) > 0) {
            List<ITFileLog> fileLogs = fileLog.gets(Cnd.where("belong_id", "=", id).and("belong_type", "=", 1));
            fileLogs.forEach(file -> {
                delFile(file.getUrl());
            });
            return ResultUtils.del(1);
        }
        return ResultUtils.del(0);
    }

    @GetMapping()
    public Result gets(String title, Integer type, Integer available, String startTime, String endTime, Pager pager) {
        Cnd cnd = Cnd.where("1", "=", 1);
        cnd.and("available", "=", 1);
        cnd.desc("create_time");
        Cnd(title, type, cnd);
        return ResultUtils.gets(repository.gets(cnd, pager), repository.count(cnd));
    }

    @GetMapping("/admin")
    public Result getsAdmin(String title, Integer type, Integer available, String startTime, String endTime, Pager pager) {
        Cnd cnd = Cnd.where("1", "=", 1);
        Cnd(title, type, cnd);
        if (NotEmpty(available)) {
            cnd.and("available", "=", available);
        }
        cnd.desc("create_time");
        return ResultUtils.gets(repository.gets(cnd, pager), repository.count(cnd));
    }

    private void Cnd(String title, Integer type, Cnd cnd) {
        if (NotEmpty(title)) {
            cnd.and("title", "like", "%" + title + "%");
        }
        if (NotEmpty(type)) {
            cnd.and("type", "=", type);
        }
    }

}
