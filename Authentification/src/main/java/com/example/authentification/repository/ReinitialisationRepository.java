package com.example.authentification.repository;
import com.example.authentification.entity.Reinitialisation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReinitialisationRepository extends CrudRepository<Reinitialisation,Long> {

    Optional<Reinitialisation> findByToken(String token);

}
