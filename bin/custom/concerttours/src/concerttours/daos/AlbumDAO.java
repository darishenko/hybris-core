package concerttours.daos;

import concerttours.model.AlbumModel;

import java.util.List;

public interface AlbumDAO {
    List<AlbumModel> findAlbumsByBandPk(String bandCode);
}
