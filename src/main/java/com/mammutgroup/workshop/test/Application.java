package com.mammutgroup.workshop.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mammutgroup.workshop.common.core.model.dto.*;
import com.mammutgroup.workshop.common.core.model.dto.bpm.TaskDto;
import com.mammutgroup.workshop.common.core.model.request.CompleteVehicleServiceResourceAssignmentTask;
import com.mammutgroup.workshop.common.core.model.request.CompleteVehicleServiceTask;
import com.mammutgroup.workshop.common.core.model.request.VehicleServiceRequest;
import com.mammutgroup.workshop.common.core.model.response.VehicleServiceResponse;
import ir.amv.os.vaseline.base.core.shared.base.dto.paging.PagingDto;

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
        WorkshopRest rest = new WorkshopRest();
        Gson gson =rest.GSON_BUILDER.create();

        try {

            System.out.println("creating a workshop ...");
            WorkshopDto workshopDto = rest.newWorkshop();
            String workshopId = rest.createEntity(workshopDto, "/workshops");
            workshopDto.setId(Long.valueOf(workshopId));

            System.out.println("adding services  ...");
            WorkshopServiceDto paintingService = new WorkshopServiceDto();
            paintingService.setName("painting");
            WorkshopServiceDto repairService = new WorkshopServiceDto();
            repairService.setName("repair");
            paintingService.setId(Long.valueOf(rest.createEntity(paintingService, "/svs")));
            repairService.setId(Long.valueOf(rest.createEntity(repairService, "/svs")));

            System.out.println("adding services to workshop ...");
            Set<WorkshopServiceDto> services = new HashSet<WorkshopServiceDto>();
            services.add(paintingService);
            services.add(repairService);
            workshopDto.setServices(services);
            rest.updateEntity(workshopDto,"/workshops");


            System.out.println("adding lines to workshop ...");
            LineDto line1 = rest.newLine(1, workshopDto);
            LineDto line2 = rest.newLine(2, workshopDto);
            LineDto line3 = rest.newLine(3, workshopDto);
            System.out.println(gson.toJson(line1));

            line1.setId(Long.valueOf(rest.createEntity(line1, "/lines")));
            line2.setId(Long.valueOf(rest.createEntity(line2, "/lines")));
            line3.setId(Long.valueOf(rest.createEntity(line3, "/lines")));


            System.out.println("adding offices ... ");
            OfficeDto managerOfficeDto = new OfficeDto();
            managerOfficeDto.setName("Manager");
            String managerOfficeId = rest.createEntity(managerOfficeDto, "/offices");
            managerOfficeDto.setId(Long.valueOf(managerOfficeId));
            OfficeDto repairmanOfficeDto = new OfficeDto();
            repairmanOfficeDto.setName("Repairman");
            String rpManOfficeId = rest.createEntity(repairmanOfficeDto, "/offices");
            repairmanOfficeDto.setId(Long.valueOf(rpManOfficeId));


            System.out.println("adding a manager employee to workshop ...");
            EmployeeDto managerEmployee = rest.newEmployee();
            managerEmployee.setUsername("manager");
            WorkshopOfficeDto workshopOfficeDto = new WorkshopOfficeDto();
            workshopOfficeDto.setWorkshop(workshopDto);
            workshopOfficeDto.setOffice(managerOfficeDto);
            workshopOfficeDto.setSalary(2000000L);
            Set<WorkshopOfficeDto> offices = new HashSet<WorkshopOfficeDto>();
            offices.add(workshopOfficeDto);
            managerEmployee.setOffices(offices);
            managerEmployee.setId(Long.valueOf(rest.createEntity(managerEmployee, "/employees")));


            System.out.println("adding a repairman employee to workshop ...");
            EmployeeDto repairmanEmployee = rest.newEmployee();
            repairmanEmployee.setUsername("repairman");
            WorkshopOfficeDto rpWorkshopOfficeDto = new WorkshopOfficeDto();
            rpWorkshopOfficeDto.setWorkshop(workshopDto);
            rpWorkshopOfficeDto.setSalary(3000000L);
            rpWorkshopOfficeDto.setOffice(repairmanOfficeDto);
            offices.clear();
            offices.add(rpWorkshopOfficeDto);
            repairmanEmployee.setOffices(offices);
            repairmanEmployee.setId(Long.valueOf(rest.createEntity(repairmanEmployee, "/employees")));


            System.out.println("adding a customer to workshop ...");
            CustomerDto customerDto = rest.newCustomer();
            customerDto.setUsername("customer");
            customerDto.setId(Long.valueOf(rest.createEntity(customerDto, "/customers")));

            System.out.println("getting customers info ...");
            rest.get("/customers");


            System.out.println("request for two service using customer credentials ...");
            VehicleServiceRequest vehicleServiceRequest = new VehicleServiceRequest();
            VehicleDto vehicleDto = new VehicleDto();
            VehicleCategoryDto categoryDto = new VehicleCategoryDto();
            categoryDto.setName("category1");
            vehicleDto.setCategory(categoryDto);
            vehicleServiceRequest.setVehicle(vehicleDto);
            vehicleServiceRequest.setService(paintingService);
            String resonse = rest.post(vehicleServiceRequest,"/request",
                    rest.createAuthHeader(customerDto.getUsername(),customerDto.getPassword()));
            VehicleServiceResponse vehicleServiceResponse = gson.fromJson(resonse,VehicleServiceResponse.class);

            System.out.println("getting service state ...");
            rest.get("/request/" + vehicleServiceResponse.getProcessId());

            PagingDto pagingDto = new PagingDto();
            pagingDto.setPageNumber(0);
            pagingDto.setPageSize(10);

            System.out.println("getting tasks assigned to repairman before any assignment! ...");
            rest.post(pagingDto,"/tasks/assigned",rest.createAuthHeader("repairman","123"));

            System.out.println("getting lines status before assignment ...");
            rest.get("/lines");



            System.out.println("Getting tasks assigned to manager ...");
            String tasksJson = rest.post(pagingDto,"/tasks/assigned",rest.createAuthHeader("manager","123"));
            List<TaskDto> tasks = gson.fromJson(tasksJson,new TypeToken<List<TaskDto>>(){}.getType());
            for(TaskDto task:tasks)
            {
                System.out.println("complete the task ...");
                CompleteVehicleServiceResourceAssignmentTask cmp = new CompleteVehicleServiceResourceAssignmentTask();
                cmp.setLine(line1);
                cmp.setEmployee(repairmanEmployee);
                cmp.setTaskId(task.getId());
                rest.post(cmp,"/manage/resourceAssignment",rest.createAuthHeader("manager","123"));

            }

            System.out.println("getting lines status after assignment ...");
            rest.get("/lines");

            System.out.println("Getting tasks assigned to manager after completion ...");
            rest.post(pagingDto,"/tasks/assigned",rest.createAuthHeader("manager","123"));

            System.out.println("getting service state ...");
            rest.get("/request/" + vehicleServiceResponse.getProcessId());


            System.out.println("getting tasks assigned to repairman after assignment! ...");
            String repairManJsonTasks = rest.post(pagingDto,"/tasks/assigned",rest.createAuthHeader("repairman","123"));
            List<TaskDto> repairmanTasks = gson.fromJson(repairManJsonTasks,new TypeToken<List<TaskDto>>(){}.getType());
            for(TaskDto task:repairmanTasks)
            {
                System.out.println("complete the task assigned to repairman...");
                CompleteVehicleServiceTask cmp = new CompleteVehicleServiceTask();
                cmp.setTaskId(task.getId());
                rest.post(cmp,"/manage/complete",rest.createAuthHeader("repairman","123"));

            }

            System.out.println("getting tasks assigned to repairman after completion! ...");
            rest.post(pagingDto,"/tasks/assigned",rest.createAuthHeader("repairman","123"));

            System.out.println("getting service state ...");
            rest.get("/request/" + vehicleServiceResponse.getProcessId());

            System.out.println("getting lines status  ...");
            rest.get("/lines");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
