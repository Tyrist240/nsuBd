package ru.nsu.pashentsev.db.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.nsu.pashentsev.db.building.entity.Cinema;

public interface CinemaRepository extends JpaRepository<Cinema, Integer>, JpaSpecificationExecutor<Cinema> {
}
