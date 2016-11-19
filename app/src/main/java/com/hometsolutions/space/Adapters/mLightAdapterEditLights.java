package com.hometsolutions.space.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hometsolutions.space.Activitys.EditComponentsActivity;
import com.hometsolutions.space.Fragments.addLightFragment;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * Created by Bishwajyoti Roy on 11/6/2016.
 */

public class mLightAdapterEditLights extends RecyclerView.Adapter<mLightAdapterEditLights.mViewHolder>  {

    private Context context;
    private String[] data;
    private LayoutInflater inflator;
    private EditComponentsActivity editComponentsActivity;
    private addLightFragment addLightFragment;
    private DatabaseHelper databaseHelper;
    private final String INTENT_ROOM_NAME = "roomName";
    private final String INTENT_ROOM_ID = "roomID";
    private TextView DetailsName;
    private TextView DetailsRoom;
    private TextView DetailsDate;
    private TextView DetailsID;

    public mLightAdapterEditLights (Context context, String[] data, EditComponentsActivity editComponentsActivity, addLightFragment addLightFragment) {
        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);
        this.editComponentsActivity = editComponentsActivity;
        this.addLightFragment = addLightFragment;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflator.inflate(R.layout.recycler_layout_component_edit, parent, false);
        mViewHolder holder = new mViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.NameTextView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return databaseHelper.LIGHT_getCount(editComponentsActivity.roomID);
    }

    class mViewHolder extends RecyclerView.ViewHolder {

        TextView NameTextView;
        CardView cardCardView;

        public mViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            NameTextView = (TextView) itemView.findViewById(R.id.Recycler_text_componrnt_edit);
            cardCardView = (CardView) itemView.findViewById(R.id.Recycler_card_componrnt_edit);
            cardCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = getAdapterPosition();
                    MaterialDialog dialog = new MaterialDialog.Builder(editComponentsActivity)
                            .title(addLightFragment.lightNames[id] + " Details")
                            .customView(R.layout.layout_about_fan_dialog, true)
                            .show();
                    View view = dialog.getCustomView();
                    DetailsName = (TextView) view.findViewById(R.id.fanDetailsName);
                    DetailsRoom = (TextView) view.findViewById(R.id.fanDetailsRoom);
                    DetailsDate = (TextView) view.findViewById(R.id.fanDetailsDate);
                    DetailsID = (TextView) view.findViewById(R.id.fanDetailsID);
                    DetailsName.setText(addLightFragment.lightNames[id]);
                    DetailsRoom.setText(editComponentsActivity.roomName);
                    DetailsDate.setText(databaseHelper.LIGHT_getTime(addLightFragment.lightNames[id]));
                    DetailsID.setText(databaseHelper.LIGHT_getID(addLightFragment.lightNames[id]));
                }
            });

            cardCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int id = getAdapterPosition();
                    new MaterialDialog.Builder(editComponentsActivity)
                            .items(R.array.room_options)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    switch (which) {
                                        case 0:
                                            String lightName_ = addLightFragment.lightNames[id];
                                            new MaterialDialog.Builder(editComponentsActivity)
                                                    .title("Rename " + lightName_)
                                                    .content("Please enter the new fan name")
                                                    .inputType(InputType.TYPE_CLASS_TEXT |
                                                            InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                                                    .inputRange(4,15)
                                                    .positiveText("Rename")
                                                    .negativeText("Cancel")
                                                    .input("Eg.: My Room", lightName_, false, new MaterialDialog.InputCallback() {
                                                        @Override
                                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                            try {
                                                                databaseHelper.LIGHT_rename(addLightFragment.lightNames[id], input.toString());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            addLightFragment.loadAdapter(editComponentsActivity.roomID);
                                                        }
                                                    }).show();
                                            break;
                                        case 1:
                                            String lightName = addLightFragment.lightNames[id];
                                            new MaterialDialog.Builder(editComponentsActivity)
                                                    .title("Delete " + lightName + "?")
                                                    .content("All the data of this fan will be deleted permanently. " +
                                                            "You can not undo this process. Are you sure you want to delete " +
                                                            lightName + "?")
                                                    .positiveText("Delete")
                                                    .negativeText("Cancel")
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            switch (which.toString()) {
                                                                case "POSITIVE":
                                                                    try {
                                                                        databaseHelper.LIGHT_delete(addLightFragment.lightNames[id]);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    addLightFragment.loadAdapter(editComponentsActivity.roomID);
                                                                    break;
                                                                case "NEGATIVE":
                                                                    break;
                                                            }
                                                        }
                                                    })
                                                    .show();
                                            break;
                                    }
                                }
                            })
                            .show();
                    return true;
                }
            });
        }
    }
}
