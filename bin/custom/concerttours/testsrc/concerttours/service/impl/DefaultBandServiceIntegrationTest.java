package concerttours.service.impl;

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
        } catch (InterruptedException exc) {
        }

        bandModel = modelService.create(BandModel.class);
        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);
    }

    @Test(expected = UnknownIdentifierException.class)
    public void getBandForCode_noBandWithCode_UnknownIdentifierException() {
        bandService.getBandForCode(BAND_CODE);
    }

    /**
     * This test tests and demonstrates that the Service's getAllBand method calls the DAOs' getAllBand method and
     * returns the data it receives from it.
     */
    @Test
    public void bandService() {
        List<BandModel> bandModels = bandService.getBands();
        final int size = bandModels.size();
        modelService.save(bandModel);
        bandModels = bandService.getBands();
        assertEquals(size + 1, bandModels.size());
        assertEquals("Unexpected band found", bandModel, bandModels.get(bandModels.size() - 1));
        final BandModel persistedBandModel = bandService.getBandForCode(BAND_CODE);
        assertNotNull("No band found", persistedBandModel);
        assertEquals("Different band found", bandModel, persistedBandModel);
    }

    /**
     * This test tests and demonstrates that the Service's getAllBand method calls the DAOs' getAllBand method and
     * returns the data it receives from it.
     */
    @Test
    public void bandServiceTours() throws Exception {
        createCoreData();
        importCsv("/impex/concerttours-bands.impex", "utf-8");
        importCsv("/impex/concerttours-yBandTour.impex", "utf-8");
        final BandModel band = bandService.getBandForCode("A001");
        assertNotNull("No band found", band);
        final Set<ProductModel> tours = band.getTours();
        assertNotNull("No tour found", tours);
        Assert.assertEquals("not found one tour", 1, tours.size());
        final Object[] objects = new Object[5];
        final Collection<VariantProductModel> concerts = ((ProductModel) tours.toArray(objects)[0]).getVariants();
        assertNotNull("No tour found", tours);
        Assert.assertEquals("not found one tour", 6, concerts.size());
    }
}