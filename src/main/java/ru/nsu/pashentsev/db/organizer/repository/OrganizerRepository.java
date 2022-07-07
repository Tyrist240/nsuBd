package ru.nsu.pashentsev.db.organizer.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.organizer.Organizer;

import java.util.List;

@Repository
public interface OrganizerRepository extends PagingAndSortingRepository<Organizer, Integer>, JpaSpecificationExecutor<Organizer> {
    List<Organizer> findAll();
}
