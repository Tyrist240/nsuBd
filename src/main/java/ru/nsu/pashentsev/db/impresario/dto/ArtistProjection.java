package ru.nsu.pashentsev.db.impresario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.nsu.pashentsev.db.impresario.projection.ImpresarioArtistProjection;

@AllArgsConstructor
@Getter
@Setter
public class ArtistProjection {

    private String artistName;

    private String artistSurname;

    private String artistBirthDate;

    public ArtistProjection(ImpresarioArtistProjection impresarioArtistProjection) {
        this.artistName = impresarioArtistProjection.getArtistName();
        this.artistSurname = impresarioArtistProjection.getArtistSurname();
        this.artistBirthDate = impresarioArtistProjection.getArtistBirthDate();
    }

}
