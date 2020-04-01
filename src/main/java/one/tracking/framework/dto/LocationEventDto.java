package one.tracking.framework.dto;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

  private OffsetDateTime timestamp;

  private Float accuracy;

  public static final LocationEventDto fromEntity(final GeoUnitType targetType, final LocationEvent event) {
    return LocationEventDto.builder()
        .latitude(GeoUnitDto.builder().type(targetType)
            .value(targetType == GeoUnitType.RADIANS ? event.getLatitude() : Math.toDegrees(event.getLatitude()))
            .build())
        .longitude(GeoUnitDto.builder().type(targetType)
            .value(targetType == GeoUnitType.RADIANS ? event.getLongitude() : Math.toDegrees(event.getLongitude()))
            .build())
        .accuracy(event.getAccuracy())
        .timestamp(event.getTimestamp())
        .build();
  }
}
