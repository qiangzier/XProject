package com.hzq.app.view.expandablerecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzq.app.view.R;
import com.hzq.lib.design.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 17/5/9
 * @version:
 * @description:
 */

public class ExpandableRecycleActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TreeAdapter treeAdapter;
    LinearLayoutManager mLinearLayoutManager;
    private List<Object> listData = new ArrayList<>();
    private List<Integer> parentDataPos = new ArrayList<>();
    private LinearLayout pinnedHeader;
    private TextView name ;
    private int mSuspensionHeight;
    private int mCurrentPosition;
    private int mOldDy;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_recycle_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        pinnedHeader = (LinearLayout) findViewById(R.id.parent_layout);
        name = (TextView) findViewById(R.id.parent_name);

        initData();

        setName1(mCurrentPosition);

        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        treeAdapter = new TreeAdapter(this, listData);
        recyclerView.setAdapter(treeAdapter);
        recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        treeAdapter.setItemOnClick(new TreeAdapter.ItemClick() {
            @Override
            public void onItemClick(ParentEntity parent, int position) {
                if (position + 1 == listData.size()) { //最后一条数据
                    treeAdapter.add(position + 1, parent.childEntities);
                } else {
                    if (listData.get(position + 1) instanceof ParentEntity) { //表示是折叠状态
                        treeAdapter.add(position + 1, parent.childEntities);
                    } else {
                        treeAdapter.delete(position + 1, parent.childEntities.size());
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = pinnedHeader.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int d = dy - mOldDy;
                mOldDy = dy;
                if(treeAdapter.getItemViewType(mCurrentPosition + 1)  == 0) {
                    View view = mLinearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                    if (view != null) {
                        if (view.getTop() <= mSuspensionHeight) {
                            pinnedHeader.setY(-(mSuspensionHeight - view.getTop()));
                        } else {
                            pinnedHeader.setY(0);
                        }
                    }

                }
                if (mCurrentPosition != mLinearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                    pinnedHeader.setY(0);
                    setName1(searchIndex(mCurrentPosition));
                }
            }
        });
    }
    private void setName1(int position){
        if(position == -1) return;
        if (listData.get(position) instanceof ParentEntity) {
            name.setText(((ParentEntity) listData.get(position)).name);
        }
    }

    /**
     * 查找位置p及以前的第一个父View
     * @param p
     * @return
     */
    private int searchIndex(int p){
        for (int i = p; i >= 0; i--) {
            if(listData.get(i) instanceof ParentEntity){
                return i;
            }
        }
        return -1;
    }

    private void initData(){
        ParentEntity p = null;
        for (int i = 0; i < 20; i++) {
            p = new ParentEntity();
            p.name = "parent item" + (i + 1);
            p.childEntities = getChildData(i);

            listData.add(p);
            if(i == 0){
                listData.addAll(p.childEntities);
            }
        }
    }

    private List<ParentEntity.ChildEntity> getChildData(int position){
        List<ParentEntity.ChildEntity> data = new ArrayList<>();
        ParentEntity.ChildEntity c = null;
        for (int i = 0; i < 10; i++) {
            c = new ParentEntity.ChildEntity();
            c.cName = (position + 1) + "child item" + (i + 1);
            data.add(c);
        }
        return data;
    }
}
