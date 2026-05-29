package com.backend.gameroster.dto.team;

import com.backend.gameroster.dto.player.PlayerDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TeamDTO {

    private Long id;

    private String name;

    private String region;

    private Integer ranking;

    private LocalDateTime createdAt;

    private List<PlayerDTO> players;
}
