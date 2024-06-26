package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.AWSService;
import com.catchup.catchup.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ModalController {

    private final MyPageService service;
    private final AWSService S3Service;

    @PostMapping("/profileupdate")
    public ResponseEntity<String> updateProfile(HttpServletRequest request
            , @RequestBody UserDTO dto, Model model) {
        // 세션 처리 완료되면 변경하기
        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        log.info("nickname >>>>>>>>>>>>>>> {}", dto.getNickname());
        log.info("profile >>>>>>>>>>>> {} ", dto.getProfile());

        String profile = service.updateNickname(uid, dto.getNickname(), dto.getProfile());
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/api")
    public String loadAndSave(@RequestBody UserDTO dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        String schoolName = dto.getSchoolName();
        String sidoCode = dto.getSidoCode();
        String sDCode = dto.getSDCode();

        System.out.println(schoolName);
        System.out.println(sidoCode);
        System.out.println(sDCode);


        /*String result = "";
        try {
            URL url = new URL("https://open.neis.go.kr/hub/schoolInfo?KEY=7578fdd86a164ce4b0eebabfdbf51a5f&Type=json?pIndex=1?pSize=100&ATPT_OFCDC_SC_CODE=J10&SCHUL_NM=" + schoolName);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection() ;
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            for(int i=0; i<jsonArray.size(); i++) {
                JSONObject object = (JSONObject)jsonArray.get(i);
                String SD_code = (String) object.get("SD_SCHUL_CODE");
                dto.setSDCode(SD_code);
                service.schoolInsert(dto, uid);
            }
        } catch (Exception e) {
            System.out.println(e);
        }*/

        model.addAttribute("view", "mypage/home");
        return "index";
    }
}
