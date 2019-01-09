package com.daniel.licenta.calendargenerator.integration.repo;

import com.daniel.licenta.calendargenerator.business.model.TimetableConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableConfigRepository extends JpaRepository<TimetableConfig, Long> {
}
