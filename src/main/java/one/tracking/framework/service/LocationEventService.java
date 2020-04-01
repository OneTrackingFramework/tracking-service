/**
 *
 */
package one.tracking.framework.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.davidmoten.geo.GeoHash;
import one.tracking.framework.domain.Geocoord;
import one.tracking.framework.dto.LocationEventDto;
import one.tracking.framework.entity.LocationEvent;
import one.tracking.framework.repo.LocationEventRepository;

/**
 * @author Marko Voß
 *
 */
@Service
public class LocationEventService {

  @Autowired
  private LocationEventRepository locationEventRepository;

  public List<LocationEventDto> getLocations(final Double distance) {

    final double distanceAlpha = (distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS);
    // return
    // this.locationEventRepository.findByDistanceAlpha(distanceAlpha).stream().map(LocationEventDto::fromEntity)
    // .collect(Collectors.toList());
    return null;
  }

  public void createLocation(final String userId, final LocationEventDto locationEvent) {

    final String geohash =
        GeoHash.encodeHash(locationEvent.getLatitude().toDegrees(), locationEvent.getLongitude().toDegrees(), 11);

    this.locationEventRepository.save(LocationEvent.builder()
        .userId(userId)
        .latitude(locationEvent.getLatitude().toRadians())
        .longitude(locationEvent.getLongitude().toRadians())
        .timestamp(locationEvent.getTimestamp())
        .geoHash(geohash)
        .boundary7(geohash.substring(0, 7))
        .boundary8(geohash.substring(0, 8))
        .build());
  }

  public List<LocationEventDto> findCloseLocations(final double distance) {
    return Collections.emptyList();
    // return this.locationEventRepository.findLocations((distance / Geocoord.EARTH_RADIUS) * (distance
    // / Geocoord.EARTH_RADIUS)).stream().map(LocationEventDto::fromEntity).
  }
}
