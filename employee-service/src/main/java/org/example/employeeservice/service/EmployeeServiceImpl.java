package org.example.employeeservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.employeeservice.dto.EmployeeDTO;
import org.example.employeeservice.dto.IrisTemplateDTO;
import org.example.employeeservice.model.Employee;
import org.example.employeeservice.model.IrisTemplate;
import org.example.employeeservice.repository.EmployeeRepository;
import org.example.employeeservice.repository.IrisTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IrisTemplateRepository irisTemplateRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, IrisTemplateRepository irisTemplateRepository) {
        this.employeeRepository = employeeRepository;
        this.irisTemplateRepository = irisTemplateRepository;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + employeeCode));
        return convertToDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByAccessLevel(Integer accessLevel) {
        return employeeRepository.findByAccessLevel(accessLevel).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setAccessLevel(employeeDTO.getAccessLevel());

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public EmployeeDTO deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        employee.setIsActive(false);
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO activateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        employee.setIsActive(true);
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public IrisTemplateDTO addIrisTemplate(Long employeeId, IrisTemplateDTO irisTemplateDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        IrisTemplate irisTemplate = new IrisTemplate();
        irisTemplate.setTemplateName(irisTemplateDTO.getTemplateName());
        irisTemplate.setEyePosition(irisTemplateDTO.getEyePosition());
        irisTemplate.setTemplateFormat(irisTemplateDTO.getTemplateFormat());
        irisTemplate.setIsActive(true);

        // Decode Base64 template data
        if (irisTemplateDTO.getTemplateDataBase64() != null && !irisTemplateDTO.getTemplateDataBase64().isEmpty()) {
            irisTemplate.setTemplateData(Base64.getDecoder().decode(irisTemplateDTO.getTemplateDataBase64()));
        }

        employee.addIrisTemplate(irisTemplate);
        Employee savedEmployee = employeeRepository.save(employee);

        // Find the saved template
        IrisTemplate savedTemplate = savedEmployee.getIrisTemplates().stream()
                .filter(t -> t.getTemplateName().equals(irisTemplate.getTemplateName()) &&
                        t.getEyePosition().equals(irisTemplate.getEyePosition()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to save iris template"));

        return convertToDTO(savedTemplate);
    }

    @Override
    public List<IrisTemplateDTO> getEmployeeIrisTemplates(Long employeeId) {
        return irisTemplateRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteIrisTemplate(Long templateId) {
        if (!irisTemplateRepository.existsById(templateId)) {
            throw new EntityNotFoundException("Iris template not found with id: " + templateId);
        }
        irisTemplateRepository.deleteById(templateId);
    }

    // Helper methods for DTO conversion
    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeCode(employeeDTO.getEmployeeCode());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setAccessLevel(employeeDTO.getAccessLevel());
        employee.setIsActive(employeeDTO.getIsActive() != null ? employeeDTO.getIsActive() : true);
        return employee;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setAccessLevel(employee.getAccessLevel());
        dto.setIsActive(employee.getIsActive());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        // Don't include iris templates to avoid circular references
        // These can be fetched separately

        return dto;
    }

    private IrisTemplateDTO convertToDTO(IrisTemplate irisTemplate) {
        IrisTemplateDTO dto = new IrisTemplateDTO();
        dto.setId(irisTemplate.getId());
        dto.setTemplateName(irisTemplate.getTemplateName());
        dto.setEyePosition(irisTemplate.getEyePosition());
        dto.setTemplateFormat(irisTemplate.getTemplateFormat());
        dto.setIsActive(irisTemplate.getIsActive());
        dto.setEmployeeId(irisTemplate.getEmployee().getId());
        dto.setCreatedAt(irisTemplate.getCreatedAt());
        dto.setUpdatedAt(irisTemplate.getUpdatedAt());

        // Convert binary data to Base64 for transfer
        if (irisTemplate.getTemplateData() != null) {
            dto.setTemplateDataBase64(Base64.getEncoder().encodeToString(irisTemplate.getTemplateData()));
        }

        return dto;
    }
}
