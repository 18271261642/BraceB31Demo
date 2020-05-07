package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brace.android.b31.R;
import com.brace.android.b31.view.widget.CustomCircleProgressBar;

/**
 * 手动测量血压
 * Created by Admin
 * Date 2019/12/23
 */
public class ManmualBloodActivity extends BaseActivity implements View.OnClickListener {

    //title
    private TextView titleTv;
    private ImageView backImg;
    //通用模式和私人模式切换
    private RelativeLayout publicRel,privateRel;
    private View pubLin,priLin;

    //无测量时显示的图片
    private ImageView b31MeaurePlaceHolderImg;

    //进度条
    private CustomCircleProgressBar b31MeaureBloadProgressView;
    //结果显示
    private TextView showBpStateTv;
    //开始或暂停按钮
    private ImageView b31MeaureStartImg;

    //是否是私人模式
    private boolean isPrivate = false;
    //是否是正在测量状态
    private boolean isManual = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manmual_blood_layout);


        initViews();

    }

    private void initViews() {
        titleTv = findViewById(R.id.commentTitleTv);
        backImg = findViewById(R.id.commentackImg);
        backImg.setVisibility(View.VISIBLE);
        publicRel = findViewById(R.id.manual_blood_public);
        privateRel = findViewById(R.id.manual_blood_private);
        pubLin = findViewById(R.id.manual_blood_public_line);
        priLin = findViewById(R.id.manual_blood_private_line);

        b31MeaurePlaceHolderImg = findViewById(R.id.b31MeaurePlaceHolderImg);
        b31MeaureBloadProgressView = findViewById(R.id.b31MeaureBloadProgressView);
        showBpStateTv = findViewById(R.id.showBpStateTv);
        b31MeaureStartImg = findViewById(R.id.b31MeaureStartImg);
        titleTv.setText("血压测量");
        publicRel.setOnClickListener(this);
        privateRel.setOnClickListener(this);
        backImg.setOnClickListener(this);
        b31MeaureStartImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
            if(id == R.id.commentackImg){   //返回
                finish();
            }else if(id == R.id.manual_blood_public){   //通用模式
                isPrivate = false;


            }else if(id == R.id.manual_blood_private){  //私人模式
                isPrivate = true;


            }else if(id == R.id.b31MeaureStartImg){ //开始或停止测量
                if(!isManual){  //开始测量
                    isManual = true;
                    b31MeaureStartImg.setImageResource(R.drawable.detect_bp_pause);
                    b31MeaureBloadProgressView.setTmpTxt(null);
                    b31MeaureBloadProgressView.setScheduleDuring(27 * 1000);
                    b31MeaureBloadProgressView.setProgress(100);


                }else{

                }
            }
        }

}
