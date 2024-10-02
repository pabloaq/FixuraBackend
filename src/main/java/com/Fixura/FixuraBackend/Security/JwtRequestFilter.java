package com.Fixura.FixuraBackend.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.Fixura.FixuraBackend.Util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
  
  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        String username = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
          username = jwtUtil.extractUserEmail(authorizationHeader);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, null);

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    
  }
}
