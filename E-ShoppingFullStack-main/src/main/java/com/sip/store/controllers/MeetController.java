package com.sip.store.controllers;

import com.sip.store.entities.Meet;
import com.sip.store.exception.ResourceNotFoundException;
import com.sip.store.repositories.MeetReposiotry;
import com.sip.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class MeetController {
    @Autowired
    MeetReposiotry meetReposiotry;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/ajouterMeet")
     public Meet Ajoutermeet(@RequestBody  Meet meet) throws ParseException {
        String heureDebutStr = meet.getHeuredebut();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date heureDebutDate = dateFormat.parse(heureDebutStr);
        Timestamp heureDebutTimestamp = new Timestamp(heureDebutDate.getTime());
        meet.setHeuredebut(String.valueOf(heureDebutTimestamp));
        String heureFinStr = meet.getHeurefin();
        Date heurefintDate = dateFormat.parse(heureFinStr);
        Timestamp heureFintTimestamp = new Timestamp(heurefintDate.getTime());
        meet.setHeurefin(String.valueOf(heureFintTimestamp));
         return meetReposiotry.save(meet);
     }
    @PutMapping("/meet/{id}")
    public Meet modifiermeet(@PathVariable("id") long id, @RequestBody Meet Meet) {
        Meet meet=null;
        Optional<Meet> meetinfo = meetReposiotry.findById(id);
        if (meetinfo.isPresent()) {
            meet = meetinfo.get();
            meet.setTitre(Meet.getTitre());
            meet.setDescription(Meet.getDescription());
            meet.setDate(Meet.getDate());
            meet.setHeuredebut(Meet.getHeuredebut());
            meet.setHeurefin(Meet.getHeurefin());
            meet.setLienmeet(Meet.getLienmeet());
            meetReposiotry.save(meet);
        }
        if(meet == null) throw new IllegalArgumentException("meet with id = "+ id +"not Found");
        return meet;
    }
    @DeleteMapping("/supprimmerMeet/{idmeet}")
    public Meet supprimermeet(@PathVariable Long idmeet)
    {
        Meet temp = null;

        Optional<Meet> opt = meetReposiotry.findById(idmeet);

        if(opt.isPresent())
        {
            temp = opt.get();
            meetReposiotry.delete(temp);

        }
        if(temp == null) throw new IllegalArgumentException("file with id = "+ idmeet +"not Found");
        return temp;
    }

    @GetMapping("/Getmeets")
    public List<Meet> getmeet(){
       return meetReposiotry.findAll();
    }
    @GetMapping("/getmeetbyid/{id}")
    public ResponseEntity<Meet> getmeetbyid(@PathVariable(value = "id") Long Id)
            throws ResourceNotFoundException {
        Meet meet = meetReposiotry.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Profrile not found for this id :: " + Id));
        return ResponseEntity.ok().body(meet);
    }

    @GetMapping("/getuserwithclasse")
     public List<Object[]> getuserbyclasse(){
      return userRepository.getUsersWithClasses();
     }

     @GetMapping("/getlienmeet")
     public List<Object[]>  getlienmeet(){
        return meetReposiotry.getLienmeet();
     }
}
