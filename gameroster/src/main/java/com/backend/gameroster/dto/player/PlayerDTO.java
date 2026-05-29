package com.backend.gameroster.dto.player;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerDTO {

    private Long id;

    private String nickname;

    private String name;

    private String role;

    private String country;

    private LocalDateTime createdAt;

    private Long teamId;

    private String teamName;
}
