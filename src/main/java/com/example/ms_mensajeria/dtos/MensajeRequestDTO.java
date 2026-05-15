package com.example.ms_mensajeria.dtos;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensajeRequestDTO {

    @NotBlank(message = " El cuerpo del mensaje no puede esdtar vacío")
    private String cuerpoMensaje;

    @NotNull(message = "El ID del remitente es obligatorio  ")
    private Long idRemitente;

    @NotNull(message = "El ID del destinatario es obligatorio")
    private Long idDestinatario;

    private String hiloConversacion;
    private MultipartFile archivo; // Para adjuntar un archivo al mensaje (opcional)

}
