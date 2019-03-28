package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITLink;
import cn.itxsl.kernel.repository.LinkRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import cn.itxsl.kernel.utils.RunUtils;
import cn.itxsl.kernel.utils.StrUtils;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 15:39
 * @description：友情链接管理
 */
@RestController
@RequestMapping("/api/link")
public class ITLinkAPI {

    @Autowired
    LinkRepository repository;

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    public Result post(@Valid ITLink link){
        link.setAvailable(1);
        link.setIcon2(RunUtils.getCurrentUser().getIcon());
        link.setNickname(RunUtils.getCurrentUser().getNickname());
        if (!NotEmpty(RunUtils.getCurrentUser().getEmail())){
            if (!NotEmpty(link.getEmail())){
                return new Result(400,"小伙子很有前途哦！");
            }
        }
        return ResultUtils.post(repository.post(link));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable(name = "id") Long id){
        return ResultUtils.get(repository.get(id));
    }

    @PutMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result put(ITLink link){
        return ResultUtils.put(repository.put(link));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Result del(@PathVariable(name = "id") Long id){
        return ResultUtils.del(repository.del(id));
    }

    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMIN")
    public Result getsAll(){
        Cnd cnd = Cnd.where("1", "=", 1);
        cnd.desc("create_time");
        return ResultUtils.gets(repository.gets(cnd),repository.count(null));
    }

    @GetMapping
    public Result gets(){
        Cnd cnd = Cnd.where("1", "=", 1);
        cnd.and("available","=",1);
        cnd.desc("create_time");
        return ResultUtils.gets(repository.gets(cnd),repository.count(cnd));
    }


}
