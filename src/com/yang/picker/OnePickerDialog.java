package com.yang.picker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.yang.picker.wheel.OnWheelClickedListener;
import com.yang.picker.wheel.WheelView;
import com.yang.picker.wheel.adapter.AbstractWheelTextAdapter;

public class OnePickerDialog extends Dialog {

	private final static int DEFAULT_ITEMS = 5;
			
	private Activity mContext;
	
	public static interface onSelectListener {
		public void onSelect(AbstractWheelTextAdapter adapter, int position);
	}
	
	public OnePickerDialog(Activity context, final AbstractWheelTextAdapter adapter, final onSelectListener listener) {
		super(context);
		mContext = context;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		getWindow().setWindowAnimations(R.style.AnimBottom);
		View rootView = getLayoutInflater().inflate(
				R.layout.dialog_one_picker, null);
		int screenWidth = mContext.getWindowManager().getDefaultDisplay()
				.getWidth();
		LayoutParams params = new LayoutParams(screenWidth,
				LayoutParams.MATCH_PARENT);
		super.setContentView(rootView, params);
		final WheelView wheelView = (WheelView) rootView
				.findViewById(R.id.wheelView);
		
		wheelView.setViewAdapter(adapter);
		wheelView.setCyclic(false);
		wheelView.setVisibleItems(DEFAULT_ITEMS);
		View cancel = findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		wheelView.addClickingListener(new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				if(itemIndex != wheelView.getCurrentItem()){
					wheelView.setCurrentItem(itemIndex, true, 500);
				} else {
					if (listener != null) {
						listener.onSelect(adapter, itemIndex);
					} 
					dismiss();
				}
				
			}
		});
		
		View done = findViewById(R.id.done);
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = wheelView.getCurrentItem();
				if (listener != null && adapter.getItemsCount() > 0) {
					listener.onSelect(adapter, position);
				} 
				dismiss();
			}
		});
	}
}
