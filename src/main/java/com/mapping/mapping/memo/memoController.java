package com.mapping.mapping.memo;

import java.util.List;
import java.util.Optional;

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

// CROS 설정 - 개빡치는놈
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/memo")
public class memoController {
	private memoRepository boardRep;

	@Value("${C_SERVER_IMGSAVE_ADDR}")
	private String SERVER_IMGSAVE_ADDR;

	@Value("${C_SERVER_DOMAIN}")
	private String SERVER_DOMAIN;
	
	@Autowired	
	public memoController(memoRepository boardRep) {
		this.boardRep = boardRep;
	}

    // URL POST 메모 생성 : POST(enctype="multipart/form-data") -> {url}/upload
    @PostMapping("/upload")
    public String createMemo(
        @RequestParam("file") MultipartFile file,
        @RequestParam("content") String content,
        @RequestParam("writer") String writer,
        @RequestParam("lat") String lat,
        @RequestParam("lng") String lng,
        @RequestParam("tag") String tag) throws IOException {

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

			String filename = file.getOriginalFilename();

			memo item = new memo();
            item.setContent(content);
            item.setWriter(writer);
            item.setLat(lat);
            item.setLng(lng);
            item.setDate(nowDate_DB);
            item.setTag(tag);

			// 만약 이미지파일이 들어오지않았을때 null 처리
			if (filename == ""){
				String img = "null";
				item.setImg(img);

			}else{
				File dest = new File(uploadPath + nowDate_Img + "_" + writer + ".jpg");
            	file.transferTo(dest);

				//저장된 이미지 불러오는 서버 주소
            	String img = "http://" + SERVER_DOMAIN + ":8080/images/" + nowDate_fd + "/" + nowDate_Img + "_" + writer + ".jpg";
           		item.setImg(img);
			}
			
            boardRep.save(item);

			
			System.out.println("\n" + nowDate_Img + ") 메모 생성 완료");
            return "INFO) Memo create Success";
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

	//작성자검색 : {url}/memo/search?writer={writer}
	@GetMapping(value = "/search")
	public List<memo> searchByWriter(@RequestParam String writer) {

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") 작성자 기준 메모 검색 완료");

    	return boardRep.findByWriter(writer);
	}

	//태그검색 : {url}/memo/tagsearch?tag={tag}
	@GetMapping(value = "/tagsearch")
	public List<memo> searchByTag(@RequestParam String tag){

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") 태그 기준 메모 로드 완료");

		return boardRep.findByTag(tag);
	}

    //메모수정 : {url}/memo/update/{id}?content={content}&writer={writer}&lat={lat}&lng={lng}&tag={tag} - 이미지수정 비할성화
	@GetMapping(value = "/update/{id}")
	public memo update(@PathVariable Long id, @RequestParam String content, @RequestParam String writer, @RequestParam String lat, @RequestParam String lng, @RequestParam String tag) {
		Optional<memo> board = boardRep.findById(id);
    	if (board.isPresent()) {

			LocalDateTime nowDate = LocalDateTime.now();
			String Update_Date_DB = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        	memo memoToUpdate = board.get();
        	memoToUpdate.setContent(content);
        	memoToUpdate.setWriter(writer);
        	memoToUpdate.setLat(lat);
        	memoToUpdate.setLng(lng);
        	memoToUpdate.setDate(Update_Date_DB);
        	memoToUpdate.setTag(tag);

			System.out.println("\n" + Update_Date_DB + ") ID : " + id  + " 메모 정보 업데이트 완료");
        	return boardRep.save(memoToUpdate);
    	} else {
        	throw new EntityNotFoundException("Error) M" + id + " Memo not found");
    	}
	}

    //메모삭제 : {url}/memo/delete?id={id}
	@GetMapping(value = "/delete")
	public void deleteById(@RequestParam(value = "id") Long id) {
    	boardRep.deleteById(id);

		LocalDateTime nowDate = LocalDateTime.now();
		String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

		System.out.println("\n" + nowDate_Img + ") ID : " + id + " 메모 삭제 완료");
	}

}