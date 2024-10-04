package com.Fixura.FixuraBackend.Service.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Categoria;

public interface IcategoriaService {
    public List<Categoria> listCategory(String token);
    public Categoria getNameCategory(String token, int id_category);
}
