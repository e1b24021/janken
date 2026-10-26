
package oit.is.z3581.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/janken", "/janken/**").authenticated()
            .anyRequest().permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()))
        .formLogin(login -> login
            .defaultSuccessUrl("/janken", true)


    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/janken", "/janken/**").authenticated()
            .anyRequest().permitAll())
        .formLogin(login -> login

            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll());

    return http.build();
  }

  @Bean

  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(
      PasswordEncoder passwordEncoder) {

    UserDetails user1 = User.withUsername("user1")
        .password(passwordEncoder.encode("isdev"))

  public InMemoryUserDetailsManager userDetailsService() {

    UserDetails user1 = User.withUsername("user1")

        .password("{bcrypt}$2y$05$y8MYjPK5H0n8IRATNzUkae37SJjME6qDP1VMGrb12/55jVMTWf1nS")

        .roles("USER")
        .build();

    UserDetails user2 = User.withUsername("user2")

        .password(passwordEncoder.encode("isdev"))
        .roles("USER")
        .build();

    UserDetails honda = User.withUsername("ほんだ")
        .password(passwordEncoder.encode("isdev"))
        .roles("USER")
        .build();

    // 生成したユーザをImMemoryUserDetailsManagerに渡す（いくつでも良い）
    return new InMemoryUserDetailsManager(user1, user2, honda);

        .password("{bcrypt}$2y$05$znK5vCR1EqYIIwW5i6blz.rYYU9BzqqlMODNTMCGAVsTgq5Emgyua")
        .roles("USER")
        .build();
    UserDetails honda = User.withUsername("ほんだ")
        .password("{bcrypt}$2y$05$.0NJVqID/HFQw1FQ64LQtuyo4aR4nMg4.DPjawIDaihdMDRyO2Nw2")
        .roles("USER")
        .build();
      return new InMemoryUserDetailsManager(user1, user2, honda);

        .password("{bcrypt}$2y$05$FwFZJiI66mUibBQTWLfZyup1g9WAJIG2hRrxDoLb0PDLkXtTnIREe").roles("USER1").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2y$05$iBeClv7bQufw4srhcO44/Oyjuq978RMq1BUk6oRJPSna7F0sBLBPO").roles("USER2").build();

    // 生成したユーザをImMemoryUserDetailsManagerに渡す（いくつでも良い）
    return new InMemoryUserDetailsManager(user1, user2);


  }
}
