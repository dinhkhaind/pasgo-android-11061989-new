package com.onepas.android.pasgo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.chat.model.MessagesFixtures;
import com.onepas.android.pasgo.ui.chat.model.Message;
import com.onepas.android.pasgo.ui.chat.model.MessageAll;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.onepas.android.pasgo.models.AnhList;
import com.onepas.android.pasgo.models.AnhSlide;
import com.onepas.android.pasgo.models.BinhLuan;
import com.onepas.android.pasgo.models.BinhLuanKhuyenMai;
import com.onepas.android.pasgo.models.Category;
import com.onepas.android.pasgo.models.CategoryHome;
import com.onepas.android.pasgo.models.CheckedIn;
import com.onepas.android.pasgo.models.ChiTietDoiTac;
import com.onepas.android.pasgo.models.DanhMucChinh;
import com.onepas.android.pasgo.models.DanhSachTaiTroDiemDen;
import com.onepas.android.pasgo.models.DatTruoc;
import com.onepas.android.pasgo.models.DiaChiHangXe;
import com.onepas.android.pasgo.models.DichVu;
import com.onepas.android.pasgo.models.DoiTacLienQuan;
import com.onepas.android.pasgo.models.FilterCategoryItems;
import com.onepas.android.pasgo.models.FilterParent;
import com.onepas.android.pasgo.models.HangXe;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.models.HomeGroupCategory;
import com.onepas.android.pasgo.models.ItemAdressFree;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanh;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanhSearchPlace;
import com.onepas.android.pasgo.models.LichSuChuyenDiItem;
import com.onepas.android.pasgo.models.LinhVucQuanTam;
import com.onepas.android.pasgo.models.LoaiXe;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.LyDoHuy;
import com.onepas.android.pasgo.models.MaDatCho;
import com.onepas.android.pasgo.models.MaDatXe;
import com.onepas.android.pasgo.models.MapStep;
import com.onepas.android.pasgo.models.MapStepChild;
import com.onepas.android.pasgo.models.NationCode;
import com.onepas.android.pasgo.models.NhomDichVu;
import com.onepas.android.pasgo.models.NhomKhuyenMai;
import com.onepas.android.pasgo.models.QuanHuyen;
import com.onepas.android.pasgo.models.ReserveSearch;
import com.onepas.android.pasgo.models.RouteMap;
import com.onepas.android.pasgo.models.SearchNews;
import com.onepas.android.pasgo.models.TagModel;
import com.onepas.android.pasgo.models.TagSpecial;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.models.UserInfo;
import com.onepas.android.pasgo.ui.pasgocard.DatChoDiemDenModel;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public final class ParserUtils {
	private static final String TAG = "ParserUtils";

	public static ArrayList<RouteMap> parserGoogleMap(JSONObject json) {
		ArrayList<RouteMap> distanceGoogleMaps = new ArrayList<RouteMap>();
		RouteMap googleMapInfo;
		try {
			JSONArray arrRoutes = getJsonArray(json, "routes");

			JSONObject objRoutes;
			for (int j = 0; j < arrRoutes.length(); j++) {
				googleMapInfo = new RouteMap();
				objRoutes = arrRoutes.getJSONObject(j);
				JSONArray arrLegs = getJsonArray(objRoutes, "legs");
				JSONObject objLegs = arrLegs.getJSONObject(0);
				JSONObject objDistance = getJsonObject(objLegs, "distance");
				JSONObject objDuration = getJsonObject(objLegs, "duration");
				googleMapInfo.setJsonString(objRoutes.toString());
				googleMapInfo.setDistanceText(getStringValue(objDistance,
						"text"));
				googleMapInfo.setDistanceValue(getDoubleValue(objDistance,
						"value"));
				googleMapInfo.setDurationText(getStringValue(objDuration,
						"text"));
				googleMapInfo
						.setDurationValue(getIntValue(objDuration, "value"));
				googleMapInfo.setStartAddress(getStringValue(objLegs,
						"start_address"));
				googleMapInfo.setEndAddress(getStringValue(objLegs,
						"end_address"));
				JSONObject objStartLocation = getJsonObject(objLegs,
						"start_location");
				JSONObject objEndLocation = getJsonObject(objLegs,
						"end_location");
				googleMapInfo.setStartLocationLat(getDoubleValue(
						objStartLocation, "lat"));
				googleMapInfo.setStartLocationLng(getDoubleValue(
						objStartLocation, "lng"));
				googleMapInfo.setEndLocationLat(getDoubleValue(objEndLocation,
						"lat"));
				googleMapInfo.setEndLocationLng(getDoubleValue(objEndLocation,
						"lng"));
				googleMapInfo.setSummary(getStringValue(objRoutes, "summary"));
				JSONArray arrSteps = getJsonArray(objLegs, "steps");
				List<MapStep> mapStepInfos = getListMapSteps(arrSteps);
				googleMapInfo.setMapStepInfos(mapStepInfos);
				distanceGoogleMaps.add(googleMapInfo);
			}

		} catch (JSONException e) {
		}
		return distanceGoogleMaps;
	}

	public static List<MapStep> getListMapSteps(JSONArray arrSteps) {
		List<MapStep> mapStepInfos = new ArrayList<MapStep>();
		MapStep mapStepInfo;
		try {
			for (int i = 0; i < arrSteps.length(); i++) {
				JSONObject objSteps = arrSteps.getJSONObject(i);
				mapStepInfo = new MapStep();
				JSONObject objDistanceSteps = getJsonObject(objSteps,
						"distance");
				JSONObject objDurationSteps = getJsonObject(objSteps,
						"duration");
				JSONObject objStartLocationSteps = getJsonObject(objSteps,
						"start_location");
				JSONObject objEndLocationSteps = getJsonObject(objSteps,
						"end_location");
				JSONObject objTransitDetails = getJsonObject(objSteps,
						"transit_details");
				JSONObject objLine = getJsonObject(objTransitDetails, "line");
				mapStepInfo.setDistanceText(getStringValue(objDistanceSteps,
						"text"));
				mapStepInfo.setDistanceValue(getStringValue(objDistanceSteps,
						"value"));
				mapStepInfo.setDurationText(getStringValue(objDurationSteps,
						"text"));
				mapStepInfo.setDurationValue(getStringValue(objDurationSteps,
						"value"));
				mapStepInfo.setStartLocationLat(getStringValue(
						objStartLocationSteps, "lat"));
				mapStepInfo.setStartLocationLng(getStringValue(
						objStartLocationSteps, "lng"));
				mapStepInfo.setEndLocationLat(getStringValue(
						objEndLocationSteps, "lat"));
				mapStepInfo.setEndLocationLng(getStringValue(
						objEndLocationSteps, "lng"));
				mapStepInfo.setTravel_mode(getStringValue(objSteps,
						"travel_mode"));
				mapStepInfo.setHtml_instructions(getStringValue(objSteps,
						"html_instructions"));
				mapStepInfo.setManeuver(getStringValue(objSteps, "maneuver"));
				mapStepInfo.setNumberStop(getIntValue(objTransitDetails,
						"num_stops"));
				if (objLine != null) {
					String lineName = getStringValue(objLine, "name");
					mapStepInfo.setLineName(lineName);
				}
				// if key mode = transit then
				JSONArray arrStepsChild = getJsonArray(objSteps, "steps");
				if (arrStepsChild.length() != 0) {
					mapStepInfo
							.setMapStepInfoChilds(getListMapStepChilds(arrStepsChild));
				}
				mapStepInfos.add(mapStepInfo);
			}
		} catch (JSONException e) {
		}
		return mapStepInfos;
	}
	public static ArrayList<DatTruoc> getDatTruocs(JSONObject json) {
		ArrayList<DatTruoc> lists = new ArrayList<DatTruoc>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				DatTruoc item = new DatTruoc();
				item.setTienKhuyenMai(getStringValue(objItem, "TienKhuyenMai"));
				item.setDiaChiDonXe(getStringValue(objItem, "DiaChiDonXe"));
				item.setLoaiHinhId(getIntValue(objItem, "LoaiHinhId"));
				item.setTongSo(getStringValue(objItem, "TongSo"));
				item.setIsHangXe(getIntValue(objItem, "IsHangXe"));
				item.setDatXeId(getStringValue(objItem, "DatXeId"));
				item.setThoiGianDonXe(getStringValue(objItem, "ThoiGianDonXe"));
				item.setDiaChiDen(getStringValue(objItem, "DiaChiDen"));
				item.setTenLoaiHinh(getStringValue(objItem, "TenLoaiHinh"));
				item.setIsKhuyenMai(getIntValue(objItem, "IsKhuyenMai"));
				item.setTienKhuyenMaiFormat(getStringValue(objItem,"TienKhuyenMaiFormat"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static MessageAll getMessageAlls(JSONObject json) {
		MessageAll messageAll = new MessageAll();
		messageAll.setOffset(getIntValue(json,"offset"));
		messageAll.setPagesize(getIntValue(json,"pagesize"));
		messageAll.setErrorcode(getIntValue(json,"errorcode"));
		JSONArray arrMessage = getJsonArray(json, "list");
		messageAll.setMessageItems(getMessageItems(arrMessage));
		return messageAll;
	}

	public static ArrayList<Message> getMessageItems(JSONArray arrItem) {
		ArrayList<Message> lists = new ArrayList<>();
		try {
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				Message item = getMessageItem(objItem);
				lists.add(item);
			}
		} catch (Exception ignored) {
		}
		return lists;
	}
	public static Message getMessageItem(JSONObject objItem) {
		Message item = new Message();
		try {
			JSONObject objNoiDung = new JSONObject(getStringValue(objItem,"NoiDung"));
			item = getNewMessage(objNoiDung);
			String ngayTao =getStringValue(objItem,"NgayTao");
			item.setId(getStringValue(objItem, "Id"));
			item.setMaKhachHang(getStringValue(objItem,"MaKhachHang"));
			item.setRoomName(getStringValue(objItem,"RoomName"));
			item.setNgayTao(ngayTao);
		} catch (Exception e) {
		}
		return item;
	}
	public static Message getNewMessage(JSONObject objItem) {
		Message item = new Message();
		try {
			String sender =getStringValue(objItem,"sender");
			item.setSender(sender);
			item.setReceiver(getStringValue(objItem,"receiver"));
			item.setMessage(getStringValue(objItem,"message"));
			String type = getStringValue(objItem,"usertype");
			item.setUsertype(type);
			String time = getStringValue(objItem,"time");
			String[] separated = time.split("\\.");
			if(separated.length>0)
				time = separated[0];
			time = time.replace("-", "/");
			item.setTime(time);
			item.setLoaitinnhan(getIntValue(objItem,"loaitinnhan"));
			String displayName = getStringValue(objItem,"displayName");
			item.setDisplayName(displayName);
			item.setDisplayColor(getStringValue(objItem,"displayColor"));

			item.setText(getStringValue(objItem,"message"));
			item.setUser(MessagesFixtures.getUser(type.equals(Constants.IS_KHACH), TextUtils.isEmpty(displayName)?sender:displayName));
			Calendar calendar1;
			try{
				calendar1 =  DatehepperUtil.toCalendar(DatehepperUtil.convertStringToDate(time,DatehepperUtil.yyyyMMddHHmmss));
			}catch (Exception e){
				calendar1 = Calendar.getInstance();
			}
			item.setCreatedAt(calendar1.getTime());
			//add auto
			String ngayTao =getStringValue(objItem,time);
			item.setId("");
			item.setMaKhachHang("");
			item.setRoomName("");
			item.setNgayTao(ngayTao);
		} catch (Exception e) {
		}
		return item;
	}
	public static List<MapStepChild> getListMapStepChilds(JSONArray arrSteps) {
		List<MapStepChild> mapStepInfoChilds = new ArrayList<MapStepChild>();
		MapStepChild mapStepInfoChild;
		for (int j = 0; j < arrSteps.length(); j++) {
			JSONObject objStepChilds;
			try {
				objStepChilds = arrSteps.getJSONObject(j);
				mapStepInfoChild = new MapStepChild();
				JSONObject objDistanceStepChilds = getJsonObject(objStepChilds,
						"distance");
				JSONObject objDurationStepChilds = getJsonObject(objStepChilds,
						"duration");
				JSONObject objStartLocationStepChilds = getJsonObject(
						objStepChilds, "start_location");
				JSONObject objEndLocationStepChilds = getJsonObject(
						objStepChilds, "end_location");

				mapStepInfoChild.setDistanceText(getStringValue(
						objDistanceStepChilds, "text"));
				mapStepInfoChild.setDistanceValue(getStringValue(
						objDistanceStepChilds, "value"));
				mapStepInfoChild.setDurationText(getStringValue(
						objDurationStepChilds, "text"));
				mapStepInfoChild.setDurationValue(getStringValue(
						objDurationStepChilds, "value"));
				mapStepInfoChild.setStartLocationLat(getStringValue(
						objStartLocationStepChilds, "lat"));
				mapStepInfoChild.setStartLocationLng(getStringValue(
						objStartLocationStepChilds, "lng"));
				mapStepInfoChild.setEndLocationLat(getStringValue(
						objEndLocationStepChilds, "lat"));
				mapStepInfoChild.setEndLocationLng(getStringValue(
						objEndLocationStepChilds, "lng"));
				mapStepInfoChild.setTravel_mode(getStringValue(objStepChilds,
						"travel_mode"));
				mapStepInfoChild.setHtml_instructions(getStringValue(
						objStepChilds, "html_instructions"));
				mapStepInfoChild.setManeuver(getStringValue(objStepChilds,
						"maneuver"));

				mapStepInfoChilds.add(mapStepInfoChild);
			} catch (JSONException e) {
			}
		}
		return mapStepInfoChilds;
	}

	public static JSONObject getJsonObject(JSONObject obj, String name) {
		JSONObject jsonObject = new JSONObject();
		try {
			String str = obj.getString(name);
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			Utils.Log(TAG, "JSONObject width" + name + " is null");
		}
		return jsonObject;
	}
	public static JSONObject stringToOvject(String str) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			Utils.Log(TAG, "JSONObject width  is null");
		}
		return jsonObject;
	}

	public static JSONArray getJsonArray(JSONObject obj, String name) {
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = obj.getJSONArray(name);
		} catch (JSONException e) {
			Utils.Log(TAG, "array width" + name + " is null");
		}
		return jsonArray;
	}

	public static String getStringValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? "" : obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	public static HashMap<String, String> getBookingIdAndTime(JSONObject json) {
		HashMap<String, String> mapData = new HashMap<String, String>();
		try {
			JSONObject jsonObject = json.getJSONObject("Item");
			if (jsonObject.has("Id"))
				mapData.put("Id", jsonObject.getString("Id"));
			if (jsonObject.has("ThoiGian"))
				mapData.put("ThoiGian", jsonObject.getString("ThoiGian"));
		} catch (Exception e) {
		}
		return mapData;
	}
	public static ArrayList<TinhHome> getAllTinhHome(JSONObject json) {
		ArrayList<TinhHome> tinhs = new ArrayList<TinhHome>();
		try {
			TinhHome item;
			JSONArray arrItem = getJsonArray(json, "Items");
			tinhs.add(new TinhHome(Pasgo.getInstance().getBaseContext().getString(R.string.tinh_default), 0));
			for (int i = 0; i < arrItem.length(); i++) {
				item = new TinhHome();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setId(getIntValue(objItem, "Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setViDo(getDoubleValue(objItem, "ViDo"));
				item.setKinhDo(getDoubleValue(objItem,"KinhDo"));
				tinhs.add(item);
			}
			if(tinhs.size()>0)
				tinhs.get(0).setIsCheck(true);
		} catch (Exception e) {
		}
		return tinhs;
	}

	public static ArrayList<TinhHome> getTinhAllV1(JSONObject json) {
		ArrayList<TinhHome> tinhs = new ArrayList<TinhHome>();
		try {
			TinhHome item;
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				item = new TinhHome();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setId(getIntValue(objItem, "Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setViDo(getDoubleValue(objItem, "ViDo"));
				item.setKinhDo(getDoubleValue(objItem,"KinhDo"));
				tinhs.add(item);
			}
			if(tinhs.size()>0)
				tinhs.get(0).setIsCheck(true);
		} catch (Exception e) {
		}
		return tinhs;
	}
	public static HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> getReasonToMap(
			JSONObject json,
			HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> mapDiaChiChiNhanh) {
		if (mapDiaChiChiNhanh == null)
			mapDiaChiChiNhanh = new HashMap<>();
		try {
			JSONArray jSONArray = json.getJSONArray("Items");
			HashMap<String, ItemDiaChiChiNhanh> itemDiaChiChiNhanhs;
			for (int i = 0; i < jSONArray.length(); i++) {
				itemDiaChiChiNhanhs = new HashMap<>();
				ItemDiaChiChiNhanh itemDiaChiChiNhanh = new ItemDiaChiChiNhanh();
				JSONObject objItem = jSONArray.getJSONObject(i);
				itemDiaChiChiNhanh.setId(objItem.getString("Id"));
				itemDiaChiChiNhanh.setTen(objItem.getString("Ten"));
				itemDiaChiChiNhanh.setDiaChi(objItem.getString("DiaChi"));
				itemDiaChiChiNhanh.setKinhDo(objItem.getDouble("KinhDo"));
				itemDiaChiChiNhanh.setMoTa(objItem.getString("MoTa"));
				if (objItem.has("DoiTacKhuyenMaiId"))
					itemDiaChiChiNhanh.setDoiTacKhuyenMaiId(objItem
							.getString("DoiTacKhuyenMaiId"));
				itemDiaChiChiNhanh.setViDo(objItem.getDouble("ViDo"));
				itemDiaChiChiNhanh.setMaNhomKM(objItem.getString("MaNhomKM"));
				itemDiaChiChiNhanh.setTitleKM(objItem.getString("TitleKM"));
				itemDiaChiChiNhanh.setNhomCnDoiTacId(objItem
						.getString("NhomCnDoiTacId"));
				itemDiaChiChiNhanh.setDatTruoc(ParserUtils.getBooleanValue(objItem, "DatTruoc"));
				itemDiaChiChiNhanh.setWebsite(objItem.getString("Website"));
				if (mapDiaChiChiNhanh.containsKey(itemDiaChiChiNhanh
						.getMaNhomKM()))
					itemDiaChiChiNhanhs = mapDiaChiChiNhanh
							.get(itemDiaChiChiNhanh.getMaNhomKM());
				if (itemDiaChiChiNhanhs.containsKey(itemDiaChiChiNhanh.getId()))
					continue;
				itemDiaChiChiNhanhs.put(itemDiaChiChiNhanh.getId(),
						itemDiaChiChiNhanh);
				mapDiaChiChiNhanh.put(itemDiaChiChiNhanh.getMaNhomKM(),
						itemDiaChiChiNhanhs);
			}
		} catch (Exception e) {
		}
		return mapDiaChiChiNhanh;
	}

    public static HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> getPartnerToMap(
            JSONObject json,
            HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> mapDiaChiChiNhanh) {
        if (mapDiaChiChiNhanh == null)
            mapDiaChiChiNhanh = new HashMap<>();
        try {
            JSONArray jSONArray = json.getJSONArray("Items");
            HashMap<String, ItemDiaChiChiNhanhSearchPlace> itemDiaChiChiNhanhs;
            for (int i = 0; i < jSONArray.length(); i++) {
                itemDiaChiChiNhanhs = new HashMap<String, ItemDiaChiChiNhanhSearchPlace>();
                ItemDiaChiChiNhanhSearchPlace itemDiaChiChiNhanh = new ItemDiaChiChiNhanhSearchPlace();
                JSONObject objItem = jSONArray.getJSONObject(i);
                itemDiaChiChiNhanh.setId(objItem.getString("Id"));
                itemDiaChiChiNhanh.setTen(objItem.getString("Ten"));
                itemDiaChiChiNhanh.setDiaChi(objItem.getString("DiaChi"));
                itemDiaChiChiNhanh.setKinhDo(objItem.getDouble("KinhDo"));
                itemDiaChiChiNhanh.setMoTa(objItem.getString("MoTa"));
                if (objItem.has("DoiTacKhuyenMaiId"))
                    itemDiaChiChiNhanh.setDoiTacKhuyenMaiId(objItem
                            .getString("DoiTacKhuyenMaiId"));
                itemDiaChiChiNhanh.setViDo(objItem.getDouble("ViDo"));
                itemDiaChiChiNhanh.setMaNhomKM(objItem.getString("MaNhomKM"));
                itemDiaChiChiNhanh.setTitleKM(objItem.getString("TitleKM"));
                itemDiaChiChiNhanh.setNhomCnDoiTacId(objItem
						.getString("NhomCnDoiTacId"));
                itemDiaChiChiNhanh.setDatTruoc(ParserUtils.getBooleanValue(objItem, "DatTruoc"));
                itemDiaChiChiNhanh.setWebsite(objItem.getString("Website"));
                itemDiaChiChiNhanh.setChiNhanhDoiTacId(getStringValue(objItem, "ChiNhanhId"));
                if (mapDiaChiChiNhanh.containsKey(itemDiaChiChiNhanh
                        .getMaNhomKM()))
                    itemDiaChiChiNhanhs = mapDiaChiChiNhanh
                            .get(itemDiaChiChiNhanh.getMaNhomKM());
                if (itemDiaChiChiNhanhs.containsKey(itemDiaChiChiNhanh.getId()))
                    continue;
                itemDiaChiChiNhanhs.put(itemDiaChiChiNhanh.getId(),
                        itemDiaChiChiNhanh);
                mapDiaChiChiNhanh.put(itemDiaChiChiNhanh.getMaNhomKM(),
                        itemDiaChiChiNhanhs);
            }
        } catch (Exception e) {
        }
        return mapDiaChiChiNhanh;
    }

    public static HashMap<String, DiaChiHangXe> getMapDiaChiHangXes(
            JSONObject json,
            HashMap<String, DiaChiHangXe> mapDiaChiChiNhanh) {
        if (mapDiaChiChiNhanh == null)
            mapDiaChiChiNhanh = new HashMap<>();
        try {
            JSONArray jSONArray = json.getJSONArray("ItemsHangXe");
            for (int i = 0; i < jSONArray.length(); i++) {
                DiaChiHangXe item = new DiaChiHangXe();
                JSONObject objItem = jSONArray.getJSONObject(i);
                item.setId(objItem.getString("Id"));
                item.setTen(objItem.getString("Ten"));
                item.setDiaChi(objItem.getString("DiaChi"));
                item.setKinhDo(objItem.getDouble("KinhDo"));
                item.setViDo(objItem.getDouble("ViDo"));
                if (mapDiaChiChiNhanh.containsKey(item.getId()))
                    continue;
                mapDiaChiChiNhanh.put(item.getId(), item);
            }
        } catch (Exception e) {
        }
        return mapDiaChiChiNhanh;
    }
	/*public static ArrayList<Tinh> getAllTinh(JSONObject json) {
		ArrayList<Tinh> tinhs = new ArrayList<Tinh>();
		try {
			Tinh taxiObject;
			JSONArray arrItem = getJsonArray(json, "Items");
			tinhs.add(new Tinh(Pasgo.getInstance().getBaseContext().getString(R.string.tinh_default), 0));
			for (int i = 0; i < arrItem.length(); i++) {
				taxiObject = new Tinh();
				JSONObject objItem = arrItem.getJSONObject(i);
				taxiObject.setId(getIntValue(objItem, "Id"));
				taxiObject.setTen(getStringValue(objItem, "Ten"));
				tinhs.add(taxiObject);
			}
			if(tinhs.size()>0)
				tinhs.get(0).setIsCheck(true);
		} catch (Exception e) {
		}
		return tinhs;
	}*/
	/*public static ArrayList<Tinh> getAllTinhDefault(JSONObject json) {
		ArrayList<Tinh> tinhs = new ArrayList<Tinh>();
		try {
			Tinh taxiObject;
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				taxiObject = new Tinh();
				JSONObject objItem = arrItem.getJSONObject(i);
				taxiObject.setId(getIntValue(objItem, "Id"));
				taxiObject.setTen(getStringValue(objItem, "Ten"));
				tinhs.add(taxiObject);
			}
			if(tinhs.size()>0)
				tinhs.get(0).setIsCheck(true);
		} catch (Exception e) {
		}
		return tinhs;
	}*/

	public static ArrayList<NhomKhuyenMai> getNhomKhuyenMaiFilters(Context context,JSONObject json) {
		ArrayList<NhomKhuyenMai> lists = new ArrayList<NhomKhuyenMai>();
		try {
			NhomKhuyenMai object;
			JSONArray arrItem = getJsonArray(json, "Items");
			object = new NhomKhuyenMai();
			object.setId(-1);
			object.setTen(context.getString(R.string.filter_nhom_km_title));
			object.setMa("");
			object.setIsParent(true);
			lists.add(object);
			for (int i = 0; i < arrItem.length(); i++) {
				object = new NhomKhuyenMai();
				JSONObject objItem = arrItem.getJSONObject(i);
				object.setId(getIntValue(objItem, "Id"));
				object.setTen(getStringValue(objItem, "Ten"));
				object.setMa(getStringValue(objItem, "Ma"));
				lists.add(object);
			}
			if(lists.size()>0)
				lists.get(1).setIsCheck(true);
		} catch (Exception e) {
		}
		return lists;
	}
	public static ArrayList<FilterParent> getAllFilters(JSONObject json) {
		ArrayList<FilterParent> lists = new ArrayList<>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				FilterParent item = new FilterParent();
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setDescription(getStringValue(objItem, "Description"));
				item.setNameVn(getStringValue(objItem, "NameVn"));
				item.setChoiceType(getIntValue(objItem, "ChoiceType"));
				item.setTypeTermId(getIntValue(objItem, "TypeTermId"));
				item.setNameEn(getStringValue(objItem, "NameEn"));
				item.setOrder(getIntValue(objItem, "Order"));
				item.setCode(getStringValue(objItem, "Code"));
				item.setId(getIntValue(objItem, "Id"));
				JSONArray arrItemCategory = getJsonArray(objItem, "FilterCategoryItems");
				item.setFilterCategoryItems(getFilterCategories(arrItemCategory));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static ArrayList<Tinh> getAllTinhGiaGiaFilters(Context context,JSONObject json) {
		ArrayList<Tinh> lists = new ArrayList<Tinh>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			// add title
				Tinh itemt = new Tinh();
				itemt.setId(-1);
				itemt.setTen(context.getString(R.string.filter_tinh_title));
				itemt.setIsParent(true);
				lists.add(itemt);
			// add gan ban
				Tinh itemg = new Tinh();
				itemg.setId(0);
				itemg.setTen(context.getString(R.string.tinh_default));
				itemg.setIsParent(false);
				itemg.setIsCheck(true);
				lists.add(itemg);
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				Tinh item = new Tinh();
				item.setId(getIntValue(objItem, "Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}

	public static ArrayList<FilterCategoryItems> getFilterCategories(JSONArray arrItem) {
		ArrayList<FilterCategoryItems> lists = new ArrayList<FilterCategoryItems>();
		try {

			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				FilterCategoryItems item = new FilterCategoryItems();
				item.setDescription(getStringValue(objItem, "Description"));
				item.setFilterCategoryTermId(getIntValue(objItem, "FilterCategoryTermId"));
				item.setNameVn(getStringValue(objItem, "NameVn"));
				item.setSelected(getBooleanValue(objItem, "IsSelected"));
				item.setNameEn(getStringValue(objItem, "NameEn"));
				item.setFilterCategoryId(getIntValue(objItem, "FilterCategoryId"));
				item.setId(getIntValue(objItem, "Id"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}

    public static HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>> getLichSuChuyenDis(JSONObject json) {
        HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>> getLichSuChuyenDi=new HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>>();
        ArrayList<Integer> years=new ArrayList<Integer>();
        // get all year
        JSONArray arrItemGetMonthYear = getJsonArray(json, "Items");
        for (int i = 0; i < arrItemGetMonthYear.length(); i++) {
            try {
                JSONObject objItem = arrItemGetMonthYear.getJSONObject(i);
                int year = 2015;
                try {
                    Date date = DatehepperUtil.stringToDate(getStringValue(objItem, "ThoiGianDonXe"), DatehepperUtil.yyyyMMddHHmmss);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    year = cal.get(Calendar.YEAR);
                } catch (Exception e) {
                    year = 2015;
                }
                if(!years.contains(year))
                    years.add(year);
            }catch (Exception e)
            {

            }
        }

        // lay cac thang trong nam do
		ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>> listMonths;
		for (int y : years)
		{
			listMonths= new ArrayList<>();
			for(int m=12;m>=1;m--)
			{
				HashMap<Integer,ArrayList<LichSuChuyenDiItem>> itemMonth=new HashMap<Integer, ArrayList<LichSuChuyenDiItem>>();
				ArrayList<LichSuChuyenDiItem> items = new ArrayList<LichSuChuyenDiItem>();
				try {
					LichSuChuyenDiItem object;
					JSONArray arrItem = getJsonArray(json, "Items");
					for (int i = 0; i < arrItem.length(); i++) {
						object = new LichSuChuyenDiItem();
						JSONObject objItem = arrItem.getJSONObject(i);
						object.setDiemDen(getStringValue(objItem, "DiaChiDen"));
						object.setDiemDon(getStringValue(objItem, "DiaChiDonXe"));
						object.setDateTime(getStringValue(objItem, "ThoiGianDonXe"));
						object.setiD(getStringValue(objItem, "Id"));
						object.setKhuyenMai(getIntValue(objItem, "IsKhuyenMai") == 0 ? false : true);
						object.setHangXe(getIntValue(objItem, "IsHangXe") == 0 ? false : true);
						object.setTienKhuyenMai(getStringValue(objItem, "TienKhuyenMai"));
						object.setTenLoaiHinh(getStringValue(objItem, "TenLoaiHinh"));
						object.setTienKhuyenMaiFormat(getStringValue(objItem, "TienKhuyenMaiFormat"));
						object.setLoaiHinhId(getIntValue(objItem, "LoaiHinhId"));
						object.setIsThanhCong(getIntValue(objItem, "IsThanhCong"));
						int month=1,year=2015;
						try {
							Date date= DatehepperUtil.stringToDate(getStringValue(objItem, "ThoiGianDonXe"),DatehepperUtil.yyyyMMddHHmmss);
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							month = cal.get(Calendar.MONTH);
							if(month<12) month++;
							year = cal.get(Calendar.YEAR);
						}catch (Exception e)
						{
							month=1;year=2015;
						}
						object.setMonth(month);
						object.setYear(year);
						if(month==m && year ==y)
							items.add(object);
					}
				} catch (Exception e) {
				}
				if(items.size()>0) {
					itemMonth.put(m, items);
					listMonths.add(itemMonth);
				}
			}
			getLichSuChuyenDi.put(y,listMonths);
		}
		return getLichSuChuyenDi;
	}

	public static boolean getStateOrder(JSONObject json) {
		try {
			return json.getBoolean("Item");
		} catch (Exception e) {
		}
		return true;
	}

	public static long getUnixTimeNow(JSONObject json) {
		try {
			JSONObject jsonObject = json.getJSONObject("Item");
			if (jsonObject.has("Time"))
				return jsonObject.getLong("Time");
		} catch (Exception e) {
		}
		return 0;
	}

	public static ArrayList<LocationMessageDriver> getDriverIds(String json,
			HashMap<String, LocationMessageDriver> locationMsgDriverMap) {
		ArrayList<LocationMessageDriver> locationMessageDrivers = new ArrayList<LocationMessageDriver>();
		try {
			JSONArray arrItem = new JSONArray(json);
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				if (objItem != null
						&& objItem.has("id")
						&& locationMsgDriverMap.containsKey(objItem
								.getString("id")))
					locationMessageDrivers.add(locationMsgDriverMap.get(objItem
							.getString("id")));
			}
		} catch (Exception e) {
		}
		return locationMessageDrivers;
	}

	public static ArrayList<LoaiXe> getAllLoaiXe(JSONObject json) {
		ArrayList<LoaiXe> loaiXes = new ArrayList<LoaiXe>();
		try {
			LoaiXe taxiObject;
			JSONArray arrItem = getJsonArray(json, "Items");
			loaiXes.add(new LoaiXe(0, R.drawable.ic_taxi_all, StringUtils
					.getStringByResourse(
							Pasgo.getInstance().getBaseContext(),
							R.string.chon_loai_xe_tat_ca)));
			for (int i = 0; i < arrItem.length(); i++) {
				taxiObject = new LoaiXe();
				JSONObject objItem = arrItem.getJSONObject(i);
				taxiObject.setId(getIntValue(objItem, "Id"));
				taxiObject.setImage(R.drawable.ic_taxi_all);
				taxiObject.setName(getStringValue(objItem, "Ten"));
				loaiXes.add(taxiObject);
			}

			for (LoaiXe loaiXe : loaiXes) {
				if (loaiXe.getId() == 1) {
					loaiXe.setImage(R.drawable.ic_taxi_4_hatback);
				}
				if (loaiXe.getId() == 2) {
					loaiXe.setImage(R.drawable.ic_taxi_4_sedan);
				}
				if (loaiXe.getId() == 3) {
					loaiXe.setImage(R.drawable.ic_taxi_7cho);
				}
			}
		} catch (Exception e) {
		}
		return loaiXes;
	}

	public static ArrayList<ReserveSearch> getTuKhoaTimKiem(Context context,JSONObject json) {
		ArrayList<ReserveSearch> lists = new ArrayList<ReserveSearch>();
		try {

			ReserveSearch item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "KeyItems");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new ReserveSearch();
				item.setTuKhoa(getStringValue(objItem, "TuKhoa"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}

	public static ArrayList<QuanHuyen> getQuanHuyens(JSONObject json) {
		ArrayList<QuanHuyen> lists = new ArrayList<QuanHuyen>();
		try {
			QuanHuyen item;
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new QuanHuyen();
				item.setId(getIntValue(objItem,"Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static ArrayList<QuanHuyen> getQuanHuyenFilters(Context context,JSONObject json) {
		ArrayList<QuanHuyen> lists = new ArrayList<QuanHuyen>();
		try {
			QuanHuyen item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "QuanItems");
			lists.add(new QuanHuyen(context.getString(R.string.tat_ca),0,true));
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new QuanHuyen();
				item.setId(getIntValue(objItem,"Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}

	public static ArrayList<DanhMucChinh> getDanhMucChinhs(JSONObject json) {
		ArrayList<DanhMucChinh> lists = new ArrayList<>();
		try {
			DanhMucChinh item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "DanhMucChinhs");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new DanhMucChinh();
				item.setId(getIntValue(objItem,"Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static ArrayList<TagModel> getTagModels(Context context, JSONObject json) {
		ArrayList<TagModel> lists = new ArrayList<TagModel>();
		try {
			TagModel item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "TagItems");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new TagModel();
				item.setId(getIntValue(objItem,"Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setMa("");
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static ArrayList<TagSpecial> getTagSpecials(JSONObject json) {
		ArrayList<TagSpecial> lists = new ArrayList<>();
		try {
			TagSpecial item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "TagSpecialItems");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new TagSpecial();
				item.setId(getIntValue(objItem,"Id"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setMoTa(getStringValue(objItem,"Mota"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}

	public static ArrayList<SearchNews> getSearchNews(JSONObject json) {
		ArrayList<SearchNews> lists = new ArrayList<>();
		try {
			SearchNews item;
			JSONObject objAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objAll, "SearchNews");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				item = new SearchNews();
				item.setId(getStringValue(objItem,"Id"));
				item.setTieuDe(getStringValue(objItem, "TieuDe"));
				item.setAnh(getStringValue(objItem, "Anh"));
				item.setNgayBatDau(getStringValue(objItem, "NgayBatDau"));
				item.setMoTa(getStringValue(objItem, "MoTa"));
				item.setLoaiTinKhuyenMai(getIntValue(objItem, "LoaiTinKhuyenMai"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
    public static ArrayList<ItemAdressFree> getAllAdressFrees(JSONObject json) {
        ArrayList<ItemAdressFree> lists = new ArrayList<ItemAdressFree>();
        try {
            JSONArray arrItem = getJsonArray(json, "Items");
            for (int i = 0; i < arrItem.length(); i++) {
                JSONObject objItem = arrItem.getJSONObject(i);
                String id = getStringValue(objItem, "Id");
                String Ma =getStringValue(objItem, "Ma");
                String doiTacKhuyenMaiId = getStringValue(objItem, "DoiTacKhuyenMaiId");
                String tenDoiTac = getStringValue(objItem, "Ten");
                String tenDuong = getStringValue(objItem, "DiaChi");
                String logo = getStringValue(objItem, "Logo");
                double km = Utils.DoubleFomat(getDoubleValue(objItem, "Km"));
                int gia = getIntValue(objItem, "Gia");
                String numberLike = getStringValue(objItem,"LuotThich");
                String numberComment = getStringValue(objItem, "SoBinhLuan");
                String nhomCNDoiTacId = getStringValue(objItem, "NhomCnDoiTacId");
                String tieuDeKhuyenMai = getStringValue(objItem, "TieuDeKhuyenMai");
                double danhGia = getDoubleValue(objItem,"DanhGia");
                int trangThai = getIntValue(objItem,"TrangThai");
                int soCheckIn =getIntValue(objItem, "SoCheckIn");
                int nhomKhuyenMaiId =getIntValue(objItem,"NhomKhuyenMaiId");
                double viDo = getDoubleValue(objItem,"ViDo");
                double kinhDo = getDoubleValue(objItem,"KinhDo");
                String chiNhanhDoiTacId =getStringValue(objItem,"ChiNhanhId");
                ItemAdressFree item = new ItemAdressFree();
                item.setId(id);
                item.setMa(Ma);
                item.setDoiTacKhuyenMaiId(doiTacKhuyenMaiId);
                item.setTenDoiTac(tenDoiTac);
                item.setTenDuong(tenDuong);
                item.setLogo(logo);
                item.setKm(km);
                item.setGia(gia);
                item.setNumberComment(numberComment);
                item.setNumberLike(numberLike);
                item.setNhomCNDoiTacId(nhomCNDoiTacId);
                item.setSoCheckIn(soCheckIn);
                item.setTieuDeKhuyenMai(tieuDeKhuyenMai);
                item.setDanhGia(danhGia);
                item.setTrangThai(trangThai);
                item.setNhomKhuyenMaiId(nhomKhuyenMaiId);
                item.setViDo(viDo);
                item.setKinhDo(kinhDo);
                item.setChiNhanhDoiTacId(chiNhanhDoiTacId);
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setChuyenMon(getStringValue(objItem,"ChuyenMon"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
                lists.add(item);
            }
        } catch (Exception e) {
        }
        return lists;
    }

	public static String getTenLoaiHinh(Context context,int loaiHinhId)
	{
		String tenLooaiHinh="";
		switch (loaiHinhId)
		{
			case 0:
				tenLooaiHinh = context.getString(R.string.dat_xe_loai0);
				break;
			case 1:
				tenLooaiHinh = context.getString(R.string.dat_xe_loai1);
				break;
			case 2:
				tenLooaiHinh = context.getString(R.string.dat_xe_loai2);
				break;
			case 3:
				tenLooaiHinh = context.getString(R.string.dat_xe_loai3);
				break;
			default:
				tenLooaiHinh = context.getString(R.string.dat_xe_loai0);
				break;
		}
		return tenLooaiHinh;
	}
    public static ArrayList<LinhVucQuanTam> getAlLinhVucQuanTams(JSONObject json) {
        ArrayList<LinhVucQuanTam> lists = new ArrayList<LinhVucQuanTam>();
        try {
            LinhVucQuanTam object;
            JSONArray arrItem = getJsonArray(json, "Items");
            for (int i = 0; i < arrItem.length(); i++) {
                object = new LinhVucQuanTam();
                JSONObject objItem = arrItem.getJSONObject(i);
                object.setId(getStringValue(objItem, "Id"));
                object.setTen(getStringValue(objItem, "Ten"));
                object.setMa(getStringValue(objItem, "Ma"));
                lists.add(object);
            }
        } catch (Exception e) {
        }
        return lists;
    }


    public static ArrayList<NhomDichVu> getNhomDichVus(JSONArray arrItem) {
        ArrayList<NhomDichVu> lists = new ArrayList<NhomDichVu>();
        try {
            NhomDichVu object;
            for (int i = 0; i < arrItem.length(); i++) {
                object = new NhomDichVu();
                JSONObject objItem = arrItem.getJSONObject(i);
                object.setId(getIntValue(objItem, "Id"));
                object.setTen(getStringValue(objItem, "Ten"));
                object.setDichVus(getDichVus(getJsonArray(objItem, "DichVus")));
                lists.add(object);
            }
        } catch (Exception e) {
        }
        return lists;
    }

    public static ArrayList<DichVu> getDichVus(JSONArray arrItem) {
        ArrayList<DichVu> lists = new ArrayList<DichVu>();
        try {
            DichVu object;
            for (int i = 0; i < arrItem.length(); i++) {
                object = new DichVu();
                JSONObject objItem = arrItem.getJSONObject(i);
                object.setDichVuId(getIntValue(objItem, "DichVuId"));
                object.setTenDichVu(getStringValue(objItem, "TenDichVu"));
                object.setGiamGia(getStringValue(objItem, "GiamGia"));
				object.setDatTruoc(getIntValue(objItem, "DatTruoc"));
                lists.add(object);
            }
        } catch (Exception e) {
        }
        return lists;
    }

	public static ArrayList<CategoryHome> getCategoryHomes(JSONObject json)
	{
		ArrayList<CategoryHome> getDiemDenModelTagList = new ArrayList<>();
		try {
			JSONObject objTag = getJsonObject(json, "Item");
			JSONArray jsonArray = getJsonArray(objTag,"Categorys");
			for (int i = 0; i < jsonArray.length(); i++) {
				CategoryHome object = new CategoryHome();
				JSONObject objItem = jsonArray.getJSONObject(i);
				object.setId(getIntValue(objItem, "Id"));
				object.setTagId(getIntValue(objItem, "TagId"));
				object.setTen(getStringValue(objItem, "Ten"));
				object.setTagModels(getTagModels(getJsonArray(objItem,"Child")));
				getDiemDenModelTagList.add(object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getDiemDenModelTagList;
	}

	public static ArrayList<TagModel> getTagModels(JSONArray jsonArray)
	{
		ArrayList<TagModel> getDiemDenModelTagList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				TagModel object = new TagModel();
				JSONObject objItem = jsonArray.getJSONObject(i);
				object.setId(getIntValue(objItem, "TagId")); // Id
				object.setTagId(getIntValue(objItem, "TagId"));
				object.setTen(getStringValue(objItem, "Ten"));
				getDiemDenModelTagList.add(object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getDiemDenModelTagList;
	}

	public static ArrayList<Category> getCategoryParents(JSONObject json) {

		ArrayList<Category> listItems = new ArrayList<>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			Category item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new Category();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setCountChild(getIntValue(objItem,"CountChild"));
				item.setParentId(getIntValue(objItem,"ParentId"));
				item.setAnh(getStringValue(objItem,"Anh"));
				item.setTen(getStringValue(objItem,"Ten"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
				item.setId(getIntValue(objItem,"Id"));
				item.setTagId(getIntValue(objItem,"TagId"));
				item.setDoiTacKhuyenMai(getIntValue(objItem,"IsDoiTacKhuyenMai")==1);
				item.setTitle(true);
				listItems.add(item);
				for (Category cate: getCategoryChilds(getJsonArray(objItem,"Child"))) {
					listItems.add(cate);
				}
			}
		} catch (Exception e) {
		}
		return listItems;
	}
	public static ArrayList<Category> getCategoryChilds(JSONArray arrItem)
	{
		ArrayList<Category> ListItems = new ArrayList<>();
		try {
			Category item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new Category();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setCountChild(getIntValue(objItem,"CountChild"));
				item.setParentId(getIntValue(objItem,"ParentId"));
				item.setAnh(getStringValue(objItem,"Anh"));
				item.setTen(getStringValue(objItem,"Ten"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
				item.setId(getIntValue(objItem,"Id"));
				item.setTagId(getIntValue(objItem,"TagId"));
				item.setDoiTacKhuyenMai(getIntValue(objItem,"IsDoiTacKhuyenMai")==1);
				item.setTitle(false);
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<DoiTacLienQuan> getDoiTacLienQuans(JSONObject json) {

		ArrayList<DoiTacLienQuan> ListItems = new ArrayList<DoiTacLienQuan>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			DoiTacLienQuan item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DoiTacLienQuan();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem,"NhomKhuyenMaiId"));
				item.setTrangThai(getIntValue(objItem,"TrangThai"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setNhomCnDoiTacId(getStringValue(objItem,"NhomCnDoiTacId"));
				item.setLogo(getStringValue(objItem,"Logo"));
				item.setCoThePASGO(getIntValue(objItem,"CoThePASGO"));
				item.setTongDaiPASGO(getStringValue(objItem,"TongDaiPASGO"));
				item.setKm(getStringValue(objItem,"Km"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
				item.setDiaChi(getStringValue(objItem,"DiaChi"));
				item.setTen(getStringValue(objItem,"Ten"));
				item.setId(getStringValue(objItem,"Id"));
				item.setChatLuong(getDoubleValue(objItem,"ChatLuong"));
				item.setChuyenMon(getStringValue(objItem,"ChuyenMon"));
				item.setDanhGia(getDoubleValue(objItem,"DanhGia"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ChiTietDoiTac getChiTietDoiTac(JSONObject json) {
		ChiTietDoiTac item = new ChiTietDoiTac();
		try {
			JSONObject objItem = getJsonObject(json, "Item");
			item.setBangGia(getStringValue(objItem, "BangGia"));
			item.setMoTaUuDai(getStringValue(objItem, "MoTaUuDai"));
			item.setMoTaGioiThieu(getStringValue(objItem, "MoTaGioiThieu"));
			item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
			int daCheckin= getIntValue(objItem,"DaCheckin");
			item.setDaCheckin(daCheckin!=0);
			JSONArray arrBinhLuans = getJsonArray(objItem, "BinhLuans");
			item.setBinhLuans(getBinhLuans(arrBinhLuans));
			JSONArray arrAnhSlides = getJsonArray(objItem, "AnhSlide");
			item.setAnhSlides(getAnhSlides(arrAnhSlides));
			item.setTenNhaHang(getStringValue(objItem,"TenNhaHang"));
			item.setGioPhucVu(getStringValue(objItem,"GioPhucVu"));
			item.setKhoangGia(getStringValue(objItem,"KhoangGia"));
			item.setDiaChi(getStringValue(objItem,"DiaChi"));
			JSONArray arrDoiTacLienQuans = getJsonArray(objItem, "DoiTacLienQuan");
			item.setDoiTacLienQuans(getDoiTacLienQuans(arrDoiTacLienQuans));
			JSONArray arrAnhBangGias = getJsonArray(objItem, "AnhBangGia");
			item.setAnhBangGias(getAnhBangGias(arrAnhBangGias));
			item.setViDo(getDoubleValue(objItem,"ViDo"));
			item.setKinhDo(getDoubleValue(objItem,"KinhDo"));
			item.setId(getStringValue(objItem,"Id"));
			JSONArray arrAnhLists = getJsonArray(objItem, "AnhList");
			item.setAnhLists(getAnhLists(arrAnhLists));
			item.setChatLuong(getDoubleValue(objItem,"ChatLuong"));
			item.setChuyenMon(getStringValue(objItem,"ChuyenMon"));
			item.setDacTrung(getStringValue(objItem,"DacTrung"));
			item.setDanhGia(getDoubleValue(objItem,"DanhGia"));
			item.setDaYeuThich(getIntValue(objItem,"DaYeuThich"));
			item.setUrlContent(getStringValue(objItem,"UrlContent"));
			item.setTrangThai(getIntValue(objItem,"TrangThai"));
			item.setMaNhomKhuyenMai(getStringValue(objItem,"MaNhomKhuyenMai"));
			int pheDuyet = getIntValue(objItem,"PheDuyet");
			item.setPheDuyet(pheDuyet!=0);
			item.setNhomCnDoiTacId(getStringValue(objItem,"NhomCnDoiTacId"));
			item.setDoiTacId(getStringValue(objItem,"DoiTacId"));
			item.setNhomKhuyenMaiId(getIntValue(objItem,"NhomKhuyenMaiId"));
			item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
			item.setLogo(getStringValue(objItem,"Logo"));
			item.setMaNhomKhuyenMai(getStringValue(objItem,"MaNhomKhuyenMai"));
			item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
		} catch (Exception e) {
		}
		return item;
	}
	public static ArrayList<BinhLuan> getBinhLuans(JSONArray jsonArray)
	{
		ArrayList<BinhLuan> binhLuans = new ArrayList<>();
		BinhLuan item;
		for (int i = 0; i < jsonArray.length(); i++) {
			item = new BinhLuan();
			JSONObject objItem = null;
			try {
				objItem = jsonArray.getJSONObject(i);
				item.setThoiGian(getStringValue(objItem, "ThoiGian"));
				item.setNguoiDungId(getStringValue(objItem, "NguoiDungId"));
				item.setTongSo(getIntValue(objItem,"TongSo"));
				item.setAnhNguoiDung(getStringValue(objItem,"AnhNguoiDung"));
				item.setId(getStringValue(objItem, "Id"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setNoiDung(getStringValue(objItem,"NoiDung"));
				item.setTenNguoiDung(getStringValue(objItem,"TenNguoiDung"));
				binhLuans.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return  binhLuans;
	}

	public static ArrayList<AnhSlide> getAnhSlides(JSONArray jsonArray)
	{
		ArrayList<AnhSlide> binhLuans = new ArrayList<>();
		AnhSlide iten;
		for (int i = 0; i < jsonArray.length(); i++) {
			iten = new AnhSlide();
			JSONObject objItem = null;
			try {
				objItem = jsonArray.getJSONObject(i);
				iten.setAnh(getStringValue(objItem,"Anh"));
				binhLuans.add(iten);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return  binhLuans;
	}
	public static ArrayList<DoiTacLienQuan> getDoiTacLienQuans(JSONArray jsonArray)
	{
		ArrayList<DoiTacLienQuan> binhLuans = new ArrayList<>();
		DoiTacLienQuan item;
		for (int i = 0; i < jsonArray.length(); i++) {
			item = new DoiTacLienQuan();
			JSONObject objItem = null;
			try {
				objItem = jsonArray.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem,"NhomKhuyenMaiId"));
				item.setTrangThai(getIntValue(objItem,"TrangThai"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setNhomCnDoiTacId(getStringValue(objItem,"NhomCnDoiTacId"));
				item.setLogo(getStringValue(objItem,"Logo"));
				item.setCoThePASGO(getIntValue(objItem,"CoThePASGO"));
				item.setTongDaiPASGO(getStringValue(objItem,"TongDaiPASGO"));
				item.setKm(getStringValue(objItem,"Km"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
				item.setDiaChi(getStringValue(objItem,"DiaChi"));
				item.setTen(getStringValue(objItem,"Ten"));
				item.setId(getStringValue(objItem,"Id"));
				item.setChatLuong(getDoubleValue(objItem,"ChatLuong"));
				item.setChuyenMon(getStringValue(objItem,"ChuyenMon"));
				item.setDanhGia(getDoubleValue(objItem,"DanhGia"));
				binhLuans.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return  binhLuans;
	}

	public static ArrayList<AnhBangGia> getAnhBangGias(JSONArray jsonArray)
	{
		ArrayList<AnhBangGia> binhLuans = new ArrayList<>();
		AnhBangGia iten;
		for (int i = 0; i < jsonArray.length(); i++) {
			iten = new AnhBangGia();
			JSONObject objItem = null;
			try {
				objItem = jsonArray.getJSONObject(i);
				iten.setTongSo(getIntValue(objItem,"TongSo"));
				iten.setAnh(getStringValue(objItem,"Anh"));
				binhLuans.add(iten);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return  binhLuans;
	}
	public static ArrayList<AnhList> getAnhLists(JSONArray jsonArray)
	{
		ArrayList<AnhList> binhLuans = new ArrayList<>();
		AnhList iten;
		for (int i = 0; i < jsonArray.length(); i++) {
			iten = new AnhList();
			JSONObject objItem = null;
			try {
				objItem = jsonArray.getJSONObject(i);
				iten.setTongSo(getIntValue(objItem,"TongSo"));
				iten.setAnh(getStringValue(objItem,"Anh"));
				binhLuans.add(iten);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return  binhLuans;
	}
	public static ArrayList<BinhLuanKhuyenMai> getBinhLuanKhuyenMais(
			JSONObject json) {
		ArrayList<BinhLuanKhuyenMai> binhLuanKhuyenMais = new ArrayList<BinhLuanKhuyenMai>();
		try {
			JSONObject objItems = getJsonObject(json, "Item");
			JSONArray arrBinhLuan = getJsonArray(objItems, "BinhLuans");
            String bestBinhLuan = getStringValue(objItems, "BestBinhLuan");
            BinhLuanKhuyenMai iten;
            if (!StringUtils.isEmpty(bestBinhLuan)) {
                JSONObject objItemBestBinhLuan = getJsonObject(objItems,
                        "BestBinhLuan");
                iten = new BinhLuanKhuyenMai();
                iten.setLuotThich(getStringValue(objItemBestBinhLuan,
                        "LuotThich"));
                iten.setDaThich(getIntValue(objItemBestBinhLuan, "DaThich"));
                iten.setThoiGian(getStringValue(objItemBestBinhLuan, "ThoiGian"));
                iten.setNoiDung(getStringValue(objItemBestBinhLuan, "NoiDung"));
                iten.setTenNguoiDung(getStringValue(objItemBestBinhLuan,
                        "TenNguoiDung"));
                iten.setDanhGia(getDoubleValue(objItemBestBinhLuan,"DanhGia"));
                iten.setBestBinhLuan(true);
                iten.setId(getStringValue(objItemBestBinhLuan, "Id"));
                iten.setNguoiDungId(getStringValue(objItemBestBinhLuan,"NguoiDungId"));
                binhLuanKhuyenMais.add(iten);
            }
			for (int i = 0; i < arrBinhLuan.length(); i++) {
				iten = new BinhLuanKhuyenMai();
				JSONObject objItem = arrBinhLuan.getJSONObject(i);
				iten.setLuotThich(getStringValue(objItem, "LuotThich"));
				iten.setDaThich(getIntValue(objItem, "DaThich"));
				iten.setThoiGian(getStringValue(objItem, "ThoiGian"));
				iten.setNoiDung(getStringValue(objItem, "NoiDung"));
				iten.setTenNguoiDung(getStringValue(objItem, "TenNguoiDung"));
				iten.setId(getStringValue(objItem, "Id"));
                iten.setDanhGia(getDoubleValue(objItem,"DanhGia"));
                iten.setNguoiDungId(getStringValue(objItem,"NguoiDungId"));
                iten.setBestBinhLuan(false);
				binhLuanKhuyenMais.add(iten);
			}

		} catch (Exception e) {
		}
		return binhLuanKhuyenMais;
	}

    public static boolean getSex(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? true : obj.getBoolean(key);
        } catch (JSONException e) {//
            return false;
        }
    }
    public static UserInfo getUserInfo(JSONObject json) {
        UserInfo userInfo=new UserInfo();
        try{
            JSONObject objItems = getJsonObject(json, "Item");
            JSONObject objNguoiDung = getJsonObject(objItems, "NguoiDung");
            userInfo.setGioiTinh(getSex(objNguoiDung,"GioiTinh"));
            userInfo.setIdCode(getStringValue(objNguoiDung, "IdCode"));
            userInfo.setId(getStringValue(objNguoiDung, "Id"));
            userInfo.setEmail(getStringValue(objNguoiDung,"Email"));
            userInfo.setSdt(getStringValue(objNguoiDung,"Sdt"));
            userInfo.setNgaySinh(getStringValue(objNguoiDung,"NgaySinh"));
            userInfo.setTenNguoiDung(getStringValue(objNguoiDung,"TenNguoiDung"));
            userInfo.setUrlAnh(getStringValue(objNguoiDung, "UrlAnh"));
            ArrayList<LinhVucQuanTam> linhVucQuanTams = new ArrayList<LinhVucQuanTam>();
            JSONArray arrLinhVucQuanTams = getJsonArray(objItems, "LinhVucQuanTams");
            LinhVucQuanTam iten;
            for (int i = 0; i < arrLinhVucQuanTams.length(); i++) {
                iten = new LinhVucQuanTam();
                JSONObject objItem = arrLinhVucQuanTams.getJSONObject(i);
                iten.setId(getStringValue(objItem, "Id"));
                iten.setTen(getStringValue(objItem, "Ten"));
                iten.setMa(getStringValue(objItem, "Ma"));
                linhVucQuanTams.add(iten);
            }
            userInfo.setLinhVucQuanTams(linhVucQuanTams);

        } catch (Exception e) {
        }

        return userInfo;
    }

	public static ArrayList<TinKhuyenMaiDoiTac> getTinKhuyenMaiDoiTacs(JSONObject jsonKhuyenmai) {

		ArrayList<TinKhuyenMaiDoiTac> ListItems = new ArrayList<>();
		try {
			JSONObject objItems = getJsonObject(jsonKhuyenmai, "Item");
			JSONArray arrItem = getJsonArray(objItems, "DoiTacKhuyenMais");
			TinKhuyenMaiDoiTac item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new TinKhuyenMaiDoiTac();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setNhomCnDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setKm(getStringValue(objItem, "Km"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setId(getStringValue(objItem, "Id"));
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}


	public static ArrayList<CheckedIn> getCheckedInLists(JSONObject json) {

		ArrayList<CheckedIn> ListItems = new ArrayList<CheckedIn>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			CheckedIn item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new CheckedIn();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTenChiNhanh(getStringValue(objItem, "Ten"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setThoiGianDen(getStringValue(objItem, "ThoiGianDen"));
				item.setSoNguoiDen(getIntValue(objItem, "SoNguoiDen"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setDoiTacId(getStringValue(objItem, "DoiTacId"));
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setTongSo(getIntValue(objItem, "TongSo"));
				item.setTieuDeKhuyenMai(getStringValue(objItem, "TieuDeKhuyenMai"));
				item.setGia(getStringValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setTienKhuyenMai(getDoubleValue(objItem, "TienKhuyenMai"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setTrangThaiDatCho(getIntValue(objItem,"TrangThaiDatCho"));
				item.setSoTreEm(getIntValue(objItem,"SoTreEm"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				item.setViDo(getDoubleValue(objItem,"ViDo"));
				item.setKinhDo(getDoubleValue(objItem,"KinhDo"));
				item.setMaNhomKhuyenMai(getStringValue(objItem,"MaNhomKhuyenMai"));
				item.setSoDienThoaiFormat(getStringValue(objItem,"SoDienThoaiFormat"));
				item.setTenKhachHang(getStringValue(objItem,"TenKhachHang"));
				//item.setTrangThaiDatCho(i+1);
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}


	public static ArrayList<DatChoDiemDenModel> getDatChoDiemDens(JSONObject json) {

		ArrayList<DatChoDiemDenModel> ListItems = new ArrayList<>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			DatChoDiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DatChoDiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCNDoiTacId"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setWebsite(getStringValue(objItem, "website"));
				item.setViDo(getDoubleValue(objItem, "ViDo"));
				item.setKinhDo(getDoubleValue(objItem, "KinhDo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setChiNhanhId(getStringValue(objItem, "ChiNhanhId"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTenChiNhanh(getStringValue(objItem, "TenChiNhanh"));
				item.setDatTruoc(getBooleanValue(objItem, "DatTruoc"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setThoiGianDen(getStringValue(objItem, "ThoiGianDen"));
				item.setSoNguoiDen(getIntValue(objItem, "SoNguoiDen"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setId(getStringValue(objItem, "Id"));
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setDieuKienMobile(getStringValue(objItem, "DieuKienMobile"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}

	public static ArrayList<DiemDenModel> getSearchDiemDens(JSONObject json) {

		ArrayList<DiemDenModel> ListItems = new ArrayList<>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			DiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setGia(getIntValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setId(getStringValue(objItem, "Id"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}

	public static ArrayList<DiemDenModel> getSearchResultDiemDens(JSONObject json) {

		ArrayList<DiemDenModel> ListItems = new ArrayList<>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objItemAll, "listDoiTac");
			DiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setGia(getIntValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setId(getStringValue(objItem, "Id"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<DiemDenModel> getHomeDetailDiemDens(JSONObject json) {

		ArrayList<DiemDenModel> ListItems = new ArrayList<>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objItemAll, "DiemDenItems");
			DiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setGia(getIntValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setId(getStringValue(objItem, "Id"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<DiemDenModel> getHomeCategoryDetailDiemDens(JSONObject json) {

		ArrayList<DiemDenModel> ListItems = new ArrayList<>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			DiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setGia(getIntValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setId(getStringValue(objItem, "Id"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<TagModel> getCategoryTags(JSONObject json) {

		ArrayList<TagModel> ListItems = new ArrayList<>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objItemAll, "Categorys");
			TagModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new TagModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setId(getIntValue(objItem, "TagId")); // Id
				item.setTagId(getIntValue(objItem, "TagId"));
				item.setTen(getStringValue(objItem, "Ten"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<ReserveSearch> getGoiYTimKiems(JSONObject json) {

		ArrayList<ReserveSearch> ListItems = new ArrayList<>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objItemAll, "KeyItems");
			ReserveSearch item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new ReserveSearch();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setTuKhoa(getStringValue(objItem, "TuKhoa"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	public static ArrayList<DiemDenModel> getSearchDiemDensV1(JSONObject json) {

		ArrayList<DiemDenModel> ListItems = new ArrayList<DiemDenModel>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrItem = getJsonArray(objItemAll, "DiemDenItems");
			DiemDenModel item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DiemDenModel();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setNhomKhuyenMaiId(getIntValue(objItem, "NhomKhuyenMaiId"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCnDoiTacId"));
				item.setTrangThai(getIntValue(objItem, "TrangThai"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setKm(Utils.DoubleFomat(getDoubleValue(objItem, "Km")));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setTen(getStringValue(objItem, "Ten"));
				item.setGia(getIntValue(objItem, "Gia"));
				item.setDaThich(getIntValue(objItem, "DaThich"));
				item.setMa(getStringValue(objItem, "Ma"));
				item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setId(getStringValue(objItem, "Id"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setCoThePASGO(getIntValue(objItem, "CoThePASGO") != 0);
				item.setChuyenMon(getStringValue(objItem, "ChuyenMon"));
				item.setTongDaiPASGO(getStringValue(objItem, "TongDaiPASGO"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				item.setGiaTrungBinh(getStringValue(objItem,"GiaTrungBinh"));
				item.setLoaiHopDong(getIntValue(objItem,"LoaiHopDong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}

	public static ArrayList<MaDatXe> getDanhSachMaKhuyenMai(
			JSONObject json) {
		
		ArrayList<MaDatXe> ListItems = new ArrayList<MaDatXe>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			MaDatXe item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new MaDatXe();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setTenMa(getStringValue(objItem, "TenMa"));
				item.setDiemDen(getStringValue(objItem, "DiemDen"));
				item.setHanSuDung(getStringValue(objItem, "HanSuDung"));
				item.setMaID(getStringValue(objItem, "MaID"));
				item.setLoaiKhuyenMai(getIntValue(objItem, "LoaiKhuyenMai"));
				item.setSoTien(getIntValue(objItem, "SoTien"));
				item.setHieuLuc(getBooleanValue(objItem, "HieuLuc"));
				item.setTongSo(getIntValue(objItem,"TongSo"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}

	public static ArrayList<MaDatCho> getMaDatChos(
			JSONObject json) {

		ArrayList<MaDatCho> ListItems = new ArrayList<MaDatCho>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			MaDatCho item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new MaDatCho();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setId(getStringValue(objItem, "Id"));
				item.setMaTaiTro(getStringValue(objItem, "MaTaiTro"));
				item.setGiamGia(getStringValue(objItem, "GiamGia"));
				item.setNgayBatDau(getStringValue(objItem, "NgayBatDau"));
				item.setNgayKetThuc(getStringValue(objItem, "NgayKetThuc"));
				item.setNhaPhatHanh(getStringValue(objItem, "NhaPhatHanh"));
				item.setQuangCao(getStringValue(objItem, "QuangCao"));
				item.setTongSo(getIntValue(objItem,"TongSo"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}

	public static ArrayList<DanhSachTaiTroDiemDen> getDanhSachDiemDen(
			JSONObject json) {
		
		ArrayList<DanhSachTaiTroDiemDen> ListItems = new ArrayList<DanhSachTaiTroDiemDen>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			DanhSachTaiTroDiemDen item;
			for (int i = 0; i < arrItem.length(); i++) {
				item = new DanhSachTaiTroDiemDen();
				JSONObject objItem = arrItem.getJSONObject(i);
				item.setLuotThich(getIntValue(objItem, "LuotThich"));
				item.setNhomCNDoiTacId(getStringValue(objItem, "NhomCNDoiTacId"));
				item.setChiNhanhId(getStringValue(objItem, "ChiNhanhId"));
				item.setSoBinhLuan(getIntValue(objItem, "SoBinhLuan"));
				item.setDiaChi(getStringValue(objItem, "DiaChi"));
				item.setLogo(getStringValue(objItem, "Logo"));
				item.setViDo(getDoubleValue(objItem, "ViDo"));
				double km=getDoubleValue(objItem, "Km");
				item.setKm(Utils.DoubleFomat(km));
				item.setKinhDo(getDoubleValue(objItem, "KinhDo"));
				item.setTenChiNhanh(getStringValue(objItem, "TenChiNhanh"));
				item.setDatTruoc(getIntValue(objItem, "DatTruoc"));
				item.setSoCheckIn(getIntValue(objItem, "SoCheckIn"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem, "DoiTacKhuyenMaiId"));
				item.setWebsite(getStringValue(objItem, "Website"));
                item.setTrangThai(getIntValue(objItem, "TrangThai"));
                item.setDanhGia(getDoubleValue(objItem, "DanhGia"));
				item.setChietKhau(getIntValue(objItem, "ChietKhau"));
				item.setChietKhauHienThi("-" + getIntValue(objItem, "ChietKhau") + "%");
				item.setChuyenMon(getStringValue(objItem,"ChuyenMon"));
				item.setTaiTro(getStringValue(objItem,"TaiTro"));
				item.setChatLuong(getDoubleValue(objItem, "ChatLuong"));
				ListItems.add(item);
			}
		} catch (Exception e) {
		}
		return ListItems;
	}
	
	
	public static ArrayList<HangXe> getHangXes(JSONObject json) {
		ArrayList<HangXe> arrayTaxiObject = new ArrayList<HangXe>();
		try {
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);				
				String hangXeId= getStringValue(objItem, "Id");
				String tenHang= getStringValue(objItem, "Ten");
				arrayTaxiObject.add(new HangXe(hangXeId, tenHang));
			}
		} catch (Exception e) {
		}
		return arrayTaxiObject;
	}

	public static ArrayList<LyDoHuy> getLyDoHuys(JSONObject json) {
		ArrayList<LyDoHuy> arrayTaxiObject = new ArrayList<LyDoHuy>();
		try {
			LyDoHuy taxiObject;
			JSONArray arrItem = getJsonArray(json, "Items");
			for (int i = 0; i < arrItem.length(); i++) {
				taxiObject = new LyDoHuy();
				JSONObject objItem = arrItem.getJSONObject(i);
				taxiObject.setId(getStringValue(objItem, "Id"));
				taxiObject.setName(getStringValue(objItem, "Ten"));
				arrayTaxiObject.add(taxiObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayTaxiObject;
	}

    public static ArrayList<NationCode> getAllNationCodes(JSONObject json) {
        ArrayList<NationCode> lists = new ArrayList<NationCode>();
        ArrayList<NationCode> listAdds = new ArrayList<NationCode>();
        try {
            NationCode object;
            JSONArray arrItem = getJsonArray(json, "Items");
            for (int i = 0; i < arrItem.length(); i++) {
                object = new NationCode();
                JSONObject objItem = arrItem.getJSONObject(i);
                object.setId(getStringValue(objItem, "Id"));
                object.setTen(getStringValue(objItem, "Ten"));
                object.setMa(getStringValue(objItem, "Ma"));
                object.setTenHienThi(getStringValue(objItem, "TenHienThi"));
                lists.add(object);
            }
        } catch (Exception e) {
        }
        String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++) {
            // sắp xếp theo thứ tự A....Z
            sections[i] = String.valueOf(mSections.charAt(i));
            ArrayList<NationCode> listSearchs = new ArrayList<NationCode>();
            for(int j=0;j<lists.size();j++)
            {
                String name= lists.get(j).getTen();
                if(String.valueOf(name.charAt(0)).equals(sections[i]))
                    listSearchs.add(lists.get(j));
            }
            if(listSearchs.size()>0)
            {
                listAdds.add(new NationCode(Constants.KEY_NATION_CODE_ID,sections[i],"",""));
                listAdds.addAll(listSearchs);
            }
        }
        return listAdds;
    }

	public static ArrayList<HomeGroupCategory> getHomeGroupCategories(JSONObject json) {
		ArrayList<HomeGroupCategory> lists = new ArrayList<>();
		try {
			JSONObject objItemAll = getJsonObject(json,"Item");
			JSONArray arrGroupCategory = getJsonArray(objItemAll, "GroupCategorys");
			for (int i = 0; i < arrGroupCategory.length(); i++) {
				JSONObject objItem = arrGroupCategory.getJSONObject(i);
				HomeGroupCategory item = new HomeGroupCategory();
				item.setLoaiGiaoDien(getIntValue(objItem, "LoaiGiaoDien"));
				item.setGroupName(getStringValue(objItem, "GroupName"));
				item.setGroupId(getIntValue(objItem, "GroupId"));
				item.setXemThemId(getIntValue(objItem,"XemThemId"));
				item.setSapXep(i+1);
				item.setSoLanXuatHien(soLanXuatHien(lists,item.getLoaiGiaoDien()));
				JSONArray arrCategory = getJsonArray(objItem, "Categorys");
				item.setHomeCategories(getHomeCategories(arrCategory));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	public static int soLanXuatHien(ArrayList<HomeGroupCategory> homeGroupCategories, int loaiGiaoDien)
	{
		int soLanXuatHien =1;
		for(HomeGroupCategory item:homeGroupCategories)
		{
			if(item.getLoaiGiaoDien() == loaiGiaoDien)
				soLanXuatHien ++;
		}
		return soLanXuatHien;
	}
	public static ArrayList<HomeCategory> getBoSuTaps(JSONObject object)
	{
		JSONArray arrCategory = getJsonArray(object, "Items");
		return getHomeCategories(arrCategory);
	}
	public static ArrayList<HomeCategory> getHomeCategories(JSONArray arrItem) {
		ArrayList<HomeCategory> lists = new ArrayList<>();
		try {

			for (int i = 0; i < arrItem.length(); i++) {
				JSONObject objItem = arrItem.getJSONObject(i);
				HomeCategory item = new HomeCategory();
				item.setId(getStringValue(objItem, "Id"));
				item.setTieuDe(getStringValue(objItem,"TieuDe"));
				item.setMoTa(getStringValue(objItem,"Mota"));
				item.setCaption(getStringValue(objItem,"Caption"));
				String anh = getStringValue(objItem,"Anh");
				if(!StringUtils.isEmpty(anh))
					anh+="&width=400";
				item.setAnh(anh);
				item.setSapXep(getIntValue(objItem, "Id"));
				item.setDoiTacKhuyenMaiId(getStringValue(objItem,"DoiTacKhuyenMaiId"));
				item.setChatLuong(getDoubleValue(objItem,"ChatLuong"));
				item.setTagId(getIntValue(objItem,"TagId"));
				item.setDanhGia(getDoubleValue(objItem,"DanhGia"));
				item.setDoiTacKhuyenMai(getIntValue(objItem,"IsDoiTacKhuyenMai")==1);
				item.setCaption1(getStringValue(objItem,"Caption1"));
				lists.add(item);
			}
		} catch (Exception e) {
		}
		return lists;
	}
	/**
	 * Get long value
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static long getLongValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0L : obj.getLong(key);
		} catch (JSONException e) {
			return 0L;
		}
	}

	public static int getIntValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0 : obj.getInt(key);
		} catch (JSONException e) {
			return 0;
		}
	}

    public static int getIntValueResponse(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? -1 : obj.getInt(key);
        } catch (JSONException e) {
            return -1;
        }
    }

	public static Double getDoubleValue(JSONObject obj, String key) {
		double d = 0.0;
		try {
			d = obj.isNull(key) ? d : obj.getDouble(key);
			return d;
		} catch (JSONException e) {
			return d;
		}
	}

	public static boolean getBooleanValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? false : obj.getBoolean(key);
		} catch (JSONException e) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static String JsonDateToDate(String jsonDate) {
		// "/Date(1321867151710)/"
		int idx1 = jsonDate.indexOf("(");
		int idx2 = jsonDate.indexOf(")");
		String s = jsonDate.substring(idx1 + 1, idx2);
		long l = Long.valueOf(s);
		Date result = new Date(l);
		int Year = result.getYear() + 1900;
		return result.getDate() + "/" + result.getMonth() + "/" + Year;
	}
}