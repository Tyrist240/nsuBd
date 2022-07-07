package ru.nsu.pashentsev.db.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.pashentsev.db.artist.dto.ArtistDTO;
import ru.nsu.pashentsev.db.artist.dto.ArtistImpresarioJenreDto;
import ru.nsu.pashentsev.db.artist.dto.ArtistResponseDTO;
import ru.nsu.pashentsev.db.artist.dto.PeriodDTO;
import ru.nsu.pashentsev.db.artist.projections.ArtistEventProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistImpresarioJenreProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistsTogetherProjection;
import ru.nsu.pashentsev.db.artist.sortingfilter.ArtistSearchParams;

import java.util.List;

@RestController
@RequestMapping(value = "/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping(
        value = "/add",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ArtistResponseDTO createArtist(@RequestBody ArtistDTO artistDTO) {
        if (artistDTO.getId() != null && artistService.isAlreadyExists(artistDTO)) {
            return null;
        }

        return artistService.saveArtist(artistDTO);
    }

    @PostMapping(
        value = "/fetch/page",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ArtistResponseDTO> fetchArtistsPage(@RequestBody ArtistSearchParams artistSearchParams) {
        if (artistSearchParams.getPageNumber() == null || artistSearchParams.getPageSize() == null) {
            return null;
        }

        return artistService.fetchArtistsPage(artistSearchParams).getContent();
    }

    @PostMapping(
        value = "/fetch/list",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ArtistResponseDTO> fetchArtistsList(@RequestBody ArtistSearchParams artistSearchParams) {
        return artistService.fetchArtistsList(artistSearchParams);
    }

    @PostMapping(
        value = "/update",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ArtistResponseDTO updateArtist(@RequestBody ArtistDTO artistDTO) {
        if (artistDTO.getId() == null || !artistService.isAlreadyExists(artistDTO)) {
            return null;
        }

        return artistService.saveArtist(artistDTO);
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteArtist(@PathVariable("id") Integer artistId) {
        artistService.deleteArtist(artistId);
    }

    @PostMapping(value = "/artistWithImpresario/add", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public void makeArtistWorkingWithImpresarioInJenre(@RequestBody ArtistImpresarioJenreDto artistImpresarioJenreDto) {
        artistService.makeArtistWorkingWithImpresarioInJenre(artistImpresarioJenreDto);
    }

    @PostMapping(value = "/artistWithImpresario/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistImpresarioJenreProjection> getArtistWorkingWithImpresarioInJenre() {
        return artistService.getArtistWorkingWithImpresarioInJenre();
    }

    @PostMapping(value = "/artistWithImpresario/delete/{imp_id}/{art_id}/{jen_id}")
    public void deleteArtistWorkingWithImpresarioInJenre(
        @PathVariable("imp_id") Integer impresarioId,
        @PathVariable("art_id") Integer artistId,
        @PathVariable("jen_id") Integer jenreId
    ) {
        artistService.deleteArtistWithImpresarioInJenre(impresarioId, artistId, jenreId);
    }

    //Получить список артистов, выступающих в некотором жанре
    @PostMapping(value = "/artistsInJenre/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistProjection> getArtistsInJenre(@PathVariable("id") Integer jenreId) {
        return artistService.getArtistsInJenre(jenreId);
    }
    //Получить список артистов, не участвовавших ни в каких конкурсах в течение определенного периода времени.
    @PostMapping(value = "/artistsNotTakingPartInPeriod", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistProjection> getArtistsNotTakingPartInPeriod(@RequestBody PeriodDTO periodDTO) {
        return artistService.getArtistsNotTakingPartInPeriod(periodDTO.getFrom(), periodDTO.getTo());
    }

    @PostMapping(value = "/artistEvent/create/{art_id}/{event_id}")
    public void bindArtistToEvent(
        @PathVariable("art_id") Integer artistId,
        @PathVariable("event_id") Integer eventId
    ) {
        artistService.bindArtistToEvent(artistId, eventId);
    }

    @PostMapping(value = "/artistEvent/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistEventProjection> getEventsOfArtist(@PathVariable("id") Integer artistId) {
        return artistService.getEventsOfArtist(artistId);
    }

    @PostMapping(value = "/artistEvent/delete/{art_id}/{event_id}")
    public void deleteEventOfArtist(
        @PathVariable("art_id") Integer artistId,
        @PathVariable("event_id") Integer eventId
    ) {
        artistService.deleteEventOfArtist(artistId, eventId);
    }

    @PostMapping(value = "/artistsTogether",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistsTogetherProjection> getArtistsTakingPartTogether() {
        return artistService.getArtistsTakingPartTogether();
    }

}
