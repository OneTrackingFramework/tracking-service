/**
 *
 */
package one.tracking.framework.domain;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
public class DistanceSearchResult {

  String userAGeoHash;
  String userBGeoHash;
  String userB;
  OffsetDateTime timestamp;
  Double distance;
  Integer timeDifference;

  public DistanceSearchResult(final String userAGeoHash, final String userBGeoHash, final String userB,
      final Date timestamp, final Integer offset, final Double distance, final BigInteger timeDifference) {

    this.userAGeoHash = userAGeoHash;
    this.userBGeoHash = userBGeoHash;
    this.userB = userB;
    this.timestamp = OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.ofTotalSeconds(offset.intValue()));
    this.distance = distance;
    this.timeDifference = timeDifference.intValue();
  }

}
