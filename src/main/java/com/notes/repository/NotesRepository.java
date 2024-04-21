package com.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.notes.models.NoteModel;

@Repository
public interface NotesRepository extends JpaRepository<NoteModel, String> {

	@Query(value = "select * from note_data where user_id = ?1 and  (title like %?2% or content like %?2%) order by case when is_favourite then 0 else 1 end, updated_at desc", nativeQuery = true)
	List<NoteModel> getNotesByUserIdAndSearchKeyWord(String userId, String keyWord);

	@Transactional
	@Modifying
	@Query(value = "update note_data set is_favourite = ?1 where id = ?2", nativeQuery = true)
	void updateFavourite(int isFavourite, String id);
}

