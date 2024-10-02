package com.Fixura.FixuraBackend.Service.Interface;

import com.Fixura.FixuraBackend.Model.IncidenciaLike;

public interface IincidenciaLikeService {
    public boolean insertLike(String token, IncidenciaLike incidenciaLike);
    public boolean deleteLike(String token, IncidenciaLike incidenciaLike);
    public boolean thaLiked(String token, IncidenciaLike incidenciaLike);
}
