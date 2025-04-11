package org.example.employeeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrisTemplateDTO {

    private Long id;

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotBlank(message = "Eye position is required")
    private String eyePosition; // LEFT, RIGHT

    // Base64 encoded string for template data
    private String templateDataBase64;

    private String templateFormat;

    private Boolean isActive = true;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
