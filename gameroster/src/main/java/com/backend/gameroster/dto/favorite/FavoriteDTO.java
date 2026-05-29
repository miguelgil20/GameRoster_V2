package com.backend.gameroster.dto.favorite;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteDTO {

    private Long id;

    private Long userId;

    private Long teamId;

    private String teamName;

    private String region;

    private Integer ranking;
}