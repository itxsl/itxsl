package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.dto.ITUserDTO;
import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITRole;
import cn.itxsl.kernel.model.mapped.ITUser;
import cn.itxsl.kernel.repository.RoleRepository;
import cn.itxsl.kernel.repository.UserRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import cn.itxsl.kernel.utils.StrUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

import static cn.itxsl.kernel.utils.OSSUtils.updateFileLog;
import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

@RestController
@RequestMapping("/api/user")
public class ITUserAPI {


    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;


    @PostMapping
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Result post(ITUser itUser, @RequestParam(name = "roleId") Long roleId, @RequestParam(name = "timestamp") Long timestamp) {
        ITRole itRole = roleRepository.get(roleId);
        if (itRole == null) {
            return new Result(400, "角色不存在！");
        }
        ArrayList<ITRole> roles = new ArrayList<>();
        roles.add(itRole);
        itUser = repository.post(itUser);
        itUser.setAuthorities(roles);
        itUser = repository.postRelation(itUser, "authorities");//设置关联关系
        updateFileLog(timestamp, itUser.getId(), ITXSL.USER);
        return ResultUtils.get(itUser);
    }

    @PutMapping
    @Transactional
    @RolesAllowed("ROLE_ADMIN")
    public Result put(@RequestBody ITUser itUser,Long roleId, Long timestamp) {

        System.out.println(itUser);
        ITUser existUser = repository.get(itUser.getId());
        itUser.setPassword(existUser.getPassword());
        if (NotEmpty(roleId)){
            ITRole itRole = roleRepository.get(roleId);
            if (itRole!=null){
                ArrayList<ITRole> roles = new ArrayList<>();
                itRole.setId(roleId);
                roles.add(itRole);
                itUser.setAuthorities(roles);
                repository.delRelation(existUser,"authorities");
                repository.postRelation(itUser,"authorities");
            }

        }
        updateFileLog(timestamp, itUser.getId(), ITXSL.USER);
        return ResultUtils.put(repository.put(itUser));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable(name = "id") Long id) {
        return ResultUtils.get(repository.getjoin(id, "authorities"));
    }

    @DeleteMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result del(Long id) {
        return ResultUtils.del(repository.del(id));
    }

    @GetMapping()
    public Result gets(String username, String nickname, Pager pager) {
        Cnd cnd = Cnd.where("1", "=", 1);
        if (NotEmpty(username)) {
            cnd.and("username", "=", username);
        }
        if (NotEmpty(nickname)) {
            cnd.and("nickname", "=", nickname);
        }
        ArrayList<ITUserDTO> arrayList = new ArrayList<>();
        repository.getsByJoin("authorities", cnd, pager).forEach(user -> {
            ITUserDTO userDTO = new ITUserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setNickname(user.getNickname());
            userDTO.setSex(user.getSex());
            userDTO.setLock(user.isAccountNonLocked());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setType(user.getType());
            userDTO.setIcon(user.getIcon());
            userDTO.setRole(getRole(user.getAuthorities()));
            userDTO.setType(user.getType());
            arrayList.add(userDTO);
        });
        return ResultUtils.gets(arrayList, repository.count(cnd));
    }

    private String getRole(List<ITRole> authorities) {
        if (authorities != null && authorities.size() > 0) {
            return authorities.get(0).getExplain();
        }
        return "尚未设置";
    }

}
