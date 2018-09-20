package com.onepas.android.pasgo.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.chat.model.Message;
import com.onepas.android.pasgo.ui.chat.model.MessageAll;
import com.onepas.android.pasgo.ui.chat.model.MessagesFixtures;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.socket.client.Ack;
import io.socket.client.Socket;

public class FragmentChatActivity extends BaseFragment implements MessagesListAdapter.OnMessageLongClickListener<Message>,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.AddTextChangedListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener {
    private final String TAG = "FragmentChat";
    private View mRoot;
    private boolean isTyping = false;
    private long mLastTimeTyping = 0;
    private ArrayList<String> mListTyping = new ArrayList<>();
    private TextView mTvListTyping;
    protected Toolbar mToolbar;
    private TextView mTvPhone, mTvEmail;
    private LinearLayout mLnChatInfo;
    private MessagesList messagesList;
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private Menu menu;
    private int selectionCount;
    private boolean mIsOverTime = false;
    private int mLoaiTinNhan = 1;
    private String mUrl = "App-Android-Trang chủ";
    private boolean mIsGoToDetail = false;
    //goto reversed
    private boolean mIsGotoReversed = false;
    private boolean mIsGotoReversedSendDone = false;
    private String mTextReserved = "";

    public FragmentChatActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_chat, container, false);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.tab_home5));
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle == null)
                bundle = savedInstanceState;
            getBudle(bundle);
            initView();
            initializeMenu();
            IntentFilter intentSocket = new IntentFilter(
                    Constants.BROADCAST_ACTION_SOCKET);
            getActivity().registerReceiver(broadcastSocket, intentSocket);
        }
        if (Pasgo.socket == null || !Pasgo.socket.connected()) {
            Pasgo.socket = null;
            Pasgo.getInstance().initSocket();
        }
        return mRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getBudle(Bundle bundle) {
        if (bundle != null) {
            mLoaiTinNhan = bundle.getInt(Constants.BUNDLE_KEY_LOAI_TIN_NHAN, 2);
            mUrl = bundle.getString(Constants.BUNDLE_KEY_CHAT_URL, "");
            mIsGoToDetail = bundle.getBoolean(Constants.BUNDLE_KEY_GO_TO_DETAIL, false);
            mIsGotoReversed = bundle.getBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_RESERVED_HISTORY, false);
            mTextReserved = bundle.getString(Constants.BUNDLE_KEY_TEXT_FROM_RESERVED_HISTORY, "");
            mIsGotoReversedSendDone = bundle.getBoolean("sendDone", false);
        }
        if (!mIsGoToDetail) {
            mLoaiTinNhan = 1;
            mUrl = "App-Android-Trang chủ";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.BUNDLE_KEY_LOAI_TIN_NHAN, mLoaiTinNhan);
        outState.putString(Constants.BUNDLE_KEY_CHAT_URL, mUrl);
        outState.putBoolean(Constants.BUNDLE_KEY_GO_TO_DETAIL, mIsGoToDetail);
        outState.putString(Constants.BUNDLE_KEY_TEXT_FROM_RESERVED_HISTORY, mTextReserved);
        outState.putBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_RESERVED_HISTORY, mIsGotoReversed);
        outState.putBoolean("sendDone", mIsGotoReversedSendDone);
    }


    private void initializeMenu() {
        try {
            mToolbar.setNavigationIcon(R.drawable.icon_menu);
            HomeActivity activity = (HomeActivity) getActivity();
            activity.mNavigationDrawerFragment.setUp(R.id.drawer_fragment, activity.mDrawerLayout, mToolbar);
            activity.mNavigationDrawerFragment.menuDatXe.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                activity.goToDatXe(HomeActivity.KEY_SERVICE_TAXI);
            });
            activity.mNavigationDrawerFragment.menuCategory.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, CategoryActivity.class);
            });
            activity.mNavigationDrawerFragment.menuTaiTro.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ThePasgoActivity.class);
            });

            activity.mNavigationDrawerFragment.menuLichSuChuyenDi.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, SuccessfulTripsActivity.class);
            });
            activity.mNavigationDrawerFragment.menuHuongDanSuDung.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, GuidActivity.class);
            });

            activity.mNavigationDrawerFragment.menuQuyDinhChung.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, TermsAndPoliciesActivity.class);
            });
            activity.mNavigationDrawerFragment.menuGioiThieuBanBe.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ShareActivity.class);
            });
            activity.mNavigationDrawerFragment.menuThietLap.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, Pasgo.getInstance().language);
                bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, activity.mIsShowKhuyenMai);
                gotoActivity(mActivity, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            });
            activity.mNavigationDrawerFragment.menuThoat.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                activity.menuThoat();
            });
            activity.mNavigationDrawerFragment.menuYeuThich.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, FavoriteActivity.class);
            });
            activity.mNavigationDrawerFragment.dat_truoc_rl.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, CalledDriverActivity.class);
            });
            activity.mNavigationDrawerFragment.check_in_rl.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ReservedHistoryActivity.class);
            });
            activity.mNavigationDrawerFragment.lnAccount.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, AccountManagerActivity.class);
            });
        } catch (Exception e) {
            mToolbar.setNavigationIcon(R.drawable.ic_action_back);
            mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft(getActivity()));
        }


    }
    @Override
    protected void initView() {
        super.initView();
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(getActivity()).load(url).into(imageView);
            }
        };
        mLnChatInfo = (LinearLayout) mRoot.findViewById(R.id.chat_info_ln);
        mLnChatInfo.setVisibility(View.GONE);
        mTvPhone = (TextView) mRoot.findViewById(R.id.phone_tv);
        mTvEmail = (TextView) mRoot.findViewById(R.id.email_tv);
        mTvPhone.setText(Pasgo.getInstance().sdt);
        mTvEmail.setText(Pasgo.getInstance().prefs.getEmail());
        mRoot.findViewById(R.id.start_chat_btn).setOnClickListener(v -> {
            emitUpdateInfo();
        });

        mTvListTyping = (TextView) mRoot.findViewById(R.id.listTyping);
        messagesList = (MessagesList) mRoot.findViewById(R.id.messagesList);
        initAdapter();
        initSocket();
        MessageInput input = (MessageInput) mRoot.findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        input.setAddTextChangedListener(this);
        mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);

    }

    private final BroadcastReceiver broadcastSocket = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String event = bundle.getString(Constants.KEY_ACTION_SOCKET_EVENT, "");
                String value = bundle.getString(Constants.KEY_ACTION_SOCKET_VALUE, "");
                String parentName = getActivity().getClass().getSimpleName();
                switch (event) {
                    case Socket.EVENT_RECONNECT:
                        ArrayList<Message> message = Pasgo.mMessageAll.getMessageItems();
                        if (message == null || message.size() == 0)
                            emitConversation(0);
                        break;
                    case Socket.EVENT_DISCONNECT:
                        break;
                    case Socket.EVENT_RECONNECTING:
                        break;
                    case Constants.E_MESSAGE:
                        JSONObject objMsg = ParserUtils.stringToOvject(value);
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String msg = ParserUtils.getStringValue(objMsg, "message");
                                    String sender = ParserUtils.getStringValue(objMsg, "sender");
                                    boolean isKhach = ParserUtils.getStringValue(objMsg, "usertype").equals(Constants.IS_KHACH);
                                    String time = ParserUtils.getStringValue(objMsg, "time");
                                    String displayName = ParserUtils.getStringValue(objMsg, "displayName");
                                    String displayColor = ParserUtils.getStringValue(objMsg, "displayColor");
                                    boolean isSendReversed = ParserUtils.getBooleanValue(objMsg, "isSendReversed");
                                    if (!isSendReversed || parentName.equals("HomeActivity"))
                                        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(msg, isKhach, sender, new Date(), displayName, displayColor), true);
                                }
                            });
                        break;
                    case Constants.E_ROOM_CONVERSATION:
                        JSONObject obj = ParserUtils.stringToOvject(value);
                        final MessageAll messageAll = ParserUtils.getMessageAlls(obj);
                        final ArrayList<Message> messages = messageAll.getMessageItems();
                        if (messageAll.getOffset() == 0) {
                            Pasgo.mMessageAll = new MessageAll();
                            //phần này dành cho lấy lịch sử
                            if (messages.size() > 0)
                            {
                                for (int i = messages.size() - 1; i >= 0; i--) {
                                    if (!checkDateAlter(messages.get(i).getCreatedAt()))
                                        messages.remove(messages.get(i));
                                }
                            }

                        }
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (messageAll.getOffset() == 0) {
                                        messagesAdapter.clear();
                                        messagesAdapter.notifyDataSetChanged();
                                    }
                                    if (messages.size() > 0)
                                        messagesAdapter.addToEnd(messages, false);
                                    if (messageAll.getOffset() == 0) {
                                        if (mIsGotoReversed && !TextUtils.isEmpty(mTextReserved)) {
                                            Message message = MessagesFixtures.getTextMessage(mTextReserved, true, "", new Date(), "", "");
                                            messagesAdapter.addToStart(message, true);
                                        }
                                    } else {
                                        for (Message item : Pasgo.mMessageAll.getMessageItems()) {
                                            if (item.getXemThemLichSu() == 1 ||  item.getXemThemLichSu() == -1) {
                                                item.setXemThemLichSu(-1);
                                                messagesAdapter.update(item);
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                        break;
                    case Constants.E_TYPING:
                        JSONObject objTyping = ParserUtils.stringToOvject(value);
                        String sender = ParserUtils.getStringValue(objTyping, "sender");
                        if (!mListTyping.contains(sender) && !sender.equals(Pasgo.getInstance().prefs.getRoomChat()))
                            mListTyping.add(sender);
                        setTypingView();
                        break;
                    case Constants.E_TYPING_REMOVE:
                        JSONObject objRemoveTyping = ParserUtils.stringToOvject(value);
                        String senderRemove = ParserUtils.getStringValue(objRemoveTyping, "sender");
                        mListTyping.remove(senderRemove);
                        setTypingView();
                        break;
                    case Constants.E_SUPPORT_ONLINE:
                        JSONObject objOnline = ParserUtils.stringToOvject(value);
                        mIsOverTime = ParserUtils.getBooleanValue(objOnline, "overtime");
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mIsOverTime && !Pasgo.getInstance().prefs.getChatUpdateInfo()) {
                                        mLnChatInfo.setVisibility(View.VISIBLE);
                                    } else {
                                        mLnChatInfo.setVisibility(View.GONE);
                                    }
                                }
                            });
                        break;
                }
            }
        }
    };
    private boolean checkDateAlter(Date dateafter) {
        boolean isCheck = false;
        String date = DatehepperUtil.getCurrentDate(DatehepperUtil.ddMMyyyy) + " 00:00:00";
        Date convertedDate = DatehepperUtil.convertStringToDate(date, DatehepperUtil.ddMMyyyyHHmmss);
        isCheck = dateafter.after(convertedDate);
        return isCheck;
    }
    private void initSocket() {
        messagesAdapter.clear();
        messagesAdapter.notifyDataSetChanged();
        if (Pasgo.socket != null && Pasgo.socket.connected()) {
            ArrayList<Message> messages = Pasgo.mMessageAll.getMessageItems();
            if (messages == null || messages.size() == 0)
                emitConversation(0);
            else {
                messagesAdapter.addToEnd(messages, false);
                if (Pasgo.getInstance().offset == 0)// vì đã có dữ liệu từ lần load trước
                    Pasgo.getInstance().mIsLoadMore = false;
                if (mIsGotoReversed && !TextUtils.isEmpty(mTextReserved)) {
                    Message message = MessagesFixtures.getTextMessage(mTextReserved, true, "", new Date(), "", "");
                    messagesAdapter.addToStart(message, true);
                }
            }

        }
    }
    private void setTypingView() {
        String str = "";
        for (String item : mListTyping) {
            if (!TextUtils.isEmpty(str))
                str += ", ";
            str += item;
        }
        if (!TextUtils.isEmpty(str))
            str += " Đang trả lời...";

        final String finalStr = str;
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvListTyping.setText(finalStr);
                }
            });
    }

    private void emitUpdateInfo() {
        try {
            String phone = mTvPhone.getText().toString().trim();
            String email = mTvEmail.getText().toString().trim();
            if (!StringUtils.isEmpty(mTvEmail.getText().toString().trim()) && !StringUtils.checkEmail(mTvEmail.getText().toString().trim())) {
                ToastUtils.showToast(getActivity(), R.string.email_kott);
                return;
            }
            JSONObject obj = new JSONObject();
            JSONObject objInfo = new JSONObject();
            objInfo.put("phone", phone);
            objInfo.put("email", email);

            obj.put("roomname", Pasgo.getInstance().prefs.getRoomChat());
            obj.put("roominfo", objInfo);
            Pasgo.getSocket().emit(Constants.E_CLIENT_UPDATE_INFO, obj, new Ack() {
                @Override
                public void call(Object... args) {
                    Utils.Log(Pasgo.TAG, "E_GET_USER_NAME");
                    final JSONObject obj = (JSONObject) args[0];
                    Utils.Log(Pasgo.TAG, obj.toString());
                    int errorCode = ParserUtils.getIntValue(obj, "errorcode");
                    String message = ParserUtils.getStringValue(obj, "message");
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (errorCode == 0) { //Cập nhật thông tin thành công
                                    Pasgo.getInstance().prefs.putChatUpdateInfo(true);
                                    mLnChatInfo.setVisibility(View.GONE);
                                } else {
                                    ToastUtils.showToast(getActivity(), message);
                                }
                            }
                        });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void emitConversation(int offset) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("roomname", Pasgo.getInstance().prefs.getRoomChat());
            obj.put("offset", offset);
            obj.put("pagesize", Constants.PAGE_SIZE_CHAT);
            Pasgo.getSocket().emit(Constants.E_ROOM_CONVERSATION, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateTyping() {
        if (Pasgo.socket == null || !Pasgo.socket.connected()) {
            Pasgo.socket = null;
            Pasgo.getInstance().initSocket();
        } else {
            if (!isTyping) {
                isTyping = true;
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sender", Pasgo.getInstance().prefs.getRoomChat());
                    obj.put("receiver", Pasgo.getInstance().prefs.getRoomChat());
                    Pasgo.getSocket().emit(Constants.E_TYPING, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mLastTimeTyping = (new Date()).getTime();
            removeTyping();
        }
    }

    private void removeTyping() {
        RemoveTypingTask removeTypingTask = new RemoveTypingTask();
        removeTypingTask.execute();
    }

    private class RemoveTypingTask extends AsyncTask<Void, Void, Void> {

        public RemoveTypingTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(Constants.TYPING_TIMER_LENGTH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handleUpdateUI.sendEmptyMessage(1);
        }
    }

    Handler handleUpdateUI = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    long typingTimer = (new Date()).getTime();
                    long timeDiff = typingTimer - mLastTimeTyping;
                    if (timeDiff >= Constants.TYPING_TIMER_LENGTH && isTyping) {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("sender", Pasgo.getInstance().prefs.getRoomChat());
                            obj.put("receiver", Pasgo.getInstance().prefs.getRoomChat());
                            Pasgo.getSocket().emit(Constants.E_TYPING_REMOVE, obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isTyping = false;
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void addTextChangedListener() {
        updateTyping();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        isTyping = false;
        removeTyping();
        if (mIsGotoReversed && !mIsGotoReversedSendDone) {
            sendMessage(mTextReserved, true);
            mIsGotoReversedSendDone = true;
        }
        sendMessage(input.toString(), false);

        return true;
    }

    private void sendMessage(String textInput, boolean isSendReversed) {
        long time = System.currentTimeMillis() - Pasgo.getInstance().prefs.getTimeUpdateOverMessage();
        int timeLastOverMessage = (int) (time / 1000) / 60;
        final boolean isOverMessage = mIsOverTime && timeLastOverMessage > 60;
        try {
            JSONObject obj = new JSONObject();
            obj.put("sender", Pasgo.getInstance().prefs.getRoomChat());
            obj.put("receiver", Pasgo.getInstance().prefs.getRoomChat());
            obj.put("message", textInput);
            obj.put("usertype", Constants.IS_KHACH);
            obj.put("time", DatehepperUtil.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss));
            obj.put("makhachhang", Pasgo.getInstance().maKichHoat);
            obj.put("url", mUrl);
            obj.put("isOverMessage", isOverMessage);
            obj.put("loaitinnhan", mLoaiTinNhan);
            obj.put("isSendReversed", isSendReversed);
            Utils.Log(Pasgo.TAG, obj.toString());
            Pasgo.getSocket().emit(Constants.E_MESSAGE, obj, new Ack() {
                @Override
                public void call(Object... args) {
                    //{"message":"Gửi tin nhắn thành công","errorcode":0}
                    if (isOverMessage)
                        Pasgo.getInstance().prefs.putTimeUpdateOverMessage(System.currentTimeMillis());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(true, Pasgo.getInstance().prefs.getRoomChat(), "", ""), true);
    }

    @Override
    public void onMessageLongClick(Message message) {
        //AppUtils.showToast(getActivity(), R.string.on_log_click_message, false);
    }

    private void initAdapter() {
        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextLayout(R.layout.item_custom_incoming_text_message)
                .setOutcomingTextLayout(R.layout.item_custom_outcoming_text_message)
                .setIncomingImageLayout(R.layout.item_custom_incoming_image_message)
                .setOutcomingImageLayout(R.layout.item_custom_outcoming_image_message);

        messagesAdapter = new MessagesListAdapter<>(senderId, holdersConfig, imageLoader);
        messagesAdapter.setOnMessageLongClickListener(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setOnMessageClickListener(message -> {
            Pasgo.getInstance().mIsLoadMore = true;
            onLoadMore(1, Pasgo.mMessageAll.getMessageItems().size());
        });
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastSocket);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Utils.Log(Pasgo.TAG, "Load more " + totalItemsCount);
        if (Pasgo.getInstance().mIsLoadMore) {
            if(totalItemsCount>1)
                totalItemsCount--;
            loadMessages(totalItemsCount);
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        menu.findItem(R.id.action_delete).setVisible(count > 0);
        menu.findItem(R.id.action_copy).setVisible(count > 0);
    }

    protected void loadMessages(int totalItemsCount) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("roomname", Pasgo.getInstance().prefs.getRoomChat());
            obj.put("offset", totalItemsCount);
            obj.put("pagesize", Constants.PAGE_SIZE_CHAT);
            Pasgo.getSocket().emit(Constants.E_ROOM_CONVERSATION, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }
}