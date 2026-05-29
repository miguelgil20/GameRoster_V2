package com.backend.gameroster.dto.role;

import com.backend.gameroster.enums.RoleType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleDTO {

    private Long id;

    private RoleType rol;
}
