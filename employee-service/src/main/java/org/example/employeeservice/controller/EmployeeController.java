package org.example.employeeservice.controller;

import jakarta.validation.Valid;
import org.example.employeeservice.dto.EmployeeDTO;
import org.example.employeeservice.dto.IrisTemplateDTO;
import org.example.employeeservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(@PathVariable String employeeCode) {
        return ResponseEntity.ok(employeeService.getEmployeeByCode(employeeCode));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    @GetMapping("/access-level/{accessLevel}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByAccessLevel(@PathVariable Integer accessLevel) {
        return ResponseEntity.ok(employeeService.getEmployeesByAccessLevel(accessLevel));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam String keyword) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<EmployeeDTO> deactivateEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.deactivateEmployee(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<EmployeeDTO> activateEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.activateEmployee(id));
    }

    // Iris Template endpoints
    @PostMapping("/{employeeId}/iris-templates")
    public ResponseEntity<IrisTemplateDTO> addIrisTemplate(
            @PathVariable Long employeeId,
            @Valid @RequestBody IrisTemplateDTO irisTemplateDTO) {
        IrisTemplateDTO createdTemplate = employeeService.addIrisTemplate(employeeId, irisTemplateDTO);
        return new ResponseEntity<>(createdTemplate, HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}/iris-templates")
    public ResponseEntity<List<IrisTemplateDTO>> getEmployeeIrisTemplates(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeIrisTemplates(employeeId));
    }

    @DeleteMapping("/iris-templates/{templateId}")
    public ResponseEntity<Void> deleteIrisTemplate(@PathVariable Long templateId) {
        employeeService.deleteIrisTemplate(templateId);
        return ResponseEntity.noContent().build();
    }
}
