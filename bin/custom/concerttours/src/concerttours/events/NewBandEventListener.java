package concerttours.events;

import concerttours.model.BandModel;
import concerttours.model.NewsModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Objects;

public class NewBandEventListener extends AbstractEventListener<AfterItemCreationEvent> {
    private static final String NEW_BAND_CODE = "%s %s";
    private static final String NEW_BAND_HEADLINE = "New band, %s";
    private static final String NEW_BAND_CONTENT =
            "There is a new band in town called, %s. Tour news to be announced soon.";
    public static final String CATALOG_ID = "concertToursProductCatalog";
    public static final String CATALOG_VERSION_NAME = "Online";

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
    protected void onEvent(@Nonnull final AfterItemCreationEvent event) {
        if (Objects.nonNull(event.getSource())) {
            final Object object = modelService.get(event.getSource());

            if (object instanceof BandModel) {
                final BandModel band = (BandModel) object;
                final NewsModel news = createNewsModel(band);

                modelService.save(news);
            }
        }
    }

    private NewsModel createNewsModel(BandModel band) {
        final String code = String.format(NEW_BAND_CODE, band.getCode(), new Date());
        final String headline = String.format(NEW_BAND_HEADLINE, band.getName());
        final String content = String.format(NEW_BAND_CONTENT, band.getName());
        final CatalogVersionModel catalogVersion =
                catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);

        final NewsModel news = modelService.create(NewsModel.class);

        news.setCode(code);
        news.setDate(new Date());
        news.setHeadline(headline);
        news.setContent(content);
        news.setCatalogVersion(catalogVersion);

        return news;
    }
}