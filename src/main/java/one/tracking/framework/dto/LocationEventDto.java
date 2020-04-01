package one.tracking.framework.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import one.tracking.framework.entity.LocationEvent;

@Data
@Builder
public class LocationEventDto {

  @NotNull
  @Valid
  private GeoUnitDto latitude;

  @NotNull
  @Valid
  private GeoUnitDto longitude;

  @Size(max = 250)
  private String name;

  public static final LocationEventDto fromEntity(final GeoUnitType targetType, final LocationEvent event) {
    return LocationEventDto.builder()
        .latitude(GeoUnitDto.builder().type(targetType)
            .value(targetType == GeoUnitType.RADIANS ? event.getLatitude() : Math.toDegrees(event.getLatitude()))
            .build())
        .longitude(GeoUnitDto.builder().type(targetType)
            .value(targetType == GeoUnitType.RADIANS ? event.getLongitude() : Math.toDegrees(event.getLongitude()))
            .build())
        .name(event.getName())
        .build();
  }
}
