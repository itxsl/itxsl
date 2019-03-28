package cn.itxsl.kernel.model.dto;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 21:35
 * @description：文件上传
 */
public class EditorDTO {

    private Integer success;

    private String message;

    private String url;

    public EditorDTO(Integer success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
