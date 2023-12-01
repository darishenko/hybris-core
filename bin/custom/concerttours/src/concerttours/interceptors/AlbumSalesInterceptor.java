package concerttours.interceptors;

import concerttours.events.AlbumSalesEvent;
import concerttours.model.AlbumModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class AlbumSalesInterceptor implements ValidateInterceptor, PrepareInterceptor {
    private static final long BIG_SALES = 50000L;
    private static final long NEGATIVE_SALES = 0L;
    private static final String ERROR_POSITIVE_ALBUM_SALES = "Album sales must be positive";

    @Autowired
    private EventService eventService;

    @Override
    public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException {
        if (model instanceof AlbumModel) {
            final AlbumModel albumModel = (AlbumModel) model;
            final Long sales = albumModel.getAlbumSales();

            if (Objects.nonNull(sales) && sales.longValue() < NEGATIVE_SALES) {
                throw new InterceptorException(ERROR_POSITIVE_ALBUM_SALES);
            }
        }
    }

    @Override
    public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException {
        if (model instanceof AlbumModel) {
            final AlbumModel albumModel = (AlbumModel) model;

            if (hasBecomeBig(albumModel, ctx)) {
                eventService.publishEvent(new AlbumSalesEvent(albumModel.getName(), albumModel.getAlbumSales(),
                        albumModel.getSongs()));
            }
        }
    }

    private boolean hasBecomeBig(final AlbumModel albumModel, final InterceptorContext ctx) {
        final Long sales = albumModel.getAlbumSales();

        return Objects.nonNull(sales) && sales.longValue() >= BIG_SALES && ctx.isNew(albumModel);
    }
}