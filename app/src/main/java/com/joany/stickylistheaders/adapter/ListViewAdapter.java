package com.joany.stickylistheaders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.joany.stickylistheaders.R;
import com.joany.stickylistheaders.utils.PinYinUtil;

import java.util.List;

/**
 * Created by joany on 2016/8/11.
 */
public class ListViewAdapter extends BaseAdapter implements SectionIndexer{

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    private Context context;
    private List<String> list;

    public ListViewAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return list!=null ? list.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        String item = list.get(position);
        String divider = getDivider(item);

        ViewHolder viewHolder;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.stickyHeader = (TextView) view.findViewById(R.id.stickyHeader);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.nameTv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(position == 0) {
            viewHolder.stickyHeader.setVisibility(View.VISIBLE);
            viewHolder.stickyHeader.setText(
                    divider);
            viewHolder.nameTv.setTag(FIRST_STICKY_VIEW);
        } else {
            String preDivider = getDivider(list.get(position-1));
            if(divider.equals(preDivider)) {
                viewHolder.stickyHeader.setVisibility(View.GONE);
                viewHolder.nameTv.setTag(NONE_STICKY_VIEW);
            } else {
                viewHolder.stickyHeader.setVisibility(View.VISIBLE);
                viewHolder.stickyHeader.setText(divider);
                viewHolder.nameTv.setTag(HAS_STICKY_VIEW);
            }
        }
        viewHolder.nameTv.setText(item);
        return view;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int position) {
        for(int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            //charAt返回指定字符的索引值
            if(getDivider(item).charAt(0) == position) {
                return i;
            }
        }
        return 0;
    }


    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    class ViewHolder{
        TextView stickyHeader;
        TextView nameTv;
    }

    private String getDivider(String item) {
        return PinYinUtil.convertToFirstSpell(item).substring(0,1).toUpperCase();
    }
}
