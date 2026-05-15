package com.example.ms_mensajeria.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


@Service
public class FileStorageService {
    private final Path ubicacionArchivos = Paths.get("archivos_adjuntos").toAbsolutePath().normalize();

    public FileStorageService(){ 
        try{
            Files.createDirectories(this.ubicacionArchivos); 

        }catch (Exception ex){ 
            throw new RuntimeException("No se pudo crear el directorio de archivos.",ex);
        }
    }

    public String guardarArchivoLocal(MultipartFile archivo){ 
        if (archivo == null || archivo.isEmpty()) return null; 
        try{
            String nombreUnico = UUID.randomUUID().toString() + "-" + archivo.getOriginalFilename();
            Path destino = this.ubicacionArchivos.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nombreUnico;
        }catch (IOException ex){
            throw new RuntimeException("Fallo al guardar el archivo.", ex);
        }
    }

}
