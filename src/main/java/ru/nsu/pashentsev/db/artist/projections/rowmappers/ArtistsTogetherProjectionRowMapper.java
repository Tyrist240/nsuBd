package ru.nsu.pashentsev.db.artist.projections.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ru.nsu.pashentsev.db.artist.projections.ArtistsTogetherProjection;
import ru.nsu.pashentsev.db.common.utils.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistsTogetherProjectionRowMapper implements RowMapper<ArtistsTogetherProjection> {

    @Nullable
    @Override
    public ArtistsTogetherProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ArtistsTogetherProjection(
            rs.getString("firstArtistName"),
            rs.getString("firstArtistSurname"),
            DateTimeFormatter.getFormattedDateFromTimestamp(rs.getDate("firstArtistDate")),
            rs.getString("secondArtistName"),
            rs.getString("secondArtistSurname"),
            DateTimeFormatter.getFormattedDateFromTimestamp(rs.getDate("secondArtistDate"))
        );
    }

}
