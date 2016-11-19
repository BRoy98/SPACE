/*****************************************************************************************
 *
 *                       Copyright (C) 2016 Bishwajyoti Roy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 ****************************************************************************************/

package com.hometsolutions.space.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hometsolutions.space.Activitys.EditComponentsActivity;
import com.hometsolutions.space.Fragments.SetupFragment;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.R;

/**
 * Created by Bishwajyoti Roy on 13-10-2016.
 */

public class mRoomAdapterEditRooms extends RecyclerView.Adapter<mRoomAdapterEditRooms.mViewHolder> {

    private Context context;
    private String[] data;
    private LayoutInflater inflator;
    private MainActivity mainActivity;
    private SetupFragment setupFragment;
    private DatabaseHelper databaseHelper;
    private final String INTENT_ROOM_NAME = "roomName";
    private final String INTENT_ROOM_ID = "roomID";
    private View layout;

    public mRoomAdapterEditRooms(Context context, String[] data, MainActivity mainActivity, SetupFragment setupFragment) {

        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);
        this.mainActivity = mainActivity;
        this.setupFragment = setupFragment;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        layout = inflator.inflate(R.layout.recycler_layout_rooms, parent, false);
        mViewHolder holder = new mViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {

        holder.roomName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        //return MainActivity.roomCount;
        return databaseHelper.getRoomCount();
    }

    class mViewHolder extends RecyclerView.ViewHolder {

        TextView roomName;
        CardView card;

        public mViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            roomName = (TextView) itemView.findViewById(R.id.Recycler_text_name_rooms);
            card = (CardView) itemView.findViewById(R.id.Recycler_card_rooms);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditComponentsActivity ed = new EditComponentsActivity();
                    final int id = getAdapterPosition();
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                    Intent intent = new Intent(layout.getContext(),ed.getClass());
                    intent.putExtra(INTENT_ROOM_NAME, databaseHelper.roomNameByID(id));
                    intent.putExtra(INTENT_ROOM_ID, id);
                    layout.getContext().startActivity(intent, bndlanimation);
                }
            });

            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int id = getAdapterPosition();
                    new MaterialDialog.Builder(mainActivity)
                            .items(R.array.room_options)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    switch (which) {
                                        case 0:
                                            new MaterialDialog.Builder(mainActivity)
                                                    .title("Rename " + databaseHelper.roomNameByID(id))
                                                    .content("Please enter the new room name")
                                                    .inputType(InputType.TYPE_CLASS_TEXT |
                                                            InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                                                    .inputRange(4,15)
                                                    .positiveText("Rename")
                                                    .negativeText("Cancel")
                                                    .input("Eg.: My Room", databaseHelper.roomNameByID(id), false, new MaterialDialog.InputCallback() {
                                                        @Override
                                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                            try {
                                                                databaseHelper.renameRoom(databaseHelper.roomNameByID(id), input.toString());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            setupFragment.loadAdapter();
                                                        }
                                                    }).show();
                                            break;
                                        case 1:
                                            String roomName = databaseHelper.roomNameByID(id);
                                            new MaterialDialog.Builder(mainActivity)
                                                    .title("Delete " + roomName + "?")
                                                    .content("All the components(lights, fans etc) of this room will be deleted permanently. " +
                                                            "You can not undo this process. Are you sure you want to delete " +
                                                            roomName + "?")
                                                    .positiveText("Delete")
                                                    .negativeText("Cancel")
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            Log.i("Press",which.toString());
                                                            switch (which.toString()) {
                                                                case "POSITIVE":
                                                                    try {
                                                                        databaseHelper.deleteRoom(databaseHelper.roomNameByID(id));
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    setupFragment.loadAdapter();
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
