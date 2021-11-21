package org.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TalkAdapter extends BaseAdapter  {
    int check;
    LayoutInflater inflater;
    ArrayList<TalkItem> talkItems;







    public TalkAdapter(LayoutInflater inflater, ArrayList<TalkItem> talkItems,Context context) {
        this.inflater = inflater;
        this.talkItems = talkItems;

    }

    @Override
    public int getCount() {
        return talkItems.size();
    }

    @Override
    public Object getItem(int position) {
        return talkItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view==null){
            view= inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView tvName= view.findViewById(R.id.tv_name);
        TextView tvDate= view.findViewById(R.id.tv_date);
        TextView tvMsg= view.findViewById(R.id.tv_msg);
        ImageView iv= view.findViewById(R.id.iv);
        Button button=view.findViewById(R.id.button4);
        TextView nickname=view.findViewById(R.id.textView11);

        TalkItem talkItem= talkItems.get(position);
        tvName.setText(talkItem.getName());
        tvDate.setText(talkItem.getDate());
        tvMsg.setText(talkItem.getMsg());
        nickname.setText(talkItem.getNickname());
        String img= talkItem.getImgPath();















        //네트워크에 있는 이미지 읽어오기.
        Glide.with(view).load(talkItem.getImgPath()).into(iv);


        String serverUrl="http://lsvk9921.cafe24.com/delete.php";
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),imageclick.class);
                intent.putExtra("path",img);
                view.getContext().startActivity(intent);
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(),"클릭",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(view.getContext(),imageDownload.class);
                intent.putExtra("path",img);
                view.getContext().startActivity(intent);
                return false;
            }
        });
        //게시물 삭제버튼
        button.setOnClickListener(new View.OnClickListener() {
            String nickname=talkItem.getNickname();
            //????왜되는거지
            String nickname2=((TalkActivity)TalkActivity.mContext).a;
            @Override
            public void onClick(View view) {
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        new AlertDialog.Builder(view.getContext()).setMessage("업로드 성공!").create().show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
                if(nickname.equals(nickname2)) {
                    smpr.addStringParam("no", Integer.toString(talkItem.no));

                    //new AlertDialog.Builder(view.getContext()).setMessage(talkItem.getNo()).create().show();
                    //요청객체를 서버로 보낼 우체통 같은 객체 생성
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    requestQueue.add(smpr);
                    Toast.makeText(view.getContext(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(view.getContext(),"글쓴이가 아닙니다.",Toast.LENGTH_SHORT).show();


                }

            }
        });


        return view;
    }
}
