/**
 *
 */
package one.tracking.framework.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import one.tracking.framework.domain.Geocoord;
import one.tracking.framework.entity.LocationEvent;
import one.tracking.framework.repo.LocationEventRepository;

/**
 * @author Marko Voß
 *
 */
@Service
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class TestDataService {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataService.class);

  private static final Random RANDOM = new Random();

  @Autowired
  private LocationEventService locationEventService;

  @Autowired
  private LocationEventRepository locationEventRepository;
  //
  // @Autowired
  // private ContactEventRepository contactEventRepository;
  //
  // @Autowired
  // private InfectionRepository infectionRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @EventListener
  public void onApplicationEvent(final ApplicationStartedEvent event) throws JsonProcessingException {
    createRandomLocations();
  }


  private void createRandomLocations() {

    final ObjectWriter writer = this.objectMapper.writerWithDefaultPrettyPrinter();

    final String userA = "40283298713632f001713632f9030000";
    final String userB = "40283298713632f001713632f9af0003";
    final String userC = "40283298713632f001713632fa270006";
    final String userD = "40283298713632f001713632faa10009";

    final Geocoord a = new Geocoord(48.16726, 11.49628);
    final Geocoord b = new Geocoord(48.16726, 11.49632);

    // Create at least one match

    this.locationEventRepository.save(LocationEvent.builder()
        .latitude(a.latitude())
        .longitude(a.longitude())
        .geoHash(a.toGeoHash(11))
        .userId(userA)
        .build());

    this.locationEventRepository.save(LocationEvent.builder()
        .latitude(b.latitude())
        .longitude(b.longitude())
        .geoHash(b.toGeoHash(11))
        .userId(userB)
        .build());


    final double distance = a.distanceTo(b); // in meters
    LOG.info("Created locations with distance of {} meters", distance);

    LOG.info("Found close locations for this distance:");
    this.locationEventService.findCloseLocations(distance).stream()
        .forEach(t -> {
          try {
            LOG.info(writer.writeValueAsString(t));
          } catch (final JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
          }
        });

    final int amount = 100000;

    LOG.info("Creating {} amount of LocationEvents around location {}", amount, a);

    final List<LocationEvent> buffer = new ArrayList<>(100);

    final OffsetDateTime start = OffsetDateTime.now();

    for (int i = 1; i <= amount; i++) {

      final Geocoord randCoord = new Geocoord(
          getRandom(a.getLatitudeDegrees()),
          getRandom(a.getLongitudeDegrees()));

      buffer.add(LocationEvent.builder()
          .latitude(randCoord.latitude())
          .longitude(randCoord.longitude())
          .geoHash(randCoord.toGeoHash(11))
          .userId(RANDOM.nextBoolean() ? userA : RANDOM.nextBoolean() ? userB : RANDOM.nextBoolean() ? userC : userD)
          .build());

      if (OffsetDateTime.now().isAfter(start.plusMinutes(5))) {
        LOG.info("Cancelling (timeout after 5 minutes). Storing {} into DB.", buffer.size());
        this.locationEventRepository.saveAll(buffer);
        buffer.clear();
        break;
      }


      if (i % 10000 == 0) {
        LOG.info("Storing {} into DB.", buffer.size());
        this.locationEventRepository.saveAll(buffer);
        buffer.clear();
      }

      if ((i * 100. / amount) % 10 == 0 && (i * 100. / amount) != 0)
        LOG.info("{}% done", (int) (i * 100. / amount));
    }
  }

  private static final double getRandom(final double position) {
    return position + RANDOM.nextInt(100000) / 1000000.0;
  }
}
