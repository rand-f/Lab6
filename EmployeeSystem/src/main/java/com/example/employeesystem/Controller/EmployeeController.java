package com.example.employeesystem.Controller;

import com.example.employeesystem.ApiResponse.ApiResponse;
import com.example.employeesystem.Model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
    return ResponseEntity.status(200).body(employees);
    }

    @PostMapping ("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employee.setHireDate(LocalDate.now());
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("employee added successfully"));
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<?> updateEmployee(@PathVariable String ID, @RequestBody Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for(Employee e:employees){
            if (e.getID().equals(ID)){
                employees.set(employees.indexOf(e),employee);
                return ResponseEntity.status(200).body(new ApiResponse("employee updated successfully successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("employee not found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String ID){
        for(Employee employee:employees){
            if (employee.getID().equals(ID)){
                employees.remove(employee);
                return ResponseEntity.status(200).body(new ApiResponse("employee removed successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("employee not found"));
    }

    @GetMapping("/get-by-position/{position}")
    public ResponseEntity<?>getByPosition(@PathVariable String position){
        if(position.equalsIgnoreCase("supervisor") || position.equalsIgnoreCase("coordinator")){
            ArrayList<Employee> positionEmployee=new ArrayList<>();
            for (Employee employee:employees){
                if(employee.getPosition().equals(position)){
                    positionEmployee.add(employee);
                }
            }
            if(positionEmployee.isEmpty()){
                return ResponseEntity.status(400).body(new ApiResponse("employees in this position are not found"));
            }

            return ResponseEntity.status(200).body(positionEmployee);
        }
        return ResponseEntity.status(400).body(new ApiResponse("position must be supervisor or coordinator"));
    }

    @GetMapping("/get-age-range/{minAge}/{maxAge}")
    public ResponseEntity<?>getByAgeRange(@PathVariable int minAge, @PathVariable int maxAge){
        if(minAge<25){
            return ResponseEntity.status(400).body(new ApiResponse("age must be 25 or greater"));
        }

        ArrayList<Employee> employeesInAgeRange=new ArrayList<>();

        for(Employee employee:employees){
            if(employee.getAge()>= minAge && employee.getAge()<=maxAge){
                employeesInAgeRange.add(employee);
            }
        }
        if(employeesInAgeRange.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("employees in this age range are not found"));
        }
        return ResponseEntity.status(200).body(employeesInAgeRange);
    }

    @PutMapping("/apply-for-leave/{ID}")
    public ResponseEntity<?>applyForAnnualLeave(@PathVariable String ID){
        for (Employee employee:employees){
            if(employee.getID().equals(ID)){
                if(employee.isOnLeave()){
                    return ResponseEntity.status(400).body(new ApiResponse("employees is already on leave"));
                }
                if(employee.getAnnualLeave()==0){
                    return ResponseEntity.status(400).body(new ApiResponse("employees has no annual leave left"));
                }
                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave()-1);
                return ResponseEntity.status(200).body(new ApiResponse("employees is now on leave "));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("employees not found"));
    }

    @GetMapping("/get-no-leave")
    public ResponseEntity<?>getNoLeave(){
        ArrayList<Employee>noAnnualLeave=new ArrayList<>();
        for (Employee employee:employees){
            if(employee.getAnnualLeave()==0){
                noAnnualLeave.add(employee);
            }
        }
        if(noAnnualLeave.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("employees with no annual leave not found"));
        }
        return ResponseEntity.status(200).body(noAnnualLeave);
    }

    @PutMapping("/prompt/{userID}/{ID}")
    public ResponseEntity<?>promptEmployee(@PathVariable String userID, @PathVariable String ID){
        for(Employee userEmployee: employees){
            if(userEmployee.getID().equals(userID)){
                if(!userEmployee.getPosition().equals("supervisor")){
                    return ResponseEntity.status(400).body(new ApiResponse("you must be a supervisor to prompt an employee"));
                }
                for (Employee employee:employees){
                    if (employee.getID().equals(ID)){
                        if(employee.getPosition().equals("supervisor")){
                            return ResponseEntity.status(400).body(new ApiResponse("this employee is already a supervisor"));
                        }
                        if(employee.getAge()<30){
                            return ResponseEntity.status(400).body(new ApiResponse("employee must be at least 30 years old"));
                        }
                        if(employee.isOnLeave()){
                            return ResponseEntity.status(400).body(new ApiResponse("employee must not be on leave"));
                        }
                        employee.setPosition("supervisor");
                        return ResponseEntity.status(200).body(new ApiResponse("employee's position changed"));
                    }
                }
                return ResponseEntity.status(400).body(new ApiResponse("employee not found"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("you must be an employee to prompt an employee"));
    }

}
