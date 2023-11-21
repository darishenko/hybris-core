package concerttours.service.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultBandServiceUnitTest {
    /**
     * Name of test band.
     */
    private static final String BAND_CODE = "Ch00X";
    /**
     * Name of test band.
     */
    private static final String BAND_NAME = "Singers All";
    /**
     * History of test band.
     */
    private static final String BAND_HISTORY =
            "Medieval choir formed in 2001, based in Munich famous for authentic monastic chants";
    private DefaultBandService bandService;
    private BandDAO bandDAO;
    private BandModel bandModel;

    @Before
    public void setUp() {
        bandService = new DefaultBandService();

        bandDAO = mock(BandDAO.class);
        bandService.setBandDAO(bandDAO);

        bandModel = new BandModel();
        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);
    }

    /**
     * This test tests and demonstrates that the Service's getAllBands method calls the DAOs' getBands method
     * and returns the data it receives from it.
     */
    @Test
    public void getAllBands_existingBand_band() {
        final List<BandModel> bandModels = Collections.singletonList(bandModel);
        when(bandDAO.findBands()).thenReturn(bandModels);

        final List<BandModel> result = bandService.getBands();

        assertEquals("We should find one", 1, result.size());
        assertEquals("And should equals what the mock returned", bandModel, result.get(0));
    }

    @Test
    public void getBand_existingBandCode_band() {
        when(bandDAO.findBandsByCode(BAND_CODE)).thenReturn(Collections.singletonList(bandModel));

        final BandModel result = bandService.getBandForCode(BAND_CODE);

        assertEquals("Band should equals() what the mock returned", bandModel, result);
    }
}