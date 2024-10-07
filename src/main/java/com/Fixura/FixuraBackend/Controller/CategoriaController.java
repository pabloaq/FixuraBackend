package com.Fixura.FixuraBackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Fixura.FixuraBackend.Model.Categoria;
import com.Fixura.FixuraBackend.Service.CategoriaService;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/list")
    public ResponseEntity<List<Categoria>> listCategory(@RequestHeader("Authorization") String token){
        List<Categoria> list  = categoriaService.listCategory(token);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

    @GetMapping("/name/{idCategory}")
    public ResponseEntity<Categoria> getNameCategory(
        @RequestHeader("Authorization") String token,
        @PathVariable int idCategory
        ){
        Categoria result  = categoriaService.getNameCategory(token, idCategory);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
