package concerttours.controller;

import concerttours.controller.constant.ControllerConstants.Catalog;
import concerttours.controller.constant.ControllerConstants.CharacterEncoding;
import concerttours.controller.constant.ControllerConstants.JspPages;
import concerttours.controller.constant.ControllerConstants.ModelsAttribute;
import concerttours.data.BandData;
import concerttours.facades.BandFacade;
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
@RequestMapping(value = "/bands")
public class BandController {
    private CatalogVersionService catalogVersionService;
    private BandFacade bandFacade;

    @Autowired
    public void setCatalogVersionService(final CatalogVersionService catalogVersionServiceService) {
        this.catalogVersionService = catalogVersionServiceService;
    }

    @Autowired
    public void setFacade(final BandFacade facade) {
        this.bandFacade = facade;
    }

    @GetMapping
    public String showBands(final Model model) {
        final List<BandData> bands = bandFacade.getBands();
        model.addAttribute(ModelsAttribute.BANDS, bands);
        return JspPages.BAND_LIST;
    }

    @GetMapping("/{bandId}")
    public String showBandDetails(@PathVariable final String bandId, final Model model)
            throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(Catalog.CATALOG_ID, Catalog.CATALOG_VERSION_NAME);

        final String decodedBandId = URLDecoder.decode(bandId, CharacterEncoding.UTF_8);
        final Optional<BandData> band = bandFacade.getBand(decodedBandId);

        if (band.isPresent()) {
            model.addAttribute(ModelsAttribute.BAND, band.get());
        }

        return JspPages.BAND_DETAILS;
    }

}