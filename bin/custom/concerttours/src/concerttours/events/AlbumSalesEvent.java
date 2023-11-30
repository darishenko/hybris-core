package concerttours.events;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.List;

public class AlbumSalesEvent extends AbstractEvent {
    private final String name;
    private final long albumSales;
    private final List<String> songs;

    public AlbumSalesEvent(final String name, final Long albumSales, final List<String> songs) {
        super();
        this.name = name;
        this.albumSales = albumSales;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public long getAlbumSales() {
        return albumSales;
    }

    public List<String> getSongs() {
        return songs;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
