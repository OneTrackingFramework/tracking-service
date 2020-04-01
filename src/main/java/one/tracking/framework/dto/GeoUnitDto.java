/**
 *
 */
package one.tracking.framework.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class GeoUnitDto {

  @NotNull
  private Double value;

  @NotNull
  private GeoUnitType type;

  public Double toDegrees() {
    return this.type == GeoUnitType.DEGREES ? this.value : Math.toDegrees(this.value);
  }

  public Double toRadians() {
    return this.type == GeoUnitType.RADIANS ? this.value : Math.toRadians(this.value);
  }
}
