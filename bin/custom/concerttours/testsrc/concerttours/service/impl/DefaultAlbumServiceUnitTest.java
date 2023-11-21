package concerttours.service.impl;

import concerttours.daos.AlbumDAO;
import concerttours.model.AlbumModel;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultAlbumServiceUnitTest {
    private static final Long BAND_PK = 1L;
    private static final String ALBUM_NAME = "album 1";

    private DefaultAlbumService albumService;
    private AlbumDAO albumDAO;
    private AlbumModel albumModel;

    @Before
    public void setUp() {
        albumService = new DefaultAlbumService();

        albumDAO = mock(AlbumDAO.class);
        albumService.setAlbumDAO(albumDAO);
    }

    @Test
    public void findAlbumsByBandPk_existingBandPk_album() {
        final List<AlbumModel> albumModels = Collections.singletonList(albumModel);
        when(albumDAO.findAlbumsByBandPk(ArgumentMatchers.<Long>any())).thenReturn(albumModels);

        final List<AlbumModel> result = albumService.findAlbumsByBandPk(BAND_PK);

        assertEquals("We should find one", 1, result.size());
        assertEquals("And should equals what the mock returned", albumModel, result.get(0));
    }
}
