package org.example.employeeservice.service;



import org.example.employeeservice.dto.EmployeeDTO;
import org.example.employeeservice.dto.IrisTemplateDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO getEmployeeById(Long id);

    EmployeeDTO getEmployeeByCode(String employeeCode);

    List<EmployeeDTO> getAllEmployees();

    List<EmployeeDTO> getEmployeesByDepartment(String department);

    List<EmployeeDTO> getEmployeesByAccessLevel(Integer accessLevel);

    List<EmployeeDTO> searchEmployees(String keyword);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    EmployeeDTO deactivateEmployee(Long id);

    EmployeeDTO activateEmployee(Long id);

    IrisTemplateDTO addIrisTemplate(Long employeeId, IrisTemplateDTO irisTemplateDTO);

    List<IrisTemplateDTO> getEmployeeIrisTemplates(Long employeeId);

    void deleteIrisTemplate(Long templateId);
}
