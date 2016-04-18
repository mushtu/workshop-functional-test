package com.mammutgroup.workshop.test;


import com.mammutgroup.workshop.common.core.model.dto.CustomerDto;
import com.mammutgroup.workshop.common.core.model.dto.EmployeeDto;
import com.mammutgroup.workshop.common.core.model.dto.LineDto;
import com.mammutgroup.workshop.common.core.model.dto.WorkshopDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * @author mushtu
 * @since 4/17/16.
 */
public class WorkshopRest {

    public final static String baseAddress = "http://localhost:8085/core-server/cxf/rest";

    public static Map.Entry<String, String> createAuthHeader(String username,String password)
    {

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
        return new AbstractMap.SimpleEntry<String, String>(authorizationHeaderName, authorizationHeaderValue);
    }


    public static Map.Entry<String, String> adminAuth() {
        String username = "admin";
        String password = "123";

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
        return new AbstractMap.SimpleEntry<String, String>(authorizationHeaderName, authorizationHeaderValue);
    }


    public static String getAllEntities(String path) {
        System.out.println("GET all ...");
        Client client = Client.create();
        WebResource webResource = client
                .resource(WorkshopRest.baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(WorkshopRest.adminAuth().getKey(), WorkshopRest.adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        String output = res.getEntity(String.class);
        System.out.println(output);
        return output;
    }

    public static void updateEntity(Object entity,String path)
    {
        System.out.println("Create entity ...");
        Client client = Client.create();

        WebResource webResource = client
                .resource(WorkshopRest.baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(WorkshopRest.adminAuth().getKey(), WorkshopRest.adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, Util.toJson(entity));
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        //String output = res.getEntity(String.class);
        //System.out.println(output);

    }

    public static String post(Object entity,String path,Map.Entry<String,String> auth)
    {
        Client client = Client.create();
        WebResource webResource = client
                .resource(WorkshopRest.baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(auth.getKey(), auth.getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, Util.toJson(entity));
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        String output = null;
        try {
            output = res.getEntity(String.class);
        }catch (Exception e)
        {

        }
        System.out.println(output);
        return output;
    }


    public static String createEntity(Object entity, String path) {

        System.out.println("Create entity ...");
        Client client = Client.create();

        WebResource webResource = client
                .resource(WorkshopRest.baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(WorkshopRest.adminAuth().getKey(), WorkshopRest.adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, Util.toJson(entity));
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        String output = res.getEntity(String.class);
        System.out.println(output);
        return output;
    }

    public static CustomerDto newCustomer() {
        CustomerDto dto = new CustomerDto();
        Random random = new Random();
        dto.setFirstName("customer-" + random.nextInt());
        dto.setUsername("customer." + random.nextInt());
        dto.setLastName("yzai");
        dto.setMobileNumber("09127863234");
        dto.setEnabled(true);
        dto.setPassword("123");
        return dto;
    }


    public static EmployeeDto newEmployee() {
        Random random = new Random();
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("employee-" + random.nextInt());
        employeeDto.setUsername("employee-" + random.nextInt());
        employeeDto.setLastName("yzai");
        employeeDto.setMobileNumber("09198147879");
        employeeDto.setPassword("123");
        employeeDto.setEnabled(true);
        return employeeDto;
    }

    public static WorkshopDto newWorkshop() {
        WorkshopDto workshopDto = new WorkshopDto();
        workshopDto.setName("workshop-" + new Random().nextInt());
        workshopDto.setAddress("Iran,Tehran");
        return workshopDto;
    }

    public static LineDto newLine(int lineNumber, WorkshopDto workshopDto) {
        LineDto lineDto = new LineDto();
        lineDto.setLineNumber(lineNumber);
        lineDto.setWorkshop(workshopDto);
        return lineDto;
    }
}
