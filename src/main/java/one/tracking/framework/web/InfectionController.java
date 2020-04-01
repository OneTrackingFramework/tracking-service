/**
 *
 */
package one.tracking.framework.web;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiParam;
import one.tracking.framework.dto.ContactEventUserDto;
import one.tracking.framework.dto.InfectionDto;
import one.tracking.framework.entity.Infection;
import one.tracking.framework.entity.InfectionType;
import one.tracking.framework.repo.InfectionRepository;
import one.tracking.framework.util.Geocoord;

/**
 * @author Marko Voß
 *
 */
// @RestController
@RequestMapping("/infect")
public class InfectionController {

  @Autowired
  private InfectionRepository infectionRepository;

  @RequestMapping(method = RequestMethod.POST)
  public void setInfected(final InfectionDto infection, final Authentication authentication) {

    if (!infection.isInfected() && this.infectionRepository.findByUserId(authentication.getName())) {
      this.infectionRepository.deleteByUserId(authentication.getName());
      return;
    }

    this.infectionRepository.save(Infection.builder()
        .infectionType(InfectionType.INFECTED)
        .timestamp(OffsetDateTime.now())
        .userId(authentication.getName())
        .build());

  }

  @RequestMapping(method = RequestMethod.GET, path = "/events")
  public List<ContactEventUserDto> getInfectedLocations(
      @ApiParam(value = "The maximum distance in meters between two locations to search for. (max. 100 meters)")
      @RequestParam(value = "distance", required = false)
      final Double distance,
      @ApiParam(value = "The maximum amount of days to search backwards from now.")
      @RequestParam(value = "maxTime", required = false)
      final Integer maxTime,
      @ApiParam(value = "The maximum amount of minutes between different users did enter the almost same location.")
      @RequestParam(value = "deltaTime", required = false)
      final Integer deltaTime,
      final Authentication authentication) {

    final double distanceX = distance == null ? 10 : distance > 100 ? 100 : distance;
    final int deltaTimeX = deltaTime == null ? 10 : deltaTime;
    final OffsetDateTime minTimeX =
        maxTime == null ? OffsetDateTime.now().minusDays(14) : OffsetDateTime.now().minusDays(maxTime);

    final double distanceAlpha =
        (distanceX / Geocoord.EARTH_RADIUS) * (distanceX / Geocoord.EARTH_RADIUS);

    // return this.infectionRepository.findContactEvents(authentication.getName(), distanceAlpha,
    // deltaTimeX, minTimeX)
    // .stream().map(ContactEventUserDto::fromEntity).collect(Collectors.toList());
    return null;
  }
}
