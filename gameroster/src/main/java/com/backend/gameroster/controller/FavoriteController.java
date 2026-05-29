package com.backend.gameroster.controller;

import com.backend.gameroster.dto.favorite.FavoriteCreateDTO;
import com.backend.gameroster.dto.favorite.FavoriteDTO;
import com.backend.gameroster.services.favorite.IFavoriteService;

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
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Gestión de equipos favoritos de los usuarios")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final IFavoriteService favoriteService;

    public FavoriteController(IFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Obtener todos los favoritos", description = "Devuelve todos los registros de favoritos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favoritos obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> findAll() {
        return ResponseEntity.ok(favoriteService.findAll());
    }

    @Operation(summary = "Obtener favorito por ID", description = "Devuelve un favorito concreto usando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorito encontrado"),
            @ApiResponse(responseCode = "404", description = "Favorito no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDTO> findById(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(favoriteService.findById(id));
    }

    @Operation(summary = "Obtener equipos favoritos de un usuario", description = "Devuelve los equipos marcados como favoritos por un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipos favoritos obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDTO>> findFavoriteTeamsByUserId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(favoriteService.findFavoriteTeamsByUserId(userId));
    }

    @Operation(summary = "Crear favorito", description = "Añade un equipo a la lista de favoritos de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Favorito creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario o equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<FavoriteDTO> save(
            @Valid @RequestBody FavoriteCreateDTO favoriteCreateDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteService.save(favoriteCreateDTO));
    }

    @Operation(summary = "Actualizar favorito", description = "Actualiza un registro de favorito existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorito actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Favorito, usuario o equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FavoriteDTO> update(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody FavoriteCreateDTO favoriteCreateDTO
    ) {
        return ResponseEntity.ok(favoriteService.update(id, favoriteCreateDTO));
    }

    @Operation(summary = "Eliminar favorito", description = "Elimina un favorito por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Favorito eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Favorito no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del favorito", example = "1")
            @PathVariable Long id
    ) {
        favoriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}