package com.example.system.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.system.R;
import com.example.system.databinding.FragmentHomeBinding;
import com.example.system.socketnetwork.SocketTransceiver;
import com.example.system.socketnetwork.TcpClient;

import java.lang.ref.WeakReference;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Button mBtnSw;
    private EditText mEditIp;
    private EditText mEditPort;
    private ImageView mIvClient;
    private static final int HOME_TCP_CONNECT = 0x0011;
    private static final int HOME_TCP_CONNECT_FAILED = 0x0012;
    private static final int HOME_TCP_RECEIVE = 0x0013;
    private static final int HOME_TCP_DISCONNECT = 0x0014;
    private UIUpdateHandler<HomeFragment> uiUpdateHandler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initView();
        return root;
    }

    private void initView() {
        mBtnSw = binding.Switch;
        mEditIp = binding.Ip;
        mEditPort = binding.port;
        mIvClient = binding.imageView;
        mBtnSw.setOnClickListener(this);
        uiUpdateHandler = new UIUpdateHandler<HomeFragment>(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * TCP连接状态监听
     */
    private TcpClient tcpClient = new TcpClient() {
        @Override
        public void onConnect(SocketTransceiver transceiver) {
            //连接成功
            uiUpdateHandler.sendEmptyMessage(HOME_TCP_CONNECT);
        }

        @Override
        public void onConnectFailed() {
            //连接失败
            uiUpdateHandler.sendEmptyMessage(HOME_TCP_CONNECT_FAILED);
        }

        @Override
        public void onReceive(SocketTransceiver transceiver, String s) {
            //接收数据
            Message msg = uiUpdateHandler.obtainMessage(HOME_TCP_RECEIVE,s);
            uiUpdateHandler.sendMessage(msg);
        }

        @Override
        public void onDisconnect(SocketTransceiver transceiver) {
            //连接断开
            uiUpdateHandler.sendEmptyMessage(HOME_TCP_DISCONNECT);
        }
    };

    public class UIUpdateHandler<T> extends Handler {
        private WeakReference<T> innerObject;

        public UIUpdateHandler(T object) {
            this.innerObject = new WeakReference<T>(object);
        }

        public T getInnerObject() {
            return innerObject == null ? null : innerObject.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (getInnerObject() == null) {
                return;
            }
            switch (msg.what){
                case HOME_TCP_CONNECT:
                    Toast.makeText(getContext(),"连接成功！",Toast.LENGTH_LONG).show();
                    mIvClient.setImageResource(R.drawable.green_circle);
                    break;
                case HOME_TCP_CONNECT_FAILED:
                    Toast.makeText(getContext(),"连接失败！",Toast.LENGTH_LONG).show();
                    mIvClient.setImageResource(R.drawable.gray_circle);
                    break;
                case HOME_TCP_DISCONNECT:
                    Toast.makeText(getContext(),"连接断开！",Toast.LENGTH_LONG).show();
                    mIvClient.setImageResource(R.drawable.gray_circle);
                    break;
                case HOME_TCP_RECEIVE:
                    Toast.makeText(getContext(),"接收数据！",Toast.LENGTH_LONG).show();
                    mIvClient.setImageResource(R.drawable.gray_circle);
                    break;
                default:
                    mIvClient.setImageResource(R.drawable.gray_circle);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Switch:
                getEditContentAndConnect();
                break;
            default:
                break;
        }

    }

    private void getEditContentAndConnect(){
        if(mEditPort!=null && mEditIp!=null){
            String ip = mEditIp.getText().toString().trim();
            String port = mEditPort.getText().toString().trim();
            tcpClient.connect(ip,Integer.valueOf(port));
        }
    }
}