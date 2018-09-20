package com.onepas.android.pasgo.ui.calldriver;

import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

public class DatXeUpdate {
	private static final String TAG = "DatXeUpdate";

	public static final void updateDatXeByTrangThai(String datxeId, int trangThai) {
		String url = WebServiceUtils.URL_UPDATE_GIAO_DICH_DAT_XE(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("datXeId", datxeId);
			jsonParams.put("trangThaiDatXeId", trangThai);
			Utils.Log(TAG, "trang thai"+trangThai);
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject json) {
							if (json != null) {
								Utils.Log(TAG, "updateDatXeByTrangThai " + json);

							}
						}

						@Override
						public void onError(int maloi) {
							Utils.Log(TAG, "updateDatXeByTrangThai maloi " + maloi);
						}

					}, new PWErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Utils.Log(TAG, "updateDatXeByTrangThai ket noi may chu " + error);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			Utils.Log(TAG, "updateDatXeByTrangThai ket noi may chu " + e);
		}
	}
}
