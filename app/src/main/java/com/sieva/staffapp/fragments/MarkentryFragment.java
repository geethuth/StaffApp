package com.sieva.staffapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sieva.staffapp.R;

public class MarkentryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_markentry, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final TextView toolbarmsg = root.findViewById(R.id.toolbar_msg);
        toolbarmsg.setText("Mark Entry");
        return root;
    }
}