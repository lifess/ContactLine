package com.ss.example.contactline;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ss.example.contactline.drawline.LinkDataBean;
import com.ss.example.contactline.drawline.SetDataView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.sdv)
    SetDataView sdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        sdv.getResult();
    }

    private void initData() {
        String data = "[  {    \"content\": \"apple\",    \"q_num\": 0,    \"type\": \"0\",    \"col\": 0,    \"row\": 0  },  {    \"content\": \"banana\",    \"q_num\": 1,    \"type\": \"0\",    \"col\": 0,    \"row\": 1  },  {    \"content\": \"pear\",    \"q_num\": 2,    \"type\": \"0\",    \"col\": 0,    \"row\": 2  },  {    \"content\": \"watermelon\",    \"q_num\": 3,    \"type\": \"0\",    \"col\": 0,    \"row\": 3  },  {    \"content\": \"pineapple\",    \"q_num\": 4,    \"type\": \"0\",    \"col\": 0,    \"row\": 4  },  {    \"content\": \"西瓜\",    \"q_num\": 3,    \"type\": \"0\",    \"col\": 1,    \"row\": 0  },  {    \"content\": \"梨\",    \"q_num\": 2,    \"type\": \"0\",    \"col\": 1,    \"row\": 1  },  {    \"content\": \"香蕉\",    \"q_num\": 1,    \"type\": \"0\",    \"col\": 1,    \"row\": 2  },  {    \"content\": \"苹果\",    \"q_num\": 0,    \"type\": \"0\",    \"col\": 1,    \"row\": 3  },  {    \"content\": \"菠萝\",    \"q_num\": 4,    \"type\": \"0\",    \"col\": 1,    \"row\": 4  }]";
        Type type = new TypeToken<ArrayList<LinkDataBean>>() {
        }.getType();
        List<LinkDataBean> list = null;
        try {
            list = new Gson().fromJson(data, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        sdv.setData(list);
    }
}
