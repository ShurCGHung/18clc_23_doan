package com.example.progallery.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.progallery.R;
import com.example.progallery.view.adapters.EmojiAdapter;
import com.example.progallery.view.listeners.EmojiFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmojiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    RecyclerView recycler_emoji;

    static EmojiFragment instance;
    public static EmojiFragment getInstance() {
        if (instance == null) {
            instance = new EmojiFragment();
        }
        return instance;
    }

    EmojiFragmentListener listener;
    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmojiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmojiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmojiFragment newInstance(String param1, String param2) {
        EmojiFragment fragment = new EmojiFragment();
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
        View itemView = inflater.inflate(R.layout.fragment_emoji, container, false);
        recycler_emoji = (RecyclerView) itemView.findViewById(R.id.recycler_emoji);
        recycler_emoji.setHasFixedSize(true);
        recycler_emoji.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()), this);
        recycler_emoji.setAdapter(adapter);

        return itemView;
    }

    @Override
    public void onEmojiSelected(String emoji) {
        listener.onEmojiSelected(emoji);
    }
}