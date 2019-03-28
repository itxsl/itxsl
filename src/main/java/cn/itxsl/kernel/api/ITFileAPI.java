package cn.itxsl.kernel.api;

import cn.itxsl.kernel.model.ITXSL;
import cn.itxsl.kernel.model.dto.EditorDTO;
import cn.itxsl.kernel.model.dto.WEditorDTO;
import cn.itxsl.kernel.model.mapped.ITFileLog;
import cn.itxsl.kernel.model.oss.OSS;
import cn.itxsl.kernel.utils.OSSUtils;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 21:32
 * @description：文件上传
 */
@RestController
public class ITFileAPI {

    @Autowired
    OSS oss;

    @Autowired
    Dao dao;

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/api/{timestamp}/upload/file")//针对editor.md编辑器写的上传接口
    public EditorDTO updateFile(@RequestParam(name = "editormd-image-file") MultipartFile multipartFile,
                                @PathVariable(name = "timestamp") Long timestamp) {
        switch (oss.getEnable()) {
            case ITXSL.ALIYUN_CLOUD:
                try {
                    String fileName = OSSUtils.aliYunUploadFile(multipartFile);
                    dao.insert(new ITFileLog(timestamp, fileName));
                    return new EditorDTO(1, "上传成功!", oss.getEndpoint() + "/" + fileName);
                } catch (IOException e) {
                    return new EditorDTO(0, e.getMessage(), null);
                }
            case ITXSL.QQ_CLOUD:
                try {
                    String fileName = OSSUtils.qCloudUploadFile(multipartFile);
                    dao.insert(new ITFileLog(timestamp, fileName));
                    return new EditorDTO(1, "上传成功!", oss.getEndpoint() + "/" + fileName);
                } catch (Exception e) {
                    return new EditorDTO(0, e.getMessage(), null);
                }
            default:
                return new EditorDTO(0, "上传失败！", null);
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/api/{timestamp}/file")//针对wangEditor编辑器写的上传接口
    public WEditorDTO file(@RequestParam(name = "file") MultipartFile multipartFile, @PathVariable(name = "timestamp") Long timestamp) {
        switch (oss.getEnable()) {
            case ITXSL.ALIYUN_CLOUD:
                try {
                    String fileName = OSSUtils.aliYunUploadFile(multipartFile);
                    dao.insert(new ITFileLog(timestamp, fileName));
                    return new WEditorDTO(0, new String[]{oss.getEndpoint() + "/" + fileName});
                } catch (IOException e) {
                    return new WEditorDTO(0, new String[]{e.getMessage()});
                }
            case ITXSL.QQ_CLOUD:
                try {
                    String fileName = OSSUtils.qCloudUploadFile(multipartFile);
                    dao.insert(new ITFileLog(timestamp, fileName));
                    return new WEditorDTO(0, new String[]{oss.getEndpoint() + "/" + fileName});
                } catch (Exception e) {
                    return new WEditorDTO(0, new String[]{e.getMessage()});
                }
            default:
                return new WEditorDTO(1, new String[]{});
        }
    }

}
