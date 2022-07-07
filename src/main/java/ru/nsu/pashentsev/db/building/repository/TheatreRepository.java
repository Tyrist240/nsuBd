package ru.nsu.pashentsev.db.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.nsu.pashentsev.db.building.entity.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre, Integer>, JpaSpecificationExecutor<Theatre> {
}
