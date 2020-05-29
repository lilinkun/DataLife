package com.datalife.datalife.contract;

import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.RedEnvelopeEntity;
import com.datalife.datalife.dao.BrushManyBean;
import com.datalife.datalife.mvp.IView;

import java.util.List;

/**
 * Created by LG on 2018/10/10.
 */

public interface BrushSaveContract {

    public interface BrushView extends IView {
        public void success();
        public void fail(String msg,List<BrushManyBean> brushManyBeans);
        public void successMul();
        public void successCount(BrushUseCount brushUseCount);
        public void failCount(String msg);

        public void successRedEnvelope(RedEnvelopeEntity redEnvelopeEntity);
        public void failRedEnvelope(String msg);

        public void successOpenRedEnvelope();
        public void failOpenRedEnvelope(String msg,String stutas);
    }

}
