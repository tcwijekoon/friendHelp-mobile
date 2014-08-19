package com.tw.friendhelp.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tw.friendhelp.R;

public class ConfirmDialog extends Dialog implements OnClickListener {
	public Button cdpoitiveButton;
	public Button cdnegativeButton;
	Context mContext;
	TextView mTitle = null;
	TextView mMessage = null;
	View v = null;

	public ConfirmDialog(Context context) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.decision_dialog);
		v = getWindow().getDecorView();
		v.setBackgroundResource(android.R.color.transparent);
		mTitle = (TextView) findViewById(R.id.cdtitle);
		mMessage = (TextView) findViewById(R.id.cdprompt);
		cdpoitiveButton = (Button) findViewById(R.id.btnOk);
		cdnegativeButton = (Button) findViewById(R.id.btnCancel);

		this.setCancelable(false);
	}

	public ConfirmDialog(Context context, String type) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.decision_dialog_single);
		v = getWindow().getDecorView();
		v.setBackgroundResource(android.R.color.transparent);
		mTitle = (TextView) findViewById(R.id.cdtitle);
		mMessage = (TextView) findViewById(R.id.cdprompt);
		cdpoitiveButton = (Button) findViewById(R.id.cdbuttonpositive);

		this.setCancelable(false);

	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mTitle.setText(title);
	}

	public void setContents(CharSequence title, CharSequence message) {
		super.setTitle(title);
		mTitle.setText(title);

		mMessage.setText(message.toString());
		// mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	public void setButtonsContents(CharSequence positive, CharSequence negative) {
		cdpoitiveButton.setText(positive.toString());
		cdnegativeButton.setText(negative.toString());
	}

	@Override
	public void setTitle(int titleId) {
		super.setTitle(titleId);
		mTitle.setText(mContext.getResources().getString(titleId));

	}

	public void setPositiveButtonText(String text) {
		cdpoitiveButton.setText(text);
	}

	public void setMessage(CharSequence message) {
		mMessage.setText(capitalize(message.toString().toLowerCase()));
		// mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	public void setMessageOriginal(CharSequence message) {
		mMessage.setText(message.toString());
		// mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	private String capitalize(String line) {
		String aa[] = line.split(",");
		String returnVal = "";
		for (int i = 0; i < aa.length; i++) {
			if (line.length() > 0) {
				returnVal = Character.toUpperCase(line.charAt(0)) + line.substring(1);
			} else {
			}
		}
		return returnVal;
	}

	public void setMessage(int messageId) {
		mMessage.setText(mContext.getResources().getString(messageId));
		// mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// dismiss();
	}

}