package ru.nsu.pashentsev.db.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


@Data
@JsonDeserialize(builder = UserIdDTO.UserIdDTOBuilder.class)
@Builder
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
public class UserIdDTO {

    @NonNull
    @JsonProperty("id")
    private Integer id;

    @NonNull
    @JsonProperty("login")
    private String login;

    @NonNull
    @JsonProperty("password")
    private String password;

    @Nullable
    @JsonProperty("userRole")
    private UserRole userRole;
}
