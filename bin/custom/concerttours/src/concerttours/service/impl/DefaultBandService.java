package concerttours.service.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;


public class DefaultBandService implements BandService {
    private static final String BAND_WITH_CODE_NOT_FOUND = "Band with code '%s' not found!";
    private static final String BAND_CODE_NOT_UNIQUE = "Band code '%s' is not unique.";
    private static final String BAND_WITH_NOT_UNIQUE_CODE_COUNT = "%d bands found!";
    private BandDAO bandDAO;

    @Required
    public void setBandDAO(final BandDAO bandDAO) {
        this.bandDAO = bandDAO;
    }

    @Override
    public List<BandModel> getBands() {
        return bandDAO.findBands();
    }

    @Override
    public BandModel getBandForCode(String code) {
        List<BandModel> bands = bandDAO.findBandsByCode(code);
        if (bands.isEmpty()) {
            throw new UnknownIdentifierException(String.format(BAND_WITH_CODE_NOT_FOUND, code));
        }
        if (bands.size() > 1) {
            throw new AmbiguousIdentifierException(String.format(BAND_CODE_NOT_UNIQUE, code)
                    .concat(String.format(BAND_WITH_NOT_UNIQUE_CODE_COUNT, bands.size())));
        }
        return bands.get(0);
    }
}
