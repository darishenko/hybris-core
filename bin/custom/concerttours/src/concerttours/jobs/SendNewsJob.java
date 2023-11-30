package concerttours.jobs;

import concerttours.model.NewsModel;
import concerttours.service.NewsService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.mail.MailUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class SendNewsJob extends AbstractJobPerformable<CronJobModel> {
    private static final Logger LOGGER = Logger.getLogger(SendNewsJob.class);
    private static final String LOGGER_INFO_SEND_NEW_EMAIL =
            "Sending news mails. Note that org.apache.commons.mail.send() can block if behind a firewall/proxy";
    private static final String LOGGER_INFO_NO_NEWS_ITEMS =
            "No news items for today, skipping send of ranking mails";
    private static final String LOGGER_ERROR_SEND_EMAIL_PROBLEM =
            "Problem sending new email. Note that org.apache.commons.mail.send() can block if behind a firewall/proxy)";
    private static final String EMAIL_SUBJECT = "Daily News Summary";
    private static final String PROPERTY_EMAIL_FOR_SENDING = "news_summary_mailing_address";
    private static final String EMAIL_CONTENT_NEWS_ITEM = "%s\n %s\n\n";
    private static final String EMAIL_CONTENT_START = "Todays news summary:\n\n";

    private NewsService newsService;
    private ConfigurationService configurationService;

    @Required
    public NewsService getNewsService() {
        return newsService;
    }

    @Required
    public void setNewsService(final NewsService newsService) {
        this.newsService = newsService;
    }

    @Required
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    @Required
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public PerformResult perform(final CronJobModel cronJob) {
        LOGGER.info(LOGGER_INFO_SEND_NEW_EMAIL);

        final List<NewsModel> newsItems = getNewsService().getNewsOfTheDay(new Date());

        if (CollectionUtils.isEmpty(newsItems)) {
            LOGGER.info(LOGGER_INFO_NO_NEWS_ITEMS);

            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }

        try {
            sendEmail(createEmailContent(newsItems));
        } catch (final EmailException e) {
            LOGGER.error(LOGGER_ERROR_SEND_EMAIL_PROBLEM, e);

            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private String createEmailContent(List<NewsModel> newsItems) {
        final StringBuilder mailContentBuilder = new StringBuilder(2000);

        mailContentBuilder.append(EMAIL_CONTENT_START);
        newsItems.forEach(news -> {
            String itemContent = String.format(EMAIL_CONTENT_NEWS_ITEM, news.getHeadline(), news.getContent());
            mailContentBuilder.append(itemContent);
        });

        return mailContentBuilder.toString();
    }

    private void sendEmail(final String message) throws EmailException {
        Configuration config = getConfigurationService().getConfiguration();
        String recipient = config.getString(PROPERTY_EMAIL_FOR_SENDING, null);

        final Email email = createEmail(recipient, message);

        email.send();
    }

    private Email createEmail(String recipient, String message) throws EmailException {
        final Email email = MailUtils.getPreConfiguredEmail();

        email.addTo(recipient);
        email.setSubject(EMAIL_SUBJECT);
        email.setMsg(message);
        email.setTLS(true);

        LOGGER.info(EMAIL_SUBJECT);
        LOGGER.info(message);

        return email;
    }
}