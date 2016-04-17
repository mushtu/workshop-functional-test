package com.mammutgroup.workshop.test;


import java.util.AbstractMap;
import java.util.Map;

/**
 * @author mushtu
 * @since 4/17/16.
 */
public class WorkshopRest {

    public final static String baseAddress = "http://localhost:8085/core-server/cxf/rest";

    public static Map.Entry<String,String> defaultAuthHeader()
    {
        String username = "admin";
        String password = "123";

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
        return new AbstractMap.SimpleEntry<String, String>(authorizationHeaderName,authorizationHeaderValue);
    }
}
