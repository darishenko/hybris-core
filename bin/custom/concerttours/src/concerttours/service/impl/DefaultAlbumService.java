package concerttours.service.impl;

import concerttours.daos.AlbumDAO;
import concerttours.model.AlbumModel;
import concerttours.service.AlbumService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultAlbumService implements AlbumService {
    private AlbumDAO albumDAO;

    @Required
    public void setAlbumDAO(final AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    @Override
    public List<AlbumModel> findAlbumsByBandPk(Long bandPk) {
        return albumDAO.findAlbumsByBandPk(bandPk);
    }
}
