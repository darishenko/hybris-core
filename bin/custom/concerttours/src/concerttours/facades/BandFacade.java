package concerttours.facades;

import concerttours.data.BandData;

import java.util.List;
import java.util.Optional;

public interface BandFacade {
    Optional<BandData> getBand(String name);

    List<BandData> getBands();
}