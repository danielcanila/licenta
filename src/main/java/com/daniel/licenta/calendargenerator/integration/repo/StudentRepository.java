package com.daniel.licenta.calendargenerator.integration.repo;

import com.daniel.licenta.calendargenerator.business.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}

