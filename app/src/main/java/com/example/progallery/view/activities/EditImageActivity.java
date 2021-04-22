package com.example.progallery.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.progallery.R;
import com.example.progallery.helpers.BitmapUtils;
import com.example.progallery.view.listeners.AddTextFragmentListener;
import com.example.progallery.view.listeners.BrushFragmentListener;
import com.example.progallery.view.listeners.EditImageFragmentListener;
import com.example.progallery.view.listeners.EmojiFragmentListener;
import com.example.progallery.view.listeners.FilterFragmentListener;
import com.example.progallery.view.fragments.AddTextToImageFragment;
import com.example.progallery.view.fragments.BrushFragment;
import com.example.progallery.view.fragments.EditImageFragment;
import com.example.progallery.view.fragments.EmojiFragment;
import com.example.progallery.view.fragments.FilterImageFragment;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImageActivity extends AppCompatActivity implements FilterFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener {
    public static final String pictureName = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    private String filepath;

    // Load image
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    List<Bitmap> bitmapUndoList;
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    Bitmap originalBitmap, filteredBitmap, finalBitmap;
    FilterImageFragment filterImageFragment;
    EditImageFragment editImageFragment;
    private MenuItem undo, redo, save;
    CardView btnFilters, btnEditImage, btnBrush, btnEmoji, btnText, btnCrop;
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;
    Uri image_selected;
    int currentShowingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter Image");

        // View
        photoEditorView = (PhotoEditorView) findViewById(R.id.imageView2);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(), "emojione-android.ttf"))
                .build();

        btnFilters = (CardView) findViewById(R.id.btnFilters);
        btnEditImage = (CardView) findViewById(R.id.btnEditImage);
        btnBrush = (CardView) findViewById(R.id.btnBrushImage);
        btnEmoji = (CardView) findViewById(R.id.btnEmoji);
        btnText = (CardView) findViewById(R.id.btnAddTextToImage);
        btnCrop = (CardView) findViewById(R.id.btnCropImage);
        
        bitmapUndoList = new ArrayList<>();

        btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterImageFragment != null) {
                    filterImageFragment.show(getSupportFragmentManager(), filterImageFragment.getTag());
                } else {
                    FilterImageFragment filterImageFragment = FilterImageFragment.getInstance(null);
                    filterImageFragment.setListener(EditImageActivity.this);
                    filterImageFragment.show(getSupportFragmentManager(), filterImageFragment.getTag());
                }
            }
        });

        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editFragment = EditImageFragment.getInstance();
                editFragment.setEditListener(EditImageActivity.this);
                editFragment.show(getSupportFragmentManager(), editFragment.getTag());
            }
        });

        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(EditImageActivity.this);
                brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
            }
        });

        btnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(EditImageActivity.this);
                emojiFragment.show(getSupportFragmentManager(), emojiFragment.getTag());
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTextToImageFragment addTextFragment = AddTextToImageFragment.getInstance();
                addTextFragment.setListener(EditImageActivity.this);
                addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());
            }
        });

        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop(image_selected);
            }
        });

        filepath = getIntent().getStringExtra("IMAGE_PATH");

        loadImage(filepath);
    }

    private void startCrop(Uri image_selected) {
        UCrop uCrop = UCrop.of(image_selected, Uri.fromFile(new File(filepath)));
        uCrop.start(EditImageActivity.this);
    }

    private void loadImage(String filepath) {
        File imgFile = new File(filepath);
        if(imgFile.exists()){
             originalBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //Drawable d = new BitmapDrawable(getResources(), myBitmap);
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            photoEditorView.getSource().setImageBitmap(originalBitmap);
        }
    }

    @Override
    public void onBrightnessChanged(int brightnessValue) {
        brightnessFinal = brightnessValue;
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightnessValue));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
        addToUndoList();
    }

    @Override
    public void onContrastChanged(float contrastValue) {
        contrastFinal = contrastValue;
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(contrastValue));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
        addToUndoList();
    }

    @Override
    public void onSaturationChanged(float saturationValue) {
        saturationFinal = saturationValue;
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(saturationValue));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
        addToUndoList();
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        filter.addSubFilter(new SaturationSubfilter(saturationFinal));
        filter.addSubFilter(new ContrastSubFilter(contrastFinal));

        finalBitmap = filter.processFilter(bitmap);
        addToUndoList();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
        addToUndoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);

        undo = menu.getItem(0);
        redo = menu.getItem(1);
        save = menu.getItem(2);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveImageToGallery();
            return true;
        } else if (id == R.id.action_undo) {
            // Undo
            originalBitmap = getUndoBitmap();
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            photoEditorView.getSource().setImageBitmap(originalBitmap);
            setButtonVisibility();
        } else if (id == R.id.action_redo) {
            // Redo
            originalBitmap = getRedoBitmap();
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            photoEditorView.getSource().setImageBitmap(originalBitmap);
            setButtonVisibility();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImageToGallery() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                // clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();
                addToUndoList();

                filterImageFragment = FilterImageFragment.getInstance(originalBitmap);
                filterImageFragment.setListener(this);
            }
        } else if (requestCode == PERMISSION_INSERT_IMAGE) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 250, 250);
            photoEditor.addImage(bitmap);
        } else if (requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(data);
        } else if (requestCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            Toast.makeText(this, "" + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri result = UCrop.getOutput(data);
        if (result != null) {
            photoEditorView.getSource().setImageURI(result);
            Bitmap bitmap = ((BitmapDrawable) photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredBitmap = originalBitmap;
            finalBitmap = originalBitmap;
            addToUndoList();
        } else {
            Toast.makeText(this, "Cannot retrieve crop image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser) {
            photoEditor.brushEraser();
        } else {
            photoEditor.setBrushDrawingMode(true);
        }
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface, text, color);
    }

    private void addToUndoList() {
        try {
            recycleBitmapList(++currentShowingIndex);
            bitmapUndoList.add(originalBitmap.copy(originalBitmap.getConfig(), true));
        } catch (OutOfMemoryError error) {
            bitmapUndoList.get(1).recycle();
            bitmapUndoList.remove(1);
            bitmapUndoList.add(originalBitmap.copy(originalBitmap.getConfig(), true));
        }
    }

    private void recycleBitmapList(int fromIndex) {
        while (fromIndex < currentShowingIndex) {
            bitmapUndoList.get(fromIndex).recycle();
            bitmapUndoList.remove(fromIndex);
        }
    }

    private Bitmap getUndoBitmap() {
        if (currentShowingIndex - 1 > 0) {
            currentShowingIndex = -1;
        } else currentShowingIndex = 0;

        return bitmapUndoList
                .get(currentShowingIndex)
                .copy(bitmapUndoList.get(currentShowingIndex).getConfig(), true);
    }

    private Bitmap getRedoBitmap() {
        if (currentShowingIndex + 1 > bitmapUndoList.size()) {
            currentShowingIndex += 1;
        } else currentShowingIndex = bitmapUndoList.size() - 1;

        return bitmapUndoList
                .get(currentShowingIndex)
                .copy(bitmapUndoList.get(currentShowingIndex).getConfig(), true);
    }

    private void setButtonVisibility() {
        if (currentShowingIndex > 0) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }

        if (currentShowingIndex + 1 < bitmapUndoList.size()) {
            redo.setEnabled(true);
        } else {
            redo.setEnabled(false);
        }
    }
}
