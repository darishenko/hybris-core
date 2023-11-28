package concerttours.attributehandlers;

import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import concerttours.model.ConcertModel;
import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class ConcertDaysUntilAttributeHandlerUnitTest {
    private static final int TWO_DAYS_ONE_HOUR_DIFF = 49 * 60 * 60 * 1000;
    private static final int ONE_DAY_DIFF = 24 * 60 * 60 * 1000;

    @Test
    public void get_futureCocertDate_getPositiveDateDiff() {
        final ConcertModel concert = new ConcertModel();
        final ConcertDaysUntilAttributeHandler handler = new ConcertDaysUntilAttributeHandler();
        final Date futureDate = new Date(new Date().getTime() + TWO_DAYS_ONE_HOUR_DIFF);

        concert.setDate(futureDate);

        long dateDiff = handler.get(concert).longValue();

        Assert.assertEquals("Wrong value for concert in the future", 2L, dateDiff);
    }

    @Test
    public void get_nullConcertDate_nullDate() {
        final ConcertModel concert = new ConcertModel();
        final ConcertDaysUntilAttributeHandler handler = new ConcertDaysUntilAttributeHandler();

        Long dateDiff = handler.get(concert);

        Assert.assertNull(dateDiff);
    }

    @Test
    public void get_pastCocertDate_getZeroDateDiff() {
        final ConcertModel concert = new ConcertModel();
        final ConcertDaysUntilAttributeHandler handler = new ConcertDaysUntilAttributeHandler();
        final Date pastDate = new Date(new Date().getTime() - ONE_DAY_DIFF);

        concert.setDate(pastDate);

        long dateDiff = handler.get(concert).longValue();

        Assert.assertEquals("Wrong value for concert in the past", 0L, dateDiff);
    }
}