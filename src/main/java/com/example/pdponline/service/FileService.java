package com.example.pdponline.service;

import com.example.pdponline.entity.File;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.res.ResFile;
import com.example.pdponline.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository videoFileRepository;

    //local uchun
    private static final Path root = Paths.get("src/main/resources");
    //server uchun
//    private static final Path root= Paths.get("/root");


    public ApiResponse<Long> saveFile(MultipartFile file) {
        String director = checkingAttachmentType(file);


        long millis = System.currentTimeMillis();
        Path resolve = root.resolve(director + "/" + millis + "-" + file.getOriginalFilename());
        File files;
        try {
            Files.copy(file.getInputStream(), resolve, StandardCopyOption.REPLACE_EXISTING);
            File videoFile = new File();
            videoFile.setFileName(file.getOriginalFilename());
            videoFile.setFilepath(root.resolve(director + "/" + millis + "-" + file.getOriginalFilename()).toString());
            videoFile.setContentType(file.getContentType());
            videoFile.setSize(file.getSize());
            files = videoFileRepository.save(videoFile);
        } catch (IOException e) {
            throw RestException.restThrow(ResponseError.NOTFOUND(e.getMessage()));
        }
        return ApiResponse.successResponse(files.getId());
    }


    //  GetFile uchun
    public ResFile loadFileAsResource(Long id) {
        try {
            Optional<File> videoFileOptional = videoFileRepository.findById(id);
            if (videoFileOptional.isPresent()) {
                File videoFile = videoFileOptional.get();
                if (videoFile.getFilepath() == null || videoFile.getFileName() == null || videoFile.getContentType() == null) {
                    throw RestException.restThrow(ResponseError.DEFAULT_ERROR("File data is missing"));
                }
                java.io.File file = new java.io.File(videoFile.getFilepath());
                if (!file.exists()) {
                    throw RestException.restThrow(ResponseError.DEFAULT_ERROR("File not found"));
                }
                Resource resource = new UrlResource(file.toURI());
                ResFile resFile = new ResFile();
                resFile.setFillName(videoFile.getFileName());
                resFile.setResource(resource);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(videoFile.getContentType()));
                headers.setContentLength(videoFile.getSize());
                resFile.setHeaders(headers);
                return resFile;
            }
            throw RestException.restThrow(ResponseError.NOTFOUND("File not found"));
        } catch (IOException e) {
            throw RestException.restThrow(ResponseError.DEFAULT_ERROR(e.getMessage()));
        }
    }


    //update
    public ApiResponse<?> updateFile(Long id, MultipartFile file) {
        try {
            Optional<File> existingVideoFile = videoFileRepository.findById(id);
            if (existingVideoFile.isPresent()) {
                File videoFile = existingVideoFile.get();
                Path oldFilePath = Paths.get(videoFile.getFilepath());
                Files.deleteIfExists(oldFilePath);

                String filename = file.getOriginalFilename();
                String director = checkingAttachmentType(file);


                long millis = System.currentTimeMillis();
                Path uploadPath = root.resolve(director + "/" + millis + "-" + file.getOriginalFilename());
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                if (filename != null) {
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    videoFile.setFileName(filename);
                    videoFile.setFilepath(filePath.toString());
                    videoFile.setContentType(file.getContentType());

                    File file1;
                    file1 = videoFileRepository.save(videoFile);
                    return ApiResponse.successResponse(file1.getId());
                } else {
                    return ApiResponse.successResponse(ResponseError.NOTFOUND("File name"));
                }
            } else {
                throw RestException.restThrow(ResponseError.NOTFOUND("File"));
            }
        } catch (IOException e) {
            throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Fileni o'qishda xatolik"));
        }
    }


    //delete file
    public ApiResponse<String> deleteFile(Long id) {
        try {
            Optional<File> existingVideoFile = videoFileRepository.findById(id);
            if (existingVideoFile.isPresent()) {
                File videoFile = existingVideoFile.get();
                Path filePath = Paths.get(videoFile.getFilepath());
                Files.deleteIfExists(filePath);
                videoFileRepository.delete(videoFile);
                return ApiResponse.successResponse("Successfully deleted");
            } else {
                throw new IOException("File not found");
            }
        } catch (IOException e) {
            throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Fileni o'qishda xatolik"));
        }
    }


    public String checkingAttachmentType(MultipartFile file) {
        String filename = file.getOriginalFilename();

        assert filename != null;
        if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".webp")
            || filename.endsWith(".PNG") || filename.endsWith(".JPG") || filename.endsWith(".JPEG") || filename.endsWith(".WEBP")) {
            return "img";
        } else if (checkFile(filename)) {
            return "files";
        }
        throw RestException.restThrow(ResponseError.NOTFOUND("Fayl yuklash uchun papka"));
    }


    public boolean checkFile(String filename) {
        return filename.endsWith(".pdf") || filename.endsWith(".docx") ||
               filename.endsWith(".pptx") || filename.endsWith(".zip") ||
               filename.endsWith(".PDF") || filename.endsWith(".DOCX") ||
               filename.endsWith(".PPTX") || filename.endsWith(".ZIP");
    }
}