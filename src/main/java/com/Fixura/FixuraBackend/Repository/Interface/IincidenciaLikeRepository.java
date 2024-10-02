package com.Fixura.FixuraBackend.Repository.Interface;

import com.Fixura.FixuraBackend.Model.IncidenciaLike;

public interface IincidenciaLikeRepository {
    public boolean insertLike(IncidenciaLike incidenciaLike);
    public boolean deleteLike(IncidenciaLike incidenciaLike);
    public boolean thaLiked(IncidenciaLike incidenciaLike);
}
