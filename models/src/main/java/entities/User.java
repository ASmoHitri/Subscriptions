package entities;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@NamedQueries(value =
        {
            @NamedQuery(name = "Users.getByUsernameOrMail",
                        query = "SELECT u FROM users u WHERE u.username=:username OR u.mail=:mail"),
            @NamedQuery(name = "Users.updateUser", query = "UPDATE users u SET u.username=:username, u.mail=:mail")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String mail;

    @Column(nullable = false)
    private String password;

    @Column(name= "password_salt", nullable = false)
    private String passwordSalt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "artist_id")
    private int artistId;

//    @Transient
//    private List<Playlist> playlists;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
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

//    public List<Playlist> getPlaylists() {
//        return playlists;
//    }
}
