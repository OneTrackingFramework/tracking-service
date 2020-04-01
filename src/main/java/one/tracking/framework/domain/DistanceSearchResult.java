/**
 *
 */
package one.tracking.framework.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import one.tracking.framework.entity.LocationEvent;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@AllArgsConstructor
public class DistanceSearchResult {

  private LocationEvent locationA;
  private LocationEvent locationB;

  private double distance;
  private int timeDiff;
}
