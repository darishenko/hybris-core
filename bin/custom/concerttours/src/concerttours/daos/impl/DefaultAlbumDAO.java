package concerttours.daos.impl;

import concerttours.daos.AlbumDAO;
import concerttours.model.AlbumModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "albumDAO")
public class DefaultAlbumDAO implements AlbumDAO {
    private static final String QUERY_SELECT_ALL_ALBUMS_BY_BAND_PK = "SELECT {p:"
            + AlbumModel.PK + "} FROM {"
            + AlbumModel._TYPECODE
            + " AS p} WHERE {p:" + AlbumModel.BAND
            + "}=?" + AlbumModel.BAND;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<AlbumModel> findAlbumsByBandPk(String bandPk) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_SELECT_ALL_ALBUMS_BY_BAND_PK);
        query.addQueryParameter(AlbumModel.BAND, bandPk);
        return flexibleSearchService.<AlbumModel>search(query).getResult();
    }
}
