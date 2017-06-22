package com.vitech.bakersbook;

import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vitech.bakersbook.adapters.InstructionPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class InstructionActivity extends AppCompatActivity {
 @BindView(R.id.instruction_pager)
  ViewPager instructionPager;
 @BindView(R.id.go_next)
 Button goNext;
 @BindView(R.id.go_previous)
  Button goPrevious;

    private InstructionPagerAdapter adapter;
    private int end = 0;
    private int position;
    public static final String ARG_INSTRUCTION_SET = "instruction_set";
    public static final String ARG_CURRENT_INSTRUCTION = "current_instruction";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        ButterKnife.bind(this);
        try{
            JSONArray instructions = new JSONArray(getIntent().getStringExtra(ARG_INSTRUCTION_SET));
            end = instructions.length()-1;
            adapter = new InstructionPagerAdapter(instructions,getSupportFragmentManager());
            instructionPager.setAdapter(adapter);
            position= getIntent().getIntExtra(ARG_CURRENT_INSTRUCTION,0);
            if(savedInstanceState!=null){
                if(savedInstanceState.containsKey(ARG_CURRENT_INSTRUCTION)){
                    position = savedInstanceState.getInt(ARG_CURRENT_INSTRUCTION);
                }
            }
            instructionPager.setCurrentItem(position);

            int display_mode = getResources().getConfiguration().orientation;
            if(display_mode==Configuration.ORIENTATION_PORTRAIT){
                setCorrectState();
            }else {
                hideSystemUI();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }


    }


    @Optional @OnClick(R.id.go_next)
    void goNext(Button goNext){
        instructionPager.setCurrentItem(++position,true);
        setCorrectState();
    }
    @Optional  @OnClick(R.id.go_previous)
    void goPrevious(Button goPrevious){
        instructionPager.setCurrentItem(--position,true);
        setCorrectState();
    }

    private void setCorrectState(){
       if(position==0){
           goPrevious.setVisibility(View.GONE);
       }
       else if(position==end){
           goNext.setVisibility(View.GONE);
       }else{
           goPrevious.setVisibility(View.VISIBLE);
           goNext.setVisibility(View.VISIBLE);
       }
    }

    private void hideSystemUI() {
        System.out.println("Hide UI");
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        goPrevious.setVisibility(View.GONE);
        goNext.setVisibility(View.GONE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENT_INSTRUCTION,position);
    }

}
