package response_entities;

import entities.User;

public class ResponseUser {
    private int id;
    private String username;
    private String mail;
    private Boolean isActive;
    private int artistId;

    public ResponseUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.mail = user.getMail();
        this.isActive = user.getActive();
        this.artistId = user.getArtistId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }
}
