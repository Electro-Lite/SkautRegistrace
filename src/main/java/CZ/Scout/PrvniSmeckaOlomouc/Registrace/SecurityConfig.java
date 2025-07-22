package CZ.Scout.PrvniSmeckaOlomouc.Registrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup.SetupDataHelper;

/**
 * Configuration class for Spring Security.
 * Sets up security filters, login/logout behavior, and user authentication details.
 */
@Configuration
public class SecurityConfig {
	 /**
     * Helper for initializing setup data, such as default credentials.
     */
	@Autowired
	SetupDataHelper setupDataHelper;
	
	/**
     * Configures the HTTP security settings, including authorization rules and login/logout behavior.
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while building the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/loginMapper", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    /**
     * Initializes the in-memory user details manager with default users and passwords.
     * User credentials are retrieved or generated using {@link SetupDataHelper}.
     *
     * @return a {@link UserDetailsService} containing "user" and "admin" users
     */
    @Bean
    public UserDetailsService users() {
    	setupDataHelper.initializeSetupData();
        UserDetails user = User.builder()
            .username("user")
            .password(setupDataHelper.findOrCreateSetup().getUserPwd())
            .roles("USER")
            .build();
        
        UserDetails admin = User.builder()
                .username("admin")
                .password(setupDataHelper.findOrCreateSetup().getAdminPwd())
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }
    
    /**
     * Configures the password encoder to use BCrypt.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
