package one.tracking.framework.dto;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import one.tracking.framework.entity.ContactEvent;

@Data
@Builder
public class ContactEventUserDto {

  @NotBlank
  private String userA;

  @NotBlank
  private String userB;

  @NotNull
  private OffsetDateTime timestamp;

  @NotNull
  private LocationEventDto locationEvent;

  public static final ContactEventUserDto fromEntity(final ContactEvent event) {
    return ContactEventUserDto.builder()
        .locationEvent(LocationEventDto.fromEntity(GeoUnitType.DEGREES, event.getLocationEvent()))
        .userA(event.getUserA())
        .userB(event.getUserB())
        .timestamp(event.getTimestampCreate())
        .build();
  }
}
