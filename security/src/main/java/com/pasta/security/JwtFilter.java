package com.pasta.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final String jwtHeaderString;
    private final String jwtTokenPrefix;
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    public JwtFilter(String jwtHeaderString,
                     String jwtTokenPrefix,
                     TokenProvider tokenProvider,
                     UserDetailsService userDetailsService) {
        this.jwtHeaderString = jwtHeaderString;
        this.jwtTokenPrefix = jwtTokenPrefix;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String header = httpServletRequest.getHeader(jwtHeaderString);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(jwtTokenPrefix)) {
            authToken = header.replace(jwtTokenPrefix,"");
            try {
                username = tokenProvider.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred while fetching Username from Token", e);
                chain.doFilter(req, res);
                return;
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired", e);
                chain.doFilter(req, res);
                return;
            } catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
                chain.doFilter(req, res);
                return;
            }
        } else {
            logger.warn("Couldn't find bearer string, header will be ignored");
            chain.doFilter(req, res);
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenProvider.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationToken(authToken, userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}
