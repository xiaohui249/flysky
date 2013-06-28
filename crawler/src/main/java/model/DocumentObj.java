package model;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-27
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */
public class DocumentObj {
    private long id;
    private String title;
    private String desc;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return this.title + " : " + this.url;
    }
}
