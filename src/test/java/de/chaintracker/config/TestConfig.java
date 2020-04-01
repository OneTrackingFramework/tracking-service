/**
 *
 */
package de.chaintracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import lombok.Getter;

/**
 * @author Marko Voß
 *
 */
@Getter
@TestConfiguration
public class TestConfig {

  @Value("${app.test.data.locations.amount}")
  private int amountLocations;

  @Value("${app.test.data.locations.timeout}")
  private int timeout;

  @Value("${app.test.data.locations.buffer}")
  private int bufferSize;

}
