/**
 *
 */
package one.tracking.framework.domain;

import com.github.davidmoten.geo.GeoHash;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@AllArgsConstructor
public class Geocoord {

  /**
   * Arithmetic mean of earth radius in meters.
   *
   * @see https://de.wikipedia.org/wiki/Erdradius#H%C3%A4ufig_verwendete_Werte
   *
   */
  public static final double EARTH_RADIUS = 6371009;

  private final double latitudeDegrees;
  private final double longitudeDegrees;

  public double latitude() {
    return Math.toRadians(this.latitudeDegrees);
  }

  public double longitude() {
    return Math.toRadians(this.longitudeDegrees);
  }

  /**
   * Using law of Cosines: d = acos( sin φ1 * sin φ2 + cos φ1 * cos φ2 * cos Δλ ) * R
   *
   * @param other
   * @return
   */
  public double distanceTo2(final Geocoord other) {
    if (other == null)
      throw new IllegalArgumentException();

    // var φ1 = lat1.toRadians(), φ2 = lat2.toRadians(), Δλ = (lon2-lon1).toRadians(), R = 6371e3; //
    // gives d in metres
    return Math.acos(Math.sin(latitude()) * Math.sin(other.latitude())
        + Math.cos(latitude()) * Math.cos(other.latitude()) * Math.cos(other.longitude() - longitude())) * 6371e3;

    //
  }

  public double distanceTo(final Geocoord other) {

    // P1 = (a1, b1)
    // P2 = (a2, b2)
    //
    // DA = (a2 - a1)
    // DB = (b2 - b1)
    // MA = (a1 + a2) / 2
    //
    // D = R * SRQT(DA² + (COS(MA)*DB)²)

    final double DA = latitude() - other.latitude();
    final double DB = longitude() - other.longitude();
    final double MA = (latitude() + other.latitude()) / 2.0;

    return EARTH_RADIUS * Math.sqrt(DA * DA + (Math.cos(MA) * DB) * (Math.cos(MA) * DB));

    // Search for -> (D/R)² <= DA² + (COS(MA)*DB)²
  }

  @Override
  public String toString() {
    return "[" + this.latitudeDegrees + ", " + this.longitudeDegrees + "]";
  }

  public String toGeoHash(final int precision) {
    return GeoHash.encodeHash(this.latitudeDegrees, this.longitudeDegrees, precision);
  }
}
