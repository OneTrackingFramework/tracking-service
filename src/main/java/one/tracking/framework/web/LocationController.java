/**
 *
 */
package one.tracking.framework.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.davidmoten.geo.GeoHash;
import one.tracking.framework.dto.LocationEventDto;
import one.tracking.framework.entity.LocationEvent;
import one.tracking.framework.repo.LocationEventRepository;
import one.tracking.framework.util.Geocoord;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping(path = "/locations")
public class LocationController {

  @Autowired
  private LocationEventRepository locationEventRepository;

  @RequestMapping(method = RequestMethod.GET)
  public List<LocationEventDto> getLocations(
      @RequestParam("distance")
      final Double distance) {

    final double distanceAlpha = (distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS);
    // return
    // this.locationEventRepository.findByDistanceAlpha(distanceAlpha).stream().map(LocationEventDto::fromEntity)
    // .collect(Collectors.toList());
    return null;
  }

  @RequestMapping(method = RequestMethod.POST)
  public void createLocation(
      @RequestBody
      final LocationEventDto locationEvent,
      final Authentication authentication) {

    final String geohash =
        GeoHash.encodeHash(locationEvent.getLatitude().toDegrees(), locationEvent.getLongitude().toDegrees(), 11);

    this.locationEventRepository.save(LocationEvent.builder()
        .latitude(locationEvent.getLatitude().toRadians())
        .longitude(locationEvent.getLongitude().toRadians())
        .userId(authentication.getName())
        .geoHash(geohash)
        .boundary7(geohash.substring(0, 7))
        .boundary8(geohash.substring(0, 8))
        .build());
  }
}
