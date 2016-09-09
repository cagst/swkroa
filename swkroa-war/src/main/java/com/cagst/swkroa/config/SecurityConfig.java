package com.cagst.swkroa.config;

import com.cagst.swkroa.security.ForceChangePasswordFilter;
import com.cagst.swkroa.security.SigninSuccessHandler;
import com.cagst.swkroa.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import javax.servlet.Filter;

/**
 * Configuration class for the system's Security framework.
 *
 * @author Craig Gaskill
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private static final int STRENGTH = 12;

  private SigninSuccessHandler signinSuccessHandler;

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(STRENGTH);
  }

  @Inject
  public void configureGlobal(AuthenticationManagerBuilder auth, UserService userService) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(getPasswordEncoder());

    signinSuccessHandler = new SigninSuccessHandler(userService);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/static/**");
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable()
        .exceptionHandling().accessDeniedPage("/auth/notauthorized")
        .and().authorizeRequests()
          .antMatchers("/public/**").permitAll()
          .antMatchers("/auth/**").permitAll()
          .antMatchers("/api/register/**").permitAll()
          .antMatchers("/**").authenticated()
        .and().formLogin()
          .loginPage("/auth/signin")
          .loginProcessingUrl("/login")
          .failureUrl("/auth/signin")
          .usernameParameter("username")
          .passwordParameter("password")
          .successHandler(signinSuccessHandler)
        .and().logout()
          .logoutUrl("/logout")
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID")
          .logoutSuccessUrl("/auth/signedout")
        .and().addFilterAfter(getChangePasswordFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  private Filter getChangePasswordFilter() {
    ForceChangePasswordFilter changePasswordFilter = new ForceChangePasswordFilter();
    changePasswordFilter.setChangePasswordUrl("/auth/changepwd");

    return changePasswordFilter;
  }
}
