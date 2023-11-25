package concerttours.daos.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "bandDAO")
public class DefaultBandDAO implements BandDAO {
    private static final String QUERY_SELECT_ALL_BANDS = "SELECT {p:"
            + BandModel.PK + "} FROM {"
            + BandModel._TYPECODE + " AS p} ";
    private static final String QUERY_WHERE_BAND_CODE = " WHERE {p:" + BandModel.CODE + "}=?" + BandModel.CODE;
    private static final String QUERY_SELECT_BAND_WHERE_CODE = QUERY_SELECT_ALL_BANDS + QUERY_WHERE_BAND_CODE;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    public List<BandModel> findBands() {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_SELECT_ALL_BANDS);

        return flexibleSearchService.<BandModel>search(query).getResult();
    }

    public List<BandModel> findBandsByCode(String code) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_SELECT_BAND_WHERE_CODE);

        query.addQueryParameter(BandModel.CODE, code);

        return flexibleSearchService.<BandModel>search(query).getResult();
    }
}
