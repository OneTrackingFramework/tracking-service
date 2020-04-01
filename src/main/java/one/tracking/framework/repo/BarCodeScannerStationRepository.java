/**
 *
 */
package one.tracking.framework.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import one.tracking.framework.entity.BarCodeScannerStation;

/**
 * @author Marko Voß
 *
 */
public interface BarCodeScannerStationRepository extends PagingAndSortingRepository<BarCodeScannerStation, String> {

}
