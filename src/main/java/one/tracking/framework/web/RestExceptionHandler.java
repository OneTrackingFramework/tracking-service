/**
 *
 */
package one.tracking.framework.web;

import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Marko Voß
 *
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(value = {NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> notFound(final Exception e) {
    LOG.debug(e.getMessage(), e);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> badRequest(final Exception e) {
    LOG.debug(e.getMessage(), e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<String> forbidden(final Exception e) {
    LOG.debug(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }
}
