package concerttours.facades.impl;

import concerttours.data.ProducerData;
import concerttours.data.TourShortInfoData;
import concerttours.facades.ProducerFacade;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultProducerFacade implements ProducerFacade {
    private ProducerService producerService;

    @Required
    public void setProducerService(final ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public Optional<ProducerData> getProducer(@Nonnull final String name) {
        final ProducerModel producerModel = producerService.getProducerForCode(name);

        if (Objects.isNull(producerModel)) {
            return Optional.empty();
        }

        ProducerData producerData = getProducerData(producerModel);

        return Optional.of(producerData);
    }

    @Override
    public List<ProducerData> getProducers() {
        final List<ProducerModel> producerModels = producerService.getProducers();
        List<ProducerData> producersData = new ArrayList<>();

        producersData = producerModels.stream()
                .map(producerModel -> {
                    return getProducerData(producerModel);
                }).collect(Collectors.toList());

        return producersData;
    }

    private ProducerData getProducerData(ProducerModel producerModel) {
        final ProducerData producer = new ProducerData();
        final List<TourShortInfoData> toursShortInfoData = getTourShortInfoData(producerModel);

        producer.setId(producerModel.getCode());
        producer.setFirstName(producerModel.getFirstName());
        producer.setLastName(producerModel.getLastName());
        producer.setTours(toursShortInfoData);

        return producer;
    }

    private List<TourShortInfoData> getTourShortInfoData(ProducerModel producerModel) {
        List<TourShortInfoData> tourShortInfoData = new ArrayList<>();

        if (Objects.nonNull(producerModel.getTours())) {
            tourShortInfoData = producerModel.getTours().stream()
                    .map(tourModel -> {
                        final TourShortInfoData tour = new TourShortInfoData();

                        tour.setId(tourModel.getCode());
                        tour.setTourName(tourModel.getName());

                        return tour;
                    }).collect(Collectors.toList());
        }

        return tourShortInfoData;
    }
}

