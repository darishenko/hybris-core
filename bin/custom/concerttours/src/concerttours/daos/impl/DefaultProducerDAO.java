package concerttours.daos.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "producerDAO")
public class DefaultProducerDAO implements ProducerDAO {
    private static final String QUERY_SELECT_ALL_PRODUCERS = "SELECT {p:"
            + ProducerModel.PK + "} FROM {"
            + ProducerModel._TYPECODE + " AS p} ";
    private static final String QUERY_WHERE_PRODUCER_CODE =
            " WHERE {p:" + ProducerModel.CODE + "}=?" + ProducerModel.CODE;
    private static final String QUERY_SELECT_PRODUCER_WHERE_CODE =
            QUERY_SELECT_ALL_PRODUCERS + QUERY_WHERE_PRODUCER_CODE;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<ProducerModel> findProducers() {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_SELECT_ALL_PRODUCERS);

        return flexibleSearchService.<ProducerModel>search(query).getResult();
    }

    @Override
    public List<ProducerModel> findProducersByCode(String code) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_SELECT_PRODUCER_WHERE_CODE);

        query.addQueryParameter(ProducerModel.CODE, code);

        return flexibleSearchService.<ProducerModel>search(query).getResult();
    }
}
