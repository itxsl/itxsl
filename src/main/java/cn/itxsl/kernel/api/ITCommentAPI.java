package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITComment;
import cn.itxsl.kernel.model.mapped.ITEmail;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.repository.ArticleRepository;
import cn.itxsl.kernel.repository.CommentRepository;
import cn.itxsl.kernel.repository.EmailRepository;
import cn.itxsl.kernel.repository.UserRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import cn.itxsl.kernel.utils.RunUtils;
import cn.itxsl.kernel.utils.StrUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/20 12:41
 * @description：评论管理
 */
@RestController
@RequestMapping("/api/comment")
public class ITCommentAPI {

    @Autowired
    CommentRepository repository;

    @Autowired
    UserRepository user;

    @Autowired
    EmailRepository email;

    @Autowired
    ArticleRepository article;

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public Result post(@Valid ITComment comment) {
        ITUser itUser = user.get(RunUtils.getCurrentUser().getId());
        if (itUser.getEmail() == null) {
            itUser.setEmail(comment.getEmail());
            user.put(itUser);
        }
        comment.setAvailable(1);
        comment.setIcon(itUser.getIcon());
        comment.setNickname(itUser.getNickname());
        if (comment.getParentId() != null && comment.getParentId() > 0 && !repository.get(comment.getParentId()).getCreateUsername().equals(RunUtils.getCurrentUser().getUsername())) {
            String email = user.get(Cnd.where("username", "=", repository.get(comment.getParentId()).getCreateUsername())).getEmail();
            ITEmail itEmail = new ITEmail();
            itEmail.setEmail(email);
            itEmail.setSubject("评论新的回复");
            String nickname = repository.get(comment.getParentId()).getNickname();
            itEmail.setContent(StrUtils.emailContent("评论新的回复",
                    nickname,
                    comment.getContent(),
                    article.get(comment.getBelongId()).getTitle(),
                    comment.getBelongId()
            ));
            ITCommentAPI.this.email.sendEmail(itEmail);
        }
        return ResultUtils.post(repository.post(comment));
    }



    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Result del(@PathVariable(name = "id")Long id) {
        return ResultUtils.del(repository.del(id));
    }


    @DeleteMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result del(String idsStrs){
        String[] idsStr = idsStrs.split(",");
        Integer[] ids = new Integer[idsStr.length];
        for (int i =0 ; i<idsStr.length;i++){
            ids[i]=Integer.valueOf(idsStr[i]);
        }
        return ResultUtils.del(repository.del(Cnd.where("id","in",ids)));
    }

    @GetMapping
    public Result gets(@RequestParam(name = "belongId") Long belongId) {
        Cnd cnd = Cnd.where("belong_id", "=", belongId);
        cnd.and("parent_id","=",0);
        cnd.desc("create_time");
        List<ITComment> parentComment = repository.gets(cnd);
        List<ITComment> parent = new ArrayList<>();
        List<ITComment> comments = repository.gets(Cnd.where("belong_id", "=", belongId).and("parent_id","!=",0).and("available","=",1));
        for (ITComment comment : parentComment) {
                List<ITComment> childComment = new ArrayList<>();
                for (ITComment comment1 : comments) {
                    if (comment.getId() == comment1.getParentId()) {
                        childComment.add(comment1);
                    }
                }
                comment.setChild(childComment);
                parent.add(comment);
        }
        return ResultUtils.gets(parent, repository.count(Cnd.where("belong_id", "=", belongId).and("available","=",1)));
    }

    @GetMapping("/all")
    public Result getsAll(Pager pager){
        return ResultUtils.gets(repository.gets(null,pager));
    }
}
