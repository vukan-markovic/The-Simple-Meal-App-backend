package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.model.Type;
import com.example.Fooddeliverysystem.repository.TypeRepo;
import com.example.Fooddeliverysystem.service.TypeServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("type")
public class TypeController {

    private TypeServiceI typeService;

    private TypeRepo typeRepo;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Type>> getTypes() {
        Collection<Type> result = typeService.getAllTypes();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/regular")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Type>> getRegularTypes() {
        Collection<Type> result = typeService.getRegularTypes();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Type> getType(@PathVariable("id") Integer id) {
        Type type = typeService.getType(id);

        if (type == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @CrossOrigin
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Type> deleteType(@PathVariable("id") Integer id) {
        if (!typeRepo.existsById(id))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        typeService.deleteType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Type> insertType(@RequestBody @Valid Type type) {
        Type result = typeService.insertType(type);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Type> updateType(@RequestBody @Valid Type type) {
        Type newType = typeService.updateType(type);

        if(newType==null) return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        return new ResponseEntity<>(newType,HttpStatus.ACCEPTED);
    }


    @Autowired
    public void setTypeService(TypeServiceI typeService) {
        this.typeService = typeService;
    }

    @Autowired
    public void setTypeRepo(TypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    //    @CrossOrigin
//    @PutMapping("/regularTypes/")
//    public ResponseEntity<List<Type>> updateRegularTypes(@RequestBody @Valid List<Type> types) {
//        List<Type> newType = typeService.updateRegularTypes(types);
//        if(newType.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
//        }
//        return new ResponseEntity<>(newType,HttpStatus.ACCEPTED);
//    }

}