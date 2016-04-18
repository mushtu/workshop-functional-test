package com.mammutgroup.workshop.test;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mammutgroup.workshop.common.core.model.dto.*;
import com.mammutgroup.workshop.common.core.model.request.VehicleServiceRequest;
import com.sun.jersey.api.client.GenericType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mushtu
 * @since 4/17/16.
 */
public class Application {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try {

            System.out.println("creating a workshop ...");
            WorkshopDto workshopDto = WorkshopRest.newWorkshop();
            String workshopId = WorkshopRest.createEntity(workshopDto, "/workshops");
            workshopDto.setId(Long.valueOf(workshopId));

            System.out.println("adding services  ...");
            WorkshopServiceDto paintingService = new WorkshopServiceDto();
            paintingService.setName("painting");
            WorkshopServiceDto repairService = new WorkshopServiceDto();
            repairService.setName("repair");
            paintingService.setId(Long.valueOf(WorkshopRest.createEntity(paintingService, "/svs")));
            repairService.setId(Long.valueOf(WorkshopRest.createEntity(repairService, "/svs")));

            System.out.println("adding services to workshop ...");
            Set<WorkshopServiceDto> services = new HashSet<WorkshopServiceDto>();
            services.add(paintingService);
            services.add(repairService);
            workshopDto.setServices(services);
            WorkshopRest.updateEntity(workshopDto,"/workshops");


            System.out.println("adding lines to workshop ...");
            LineDto line1 = WorkshopRest.newLine(1, workshopDto);
            LineDto line2 = WorkshopRest.newLine(2, workshopDto);
            LineDto line3 = WorkshopRest.newLine(3, workshopDto);
            line1.setId(Long.valueOf(WorkshopRest.createEntity(line1, "/lines")));
            line2.setId(Long.valueOf(WorkshopRest.createEntity(line2, "/lines")));
            line3.setId(Long.valueOf(WorkshopRest.createEntity(line3, "/lines")));


            System.out.println("adding offices ... ");
            OfficeDto managerOfficeDto = new OfficeDto();
            managerOfficeDto.setName("Manager");
            String managerOfficeId = WorkshopRest.createEntity(managerOfficeDto, "/offices");
            managerOfficeDto.setId(Long.valueOf(managerOfficeId));
            OfficeDto repairmanOfficeDto = new OfficeDto();
            repairmanOfficeDto.setName("Repairman");
            String rpManOfficeId = WorkshopRest.createEntity(repairmanOfficeDto, "/offices");
            repairmanOfficeDto.setId(Long.valueOf(rpManOfficeId));


            System.out.println("adding a manager employee to workshop ...");
            EmployeeDto managerEmployee = WorkshopRest.newEmployee();
            managerEmployee.setUsername("manager");
            WorkshopOfficeDto workshopOfficeDto = new WorkshopOfficeDto();
            workshopOfficeDto.setWorkshop(workshopDto);
            workshopOfficeDto.setOffice(managerOfficeDto);
            workshopOfficeDto.setSalary(2000000L);
            Set<WorkshopOfficeDto> offices = new HashSet<WorkshopOfficeDto>();
            offices.add(workshopOfficeDto);
            managerEmployee.setOffices(offices);
            managerEmployee.setId(Long.valueOf(WorkshopRest.createEntity(managerEmployee, "/employees")));


            System.out.println("adding a repairman employee to workshop ...");
            EmployeeDto repairmanEmployee = WorkshopRest.newEmployee();
            repairmanEmployee.setUsername("repairman");
            WorkshopOfficeDto rpWorkshopOfficeDto = new WorkshopOfficeDto();
            rpWorkshopOfficeDto.setWorkshop(workshopDto);
            rpWorkshopOfficeDto.setSalary(3000000L);
            rpWorkshopOfficeDto.setOffice(repairmanOfficeDto);
            offices.clear();
            offices.add(rpWorkshopOfficeDto);
            repairmanEmployee.setOffices(offices);
            repairmanEmployee.setId(Long.valueOf(WorkshopRest.createEntity(repairmanEmployee, "/employees")));


            System.out.println("adding a customer to workshop ...");
            CustomerDto customerDto = WorkshopRest.newCustomer();
            customerDto.setUsername("customer");
            customerDto.setId(Long.valueOf(WorkshopRest.createEntity(customerDto, "/customers")));


            System.out.println("getting workshop lines status ...");
            String response = WorkshopRest.getAllEntities("/lines");
            List<LineDto> lines = gson.fromJson(response,new TypeToken<ArrayList<LineDto>>(){}.getType());

            System.out.println("getting customers info ...");
            WorkshopRest.getAllEntities("/customers");


            System.out.println("request for a service using customer credentials ...");
            VehicleServiceRequest vehicleServiceRequest = new VehicleServiceRequest();
            VehicleDto vehicleDto = new VehicleDto();
            VehicleCategoryDto categoryDto = new VehicleCategoryDto();
            categoryDto.setName("category1");
            vehicleDto.setCategory(categoryDto);
            vehicleServiceRequest.setVehicle(vehicleDto);
            vehicleServiceRequest.setService(paintingService);
            WorkshopRest.post(vehicleServiceRequest,"/customer-service",
                    WorkshopRest.createAuthHeader(customerDto.getUsername(),customerDto.getPassword()));


            //serviceDto.setVehicleService();


            //Type type = new TypeToken<ArrayList<EmployeeDto>>(){}.getType();
            //String out = WorkshopRest.getAllEntities("/employees");
            //List<EmployeeDto> employees = gson.fromJson(out,type);


            //WorkshopRest.createEntity(WorkshopRest.newLine(1),"/lines");
            //
            //WorkshopRest.getAllEntities("/workshops");
            //WorkshopRest.createEntity(WorkshopRest.newLine(1,));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
