package concerttours.facades;

import concerttours.data.ProducerData;

import java.util.List;
import java.util.Optional;

public interface ProducerFacade {
    Optional<ProducerData> getProducer(String name);

    List<ProducerData> getProducers();
}
