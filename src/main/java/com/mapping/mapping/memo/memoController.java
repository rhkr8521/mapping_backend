package com.mapping.mapping.memo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/memo")
public class memoController {
	private memoRepository boardRep;
	
	@Autowired	
	public memoController(memoRepository boardRep) {
		this.boardRep = boardRep;
	}
	
    //POST로 유저 추가
	@PostMapping
	public memo put(@RequestParam String content, @RequestParam String writer, @RequestParam String lat, @RequestParam String log, @RequestParam String img, @RequestParam String date, @RequestParam String tag) {
		return boardRep.save(new memo(content, writer, lat, log, img, date, tag));
	}
	
    //메모 전체 로드 : {url}/memo
	@GetMapping
	public Iterable<memo> list(){
		return boardRep.findAll();
	}
	
    //ID검색 : {url}/memo/{id}
	@GetMapping(value = "/{id}")
	public Optional<memo> findOne(@PathVariable Long id) {
		return boardRep.findById(id);
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

    //id로 테이블 값 수정
	@PutMapping(value = "/{id}")
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
        	throw new EntityNotFoundException("Memo with id " + id + " not found");
    	}
	}

	
    //id로 테이블 값 삭제
	@DeleteMapping(value = "/delete")
	public void deleteById(@RequestParam(value = "id") Long id) {
    	boardRep.deleteById(id);
	}

}