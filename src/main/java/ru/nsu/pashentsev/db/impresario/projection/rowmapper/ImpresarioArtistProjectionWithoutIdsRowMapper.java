package ru.nsu.pashentsev.db.impresario.projection.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ru.nsu.pashentsev.db.common.utils.DateTimeFormatter;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjectionWithoutIds;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImpresarioArtistProjectionWithoutIdsRowMapper implements RowMapper<ImpresarioArtistProjectionWithoutIds> {

    @Nullable
    @Override
    public ImpresarioArtistProjectionWithoutIds mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ImpresarioArtistProjectionWithoutIds(
            rs.getString("impresarioName"),
            rs.getString("impresarioSurname"),
            DateTimeFormatter.getFormattedDateFromTimestamp(rs.getDate("impresarioDate")),
            rs.getString("artistName"),
            rs.getString("artistSurname"),
            DateTimeFormatter.getFormattedDateFromTimestamp(rs.getDate("artistDate"))
        );
    }

}
