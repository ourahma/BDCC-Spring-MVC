package net.ourahma.bdccfsspringmvc.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //pour indiquer que c une classe de configuraton
@EnableWebSecurity // pour activer la sécurité web
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean // indiquer qu'ils sont les utilisateurs qui ont droit à accéder l'apk
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        PasswordEncoder encoder = passwordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(encoder.encode("1234")).roles("USER","ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // spécifier la stratégie de sécurité
        /**
         * Customizer.withDefaults(): si un utilisateur n'est oas authentifié utilise ton propre form d'authentification.
         * authorizeHttpRequests(ar -> ar.anyRequest().authenticated()): impliquer que tous les requêtes sont authentifiées
         * ar.requestMatchers("/index/**").hasRole("USER") : toutes les requêtes avec ce url doivent avoir role user
         * permitALL : ne nécessite pas d'authentification
         * **/
        return http
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(ar -> ar.requestMatchers("/user/**").hasRole("USER"))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/public/*").permitAll())
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .build(

        );
    }
}
