package pl.rentowne.apartment.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * Serwis plików graficznych
 */
public interface ApartmentImageService {

    String uploadImage(String fileName, InputStream inputStream);

    Resource serveFiles(String filename);
}
