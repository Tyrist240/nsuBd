package ru.nsu.pashentsev.db.event.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.event.Event;

import java.util.List;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    List<Event> findAll();
}
