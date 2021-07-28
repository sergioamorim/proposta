package br.com.zupacademy.sergio.proposal.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
  override fun configure(httpSecurity: HttpSecurity) {
    httpSecurity {

      csrf { disable() }

      authorizeRequests {
        authorize(
          AntPathRequestMatcher("/actuator/prometheus", HttpMethod.GET.name),
          hasAuthority("SCOPE_prometheus")
        )
        authorize(
          AntPathRequestMatcher("/**", HttpMethod.GET.name),
          hasAuthority("SCOPE_proposal:read")
        )
        authorize(
          AntPathRequestMatcher("/**", HttpMethod.POST.name),
          hasAuthority("SCOPE_proposal:write")
        )
        authorize(anyRequest, authenticated)
      }

      sessionManagement {
        sessionCreationPolicy = SessionCreationPolicy.STATELESS
      }

      oauth2ResourceServer { jwt { } }

    }
  }
}
