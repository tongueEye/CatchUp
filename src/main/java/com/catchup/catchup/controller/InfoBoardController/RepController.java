package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.service.InfoBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepController {
    private final InfoBoardService infoService;

    @GetMapping("/repcontent/{iid}")
    public ResponseEntity<List<String>> list(
            @PathVariable Long iid
    ){
        List<String> repcontent = infoService.getRep(iid);
        return ResponseEntity.ok()
                .body(repcontent);
    }

    @PostMapping("/addrep")
    public ResponseEntity<InfoBoardDTO> addRepContent(@RequestBody InfoBoardDTO infoBoardDTO) {
        InfoBoardDTO updatedInfoBoard = infoService.updateRep(infoBoardDTO);
        return ResponseEntity.ok().body(updatedInfoBoard);
    }

}
