package ru.nsu.pashentsev.db.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.nsu.pashentsev.db.building.entity.Stage;

public interface StageRepository extends JpaRepository<Stage, Integer>, JpaSpecificationExecutor<Stage> {
}
