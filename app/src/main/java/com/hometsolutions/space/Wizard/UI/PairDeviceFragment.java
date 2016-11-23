
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

package com.hometsolutions.space.Wizard.UI;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hometsolutions.space.Activitys.NewConnectionActivity;
import com.hometsolutions.space.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * A simple {@link Fragment} subclass.
 */
public class PairDeviceFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "key1";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private static PairDevicePage mPage;
    Button open_settings;
    static Button connect_btn;
    public static TextView connect_text;
    TextView settings_text;
    TextView title;
    static IconTextView connect_icon;

    public static void updateConnectState(boolean state) {
        if(state) {
            connect_text.setText("Connected");
            connect_icon.setText("{md-done}");
            mPage.isConnected = true;
            connect_btn.setText("Disconnect");
            mPage.notifyDataChanged();

        } else if ((!state)) {
            connect_text.setText("Disconnected");
            connect_icon.setText("{md-clear}");
            mPage.isConnected = false;
            connect_btn.setText("Connect");
            mPage.notifyDataChanged();
        }
    }

    public PairDeviceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        PairDeviceFragment fragment = new PairDeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (PairDevicePage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.wizard_fragment_pair_device, container, false);
        open_settings = (Button) rootView.findViewById(R.id.btn_open_bt_settings);
        connect_btn = (Button) rootView.findViewById(R.id.btn_wizard_connect);
        connect_text = (TextView) rootView.findViewById(R.id.textView_crd2);
        settings_text = (TextView) rootView.findViewById(R.id.textView_crd1);
        title = ((TextView) rootView.findViewById(R.id.pair_title));
        connect_icon = (IconTextView) rootView.findViewById(R.id.connect_icon);

        title.setText(mPage.getTitle());
        title.setTextColor(getResources().getColor(com.tech.freak.wizardpager.R.color.step_pager_selected_tab_color));
        setTextViewHTML(settings_text,
                "<p>In order to connect to the device you need to pair with the device via bluetooth in the settings." +
                        "<br />If you have already paired you can skip this step.<br /><br /> " +
                        "Visit <a href=\"http://support.hometsolutions.com\">support.hometsolutions.com</a> for more info.</p>");
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_bt_settings:
                Intent btSettings = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(btSettings, 0);
                break;
            case R.id.btn_wizard_connect:
                if(mPage.isConnected) {
                    ((NewConnectionActivity) getActivity ()).bluetoothSerial.stop();
                    updateConnectState(false);
                }
                else if(!mPage.isConnected)
                    ((NewConnectionActivity) getActivity ()).showDeviceListDialog(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }
        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        open_settings.setOnClickListener(this);
        connect_btn.setOnClickListener(this);
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.e("SPACE", "on click");
                Uri webpage = Uri.parse("http://support.hometsolutions.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        //strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setLinksClickable(true);
        text.setAutoLinkMask(Linkify.ALL);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Drawable setTint(Drawable drawable, int color) {
        final Drawable newDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newDrawable, color);
        return newDrawable;
    }

}
