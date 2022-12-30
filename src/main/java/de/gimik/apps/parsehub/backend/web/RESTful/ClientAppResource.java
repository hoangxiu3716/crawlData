package de.gimik.apps.parsehub.backend.web.RESTful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import de.gimik.apps.parsehub.backend.security.MD5Encoder;
import de.gimik.apps.parsehub.backend.util.ServerConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;

@Component
@Path("/clientapp")
public class ClientAppResource {

	@Autowired
	private MD5Encoder passwordEncoder;
	@Autowired
	private ServerConfig serverConfig;

	
}
