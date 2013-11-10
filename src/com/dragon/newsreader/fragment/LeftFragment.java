package com.dragon.newsreader.fragment;

import com.dragon.newsreader.R;
import com.dragon.newsreader.adapter.LeftFragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends ListFragment {

	private static final String TAG = "LeftFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.left_fragment_layout, container,
				false);
		if (view instanceof ViewGroup) {
			Log.d(TAG, "LeftFragment init title");
			View bgView = view.findViewById(R.id.left_fragment_title);
			bgView.setBackgroundColor(0x99cd0000);

			View childView = view.findViewById(R.id.left_fragment_title);
			TextView titleImage = (TextView) childView
					.findViewById(R.id.item_image);
			titleImage.setBackgroundResource(R.drawable.left_title_image);

			TextView titleCN = (TextView) childView
					.findViewById(R.id.item_name_cn);
			titleCN.setText(R.string.left_title_name_cn);
			titleCN.setTextColor(0xaa000000);
			TextView titleEN = (TextView) childView
					.findViewById(R.id.item_name_en);
			titleEN.setText(R.string.left_title_name_en);
			titleEN.setTextColor(0xaa000000);
		} else {
			Log.d(TAG, "LeftFragment not init title" + view);
		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new LeftFragmentAdapter(getActivity()
				.getApplicationContext()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this.getActivity(),
				this.getActivity().getString(R.string.no_func),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
	}

}
