package kirill.app.diary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kirill.app.diary.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

	List<Note> findByUserId(Long userId);
}
