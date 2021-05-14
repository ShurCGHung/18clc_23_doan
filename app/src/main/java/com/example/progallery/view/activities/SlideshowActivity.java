package com.example.progallery.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.model.models.Media;
import com.example.progallery.model.services.MediaFetchService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlideshowActivity extends AppCompatActivity {

    ImageSlider imageSlider;
    List<SlideModel> slideModelArrayList;
    String startImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        MediaFetchService service = MediaFetchService.getInstance();
        List<Media> mediaArrayList = service.getMediaList(SlideshowActivity.this).toList().blockingGet().get(0);

        Intent intent = getIntent();
        startImage = intent.getStringExtra(Constant.EXTRA_PATH);

        slideModelArrayList = new ArrayList<>();
        int indexToSplit = -1;

        for (Media media : mediaArrayList) {
            if (media.getMediaPath().equals(startImage)) {
                indexToSplit = mediaArrayList.indexOf(media);
            }
            if (media.getMediaType().equals(Integer.toString(1))) {
                slideModelArrayList.add(new SlideModel("file://" + media.getMediaPath()));
            }
        }

        if(indexToSplit != 0){
            List<SlideModel> first = slideModelArrayList.subList(0, indexToSplit);
            List<SlideModel> last = slideModelArrayList.subList(indexToSplit, slideModelArrayList.size());
            slideModelArrayList = Stream.concat(last.stream(), first.stream())
                    .collect(Collectors.toList());
        }

        imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModelArrayList, true);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                onBackPressed();
            }
        });
    }
}