package com.backend.gameroster.controller;

import com.backend.gameroster.dto.user.CreateUseroDTO;
import com.backend.gameroster.dto.user.LoginUserDTO;
import com.backend.gameroster.exception.CreateEntityException;
import com.backend.gameroster.exception.ErrorGenericoException;
import com.backend.gameroster.services.autentify.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro y login de usuarios con JWT")
public class AuthController {

    private final IAuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario en GameRoster con rol USER por defecto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario/email ya existente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody CreateUseroDTO dto
    ) {

        logger.info("PETICIÓN REGISTER usuario {}", dto.getNombreUsuario());

        try {

            authService.register(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Usuario registrado correctamente");

        } catch (ErrorGenericoException | CreateEntityException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new CreateEntityException(
                    "User",
                    dto,
                    ex
            );
        }
    }

    @Operation(
            summary = "Login de usuario",
            description = "Autentica un usuario y devuelve un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login correcto, devuelve token JWT"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginUserDTO dto
    ) {

        logger.info("PETICIÓN LOGIN usuario {}", dto.getNombreUsuario());

        try {

            String token = authService.login(dto);

            return ResponseEntity.ok(token);

        } catch (ErrorGenericoException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ErrorGenericoException(
                    "Error en autenticación",
                    ex
            );
        }
    }
}