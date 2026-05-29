package com.backend.gameroster.controller;

import com.backend.gameroster.dto.team.TeamDTO;
import com.backend.gameroster.services.team.ITeamService;

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
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Gestión de equipos competitivos de Valorant")
@SecurityRequirement(name = "bearerAuth")
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(summary = "Obtener todos los equipos", description = "Devuelve una lista de todos los equipos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipos obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<TeamDTO>> findAll() {
        return ResponseEntity.ok(teamService.findAll());
    }

    @Operation(summary = "Obtener equipo por ID", description = "Devuelve un equipo concreto usando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findById(
            @Parameter(description = "ID del equipo", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(teamService.findById(id));
    }

    @Operation(summary = "Crear equipo", description = "Crea un nuevo equipo competitivo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<TeamDTO> save(
            @Valid @RequestBody TeamDTO teamDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teamService.save(teamDTO));
    }

    @Operation(summary = "Actualizar equipo", description = "Actualiza los datos de un equipo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(
            @Parameter(description = "ID del equipo", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TeamDTO teamDTO
    ) {
        return ResponseEntity.ok(teamService.update(id, teamDTO));
    }

    @Operation(summary = "Eliminar equipo", description = "Elimina un equipo por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Equipo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del equipo", example = "1")
            @PathVariable Long id
    ) {
        teamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}