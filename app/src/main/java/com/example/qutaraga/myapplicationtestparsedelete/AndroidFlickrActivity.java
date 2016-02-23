package com.example.qutaraga.myapplicationtestparsedelete;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

import com.example.qutaraga.myapplicationtestparsedelete.Flickr.Flickr;
import com.example.qutaraga.myapplicationtestparsedelete.Utils.DataAdapter;
import com.example.qutaraga.myapplicationtestparsedelete.Utils.EndlessRecyclerOnScrollListener;
import com.example.qutaraga.myapplicationtestparsedelete.Utils.PhotoURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Осталось исправить:
 * 1)Убрать compile 'com.squareup.picasso:picasso:2.5.2' (заменить)
 * 2)в партере по 2 картинке в строчке , в ландшафтте по 4
 * 3)внизу появляется кнопка загрузить еще если у нас нет скрола
 * 4)сделать возможность кеширования картинок в оперативной память , и на диске
 * 5)сделать возможность паррралельной загрузке нескольких изображений
 * настройка количество должно управляться параметром
 * 6)при перевороте экрана не прерывать загрузку
 * 7)сделать оповещения о состоянии загрузки через BoarcastReciver
 * лучше сделать чтобы сообщения приходили в BoarcastReciver а из него происходил all back тех кто подписался callBack
 * 8)когда будешь делать загрузку картинок и кеширование + не загружать заново при перевороте - поймешь
 * + для того чтобы ты немного разобрался с этим компонентом
 *
 *
 * Исправленно:
 * 1)Убраны deprecated библиотеки из Flickr, запрос был заменен на HttpURLConnection
 * 2)В классе Flickr все ссылочные поля класса были заменены на static final.
 * 3)Dialog реализован в отдельном классе
 * 4)"нужно использовать ndroid.support.v7.app.AppCompatActivity" - +
 * 5)а почему тогда я вижу этот код: for(int i =0;i<adapter.getPhotoURLMyList().listSize();i++) - исправленно
 * 6)сделать по нормальному работу с данными о списке фоток, а то у нас в Activity  один список MyList<String> photo = new MyList<>();
 * в адаптер мы передаем другой, это все плохо ----> убраны дополнительные MyList, убран лишний метод prepareDate,
 * остался единственный лист MyList<PhotoURL> photoURLMyList;
 * 7)хоть бы по пакетам разбил как-то а то все в одной куче -----> исправленно.
 **/

public class AndroidFlickrActivity extends AppCompatActivity {
	RecyclerView recyclerView;

	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

	EditText searchText;
    Button searchButton;
    ImageView  showImage;
	Flickr flickr = new Flickr();
	DataAdapter adapter;
	Dialog mydialog;

	MyList<PhotoURL> photoURLMyList;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		mydialog = new Dialog(AndroidFlickrActivity.this);

		InView();
    }

	EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {

		@Override
		public void onLoadMore(int current_page) {
			if(adapter.getPhotoURLMyList().listSize()>0) {
				flickr.setCountPage(current_page);
				showLoadedImages();
			}
		}
	};

	String searchQ;
    private Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener(){
		public void onClick(View arg0) {
			adapter = new DataAdapter(getApplicationContext(),new MyList<PhotoURL>());
			recyclerView.setAdapter(adapter);

			//конвектор для поискового запроса
			try {
				searchQ = URLEncoder.encode(searchText.getText().toString(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			adapter.getPhotoURLMyList().clear();

			flickr.setCountPage(1);
			showLoadedImages();
		}};

	private void showLoadedImages()
	{
		photoURLMyList = new MyList<>();
		mydialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				String searchResult = flickr.QueryFlickr(searchQ);
				ParseJSON(searchResult);

				for(int  i=0;i<photoURLMyList.listSize();i++)
					adapter.getPhotoURLMyList().add(photoURLMyList.get(i));

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						endlessRecyclerOnScrollListener.setLoading(false);
						mydialog.dismiss();
					}
				});
			}
		}).start();
	}
    private void ParseJSON(String json){

		photoURLMyList = new MyList<>();

    	String flickrId,flickrSecret,flickrServer,flickrFarm;
		//image size - z medium 640, 640 on longest side
		String size = "z";

    	try {
			JSONObject JsonObject = new JSONObject(json);
			JSONObject Json_photos = JsonObject.getJSONObject("photos");
			JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");

			for (int i = 0; i < JsonArray_photo.length(); i++){
				JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(i);
				flickrId = FlickrPhoto.getString("id");
				flickrSecret = FlickrPhoto.getString("secret");
				flickrServer = FlickrPhoto.getString("server");
				flickrFarm = FlickrPhoto.getString("farm");

				String site = "http://farm"+flickrFarm +".staticflickr.com/"+flickrServer+"/"+flickrId+"_"+flickrSecret+"_"+size+".jpg";

				PhotoURL photoURL = new PhotoURL();

				photoURL.setPhotoURL(site);
				photoURLMyList.add(photoURL);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void InView(){
		searchText = (EditText)findViewById(R.id.searchtext);
		//текстовые элементы по центру
		searchText.setGravity(Gravity.CENTER_HORIZONTAL);
		//запрет на перенос строки
		searchText.setMaxLines(1);

		searchButton = (Button)findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(searchButtonOnClickListener);

		showImage = (ImageView) findViewById(R.id.img_android);


		recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
	}
/*
реализовать диалог для проверки интернет соеденения, при запуске программы выводить диалог только в том случае,
если интернет соединение отсутствует.

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	*/
}

