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
public class ChangePasscode extends AppCompatActivity {

    SharedPreferences preferences;
    Button btnChangePin;
    private String savedPIN;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin_passcode);

        //region Enter old PIN
        final PinView enterOldPinView = findViewById(R.id.enterOldPINView);
        enterOldPinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        enterOldPinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.black, getTheme()));
        enterOldPinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.purple_200, getTheme()));
        enterOldPinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.purple_200, getTheme()));
        enterOldPinView.setItemCount(4);
        enterOldPinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        enterOldPinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        enterOldPinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        enterOldPinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        enterOldPinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        enterOldPinView.setAnimationEnable(true);// start animation when adding text
        enterOldPinView.setCursorVisible(false);
        enterOldPinView.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        enterOldPinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        enterOldPinView.addTextChangedListener(new TextWatcher() {
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
        enterOldPinView.setHideLineWhenFilled(false);
        enterOldPinView.setPasswordHidden(false);
        enterOldPinView.setTransformationMethod(new PasswordTransformationMethod());
        //endregion

        //region New PIN
        final PinView enterNewPinView = findViewById(R.id.changePINView);
        enterNewPinView.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        enterNewPinView.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.black, getTheme()));
        enterNewPinView.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.purple_200, getTheme()));
        enterNewPinView.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.purple_200, getTheme()));
        enterNewPinView.setItemCount(4);
        enterNewPinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        enterNewPinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        enterNewPinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        enterNewPinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        enterNewPinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        enterNewPinView.setAnimationEnable(true);// start animation when adding text
        enterNewPinView.setCursorVisible(false);
        enterNewPinView.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
        enterNewPinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        enterNewPinView.addTextChangedListener(new TextWatcher() {
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
        enterNewPinView.setHideLineWhenFilled(false);
        enterNewPinView.setPasswordHidden(false);
        enterNewPinView.setTransformationMethod(new PasswordTransformationMethod());
        //endregion

        //region Confirm new PIN
        final PinView confirmPinView = findViewById(R.id.confirmChangePINView);
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

        preferences = getSharedPreferences(Constant.PIN, Context.MODE_PRIVATE);
        if (preferences.contains(Constant.PIN)) {
            savedPIN = preferences.getString(Constant.PIN, "");
            Log.d("Saved Passcode", savedPIN);
        }

        btnChangePin = (Button) findViewById(R.id.btnChangePin);
        btnChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence oldPinInput = enterOldPinView.getText();
                CharSequence newPinInput = enterNewPinView.getText();
                CharSequence confirmPIN = confirmPinView.getText();

                if (!savedPIN.isEmpty()) {
                    if (oldPinInput.toString().equals(savedPIN)) {
                        try {
                            if (newPinInput.toString().equals(confirmPIN.toString())) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Constant.PIN, newPinInput.toString());
                                editor.commit();
                                Toast.makeText(ChangePasscode.this, getResources().getString(R.string.passcode_changed), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasscode.this, getResources().getString(R.string.unmatched_pin), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePasscode.this, getResources().getString(R.string.failed_change_password), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasscode.this, getResources().getString(R.string.old_passcode_unmatched), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
