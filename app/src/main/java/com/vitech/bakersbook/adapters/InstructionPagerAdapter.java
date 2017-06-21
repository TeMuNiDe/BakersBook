package com.vitech.bakersbook.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.vitech.bakersbook.InstructionFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import static com.vitech.bakersbook.InstructionFragment.ARG_INSTRUCTION;

/**
 * Created by varma on 19-06-2017.
 */

public class InstructionPagerAdapter extends FragmentStatePagerAdapter {
    private JSONArray instructions;
    private SparseArray<InstructionFragment> fragmentHashMap;
    public InstructionPagerAdapter(JSONArray instructions, FragmentManager fm) {
        super(fm);
        this.instructions = instructions;
        fragmentHashMap = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();
        try {
            args.putString(ARG_INSTRUCTION, instructions.getJSONObject(position).toString());
        }catch (JSONException j){
            j.printStackTrace();
        }
        fragment.setArguments(args);
        fragmentHashMap.put(position,fragment);
        return fragment;

    }

    @Override
    public int getCount() {
        return instructions.length();
    }



}
