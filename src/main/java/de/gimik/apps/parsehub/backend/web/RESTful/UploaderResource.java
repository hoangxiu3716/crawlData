package de.gimik.apps.parsehub.backend.web.RESTful;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.gimik.apps.parsehub.backend.util.ServerConfig;
import de.gimik.apps.parsehub.backend.web.viewmodel.DataHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/uploader")
public class UploaderResource {
	
	@Autowired
	private ServerConfig serverConfig;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("generateUploadedFilename")
    public DataHolder generateUploadedFilename() {
        return new DataHolder(UUID.randomUUID().toString());
    }
    
  
}
