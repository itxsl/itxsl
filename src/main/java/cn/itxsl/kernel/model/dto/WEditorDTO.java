package cn.itxsl.kernel.model.dto;

/**
 * 描述:
 * 王editor
 *
 * @author itxsl
 * @create 2019-01-15 8:53
 */
public class WEditorDTO {

    private Integer errno;

    private String[]  data;

    public WEditorDTO() {
    }

    public WEditorDTO(Integer errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}