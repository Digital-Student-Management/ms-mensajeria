package com.example.ms_mensajeria.dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MensajeResponseDTO {
     private Long idMensaje;
    private String cuerpoMensaje;   
    private LocalDateTime fechaHoraEnvio;
    private String hiloConversacion;
    private Long idRemitente;
    private Long idDestinatario;
    private String urlArchivoAdjunto;

}
