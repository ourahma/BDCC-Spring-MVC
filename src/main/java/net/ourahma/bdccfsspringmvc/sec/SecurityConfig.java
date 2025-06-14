package net.ourahma.bdccfsspringmvc.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //pour indiquer que c une classe de configuraton
@EnableWebSecurity // pour activer la sécurité web
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

    //@Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        };
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
                .formLogin(fl ->fl.loginPage("/login").permitAll())
                //.csrf(csrf -> csrf.disable()) // à désactiver seulement dans authentification stateless
                .csrf(Customizer.withDefaults())
                //.authorizeHttpRequests(ar -> ar.requestMatchers("/user/**").hasRole("USER"))
                //.authorizeHttpRequests(ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/public/*","/webjars/**").permitAll())
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .exceptionHandling( eh -> eh.accessDeniedPage("/notAuthorized"))
                .build(

        );
    }
}
