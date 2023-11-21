package com.example.authentification.controller;

import com.example.authentification.dto.*;
import com.example.authentification.entity.Utilisateur;
import com.example.authentification.security.JWTGenerator;
import com.example.authentification.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UtilisateurController {
    private final AuthenticationManager authenticationManager;

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator generator;



    public UtilisateurController(AuthenticationManager authenticationManager, UtilisateurService utilisateurService, PasswordEncoder passwordEncoder, JWTGenerator generator) {
        this.authenticationManager = authenticationManager;
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.generator = generator;
    }


    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Utilisateur utilisateur = utilisateurService.trouverParEmail(loginRequestDTO.getEmail());
            return ResponseEntity.ok(LoginResponseDTO.builder().token(generator.generateToken(authentication))
                    .id(utilisateur.getId())
                    .email(utilisateur.getEmail())
                    .admin(utilisateur.isAdmin())
                    .build());
        }catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            Utilisateur userApp = utilisateurService.enregistrerUtilisateur(registerRequestDTO.getEmail(), passwordEncoder.encode(registerRequestDTO.getPassword()),registerRequestDTO.isAdmin());
            return ResponseEntity.ok(RegisterResponseDTO.builder().id(userApp.getId()).message("User created").build());
        }catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Utilisateur> get(@PathVariable(value = "id") Long id){
        try {
            Utilisateur utilisateur = utilisateurService.trouverParId(id);
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Utilisateur> updatePassword(@RequestBody String email ,@RequestBody String newPassword){
        try {
            Utilisateur utilisateur = utilisateurService.updatePassword(email,newPassword);
            if(utilisateur != null){
                return ResponseEntity.ok(utilisateur);
            } else {
                return ResponseEntity.status(401).body(null);
            }
        }catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }


}
