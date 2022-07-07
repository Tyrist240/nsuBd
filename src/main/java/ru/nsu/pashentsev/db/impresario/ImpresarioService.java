package ru.nsu.pashentsev.db.impresario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.nsu.pashentsev.db.impresario.dto.ArtistProjection;
import ru.nsu.pashentsev.db.impresario.dto.ImpresarioDTO;
import ru.nsu.pashentsev.db.impresario.dto.ImpresarioResponseDTO;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjection;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjectionWithoutIds;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioProjection;
import ru.nsu.pashentsev.db.impresario.repository.ImpresarioJDBCRepository;
import ru.nsu.pashentsev.db.impresario.repository.ImpresarioRepository;
import ru.nsu.pashentsev.db.impresario.sortingfilter.ImpresarioSearchParams;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImpresarioService {

    @Autowired
    private ImpresarioRepository impresarioRepository;

    @Autowired
    private ImpresarioJDBCRepository impresarioJDBCRepository;

    static java.util.function.Predicate<ImpresarioArtistProjection> distinctByPairOfKeys(
        java.util.function.Function<ImpresarioArtistProjection, Pair<Integer, Integer>> pairExtractor
    ) {
        Set<Pair<Integer, Integer>> seen = ConcurrentHashMap.newKeySet();
        return (t) -> seen.add(pairExtractor.apply(t));
    }

    boolean isAlreadyExists(ImpresarioDTO impresarioDTO) {
        return impresarioRepository.existsById(impresarioDTO.getId());
    }

    ImpresarioResponseDTO saveImpresario(ImpresarioDTO impresarioDTO) {
        return ImpresarioMapper.toImpresarioDTO(
            impresarioRepository.save(
                ImpresarioMapper.toImpresario(impresarioDTO)
            )
        );
    }

    Page<ImpresarioResponseDTO> fetchImpresariosPage(ImpresarioSearchParams impresarioSearchParams) {
        Pageable sortedBySelectedField = impresarioSearchParams.getSortBy() == null ?
            PageRequest.of(
                impresarioSearchParams.getPageNumber(),
                impresarioSearchParams.getPageSize()
            ) :
            PageRequest.of(
                impresarioSearchParams.getPageNumber(),
                impresarioSearchParams.getPageSize(),
                Sort.by(impresarioSearchParams.getSortBy()).ascending()
            );

        Specification<Impresario> filter = buildSpec(impresarioSearchParams);

        return impresarioRepository.findAll(filter, sortedBySelectedField).map(ImpresarioMapper::toImpresarioDTO);
    }

    List<ImpresarioResponseDTO> fetchImpresariosList(ImpresarioSearchParams impresarioSearchParams) {
        Specification<Impresario> filter = buildSpec(impresarioSearchParams);

        return impresarioRepository.findAll(filter)
            .stream()
            .map(ImpresarioMapper::toImpresarioDTO)
            .collect(Collectors.toList());
    }

    void deleteImpresario(Integer impresarioId) {
        impresarioRepository.deleteById(impresarioId);
    }

    public List<ImpresarioArtistProjectionWithoutIds> getArtistsInMultipleJenres(Integer impresarioId) {
        return impresarioJDBCRepository.getArtistsWithImpresarioInMultipleJenres(impresarioId);
    }

    public List<ArtistProjection> getArtistsOfAllImpresarios(Integer impresarioId) {
        List<ImpresarioArtistProjection> projections =
            impresarioJDBCRepository.getArtistsOfAllImpresarios(impresarioId);
        List<ArtistProjection> temp = new ArrayList<>();
        for(ImpresarioArtistProjection imp : projections){
            temp.add(new ArtistProjection(imp.getArtistName(), imp.getArtistSurname(), imp.getArtistBirthDate()));
        }
        return temp;
//        Stream<Stream<ImpresarioArtistProjection>> partitionedStreams =
//            partitioningListsByImpresarioId(projections);
//
//        return partitionedStreams.map(
//            impresarioArtistProjectionStream ->
//                impresarioArtistProjectionStream
//                    .filter(distinctByPairOfKeys(ImpresarioArtistProjection::getIds))
//                    .map(it ->
//                        new ArtistsOfAllImpresariosDTO(
//                            it.getImpresarioName(),
//                            it.getImpresarioSurname(),
//                            it.getImpresarioBirthDate(),
//                            new ArrayList<>(
//                                Collections.singletonList(new ArtistProjection(it))
//                            )
//                        )
//                    ).reduce((x, y) -> new ArtistsOfAllImpresariosDTO(
//                        x.getImpresarioName(),
//                        x.getImpresarioSurname(),
//                        x.getImpresarioBirthDate(),
//                        Stream.concat(
//                            x.getArtistProjectionList().stream(),
//                            y.getArtistProjectionList().stream()
//                        ).collect(Collectors.toList())
//                    )).map(
//                        dto -> new ArtistsOfAllImpresariosDTO(
//                            dto.getImpresarioName(),
//                            dto.getImpresarioSurname(),
//                            dto.getImpresarioBirthDate(),
//                            dto.getArtistProjectionList()
//                                .stream()
//                                .filter(it -> it.getArtistName() != null)
//                                .collect(Collectors.toList())
//                        )
//                    )
//        ).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

    }

    private Stream<Stream<ImpresarioArtistProjection>> partitioningListsByImpresarioId(
        Stream<ImpresarioArtistProjection> impresarioArtistProjectionStream
    ) {
        return impresarioArtistProjectionStream.collect(
            Collectors.groupingBy(ImpresarioArtistProjection::getImpresarioId)
        ).values().stream().map(List::stream);
    }

    public List<ImpresarioProjection> getImpresariosOfArtist(Integer artistId) {
        return impresarioJDBCRepository.getImpresariosOfArtist(artistId);
    }

    public List<ImpresarioProjection> getImpresariosInJenre(Integer jenreId) {
        return impresarioJDBCRepository.getImpresariosInJenre(jenreId);
    }

    private Specification<Impresario> buildSpec(ImpresarioSearchParams impresarioSearchParams) {
        return new Specification<>() {
            @Nullable
            @Override
            public Predicate toPredicate(
                @NonNull Root<Impresario> root,
                @NonNull CriteriaQuery<?> query,
                @NonNull CriteriaBuilder criteriaBuilder
            ) {
                List<Predicate> filterPredicates = new ArrayList<>();

                if (impresarioSearchParams.getName() != null) {
                    filterPredicates.add(criteriaBuilder.equal(root.get("name"), impresarioSearchParams.getName()));
                }

                if (impresarioSearchParams.getSurname() != null) {
                    filterPredicates.add(criteriaBuilder.equal(root.get("surname"), impresarioSearchParams.getSurname()));
                }

                if (impresarioSearchParams.getBirthDate() != null) {
                    filterPredicates.add(criteriaBuilder.equal(root.get("birthDate"), impresarioSearchParams.getBirthDate()));
                }

                return criteriaBuilder.and(
                    filterPredicates.toArray(new Predicate[0])
                );
            }
        };
    }

}
