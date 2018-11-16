package com.pdp.app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	Environment env;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {		
		auth.inMemoryAuthentication()
        .withUser(env.getProperty("login.username")).password(env.getProperty("login.password"))
        .roles(env.getProperty("login.role"))
        .and().withUser(env.getProperty("admin.username")).password(env.getProperty("admin.password"))
        .roles(env.getProperty("admin.role"));		
	}	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	  http.authorizeRequests()
	  .antMatchers("/welcome").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		  .formLogin().loginPage("/login")
		  .usernameParameter("username").passwordParameter("password")
		.and()
		  .logout().logoutSuccessUrl("/login?logout")	
		 .and()
		 .exceptionHandling().accessDeniedPage("/403")
		.and()
		  .csrf();
	}
	
}
