package concerttours.service;

import concerttours.model.AlbumModel;

import java.util.List;

public interface AlbumService {
    List<AlbumModel> findAlbumsByBandPk(String bandCode);
}
