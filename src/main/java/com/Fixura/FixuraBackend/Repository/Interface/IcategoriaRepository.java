package com.Fixura.FixuraBackend.Repository.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Categoria;

public interface IcategoriaRepository {
    public List<Categoria> listCategory();
    public Categoria getNameCategory(int id_categoria);
}
