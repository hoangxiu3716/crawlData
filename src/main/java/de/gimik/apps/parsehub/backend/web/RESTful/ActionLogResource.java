package de.gimik.apps.parsehub.backend.web.RESTful;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import de.gimik.apps.parsehub.backend.model.ActionLog;
import de.gimik.apps.parsehub.backend.model.search.ActionLogSearchInfo;
import de.gimik.apps.parsehub.backend.service.ActionLogService;
import de.gimik.apps.parsehub.backend.util.DataUtility;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.Collection;

@Component
@Path("/actionlog")
public class ActionLogResource {

    @Autowired
    private ActionLogService actionLogService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public PageInfo list(
            @DefaultValue("0") @QueryParam("page") int pageIndex,
            @DefaultValue("10") @QueryParam("size") int pageSize,
            @QueryParam("field") String field,
            @QueryParam("direction") String direction,
            ActionLogSearchInfo searchInfo
    ) throws JsonGenerationException, JsonMappingException {
        Page p = actionLogService.getAll(pageIndex, pageSize, field, direction, searchInfo);
        return TransferHelper.convertToPageTransfer(p);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public ActionLog read(@PathParam("id") Long id) {
        ActionLog actionLog = actionLogService.getByID(id);
        if (actionLog == null) {
            throw new WebApplicationException(404);
        }
        return actionLog;
    }

    @Path("listActions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> listActions() throws JsonGenerationException, JsonMappingException {
        return DataUtility.getLogActions();
    }

    @Path("listObjects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> listObjects() throws JsonGenerationException, JsonMappingException {
        return DataUtility.getLogObjects();
    }
}
