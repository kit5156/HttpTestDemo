package com.example.android.httptestdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 材料网站
 * http://www.apihome.cn/view-detail-70212.html
 */
public class MainActivity extends AppCompatActivity {

    RequestQueue mQueue;
    StringRequest stringRequest, stringRequest_post;
    JsonObjectRequest jsonObjectRequest;
    ImageRequest imageRequest;
    ImageLoader imageLoader;

    private TextView tv;
    private ImageView img;
    private NetworkImageView img_net;
    private String url = "http://api.avatardata.cn/MingRenMingYan/LookUp";
    private String url_img = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
    private List<Modle> data = new ArrayList<Modle>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.text);
        img = (ImageView)findViewById(R.id.img);
        img_net = (NetworkImageView)findViewById(R.id.img_net);


        //发起一条HTTP请求，然后接收HTTP响应。首先需要获取到一个RequestQueue对象，
        //RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
        // RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，
        // 这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了
        mQueue = Volley.newRequestQueue(this);

    }


    public void getimg(View view){
        //第一种方法
//        getImg();
//        mQueue.add(imageRequest);

        //第二种方法
//        imageLoader();

        //第三种方法
        net_Img();
    }

    public void get(View view){
//        stringRequest();
//        mQueue.add(stringRequest);

        //最后，将这个StringRequest对象添加到RequestQueue里面就可以了
        jsonObjectRequest();
        mQueue.add(jsonObjectRequest);

    }


    public void post(View view){
        stringRequest_post();
        mQueue.add(stringRequest_post);
    }


    private void stringRequest(){
        //接下来为了要发出一条HTTP请求，我们还需要创建一个StringRequest对象，
        stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("-------------", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("-------------", error.getMessage(), error);
            }
        });
        //这里new出了一个StringRequest对象，StringRequest的构造函数需要传入三个参数，第一个参数就是目标服务器的URL地址，
        // 第二个参数是服务器响应成功的回调，第三个参数是服务器响应失败的回调。其中，目标服务器地址我们填写的是百度的首页，
        // 然后在响应成功的回调里打印出服务器返回的内容，在响应失败的回调里打印出失败的详细信息。

    }


    private void stringRequest_post(){
        stringRequest_post = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("-------------", response);

                        tv.setText(response);
                        ZJResponse zjResponse = JsonUtil.fromJson(response, ZJResponse.class);
//
//
                        Log.e("-------------", zjResponse.getTotal() + "");
                        Log.e("-------------", zjResponse.getResult().toString());
//
                        data = JsonUtil.fromJson(zjResponse.getResult().toString(),
                                new TypeToken<List<Modle>>() {}.getType());

                        Log.e("-------------", data.size() + "");
                        Log.e("-------------", data.toString());
                        Log.e("-------------", data.get(0).getFamous_name());
                        Log.e("-------------", data.get(0).getFamous_saying());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("-------------", error.getMessage(), error);
            }
        }) {
            /*
    key	String	是	应用APPKEY
 	keyword	String	是	查找关键字
 	page	Int	否	请求页数，默认page=1
 	rows	Int	否	返回记录条数，默认rows=20,最大50
 	dtype	String	否	返回结果格式：可选JSON/XML，默认为JSON
 	format	Boolean	否	当返回结果格式为JSON时，是否对其进行格式化，为了节省流量默认为false，测试时您可以传入true来熟悉返回内容*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "9449b098129b447f93b713ab67ac3f93");
                map.put("keyword", "名人");
                map.put("rows", 10 + "");
                return map;
            }
        };
    }



    private void jsonObjectRequest(){
        jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("---------", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("---------", error.getMessage(), error);
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key", "9449b098129b447f93b713ab67ac3f93");
                    map.put("keyword", "名人");
                    map.put("rows", 10 + "");
                    return map;
                }
        };
    }


    //ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小,RGB_565则占据2个字节
    private void getImg(){
        imageRequest = new ImageRequest(url_img,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                img.setImageResource(R.mipmap.ic_launcher);
            }
        });
    }


    /*使用ImageRequest和ImageLoader这两种方式来加载网络图片，都可以传入一个最大宽度和高度的参数来对图片进行压缩，
    而NetworkImageView中则完全没有提供设置最大宽度和高度的方法，那么是不是使用NetworkImageView来加载的图片都不会进行压缩呢？
    其实并不是这样的，NetworkImageView并不需要提供任何设置最大宽高的方法也能够对加载的图片进行压缩。
    这是由于NetworkImageView是一个控件，在加载图片的时候它会自动获取自身的宽高，然后对比网络图片的宽度，再决定是否需要对图片进行压缩。
    也就是说，压缩过程是在内部完全自动化的，并不需要我们关心，NetworkImageView会始终呈现给我们一张大小刚刚好的网络图片，
    不会多占用任何一点内存，这也是NetworkImageView最简单好用的一点吧。
    当然了，如果你不想对图片进行压缩的话，其实也很简单，
    只需要在布局文件中把NetworkImageView的layout_width和layout_height都设置成wrap_content就可以了，
    这样NetworkImageView就会将该图片的原始大小展示出来，不会进行任何压缩*/
    private void net_Img(){
        imageLoader = new ImageLoader(mQueue, new BitmapCache());
        img_net.setDefaultImageResId(R.drawable.a);
        img_net.setErrorImageResId(R.mipmap.ic_launcher);
        img_net.setImageUrl(url_img, imageLoader);
    }


    //ImageLoader也可以用于加载网络上的图片，并且它的内部也是使用ImageRequest来实现的，
    //不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，
    //还可以过滤掉重复的链接，避免重复发送请求
    //1. 创建一个RequestQueue对象。
    //2. 创建一个ImageLoader对象。
    //3. 获取一个ImageListener对象。
    //4. 调用ImageLoader的get()方法加载网络上的图片。
    private void imageLoader(){
    //1.创建的ImageCache对象是一个空的实现，完全没能起到图片缓存的作用
//        imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//            }
//
//            @Override
//            public Bitmap getBitmap(String url) {
//                return null;
//            }
//        });

        //2.使用下面ImageCache接口来实现图片缓存，避免内存溢出
        imageLoader = new ImageLoader(mQueue, new BitmapCache());


        //我们通过调用ImageLoader的getImageListener()方法能够获取到一个ImageListener对象
        //第一个参数指定用于显示图片的ImageView控件，
        // 第二个参数指定加载图片的过程中显示的图片，
        // 第三个参数指定加载图片失败的情况下显示的图片。
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(img,
                R.drawable.a, R.mipmap.ic_launcher);

        //1.最后，调用ImageLoader的get()方法来加载图片
        imageLoader.get(url_img, listener);

        //2.当然，如果你想对图片的大小进行限制，也可以使用get()方法的重载，指定图片允许的最大宽度和高度，
//        imageLoader.get(url_img, listener, 200, 200);
    }



    /**
     * 上面创建的ImageCache对象是一个空的实现，完全没能起到图片缓存的作用
     * 新建一个BitmapCache并实现了ImageCache接口
     * 这里我们将缓存图片的大小设置为10M。接着修改创建ImageLoader实例的代码，第二个参数传入BitmapCache的实例
     */
    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }


}


