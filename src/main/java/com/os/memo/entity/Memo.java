package com.os.memo.entity;

import com.os.memo.dto.MemoDTO;
import com.os.user.entity.User;
import com.os.util.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "memo_id")
    private Long id;

    @Column(nullable = false)
    @Length(min = 1 , max = 300 , message = "내용을 최소 1글자부터 300글자 사이로 입력해주세요.")
    private String memoContents;

    @Column(nullable = false)
    @Length(min = 1 , max = 1 , message = "게시글 노출여부를 반드시 선택해 주셔야 합니다.")
    private String memoExposeYn;

    @Column(nullable = false)
    private String memoDelYn;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",nullable = false)
    private User user;

    public static Memo toEntity(MemoDTO memoDTO, User user) {
        Memo memo = new Memo();
        memo.setUser(user);
        memo.setMemoContents(memoDTO.getMemoContents());
        memo.setMemoExposeYn(memoDTO.getMemoExposeYn());
        memo.setMemoDelYn("N");

        return memo;
    }
}
