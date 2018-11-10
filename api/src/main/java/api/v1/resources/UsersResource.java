package api.v1.resources;

import response_entities.ResponseUser;
import beans.UsersBean;
import entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    private UsersBean usersBean;

    @GET
    public Response getUsers() {
        List<ResponseUser> users = usersBean.getUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int userId) {
        ResponseUser user = usersBean.getUser(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @POST
    public Response addUser(User user) {
        if (user == null || user.getUsername() == null || user.getMail() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        // check for duplicates
        List<User> existingUsers = usersBean.getByUsernameOrMail(user.getUsername(), user.getMail());
        if (!existingUsers.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Boolean success = usersBean.addUser(user);
        if (!success) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") int userId, User user) {
        if (user == null || userId < 0 || user.getUsername() == null || user.getMail() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ResponseUser responseUser = usersBean.updateUser(userId, user);
        if (responseUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(responseUser).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int userId) {

        boolean success = usersBean.removeUser(userId);

        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
