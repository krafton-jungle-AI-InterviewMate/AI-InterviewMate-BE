package jungle.krafton.AIInterviewMate.jwt;

import jungle.krafton.AIInterviewMate.domain.AuthProvider;
import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import jungle.krafton.AIInterviewMate.dto.login.OAuthAttributeDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionBoxRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final QuestionBoxRepository questionBoxRepository;

    private final QuestionRepository questionRepository;

    // OAuth2UserRequest에 있는 Access Token으로 유저정보 get
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return process(oAuth2UserRequest, oAuth2User);
    }

    // 획득한 유저정보를 Java Model과 맵핑하고 프로세스 진행
    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributeDto attributes = OAuthAttributeDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        if (attributes.getEmail().isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_MEMBER);
        }
        Optional<Member> userOptional = memberRepository.findByEmail(attributes.getEmail());
        Member user;

        if (userOptional.isPresent()) {        // 이미 가입된 경우
            user = userOptional.get();
            if (authProvider != user.getAuthProvider()) {
                throw new PrivateException(StatusCode.WRONG_SOCIAL_LOGIN_TYPE);
            }

        } else {            // 가입되지 않은 경우 ( 유저 생성 시 질문 꾸러미 10개 + 첫 꾸러미에 기본 질문 10개 추가 )
            user = createUser(attributes, authProvider);

            List<QuestionBox> boxes = new ArrayList<>();

            for (int i = 1; i < 11; i++) {
                QuestionBox questionBox = QuestionBox.builder()
                        .member(user)
                        .boxName("질문꾸러미-" + i)
                        .questionNum(i == 1 ? 10 : 0)
                        .build();
                boxes.add(questionBox);
            }
            questionBoxRepository.saveAll(boxes);
            List<Question> questions;
            try {
                questions = getQuestionList(boxes.get(0));
            } catch (IOException e) {
                throw new PrivateException(StatusCode.FILE_BUFFER_ERROR);
            }
            questionRepository.saveAll(questions);
        }
        return CustomUserDetails.create(user, oAuth2User.getAttributes());
    }

    private Member createUser(OAuthAttributeDto userInfo, AuthProvider authProvider) {
        Member user = Member.builder()
                .nickname(userInfo.getName())
                .email(userInfo.getEmail())
                .authProvider(authProvider)
                .build();
        return memberRepository.save(user);
    }

    public List<Question> getQuestionList(QuestionBox questionBox) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("./src/main/resources/InitQuestion.txt") // TODO: EC2 서버에 배포 시 파일 경로 변경
//                new FileReader("./InitQuestion.txt") // TODO: EC2 서버에 배포 시 파일 경로 변경
        );

        List<Question> questions = new ArrayList<>();
        String str;
        while ((str = reader.readLine()) != null) {
            List<String> line = new ArrayList<>(List.of(str.split(";")));

            for (int i = line.size(); i < 6; i++) {
                line.add(null);
            }

            Question question = Question.builder()
                    .questionTitle(line.get(0))
                    .questionBox(questionBox)
                    .keyword1(line.get(1))
                    .keyword2(line.get(2))
                    .keyword3(line.get(3))
                    .keyword4(line.get(4))
                    .keyword5(line.get(5))
                    .build();

            questions.add(question);
        }

        reader.close();

        return questions;
    }
}
