package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.services.QrCodeGeneratorService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/principal")
@RequiredArgsConstructor
public class PrincipalController {

    private final QrCodeGeneratorService qrCodeService;

    @GetMapping("generate-qr-url")
    public ResponseEntity<String> generateQrUrl() {
        String qrCodeUrl = qrCodeService.generateQRUrl();
        return ResponseEntity.ok(qrCodeUrl);
    }
}
