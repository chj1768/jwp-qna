package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @Test
    @DisplayName("답변 삭제하기 로직(성공)")
    void deleteAnswer() {
        Question question1 = new Question("t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(UserTest.JAVAJIGI, question1, "answer c1");
        assertThatCode(()->{
            DeleteHistory deleted = answer1.delete(answer1.getWriter());
            assertThat(answer1).extracting("deleted").isEqualTo(true);
            assertThat(deleted).extracting("contentId").isEqualTo(answer1.getId());
            assertThat(deleted.getContentType()).isEqualTo(ContentType.ANSWER);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("답변 삭제하기 로직(실패-작성자가 다를경우")
    void deleteAnswerFail() {
        Question question1 = new Question("t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(UserTest.JAVAJIGI, question1, "answer c1");
        assertThatThrownBy(()->{
            answer1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
