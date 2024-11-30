package com.project.project.Service;

import com.project.project.Entity.User;
import com.project.project.Repository.UserRepository;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Component
@Path("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserService {

    @Autowired
    private UserRepository ur;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @POST
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response submit(User user) {
        ur.save(user);
        return Response.ok("Data added successfully!").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getUsers() {
        List<User> users = ur.findAll();
        return Response.ok(users).build();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(User user) {
        if (ur.findByEmail(user.getEmail()) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse("Email already exists", null))
                    .build();
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        ur.save(user);
        return Response.ok(new ApiResponse("User registered successfully", null)).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User u) {
        User user = ur.findByEmail(u.getEmail());
        if (user != null && passwordEncoder.matches(u.getPassword(), user.getPassword())) {
            return Response.ok(new ApiResponse("Login successful", user.getId())).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse("Invalid email or password", null))
                    .build();
        }
    }

    public static class ApiResponse {
        private String message;
        private Long userId;

        public ApiResponse(String message, Long userId) {
            this.message = message;
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
