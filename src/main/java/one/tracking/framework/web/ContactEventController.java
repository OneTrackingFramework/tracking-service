/**
 *
 */
package one.tracking.framework.web;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import one.tracking.framework.dto.ContactEventDto;
import one.tracking.framework.dto.ContactEventUserDto;
import one.tracking.framework.dto.GeoUnitType;
import one.tracking.framework.dto.LocationEventDto;
import one.tracking.framework.entity.ContactEvent;
import one.tracking.framework.entity.LocationEvent;
import one.tracking.framework.repo.ContactEventRepository;
import one.tracking.framework.repo.LocationEventRepository;
import one.tracking.framework.util.JWTHelper;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/contact-event")
public class ContactEventController {

  @Autowired
  private ContactEventRepository contactEventRepository;

  @Autowired
  private JWTHelper jwtHelper;

  /**
   * At this point, we really could utilize the @Service level :)
   */
  @Autowired
  private LocationEventRepository locationEventRepository;

  @Value("${app.token.secret}")
  private String tokenSecret;

  @RequestMapping(method = RequestMethod.POST)
  public void post(
      @RequestBody
      final ContactEventDto contactEvent,
      final Authentication authentication) {

    final String scannedUserId = this.jwtHelper.decodeJWT(contactEvent.getScannedQrCode()).getSubject();

    final LocationEvent locationEvent = this.locationEventRepository.save(LocationEvent.builder()
        .latitude(contactEvent.getLocationEvent().getLatitude().toRadians())
        .longitude(contactEvent.getLocationEvent().getLongitude().toRadians())
        .name(contactEvent.getLocationEvent().getName())
        .userId(authentication.getName())
        .build());

    this.contactEventRepository.save(ContactEvent.builder()
        .locationEvent(locationEvent)
        .userA(authentication.getName())
        .userB(scannedUserId)
        .build());
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<ContactEventUserDto> getContacts(final Authentication authentication) {

    return this.contactEventRepository.findByUserAOrUserB(authentication.getName(), authentication.getName()).stream()
        .map(c -> {

          if (authentication.getName().equals(c.getUserA()))
            return ContactEventUserDto.fromEntity(c);

          return ContactEventUserDto.builder()
              .locationEvent(LocationEventDto.fromEntity(GeoUnitType.DEGREES, c.getLocationEvent()))
              .userA(c.getUserB())
              .userB(c.getUserA())
              .timestamp(c.getTimestampCreate())
              .build();
        }).collect(Collectors.toList());
  }
}
