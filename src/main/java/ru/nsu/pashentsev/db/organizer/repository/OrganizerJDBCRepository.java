package ru.nsu.pashentsev.db.organizer.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.organizer.projection.OrganizerEventProjection;
import ru.nsu.pashentsev.db.organizer.projection.OrganizerWithEventCountProjection;
import ru.nsu.pashentsev.db.organizer.projection.rowmapper.OrganizerEventProjectionRowMapper;
import ru.nsu.pashentsev.db.organizer.projection.rowmapper.OrganizerWithEventCountProjectionRowMapper;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository
public class OrganizerJDBCRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //11
    public List<OrganizerWithEventCountProjection> getOrganizersWithEventCountsInPeriod(Date from, Date to) {
        return jdbcTemplate.query(
            "SELECT DISTINCT ON (x.id) x.name AS organizerName, " +
                "x.surname AS organizerSurname, " +
                "x.birthdate AS organizerBirthDate, " +
                "count(*) OVER (PARTITION BY x.id) AS eventCount " +
                "FROM " +
                "(SELECT o.* " +
                "FROM organizer o " +
                "INNER JOIN organizer_event oe on o.id = oe.organizer " +
                "INNER JOIN event e on e.id = oe.event " +
                "WHERE e.eventdate BETWEEN ? AND ?) AS x ",
            new OrganizerWithEventCountProjectionRowMapper(),
            from,
            to
        );
    }

    public void bindOrganizerToEvent(Integer organizerId, Integer eventId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO organizer_event(organizer, event) VALUES (?, ?)"
            );

            preparedStatement.setInt(1, organizerId);
            preparedStatement.setInt(2, eventId);

            return preparedStatement;
        }, keyHolder);
    }

    public List<OrganizerEventProjection> getEventsOfOrganizer(Integer organizerId) {
        return jdbcTemplate.query(
            "SELECT " +
                "o.id AS organizerId, " +
                "o.name AS organizerName, " +
                "o.surname AS organizerSurname, " +
                "o.birthDate AS organizerDate, " +
                "e.id AS eventId, " +
                "e.name AS eventName, " +
                "et.eventtype AS eventType, " +
                "b.name AS eventPlace, " +
                "e.eventdate AS eventDate " +
                "FROM organizer_event oe " +
                "INNER JOIN organizer o on oe.organizer = o.id " +
                "INNER JOIN event e on e.id = oe.event " +
                "INNER JOIN building b on b.id = e.eventplace " +
                "INNER JOIN event_type et on et.id = e.eventtype " +
                "WHERE o.id = ? " +
                "ORDER BY (oe.organizer, oe.event)",
            new OrganizerEventProjectionRowMapper(),
            organizerId
        );
    }

    public void deleteEventOfOrganizer(Integer organizerId, Integer eventId) {
        jdbcTemplate.update(
            "DELETE FROM organizer_event " +
                "WHERE organizer = ? AND event = ?",
            organizerId,
            eventId
        );
    }

    public List<OrganizerEventProjection> getEventsOfAllOrganizers() {
        return jdbcTemplate.query(
            "SELECT o.id AS organizerId, o.name AS organizerName, " +
                "o.surname AS organizerSurname, " +
                "o.birthDate AS organizerDate, " +
                "x.event AS eventId," +
                "x.eventName AS eventName, " +
                "x.eventType AS eventType, " +
                "x.eventPlace AS eventPlace, " +
                "x.eventDate AS eventDate " +
                "FROM organizer o " +
                "INNER JOIN LATERAL " +
                "(SELECT oe.organizer as organizer, oe.event as event, " +
                "e.name as eventName, " +
                "et.eventtype as eventType, " +
                "b.name AS eventPlace, " +
                "e.eventDate as eventDate " +
                "FROM organizer_event oe " +
                "INNER JOIN event e on oe.event = e.id INNER JOIN event_type et on e.eventtype = et.id " +
                "INNER JOIN building b ON e.eventplace = b.id WHERE o.id = oe.organizer) AS x " +
                "ON TRUE " +
                "ORDER BY (x.organizer, x.event)",
            new OrganizerEventProjectionRowMapper()
        );
    }

}
