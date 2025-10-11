package com.osama.projects.jobportal.controller;

import com.osama.projects.jobportal.util.FileDownloadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class CandidateFilesController {

    private final FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();

    // Resume (PDF) – you save to: docs/candidate/{userId}
    @GetMapping("/download/resume")
    public ResponseEntity<?> downloadResume(@RequestParam("userId") String userId,
                                            @RequestParam("fileName") String fileName) {
        return serveFrom("docs/candidate/" + userId, fileName, false);
    }

    // Experience proofs (image/PDF) – you save to: docs/candidate/{userId}/experience
    @GetMapping("/download/experience")
    public ResponseEntity<?> downloadExperience(@RequestParam("userId") String userId,
                                                @RequestParam("fileName") String fileName) {
        return serveFrom("docs/candidate/" + userId + "/experience", fileName, false);
    }

    // (Optional) profile photo if you want to let recruiters download it
    @GetMapping("/download/photo")
    public ResponseEntity<?> downloadPhoto(@RequestParam("userId") String userId,
                                           @RequestParam("fileName") String fileName) {
        return serveFrom("photos/candidate/" + userId, fileName, true); // inline for images
    }

    private ResponseEntity<?> serveFrom(String dir, String fileName, boolean inline) {
        if (!StringUtils.hasText(fileName)) return ResponseEntity.badRequest().build();
        try {
            Resource resource = fileDownloadUtil.getFileAsResource(dir, fileName);
            if (resource == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");

            String contentType;
            try {
                Path p = Paths.get(resource.getURI());
                contentType = Files.probeContentType(p);
            } catch (Exception e) {
                contentType = "application/octet-stream";
            }
            if (!StringUtils.hasText(contentType)) contentType = "application/octet-stream";

            String disp = (inline ? "inline" : "attachment")
                    + "; filename=\"" + UriUtils.encodePath(resource.getFilename(), StandardCharsets.UTF_8) + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disp)
                    .cacheControl(CacheControl.noCache())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}