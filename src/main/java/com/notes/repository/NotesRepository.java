package com.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.notes.models.NoteModel;

@Repository
public interface NotesRepository extends JpaRepository<NoteModel, String> {

	@Query(value = "select * from note_data where user_id = ?1 and  (title like %?2% or content like %?2%) order by updated_at desc", nativeQuery = true)
	List<NoteModel> getNotesByUserIdAndSearchKeyWord(String userId, String keyWord);

}

