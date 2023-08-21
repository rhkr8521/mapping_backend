package com.mapping.mapping.memo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping(value = "/memo")
public class memoController {
	private memoRepository boardRep;
	
	@Autowired	
	public memoController(memoRepository boardRep) {
		this.boardRep = boardRep;
	}
	
    // URL GET 메모 생성: {url}/memo/create?content={content}&writer={writer}&lat={lat}&log={log}&img={img}&date={date}&tag={tag}
	@GetMapping(value = "/create")
	public memo createFromParams(
    	@RequestParam String content,
    	@RequestParam String writer,
    	@RequestParam String lat,
    	@RequestParam String log,
    	@RequestParam String img,
    	@RequestParam String date,
    	@RequestParam String tag
	) {
    	memo newMemo = new memo(content, writer, lat, log, img, date, tag);
    	return boardRep.save(newMemo);
	}

	// URL POST 메모 생성
	@PostMapping("/upload")
	public String createMemo(
		@RequestParam("file") MultipartFile file,
		@RequestParam("content") String content,
		@RequestParam("writer") String writer,
		@RequestParam("lat") String lat,
		@RequestParam("log") String log,
		@RequestParam("date") String date,
		@RequestParam("tag") String tag) throws IOException {

			String uploadPath = "/uploaded/";
			String filename = file.getOriginalFilename();
			File dest = new File(uploadPath + filename);
			file.transferTo(dest);

			String img = "http://localhost:8080/uploaded/" + filename;

			memo item = new memo();
			item.setContent(content);
			item.setWriter(writer);
			item.setLat(lat);
			item.setLog(log);
			item.setDate(date);
			item.setImg(img);
			item.setTag(tag);

			boardRep.save(item);

			return "Memo create Success";
		}
	
    //메모 전체 로드 : {url}/memo - 작동
	@GetMapping
	public Iterable<memo> list(){
		return boardRep.findAll();
	}
	
    //ID검색 : {url}/memo/info/{id}
	@GetMapping(value = "/info/{id}")
	public Optional<memo> findOne(@PathVariable Long id) {
		return boardRep.findById(id);
	}

	//내용 일부 검색 : {url}/memo/content_search?content={검색할 내용}
	@GetMapping(value = "/content_search")
	public List<memo> searchByContent(@RequestParam String content) {
    	return boardRep.findByContentContains(content);
	}

	//작성자검색 : {url}/memo/search?writer={writer}
	@GetMapping(value = "/search")
	public List<memo> searchByWriter(@RequestParam String writer) {
    	return boardRep.findByWriter(writer);
	}

	//태그검색 : {url}/memo/tagsearch?tag={tag}
	@GetMapping(value = "/tagsearch")
	public List<memo> searchByTag(@RequestParam String tag){
		return boardRep.findByTag(tag);
	}

    //메모수정 : {url}/memo/update/{id}?content={content}&writer={writer}&lat={lat}&log={log}&img={img}&date={date}&tag={tag}
	@GetMapping(value = "/update/{id}")
	public memo update(@PathVariable Long id, @RequestParam String content, @RequestParam String writer, @RequestParam String lat, @RequestParam String log, @RequestParam String img, @RequestParam String date, @RequestParam String tag) {
		Optional<memo> board = boardRep.findById(id);
    	if (board.isPresent()) {
        	memo memoToUpdate = board.get();
        	memoToUpdate.setContent(content);
        	memoToUpdate.setWriter(writer);
        	memoToUpdate.setLat(lat);
        	memoToUpdate.setLog(log);
        	memoToUpdate.setImg(img);
        	memoToUpdate.setDate(date);
        	memoToUpdate.setTag(tag);
        	return boardRep.save(memoToUpdate);
    	} else {
        	throw new EntityNotFoundException("Error) M" + id + " Memo not found");
    	}
	}

    //메모삭제 : {url}/memo/delete?id={id}
	@GetMapping(value = "/delete")
	public void deleteById(@RequestParam(value = "id") Long id) {
    	boardRep.deleteById(id);
	}

}