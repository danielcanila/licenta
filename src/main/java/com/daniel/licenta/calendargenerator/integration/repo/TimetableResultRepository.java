package com.daniel.licenta.calendargenerator.integration.repo;

import com.daniel.licenta.calendargenerator.business.model.TimetableResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableResultRepository extends JpaRepository<TimetableResult, Long> {
}
