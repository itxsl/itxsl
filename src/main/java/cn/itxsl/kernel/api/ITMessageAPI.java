package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITEmail;
import cn.itxsl.kernel.model.mapped.ITMessage;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.repository.EmailRepository;
import cn.itxsl.kernel.repository.MessageRepository;
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

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/21 19:34
 * @description：留言管理
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/message")
public class ITMessageAPI {

    @Autowired
    MessageRepository repository;

    @Autowired
    UserRepository user;

    @Autowired
    EmailRepository email;


    @PostMapping
    public Result post(@Valid ITMessage message) {
        ITUser currentUser = RunUtils.getCurrentUser();
        message.setNickname(currentUser.getNickname());
        message.setIcon(currentUser.getIcon());
        message.setAvailable(1);
        if (!NotEmpty(currentUser.getEmail())){
            currentUser.setEmail(message.getEmail());
            user.put(currentUser);
        }
        if (!NotEmpty(message.getParentId())||message.getParentId()<1){
            message.setParentId(0L);
        }
        if (message.getParentId() != null && message.getParentId() > 0 && !repository.get(message.getParentId()).getCreateUsername().equals(RunUtils.getCurrentUser().getUsername())) {
            String email = user.get(Cnd.where("username", "=", repository.get(message.getParentId()).getCreateUsername())).getEmail();
            ITEmail itEmail = new ITEmail();
            itEmail.setEmail(email);
            itEmail.setSubject("留言新的回复");
            String nickname = repository.get(message.getParentId()).getNickname();
            itEmail.setContent(StrUtils.emailContent("留言新的回复",
                    nickname,
                    message.getMessage()
            ));
            this.email.sendEmail(itEmail);
        }
        return ResultUtils.post(repository.post(message));
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

    @GetMapping("/all")
    public Result getsAll(Pager pager) {
        return ResultUtils.gets(repository.gets(null, pager));
    }

    @GetMapping
    public Result gets(Pager pager) {
        Cnd cnd = Cnd.where("available", "=", 1);
        cnd.and("parent_id", "=", 0);
        cnd.desc("createTime");
        List<ITMessage> parentMessages = repository.gets(cnd, pager);
        List<Long> ids = new ArrayList<>();
        parentMessages.forEach(msg -> {
            ids.add(msg.getId());
        });
        Cnd where = Cnd.where("parent_id", "in", ids);
        where.and("available","=",1);
        where.asc("createTime");
        List<ITMessage> childMessages = repository.gets(where);
        List<ITMessage> messages = new ArrayList<>();
        parentMessages.forEach(message -> {
            ArrayList<ITMessage> child = new ArrayList<>();
            childMessages.forEach(childMessage -> {
                if (message.getId() == childMessage.getParentId()) {
                    child.add(childMessage);
                }
            });
            message.setChilds(child);
            messages.add(message);
        });
        return ResultUtils.gets(messages,repository.count(cnd));
    }

}
