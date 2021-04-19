package com.example.progallery.helpers;

import com.example.progallery.model.entities.Media;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Converter {
    public static LinkedHashMap<String, List<Media>> toHashMap(List<Media> mediaList) {
        LinkedHashMap<String, List<Media>> groupedData = new LinkedHashMap<>();
        for (Media media : mediaList) {
            String hashMapKey = media.getMediaDateAdded();
            if (groupedData.containsKey(hashMapKey)) {
                Objects.requireNonNull(groupedData.get(hashMapKey)).add(media);
            } else {
                List<Media> list = new ArrayList<>();
                list.add(media);
                groupedData.put(hashMapKey, list);
            }
        }
        return groupedData;
    }
}
