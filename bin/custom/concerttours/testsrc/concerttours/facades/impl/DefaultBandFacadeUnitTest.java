package concerttours.facades.impl;

import concerttours.data.BandData;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultBandFacadeUnitTest {
    private static final String BAND_CODE = "ROCK-11";
    private static final String BAND_NAME = "Ladies of Rock";
    private static final String BAND_HISTORY = "All female rock band formed in Munich in the late 1990s";

    private DefaultBandFacade bandFacade;
    @Mock
    private ModelService modelService;
    @Mock
    private BandService bandService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        bandFacade = new DefaultBandFacade();
        bandFacade.setBandService(bandService);
    }

    @Test
    public void getAllBands_notEmptyBandList() {
        final List<BandModel> bands = getDataBandList();
        final BandModel band = createTestBand();

        when(bandService.getBands()).thenReturn(bands);

        final List<BandData> dto = bandFacade.getBands();

        Assert.assertNotNull(dto);
        Assert.assertEquals(bands.size(), dto.size());
        Assert.assertEquals(band.getCode(), dto.get(0).getId());
        Assert.assertEquals(band.getName(), dto.get(0).getName());
        Assert.assertEquals(band.getHistory(), dto.get(0).getDescription());
    }

    @Test
    public void getBand_existingBandCode_band() {
        final BandModel band = createTestBand();

        when(bandService.getBandForCode(BAND_CODE)).thenReturn(band);

        final BandData dto = bandFacade.getBand(BAND_CODE).get();

        Assert.assertNotNull(dto);
        Assert.assertEquals(band.getCode(), dto.getId());
        Assert.assertEquals(band.getName(), dto.getName());
        Assert.assertEquals(band.getHistory(), dto.getDescription());
    }

    private BandModel createTestBand() {
        final BandModel band = new BandModel();

        band.setCode(BAND_CODE);
        band.setName(BAND_NAME);
        band.setHistory(BAND_HISTORY);

        modelService.attach(band);

        return band;
    }

    private List<BandModel> getDataBandList() {
        final List<BandModel> bands = new ArrayList<BandModel>();
        final BandModel band = createTestBand();

        bands.add(band);

        return bands;
    }
}