package ru.nsu.pashentsev.db.impresario.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjection;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjectionWithoutIds;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioProjection;
import ru.nsu.pashentsev.db.impresario.projection.rowmapper.ImpresarioArtistProjectionRowMapper;
import ru.nsu.pashentsev.db.impresario.projection.rowmapper.ImpresarioArtistProjectionWithoutIdsRowMapper;
import ru.nsu.pashentsev.db.impresario.projection.rowmapper.ImpresarioProjectionRowMapper;

import java.util.List;

@Repository
public class ImpresarioJDBCRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //3
    public List<ImpresarioArtistProjectionWithoutIds> getArtistsWithImpresarioInMultipleJenres(Integer impresarioId) {
        return jdbcTemplate.query(
            "SELECT artistName, artistSurname, artistDate, impresarioName, impresarioSurname, impresarioDate " +
                "FROM " +
                "(SELECT DISTINCT ON (a.id, i.id) a.id, a.name AS artistName, " +
                "a.surname AS artistSurname, " +
                "a.birthDate AS artistDate, " +
                "i.id, i.name AS impresarioName, " +
                "i.surname AS impresarioSurname, " +
                "i.birthDate AS impresarioDate " +
                "FROM impresario i " +
                "INNER JOIN impresario_artist_jenre iaj on i.id = iaj.impresario " +
                "INNER JOIN artist a on a.id = iaj.artist " +
                "INNER JOIN jenre j on j.id = iaj.jenre " +
                "WHERE i.id = ? " +
                "GROUP BY (a.id, a.name, a.surname, a.birthDate, " +
                "i.id, i.name, i.surname, i.birthDate) HAVING count(*) > 1) AS X " +
                "ORDER BY (artistName, artistSurname)",
            new ImpresarioArtistProjectionWithoutIdsRowMapper(),
            impresarioId
        );
    }

    //4
    public List<ImpresarioArtistProjection> getArtistsOfAllImpresarios(Integer impresarioId) {
        return jdbcTemplate.query(
            "SELECT i.id AS impresarioId, " +
                "i.name AS impresarioName, " +
                "i.surname AS impresarioSurname, " +
                "i.birthDate AS impresarioDate, " +
                "a.id AS artistId, " +
                "a.name AS artistName, " +
                "a.surname AS artistSurname, " +
                "a.birthDate AS artistDate " +
                "FROM impresario i " +
                "INNER JOIN (impresario_artist_jenre iaj " +
                "INNER JOIN artist a on a.id = iaj.artist " +
                "INNER JOIN jenre j on j.id = iaj.jenre) on i.id = iaj.impresario " +
                    "WHERE iaj.impresario = ? " +
                "ORDER BY (i.id, a.id)" ,
            new ImpresarioArtistProjectionRowMapper(),
                impresarioId
        );
    }

    //5
    public List<ImpresarioProjection> getImpresariosOfArtist(Integer artistId) {
        return jdbcTemplate.query(
            "SELECT DISTINCT ON(iaj.impresario, iaj.artist) i.name AS impresarioName, " +
                "i.surname AS impresarioSurname, " +
                "i.birthDate AS impresarioDate " +
                "FROM impresario i " +
                "INNER JOIN impresario_artist_jenre iaj on i.id = iaj.impresario " +
                "WHERE iaj.artist = ?",
            new ImpresarioProjectionRowMapper(),
            artistId
        );
    }

    //9
    public List<ImpresarioProjection> getImpresariosInJenre(Integer jenreId) {
        return jdbcTemplate.query(
            "SELECT DISTINCT ON(iaj.impresario, iaj.jenre) i.name AS impresarioName, " +
                "i.surname AS impresarioSurname, " +
                "i.birthDate AS impresarioDate " +
                "FROM impresario i " +
                "INNER JOIN impresario_artist_jenre iaj on i.id = iaj.impresario " +
                "WHERE iaj.jenre = ?",
            new ImpresarioProjectionRowMapper(),
            jenreId
        );
    }

}
