package com.vitech.bakersbook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstructionListAdapter extends RecyclerView.Adapter<InstructionListAdapter.InstructionHolder> {
private Context context;
private JSONArray instructions;
private OnInstructionClickedListener onInstructionClickedListener;

    public InstructionListAdapter(Context context, JSONArray instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    public void setOnInstructionClickedListener(OnInstructionClickedListener onInstructionClickedListener) {
        this.onInstructionClickedListener = onInstructionClickedListener;
    }

    @Override
    public InstructionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView instructionView = new TextView(parent.getContext());
        instructionView.setGravity(Gravity.CENTER);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        instructionView.setLayoutParams(layoutParams);
        instructionView.setTextColor(Color.BLACK);
        return new InstructionHolder(instructionView);
    }

    @Override
    public void onBindViewHolder(InstructionHolder holder, int position) {
        try {
            holder.bindInstruction(instructions.getJSONObject(position),position);
        }catch (JSONException j){
            j.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return instructions.length();
    }

    class InstructionHolder extends RecyclerView.ViewHolder{
        TextView instructionView;
        int position;
      InstructionHolder(final View itemView) {
            super(itemView);
            instructionView = (TextView)itemView;
          instructionView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(onInstructionClickedListener!=null){
                      onInstructionClickedListener.OnInstructionClicked(position);
                  }
              }
          });
        }
    void bindInstruction(JSONObject instruction,int position)throws JSONException {
        this.position = position;
        instructionView.setText(instruction.getString("shortDescription"));

    }
    }

   public interface OnInstructionClickedListener{
        void OnInstructionClicked(int position);
    }
}
