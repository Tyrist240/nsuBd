package ru.nsu.pashentsev.db.artist.projections.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ru.nsu.pashentsev.db.artist.projections.ArtistProjection;
import ru.nsu.pashentsev.db.common.utils.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistProjectionRowMapper implements RowMapper<ArtistProjection> {

    @Nullable
    @Override
    public ArtistProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ArtistProjection(
            rs.getString("artistName"),
            rs.getString("artistSurname"),
            DateTimeFormatter.getFormattedDateFromTimestamp(rs.getDate("artistDate"))
        );
    }

}
