package com.example.progallery;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.progallery.Adapter.ColorAdapter;
import com.example.progallery.Adapter.FontAdapter;
import com.example.progallery.Interface.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTextToImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTextToImageFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterListener {

    int colorSelected = Color.parseColor("#000000"); // Black by default

    AddTextFragmentListener listener;

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    static AddTextToImageFragment instance;
    public static AddTextToImageFragment getInstance() {
        if (instance == null) {
            instance = new AddTextToImageFragment();
        }
        return instance;
    }

    EditText edit_add_text;
    RecyclerView recyclerColor, textFontLayout;
    Button btn_done;

    Typeface typefaceSelected = Typeface.DEFAULT;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTextToImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTextToImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTextToImageFragment newInstance(String param1, String param2) {
        AddTextToImageFragment fragment = new AddTextToImageFragment();
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
        View itemView = inflater.inflate(R.layout.fragment_add_text_to_image, container, false);

        edit_add_text = (EditText) itemView.findViewById(R.id.edit_add_text);
        btn_done = (Button) itemView.findViewById(R.id.btnAddTextCompleted);

        recyclerColor = (RecyclerView) itemView.findViewById(R.id.brushColor);
        recyclerColor.setHasFixedSize(true);
        recyclerColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        textFontLayout = (RecyclerView) itemView.findViewById(R.id.textFont);
        textFontLayout.setHasFixedSize(true);
        textFontLayout.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ColorAdapter colorAdapter = new ColorAdapter(getContext(), this);
        recyclerColor.setAdapter(colorAdapter);

        FontAdapter fontAdapter = new FontAdapter(getContext(), this);
        textFontLayout.setAdapter(fontAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), colorSelected);
            }
        });

        return itemView;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected = color;
    }

    @Override
    public void onFontSelected(String fontName) {
        typefaceSelected = Typeface.createFromAsset(getContext().getAssets(), new StringBuilder("fonts/")
                .append(fontName).toString());
    }
}