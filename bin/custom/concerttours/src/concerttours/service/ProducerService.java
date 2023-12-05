package concerttours.service;

import concerttours.model.ProducerModel;

import java.util.List;

public interface ProducerService {
    /**
     * Gets all Producers in the system.
     *
     * @return all Producers in the system
     */
    List<ProducerModel> getProducers();

    /**
     * Gets the Producer for the given code.
     *
     * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException in case more than one Producer is found for the given code
     * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException   in case no Producer for the given code can be found
     */
    ProducerModel getProducerForCode(String code);
}
