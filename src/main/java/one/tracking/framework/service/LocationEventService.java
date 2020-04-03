/**
 *
 */
package one.tracking.framework.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.davidmoten.geo.GeoHash;
import one.tracking.framework.domain.DistanceSearchResult;
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

  public Collection<DistanceSearchResult> findLocations(final String userId, final Double distanceMeters,
      final Integer timediffSeconds, final Integer lookingBackDays) {

    final List<DistanceSearchResult> results = this.locationEventRepository.findByUserIdAndDistanceAndTimediff(
        userId, distanceMeters, timediffSeconds, lookingBackDays);

    final Map<String, DistanceSearchResult> userIdMap = new HashMap<>();

    results.forEach(c -> {
      final DistanceSearchResult current = userIdMap.get(c.getUserB());
      if (current == null) {
        userIdMap.put(c.getUserB(), c);
      } else {
        if (current.getTimestamp().isAfter(c.getTimestamp()))
          userIdMap.put(c.getUserB(), c);
      }
    });

    return userIdMap.values();
  }

  public void createLocation(final String userId, final LocationEventDto locationEvent) {

    final String geohash =
        GeoHash.encodeHash(locationEvent.getLatitude().toDegrees(), locationEvent.getLongitude().toDegrees(), 11);

    this.locationEventRepository.save(LocationEvent.builder()
        .userId(userId)
        .latitude(locationEvent.getLatitude().toRadians())
        .longitude(locationEvent.getLongitude().toRadians())
        .timestampCreate(locationEvent.getTimestamp().toInstant())
        .timestampOffset(locationEvent.getTimestamp().getOffset().getTotalSeconds())
        .geoHash(geohash)
        .boundary7(geohash.substring(0, 7))
        .boundary8(geohash.substring(0, 8))
        .build());
  }

}
