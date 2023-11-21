package com.example.authentification.service;

import com.example.authentification.entity.Utilisateur;
import com.example.authentification.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Utilisateur enregistrerUtilisateur(String email, String password,boolean admin) {
        Utilisateur user = new Utilisateur(email, password,admin);
        return userRepository.save(user);
    }

    public Utilisateur trouverParEmail(String email) {
        Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
        if(utilisateur.isPresent()){
            return utilisateur.get();
        }
        throw new RuntimeException("Not found");
    }

    public Utilisateur trouverParId(Long id) {
        Optional<Utilisateur> utilisateur = userRepository.findById(id);
        if(utilisateur.isPresent()){
            return utilisateur.get();
        }
        throw new RuntimeException("Not found");
    }

    public Utilisateur updatePassword(String email, String newPassword) {

        Utilisateur utilisateur = trouverParEmail(email);
        if(utilisateur != null){
            String encodedPassword = passwordEncoder.encode(newPassword);
            utilisateur.setPassword(encodedPassword);
            userRepository.save(utilisateur);
            return utilisateur;
        }
        return null;
    }


}

