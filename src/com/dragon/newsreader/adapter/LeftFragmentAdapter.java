package com.dragon.newsreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dragon.newsreader.R;

public class LeftFragmentAdapter extends BaseAdapter {

	private int stringArray[][] = {
			{ R.string.left_item_reading_cn, R.string.left_item_reading_en },
			{ R.string.left_item_local_cn, R.string.left_item_local_en },
			{ R.string.left_item_comment_cn, R.string.left_item_comment_en },
			{ R.string.left_item_photo_cn, R.string.left_item_photo_en },
			{ R.string.left_item_topic_cn, R.string.left_item_topic_en },
			{ R.string.left_item_vote_cn, R.string.left_item_vote_en },
			{ R.string.left_item_focus_cn, R.string.left_item_focus_en }, };

	private int imageArray[] = { R.drawable.left_item_img_reading,
			R.drawable.left_item_img_local, R.drawable.left_item_img_comment,
			R.drawable.left_item_img_photo, R.drawable.left_item_img_topic,
			R.drawable.left_item_img_vote, R.drawable.left_item_img_focus, };

	private Context context;

	public LeftFragmentAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.left_fragment_item, null, false);
			convertView = view;

			AbsListView.LayoutParams lp = (AbsListView.LayoutParams)convertView.getLayoutParams();
			if (lp == null)
				lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						AbsListView.LayoutParams.WRAP_CONTENT);

			lp.height = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources()
							.getDisplayMetrics());
			convertView.setLayoutParams(lp);

			ViewHolder holder = new ViewHolder();
			holder.itemIconView = (TextView) view.findViewById(R.id.item_image);
			holder.itemNameCNView = (TextView) view
					.findViewById(R.id.item_name_cn);
			holder.itemNameENView = (TextView) view
					.findViewById(R.id.item_name_en);

			convertView.setTag(holder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.itemIconView.setBackgroundResource(imageArray[pos]);
		viewHolder.itemNameCNView.setText(stringArray[pos][0]);
		viewHolder.itemNameCNView.setTextColor(Color.BLACK);
		viewHolder.itemNameENView.setText(stringArray[pos][1]);
		viewHolder.itemNameENView.setTextColor(Color.BLACK);

		return convertView;
	}

	class ViewHolder {
		TextView itemIconView;
		TextView itemNameCNView;
		TextView itemNameENView;
	}

}
