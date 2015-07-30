package com.yang.picker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Util {
	/**
	 * 显示进度加载框
	 * 
	 * @param context
	 * @param msg
	 * @param isAnimation
	 * @param imageId
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg,
			boolean isAnimation, int imageId) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

		if (imageId != 0) {
			spaceshipImage.setImageResource(imageId);
		}
		if (isAnimation) {
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		}

		if (TextUtils.isEmpty(msg)) {
			tipTextView.setVisibility(View.GONE);
		} else {
			tipTextView.setVisibility(View.VISIBLE);
			tipTextView.setText(msg);// 设置加载信息
		}

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
}
