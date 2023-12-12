package concerttours.events;

import concerttours.model.NewsModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Nonnull;
import java.util.Date;

public class AlbumSalesEventListener extends AbstractEventListener<AlbumSalesEvent> {
    public static final String CATALOG_ID = "concertToursProductCatalog";
    public static final String CATALOG_VERSION_NAME = "Online";
    private static final String BAND_SALES_CODE = "%s %s";
    private static final String BAND_SALES_HEADLINE = "%s album sales exceed 50000";
    private static final String BAND_SALES_CONTENT = "%s album sales reported as %d";

    private ModelService modelService;
    private CatalogVersionService catalogVersionService;

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    public void setCatalogVersionService(final CatalogVersionService catalogVersionServiceService) {
        this.catalogVersionService = catalogVersionServiceService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    protected void onEvent(@Nonnull final AlbumSalesEvent event) {
        final String code = String.format(BAND_SALES_CODE, event.getName(), new Date());
        final String headline = String.format(BAND_SALES_HEADLINE, event.getName());
        final String content = String.format(BAND_SALES_CONTENT, event.getName(), event.getAlbumSales());

        final NewsModel news = createNewsModel(headline, content, code);

        modelService.save(news);
    }

    private NewsModel createNewsModel(String headline, String content, String code) {
        final NewsModel news = modelService.create(NewsModel.class);
        final CatalogVersionModel catalogVersion =
                catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);

        news.setCode(code);
        news.setDate(new Date());
        news.setHeadline(headline);
        news.setContent(content);
        news.setCatalogVersion(catalogVersion);

        return news;
    }
}
