package com.onepas.android.pasgo.ui.search;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchGoogleAdapter extends ArrayAdapter<Place> {

	List<Place> listPlace = new ArrayList<Place>();

	private Context context;
	private double fromLat;
	private double fromLong;
	private DatabaseHandler mDatabaseHandler;

	ViewHolder holder = null;

	public SearchGoogleAdapter(Context context, int textViewResourceId,
							   List<Place> listPlace, double fromLat, double fromLong) {
		super(context, textViewResourceId, listPlace);
		this.listPlace = listPlace;
		this.context = context;
		this.fromLat = fromLat;
		this.fromLong = fromLong;

		mDatabaseHandler = new DatabaseHandler(context);
	}

	private class ViewHolder {
		TextView duong;
		TextView diaChi;
		TextView khoangCach;
		LinearLayout linerStart;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.item_search_near, null);

			holder = new ViewHolder();
			holder.linerStart = (LinearLayout) convertView
					.findViewById(R.id.lnDiemQT);
			holder.duong = (TextView) convertView.findViewById(R.id.lblTenDoiTac);
			holder.diaChi = (TextView) convertView.findViewById(R.id.tvDiaChi);
			holder.khoangCach = (TextView) convertView
					.findViewById(R.id.tvKhoangCach);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.linerStart.setOnClickListener(v -> {
            Place placech = listPlace.get(position);
            boolean check = placech.isCheckStar();
            // String name = placech.getName();
            holder.linerStart.setTag(placech);

            if (check == false) {
                placech.setCheckStar(!check);
                mDatabaseHandler.insertItemPlace(placech);
            } else {
                // delete
                placech.setCheckStar(!check);
                mDatabaseHandler.deleteFavoriteItem(placech);
                notifyDataSetChanged();
            }
            notifyDataSetChanged();

        });
		if (listPlace.size() > 0) {
			Place state = listPlace.get(position);
			boolean check = state.isCheckStar();
			double toLat = listPlace.get(position).getLatitude();
			double toLong = listPlace.get(position).getLongitude();
			double khoangCach = calculateDistance(fromLong, fromLat, toLong,
					toLat);
			khoangCach = khoangCach / 10;
			holder.duong.setText(state.getVicinity());
			holder.diaChi.setText(state.getName());
			holder.khoangCach.setText("" + khoangCach + " Km");
		}

		return convertView;
	}

	private double calculateDistance(double fromLong, double fromLat,
			double toLong, double toLat) {
		double d2r = Math.PI / 180;
		double dLong = (toLong - fromLong) * d2r;
		double dLat = (toLat - fromLat) * d2r;
		double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
				* Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = 6367000 * c;
		return (Math.round(d)) / 100;
	}

	public double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);
		Location locationB = new Location("B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		return distance;

	}

}
