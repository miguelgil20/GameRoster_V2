package com.backend.gameroster.services.autentify;

import com.backend.gameroster.dto.user.CreateUseroDTO;
import com.backend.gameroster.dto.user.LoginUserDTO;


public interface IAuthService {

    void register(CreateUseroDTO dto);

    String login(LoginUserDTO dto);
}