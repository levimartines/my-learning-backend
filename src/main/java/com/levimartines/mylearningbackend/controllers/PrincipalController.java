package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.dtos.UserDTO;
import com.levimartines.mylearningbackend.security.PrincipalService;
import com.levimartines.mylearningbackend.services.QrCodeGeneratorService;
import com.levimartines.mylearningbackend.services.UserService;

import java.net.URI;
import java.util.Base64;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.levimartines.mylearningbackend.utils.Base64Utils.encode;

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

    @GetMapping("/picture")
    public ResponseEntity<String> getProfilePicture() {
        byte[] picture = userService.getProfilePicture();
        return picture.length == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(encode(picture));
    }

    @PostMapping("/picture")
    public ResponseEntity<URI> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        userService.uploadProfilePicture(file);
        return ResponseEntity.ok().build();
    }
}
