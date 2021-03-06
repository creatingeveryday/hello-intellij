package hello.hellointellij.service;

import hello.hellointellij.domain.Member;
import hello.hellointellij.repository.MemberRepository;
import hello.hellointellij.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 비즈니스로직을 다루는 서비스 계층에서는 비즈니스 처리에 맞는 용어를 선정하여 사용하는 경향. / repo에 쓰는 용어는 저장소에 어울리는 용어를 선정.

//AOP 공통관심사항 cross-cutting concern  / 핵심관심사항 core concern 분리하여 관리하는게 유지보수 및 가독성에 좋다. 효율적.
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     * */
    public Long join(Member member){

//         long start = System.currentTimeMillis();

         try {
        validateDuplicate(member);//중복 회원 검증
        memberRepository.save(member);
        return member.getId();

         }finally {
//             long finish = System.currentTimeMillis();
//             long timeMs = finish - start;
//             System.out.println("join => "+ timeMs);
         }

    }

    private void validateDuplicate(Member member) {
        memberRepository.findByName(member.getName())
             .ifPresent(memberInfo -> {
                 throw new IllegalStateException("이미존재하는 회원입니다.");
             });
    }

    /**
     * 전체 회원 조회
     * */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
