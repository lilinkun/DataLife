package com.datalife.datalife.fragment;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import com.datalife.datalife.BR;
import com.datalife.datalife.R;
import com.datalife.datalife.adapter.DataBindingAdapter;
import com.datalife.datalife.widget.CustomRecyclerView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.whealthService.OnBLEService;

/**
 * Created by LG on 2018/1/18.
 */

public class BleDeviceListDialogFragment extends BaseDialogFragment
        implements CustomRecyclerView.RecyclerItemClickListener {

    private DataBindingAdapter<OnBLEService.DeviceSort> adapter;

    public BleDeviceListDialogFragment() {
        super();
    }

    @Override
    protected boolean isDataBinding() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_ble_dev_list;
    }

    @Override
    protected void onInit() {
        setDialogTitle("设备扫描列表");
        adapter = new DataBindingAdapter<>(getContext(), R.layout.item_ble_dev);
        mBinding.setVariable(BR.recyclerAdapter, adapter);
        mBinding.setVariable(BR.listCount, adapter.getListSize());
        mBinding.setVariable(BR.itemClickListener, this);
        MonitorDataTransmissionManager.getInstance().autoScan(true);
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MonitorDataTransmissionManager.getInstance().autoScan(false);
    }

    @Override
    protected int getNegativeText() {
        return 0;
    }

    @Override
    protected int getNeutralText() {
        return 0;
    }

    @Override
    protected int getPositiveText() {
        return R.string.close;
    }

    @Override
    public void onItemClick(View view, int position) {
        OnBLEService.DeviceSort item = adapter.getItem(position);
        BluetoothDevice bleDevice = item.bleDevice;
        dismiss();
        MonitorDataTransmissionManager.getInstance().connectToBle(bleDevice);
    }

    public void refresh() {
        adapter.setItems(MonitorDataTransmissionManager.getInstance().getDeviceList());
    }
}
