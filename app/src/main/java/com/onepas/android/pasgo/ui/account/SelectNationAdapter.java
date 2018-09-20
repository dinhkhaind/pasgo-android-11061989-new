package com.onepas.android.pasgo.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.NationCode;
import com.onepas.android.pasgo.utils.StringMatcher;

import java.util.ArrayList;
import java.util.List;

public class SelectNationAdapter extends ArrayAdapter<NationCode> implements SectionIndexer {

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    List<NationCode> lists = new ArrayList<NationCode>();
    private Context context;
    ViewHolder holder = null;
    int textViewResourceId;

    public SelectNationAdapter(Context context, int textViewResourceId,
                               List<NationCode> lists) {
        super(context, textViewResourceId, lists);
        this.lists = lists;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvCode;
        TextView tvTitle;
        LinearLayout lnTitle;
        RelativeLayout rlLanguage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(textViewResourceId, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvCode = (TextView) convertView
                    .findViewById(R.id.tvCode);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.lnTitle = (LinearLayout) convertView.findViewById(R.id.lnTitle);
            holder.rlLanguage = (RelativeLayout) convertView.findViewById(R.id.rlLanguage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (lists.size() > 0) {
            if(lists.get(position).getId().equals(Constants.KEY_NATION_CODE_ID)) {
                holder.lnTitle.setVisibility(View.VISIBLE);
                holder.rlLanguage.setVisibility(View.GONE);
            }
            else
            {
                holder.lnTitle.setVisibility(View.GONE);
                holder.rlLanguage.setVisibility(View.VISIBLE);
            }
            String name = lists.get(position).getTen();
            String code = lists.get(position).getMa();
            holder.tvTitle.setText(name);
            holder.tvName.setText(name);
            holder.tvCode.setText(code);
        }

        return convertView;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(
                                String.valueOf(getItem(j).getTen()),
                                String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(
                            String.valueOf(getItem(j).getTen()),
                            String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }
}