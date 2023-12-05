package concerttours.service.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DefaultProducerService implements ProducerService {
    private static final String PRODUCER_WITH_CODE_NOT_FOUND = "Producer with code '%s' not found!";
    private static final String PRODUCER_CODE_NOT_UNIQUE = "Producer code '%s' is not unique.";
    private static final String PRODUCER_WITH_NOT_UNIQUE_CODE_COUNT = "%d producers found!";

    private ProducerDAO producerDAO;

    @Required
    public void setProducerDAO(final ProducerDAO producerDAO) {
        this.producerDAO = producerDAO;
    }

    @Override
    public List<ProducerModel> getProducers() {
        return producerDAO.findProducers();
    }

    @Override
    public ProducerModel getProducerForCode(String code) {
        List<ProducerModel> producers = producerDAO.findProducersByCode(code);

        if (CollectionUtils.isEmpty(producers)) {
            throw new UnknownIdentifierException(String.format(PRODUCER_WITH_CODE_NOT_FOUND, code));
        }

        if (producers.size() > 1) {
            throw new AmbiguousIdentifierException(String.format(PRODUCER_CODE_NOT_UNIQUE, code)
                    .concat(String.format(PRODUCER_WITH_NOT_UNIQUE_CODE_COUNT, producers.size())));
        }

        return producers.get(0);
    }
}
