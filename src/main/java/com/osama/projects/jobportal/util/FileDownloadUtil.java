package com.osama.projects.jobportal.util;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {

    public Resource getFileAsResource(String downloadDir, String fileName) throws IOException {
        Path baseDir = Paths.get(downloadDir).toAbsolutePath().normalize();
        if (!Files.exists(baseDir)) return null;

        // Prevent path traversal
        Path filePath = baseDir.resolve(fileName).normalize();
        if (!filePath.startsWith(baseDir)) {
            throw new SecurityException("Invalid path");
        }

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            return null;
        }
        return new UrlResource(filePath.toUri());
    }
}