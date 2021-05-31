package qna.domain.entity;

import lombok.ToString;
import qna.domain.entity.common.Deleteable;
import qna.domain.entity.common.TraceDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * create table question
 * (
 *     id         bigint generated by default as identity,
 *     contents   clob,
 *     created_at timestamp    not null,
 *     deleted    boolean      not null,
 *     title      varchar(100) not null,
 *     updated_at timestamp,
 *     writer_id  bigint,
 *     primary key (id)
 * )
 */
@Getter
@NoArgsConstructor
@Entity
@ToString(of = {"id", "contents", "title", "writer", "deleted"})
public class Question extends TraceDate implements Deleteable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "question")
    List<Answer> answers = new ArrayList<>();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    @Builder
    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.deleted = false;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    @Override
    public void deleted() {
        this.deleted = true;
    }
}