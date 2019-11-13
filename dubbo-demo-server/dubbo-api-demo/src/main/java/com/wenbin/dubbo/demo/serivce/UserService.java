package com.wenbin.dubbo.demo.serivce;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/user")
public interface UserService {

    @GET
    @Path("/register/{id}")
    void register(@PathParam("id") int id);
}
