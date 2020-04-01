/**
 *
 */
package one.tracking.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import one.tracking.framework.security.BearerAuthenticationFilter;
import one.tracking.framework.util.JWTHelper;

/**
 * @author Marko Voß
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JWTHelper jwtHelper;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/h2-console/**")
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilter(bearerAuthenticationFilter())
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.headers().frameOptions().disable();
  }

  public BearerAuthenticationFilter bearerAuthenticationFilter() throws Exception {

    return new BearerAuthenticationFilter(authenticationManager(), this.jwtHelper) {

      /**
       * Validity of AuthN done by signature check of bearer token. This service does simply store userId
       * related data, without the requirement that this userId does actually exist.
       */
      @Override
      protected boolean checkIfUserExists(final String userId) {
        return true;
      }

    };
  }
}
