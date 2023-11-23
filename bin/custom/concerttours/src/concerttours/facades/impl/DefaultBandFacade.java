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
import org.springframework.beans.factory.annotation.Required;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<BandData> bandFacadeData = new ArrayList<>();

        bandFacadeData = bandModels.stream()
                .map(bandModel -> {
                    final BandData sfd = new BandData();
                    sfd.setId(bandModel.getCode());
                    sfd.setName(bandModel.getName());
                    sfd.setDescription(bandModel.getHistory());
                    return sfd;
                }).collect(Collectors.toList());

        return bandFacadeData;
    }

    @Override
    public Optional<BandData> getBand(@NonNull final String name) {
        final BandModel band = bandService.getBandForCode(name);

        if (Objects.isNull(band)) {
            return Optional.empty();
        }
        final BandData bandData = getBandData(band);

        return Optional.of(bandData);
    }

    private BandData getBandData(BandModel band) {
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
        List<String> genres = new ArrayList<>();

        if (Objects.nonNull(band.getTypes())) {
            genres = band.getTypes().stream()
                    .map(MusicType::getCode)
                    .collect(Collectors.toList());
        }

        return genres;
    }

    private List<TourSummaryData> getBandTourSummaryData(BandModel band) {
        List<TourSummaryData> tourSummaryData = new ArrayList<>();

        if (Objects.nonNull(band.getTours())) {
            tourSummaryData = band.getTours().stream()
                    .map(tour -> {
                        final TourSummaryData tourData = new TourSummaryData();
                        tourData.setId(tour.getCode());
                        tourData.setTourName(tour.getName());
                        tourData.setNumberOfConcerts(Integer.toString(tour.getVariants().size()));
                        return tourData;
                    }).collect(Collectors.toList());
        }

        return tourSummaryData;
    }

    private List<AlbumData> getBandAlbumData(Long bandPk) {
        List<AlbumData> albumDataList = new ArrayList<>();
        final List<AlbumModel> albums = albumService.findAlbumsByBandPk(bandPk);

        albumDataList = albums.stream()
                .map(albumModel -> {
                    final AlbumData album = new AlbumData();
                    album.setName(albumModel.getName());
                    album.setAlbumSales(Long.toString(albumModel.getAlbumSales()));
                    album.setSongs(albumModel.getSongs());
                    return album;
                }).collect(Collectors.toList());

        return albumDataList;
    }

}