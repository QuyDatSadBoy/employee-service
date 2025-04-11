package org.example.employeeservice.repository;

import org.example.employeeservice.model.IrisTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrisTemplateRepository extends JpaRepository<IrisTemplate, Long> {

    List<IrisTemplate> findByEmployeeId(Long employeeId);

    Optional<IrisTemplate> findByEmployeeIdAndEyePosition(Long employeeId, String eyePosition);

    List<IrisTemplate> findByIsActive(Boolean isActive);

    void deleteByEmployeeId(Long employeeId);
}
