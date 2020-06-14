package Entiny;
import org.json.JSONObject;
import java.io.Serializable;
public class PostEntiny implements Serializable {

    private  int id;
    private String title;
    private  String source;
    private String thumb;
    private int category;
    private String date;
    private String content;
    private String link;

    public PostEntiny(int id, String title, String source, String thumb, int category, String date, String content) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.thumb = thumb;
        this.category = category;
        this.date = date;
        this.content = content;
    }


    public PostEntiny(int id, String title, String source, String thumb, int category, String date) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.thumb = thumb;
        this.category = category;
        this.date = date;

    }

    @Override
    public String toString() {
        return "PostEntiny{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", thumb='" + thumb + '\'' +
                ", category=" + category +
                ", date='" + date + '\'' +

                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostEntiny() {
    }

    public PostEntiny(JSONObject jsonObject) {
        id = jsonObject.optInt("post_id",0);
        title = jsonObject.optString("post_title","");
        source = jsonObject.optString("post_source","");
        thumb = jsonObject.optString("post_thumb","");
        category = jsonObject.optInt("category_id",0);
        date = jsonObject.optString("date_time","");
        content = jsonObject.optString("post_content","");
        link = jsonObject.optString("post_link","");






    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
