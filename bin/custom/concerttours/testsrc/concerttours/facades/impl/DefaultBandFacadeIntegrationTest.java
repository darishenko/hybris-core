package concerttours.facades.impl;

import concerttours.daos.impl.DefaultBandDAOIntegrationTest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBandFacadeIntegrationTest.class);

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
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }

        bandModel = getBandModel();
        modelService.save(bandModel);
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
    public void getBands_notEmptyBandsList_bands() {
        List<BandData> bandListData = bandFacade.getBands();

        assertNotNull(bandListData);
        assertEquals(1, bandListData.size());
        assertEquals(BAND_CODE, bandListData.get(0).getId());
        assertEquals(BAND_NAME, bandListData.get(0).getName());
        assertEquals(BAND_HISTORY, bandListData.get(0).getDescription());
    }

    @Test
    public void getBand_existingBandCode_band() {
        final BandData persistedBandData = bandFacade.getBand(BAND_CODE).get();

        assertNotNull(persistedBandData);
        assertEquals(BAND_CODE, persistedBandData.getId());
        assertEquals(BAND_NAME, persistedBandData.getName());
        assertEquals(BAND_HISTORY, persistedBandData.getDescription());
    }

    private BandModel getBandModel() {
        final BandModel bandModel = modelService.create(BandModel.class);

        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);

        return bandModel;
    }
}