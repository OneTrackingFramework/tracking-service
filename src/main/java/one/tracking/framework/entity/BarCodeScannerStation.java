/**
 *
 */
package one.tracking.framework.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BarCodeScannerStation {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false, updatable = false)
  private OffsetDateTime timestampCreate;

  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampUpdate;

  @Column(length = 32, nullable = false)
  private String userCreate;

  @Column(length = 32, nullable = false)
  private String userUpdate;

  @Column(length = 250)
  private String name;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @PrePersist
  void onPrePersist() {
    if (this.id == null) {
      setTimestampCreate(OffsetDateTime.now());
    }
  }
}
