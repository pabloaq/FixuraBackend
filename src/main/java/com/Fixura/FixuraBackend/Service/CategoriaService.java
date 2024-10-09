package com.Fixura.FixuraBackend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fixura.FixuraBackend.Model.Categoria;
import com.Fixura.FixuraBackend.Repository.CategoriaRepository;
import com.Fixura.FixuraBackend.Service.Interface.IcategoriaService;
import com.Fixura.FixuraBackend.Util.JwtUtil;

@Service
public class CategoriaService implements IcategoriaService{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listCategory(String token) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
            return categoriaRepository.listCategory();
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener lista de categorías", ex);
        }
    }

    @Override
    public Categoria getNameCategory(String token, int id_category) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
				throw new RuntimeException("Token Expirado...");
			}
            return categoriaRepository.getNameCategory(id_category);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener nombre de categoría ", ex);
        }
    }

}
