package com.daniel.licenta.calendargenerator.integration.repo;

import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.business.model.SlotReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotReservationRepository extends JpaRepository<SlotReservation, Long> {
}
