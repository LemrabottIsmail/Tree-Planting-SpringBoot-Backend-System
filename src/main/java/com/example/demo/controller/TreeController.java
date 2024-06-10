package com.example.demo.controller;

import com.example.demo.model.Tree;
import com.example.demo.model.dto.TreeDto;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.controller.AuthenticationController.AUTHORIZATION_HEADER;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tree")
public class TreeController {

    @Autowired
    private TreeService treeService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("userId") Long userId
            , @RequestParam("treeLocation") String treeLocation
            , @RequestParam("species") String species
            , @RequestParam("plantingTime") String plantingTime
            , @RequestParam("image") MultipartFile file
            , @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) throws IOException { // Optional<String> : might be present, not present -> throw 401, present -> validateToken
        authenticationService.authenticateUser(bearerToken);
        TreeDto newTree = new TreeDto();

        newTree.setUserId(userId);
        newTree.setTreeLocation(treeLocation);
        newTree.setSpecies(species);
        newTree.setPlantingTime(plantingTime);

        String uploadImage = treeService.uploadTreeImage(newTree, file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("/download/{treeId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long treeId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) throws Exception {
        authenticationService.authenticateUser(bearerToken);
        byte[] imageData = treeService.downloadImage(treeId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(IMAGE_PNG_VALUE))
                .body(imageData);
    }

    @GetMapping("/find-tree/{treeId}")
    public TreeDto getTreeById(@PathVariable Long treeId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) throws Exception {
        authenticationService.authenticateUser(bearerToken);
        return treeService.getTreeById(treeId);
    }

    @GetMapping("/planting-history/{userId}")
    public ArrayList<TreeDto> getUserPlantingHistory(@PathVariable Long userId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken){
        authenticationService.authenticateUser(bearerToken);
        return treeService.getUserPlantingHistory(userId);
    }

}
