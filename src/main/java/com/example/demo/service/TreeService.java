package com.example.demo.service;

import com.example.demo.model.Tree;
import com.example.demo.model.dto.TreeDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TreeService {

    String uploadTreeImage(TreeDto treeDto, MultipartFile multipartFile) throws IOException;

    byte[] downloadImage(Long treeId) throws Exception;

    TreeDto getTreeById(Long treeId);

    ArrayList<TreeDto> getUserPlantingHistory(Long userId);

}
