/**
 *
 */
package one.tracking.framework.service;

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
import one.tracking.framework.repo.ContactEventRepository;
import one.tracking.framework.repo.InfectionRepository;
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
  private LocationEventRepository locationEventRepository;

  @Autowired
  private ContactEventRepository contactEventRepository;

  @Autowired
  private InfectionRepository infectionRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @EventListener
  public void onApplicationEvent(final ApplicationStartedEvent event) throws JsonProcessingException {
    // createExampleData();
  }

  // private final void createExampleData() throws JsonProcessingException {
  //
  // final ObjectWriter writer = this.objectMapper.writerWithDefaultPrettyPrinter();
  //
  // final String userA = UUID.randomUUID().toString();
  // final String userB = UUID.randomUUID().toString();
  // final String userC = UUID.randomUUID().toString();
  // final String userD = UUID.randomUUID().toString();
  //
  // /*
  // * Location Event User A -> User B on 52.527338, 13.430731: User A scanning User B
  // */
  //
  // final LocationEvent locationEventAB = this.locationEventRepository.save(LocationEvent.builder()
  // .latitude(Math.toRadians(52.527338))
  // .longitude(Math.toRadians(13.430731))
  // .name("SYSTEM")
  // .userId(userA)
  // .build());
  //
  // final ContactEvent contactEventAB = this.contactEventRepository.save(ContactEvent.builder()
  // .locationEvent(locationEventAB)
  // .userA(userA)
  // .userB(userB)
  // .userCreate(userA)
  // .build());
  //
  // LOG.info("Created ContactEvent User A scanning User B:\n{}",
  // writer.writeValueAsString(contactEventAB));
  //
  // /*
  // * Location Event User C -> User D on 52.527467, 13.431369: User C scanning User D
  // */
  //
  // final LocationEvent locationEventCD = this.locationEventRepository.save(LocationEvent.builder()
  // .externalId(UUID.randomUUID().toString())
  // .latitude(Math.toRadians(52.527467))
  // .longitude(Math.toRadians(13.431369))
  // .name("SYSTEM")
  // .userCreate(userC)
  // .build());
  //
  // final ContactEvent contactEventCD = this.contactEventRepository.save(ContactEvent.builder()
  // .externalId(UUID.randomUUID().toString())
  // .locationEvent(locationEventCD)
  // .user1(userC)
  // .user2(userD)
  // .userCreate(userC)
  // .build());
  //
  // LOG.info("Created ContactEvent User C scanning User D:\n{}",
  // writer.writeValueAsString(contactEventCD));
  //
  // final OffsetDateTime infectionTime = OffsetDateTime.now().plus(1, ChronoUnit.WEEKS);
  // final Infection infection = this.infectionRepository.save(Infection.builder()
  // .infectionType(InfectionType.INFECTED)
  // .timestamp(infectionTime)
  // .user(userC)
  // .build());
  //
  // LOG.info("Created Infection of User C one week later:\n{}",
  // writer.writeValueAsString(infection));
  //
  // this.infectionRepository.save(Infection.builder()
  // .infectionType(InfectionType.AT_RISK)
  // .stage(5)
  // .timestamp(infectionTime)
  // .user(userD)
  // .build());
  //
  // this.infectionRepository.save(Infection.builder()
  // .infectionType(InfectionType.AT_RISK)
  // .stage(1)
  // .timestamp(infectionTime)
  // .user(userB)
  // .build());
  //
  // this.infectionRepository.save(Infection.builder()
  // .infectionType(InfectionType.HEALTHY)
  // .timestamp(infectionTime)
  // .user(userA)
  // .build());
  //
  // createRandomLocations(writer, userA, userB, userC, userD);
  //
  // LOG.info("DONE");
  // }
  //
  // /**
  // * @param writer
  // * @param userA
  // * @param userC
  // * @param userB
  // * @param userD
  // */
  // private void createRandomLocations(final ObjectWriter writer,
  // final User userA,
  // final User userB,
  // final User userC,
  // final User userD) {
  //
  // final Geocoord a = new Geocoord(48.16726, 11.49628);
  // final Geocoord b = new Geocoord(48.16726, 11.49632);
  //
  // // Create at least one match
  //
  // this.locationEventRepository.save(LocationEvent.builder()
  // .externalId(UUID.randomUUID().toString())
  // .latitude(a.latitude())
  // .longitude(a.longitude())
  // .name("A")
  // .userCreate(userC)
  // .build());
  //
  // this.locationEventRepository.save(LocationEvent.builder()
  // .externalId(UUID.randomUUID().toString())
  // .latitude(b.latitude())
  // .longitude(b.longitude())
  // .name("B")
  // .userCreate(userC)
  // .build());
  //
  // final double distance = a.distanceTo(b); // in meters
  //
  // LOG.info("Created locations with distance of {} meters", distance);
  //
  // LOG.info("Found close locations for this distance:");
  // this.locationEventRepository
  // .findLocations((distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS)).stream()
  // .forEach(t -> {
  // t.getFirst().setUserCreate(null); // we do not have a session at this application event
  // t.getSecond().setUserCreate(null); // we do not have a session at this application event
  // try {
  // LOG.info(writer.writeValueAsString(t));
  // } catch (final JsonProcessingException e) {
  // LOG.error(e.getMessage(), e);
  // }
  // });
  //
  // final int amount = 100000;
  //
  // LOG.info("Creating {} amount of LocationEvents around location {}", amount, a);
  //
  // final List<LocationEvent> buffer = new ArrayList<>(100);
  //
  // final OffsetDateTime start = OffsetDateTime.now();
  //
  // for (int i = 1; i <= amount; i++) {
  //
  // final Geocoord randCoord = new Geocoord(
  // getRandom(a.getLatitudeDecimal()),
  // getRandom(a.getLongitudeDecimal()));
  //
  // buffer.add(LocationEvent.builder()
  // .externalId(UUID.randomUUID().toString())
  // .latitude(randCoord.latitude())
  // .longitude(randCoord.longitude())
  // .name("RANDOM-" + i)
  // .userCreate(
  // RANDOM.nextBoolean() ? userA : RANDOM.nextBoolean() ? userB : RANDOM.nextBoolean() ? userC :
  // userD)
  // .build());
  //
  // if (OffsetDateTime.now().isAfter(start.plusMinutes(5))) {
  // LOG.info("Cancelling (timeout after 5 minutes). Storing {} into DB.", buffer.size());
  // this.locationEventRepository.saveAll(buffer);
  // buffer.clear();
  // break;
  // }
  //
  //
  // if (i % 10000 == 0) {
  // LOG.info("Storing {} into DB.", buffer.size());
  // this.locationEventRepository.saveAll(buffer);
  // buffer.clear();
  // }
  //
  // if ((i * 100. / amount) % 10 == 0 && (i * 100. / amount) != 0)
  // LOG.info("{}% done", (int) (i * 100. / amount));
  // }
  // }
  //
  // private static final double getRandom(final double position) {
  // return position + RANDOM.nextInt(100000) / 1000000.0;
  // }
}
