package com.brace.android.b31.spo2andhrv.hrv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.R;
import com.brace.android.b31.spo2andhrv.GlossaryExpandableListAdapter;
import com.brace.android.b31.spo2andhrv.bpoxy.enums.EnumGlossary;
import com.brace.android.b31.spo2andhrv.glossary.AGlossary;
import com.brace.android.b31.spo2andhrv.glossary.BeathBreathGlossary;
import com.brace.android.b31.spo2andhrv.glossary.BreathGlossary;
import com.brace.android.b31.spo2andhrv.glossary.HeartGlossary;
import com.brace.android.b31.spo2andhrv.glossary.LowOxgenGlossary;
import com.brace.android.b31.spo2andhrv.glossary.LowRemainGlossary;
import com.brace.android.b31.spo2andhrv.glossary.OsahsGlossary;
import com.brace.android.b31.spo2andhrv.glossary.OxgenGlossary;
import com.brace.android.b31.spo2andhrv.glossary.RateVariGlossary;
import com.brace.android.b31.spo2andhrv.glossary.SleepBreathBreakGlossary;
import com.brace.android.b31.spo2andhrv.glossary.SleepGlossary;


/**
 * 名词解释
 */
public class GlossaryDetailActivity extends Activity {

    ExpandableListView mExpandList;
    ImageView commentB30BackImg;
    TextView commentB30TitleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpgloassay_activity_detail);


        initViews();

        initAdapter();
    }

    private void initViews() {
        commentB30BackImg = findViewById(R.id.commentackImg);
        mExpandList = (ExpandableListView) findViewById(R.id.glossary_list);
        commentB30BackImg.setVisibility(View.VISIBLE);
        commentB30BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initAdapter() {
        int value = getIntent().getIntExtra("type", 0);
        AGlossary glossary = getGlossaryOsahs(getApplicationContext(), value);
        commentB30TitleTv.setText(glossary.getHead());
        GlossaryExpandableListAdapter adapter
                = new GlossaryExpandableListAdapter(getApplicationContext(), glossary);
        mExpandList.setGroupIndicator(null);// 将控件默认的左边箭头去掉，
        mExpandList.setAdapter(adapter);
        expandList();
    }

    private void expandList() {
        int groupCount = mExpandList.getCount();
        for (int i = 0; i < groupCount; i++) {
            mExpandList.expandGroup(i);
        }
        mExpandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    private AGlossary getGlossaryOsahs(Context context, int value) {
        EnumGlossary enumGlossary = EnumGlossary.getEnum(value);
        switch (enumGlossary) {
            case OSHAHS:
                return new OsahsGlossary(context);
            case BREATHBREAK:
                return new BeathBreathGlossary(context);
            case LOWOXGEN:
                return new LowOxgenGlossary(context);
            case HEART:
                return new HeartGlossary(context);
            case RATEVARABLE:
                return new RateVariGlossary(context);
            case SLEEP:
                return new SleepGlossary(context);
            case LOWREAMIN:
                return new LowRemainGlossary(context);
            case SLEEPBREATHBREAKTIP:
                return new SleepBreathBreakGlossary(context);
            case BREATH:
                return new BreathGlossary(context);
            case OXGEN:
                return new OxgenGlossary(context);
        }
        return new OsahsGlossary(context);
    }

}
