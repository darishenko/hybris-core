package concerttours.service.impl;

import concerttours.daos.impl.DefaultAlbumDAOIntegrationTest;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@IntegrationTest
public class DefaultBandServiceIntegrationTest extends ServicelayerTest {
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBandServiceIntegrationTest.class);

    @Resource
    private BandService bandService;
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
    public void getBandForCode_noBandWithCode_UnknownIdentifierException() {
        bandService.getBandForCode("");
    }

    @Test
    public void getBands_notEmptyBandList_bands() {
        List<BandModel> bandModels = bandService.getBands();

        assertEquals(1, bandModels.size());
        assertEquals("Unexpected band found", bandModel, bandModels.get(bandModels.size() - 1));
    }

    @Test
    public void getBandForCode_existingBandCode_band() {
        final BandModel persistedBandModel = bandService.getBandForCode(BAND_CODE);

        assertNotNull("No band found", persistedBandModel);
        assertEquals("Different band found", bandModel, persistedBandModel);
    }

    private BandModel getBandModel() {
        BandModel bandModel = modelService.create(BandModel.class);

        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);

        return bandModel;
    }
}