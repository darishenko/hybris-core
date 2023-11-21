package concerttours.facades.impl;

import concerttours.data.AlbumData;
import concerttours.data.BandData;
import concerttours.data.TourSummaryData;
import concerttours.enums.MusicType;
import concerttours.facades.BandFacade;
import concerttours.model.AlbumModel;
import concerttours.model.BandModel;
import concerttours.service.AlbumService;
import concerttours.service.BandService;
import de.hybris.platform.core.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultBandFacade implements BandFacade {
    private static final String NULL_BAND_NAME = "Band name cannot be null";
    private BandService bandService;
    private AlbumService albumService;

    @Required
    public void setBandService(final BandService bandService) {
        this.bandService = bandService;
    }

    @Required
    public void setAlbumService(final AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    public List<BandData> getBands() {
        final List<BandModel> bandModels = bandService.getBands();
        final List<BandData> bandFacadeData = new ArrayList<>();
        for (final BandModel sm : bandModels) {
            final BandData sfd = new BandData();
            sfd.setId(sm.getCode());
            sfd.setName(sm.getName());
            sfd.setDescription(sm.getHistory());
            bandFacadeData.add(sfd);
        }
        return bandFacadeData;
    }

    @Override
    public BandData getBand(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException(NULL_BAND_NAME);
        }
        final BandModel band = bandService.getBandForCode(name);
        if (Objects.isNull(band)) {
            return null;
        }

        final BandData bandData = createBandData(band);
        return bandData;
    }

    private BandData createBandData(BandModel band) {
        final BandData bandData = new BandData();
        final List<String> genres = getBandGenres(band);
        final List<TourSummaryData> tours = getBandTourSummaryData(band);
        final List<AlbumData> albums = getBandAlbumData(band.getPk().getLong());

        bandData.setId(band.getCode());
        bandData.setName(band.getName());
        bandData.setDescription(band.getHistory());
        bandData.setGenres(genres);
        bandData.setTours(tours);
        bandData.setAlbums(albums);
        return bandData;
    }

    private List<String> getBandGenres(BandModel band) {
        final List<String> genres = new ArrayList<>();
        if (band.getTypes() != null) {
            for (final MusicType musicType : band.getTypes()) {
                genres.add(musicType.getCode());
            }
        }
        return genres;
    }

    private List<TourSummaryData> getBandTourSummaryData(BandModel band) {
        List<TourSummaryData> tourSummaryData = new ArrayList<>();
        if (Objects.nonNull(band.getTours())) {
            for (final ProductModel tour : band.getTours()) {
                final TourSummaryData tourData = new TourSummaryData();
                tourData.setId(tour.getCode());
                tourData.setTourName(tour.getName());
                tourData.setNumberOfConcerts(Integer.toString(tour.getVariants().size()));
                tourSummaryData.add(tourData);
            }
        }
        return tourSummaryData;
    }

    private List<AlbumData> getBandAlbumData(Long bandPk) {
        List<AlbumData> albumDataList = new ArrayList<>();
        final List<AlbumModel> albums = albumService.findAlbumsByBandPk(bandPk);
        for (final AlbumModel albumModel : albums) {
            final AlbumData album = new AlbumData();
            album.setName(albumModel.getName());
            album.setAlbumSales(Long.toString(albumModel.getAlbumSales()));
            album.setSongs(albumModel.getSongs());
            albumDataList.add(album);
        }
        return albumDataList;
    }

}