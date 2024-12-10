package com.os.controller;

import com.os.memo.dto.MemoDTO;
import com.os.user.entity.User;
import com.os.memo.service.MemoService;
import com.os.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;
    private final UserService userService;

    /**
     @method : saveForm
     @desc : 메모 작성 페이지로 리다이렉트 (이동)
     @author : 한석희
     */
    @GetMapping("/save")
    public String saveForm() { return "/memo/save"; }

    /**
     @method : save
     @desc : 메모글 작성 완료 및 저장 후, 대시보드 메인 페이지로 리다이렉트 (이동)
     @author : 한석희
     */
    @PostMapping("/save")
    public String save(@ModelAttribute MemoDTO memoDTO) throws IOException {
        System.out.println("memoDTO = " + memoDTO);
        memoService.save(memoDTO);
        return "redirect:/dashboard";
    }

    /**
     @method : findById
     @desc : 게시글을 글번호로 찾을 수 있게 하고, 다시 상세 페이지로 리다이렉트(이동)
     @author : 한석희
     */
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable) {
//        게시글 데이터를 가져와서 detail.html 에 출력

        MemoDTO memoDTO = memoService.findById(id);
        User session = userService.findId();
        Long sessionId = session.getId();

        model.addAttribute("sessionId", sessionId);
        model.addAttribute("Memo", memoDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "/memo/detail";
    }

    /**
     @method : updateForm
     @desc : 해당 메모를 수정할 수 있는 메서드
     @author : 한석희
     */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        MemoDTO memoDTO = memoService.findById(id);
        model.addAttribute("MemoUpdate",memoDTO);

        return "/memo/update";
    }

    /**
     @method : update
     @desc : 메모 글 수정 후, 다시 대시보드 메인 페이지로 리다이렉트 (이동)
     @author : 한석희
     */
    @PostMapping("/update")
    public String update(@ModelAttribute MemoDTO memoDTO, Model model) {
        memoService.update(memoDTO);

        return "redirect:/dashboard";
    }

    /**
     @method : deleteSelectedItems
     @desc : 체크된 메모글만 삭제할 수 있게 해 주는 기능
     @author : 한석희
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteSelectedItems(@RequestBody List<Long> selectedIds) {
        try {
            System.out.println("Controller => " + selectedIds);
            memoService.deleteSelectedItems(selectedIds);

            return ResponseEntity.ok("Selected items deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting selected items.");
        }
    }

    /**
     @method : paging
     @desc : 상세 글 페이지 밑의 페이지 번호를  표시해 줄 수 있게 해 주는 메서드
     @author : 한석희
     */
    @GetMapping("/paging")
    public String paging(Model model, @PageableDefault(sort = "CreateAt", direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "columnName", required = false) String columnName) {

        Page<MemoDTO> allMemosPage;

        // keyword 와 columnName 이 모두 제공되었는지 확인
        if (keyword != null && !keyword.isEmpty() && columnName != null && !columnName.isEmpty()) {
            if (columnName.equals("memoContents")) {
                allMemosPage = memoService.findByMemoContentsContaining(keyword, pageable);
            } else if (columnName.equals("userName")) {
                allMemosPage = memoService.findByUserUsernameContaining(keyword, pageable);
            } else if (columnName.equals("memoExposeYn")) {
                // columnName 이 memoExposeYn 이면서 keyword 가 Y 또는 N인 경우에만 처리
                if (keyword.equals("Y") || keyword.equals("N")) {
                    allMemosPage = memoService.findByMemoExposeYn(keyword, pageable);
                } else {
                    // 그 외의 경우에는 전체 메모 목록을 반환
                    allMemosPage = memoService.findAllMemos(pageable);
                }
            } else {
                allMemosPage = memoService.findAllMemos(pageable);
            }
        } else {
            allMemosPage = memoService.findAllMemos(pageable);
        }

        long memoCount = allMemosPage.getTotalElements();

        model.addAttribute("MemoList", allMemosPage);
        model.addAttribute("memoCount", memoCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("columnName", columnName); // 선택된 컬럼을 뷰로 전달

        return "/memo/paging";
    }
}
