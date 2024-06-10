package com.example.demo.service.impl;

import com.example.demo.service.RewardsService;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Tree;
import com.example.demo.model.dto.TreeDto;
import com.example.demo.repository.TreeRepository;
import com.example.demo.service.TreeService;
import com.example.demo.utils.ImageUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Service
public class TreeServiceImpl implements TreeService {

    private Map<Long, ArrayList<TreeDto>> usersPlantingHistory;
    private Map<Long, byte[]> downloadedImages;

    @PostConstruct
    public void init(){
        long timeToLiveMinutes = 5;
        usersPlantingHistory = new PassiveExpiringMap<>(timeToLiveMinutes * 60 * 1000);
        downloadedImages = new PassiveExpiringMap<>(timeToLiveMinutes * 60 * 1000);
    }

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private RewardsService rewardsService;

    public String uploadTreeImage(TreeDto tree, MultipartFile imageFile) throws IOException {

        // TreeDto --> (Request Body), MultipartFile --> file we send (Tree Image).
        // We need to make a Tree Entity Object to save it inside MySQL

        Tree treeToSave = new Tree();
        treeToSave.setUserId(tree.getUserId());
        treeToSave.setTreeLocation(tree.getTreeLocation());
        treeToSave.setPlantingTime(tree.getPlantingTime());
        treeToSave.setSpecies(tree.getSpecies());
        treeToSave.setTreePhoto(ImageUtils.compressImage(imageFile.getBytes()));

        Tree savedTree = treeRepository.save(treeToSave);

        rewardsService.calculateTreePoints(savedTree);

        return "file uploaded successfully : " + imageFile.getOriginalFilename();

    }

    public byte[] downloadImage(Long treeId) throws Exception {

        // Do we have this data inside Cache ? yes -> return it, no -> continue the code flow.

        if (downloadedImages.containsKey(treeId)) {
            System.out.println("Loading tree image from Cache..");
            return downloadedImages.get(treeId);
        }

        System.out.println("Loading tree image from MySQL Database..");

        Tree tree = treeRepository.findById(treeId).orElseThrow(() -> new NotFoundException("Tree", "Id", treeId));

        try {
            byte[] image = ImageUtils.decompressImage(tree.getTreePhoto());
            downloadedImages.put(treeId, image);
            return image;
        } catch (DataFormatException | IOException exception) {
            throw new Exception("Error downloading a tree image with ID " + tree.getTreeId());
        }

    }

    public TreeDto getTreeById(Long treeId) {

        Tree tree = treeRepository.findById(treeId).orElseThrow(() -> new NotFoundException("Tree", "Id", treeId));

        TreeDto treeDto = new TreeDto();
        treeDto.setTreeId(tree.getTreeId());
        treeDto.setUserId(tree.getUserId());
        treeDto.setTreeLocation(tree.getTreeLocation());
        treeDto.setPlantingTime(tree.getPlantingTime());
        treeDto.setSpecies(tree.getSpecies());
        treeDto.setTreePlantingDate(tree.getTreePlantingDate());

        return treeDto;
    }

    @Override
    public ArrayList<TreeDto> getUserPlantingHistory(Long userId) {

        // Do we have this data inside Cache ? yes -> return it, no -> continue the code flow.

        if (usersPlantingHistory.containsKey(userId)) {
            System.out.println("Loading user planting history from Cache..");
            return usersPlantingHistory.get(userId);
        }

        System.out.println("Loading user planting history from MySQL Database..");

        List<Tree> databaseTrees = treeRepository.getUserPlantingHistory(userId); // List of Tree Entity (With Image)

        ArrayList<TreeDto> plantedTrees = new ArrayList<>(); // List of Dto (Without Image)

        for (int i = 0; i < databaseTrees.size(); i++) { // For each Tree Entity -> create a new TreeDto without the image using Builder
            Tree curretTree = databaseTrees.get(i);

            TreeDto treeDto = new TreeDto();
            treeDto.setTreeId(curretTree.getTreeId());
            treeDto.setUserId(curretTree.getUserId());
            treeDto.setTreeLocation(curretTree.getTreeLocation());
            treeDto.setPlantingTime(curretTree.getPlantingTime());
            treeDto.setSpecies(curretTree.getSpecies());
            treeDto.setTreePlantingDate(curretTree.getTreePlantingDate());

            plantedTrees.add(treeDto);
        }

        usersPlantingHistory.put(userId, plantedTrees);

        return plantedTrees;
    }

}