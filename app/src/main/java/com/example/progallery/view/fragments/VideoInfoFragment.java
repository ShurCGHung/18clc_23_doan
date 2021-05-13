package com.example.progallery.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;

import com.example.progallery.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VideoInfoFragment extends DialogFragment {

    private static final String TAG = "Format Video";
    TextView vidTitle, vidSource, vidDatetime, vidDuration, vidWidth, vidHeight;
    ExifInterface exif;
    private String mediaPath;
    boolean isVault;

    public VideoInfoFragment(String mediaPath, boolean isVault) {
        this.mediaPath = mediaPath;
        this.isVault = isVault;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.video_details, null);

        builder.setView(view)
                .setTitle("Image Information")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        LinearLayout linearLayout = view.findViewById(R.id.pathText);
        if (isVault) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }

        vidTitle = view.findViewById(R.id.vidTitle);
        vidSource = view.findViewById(R.id.vidSource);
        vidDatetime = view.findViewById(R.id.vidDatetime);
        vidDuration = view.findViewById(R.id.vidDuration);
        vidWidth = view.findViewById(R.id.vidWidth);
        vidHeight = view.findViewById(R.id.vidHeight);
        try {
            Uri uri = Uri.fromFile(new File(mediaPath));
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getContext(), uri);

            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

            Long milliseconds = Long.parseLong(duration);
            long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
            milliseconds -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
            String videoDuration = String.format(
                    "%02d:%02d:%02d",
                    hours, minutes, seconds

            );

            String datetime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
//            Date date = new Date(datetime);
//            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//            String formatted = format.format(date);
            String formattedDate = HandleFormatDate(datetime);
            retriever.release();

            vidTitle.setText(mediaPath.substring(mediaPath.lastIndexOf("/") + 1));
            vidDuration.setText(videoDuration);
            vidDatetime.setText(formattedDate);
            vidWidth.setText(width);
            vidHeight.setText(height);
            vidSource.setText(mediaPath);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to display video information", Toast.LENGTH_SHORT).show();
        }

        return builder.create();
    }

    private static String HandleFormatDate(String date) {
        String formattedDate = "";
        try {
            Date inputDate = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault()).parse(date);
            formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(inputDate);
        }
        catch (Exception e){
            Log.w(TAG, "error parsing date: ", e);
            try {
                Date inputDate = new SimpleDateFormat("yyyy MM dd", Locale.getDefault()).parse(date);
                formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(inputDate);
            } catch (Exception ex) {
                Log.e(TAG, "error parsing date: ", ex);
            }
        }
        return formattedDate;
    }
}
