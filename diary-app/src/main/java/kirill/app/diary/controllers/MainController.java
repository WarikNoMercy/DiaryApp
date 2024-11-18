package kirill.app.diary.controllers;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kirill.app.diary.models.Note;
import kirill.app.diary.models.UserDiary;
import kirill.app.diary.services.NoteService;
import kirill.app.diary.services.UserService;


@Controller
public class MainController {


	@Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;
    
	@GetMapping("/home")
	public String homePage(Model model, Principal principal) {
		
		String username = principal.getName();
        UserDiary user = userService.findByUsername(username).orElseThrow();
        
        List<Note> notes = noteService.getNotesByUserId(user.getId());
        model.addAttribute("username", username);
        model.addAttribute("notes",notes);
		return "index";
	}
	
	@GetMapping("/note/{id}")
    public String viewNote(@PathVariable("id") Long id, Model model) {
        Note note = noteService.getNoteById(id);

        if (!isUserAuthorized(note)) {
            return "error403";
        }
        if (note == null) {
            return "404"; 
        }
        model.addAttribute("note", note);
        return "view-note"; 
    }
	
	@GetMapping("/create-note")
    public String createNoteForm(Model model) {
        model.addAttribute("note", new Note());
        return "create-note";
    }

	
    @PostMapping("/create-note")
    public String createNote(@ModelAttribute Note note, Principal principal) {
        String username = principal.getName();
        UserDiary user = userService.findByUsername(username).orElseThrow();
        
        noteService.createNoteForUser(user.getId(), note);
        return "redirect:/home"; 
    }
    
    @GetMapping("/note/edit/{id}")
    public String editNotePage(@PathVariable("id") Long id, Model model) {
    	
        Note note = noteService.getNoteById(id);
        if (!isUserAuthorized(note)) {
            return "error403";
        }
        if (note == null) {
            return "404";
        }
        model.addAttribute("note", note);
        return "edit-note";
    }

    
    @PostMapping("/note/edit/{id}")
    public String editNote(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content) {

        Note note = noteService.getNoteById(id);
        if (!isUserAuthorized(note)) {
            return "error403";
        }
        if (note == null) {
            return "404";
        }
        note.setTitle(title);
        note.setContent(content);
        noteService.updateNote(note); 

        return "redirect:/note/" + id;
    }
    
	@GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }
	
	private boolean isUserAuthorized(Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); 

        return note != null && note.getUser() != null && note.getUser().getUsername().equals(currentUsername);
    }
}
