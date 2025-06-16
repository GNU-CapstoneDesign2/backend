package org.duckdns.petfinderapp.global.security;

import java.io.IOException;

import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.exception.UserNotFoundException;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final TokenBlackListService blacklistService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String token = null;
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.equals("Bearer ")) {
      token = header.substring(7);
    } else {
      String param = request.getParameter("token");
      if (param != null && !param.isEmpty()) {
        token = param;
      }
    }

    if (token != null && jwtTokenProvider.validateToken(token)) {
      // 1) 블랙리스트 체크
      if (blacklistService.isBlackListed(jwtTokenProvider.getJti(token))) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
        return;
      }

      // 2) 토큰에서 사용자 ID 획득
      String userId = jwtTokenProvider.getUserId(token);
      // 3) UserDetailsService 로부터 실제 사용자 정보 조회
      User user = userRepository.findByProviderId(userId)
          .orElseThrow(UserNotFoundException::missingUser);

      // 4) principal 에 엔티티를, credentials 는 null, 권한 목록 세팅
      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(user, null, null);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }
}
