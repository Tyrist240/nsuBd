package ru.nsu.pashentsev.db.building.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.building.entity.Building;

import java.util.List;

@Repository
public interface BuildingRepository extends PagingAndSortingRepository<Building, Integer>, JpaSpecificationExecutor<Building> {
    List<Building> findAll();
}
