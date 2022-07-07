package ru.nsu.pashentsev.db.artist.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.artist.dto.ArtistImpresarioJenreDto;
import ru.nsu.pashentsev.db.artist.projections.ArtistEventProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistImpresarioJenreProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistProjection;
import ru.nsu.pashentsev.db.artist.projections.ArtistsTogetherProjection;
import ru.nsu.pashentsev.db.artist.projections.rowmappers.ArtistEventProjectionRowMapper;
import ru.nsu.pashentsev.db.artist.projections.rowmappers.ArtistImpresarioJenreProjectionRowMapper;
import ru.nsu.pashentsev.db.artist.projections.rowmappers.ArtistProjectionRowMapper;
import ru.nsu.pashentsev.db.artist.projections.rowmappers.ArtistsTogetherProjectionRowMapper;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository
public class ArtistJDBCRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertArtistWithImpresarioInJenre(ArtistImpresarioJenreDto artistImpresarioJenreDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO impresario_artist_jenre(impresario, artist, jenre) VALUES (?, ?, ?)"
            );

            preparedStatement.setInt(1, artistImpresarioJenreDto.getImpresarioId());
            preparedStatement.setInt(2, artistImpresarioJenreDto.getArtistId());
            preparedStatement.setInt(3, artistImpresarioJenreDto.getJenreId());

            return preparedStatement;
        }, keyHolder);
    }

    public List<ArtistImpresarioJenreProjection> getArtistsWithImpresariosInJenre() {
        return jdbcTemplate.query(
            "SELECT a.id as artistId, a.name AS artistName, " +
                "a.surname AS artistSurname, " +
                "a.birthDate AS artistDate, " +
                "i.id as impresarioId, i.name AS impresarioName, " +
                "i.surname AS impresarioSurname, " +
                "i.birthDate AS impresarioDate, " +
                "j.id as jenreId, j.name AS jenreName " +
                "FROM impresario_artist_jenre iaj " +
                "INNER JOIN artist a on iaj.artist = a.id " +
                "INNER JOIN impresario i on i.id = iaj.impresario " +
                "INNER JOIN jenre j on j.id = iaj.jenre ",
            new ArtistImpresarioJenreProjectionRowMapper()
        );
    }

    //Check for trigger working
    public void deleteArtistWithImpresarioInJenre(Integer impresarioId, Integer artistId, Integer jenreId) {
        jdbcTemplate.update(
            "DELETE FROM impresario_artist_jenre " +
                "WHERE impresario = ? AND artist = ? AND jenre = ?",
            impresarioId,
            artistId,
            jenreId
        );
    }

    //2
    public List<ArtistProjection> getArtistsInJenre(Integer jenreId) {
        return jdbcTemplate.query(
            "SELECT DISTINCT ON(iaj.artist, iaj.jenre) a.name AS artistName, " +
                "a.surname AS artistSurname, " +
                "a.birthDate AS artistDate " +
                "FROM artist a " +
                "INNER JOIN impresario_artist_jenre iaj on a.id = iaj.artist " +
                "WHERE iaj.jenre = ?",
            new ArtistProjectionRowMapper(),
            jenreId
        );
    }

    //10
    public List<ArtistProjection> getArtistsNotTakingPartInPeriod(Date from, Date to) {
        return jdbcTemplate.query(
            "SELECT a.name as artistName, " +
                "a.surname as artistSurname, " +
                "a.birthdate as artistDate " +
                "FROM artist a INNER JOIN " +
                "(SELECT foundArtistId, commonArtistId FROM " +
                "(SELECT x.id AS foundArtistId, y.id AS commonArtistId FROM " +
                "(SELECT a.id FROM artist a " +
                "INNER JOIN artist_event ae on a.id = ae.artist " +
                "INNER JOIN event e on e.id = ae.event " +
                "WHERE e.eventdate BETWEEN ? AND ?" +
                ") AS x " +
                "RIGHT JOIN " +
                "(SELECT * FROM artist a) AS y ON x.id = y.id) AS lateralJoined " +
                "WHERE foundArtistId IS NULL) AS resultTable ON a.id = resultTable.commonArtistId",
            new ArtistProjectionRowMapper(),
            from,
            to
        );
    }

    public void bindArtistToEvent(Integer artistId, Integer eventId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO artist_event(artist, event) VALUES (?, ?)"
            );

            preparedStatement.setInt(1, artistId);
            preparedStatement.setInt(2, eventId);

            return preparedStatement;
        }, keyHolder);
    }

    public List<ArtistEventProjection> getEventsOfArtist(Integer artistId) {
        return jdbcTemplate.query(
            "SELECT a.id AS artistId, a.name AS artistName, " +
                "a.surname AS artistSurname, " +
                "a.birthDate AS artistDate, " +
                "e.id AS eventId, e.name AS eventName, " +
                "et.eventtype AS eventType, " +
                "b.name AS eventPlace, " +
                "e.eventdate AS eventDate " +
                "FROM artist_event ae " +
                "INNER JOIN artist a on ae.artist = a.id " +
                "INNER JOIN event e on e.id = ae.event " +
                "INNER JOIN building b on b.id = e.eventplace " +
                "INNER JOIN event_type et on et.id = e.eventtype " +
                "WHERE a.id = ? " +
                "ORDER BY (ae.artist, ae.event)",
            new ArtistEventProjectionRowMapper(),
            artistId
        );
    }

    public void deleteEventOfArtist(Integer artistId, Integer eventId) {
        jdbcTemplate.update(
            "DELETE FROM artist_event " +
                "WHERE artist = ? AND event = ?",
            artistId,
            eventId
        );
    }

    public List<ArtistsTogetherProjection> getArtistsTakingPartTogether() {
        return jdbcTemplate.query(
            "WITH artist_cartesian AS ( " +
                "SELECT a1.id as firstArtistId, " +
                "a1.name as firstArtistName, " +
                "a1.surname  as firstArtistSurname, " +
                "a1.birthdate as firstArtistDate, " +
                "a2.id as secondArtistId, " +
                "a2.name as secondArtistName, " +
                "a2.surname as secondArtistSurname, " +
                "a2.birthdate as secondArtistDate " +
                "FROM artist a1, artist a2 " +
                "WHERE a1.id < a2.id) " +
                "SELECT * " +
                "FROM artist_cartesian " +
                "INNER JOIN LATERAL " +
                "(SELECT count(*) as counter FROM " +
                "(SELECT ae.event as firstEvent " +
                "FROM artist_event ae " +
                "WHERE ae.artist = artist_cartesian.firstArtistId " +
                "INTERSECT " +
                "SELECT ae.event as firstEvent " +
                "FROM artist_event ae " +
                "WHERE ae.artist = artist_cartesian.secondArtistId " +
                ") as intersection) cnt ON TRUE " +
                "WHERE counter > 0",
            new ArtistsTogetherProjectionRowMapper()
        );
    }

}
