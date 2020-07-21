//package com.wyjax.withstudy;
//
//import com.wyjax.withstudy.config.auth.dto.SessionUser;
//import com.wyjax.withstudy.web.content.model.repository.ContentRepository;
//import com.wyjax.withstudy.web.member.model.Member;
//import com.wyjax.withstudy.web.member.model.MemberRepository;
//import com.wyjax.withstudy.web.member.model.Role;
//import com.wyjax.withstudy.web.push.model.repository.PushRepository;
//import com.wyjax.withstudy.web.study.model.TechStack;
//import com.wyjax.withstudy.web.study.model.repository.TechStackRepository;
//import com.wyjax.withstudy.web.study.model.StudyMember;
//import com.wyjax.withstudy.web.study.model.StudyRoom;
//import com.wyjax.withstudy.web.study.model.StudyStatus;
//import com.wyjax.withstudy.web.content.model.Content;
//import com.wyjax.withstudy.web.push.model.Push;
//import com.wyjax.withstudy.web.study.model.repository.StudyMemberRepository;
//import com.wyjax.withstudy.web.study.model.repository.StudyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpSession;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class InitDB {
//    private final InitService initService;
//
//    @PostConstruct // Post~~ spring이 올라오고나면 호출을 해준다.
//    public void init(){
//        initService.initMemberDB();
////        initService.initStudyDB();
//        initService.initContentDB();
////        initService.initTechStach();
////        initService.initPush();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//        private final MemberRepository memberRepository;
//        private final StudyRepository studyRepository;
//        private final StudyMemberRepository studyMemberRepository;
//        private final ContentRepository contentRepository;
//        private final TechStackRepository techStackRepository;
//        private final PushRepository pushRepository;
//        private final HttpSession session;
//
//        public void initTechStach() {
//            String[] arr = {"Spring Boot", "Spring MVC", "React", "Java", "Python"};
//
//            for (int i = 0; i < arr.length; i++) {
//                TechStack techStack = TechStack.builder()
//                        .name(arr[i])
//                        .build();
//                techStackRepository.save(techStack);
//            }
//        }
//
//        public void initMyDB() {
//            Member member = memberRepository.save(Member.builder()
//                                                   .uid("wyjax")
//                                                   .nickname("wyjax")
//                                                   .picture("")
//                                                   .role(Role.USER)
//                                                   .build());
//            session.setAttribute("user", new SessionUser(member));
//        }
//
//        public void initMemberDB() {
//                Member member = Member.builder()
//                        .nickname("wyjax")
//                        .uid("38483918")
//                        .picture("https://avatars3.githubusercontent.com/u/38483918?v=4")
//                        .role(Role.USER)
//                        .build();
//            member = memberRepository.save(member);
////            session.setAttribute("user", new SessionUser(member));
//        }
//
//        public void initStudyDB() {
//            List<Member> members = memberRepository.findAll();
//            int cnt = 1;
//
//            for (int i = 0; i < members.size(); i++) {
//                for (int j = 0; j < 5; j++) {
//                    StudyRoom studyRoom = StudyRoom.builder()
//                            .roomName("test" + cnt)
//                            .roomDesc("test" + cnt)
//                            .joinNumm(1)
//                            .roomLimit(4)
//                            .member(members.get(i))
//                            .status(StudyStatus.READY)
//                            .techs("")
//                            .build();
//                    studyRepository.save(studyRoom);
//                    cnt++;
//
//                    StudyMember studyMember = StudyMember.builder()
//                            .studyRoom(studyRoom)
//                            .memberId(members.get(i).getUid())
//                            .build();
//                    studyMemberRepository.save(studyMember);
//                }
//            }
//        }
//
//        public void initContentDB() {
//            List<Member> members = memberRepository.findAll();
//
//            for (int i = 0; i < members.size(); i++) {
//                Member m = members.get(i);
//
//                for (int j = 0; j < 5; j++) {
//                    Content content = Content.builder()
//                            .member(m)
//                            .content("<script>alert('hi');</script>" + i)
//                            .title("<script>alert('hi');</script>" + i)
//                            .build();
//                    contentRepository.save(content);
//                }
//            }
//        }
//
//        public void initPush() {
//            boolean val = false;
//            for (int i = 1; i <= 15; i++) {
//                Push push = Push.builder()
//                        .message("님이 스터디를 신청했습니다 !")
//                        .url("study/1")
//                        .toid("38483918")
//                        .fromid("38483918")
//                        .isRead(val)
//                        .build();
//                pushRepository.save(push);
//                val = !val;
//            }
//        }
//    }
//}