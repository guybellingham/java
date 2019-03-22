package com.innotas.r2d2.api;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
/**
 * <p>This class uses the Spring MVC annotations and is a web @Controller, 
 * so Spring considers it when handling incoming web requests.
 * 
 */
@RestController
public class ServerRoutes extends BaseRoutes {
	/**
	 * Tells Spring that any HTTP GET request with the <code>/status</code> path should be mapped 
	 * to this getStatus() method.
	 * @return the JSON to be output in the response
	 */
    @RequestMapping(path = "/status", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    @ResponseBody
    public String getStatus() {
        JsonObject obj = new JsonObject();
        obj.addProperty("status", "ok");
        obj.addProperty("httpRequest", httpRequest.toString());
        return obj.toString();
    }
}
