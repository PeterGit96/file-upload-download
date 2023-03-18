package co.develhope.fileuploaddownload.services;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    public String upload(@NotNull MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + "." + extension;

        File finalFolder = new File(fileRepositoryFolder);
        if(!finalFolder.exists()) {
            throw new IOException("The final folder doesn't exists");
        }
        if(!finalFolder.isDirectory()) {
            throw new IOException("The final folder isn't a directory");
        }

        File finalDestination = new File(fileRepositoryFolder + "\\" + newFileName);
        if(finalDestination.exists()) {
            throw new IOException("File conflict");
        }

        file.transferTo(finalDestination);
        return newFileName;
    }

    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepository.exists()) {
            throw new IOException("File doesn't exists");
        }
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }

}
