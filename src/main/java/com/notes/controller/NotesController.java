package com.notes.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes.models.NoteModel;
import com.notes.models.NotesResponse;
import com.notes.models.User;
import com.notes.repository.NotesRepository;
import com.notes.repository.UserRepository;

import jakarta.annotation.Nullable;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/notes")
public class NotesController {

	ObjectMapper mapper = new ObjectMapper();
	
	Logger LOGGER = LoggerFactory.getLogger(NotesController.class);
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private UserRepository userRepo;
			
	@PostMapping("/create")
	public NotesResponse addNotes(@RequestBody NoteModel noteModel) {
		NotesResponse response = new NotesResponse(HttpStatus.BAD_REQUEST.value(), "FAIL",
				HttpStatus.BAD_REQUEST.getReasonPhrase());
		try {
			 LocalDateTime now = LocalDateTime.now();
			if(noteModel.getId() == null) {
				noteModel.setCreatedAt(now);
			}
			noteModel.setUpdatedAt(now);
			
			notesRepository.save(noteModel);
			response = new NotesResponse(HttpStatus.CREATED.value(), "SUCCESS", "Notes saved successfully");
		}catch(Exception e) {
			response = new NotesResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL",
					e.getMessage());
		}
		return response;
	}	
	
	@GetMapping("/getAll/{userId}/{searchKeyWord}")
	public NotesResponse getAllNotes(@PathVariable(value="userId") String userId, @PathVariable(value = "searchKeyWord") @Nullable String searchKeyWord) {
		NotesResponse response = new NotesResponse(HttpStatus.BAD_REQUEST.value(), "FAIL",
				HttpStatus.BAD_REQUEST.getReasonPhrase());
		try {
			 if ("null".equals(searchKeyWord)) {
		            searchKeyWord = "";
		        }
			
			List<NoteModel> datas = notesRepository.getNotesByUserIdAndSearchKeyWord(userId, searchKeyWord);
			response = new NotesResponse(200, "SUCCESS", "Record Fetched successfully", datas);
		}catch(Exception e) {
			response = new NotesResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL",
					e.getMessage());
		}
		return response;
	}
	
	@GetMapping("/user/{userName}")
	public ResponseEntity<User> getUserInfo(@PathVariable String userName){
		 try {
             User userResponse = userRepo.findByusername(userName);
             return ResponseEntity.ok(userResponse);
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
	}
	
	@GetMapping("/{id}")
	public NotesResponse getNoteById(@PathVariable String id){
		NotesResponse response = new NotesResponse(HttpStatus.BAD_REQUEST.value(), "FAIL",
				HttpStatus.BAD_REQUEST.getReasonPhrase());
		 try {
             NoteModel note = notesRepository.findById(id).get();
             response = new NotesResponse(200, "SUCCESS", "Record Fetched successfully", note);
         } catch (Exception e) {
        	 response = new NotesResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL",
 					e.getMessage());
         }
		 return response;
	}
	
	@DeleteMapping("/delete/{id}")
	public NotesResponse deleteNoteById(@PathVariable String id) {
		NotesResponse response = new NotesResponse(HttpStatus.BAD_REQUEST.value(), "FAIL",
				HttpStatus.BAD_REQUEST.getReasonPhrase());
		try {
			
			notesRepository.deleteById(id);
            response = new NotesResponse(200, "SUCCESS", "Record Deleted successfully");

			
		}catch(Exception e) {
			response = new NotesResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL",
 					e.getMessage());
		}
		return response;
		
	}
	
	@GetMapping("/download/{noteId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String noteId) throws Exception {
    	try {
    		 NoteModel model = notesRepository.findById(noteId).get();

//           String htmlContent = "<html><body><h1>Hello, PDF!</h1></body></html>"; // Your HTML content
           StringBuilder htmlContent = new StringBuilder();
           htmlContent.append("<html><body><h1>");
           htmlContent.append(model.getTitle());
           htmlContent.append("</h1>");
           htmlContent.append(model.getContent());
           htmlContent.append("</body></html>");
           
           byte[] pdfBytes = generatePdfFromHtml(htmlContent.toString());

           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_PDF);
           headers.setContentDispositionFormData("filename", model.getTitle()+".pdf");

           return ResponseEntity.ok()
                   .headers(headers)
                   .body(pdfBytes);
    	}catch (Exception e) {
			e.printStackTrace();
		}
       return null;
    }
	
	 public byte[] generatePdfFromHtml(String htmlContent) throws Exception {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocumentFromString(htmlContent);
	        renderer.layout();
	        renderer.createPDF(outputStream);
	        renderer.finishPDF();
	        return outputStream.toByteArray();
	    }
	 
	 @PatchMapping("/favourite")
	 public NotesResponse updateFavourite(@RequestBody NoteModel payload) {
			NotesResponse response = new NotesResponse(HttpStatus.BAD_REQUEST.value(), "FAIL",
					HttpStatus.BAD_REQUEST.getReasonPhrase());
			try {
//				LOGGER.info("payload {}", mapper.writeValueAsString(payload) );
				System.out.println(payload.getIsFavourite() );
				System.out.println(payload.getId());
				notesRepository.updateFavourite(payload.getIsFavourite() ? 1 :0, payload.getId());			
	            response = new NotesResponse(200, "SUCCESS", "Record updated successfully");

				
			}catch(Exception e) {
				response = new NotesResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL",
	 					e.getMessage());
			}
			return response;
	 }
}
