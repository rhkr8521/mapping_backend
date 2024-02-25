package com.mapping.mapping.memo;

import com.mapping.mapping.user.security.TokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

// CORS 설정 - 개빡치는놈
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/memo")
public class memoController{
	private memoRepository boardRep;
	
	@Autowired	
	public memoController(memoRepository boardRep) {
		this.boardRep = boardRep;
	}

	@Value("${C_SERVER_IMGSAVE_ADDR}")
    private String SERVER_IMGSAVE_ADDR;

    @Value("${C_SERVER_DOMAIN}")
    private String SERVER_DOMAIN;

	@Autowired
    private TokenProvider tokenProvider;

 	// URL POST 메모 생성 : POST(enctype="multipart/form-data") -> {url}/upload
    @PostMapping("/upload")
    public String createMemo(
        HttpServletRequest request,
        @RequestParam(value = "file",required = false) MultipartFile file,
        @RequestParam("content") String content,
        @RequestParam("lat") String lat,
        @RequestParam("lng") String lng,
        @RequestParam("tag") String tag,
		@RequestParam("ip") String ip) throws IOException {
            
            try{
                String token = parseBearerToken(request);
                if(token != null && !token.equalsIgnoreCase("null")){
                    String userNickname = tokenProvider.validate(token);

            if (userNickname == null){
                return "잘못된 접근입니다.";
            }else{
            LocalDateTime nowDate = LocalDateTime.now();
    
            String nowDate_DB = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
            String nowDate_fd = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //이미지 업로드 경로 지정(절대경로)
            String uploadPath = SERVER_IMGSAVE_ADDR + "/"+ nowDate_fd + "/";

            File UploadFolder = new File(uploadPath);

             //만약 해당 날짜 폴더가 없다면 생성
            if (!UploadFolder.exists()) {
                try{
                    UploadFolder.mkdir();
                    } 
                    catch(Exception e){
                    e.getStackTrace();
                }        
                }else {
            }

            memo item = new memo();
            item.setContent(content);
            item.setWriter(userNickname);
            item.setLat(lat);
            item.setLng(lng);
            item.setDate(nowDate_DB);
            item.setTag(tag);
			item.setIp(ip);

            // 만약 이미지파일이 들어오지않았을때 null 처리
            if (file == null || file.isEmpty()){
                String img = "null";
                item.setImg(img);

            }else{
                File dest = new File(uploadPath + nowDate_Img + "_" + userNickname + ".jpg");
                file.transferTo(dest);

                //저장된 이미지 불러오는 서버 주소
                String img = "https://" + SERVER_DOMAIN + ":81/api/images/" + nowDate_fd + "/" + nowDate_Img + "_" + userNickname + ".jpg";
                item.setImg(img);
            }
            
            boardRep.save(item);

            
            System.out.println("\n" + nowDate_Img + ") 메모 생성 완료");
            return "INFO) Memo create Success";
                }
            }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        return "INFO) Memo create fail";
    }   
	
    //메모 전체 로드 : {url}/memo - 작동
	@GetMapping
	public Iterable<memo> list(){

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") 메모 전체 로드 완료");
		return boardRep.findAll();
	}
	
    //ID검색 : {url}/memo/info/{id}
	@GetMapping(value = "/info/{id}")
	public Optional<memo> findOne(@PathVariable Long id) {

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") ID 기준 메모 검색 완료");
		return boardRep.findById(id);
	}

	//내용 일부 검색 : {url}/memo/content_search?content={검색할 내용}
	@GetMapping(value = "/content_search")
	public List<memo> searchByContent(@RequestParam String content) {

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") 메모 컨텐츠 검색 완료");

    	return boardRep.findByContentContains(content);
	}

	//자기가 만든 메모만 불러오기(With token) : {url}/memo/search
	@GetMapping(value = "/search")
	public List<memo> searchByWriter(HttpServletRequest request) {
 	   try {
  	    	String token = parseBearerToken(request);
   	    	if (token != null && !token.equalsIgnoreCase("null")) {
        	    String userNickname = tokenProvider.validate(token);
        	    if (userNickname != null) {
            	    LocalDateTime nowDate = LocalDateTime.now();
            	    String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

					System.out.println("\n" + nowDate_Img + ") 작성자 기준 메모 검색 완료");
            	    return boardRep.findByWriter(userNickname);
            	}
        	}
    	} catch (Exception exception) {
        	exception.printStackTrace();
    	}

		// 빈 리스트 반환
    	return Collections.emptyList();
	}

	//태그검색 : {url}/memo/tagsearch?tag={tag}
	@GetMapping(value = "/tagsearch")
	public List<memo> searchByTag(@RequestParam String tag){

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") 태그 기준 메모 로드 완료");

		return boardRep.findByTag(tag);
	}

	//메모 수정 : {url}/update{id}
	@PostMapping("/update/{id}")
	public String updateMemo(
		HttpServletRequest request,
		@PathVariable Long id,
		@RequestParam(value = "file", required = false) MultipartFile file,
		@RequestParam("content") String content,
		@RequestParam("lat") String lat,
		@RequestParam("lng") String lng,
		@RequestParam("tag") String tag,
		@RequestParam("ip") String ip) throws IOException {
	
		try {
			String token = parseBearerToken(request);
			if (token != null && !token.equalsIgnoreCase("null")) {
				String userNickname = tokenProvider.validate(token);
	
				if (userNickname == null) {
					return "잘못된 접근입니다.";
				} else {
					Optional<memo> memoOptional = boardRep.findById(id);
					if (memoOptional.isPresent()) {
						memo memoToUpdate = memoOptional.get();
	
						if (!memoToUpdate.getWriter().equals(userNickname)) {
							return "작성자와 로그인 사용자가 다릅니다.";
						}
	
						LocalDateTime nowDate = LocalDateTime.now();
						String nowDate_DB = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	            		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
            			String nowDate_fd = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

						//이미지 업로드 경로 지정(절대경로)
           				String uploadPath = SERVER_IMGSAVE_ADDR + "/"+ nowDate_fd + "/";
            			File UploadFolder = new File(uploadPath);

						//만약 해당 날짜 폴더가 없다면 생성
            			if (!UploadFolder.exists()) {
                			try{
                    		UploadFolder.mkdir();
                    	} 
                    		catch(Exception e){
                    		e.getStackTrace();
                		}        
                		}
	
						// 만약 이미지파일이 들어오지않았을때 null 처리
            			if (file == null || file.isEmpty()){

            			}else{
                		File dest = new File(uploadPath + nowDate_Img + "_" + userNickname + ".jpg");
                		file.transferTo(dest);

               		 	//저장된 이미지 불러오는 서버 주소
                		String img = "https://" + SERVER_DOMAIN + ":81/api/images/" + nowDate_fd + "/" + nowDate_Img + "_" + userNickname + ".jpg";
                		memoToUpdate.setImg(img);
            		}

						memoToUpdate.setContent(content);
						memoToUpdate.setLat(lat);
						memoToUpdate.setLng(lng);
						memoToUpdate.setDate(nowDate_DB);
						memoToUpdate.setTag(tag);
						memoToUpdate.setIp(ip);
	
						boardRep.save(memoToUpdate);
	
						System.out.println("\n" + nowDate_DB + ") 메모 업데이트 완료");
						return "INFO) Memo update Success";
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return "INFO) Memo update fail";
	}
	

    //메모삭제 : {url}/memo/delete?id={id}
	@GetMapping(value = "/delete")
	public String deleteById(HttpServletRequest request, @RequestParam(value = "id") Long id) {
		try {
			String token = parseBearerToken(request);
			if (token != null && !token.equalsIgnoreCase("null")) {
				String userNickname = tokenProvider.validate(token);
	
				LocalDateTime nowDate = LocalDateTime.now();
				String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
	
				System.out.println("\n" + nowDate_Img + ") ID : " + id + " 메모 삭제 시도");
	
				Optional<memo> memoOptional = boardRep.findById(id);
				if (memoOptional.isPresent()) {
					String writer = memoOptional.get().getWriter();
	
					if (writer.equals(userNickname)) {
						boardRep.deleteById(id);
						System.out.println("메모 삭제 완료");
						return "메모 삭제 완료";
					} else {
						System.out.println("작성자와 로그인 사용자가 다릅니다.");
						return "작성자와 로그인 사용자가 다릅니다.";
					}
				} else {
					System.out.println("해당 ID의 메모가 없습니다.");
					return "해당 ID의 메모가 없습니다.";
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return "An error occurred";
	}
	
    //Request Header에서 Authorization 필드의 Bearer Token을 가져오는 메서드
    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        //문자를 가지고 있는지 bearerToken이 맞는지 확인, 맞으면 7번째 문자부터 가져온다.
        if(org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

}
