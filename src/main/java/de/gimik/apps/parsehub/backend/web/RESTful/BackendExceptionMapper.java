package de.gimik.apps.parsehub.backend.web.RESTful;

import com.google.common.collect.Maps;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.ErrorInfo;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import java.util.Map;

@Provider
@Component
public class BackendExceptionMapper implements ExceptionMapper<BackendException> {

    private static final Map<String, String> ErrorMap = Maps.newHashMap();

    static {
        ErrorMap.put(Constants.ErrorCode.NOT_AVAILABLE_FUNCTION, "Diese Funktion ist nicht verfügbar.");
        ErrorMap.put(Constants.ErrorCode.UNKNOWN_ERROR, "Unbekannter Fehler aufgetreten. Bitte versuchen Sie es erneut.");
        ErrorMap.put(Constants.ErrorCode.IMAGE_UPLOAD_ERROR, "Error when uploading image file.");
        ErrorMap.put(Constants.ErrorCode.CENTER_NOT_SELECTED, "Bitte wählen Sie einen Center.");
        ErrorMap.put(Constants.ErrorCode.FROM_DATE_MUST_BEFORE_TO_DATE, "Von Datum muss vor bis Datum.");

        ErrorMap.put(Constants.ErrorCode.USERNAME_DUPLICATED, "The username already exists. ");
        ErrorMap.put(Constants.ErrorCode.ABBREVIATION_DUPLICATED, "The abrreviation already exists. ");
        ErrorMap.put(Constants.ErrorCode.USER_ROLE_NOT_SET, "Es muss mindestens eine Rolle ausgewählt werden.");
        ErrorMap.put(Constants.ErrorCode.USERNAME_NOT_EXIST, "Der Benutzer nicht existiert.");
        ErrorMap.put(Constants.ErrorCode.PASSWORD_INVALID, "Das aktuelle Passwort ist ungültig.");
        
        ErrorMap.put(Constants.ErrorCode.NAME_DUPLICATE, "Name is already exist.");
        ErrorMap.put(Constants.ErrorCode.ROOM_INFO_DUPLICATE, "Combination \"room number , building , floor\" is already exist in system .");
        ErrorMap.put(Constants.ErrorCode.PROJECT_NUMBER_EXIST, "Project already exists in system.");
    }

    @Override
    public Response toResponse(BackendException e) {
        return Response.status(Response.Status.OK)
                       .entity(new ErrorInfo(e.getErrorCode(), ErrorMap.get(e.getErrorCode()), e.getLocalizedMessage()))
                       .type(MediaType.APPLICATION_JSON).build();
    }

    public static String getError(String errorCode){
        return ErrorMap.get(errorCode);
    }
}
