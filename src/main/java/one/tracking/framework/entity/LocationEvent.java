/**
 *
 */
package one.tracking.framework.entity;

import java.time.Instant;
import java.time.OffsetDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PrePersist;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.tracking.framework.domain.DistanceSearchResult;

/**
 * FIXME: Optimize query to return only one row per userId using the first contact timestamp. If
 * this is not possible, perform the query using {@link EntityManager} directly and filter entries
 * before creating {@link DistanceSearchResult} instances. Currently all rows matching the criteria
 * are being returned.<br/>
 * TODO: Discuss filter by probabilities<br/>
 * TODO: Extend from TIMESTAMP_CREATE to FROM_TIMESTAMP, TO_TIMESTAMP to handle contact time as well
 *
 * @author Marko Voß
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "LocationEvent.findByUserIdAndDistanceAndTimediff",
        query = "SELECT A_HASH, B_HASH, B_USER, B_TIMESTAMP, B_OFFSET, DISTANCE, TIMEDIFF "
            + "FROM ("
            + "    SELECT"
            + "        e1.GEO_HASH AS A_HASH,"
            + "        e2.GEO_HASH AS B_HASH,"
            + "        e2.USER_ID AS B_USER,"
            + "        e2.TIMESTAMP_CREATE AS B_TIMESTAMP,"
            + "        e2.TIMESTAMP_OFFSET AS B_OFFSET,"
            + "        SQRT((e1.latitude - e2.latitude) * (e1.latitude - e2.latitude) + COS((e1.latitude + e2.latitude) / 2) * COS((e1.latitude + e2.latitude) / 2) * (e1.longitude - e2.longitude) * (e1.longitude - e2.longitude))*6371009 AS DISTANCE,"
            + "        ABS(TIMESTAMPDIFF('SECOND', e1.TIMESTAMP_CREATE, e2.TIMESTAMP_CREATE)) AS TIMEDIFF"
            + "    FROM LOCATION_EVENT e1, LOCATION_EVENT e2"
            + "    WHERE e1.USER_ID <> e2.USER_ID"
            + "     AND e1.BOUNDARY8 = e2.BOUNDARY8"
            + "        AND e1.TIMESTAMP_CREATE <= CURRENT_TIMESTAMP()"
            + "        AND e1.TIMESTAMP_CREATE >= DATEADD('DAY', -?4, CURRENT_TIMESTAMP())"
            + "        AND e2.TIMESTAMP_CREATE <= CURRENT_TIMESTAMP()"
            + "        AND e2.TIMESTAMP_CREATE >= DATEADD('DAY', -?4, CURRENT_TIMESTAMP())"
            + "        AND (e1.TIMESTAMP_CREATE >= DATEADD('MINUTE', -?3, e2.TIMESTAMP_CREATE) OR e1.TIMESTAMP_CREATE <= DATEADD('MINUTE',  ?3, e2.TIMESTAMP_CREATE))"
            + "        AND e1.USER_ID = ?1"
            + ") x "
            + "WHERE x.DISTANCE <= ?2"
            + " AND x.TIMEDIFF <= ?3",
        resultSetMapping = "DistanceSearchResult")
})
@SqlResultSetMapping(name = "DistanceSearchResult", classes = {
    @ConstructorResult(targetClass = DistanceSearchResult.class,
        columns = {
            @ColumnResult(name = "A_HASH"),
            @ColumnResult(name = "B_HASH"),
            @ColumnResult(name = "B_USER"),
            @ColumnResult(name = "B_TIMESTAMP"),
            @ColumnResult(name = "B_OFFSET"),
            @ColumnResult(name = "DISTANCE"),
            @ColumnResult(name = "TIMEDIFF")})
})
@Table(indexes = {
    @Index(name = "IDX_BOUNDARY7", columnList = "boundary7"),
    @Index(name = "IDX_BOUNDARY8", columnList = "boundary8"),
    @Index(name = "IDX_GEOHASH", columnList = "geoHash")
})
public class LocationEvent {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(length = 32, nullable = false)
  private String userId;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false)
  private Float accuracy;

  @Column(length = 11)
  private String geoHash;

  @Column(length = 8)
  private String boundary8;

  @Column(length = 7)
  private String boundary7;

  @Basic
  private Instant timestampCreate;

  private int timestampOffset;

  @PrePersist
  void onPrePersist() {
    if (this.id == null && this.timestampCreate == null) {
      final OffsetDateTime dateTime = OffsetDateTime.now();
      setTimestampCreate(dateTime.toInstant());
      setTimestampOffset(dateTime.getOffset().getTotalSeconds());
    }

    if (this.accuracy == null)
      setAccuracy(0f);
    if (this.boundary7 == null && this.geoHash != null)
      setBoundary7(this.geoHash.substring(0, 7));
    if (this.boundary8 == null && this.geoHash != null)
      setBoundary8(this.geoHash.substring(0, 8));
  }
}
