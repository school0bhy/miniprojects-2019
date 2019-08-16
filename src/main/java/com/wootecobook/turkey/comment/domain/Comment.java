package com.wootecobook.turkey.comment.domain;

import com.wootecobook.turkey.commons.BaseEntity;
import com.wootecobook.turkey.post.domain.Post;
import com.wootecobook.turkey.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    protected static final String CONTENTS_DELETE_MESSAGE = "삭제된 글입니다.";

    @Lob
    @Column(nullable = false)
    private String contents;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_comment_to_post"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_comment_to_comment"))
    private Comment parent;

    @Builder
    public Comment(final String contents, final User user, final Post post, final Comment parent) {
        this.contents = contents;
        this.user = user;
        this.post = post;
        this.parent = parent;
        this.isDeleted = false;
    }

    public boolean isWrittenBy(Long userId) {
        if (this.user.getId().equals(userId)) {
            return true;
        }
        throw new NotCommentOwnerException();
    }

    public void update(final Comment other) {
        this.contents = other.getContents();
    }

    public void delete() {
        isDeleted = true;
        contents = CONTENTS_DELETE_MESSAGE;
    }
}
