package com.joany.stickylistheaders;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joany.stickylistheaders.adapter.ListViewAdapter;
import com.joany.stickylistheaders.utils.PinYinComparator;
import com.joany.stickylistheaders.utils.PinYinUtil;
import com.joany.stickylistheaders.view.SideBar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private static String[] originalString = new String[]{
            "傅园慧", "张继科", "孙杨", "徐莉佳", "宁泽涛", "何炅", "汪涵", "谢娜",
            "赵丽颖", "杨幂", "佟丽娅", "刘璇", "钟汉良", "李小鹏", "奥利", "甜馨",
            "李晓萍", "贾乃亮", "王嘉尔", "鹿晗", "吴亦凡", "张艺兴", "黄渤",
            "孙俪", "邓超", "张杰", "吴昕", "李维嘉", "杜海涛", "林允儿",
            "宋仲基", "宋慧乔", "苏有朋", "林心如", "霍建华", "赵薇", "刘诗诗",
            "范冰冰", "吴奇隆", "林依晨", "郑爽", "杨洋", "杨颖", "黄晓明",
            "昆凌", "周杰伦", "梁静茹", "范玮琪", "陈建州", "胡夏", "蔡依林",
            "刘亦菲", "井柏然", "风清扬", "郭靖", "李湘", "郭襄", "小龙女",
            "风晴雪", "碧瑶", "花千骨", "白子画", "梅长苏", "乔布斯", "唐嫣",
            "碧昂斯", "玛丽莲梦露", "黄磊"
    };

    private List<String> list = Arrays.asList(originalString);

    private ListView listView;
    private TextView stickyHeader;
    private SideBar sideBar;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button clearBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData(){
        listView = (ListView) findViewById(R.id.listview);
        stickyHeader = (TextView) findViewById(R.id.stickyHeader);
        sideBar = (SideBar) findViewById(R.id.sideBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        clearBtn = (Button) findViewById(R.id.clearBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
    }

    private void initView(){
        Collections.sort(list, new PinYinComparator());
        listView.setAdapter(new ListViewAdapter(this, list));
        listView.setOnScrollListener(onScrollListener);
        sideBar.setListView(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                R.mipmap.ic_drawer,R.string.drawer_open,R.string.drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            //被stickyheader覆盖的首个可见的view
            View currentView = findChildViewUnder(1);
            if(currentView != null) {
                TextView contentTv = (TextView) currentView.findViewById(R.id.nameTv);
                stickyHeader.setText(PinYinUtil.convertToFirstSpell((String) contentTv.getText())
                        .substring(0,1).toUpperCase());
            }

            //stickyheader下面的首个view
            View belowView = findChildViewUnder(stickyHeader.getMeasuredHeight() + 1);
            if(belowView != null) {
                TextView contentView = (TextView) belowView.findViewById(R.id.nameTv);
                if(contentView.getTag() != null) {
                    int stickyStatus = (int) contentView.getTag();

                    if (stickyStatus == ListViewAdapter.HAS_STICKY_VIEW) {
                        if (belowView.getTop() > 0) {
                            int dealtY = belowView.getTop() - stickyHeader.getMeasuredHeight();
                            stickyHeader.setTranslationY(dealtY);
                        } else {//sticky头部已完全移出
                            stickyHeader.setTranslationY(0);
                        }
                    } else if (stickyStatus == ListViewAdapter.NONE_STICKY_VIEW) {
                        stickyHeader.setTranslationY(0);
                    }
                }
            }
        }
    };

    private View findChildViewUnder(float y) {
        int count = listView.getChildCount();
        for(int i = 0; i < count; i++) {
            //判断y坐标是否落在该child区域内
            View child = listView.getChildAt(i);
            if( y >= child.getTop() && y <= child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //The action bar home/up action should open or close the drawer
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
