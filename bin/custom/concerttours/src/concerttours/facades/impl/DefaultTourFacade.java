package concerttours.facades.impl;

import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultTourFacade implements TourFacade {
    private static final String NULL_TOUR_ID = "Tour id cannot be null";
    private static final String CONCERT_TYPE_OUTDOORS = "Outdoors";
    private static final String CONCERT_TYPE_INDOORS = "Indoors";
    private ProductService productService;

    @Required
    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }

    @Override
    public TourData getTourDetails(final String tourId) {
        if (Objects.isNull(tourId)) {
            throw new IllegalArgumentException(NULL_TOUR_ID);
        }
        final ProductModel product = productService.getProductForCode(tourId);
        if (Objects.isNull(product)) {
            return null;
        }

        final TourData tourData = createTourData(product);
        return tourData;
    }

    private TourData createTourData(ProductModel product) {
        final TourData tourData = new TourData();
        final List<ConcertSummaryData> concerts = getConcertSummaryData(product);

        tourData.setId(product.getCode());
        tourData.setTourName(product.getName());
        tourData.setDescription(product.getDescription());
        tourData.setConcerts(concerts);
        return tourData;
    }

    private List<ConcertSummaryData> getConcertSummaryData(ProductModel product) {
        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if (product.getVariants() != null) {
            for (final VariantProductModel variant : product.getVariants()) {
                if (variant instanceof ConcertModel) {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(concert.getVenue());
                    summary.setType(concert.getConcertType() ==
                            ConcertType.OPENAIR
                            ? CONCERT_TYPE_OUTDOORS
                            : CONCERT_TYPE_INDOORS);
                    concerts.add(summary);
                }
            }
        }
        return concerts;
    }

}