
package com.project.project.Service;

import com.project.project.Entity.Notes;
import com.project.project.Entity.User;
import com.project.project.Repository.NotesRepository;
import com.project.project.Repository.UserRepository;
import jakarta.ws.rs.*;
        import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Component
@Path("/note")
@CrossOrigin(origins = "http://localhost:3000")
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserRepository userRepository;


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addNoteToUser(Notes note) {
        Long userId = note.getUser().getId();
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "User not found. Note not added.";
        }
        note.setUser(user);
        notesRepository.save(note);

        return "Note added successfully!";
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteNoteById(@PathParam("id") Long id) {
        if (notesRepository.existsById(id)) {
            notesRepository.deleteById(id);
            return "Note supprimée avec succès.";
        } else {
            return "Erreur : la note avec l'ID " + id + " est introuvable.";
        }
    }
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoteById(@PathParam("id") Long id) {
        Notes note = notesRepository.findById(id).orElse(null);

        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Note with ID " + id + " not found.")
                    .build();
        }

        NoteResponse noteResponse = new NoteResponse(note.getTitre(), note.getDescription());

        return Response.ok(noteResponse).build();
    }
        @PUT
        @Path("/update/{id}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response updateNote(@PathParam("id") Long id, Notes updatedNote) {
            Notes existingNote = notesRepository.findById(id).orElse(null);

            if (existingNote == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Note with ID " + id + " not found.")
                        .build();
            }

            existingNote.setTitre(updatedNote.getTitre());
            existingNote.setDescription(updatedNote.getDescription());

            notesRepository.save(existingNote);

            return Response.ok(existingNote).build();
        }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNotes() {
        return Response.ok(notesRepository.findAll()).build();
    }
    @GET
    @Path("/all/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNotesByUser(@PathParam("userId") Long userId) {
        List<Notes> notes = notesRepository.findByUserId(userId);

        if (notes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No notes found for user with ID " + userId)
                    .build();
        }

        return Response.ok(notes).build();
    }
    public static class NoteResponse {
        private String titre;
        private String description;

        public NoteResponse(String titre, String description) {
            this.titre = titre;
            this.description = description;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }



}






