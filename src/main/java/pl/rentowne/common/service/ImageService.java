package pl.rentowne.common.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * Serwis plików graficznych
 */
public interface ImageService {

    /**
     * Zapisuje plik graficzny na dysku
     * @param fileName nazwa pliku
     * @param inputStream plik
     * @return nazwa zapisanego pliku
     */
    String uploadImage(String fileName, InputStream inputStream);

    /**
     * Pobiera plik graficzny
     * @param filename nazwa pliku do pobrania
     * @return plik
     */
    Resource serveFiles(String filename);
}
