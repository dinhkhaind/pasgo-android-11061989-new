package com.onepas.android.pasgo;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.ui.chat.model.Message;
import com.onepas.android.pasgo.ui.chat.model.MessageAll;
import com.onepas.android.pasgo.ui.chat.model.MessagesFixtures;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.LruBitmapCache;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;


public class Pasgo extends Application {

	public static final String TAG = "PasgoApplication";
    public LruBitmapCache mBitmapCache;

    public static RequestQueue mRequestQueue;
	private static Pasgo sInstance;
	private static Context mAppContext;

	public boolean isUpdatePassWord = false;
    public boolean isGotoUpdatePassWordActivity = false;
	public String token = "";
	public String userId = "";
	public String sdtKhachHang = "";
	public String language = "";
	public String sdt = "";
	public String email = "";
	public String maKichHoat = "";
	public String username = "";
	public String ma = "";
	public boolean isUserNotNull = false;
	public boolean isPopupDatCho = false;
    public String urlAnh = "";
	public PastaxiPref prefs;
	public PastaxiDB mDatabase;
    public boolean isUpdateImage=false;
	public int datTruocNhan=0;
	public int checkInDangCho=0;
	public int datTruocChuaNhan=0;
	public int mNhomKhuyenMaiId;
	public int mTinhId=0;
	//socket
	public static Socket socket;
	//public String  myUser="";//Android khaivd   Guest-11
	public static MessageAll mMessageAll = new MessageAll();
	public boolean isOnChatActivity = false;
	public int offset = 0;
	public boolean mIsLoadMore = true;

	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		sInstance = this;
		mAppContext = this;
		Fresco.initialize(this);
		prefs = new PastaxiPref(this);
		mDatabase = new PastaxiDB(mAppContext);
		//NukeSSLCerts.nuke();
		Utils.Log(TAG, "application Create");
		mRequestQueue = Volley.newRequestQueue(this);
		//
		BackendMonitor pnBackendMponitor = new BackendMonitor();
		registerComponentCallbacks(pnBackendMponitor);
		registerActivityLifecycleCallbacks(pnBackendMponitor);
        getTokenPrefs();
	}
	//socket

	public static synchronized Socket getSocket()
	{

		if(socket==null)
		{
			IO.Options options = new IO.Options();
			options.forceNew = true;
			options.reconnection = true;

			try {
				socket = IO.socket(Constants.SOCKET_URL,options);
				socket.connect();
			} catch (URISyntaxException e) {
				throw new RuntimeException("Socket connection failed.", e);
			}
		}
		return socket;
	}

	public void initSocket()
	{
		getSocket().on(Socket.EVENT_CONNECT, args -> {
            Utils.Log(Pasgo.TAG,"EVENT_CONNECT");
            sendBroadcastSoccase(Socket.EVENT_CONNECT,"EVENT_CONNECT");
            mMessageAll = new MessageAll();
            emitGetName();
        }).on(Socket.EVENT_RECONNECT, args -> {
			Utils.Log(Pasgo.TAG,"EVENT_RECONNECT");
            sendBroadcastSoccase(Socket.EVENT_RECONNECT,"EVENT_CONNECT");
        }).on(Socket.EVENT_DISCONNECT, args -> {
			Utils.Log(Pasgo.TAG,"EVENT_DISCONNECT");
            sendBroadcastSoccase(Socket.EVENT_DISCONNECT,"EVENT_CONNECT");
        }).on(Socket.EVENT_RECONNECTING, args -> {
			Utils.Log(Pasgo.TAG,"EVENT_RECONNECTING");
            sendBroadcastSoccase(Socket.EVENT_RECONNECTING,"EVENT_CONNECT");
        }).on(Constants.E_ROOM_CONVERSATION, args -> {
            JSONObject obj = (JSONObject)args[0];
			Utils.Log(Pasgo.TAG,"E_ROOM_CONVERSATION");
			Utils.Log(Pasgo.TAG,obj.toString());
			final MessageAll messageAll = ParserUtils.getMessageAlls(obj);
			final ArrayList<Message> messages = messageAll.getMessageItems();
			Pasgo.getInstance().offset = messageAll.getOffset();
			if (messageAll.getOffset() == 0) {
				Pasgo.mMessageAll = new MessageAll();
				//phần này dành cho lấy lịch sử
				if (messages.size() == 0)
					mIsLoadMore = false;
				else
				{
					mIsLoadMore = false;
					for (int i = messages.size() - 1; i >= 0; i--) {// xóa hết tin nhắn của ngày hôm trước
						if (!checkDateAlter(messages.get(i).getCreatedAt()))
							messages.remove(messages.get(i));
					}
					/*for (Message item : messages) { // nếu có tin nhắn nhưng chỉ là tin nhắn của tổng đài thì cũng cho hiện thị Lịch sử
						if (item.getUsertype().equals(Constants.IS_KHACH) || item.getUsertype().equals(Constants.IS_TONG_DAI_VIEN)) {
							mIsLoadMore = true;
							break;
						}
					}*/
				}

			}else{
				mIsLoadMore = true;
			}
			if (Pasgo.mMessageAll.getMessageItems().size() == 0)
				Pasgo.mMessageAll = messageAll;
			else
				Pasgo.mMessageAll.getMessageItems().addAll(messages);
			if (messageAll.getOffset() == 0 && !mIsLoadMore) {
				Message message = MessagesFixtures.getMessageXemThemLichSu(new Date(), 1);
				Pasgo.mMessageAll.getMessageItems().add(message);
			}
            sendBroadcastSoccase(Constants.E_ROOM_CONVERSATION, obj.toString());
        }).on(Constants.E_MESSAGE, args -> {
            Utils.Log(Pasgo.TAG,"E_MESSAGE");
            final JSONObject obj = (JSONObject)args[0];
            sendBroadcastSoccase(Constants.E_MESSAGE,obj.toString());
            //Update number
            Intent broadcast = new Intent();
            broadcast
                    .setAction(Constants.BROADCAST_ACTION_NUMBER_MESSAGE);
            sendBroadcast(broadcast);
        }).on(Constants.E_TYPING, args -> {
            final JSONObject obj = (JSONObject)args[0];
            sendBroadcastSoccase(Constants.E_TYPING,obj.toString());
        }).on(Constants.E_TYPING_REMOVE, args -> {
            final JSONObject obj = (JSONObject)args[0];
            sendBroadcastSoccase(Constants.E_TYPING_REMOVE,obj.toString());
        }).on(Constants.E_SUPPORT_ONLINE, args -> {
            final JSONObject obj = (JSONObject)args[0];
            sendBroadcastSoccase(Constants.E_SUPPORT_ONLINE,obj.toString());
        });
	}
	private boolean checkDateAlter(Date dateafter) {
		boolean isCheck = false;
		String date = DatehepperUtil.getCurrentDate(DatehepperUtil.ddMMyyyy) + " 00:00:00";
		Date convertedDate = DatehepperUtil.convertStringToDate(date, DatehepperUtil.ddMMyyyyHHmmss);
		isCheck = dateafter.after(convertedDate);
		return isCheck;
	}
	public void sendBroadcastSoccase(String event, String jsonSring){
		Intent broadcast = new Intent();
		broadcast.setAction(Constants.BROADCAST_ACTION_SOCKET);
		broadcast.putExtra(Constants.KEY_ACTION_SOCKET_EVENT,event);
		broadcast.putExtra(Constants.KEY_ACTION_SOCKET_VALUE,jsonSring);
		sendBroadcast(broadcast);
		// add to object
		if(event.equals(Constants.E_MESSAGE)){
			JSONObject objMsg = ParserUtils.stringToOvject(jsonSring);
			final com.onepas.android.pasgo.ui.chat.model.Message newMessage = ParserUtils.getNewMessage(objMsg);
			mMessageAll.getMessageItems().add(0, newMessage);
		}
	}
	private void emitGetName(){
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", prefs.getRoomChat());
			obj.put("makhachhang", Pasgo.getInstance().ma);
			Utils.Log(TAG,"emit get name"+ obj.toString());
			Pasgo.getSocket().emit(Constants.E_GET_USER_NAME, obj, new Ack() {
				@Override
				public void call(Object... args) {
					Utils.Log(Pasgo.TAG,"E_GET_USER_NAME");
					final JSONObject obj = (JSONObject)args[0];
					prefs.putRoomChat(ParserUtils.getStringValue(obj, "username"));
					Utils.Log(TAG,"emit get name"+ obj.toString());
					emitConnect();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	private void emitConnect()
	{
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", prefs.getRoomChat());
			obj.put("usertype", Constants.IS_KHACH);
			obj.put("makhachhang", Pasgo.getInstance().ma);
			Pasgo.getSocket().emit(Constants.E_CONNECT, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static synchronized void disConnectSocket()
	{
		if(socket!=null && socket.connected())
		{
			socket.disconnect();
			socket =null;
		}
	}
	//end socket
    private void getTokenPrefs() {
        if (Pasgo.getInstance().prefs == null)
            return;
        Pasgo.getInstance().token = Pasgo.getInstance().prefs.getToken();
        Pasgo.getInstance().userId = Pasgo.getInstance().prefs.getUserId();
        Pasgo.getInstance().sdt = Pasgo.getInstance().prefs.getSdt();
        Pasgo.getInstance().maKichHoat = Pasgo.getInstance().prefs
                .getMakichhoat();
        Pasgo.getInstance().ma = Pasgo.getInstance().prefs.getMa();
        Pasgo.getInstance().username = Pasgo.getInstance().prefs.getUserName();
        Pasgo.getInstance().isUpdatePassWord = Pasgo.getInstance().prefs.getisUpdatePassword();
        Pasgo.getInstance().isGotoUpdatePassWordActivity = Pasgo.getInstance().prefs.getIsUpdatePasswordActivity();
        Pasgo.getInstance().urlAnh = Pasgo.getInstance().prefs.getUrlAnh();
        if(StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationCode())||StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationName()))
        {
            Pasgo.getInstance().prefs.putNationCode(Constants.KEY_VIETNAM_CODE);
            Pasgo.getInstance().prefs.putNationName(Constants.KEY_VIETNAM_NAME);
        }
        if(StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().isUserNotNull=false;
        else
            Pasgo.getInstance().isUserNotNull=true;

        setupLanguage();
    }

    private void setupLanguage()
    {
        String language= Pasgo.getInstance().prefs.getLanguage();
        if(StringUtils.isEmpty(language))
        {
            String locale = Locale.getDefault().getLanguage();
            if(locale.equals(Constants.LANGUAGE_VIETNAM))
                language=Constants.LANGUAGE_VIETNAM;
            else
                language=Constants.LANGUAGE_ENGLISH;
        }
        Pasgo.getInstance().language=language;
        Pasgo.getInstance().prefs.putLanguage(language);
        Utils.changeLocalLanguage(language);
    }

	public static Context getAppContext() {
		return mAppContext;
	}

	public static boolean isLogged() {
		PastaxiPref pasWayPref = new PastaxiPref(getAppContext());

		return pasWayPref.getToken() != null
				&& !pasWayPref.getToken().isEmpty()
				&& pasWayPref.getUserId() != null
				&& !pasWayPref.getUserId().isEmpty();
	}

	public static synchronized Pasgo getInstance() {
		return sInstance;

	}

	public PastaxiPref getPaswayPref() {
		if (prefs == null)
			prefs = new PastaxiPref(sInstance);
		return prefs;
	}

	public static void xoaTaiKhoan() {

		PastaxiPref pasWayPref = new PastaxiPref(getAppContext());
		Pasgo.getInstance().token = "";
		Pasgo.getInstance().userId = "";
		Pasgo.getInstance().sdt = "";
		Pasgo.getInstance().username = "";
		Pasgo.getInstance().email = "";
		Pasgo.getInstance().ma = "";
        Pasgo.getInstance().urlAnh = "";
		pasWayPref.putToken(Pasgo.getInstance().token);
		pasWayPref.putUserId(Pasgo.getInstance().userId);
		pasWayPref.putSdt(Pasgo.getInstance().sdt);
		pasWayPref.putUserName(Pasgo.getInstance().username);
		pasWayPref.putEmail(Pasgo.getInstance().email);
		pasWayPref.putMa(Pasgo.getInstance().ma);
        pasWayPref.putUrlAnh(Pasgo.getInstance().urlAnh);
        pasWayPref.putIsKichHoat(false);
        pasWayPref.putIsKichHoatActivity(false);
		pasWayPref.putIsUpdatePassword(false);
        pasWayPref.putIsUpdatePasswordActivity(false);
	}

	private LruBitmapCache getBitmapCache() {
		if (mBitmapCache == null) {
			mBitmapCache = new LruBitmapCache();
		}
		return mBitmapCache;
	}

	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}



	public interface PWErrorListener {
		public void onErrorResponse(VolleyError error);
	}

	public interface PWListener {
		public void onResponse(JSONObject response);

		public void onError(int maloi);
	}

	public interface PWImageListener {
		public void onResponse(Bitmap response);

		public void onError(int maloi);
	}
	public interface PWEImagerrorListener {
		public void onErrorResponse(VolleyError error);
	}
	public void addToRequestQueue(String url, final JSONObject jsonObject,
			final PWListener listener, final PWErrorListener errorListener) {
		Utils.Log(TAG,"url:-->>"+url);
        if(jsonObject!=null)
            Utils.Log(TAG,"json:-->>"+jsonObject.toString());
		JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (!response.has(Constants.KEY_RESPONSE)) {
								listener.onResponse(response);
								return;
							}
							JSONObject objD = new JSONObject(response
									.getString(Constants.KEY_RESPONSE));
                            Utils.Log(TAG,objD.toString());
							int iMaLoi = objD.getInt(Constants.KEY_ERROR_CODE);
							switch (iMaLoi) {
							case Constants.KEY_SUCCESS_RESPONSE:
								listener.onResponse(objD);
								break;
							case 1301:
								listener.onResponse(objD);
								break;
							case Constants.KEY_PROMOTION_IS_NOT_VALID:
								listener.onResponse(objD);
								break;
							case Constants.KEY_PROMOTION_IS_NOT_VALID_V2:
								listener.onResponse(objD);
								break;
                            case Constants.KEY_CHECKIN_MAX:
                                listener.onResponse(objD);
                                break;
							case Constants.KEY_CHECKIN_NGOAI_GIO_PHUC_VU:
								listener.onResponse(objD);
								break;
                            case Constants.KEY_UPDATE_PASSWORD:
                                listener.onResponse(objD);
                                // sau này tất cả các api trả về sẽ sẽ kiểm tra và cùng hiện ra popup đã viết trong .class// hiện tại thì làm như trên
                                /*Intent broadcastUpdatePass = new Intent();
                                broadcastUpdatePass
                                        .setAction(Constants.BROADCAST_UPDATE_PASSWORD);
                                sendBroadcast(broadcastUpdatePass);*/
                                break;
                            case Constants.KEY_REGISTER_PHONE_IS_EXIST:
                                listener.onResponse(objD);
                                break;
                            case Constants.KEY_PRESENTER_CODE_INVAIL:
                                listener.onResponse(objD);
                                break;
                            case Constants.KEY_LOGIN_RESPONSE:
								if(Pasgo.getInstance().prefs.getIsTrial()) return;
                                Utils.Log(TAG, "logout");
                                listener.onError(iMaLoi);
                                Intent broadcast = new Intent();
                                broadcast.setAction(Constants.BROADCAST_ACTION_SIGNOUT);
                                sendBroadcast(broadcast);
                                break;
							case  Constants.KEY_REGISTER_RESPONSE:
								listener.onError(iMaLoi);
								Intent broadcastRegister = new Intent();
								broadcastRegister.setAction(Constants.BROADCAST_ACTION_REQUEST_LOGIN);
								sendBroadcast(broadcastRegister);

								break;
							case Constants.KEY_SQL_ERROR:
								listener.onError(iMaLoi);
								Utils.Log(TAG, objD.getString(Constants.KEY_INFO_RESPONSE));
								break;
							case Constants.KEY_CHECKIN_IS_EXISTS:
								ToastUtils.showToast(getBaseContext(), getString(R.string.reserve_is_exists));
								listener.onError(iMaLoi);
								break;
							default:
								listener.onError(iMaLoi);
                                ToastUtils.showToast(getBaseContext(),objD.getString(Constants.KEY_INFO_RESPONSE));
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (errorListener != null)
							errorListener.onErrorResponse(error);
						else {
                            ToastUtils.showToast(getBaseContext(),getResources()
                                    .getString(
                                            R.string.connect_error_check_network));
						}
						VolleyLog.e("Error: ", error.getMessage());
					}
				});
		req.setRetryPolicy(new DefaultRetryPolicy(
				Constants.CONNECT_SOCKET_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		getRequestQueue().add(req);
	}

	public void addToRequestQueueWithTag(String url, JSONObject jsonObject,
			final PWListener listener, final PWErrorListener errorListener,
			String tag) {
		Utils.Log(TAG,"url:-->>"+url);
		if(jsonObject!=null)
			Utils.Log(TAG,"json:-->>"+jsonObject.toString());
		JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (!response.has(Constants.KEY_RESPONSE)) {
								listener.onResponse(response);
								return;
							}

							JSONObject objD = new JSONObject(response
									.getString(Constants.KEY_RESPONSE));
                            Utils.Log(TAG,objD.toString());
							int iMaLoi = objD.getInt(Constants.KEY_ERROR_CODE);
							switch (iMaLoi) {
							case Constants.KEY_SUCCESS_RESPONSE:
								listener.onResponse(objD);
								break;
							case Constants.KEY_LOGIN_RESPONSE:
								if(Pasgo.getInstance().prefs.getIsTrial()) return;
								Utils.Log(TAG, "logout");
								listener.onError(iMaLoi);
								Intent broadcast = new Intent();
								broadcast
										.setAction(Constants.BROADCAST_ACTION_SIGNOUT);
								sendBroadcast(broadcast);
								break;
                            case Constants.KEY_UPDATE_PASSWORD:
                                listener.onResponse(objD);
                                // sau này tất cả các api trả về sẽ sẽ kiểm tra và cùng hiện ra popup đã viết trong Base.class// hiện tại thì làm như trên
                                /*Intent broadcastUpdatePass = new Intent();
                                broadcastUpdatePass
                                        .setAction(Constants.BROADCAST_UPDATE_PASSWORD);
                                sendBroadcast(broadcastUpdatePass);*/
							break;
							case  Constants.KEY_REGISTER_RESPONSE:
								listener.onError(iMaLoi);
								Intent broadcastRegister = new Intent();
								broadcastRegister.setAction(Constants.BROADCAST_ACTION_REQUEST_LOGIN);
								sendBroadcast(broadcastRegister);
							case Constants.KEY_SQL_ERROR:
								listener.onError(iMaLoi);
								Utils.Log(TAG, objD.getString(Constants.KEY_INFO_RESPONSE));
								break;
							default:
								listener.onError(iMaLoi);
                                ToastUtils.showToast(getBaseContext(),objD.getString(Constants.KEY_INFO_RESPONSE));
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (errorListener != null)
							errorListener.onErrorResponse(error);
						else {
                            ToastUtils.showToast(getBaseContext(),getResources()
                                    .getString(
											R.string.connect_error_check_network));
						}
						VolleyLog.e("Error: ", error.getMessage());
					}
				});
		req.setRetryPolicy(new DefaultRetryPolicy(
				Constants.CONNECT_SOCKET_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		addToRequestQueue(req, tag);
	}


	public void addToRequestGet(String url,final PWListener listener, final PWErrorListener errorListener) {
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						JSONObject json = new JSONObject();
						try {
							json = new JSONObject(response);
							listener.onResponse(json);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (errorListener != null)
							errorListener.onErrorResponse(error);
						else {
							ToastUtils.showToast(getBaseContext(),getResources()
									.getString(
											R.string.connect_error_check_network));
						}
						VolleyLog.e("Error: ", error.getMessage());
					}
		});
		queue.add(stringRequest);
	}
	public void addToRequestGetImage(String url,final PWImageListener listener, final PWEImagerrorListener errorListener)
	{
		RequestQueue rq = Volley.newRequestQueue(this);
		ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				listener.onResponse(response);
			}

		}, 0, 0, null, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (errorListener != null)
					errorListener.onErrorResponse(error);
				else {
					ToastUtils.showToast(getBaseContext(),getResources()
							.getString(
									R.string.connect_error_check_network));
				}
				VolleyLog.e("Error: ", error.getMessage());
			}
		});
		rq.add(ir);
	}
	public void cancelAllToRequestQueue() {
		getRequestQueue().cancelAll(TAG);
	}


	private class BackendMonitor implements
			Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
		@Override
		public void onConfigurationChanged(final Configuration newConfig) {
		}

		@Override
		public void onLowMemory() {
		}

		@Override
		public void onTrimMemory(final int level) {

			if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			} else {
			}

			if (level < ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
					&& mBitmapCache != null) {
				mBitmapCache.evictAll();
			}
		}

		@Override
		public void onActivityCreated(Activity arg0, Bundle arg1) {
		}

		@Override
		public void onActivityDestroyed(Activity arg0) {

		}
		@Override
		public void onActivityResumed(Activity activity) {
			// kiểm tra nếu service chưa bật thì bật nó lên
			Log.e(Pasgo.TAG, Utils.isMyServiceRunning(activity,PTService.class)?" + Đang chạy ":" + Đã tắt");
			Intent broadcast = new Intent();
			broadcast.putExtra(Constants.BUNDLE_KEY_LIFECYCLE_RESUME,true);
			broadcast.setAction(Constants.BROADCAST_ACTION_LIFECYCLE);
			activity.sendBroadcast(broadcast);
		}
		@Override
		public void onActivityPaused(Activity activity) {
			new Date();
			Intent broadcast = new Intent();
			broadcast.putExtra(Constants.BUNDLE_KEY_LIFECYCLE_RESUME,false);
			broadcast.setAction(Constants.BROADCAST_ACTION_LIFECYCLE);
			sendBroadcast(broadcast);
		}

		@Override
		public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
		}

		@Override
		public void onActivityStarted(Activity arg0) {
		}

		@Override
		public void onActivityStopped(Activity arg0) {
		}


	}

	public static String getStringValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? "" : obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	public static JSONObject getJsonObject(JSONObject obj, String name) {

		JSONObject jsonObject = new JSONObject();
		try {
			String str = obj.getString(name);
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONArray getJsonArray(JSONObject obj, String name) {
		JSONArray jsonArray = new JSONArray();
		try {
			String str = obj.getString(name);
			jsonArray = new JSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

}