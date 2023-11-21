package concerttours.controller;

import concerttours.controller.constant.ControllerConstant;
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

import static concerttours.controller.constant.ControllerConstant.*;


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
        return JspPage.BAND_LIST;
    }

    @GetMapping("/{bandId}")
    public String showBandDetails(@PathVariable final String bandId, final Model model)
            throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(Catalog.CATALOG_ID, Catalog.CATALOG_VERSION_NAME);
        final String decodedBandId = URLDecoder.decode(bandId, CharacterEncoding.UTF_8);
        final BandData band = bandFacade.getBand(decodedBandId);
        model.addAttribute(ControllerConstant.ModelsAttribute.BAND, band);
        return ControllerConstant.JspPage.BAND_DETAILS;
    }

}