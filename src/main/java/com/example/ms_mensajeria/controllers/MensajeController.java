package com.example.ms_mensajeria.controllers;


import com.example.ms_mensajeria.dtos.MensajeRequestDTO;
import com.example.ms_mensajeria.dtos.MensajeResponseDTO;
import com.example.ms_mensajeria.dtos.MensajeUpdateDTO;
import com.example.ms_mensajeria.services.MensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
@Tag(name = "Mensajería" , description = "Operaciones Crud para la gestion de mensajes")


public class MensajeController {

    private final MensajeService mensajeService;
    @PostMapping( value = "/enviar" , consumes = "multipart/form-data")
    @Operation(summary = "Enviar un mensaje" , description = "Crea un nuevo mensaje opcionalmente con un archivo adjunto")
    public ResponseEntity<MensajeResponseDTO> enviarMensaje(@Valid @ModelAttribute MensajeRequestDTO request) {
        return ResponseEntity.ok(mensajeService.enviarMensaje(request));
    }

    @GetMapping("/bandeja/{idDestinatario}")
    @Operation(summary = "Ver bandeja de entrada" , description = "Listar los mensajes recibidos por un usuario")
    public ResponseEntity<List<MensajeResponseDTO>> verBandeja(@PathVariable Long idDestinatario) {
        return ResponseEntity.ok(mensajeService.obtenerBandeja(idDestinatario));
    }

    @GetMapping("/{idMensaje}")
    @Operation(summary = "Obtener Mensaje" , description = "Obtiene los detalles de un mensaje especifico por su ID")
    public ResponseEntity<MensajeResponseDTO> obtenerMensajePorId(@PathVariable Long idMensaje) {
        return ResponseEntity.ok(mensajeService.obtenerMensaje(idMensaje));
    }

    @PutMapping("/{idMensaje}")
    @Operation(summary = "Editar Mensaje" , description = "Modifica el texto de un mensaje existente")
    public ResponseEntity <MensajeResponseDTO> editarMensaje(@PathVariable Long idMensaje, @Valid @RequestBody MensajeUpdateDTO request) {
        return ResponseEntity.ok(mensajeService.editarMensaje( idMensaje , request));
    }

    @DeleteMapping("/{idMensaje}")
    @Operation (summary = "Eliminar Mensaje" , description = "Borra un mensaje del sistema de forma permanente")
    public ResponseEntity<String> eliminarMensaje(@PathVariable Long idMensaje) {
        mensajeService.eliminarMensaje(idMensaje);
        return ResponseEntity.ok("Mensaje Eliminado Correctamente"); 

    }
}
