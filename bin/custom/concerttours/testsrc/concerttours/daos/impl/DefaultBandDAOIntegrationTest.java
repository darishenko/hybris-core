package concerttours.daos.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * The purpose of this test is to illustrate DAO best practices and behaviour.
 * <p>
 * The DAO logic is factored into a separate POJO. Stepping into these will illustrate how to write and execute
 * FlexibleSearchQueries - the basis on which most hybris DAOs operate.
 */
@IntegrationTest
public class DefaultBandDAOIntegrationTest extends ServicelayerTransactionalTest {
    private static final String CHECKPOINT = "CHECKPOINT";
    /**
     * Name of test band.
     */
    private static final String BAND_CODE = "ROCK-11";
    /**
     * Name of test band.
     */
    private static final String BAND_NAME = "Ladies of Rock";
    /**
     * History of test band.
     */
    private static final String BAND_HISTORY = "All female rock band formed in Munich in the late 1990s";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBandDAOIntegrationTest.class);

    private BandModel bandModel;
    /**
     * As this is an integration test, the class (object) being tested gets injected here.
     */
    @Resource
    private BandDAO bandDAO;
    /**
     * Platform's ModelService used for creation of test data.
     */
    @Resource
    private ModelService modelService;

    @Before
    public void setUp() throws Exception {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            new JdbcTemplate(Registry.getCurrentTenant().getDataSource()).execute(CHECKPOINT);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }

        bandModel = getBandModel();
        modelService.save(bandModel);
    }

    @Test
    public void findBands_notEmptyBandsTable_bands() {
        List<BandModel> allBands = bandDAO.findBands();

        Assert.assertEquals(1, allBands.size());
        Assert.assertTrue("Band not found", allBands.contains(bandModel));
    }

    @Test
    public void findBandsByCode_existingBandCode_band() {
        List<BandModel> bandsByCode = bandDAO.findBandsByCode(BAND_CODE);

        Assert.assertEquals("Did not find the Band we just saved", 1, bandsByCode.size());
        Assert.assertEquals("Retrieved Band's code attribute incorrect",
                BAND_CODE, bandsByCode.get(0).getCode());
        Assert.assertEquals("Retrieved Band's name attribute incorrect",
                BAND_NAME, bandsByCode.get(0).getName());
        Assert.assertEquals("Retrieved Band's history attribute incorrect",
                BAND_HISTORY, bandsByCode.get(0).getHistory());
    }

    @Test
    public void findBandsByCode_emptyBandCode_noBands() {
        final List<BandModel> bands = bandDAO.findBandsByCode("");

        Assert.assertTrue("No Band should be returned", bands.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findBandsByCode_nullBandCode_IllegalArgumentException() {
        bandDAO.findBandsByCode(null);
    }

    private BandModel getBandModel() {
        final BandModel bandModel = modelService.create(BandModel.class);

        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);

        return bandModel;
    }
}