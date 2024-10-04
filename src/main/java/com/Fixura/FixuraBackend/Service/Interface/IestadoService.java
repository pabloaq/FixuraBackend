package com.Fixura.FixuraBackend.Service.Interface;

import java.util.List;

import com.Fixura.FixuraBackend.Model.Estado;

public interface IestadoService {
    public List<Estado> listState(String token);
    public Estado getNameState(String token, int id_state);
}
