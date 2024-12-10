package com.os.memo.service;

import com.os.memo.dto.MemoDTO;
import com.os.memo.entity.Memo;
import com.os.user.entity.User;
import com.os.repository.MemoRepository;
import com.os.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final UserService userService;

    /**
     @method : findAll
     @desc :메모 전체를 페이지별로 조회하는 메서드
     @author : 한석희
     */
    public List<MemoDTO> findAll() {
        LocalDateTime week = LocalDateTime.now().minusWeeks(1);
        List<Memo> MemoList = memoRepository.findAllByCreateAtAfterOrderByCreateAtDesc(week);
        List<MemoDTO> MemoDTOList = new ArrayList<>();
        for(Memo Memo : MemoList){
            MemoDTOList.add(MemoDTO.toMemoDTO(Memo));
        }

        return MemoDTOList;
    }

    /**
     @method : findAllMemos
     @desc : 조회된 메모들을 메모 DTO로 변환하여 페이지 단위로 반환하는 메서드
     @author : 한석희
     */
    public Page<MemoDTO> findAllMemos(Pageable pageable) {
        Page<Memo> memoPage = memoRepository.findAll(pageable);

        return memoPage.map(MemoDTO::toMemoDTO);
    }

    /**
     @method : findByMemoContentsContaining
     @desc : 게시글 내용을 포함하고있는 메모를 찾아주는 메서드
     @author : 한석희
     */
    public Page<MemoDTO> findByMemoContentsContaining(String keyword, Pageable pageable) {
        Page<Memo> allPaymentsPage = memoRepository.findByMemoContentsContaining(keyword, pageable);

        return allPaymentsPage.map(MemoDTO::toMemoDTO);
    }

    /**
     @method : findByUserUsernameContaining
     @desc :  게시자 이름으로 메모 내용을 찾아주는 메서드
     @author : 한석희
     */
    // 작성자로 메모 검색
    public Page<MemoDTO> findByUserUsernameContaining(String keyword, Pageable pageable) {
        Page<Memo> allPaymentsPage = memoRepository.findByUserUsernameContaining(keyword, pageable);

        return allPaymentsPage.map(MemoDTO::toMemoDTO);
    }

    /**
     @method : findByMemoExposeYn
     @desc :  게시글 노출여부 (Y/N)으로 메모 내용을 찾아주는 메서드
     @author : 한석희
     */
    public Page<MemoDTO> findByMemoExposeYn(String keyword, Pageable pageable) {
        Page<Memo> allPaymentsPage = memoRepository.findByMemoExposeYn(keyword, pageable);

        return allPaymentsPage.map(MemoDTO::toMemoDTO);
    }

    /**
     @method : save
     @desc : 메모글을 작성하고, 저장할수 있게 해주는 메서드
     @author : 한석희
     */
    public void save(MemoDTO memoDTO) {
        User user = userService.findId();
        Memo memo = Memo.toEntity(memoDTO, user);

        memoRepository.save(memo);
    }

    /**
     @method : deleteSelectedItems
     @desc : 체크된 글을 삭제할 수 있게 해주는 메서드
     @author : 한석희
     */
    public void deleteSelectedItems(List<Long> selectedIds) {
        memoRepository.deleteAllById(selectedIds);
    }

    /**
     @method : findById
     @desc : 글 번호로 해당 글을 검색할 수 있게 해주는 메서드
     @author : 한석희
     */
    public MemoDTO findById(Long id) {
        Optional<Memo> optionalMemo = memoRepository.findById(id);
        if(optionalMemo.isPresent()) {
            Memo memo = optionalMemo.get();

            return MemoDTO.toMemoDTO(memo);
        } else {
            return null;
        }
    }

    /**
     @method : update
     @desc : 메모글 작성자와 로그인한 사용자가 같을 경우에만 게시글을 수정할 수 있게 해주는 메서드
     @author : 한석희
     */
    public void update(MemoDTO memoDTO){
        User user = userService.findId();
        Optional<Memo> memoOptional  = memoRepository.findById(memoDTO.getId());
        if (memoOptional.isPresent()) {
            Memo memo = memoOptional.get();

            if (memo.getUser().getId().equals(user.getId())) {
                memo.setMemoContents(memoDTO.getMemoContents());
                memo.setMemoExposeYn(memoDTO.getMemoExposeYn());

                memoRepository.save(memo);
            } else {
                System.out.println("오류");
            }
        }
    }
}
