/**
 *
 */
package one.tracking.framework.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class InfectionDto {

  private boolean isInfected;
}
