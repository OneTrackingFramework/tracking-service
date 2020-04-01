/**
 *
 */
package one.tracking.framework.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.entity.ContactEvent;

/**
 * @author Marko Voß
 *
 */
public interface ContactEventRepository extends CrudRepository<ContactEvent, String> {

  List<ContactEvent> findByUserAOrUserB(String userA, String userB);

}
