
package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll()
			.antMatchers(HttpMethod.GET, "/", "/oups").permitAll()
			.antMatchers("/users/new").permitAll()
			.antMatchers("/admin/**").hasAnyAuthority("admin")
			.antMatchers("/pettype/**").hasAnyAuthority("admin")
			.antMatchers("/specialty/admin/**").hasAnyAuthority("admin")
			.antMatchers("/specialty/vet/**").hasAnyAuthority("veterinarian")
			.antMatchers("/suggestion/admin/**").hasAnyAuthority("admin")
			.antMatchers("/suggestion/user/**").hasAnyAuthority("veterinarian", "owner", "manager", "provider")
			.antMatchers("/visits/**").hasAnyAuthority("veterinarian", "owner")
			.antMatchers("/stays/**").hasAnyAuthority("veterinarian", "owner")
			.antMatchers("/product/**").hasAnyAuthority("provider")
			.antMatchers("/stays/listHistoryByPet/").hasAnyAuthority("owner")
			.antMatchers("/clinics/**").hasAnyAuthority("veterinarian", "owner")
			.antMatchers("/pets/**").hasAnyAuthority("owner")
			.antMatchers("/owners/**").hasAnyAuthority("owner", "admin")
			.antMatchers("/vets/**").hasAnyAuthority("manager", "admin")
			.antMatchers("/orders/**").hasAnyAuthority("manager", "provider")
			.antMatchers("/providers/**").hasAnyAuthority("manager")
			.antMatchers("/managers/**").hasAnyAuthority("manager", "admin")
			.anyRequest().denyAll().and().formLogin()

			/* .loginPage("/login") */
			.failureUrl("/login-error").and().logout().logoutSuccessUrl("/");
		// Configuración para que funcione la consola de administración
		// de la BD H2 (deshabilitar las cabeceras de protección contra
		// ataques de tipo csrf y habilitar los framesets si su contenido
		// se sirve desde esta misma página.
		http.csrf().ignoringAntMatchers("/h2-console/**");
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select username,password,enabled " + "from user_account " + "where username = ?")
			.authoritiesByUsernameQuery("select username, authority " + "from authority " + "where username = ?").passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		return encoder;
	}

}
