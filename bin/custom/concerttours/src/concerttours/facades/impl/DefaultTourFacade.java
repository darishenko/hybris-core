package concerttours.facades.impl;

import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public Optional<TourData> getTourDetails(@Nonnull final String tourId) {
        final ProductModel product = productService.getProductForCode(tourId);

        if (Objects.isNull(product)) {
            return Optional.empty();
        }
        final TourData tourData = getTourData(product);

        return Optional.of(tourData);
    }

    private TourData getTourData(ProductModel product) {
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

        if (Objects.nonNull(product.getVariants())) {
            product.getVariants().forEach(variant -> {
                if (variant instanceof ConcertModel) {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();

                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(concert.getVenue());
                    summary.setType(getConcertType(concert.getConcertType()));
                    summary.setCountDown(concert.getDaysUntil());

                    concerts.add(summary);
                }
            });
        }

        return concerts;
    }

    private String getConcertType(ConcertType concertType) {
        if (ConcertType.OPENAIR.equals(concertType)) {
            return CONCERT_TYPE_OUTDOORS;
        }

        return CONCERT_TYPE_INDOORS;
    }

}