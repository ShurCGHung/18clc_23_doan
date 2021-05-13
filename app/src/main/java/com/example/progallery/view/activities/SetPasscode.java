package com.example.progallery.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.chaos.view.PinView;
import com.example.progallery.R;
import com.example.progallery.helpers.Constant;

import java.io.File;

public class SetPasscode extends AppCompatActivity {

    SharedPreferences preferences;
    Button btnSetPin;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pin_passcode);

        //region Set PIN view
        final PinView setPinView = findViewById(R.id.setPINView);
        setPinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        setPinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.black, getTheme()));
        setPinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.purple_200, getTheme()));
        setPinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.purple_200, getTheme()));
        setPinView.setItemCount(4);
        setPinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        setPinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        setPinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        setPinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        setPinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        setPinView.setAnimationEnable(true);// start animation when adding text
        setPinView.setCursorVisible(false);
        setPinView.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        setPinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        setPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(Constant.TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setPinView.setHideLineWhenFilled(false);
        setPinView.setPasswordHidden(false);
        setPinView.setTransformationMethod(new PasswordTransformationMethod());
        //endregion

        //region Confirm Set PIN view
        final PinView confirmPinView = findViewById(R.id.confirmSetPINView);
        confirmPinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        confirmPinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.black, getTheme()));
        confirmPinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.purple_200, getTheme()));
        confirmPinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.purple_200, getTheme()));
        confirmPinView.setItemCount(4);
        confirmPinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        confirmPinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        confirmPinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        confirmPinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        confirmPinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        confirmPinView.setAnimationEnable(true);// start animation when adding text
        confirmPinView.setCursorVisible(false);
        confirmPinView.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        confirmPinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        confirmPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(Constant.TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPinView.setHideLineWhenFilled(false);
        confirmPinView.setPasswordHidden(false);
        confirmPinView.setTransformationMethod(new PasswordTransformationMethod());
        //endregion

        btnSetPin = (Button) findViewById(R.id.btnSetPin);
        btnSetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CharSequence PinSequence = setPinView.getText();
                    CharSequence ConfirmPinSequence = confirmPinView.getText();

                    Log.d("Pin Sequence", PinSequence.toString());
                    if (PinSequence.toString().equals(ConfirmPinSequence.toString())) {
                        preferences = getSharedPreferences(Constant.PIN, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constant.PIN, PinSequence.toString());
                        editor.commit();
                        File file = new File(getApplicationContext().getExternalFilesDir(".hidden").getAbsolutePath());
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        Toast.makeText(SetPasscode.this, "Hidden Directory created", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SetPasscode.this, "Unmatched PIN", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SetPasscode.this, "Failed to set passcode", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
