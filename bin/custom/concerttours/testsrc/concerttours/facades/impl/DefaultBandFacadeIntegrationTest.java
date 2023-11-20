package concerttours.facades.impl;

import concerttours.data.BandData;
import concerttours.facades.BandFacade;
import concerttours.model.BandModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@IntegrationTest
public class DefaultBandFacadeIntegrationTest extends ServicelayerTransactionalTest {
    /**
     * Name of test band.
     */
    private static final String BAND_CODE = "101-JAZ";
    /**
     * Name of test band.
     */
    private static final String BAND_NAME = "Tight Notes";
    /**
     * History of test band.
     */
    private static final String BAND_HISTORY = "New contemporary, 7-piece Jaz unit from London, formed in 2015";

    @Resource
    private BandFacade bandFacade;
    @Resource
    private ModelService modelService;
    /**
     * Test band
     */
    private BandModel bandModel;

    @Before
    public void setUp() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            new JdbcTemplate(Registry.getCurrentTenant().getDataSource()).execute("CHECKPOINT");
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException exc) {
        }

        bandModel = modelService.create(BandModel.class);
        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);
    }

    @Test(expected = UnknownIdentifierException.class)
    public void getBand_nonexistentBandName_UnknownIdentifierException() {
        bandFacade.getBand(BAND_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBand_nullandName_IllegalArgumentException() {
        bandFacade.getBand(null);
    }

    @Test
    public void bandFacade() {
        List<BandData> bandListData = bandFacade.getBands();
        assertNotNull(bandListData);
        final int size = bandListData.size();
        modelService.save(bandModel);
        bandListData = bandFacade.getBands();
        assertNotNull(bandListData);
        assertEquals(size + 1, bandListData.size());
        assertEquals(BAND_CODE, bandListData.get(size).getId());
        assertEquals(BAND_NAME, bandListData.get(size).getName());
        assertEquals(BAND_HISTORY, bandListData.get(size).getDescription());
        final BandData persistedBandData = bandFacade.getBand(BAND_CODE);
        assertNotNull(persistedBandData);
        assertEquals(BAND_CODE, persistedBandData.getId());
        assertEquals(BAND_NAME, persistedBandData.getName());
        assertEquals(BAND_HISTORY, persistedBandData.getDescription());
    }

    @After
    public void teardown() {

    }
}