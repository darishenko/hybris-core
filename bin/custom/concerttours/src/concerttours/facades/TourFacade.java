package concerttours.facades;

import concerttours.data.TourData;

import java.util.Optional;

public interface TourFacade {
    Optional<TourData> getTourDetails(final String tourId);
}