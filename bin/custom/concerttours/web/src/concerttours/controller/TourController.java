package concerttours.controller;

import concerttours.data.TourData;
import concerttours.facades.TourFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static concerttours.controller.constant.ControllerConstant.*;


@Controller
@RequestMapping("/tours")
public class TourController {
    private CatalogVersionService catalogVersionService;
    private TourFacade tourFacade;

    @GetMapping(value = "/{tourId}")
    public String showTourDetails(@PathVariable final String tourId, final Model model)
            throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(Catalog.CATALOG_ID, Catalog.CATALOG_VERSION_NAME);
        final String decodedTourId = URLDecoder.decode(tourId, CharacterEncoding.UTF_8);
        final TourData tour = tourFacade.getTourDetails(decodedTourId);
        model.addAttribute(ModelsAttribute.TOUR, tour);
        return JspPage.TOUR_DETEILS;
    }

    @Autowired
    public void setCatalogVersionService(final CatalogVersionService catalogVersionServiceService) {
        this.catalogVersionService = catalogVersionServiceService;
    }

    @Autowired
    public void setFacade(final TourFacade facade) {
        this.tourFacade = facade;
    }

}