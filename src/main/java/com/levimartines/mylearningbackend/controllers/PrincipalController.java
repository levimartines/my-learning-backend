package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.dtos.UserDTO;
import com.levimartines.mylearningbackend.security.PrincipalService;
import com.levimartines.mylearningbackend.services.QrCodeGeneratorService;
import com.levimartines.mylearningbackend.services.UserService;

import java.util.Base64;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/principal")
@RequiredArgsConstructor
public class PrincipalController {

    private final QrCodeGeneratorService qrCodeService;
    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> findMe() {
        return ResponseEntity.ok(mapper.map(PrincipalService.getUser(), UserDTO.class));
    }

    @PutMapping("/mfa")
    public ResponseEntity<Void> useMFA(boolean useMFA) {
        Long loggedUser = PrincipalService.getUserId();
        userService.setUseMFA(loggedUser, useMFA);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("generate-qr-url")
    public ResponseEntity<String> generateQrUrl() {
        byte[] qrCode = qrCodeService.generateQrCode();
        String base64 = Base64.getEncoder().encodeToString(qrCode);
        return ResponseEntity.ok(base64);
    }
}
