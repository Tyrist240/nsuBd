package ru.nsu.pashentsev.db.impresario.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.impresario.Impresario;

import java.util.List;

@Repository
public interface ImpresarioRepository extends PagingAndSortingRepository<Impresario, Integer>, JpaSpecificationExecutor<Impresario> {
    List<Impresario> findAll();
}
