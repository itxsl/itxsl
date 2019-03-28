package cn.itxsl.kernel.api;

import cn.itxsl.kernel.action.Views;
import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.dto.Result;
import cn.itxsl.kernel.model.mapped.ITAccessMonitoring;
import cn.itxsl.kernel.model.mapped.ITCommon;
import cn.itxsl.kernel.model.oss.OSS;
import cn.itxsl.kernel.repository.CommonRepository;
import cn.itxsl.kernel.repository.FileLogRepository;
import cn.itxsl.kernel.utils.ResultUtils;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Date;

import static cn.itxsl.kernel.utils.CndUtils.cnd;
import static cn.itxsl.kernel.utils.OSSUtils.delFile;
import static cn.itxsl.kernel.utils.OSSUtils.updateFileLog;
import static cn.itxsl.kernel.utils.StrUtils.subStrUrl;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/15 21:16
 * @description：网站共用信息管理
 */
@RestController
@RequestMapping("/api/{timestamp}/common")
public class ITCommonAPI {

    @Autowired
    private CommonRepository repository;

    @Autowired
    private FileLogRepository fileLog;

    @Autowired
    OSS oss;

    @Autowired
    Dao dao;

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result put(@Valid ITCommon common, @PathVariable(name = "timestamp") Long timestamp) {
        Views.common = null;
        ITCommon existCommon = repository.get(cnd());
        common.setVisitorVolume(Long.valueOf(dao.count(ITAccessMonitoring.class)));//网站访问量
        if (existCommon == null) {//如果未设置
            common.setUpdateDate(new Date());//网站最后更新时间
            ITCommon post = repository.post(common);//设置网站基本信息
            updateFileLog(timestamp, post.getId(), ITXSL.COMMON);//为网站上传图片做记录，方便清理
            return ResultUtils.post(post);
        }
        if (!existCommon.getLogo().equals(common.getLogo())) {//判断网站logo是否更改
            delFile(subStrUrl(existCommon.getLogo()));//删除存储服务器中对应图片
        }
        if (!existCommon.getIndexBackground().equals(common.getIndexBackground())) {//判断首页背景是否更换
            delFile(subStrUrl(existCommon.getIndexBackground()));//删除图片记录和图片
        }
        common.setUpdateDate(new Date());
        common.setId(existCommon.getId());
        ITCommon put = repository.put(common);
        updateFileLog(timestamp, put.getId(), ITXSL.COMMON);
        return ResultUtils.put(put);
    }

    @GetMapping
    @RolesAllowed("ROLE_ADMIN")
    public Result get() {
        return ResultUtils.get(repository.get(cnd()));
    }


}
