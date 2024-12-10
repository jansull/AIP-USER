package com.os.memo.dto;

import com.os.memo.entity.Memo;
import lombok.*;

import java.time.LocalDateTime;

// DTO => (Data Transfer Object), VO, Bean,        Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor
public class MemoDTO {
    private Long id;

    private String userName;

    private String memoContents;

    private LocalDateTime CreateAt;

    private LocalDateTime UpdateAt;

    private String memoExposeYn;

    private String memoDelYn;

    private Long userId;

    public static MemoDTO toMemoDTO(Memo memo) {
        MemoDTO memoDTO = new MemoDTO();
        memoDTO.setId(memo.getId());
        memoDTO.setUserId(memoDTO.getUserId());
        memoDTO.setUserName(memo.getUser().getUsername());
        memoDTO.setMemoContents(memo.getMemoContents());
        memoDTO.setMemoExposeYn(memo.getMemoExposeYn());
        memoDTO.setCreateAt(memo.getCreateAt());
        memoDTO.setUpdateAt(memo.getUpdateAt());

        return memoDTO;
    }
}
