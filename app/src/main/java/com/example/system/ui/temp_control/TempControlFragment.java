package com.example.system.ui.temp_control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.system.databinding.FragmentHomeBinding;
import com.example.system.databinding.FragmentTempControlBinding;

public class TempControlFragment extends Fragment {

    private FragmentTempControlBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentTempControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}