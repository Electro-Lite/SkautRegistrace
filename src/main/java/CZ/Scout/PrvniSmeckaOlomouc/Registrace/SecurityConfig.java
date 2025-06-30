package CZ.Scout.PrvniSmeckaOlomouc.Registrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup.SetupDataHelper;

@Configuration
public class SecurityConfig {
	@Autowired
	SetupDataHelper setupDataHelper;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/css/**").permitAll()
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
//                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
