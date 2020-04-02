/**
 *
 */
package one.tracking.framework.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.domain.DistanceSearchResult;
import one.tracking.framework.entity.LocationEvent;

/**
 * @author Marko Voß
 *
 */
public interface LocationEventRepository extends CrudRepository<LocationEvent, String> {

  @Query(nativeQuery = true)
  List<DistanceSearchResult> findByUserIdAndDistanceAndTimediff(
      String userId, double distanceMeters, int timediffSeconds);
}
