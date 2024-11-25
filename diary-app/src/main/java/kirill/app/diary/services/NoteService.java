package kirill.app.diary.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kirill.app.diary.models.Note;
import kirill.app.diary.models.UserDiary;
import kirill.app.diary.repositories.NoteRepository;
import kirill.app.diary.repositories.UserRepository;

@Service
public class NoteService {


    private NoteRepository noteRepository;

    private UserRepository userRepository;

    
    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
    	this.noteRepository = noteRepository;
    	this.userRepository = userRepository;
    }
    
    public Note createNoteForUser(Long userId, Note note) {
        Optional<UserDiary> user = userRepository.findById(userId);
        if (user.isPresent()) {
            note.setUser(user.get());
            note.setTitle(note.getTitle());
            note.setContent(note.getContent());
            return noteRepository.save(note);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Note getNoteById(Long noteId) {
    	Optional<Note> note = noteRepository.findById(noteId);
    	return note.get();
    }
    public List<Note> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    public void updateNote(Note note) {
        noteRepository.save(note); 
    }
    
    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
