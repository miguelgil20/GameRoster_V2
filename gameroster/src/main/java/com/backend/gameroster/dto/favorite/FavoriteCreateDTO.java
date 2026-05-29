package com.backend.gameroster.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteCreateDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id del equipo es obligatorio")
    private Long teamId;
}
