package com.hanaro.security;

import com.hanaro.entity.Member;
import com.hanaro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
		throws UsernameNotFoundException {
		Member member = memberRepository.findByEmailAndDeletedFalse(email)
			.orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다: " + email));

		return new User(
			member.getEmail(),
			member.getPassword(),
			List.of(new SimpleGrantedAuthority(member.getRole()))
		);
	}
}
