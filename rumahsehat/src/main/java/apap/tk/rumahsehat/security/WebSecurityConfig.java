package apap.tk.rumahsehat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import apap.tk.rumahsehat.config.JwtAuthenticationEntryPoint;
import apap.tk.rumahsehat.config.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    String admin = "Admin";
    String apoteker = "Apoteker";
    String dokter = "Dokter";
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/login-sso", "/validate-ticket").permitAll()
            .antMatchers("/api/v1/pasien/add").permitAll()
            .antMatchers("/api/v1/pasien/login").permitAll()
            .antMatchers("/api/v1/list-appointment").hasAuthority("Pasien")
            .antMatchers("/user").hasAuthority(admin)
            .antMatchers("/user/*").hasAuthority(admin)
            .antMatchers("/resep/add").hasAnyAuthority(dokter, admin)
            .antMatchers("/resep/viewAll").hasAnyAuthority(apoteker, admin)
            .antMatchers("/obat/ubah-stok").hasAuthority(apoteker)
            .antMatchers("/obat/viewAll").hasAnyAuthority(apoteker, admin)
            .antMatchers("/appointment/list").hasAnyAuthority(admin, dokter)
            .antMatchers("/appointment/detail").hasAnyAuthority(admin, dokter)
                .antMatchers("/chart/*").hasAnyAuthority(admin)
                .antMatchers("/resep/konfirmasi-resep").hasAnyAuthority(apoteker)
                .antMatchers("/resep/konfirmasi-resep/*").hasAnyAuthority(apoteker)
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login").permitAll()
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login").permitAll();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }


	@Autowired
	JwtAuthenticationEntryPoint authenticationEntryPoint;

	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		authenticationManagerBuilder.userDetailsService(userDetailsService)
									.passwordEncoder(encoder());
	}
	

	@Bean
	public JwtTokenFilter jwtTokenFilter(){
		return new JwtTokenFilter();
	}

	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
					.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests().antMatchers("/auth/**").permitAll()
					.antMatchers(HttpMethod.GET, "/user/allusers").permitAll()
					.anyRequest().authenticated();
		
		httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);					
	}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}