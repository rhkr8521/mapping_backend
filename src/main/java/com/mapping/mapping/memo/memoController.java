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
	
    //테이블 리스트 가져오기
	@GetMapping
	public Iterable<memo> list(){
		return boardRep.findAll();
	}
	
    //id로 테이블 값 가져오기
	@GetMapping(value = "/{id}")
	public Optional<memo> findOne(@PathVariable Long id) {
		return boardRep.findById(id);
	}

    //id로 테이블 값 수정
	@PutMapping(value = "/{id}")
	public memo update(@PathVariable Long id, @RequestParam String writer) {
		Optional<memo> board = boardRep.findById(id);
		board.get().setWriter(writer);
		return boardRep.save(board.get());
}
	
    //id로 테이블 값 삭제
	@DeleteMapping
	public void delete(@RequestParam Long id) {
		boardRep.deleteById(id);
	}
}