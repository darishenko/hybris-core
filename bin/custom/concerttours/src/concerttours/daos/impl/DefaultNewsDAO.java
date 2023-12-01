package concerttours.daos.impl;

import concerttours.daos.NewsDAO;
import concerttours.model.NewsModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component(value = "newsDAO")
public class DefaultNewsDAO implements NewsDAO {
    private static final String QUERY_SELECT_NEWS_WHERE_DATE = "SELECT {p:"
            + NewsModel.PK + "} " + "FROM {"
            + NewsModel._TYPECODE + " AS p} "
            + "WHERE {date} >= DATE '%S' "
            + "AND {date} <= DATE '%S'";
    private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<NewsModel> getNewsOfTheDay(final Date date) {
        if (Objects.isNull(date)) {
            return Collections.emptyList();
        }

        final String theDay = new SimpleDateFormat(SQL_DATE_FORMAT).format(date);
        final String theNextDay = new SimpleDateFormat(SQL_DATE_FORMAT).format(getOneDayAfter(date));
        final String queryString = String.format(QUERY_SELECT_NEWS_WHERE_DATE, theDay, theNextDay);

        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

        return flexibleSearchService.<NewsModel>search(query).getResult();
    }

    private Date getOneDayAfter(final Date date) {
        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }
}