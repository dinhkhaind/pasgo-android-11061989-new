package com.onepas.android.pasgo.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.onepas.android.pasgo.R;

public class LinkEnabledTextView extends TextView {
	private ArrayList<Hyperlink> listOfLinks;
	TextLinkClickListener mListener;
	Pattern screenNamePattern = Pattern.compile("(@[a-zA-Z0-9_]+)");

	Pattern hashTagsPattern = Pattern.compile("(#[a-zA-Z0-9_-]+)");

	Pattern hashGuiLai = Pattern.compile(getResources().getString(
			R.string.link_gui_lai2));

	Pattern website = Pattern.compile(getResources().getString(
			R.string.pastaxi_vn));
	Pattern dieuKhoanSD = Pattern.compile(getResources().getString(
			R.string.dieu_khoan_su_dung));
	Pattern chinhSachBM = Pattern.compile(getResources().getString(
			R.string.chinh_sach_bao_mat));
	Pattern chinhSachBM1 = Pattern.compile(getResources().getString(
			R.string.tai_day));

	Pattern hashTagsPattern1 = Pattern.compile("(#[a-zA-Z0-9_-]+)");

	Pattern hyperLinksPattern = Pattern
			.compile("([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\'>\\]\\)]*[^\\. ,'\'>\\]\\)])");
	private SpannableString linkableText;

	public LinkEnabledTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		listOfLinks = new ArrayList<Hyperlink>();
	}

	public void gatherLinksForText(String text) {
		linkableText = new SpannableString(text);
		gatherLinks(listOfLinks, linkableText, screenNamePattern);
		gatherLinks(listOfLinks, linkableText, hashTagsPattern);
		gatherLinks(listOfLinks, linkableText, hyperLinksPattern);

		gatherLinks(listOfLinks, linkableText, hashGuiLai);
		gatherLinks(listOfLinks, linkableText, website);
		gatherLinks(listOfLinks, linkableText, dieuKhoanSD);
		gatherLinks(listOfLinks, linkableText, chinhSachBM);
		gatherLinks(listOfLinks, linkableText, chinhSachBM1);

		for (int i = 0; i < listOfLinks.size(); i++) {
			Hyperlink linkSpec = listOfLinks.get(i);
			android.util.Log.v("listOfLinks :: " + linkSpec.textSpan,
					"listOfLinks :: " + linkSpec.textSpan);
			linkableText.setSpan(linkSpec.span, linkSpec.start, linkSpec.end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		setText(linkableText);
	}

	public void setOnTextLinkClickListener(TextLinkClickListener newListener) {
		mListener = newListener;
	}

	private final void gatherLinks(ArrayList<Hyperlink> links, Spannable s,
			Pattern pattern) {
		Matcher m = pattern.matcher(s);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			Hyperlink spec = new Hyperlink();
			spec.textSpan = s.subSequence(start, end);
			spec.span = new InternalURLSpan(spec.textSpan.toString());
			spec.start = start;
			spec.end = end;
			links.add(spec);
		}
	}

	public class InternalURLSpan extends ClickableSpan {
		private String clickedSpan;

		public InternalURLSpan(String clickedString) {
			clickedSpan = clickedString;
		}

		@Override
		public void onClick(View textView) {
			mListener.onTextLinkClick(textView, clickedSpan);
		}
	}

	class Hyperlink {
		CharSequence textSpan;
		InternalURLSpan span;
		int start;
		int end;
	}
}
