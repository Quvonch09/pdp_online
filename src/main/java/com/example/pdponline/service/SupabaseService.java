package com.example.pdponline.service;

import com.example.pdponline.exception.RestException;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api_key}")
    private String supabaseApiKey;

    @Value("${supabase.bucket_name}")
    private String bucketName;

    private final RestTemplate restTemplate;


    @Async
    @CacheEvict(value = "files", allEntries = true)
    public CompletableFuture<String> uploadFile(MultipartFile file, String fileName) throws IOException {
        String uniqueName = LocalDateTime.now() + "_" + fileName;
        String filePath = "uploads/" + uniqueName;

        String uploadUrl = String.format("%s/storage/v1/object/%s/%s", supabaseUrl, bucketName, filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
        headers.set("Authorization", "Bearer " + supabaseApiKey);

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl, HttpMethod.POST, entity, String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return CompletableFuture.completedFuture(supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filePath);
        } else {
            throw RestException.restThrow("Fayl yuklashda xatolik: " + response.getStatusCode());
        }
    }




    @CacheEvict(value = "files", allEntries = true)
    public CompletableFuture<String> updateFile(String oldUrl, MultipartFile newFile) {
        try {
            String basePublicUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/";
            if (!oldUrl.startsWith(basePublicUrl)) {
                throw new IllegalArgumentException("Invalid Supabase URL");
            }

            if (!deleteFile(oldUrl)) {
                return CompletableFuture.completedFuture("false");
            }


            return uploadFile(newFile, newFile.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("false");
        }
    }


    public boolean deleteFile(String fileUrl) {
        try {
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);

            String[] parts = decodedUrl.split("/storage/v1/object/public/");
            if (parts.length != 2) {
                System.err.println("Noto‘g‘ri fayl URL: " + fileUrl);
                return false;
            }

            String fullPath = parts[1]; // ex: media/folder/image.jpg
            int slashIndex = fullPath.indexOf('/');
            if (slashIndex == -1) {
                System.err.println("Bucket yoki fayl yo‘li noto‘g‘ri: " + fullPath);
                return false;
            }

            String bucket = fullPath.substring(0, slashIndex); // media
            String filePath = fullPath.substring(slashIndex + 1); // folder/image.jpg

            String apiUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filePath + "/remove";

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("apikey", supabaseApiKey);
                httpPost.setHeader("Authorization", "Bearer " + supabaseApiKey);
                httpPost.setHeader("Content-Type", "application/json");

                JSONArray fileArray = new JSONArray();
                fileArray.put(filePath); // kerakli narsa

                StringEntity entity = new StringEntity(fileArray.toString(), ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    int statusCode = response.getCode();
                    if (statusCode == 200) {
                        System.out.println("✅ Fayl Supabasedan o‘chirildi.");
                        return true;
                    } else {
                        System.out.println("❌ Status: " + statusCode);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("Supabase javobi: " + line);
                        }
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

