package com.example.ms_mensajeria.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MensajeUpdateDTO {
    @NotBlank(message = "El cuerpo del mensaje no puede estar vacío")
    private String cuerpoMensaje;
}