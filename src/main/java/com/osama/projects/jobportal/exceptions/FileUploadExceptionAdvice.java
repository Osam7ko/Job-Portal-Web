package com.osama.projects.jobportal.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSize(MaxUploadSizeExceededException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("message", "File too large. Max allowed is 10MB per file.");
        // Change the redirect target to your form page
        return "redirect:/recruiter-profile/edit";
    }
}