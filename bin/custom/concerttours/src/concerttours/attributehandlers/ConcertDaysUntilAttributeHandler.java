package concerttours.attributehandlers;

import concerttours.model.ConcertModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class ConcertDaysUntilAttributeHandler extends AbstractDynamicAttributeHandler<Optional<Long>, ConcertModel> {
    @Override
    public Optional<Long> get(@NonNull final ConcertModel model) {
        if (model.getDate() == null) {
            return Optional.empty();
        }

        final ZonedDateTime concertDate = model.getDate().toInstant().atZone(ZoneId.systemDefault());
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration duration = Duration.between(now, concertDate);

        if (concertDate.isBefore(now)) {
            return Optional.of(Long.valueOf(0L));
        }

        return Optional.of(Long.valueOf(duration.toDays()));
    }
}