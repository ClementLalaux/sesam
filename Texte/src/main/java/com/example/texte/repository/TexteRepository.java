package com.example.texte.repository;

import com.example.texte.entity.Texte;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TexteRepository extends CrudRepository<Texte,Long> {

    public List<Texte> findAllByPageOrderByPosition(String page);

}
