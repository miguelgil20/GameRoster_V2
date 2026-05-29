package com.backend.gameroster.controller;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;
import com.backend.gameroster.services.player.IPlayerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "Gestión de jugadores de equipos de Valorant")
@SecurityRequirement(name = "bearerAuth")
public class PlayerController {

    private final IPlayerService playerService;

    public PlayerController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Obtener todos los jugadores", description = "Devuelve una lista con todos los jugadores registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugadores obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PlayerDTO>> findAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @Operation(summary = "Obtener jugador por ID", description = "Devuelve un jugador concreto usando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador encontrado"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> findById(
            @Parameter(description = "ID del jugador", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @Operation(summary = "Crear jugador", description = "Crea un nuevo jugador y lo asigna a un equipo mediante teamId")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Jugador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PlayerDTO> save(
            @Valid @RequestBody PlayerCreateDTO playerCreateDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playerService.save(playerCreateDTO));
    }

    @Operation(summary = "Actualizar jugador", description = "Actualiza los datos de un jugador existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Jugador o equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> update(
            @Parameter(description = "ID del jugador", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PlayerCreateDTO playerCreateDTO
    ) {
        return ResponseEntity.ok(playerService.update(id, playerCreateDTO));
    }

    @Operation(summary = "Eliminar jugador", description = "Elimina un jugador por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Jugador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del jugador", example = "1")
            @PathVariable Long id
    ) {
        playerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}