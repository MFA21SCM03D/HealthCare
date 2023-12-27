package com.example.rohanspc.healthcare;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentNearBy extends Fragment{
    View v;

    public FragmentNearBy() {
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_nearby_fragment,container,false);
        return v;
    }
}
