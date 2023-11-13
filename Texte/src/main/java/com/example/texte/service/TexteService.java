package com.example.texte.service;

import com.example.texte.dto.TexteResponseDTO;
import com.example.texte.dto.UtilisateurDTO;
import com.example.texte.entity.Texte;
import com.example.texte.repository.TexteRepository;
import com.example.texte.tool.RestClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TexteService {

    private final TexteRepository texteRepository;

    public TexteService(TexteRepository texteRepository) {
        this.texteRepository = texteRepository;
    }

    public Texte createTexte(Texte texte){
        return texteRepository.save(texte);
    }

    public List<Texte> getAllTextes(){
        return (List<Texte>) texteRepository.findAll();
    }

    public Texte getById(Long id){
        if(texteRepository.findById(id).isPresent()){
            Texte texte = texteRepository.findById(id).get();
            return texte;
        }
        throw new RuntimeException("Not found");
    }

    public List<Texte> getTextesByPage(String page) {
        if(texteRepository.findAllByPage(page) != null){
            return texteRepository.findAllByPage(page);
        }
        throw new RuntimeException("Not found");
    }

    public Texte updateTexte(Long id, TexteResponseDTO texteResponseDTO){
        RestClient<UtilisateurDTO, String> restClient = new RestClient<>("http://localhost:8081/api/");
        UtilisateurDTO userDTO = restClient.get("auth/"+texteResponseDTO.getUtilisateurId(), UtilisateurDTO.class);
        Texte texte = getById(id);
        if(userDTO != null  && texte !=null){
            texte.setContenu(texteResponseDTO.getContenu());
            texte.setPage(texteResponseDTO.getPage());
            texte.setPosition(texte.getPosition());
            texteRepository.save(texte);
            return texte;
        }
        throw new RuntimeException("Not found");
    }

    public boolean deleteTexte(Long id){
        Texte texte = getById(id);
        if(texte != null){
            texteRepository.delete(texte);
            return true;
        }
        throw new RuntimeException("Error");
    }
}
