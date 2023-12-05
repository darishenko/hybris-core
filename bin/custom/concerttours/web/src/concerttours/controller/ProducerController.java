package concerttours.controller;

import concerttours.controller.constant.ControllerConstants.Catalog;
import concerttours.controller.constant.ControllerConstants.CharacterEncoding;
import concerttours.controller.constant.ControllerConstants.JspPages;
import concerttours.controller.constant.ControllerConstants.ModelsAttribute;
import concerttours.data.ProducerData;
import concerttours.facades.ProducerFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/producers")
public class ProducerController {
    private CatalogVersionService catalogVersionService;
    private ProducerFacade producerFacade;

    @Autowired
    public void setCatalogVersionService(final CatalogVersionService catalogVersionServiceService) {
        this.catalogVersionService = catalogVersionServiceService;
    }

    @Autowired
    public void setFacade(final ProducerFacade facade) {
        this.producerFacade = facade;
    }

    @GetMapping
    public String showProducers(final Model model) {
        catalogVersionService.setSessionCatalogVersion(Catalog.CATALOG_ID, Catalog.CATALOG_VERSION_NAME);

        final List<ProducerData> producers = producerFacade.getProducers();

        model.addAttribute(ModelsAttribute.PRODUCERS, producers);

        return JspPages.PRODUCER_LIST;
    }

    @GetMapping("/{producerId}")
    public String showProducerDetails(@PathVariable final String producerId, final Model model)
            throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(Catalog.CATALOG_ID, Catalog.CATALOG_VERSION_NAME);

        final String decodedProducerId = URLDecoder.decode(producerId, CharacterEncoding.UTF_8);
        final Optional<ProducerData> producer = producerFacade.getProducer(decodedProducerId);

        producer.ifPresent(producerData -> model.addAttribute(ModelsAttribute.PRODUCER, producerData));

        return JspPages.PRODUCER_DETAILS;
    }
}
