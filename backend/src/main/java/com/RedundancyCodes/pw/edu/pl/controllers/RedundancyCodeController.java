package com.RedundancyCodes.pw.edu.pl.controllers;

import com.RedundancyCodes.pw.edu.pl.domain.*;
import com.RedundancyCodes.pw.edu.pl.services.RedundancyCodeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class RedundancyCodeController {
    @Autowired
    private RedundancyCodeAPI redundancyCodeAPI;

    @GetMapping("/get-original-image")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getOrgImage() {
        InputStream in;
        try {
            in = redundancyCodeAPI.getOrgPicture();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/get-processed-image")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getProcImage() {
        InputStream in;
        try {
            in = redundancyCodeAPI.getProcPicture();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/get-chunk-info")
    @ResponseBody
    public ResponseEntity<?> getBitCount() {
        int bitCount = redundancyCodeAPI.getBitCount();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bitCount);
    }

    @GetMapping("/get-progress")
    @ResponseBody
    public ResponseEntity<?> getProgress() {
        Progress progress = redundancyCodeAPI.getProgress();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(progress);
    }

    @PostMapping("/get-result")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> getResult(@Valid @RequestBody ConfigurationDTO conf) {
        Result result = redundancyCodeAPI.getResult(conf);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (redundancyCodeAPI.setOrgPicture(file)) {
                return ResponseEntity.ok().build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.internalServerError().build();
    }
}
