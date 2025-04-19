package com.example.pdponline.controller;

import com.example.pdponline.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final SupabaseService supabaseService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = supabaseService.uploadFile(file, file.getOriginalFilename());
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xatolik: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String path) {
        boolean success = supabaseService.deleteFile(path);

        if (success) {
            return ResponseEntity.ok("Fayl o‘chirildi.");
        } else {
            return ResponseEntity.status(500).body("O‘chirishda xatolik yuz berdi.");
        }
    }



    @PutMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<String> update(@RequestParam("file") MultipartFile file, @RequestParam String path) {

        try {
            String fileUrl = supabaseService.updateFile(path, file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xatolik: " + e.getMessage());
        }
    }

}
