package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITRole;
import cn.itxsl.kernel.repository.RoleRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

@RestController
@RequestMapping("/api/role")
public class ITRoleAPI {


    @Autowired
    RoleRepository repository;

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result post(ITRole sysRole){
        return ResultUtils.get(repository.post(sysRole));
    }

    @PutMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result put(ITRole sysRole){
        return ResultUtils.put(repository.put(sysRole));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable(name = "id") Long id){
        return ResultUtils.get(repository.get(id));
    }

    @DeleteMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result del(Long id){
        return ResultUtils.del(repository.del(id));
    }

    @GetMapping()
    public Result gets(String role, Pager pager){
        Cnd cnd = Cnd.where("1","=",1);
        if (NotEmpty(role)){
            cnd.and("role","=",role);
        }
        return ResultUtils.gets(repository.gets(cnd,pager),repository.count(cnd));
    }

}
