package Entiny;


public class UserEntiny  {
    private String name;
    private String comment;
    private long id;
    private String profileId;

    public UserEntiny() {
    }

    public UserEntiny(long id,String name, String comment,String profileId) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.profileId = profileId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
