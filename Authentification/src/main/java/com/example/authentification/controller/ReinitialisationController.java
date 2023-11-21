package com.example.authentification.controller;

import com.example.authentification.service.ReinitialisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/reinitialise")
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ReinitialisationController {

    private final ReinitialisationService reinitialisationService;

    public ReinitialisationController(ReinitialisationService reinitialisationService) {
        this.reinitialisationService = reinitialisationService;
    }

    @PostMapping("/update/{token}")
    public ResponseEntity<Boolean> updatePassword(@RequestParam(value = "newPassword") String newPassword, @PathVariable(value = "token") String token){
        try {
            return ResponseEntity.ok(reinitialisationService.resetPassword(token,newPassword));
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam(value = "email") String email){
        try {
            reinitialisationService.processPasswordResetRequest(email);
            return ResponseEntity.ok("Reset password request initiated successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to initiate password reset request.");
        }
    }
}

