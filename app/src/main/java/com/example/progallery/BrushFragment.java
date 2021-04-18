package com.example.progallery;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.progallery.Adapter.ColorAdapter;
import com.example.progallery.Interface.BrushFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrushFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    SeekBar seekbar_brush_size, seekbar_brush_opacity;
    RecyclerView brushColorList;
    ToggleButton btn_brush_state;
    ColorAdapter colorAdapter;

    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    BrushFragmentListener listener;

    static BrushFragment instance;
    public static BrushFragment getInstance() {
        if (instance == null) {
            instance = new BrushFragment();
        }
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrushFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrushFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrushFragment newInstance(String param1, String param2) {
        BrushFragment fragment = new BrushFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_brush, container, false);

        seekbar_brush_size = (SeekBar) itemView.findViewById(R.id.seekbar_brushSize);
        seekbar_brush_opacity = (SeekBar) itemView.findViewById(R.id.seekbar_brushOpacity);
        btn_brush_state = (ToggleButton) itemView.findViewById(R.id.brushState);
        brushColorList = (RecyclerView) itemView.findViewById(R.id.brushColor);

        brushColorList.setHasFixedSize(true);
        brushColorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        colorAdapter = new ColorAdapter(getContext(), getColorList(), this);
        brushColorList.setAdapter(colorAdapter);

        // Event
        seekbar_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_brush_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_brush_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onBrushStateChangedListener(isChecked);
            }
        });

        return itemView;
    }

    private List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#f9bed6"));
        colorList.add(Color.parseColor("#ffebf6"));
        colorList.add(Color.parseColor("#ffd3e6"));
        colorList.add(Color.parseColor("#f5cee5"));
        colorList.add(Color.parseColor("#fed2ea"));
        colorList.add(Color.parseColor("#cff1f8"));
        colorList.add(Color.parseColor("#93c4f6"));
        colorList.add(Color.parseColor("#e91e63"));
        colorList.add(Color.parseColor("#00ebc2"));
        colorList.add(Color.parseColor("#cab8b0"));
        colorList.add(Color.parseColor("#c5a89d"));
        colorList.add(Color.parseColor("#b19d97"));
        colorList.add(Color.parseColor("#660000"));
        colorList.add(Color.parseColor("#9467f9"));
        colorList.add(Color.parseColor("#ffd9ea"));
        colorList.add(Color.parseColor("#ffb6c1"));
        colorList.add(Color.parseColor("#00ebc2"));
        colorList.add(Color.parseColor("#b7f7ff"));

        return colorList;
    }

    public void onColorSelected(int color) {
        listener.onBrushColorChangedListener(color);
    }
}