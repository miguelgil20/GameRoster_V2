package com.backend.gameroster.dto.player;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerCreateDTO {

    private String nickname;

    private String name;

    private String role;

    private String country;

    private Long teamId;
}