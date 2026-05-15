package com.example.ms_mensajeria.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajeria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje; 

    @Column(name= "cuerpo_mensaje", nullable = false, length = 1000)
    private String cuerpoMensaje; 

    @Column(name = "fecha_hora_envio", nullable = false)
    private LocalDateTime fechaHoraEnvio;

    @Column(name = "hilo_conversacion", length = 50)
    private String hiloConversacion;

    @Column(name = " id_remitente", nullable = false)
    private Long idRemitente;

    @Column(name = "id_destinatario", nullable = false)
    private Long idDestinatario;

    @Column(name= "ruta_archivo")
    private String rutaArchivo;

}
