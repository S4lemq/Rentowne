package pl.rentowne.apartment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.rentowne.common.service.ImageService;
import pl.rentowne.util.ExistingFileRenameUtils;
import pl.rentowne.util.UploadedFilesNameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${application.uploadDir}")
    private String uploadDir;

    public String uploadImage(String filename, InputStream inputStream) {
        createDirectoryIfNotExists(uploadDir);

        String newFileName = UploadedFilesNameUtils.slugifyFileName(filename);
        newFileName = ExistingFileRenameUtils.renameIfExists(Path.of(uploadDir), newFileName);
        Path filePath = Paths.get(uploadDir).resolve(newFileName);

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Nie mogę zapisać pliku", e);
        }
        return newFileName;
    }

    public Resource serveFiles(String filename) {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        return fileSystemResourceLoader.getResource(uploadDir + filename);
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Nie udało się stworzyć katalogu: " + directoryPath, e);
            }
        }
    }

}
