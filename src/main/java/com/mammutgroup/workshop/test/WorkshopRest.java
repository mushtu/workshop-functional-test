package com.mammutgroup.workshop.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public final  String baseAddress = "http://localhost:8085/core-server/cxf/rest";

    public final  GsonBuilder GSON_BUILDER = new GsonBuilder();

    public WorkshopRest()
    {
        new GraphAdapterBuilder().addType(LineDto.class).registerOn(GSON_BUILDER);
    }



    public  Map.Entry<String, String> createAuthHeader(String username,String password)
    {

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
        return new AbstractMap.SimpleEntry<String, String>(authorizationHeaderName, authorizationHeaderValue);
    }


    public  Map.Entry<String, String> adminAuth() {
        String username = "admin";
        String password = "123";

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
        return new AbstractMap.SimpleEntry<String, String>(authorizationHeaderName, authorizationHeaderValue);
    }


    public  String get(String path) {
        System.out.println("GET all ...");
        Client client = Client.create();
        WebResource webResource = client
                .resource(baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(adminAuth().getKey(), adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        String output = res.getEntity(String.class);
        System.out.println(output);
        return output;
    }

    public  void updateEntity(Object entity,String path)
    {
        System.out.println("Create entity ...");
        Client client = Client.create();

        WebResource webResource = client
                .resource(baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(adminAuth().getKey(), adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, GSON_BUILDER.create().toJson(entity));
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        //String output = res.getEntity(String.class);
        //System.out.println(output);

    }

    public  String post(Object entity,String path,Map.Entry<String,String> auth)
    {
        Client client = Client.create();
        WebResource webResource = client
                .resource(baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(auth.getKey(), auth.getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, GSON_BUILDER.create().toJson(entity));
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


    public  String createEntity(Object entity, String path) {

        System.out.println("Create entity ...");
        Client client = Client.create();

        WebResource webResource = client
                .resource(baseAddress + path);
        ClientResponse res = webResource.type("application/json").header(adminAuth().getKey(), adminAuth().getValue())
                .accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, GSON_BUILDER.create().toJson(entity));
        System.out.println("Output from Server (status code: " + res.getStatus() + ")");
        String output = res.getEntity(String.class);
        System.out.println(output);
        return output;
    }

    public  CustomerDto newCustomer() {
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


    public  EmployeeDto newEmployee() {
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

    public  WorkshopDto newWorkshop() {
        WorkshopDto workshopDto = new WorkshopDto();
        workshopDto.setName("workshop-" + new Random().nextInt());
        workshopDto.setAddress("Iran,Tehran");
        return workshopDto;
    }

    public  LineDto newLine(int lineNumber, WorkshopDto workshopDto) {
        LineDto lineDto = new LineDto();
        lineDto.setLineNumber(lineNumber);
        lineDto.setWorkshop(workshopDto);
        return lineDto;
    }
}
