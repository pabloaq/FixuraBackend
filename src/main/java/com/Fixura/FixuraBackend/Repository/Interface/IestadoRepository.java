package com.Fixura.FixuraBackend.Repository.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Estado;

public interface IestadoRepository {
    public List<Estado> listState();
    public Estado getNameState(int id_state);
}
