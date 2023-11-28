package concerttours.attributehandlers;

import concerttours.model.ConcertModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

@IntegrationTest
public class ConcertDaysUntilAttributeHandlerIntegrationTest extends ServicelayerTransactionalTest {
    private static final int TWO_DAYS_ONE_HOUR_DIFF = 49 * 60 * 60 * 1000;
    private static final int ONE_DAY_DIFF = 24 * 60 * 60 * 1000;

    @Resource
    private ModelService modelService;

    @Test
    public void get_futureCocertDate_getPositiveDateDiff() throws Exception {
        final ConcertModel concert = modelService.create(ConcertModel.class);
        final Date futureDate = new Date(new Date().getTime() + TWO_DAYS_ONE_HOUR_DIFF);

        concert.setDate(futureDate);

        long dateDiff = concert.getDaysUntil().longValue();

        Assert.assertEquals("Wrong value for concert in the future: " + concert.getDate().longValue(), 2L, dateDiff);
    }

    @Test
    public void get_nullConcertDate_nullDate() {
        final ConcertModel concert = modelService.create(ConcertModel.class);

        Long dateDiff = concert.getDaysUntil();

        Assert.assertNull("No concert date does not return null: " + dateDiff, dateDiff);
    }

    @Test
    public void get_pastCocertDate_getZeroDateDiff() throws Exception {
        final ConcertModel concert = modelService.create(ConcertModel.class);
        final Date pastDate = new Date(new Date().getTime() - ONE_DAY_DIFF);

        concert.setDate(pastDate);

        long dateDiff = concert.getDaysUntil().longValue();

        Assert.assertEquals("Wrong value for concert in the past: " + dateDiff, 0L, dateDiff);
    }
}