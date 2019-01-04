package beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import configurations.AppConfigs;
import dtos.Playlist;
import entities.User;
import dtos.ResponseUser;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsersBean {

    @Inject
    private AppConfigs appConfig;

    @Context
    protected UriInfo uriInfo;

    @PersistenceContext(unitName = "subscriptions-jpa")
    private EntityManager entityManager;

    private Client httpClient = ClientBuilder.newClient();

    @Inject
    @DiscoverService("microservice-catalogs")
    private Optional<String> basePath;

    public List<ResponseUser> getUsers() {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<User> users = JPAUtils.queryEntities(entityManager, User.class, queryParameters);
        List<ResponseUser> responseUsers = new ArrayList<>();
        for(User user: users) {
            responseUsers.add(new ResponseUser(user));
        }
        return responseUsers;
    }

    public ResponseUser getUser(int userId) {
        if (appConfig.getMaintenanceMode()) {
            System.out.println("Warning: maintenance mode enabled");
        }
        User user = entityManager.find(User.class, userId);
        if (user != null){
            return new ResponseUser(user);
        }
        return null;
    }

    public List<User> getByUsernameOrMail(String username, String mail) {
        Query query = entityManager.createNamedQuery("Users.getByUsernameOrMail");
        query.setParameter("username", username);
        query.setParameter("mail", mail);
        return query.getResultList();
    }

    public boolean addUser(User user) {
        if (user != null) {
            // generate password salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // hash password
            try {
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(user.getPassword().toCharArray(), salt, 10000, 256);
                String hashedPassword = new String(secretKeyFactory.generateSecret(spec).getEncoded(), "UTF-8");
                user.setPassword(hashedPassword);
                user.setPasswordSalt(new String(salt, "UTF-8"));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException e) {
                return false;
            }

            try{
                beginTx();
                entityManager.persist(user);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
                return false;
            }
            return true;
        }
        return false;
    }

    public ResponseUser updateUser(int userId, User user) {
        if (getUser(userId) == null || user == null) {
            return null;
        }
        try{
            beginTx();
            user.setId(userId);
            Query query = entityManager.createNamedQuery("Users.updateUser");
            query.setParameter("username", user.getUsername());
            query.setParameter("mail", user.getMail());
            query.executeUpdate();
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return null;
        }
        return new ResponseUser(user);
    }

    public boolean removeUser(int userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            try{
                beginTx();
                entityManager.remove(user);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
                return false;
            }
            return true;
        }
        return false;
    }

    public List<Playlist> getPlaylists(int userId) {
        if (basePath.isPresent()) {
            try {
                List<Playlist> playlists =  httpClient.target(basePath.get() + "/api/v1/playlists?where=userId:EQ:" + userId)
                        .request().get(new GenericType<List<Playlist>>(){});
                return playlists;
            } catch (WebApplicationException | ProcessingException exception) {
                System.out.println(exception.getMessage());
                return null;
            }
        }
        return null;
    }

    private void beginTx() {
        if (!entityManager.getTransaction().isActive())
            entityManager.getTransaction().begin();
    }

    private void commitTx() {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().commit();
    }

    private void rollbackTx() {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }
}