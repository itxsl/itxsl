package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITMusic;
import cn.itxsl.kernel.repository.MusicRepository;
import cn.itxsl.kernel.utils.OSSUtils;
import cn.itxsl.kernel.utils.ResultUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import static cn.itxsl.kernel.utils.StrUtils.NotEmpty;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/19 20:44
 * @description：音乐管理
 */
@RestController
@RequestMapping("/api/music")
public class ITMusicAPI {

    @Autowired
    MusicRepository repository;

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public Result post(@Valid ITMusic music) {
        if (NotEmpty(music.getUrl())&&music.getUrl().length()>5){
            if (!music.getUrl().substring(0,5).equals("https")){
                return  new Result(201,"音乐必须是https资源");
            }
        }
        ITMusic post = repository.post(music);
        return ResultUtils.post(post);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Result del(@PathVariable(name = "id")Long id){
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


    @GetMapping("/{id}")
    public Result get(@PathVariable(name = "id")Long id){
        return ResultUtils.get(repository.get(id));
    }

    @GetMapping
    public Result gets(Pager pager){
        return ResultUtils.gets(repository.gets(null,pager),repository.count(null));
    }
}
