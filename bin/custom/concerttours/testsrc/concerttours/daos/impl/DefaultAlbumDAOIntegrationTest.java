package concerttours.daos.impl;

import concerttours.daos.AlbumDAO;
import concerttours.model.AlbumModel;
import concerttours.model.BandModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@IntegrationTest
public class DefaultAlbumDAOIntegrationTest extends ServicelayerTransactionalTest {
    private static final String BAND_CODE = "ROCK-111";
    private static final String BAND_NAME = "Ladies of Rock 1";
    private static final String BAND_HISTORY = "All female rock band formed in Munich in the late 1990s";
    private static final String ALBUM_NAME_1 = "album 1";
    private static final String ALBUM_NAME_2 = "album 2";
    private static final String CHECKPOINT = "CHECKPOINT";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAlbumDAOIntegrationTest.class);

    @Resource
    private ModelService modelService;
    @Resource
    private AlbumDAO albumDAO;

    @Before
    public void setUp() throws Exception {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            new JdbcTemplate(Registry.getCurrentTenant().getDataSource()).execute(CHECKPOINT);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    @Test
    public void findAlbumsByBandPk_existingBandPk_notEmptyAlbumsList() {
        BandModel band = getBandModel();
        AlbumModel albumModel1 = getAlbumModel(band, ALBUM_NAME_1);
        AlbumModel albumModel2 = getAlbumModel(band, ALBUM_NAME_2);

        modelService.save(band);
        modelService.save(albumModel1);
        modelService.save(albumModel2);

        final List<AlbumModel> albums = albumDAO.findAlbumsByBandPk(band.getPk().getLong());

        Assert.assertEquals("Did not find the Albums we just saved", 2, albums.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAlbumsByBandPk_nullBandPk_IllegalArgumentException() {
        final List<AlbumModel> albums = albumDAO.findAlbumsByBandPk(null);
    }

    @Test
    public void findAlbumsByBandPk_nonexistentBandPk_noAlbums() {
        final List<AlbumModel> albums = albumDAO.findAlbumsByBandPk(1L);

        Assert.assertTrue("No album should be returned", albums.isEmpty());
    }

    private BandModel getBandModel() {
        BandModel bandModel = modelService.create(BandModel.class);

        bandModel.setCode(BAND_CODE);
        bandModel.setName(BAND_NAME);
        bandModel.setHistory(BAND_HISTORY);

        return bandModel;
    }

    private AlbumModel getAlbumModel(BandModel bandModel, String albumName) {
        AlbumModel albumModel = modelService.create(AlbumModel.class);

        albumModel.setName(albumName);
        albumModel.setBand(bandModel);

        return albumModel;
    }

}
