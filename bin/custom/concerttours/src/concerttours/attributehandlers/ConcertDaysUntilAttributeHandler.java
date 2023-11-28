package concerttours.attributehandlers;

import concerttours.model.ConcertModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class ConcertDaysUntilAttributeHandler extends AbstractDynamicAttributeHandler<Long, ConcertModel> {
    @Override
    @Nullable
    public Long get(@NonNull final ConcertModel model) {
        if (model.getDate() == null) {
            return null;
        }

        final ZonedDateTime concertDate = model.getDate().toInstant().atZone(ZoneId.systemDefault());
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration duration = Duration.between(now, concertDate);

        if (concertDate.isBefore(now)) {
            return Long.valueOf(0L);
        }

        return Long.valueOf(duration.toDays());
    }
}