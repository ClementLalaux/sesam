package com.example.authentification.repository;

import com.example.authentification.entity.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Long> {

    Optional<Utilisateur> findByEmail(String email);


}
