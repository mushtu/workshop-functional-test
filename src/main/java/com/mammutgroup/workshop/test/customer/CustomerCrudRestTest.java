package com.mammutgroup.workshop.test.customer;

import com.mammutgroup.workshop.common.core.model.dto.CustomerDto;
import com.mammutgroup.workshop.test.Util;
import com.mammutgroup.workshop.test.WorkshopRest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.Random;


/**
 * @author mushtu
 * @since 4/17/16.
 */
public class CustomerCrudRestTest {

    public final static String path = "/customers";


    public void createCustomer()   {

        Client client = Client.create();

        WebResource webResource = client
                .resource(WorkshopRest.baseAddress+path);
        ClientResponse res = webResource.type("application/json").header(WorkshopRest.defaultAuthHeader().getKey(),WorkshopRest.defaultAuthHeader().getValue())
                .post(ClientResponse.class, Util.toJson(newCustomer()));
        System.out.println("Output from Server .... \n");
        String output = res.getEntity(String.class);
        System.out.println(output);
    }


    private CustomerDto newCustomer()
    {
        CustomerDto dto = new CustomerDto();
        Random random = new Random();
        dto.setFirstName("customer-" + random.nextInt());
        dto.setUsername("customer."+random.nextInt());
        dto.setLastName("yzai");
        dto.setMobileNumber("09127863234");
        dto.setEnabled(true);
        dto.setPassword("123");
        return dto;
    }


}
