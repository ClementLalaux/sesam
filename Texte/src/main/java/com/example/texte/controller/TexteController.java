package com.example.texte.controller;

import com.example.texte.dto.TexteResponseDTO;
import com.example.texte.entity.Texte;
import com.example.texte.service.TexteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/texte")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT , RequestMethod.DELETE})
public class TexteController {

    private final TexteService texteService;

    public TexteController(TexteService texteService) {
        this.texteService = texteService;
    }

    @GetMapping("")
    public ResponseEntity<List<Texte>> getAll(){
        return ResponseEntity.ok(texteService.getAllTextes());
    }

    @PostMapping("")
    public ResponseEntity<Texte> post(@RequestBody Texte texte){
        Texte texte1 = texteService.createTexte(texte);
        return ResponseEntity.ok(texte1);
    }

    @GetMapping("{id}")
    public ResponseEntity<Texte> getById(@PathVariable(value = "id") Long id){
        try {
            Texte texte = texteService.getById(id);
            return ResponseEntity.ok(texte);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("{page}")
    public ResponseEntity<List<Texte>> getByPage(@PathVariable(value = "page") String page){
        try {
            List<Texte> textes = texteService.getTextesByPage(page);
            return ResponseEntity.ok(textes);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable(value="id") Long id){
        texteService.deleteTexte(id);
        return new ResponseEntity<>("Texte supprimé avec succès", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Texte> update(@RequestBody TexteResponseDTO texteResponseDTO, @PathVariable(value = "id") Long id){
        Texte texte = texteService.updateTexte(id,texteResponseDTO);
        return ResponseEntity.ok(texte);
    }
}
