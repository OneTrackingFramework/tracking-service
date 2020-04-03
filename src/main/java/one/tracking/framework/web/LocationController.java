/**
 *
 */
package one.tracking.framework.web;

import java.util.Collection;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import one.tracking.framework.domain.DistanceSearchResult;
import one.tracking.framework.dto.LocationEventDto;
import one.tracking.framework.service.LocationEventService;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping(path = "/locations")
public class LocationController {

  @Autowired
  private LocationEventService service;

  @RequestMapping(method = RequestMethod.GET)
  public Collection<DistanceSearchResult> getLocations(
      @RequestParam("userId")
      final String userId,
      @RequestParam("distance")
      final Double distanceMeters,
      @RequestParam("timediff")
      final Integer timediffSeconds,
      @RequestParam("lookingBackDays")
      final Integer lookingBackDays) {

    return this.service.findLocations(userId, distanceMeters, timediffSeconds, lookingBackDays);
  }

  @RequestMapping(method = RequestMethod.POST)
  public void createLocation(
      @RequestBody
      @Valid
      final LocationEventDto locationEvent,
      final Authentication authentication) {

    this.service.createLocation(authentication.getName(), locationEvent);
  }
}
