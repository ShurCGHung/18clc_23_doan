package com.example.progallery.helpers;

import com.example.progallery.model.entities.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Converter {
    public static LinkedHashMap<String, List<Image>> toHashMap(List<Image> imageList) {
        LinkedHashMap<String, List<Image>> groupedData = new LinkedHashMap<>();
        for (Image image : imageList) {
            String hashMapKey = image.getImageDateAdded();
            if (groupedData.containsKey(hashMapKey)) {
                Objects.requireNonNull(groupedData.get(hashMapKey)).add(image);
            } else {
                List<Image> list = new ArrayList<>();
                list.add(image);
                groupedData.put(hashMapKey, list);
            }
        }
        return groupedData;
    }
}
