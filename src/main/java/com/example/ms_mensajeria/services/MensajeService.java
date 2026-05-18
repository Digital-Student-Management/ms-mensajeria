package com.example.ms_mensajeria.services;
import com.example.ms_mensajeria.dtos.MensajeRequestDTO;
import com.example.ms_mensajeria.dtos.MensajeResponseDTO;
import com.example.ms_mensajeria.dtos.MensajeUpdateDTO;
import com.example.ms_mensajeria.models.Mensaje;
import com.example.ms_mensajeria.repositories.MensajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio del Microservicio de Mensajería.
 * Implementa la comunicación con el MS de Usuarios para validación de identidad.
 */
@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final FileStorageService fileStorageService;
    private final RestTemplate restTemplate;
    
    // Ruta base al microservicio de usuarios
    private final String MS_USUARIOS_URL = "http://localhost:8080/api/usuarios/";

    //validar que el id del remitente y destinatario existan en el microservicio de usuarios...
    public MensajeResponseDTO enviarMensaje(MensajeRequestDTO request) {
        
        // 1. Validación de Integridad Cruzada (Remitente y Destinatario)
        validarUsuarioExistente(request.getIdRemitente(), "Remitente");
        validarUsuarioExistente(request.getIdDestinatario(), "Destinatario");

        // 2. Procesamiento del archivo adjunto (si existe)
        String rutaArchivo = null;
        if (request.getArchivo() != null && !request.getArchivo().isEmpty()) {
            rutaArchivo = fileStorageService.guardarArchivoLocal(request.getArchivo());
        }

        // 3. Creación de la entidad utilizando el patrón Builder de Lombok
        Mensaje nuevoMensaje = Mensaje.builder()
                .cuerpoMensaje(request.getCuerpoMensaje())
                .idRemitente(request.getIdRemitente())
                .idDestinatario(request.getIdDestinatario())
                .hiloConversacion(request.getHiloConversacion())
                .rutaArchivo(rutaArchivo)
                .fechaHoraEnvio(LocalDateTime.now())
                .build();

        // 4. Persistencia en MySQL local
        Mensaje mensajeGuardado = mensajeRepository.save(nuevoMensaje);
        
        return mapearAResponse(mensajeGuardado);
    }

    /**
     * Obtiene la bandeja de entrada de un usuario.
     */
    public List<MensajeResponseDTO> obtenerBandeja(Long idDestinatario) {
        List<Mensaje> mensajes = mensajeRepository.findByIdDestinatarioOrderByFechaHoraEnvioDesc(idDestinatario);
        return mensajes.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Recupera un mensaje específico por su identificador único.
     */
    public MensajeResponseDTO obtenerMensaje(Long idMensaje) {
        Mensaje mensaje = mensajeRepository.findById(idMensaje)
                .orElseThrow(() -> new RuntimeException("Mensaje con ID " + idMensaje + " no encontrado."));
        return mapearAResponse(mensaje);
    }

    /**
     * Actualiza el cuerpo de un mensaje enviado.
     */
    public MensajeResponseDTO editarMensaje(Long idMensaje, MensajeUpdateDTO request) {
        Mensaje mensajeExistente = mensajeRepository.findById(idMensaje)
                .orElseThrow(() -> new RuntimeException("No se encontró el mensaje para editar."));
        
        mensajeExistente.setCuerpoMensaje(request.getCuerpoMensaje());
        Mensaje mensajeActualizado = mensajeRepository.save(mensajeExistente);
        
        return mapearAResponse(mensajeActualizado);
    }

    /**
     * Elimina físicamente un registro de la base de datos de mensajería.
     */
    public void eliminarMensaje(Long idMensaje) {
        if (!mensajeRepository.existsById(idMensaje)) {
            throw new RuntimeException("El mensaje que intenta eliminar no existe.");
        }
        mensajeRepository.deleteById(idMensaje);
    }

    // ============================================================
    // MÉTODOS AUXILIARES DE VALIDACIÓN Y MAPEO
    // ============================================================

    /**
     * Verifica sincrónicamente que un ID exista en el MS-Usuarios.
     */
    private void validarUsuarioExistente(Long id, String rolParticipante) {
        try {
            restTemplate.getForObject(MS_USUARIOS_URL + id, java.util.Map.class);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Error de Integridad: El " + rolParticipante + " con ID " + id + " no existe en el sistema escolar.");
        } catch (Exception e) {
            throw new RuntimeException("Fallo en la comunicación con MS-Usuarios al validar el " + rolParticipante + ": " + e.getMessage());
        }
    }

    /**
     * Transforma la Entidad de BD a un DTO de respuesta.
     */
    private MensajeResponseDTO mapearAResponse(Mensaje mensaje) {
        return MensajeResponseDTO.builder()
                .idMensaje(mensaje.getIdMensaje())
                .cuerpoMensaje(mensaje.getCuerpoMensaje())
                .idRemitente(mensaje.getIdRemitente())
                .idDestinatario(mensaje.getIdDestinatario())
                .hiloConversacion(mensaje.getHiloConversacion())
                .fechaHoraEnvio(mensaje.getFechaHoraEnvio())
                .urlArchivoAdjunto(mensaje.getRutaArchivo() != null ? "/archivos/" + mensaje.getRutaArchivo() : null)
                .build();
    }
}


