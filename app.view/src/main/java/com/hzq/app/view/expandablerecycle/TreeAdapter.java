package com.hzq.app.view.expandablerecycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzq.app.view.R;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 17/5/9
 * @version:
 * @description:
 */

public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.MyViewHolder>{

    public Context context;
    public List<Object> mData ;

    public TreeAdapter(Context context,List<Object> objects){
        this.context = context;
        this.mData = objects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.expandable_recycle_parent_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Object o = mData.get(position);
        if(o instanceof ParentEntity){
            final ParentEntity parentEntity = (ParentEntity) o;
            holder.parent_layout.setVisibility(View.VISIBLE);
            holder.child_layout.setVisibility(View.GONE);
            holder.parent_name.setText(parentEntity.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClick != null)

                        itemClick.onItemClick(parentEntity,holder.getLayoutPosition());
                }
            });
        }else if(o instanceof ParentEntity.ChildEntity){
            final ParentEntity.ChildEntity childEntity = (ParentEntity.ChildEntity) o;
            holder.parent_layout.setVisibility(View.GONE);
            holder.child_layout.setVisibility(View.VISIBLE);
            holder.child_name.setText(childEntity.cName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, childEntity.cName, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position) instanceof ParentEntity){
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parent_layout;
        LinearLayout child_layout;
        TextView parent_title;
        TextView parent_name;
        TextView child_title;
        TextView child_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            parent_layout = (LinearLayout) itemView.findViewById(R.id.parent);
            child_layout = (LinearLayout) itemView.findViewById(R.id.child);

            parent_title = (TextView) itemView.findViewById(R.id.parent_title);
            parent_name = (TextView) itemView.findViewById(R.id.parent_name);

            child_title = (TextView) itemView.findViewById(R.id.child_title);
            child_name = (TextView) itemView.findViewById(R.id.child_name);
        }
    }

    private ItemClick itemClick;
    public void setItemOnClick(ItemClick i){
        this.itemClick = i;
    }
    public interface ItemClick{
        void onItemClick(ParentEntity parent,int position);
    }

    public void add(int position,List<?> list){
        mData.addAll(position,list);
        notifyItemRangeInserted(position,list.size());
    }

    public void delete(int position,int itemnum){
        for (int i = 0; i < itemnum; i++) {
            mData.remove(position);
        }
        notifyItemRangeRemoved(position,itemnum);
    }
}
