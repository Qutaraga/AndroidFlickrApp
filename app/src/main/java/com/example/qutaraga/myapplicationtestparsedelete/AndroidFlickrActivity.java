package com.example.qutaraga.myapplicationtestparsedelete;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 1)Вероятно стоит добавить отображение title у изображений(PhotoURL - новая переменная title)
 * 2)Сделать нормальный вид приложению. Приятный для пользователя GUI.
 * 3)Так же стоит на превью сделать уменьшенные изображения, а при нажатии они принимали свой нормальный вид.
 * 4)Реализовать функцию сохранения изображения на sdcard
 *
 * Немного об архетектуре:
 * 1)Flickr - класс для взаиможействия программы с сайтом. Реализован как отдельный класс и при необходимости изменения
 * вида поисковика, нам не придется разбираться в остальной части программы.
 *
 * 2)PhotoURL - удобный вспомогательный класс. Был реализован для хранения url ссылки и дальнейшей ее вставки в imageView.
 * При желании, если мы захотим возвращать какой - либо другой элемент изображения с сервера, то для этой задачи на просто нужно будет добать новую переменную и
 * реализовать для нее set  и get.
 *
 * 3)DataAdapter - нужен для правильной организации элементов на экране.
 * Для вставки используется row_layout, для отображения библиотека picasso.
 *
 *
 * */


public class AndroidFlickrActivity extends Activity {
	RecyclerView recyclerView;
	MyList<String> photo = new MyList<>();
	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

	EditText searchText;
    Button searchButton;
    ImageView  showImage;
	ProgressDialog dialog;
	Flickr flickr = new Flickr();
	DataAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		dialog = new ProgressDialog(AndroidFlickrActivity.this)
		{
			@Override
			public void onBackPressed() {}
		};
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Loading pictures...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);


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

	EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {

		@Override
		public void onLoadMore(int current_page) {
			if(adapter.getPhotoURLMyList().listSize()>0) {
				flickr.setCountPage(current_page);
				showLoadedImages();
			}
		}
	};

	private MyList<PhotoURL> prepareData(){

		MyList<PhotoURL> photoURLMyList = new MyList<>();

		for(int i=0;i<photo.listSize();i++){

			PhotoURL photoURL = new PhotoURL();
			photoURL.setPhotoURL(photo.get(i));
			photoURLMyList.add(photoURL);

		}
		return photoURLMyList;
	}

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

			for(int i =0;i<adapter.getPhotoURLMyList().listSize();i++)
				adapter.getPhotoURLMyList().remove(0);

			flickr.setCountPage(1);
			showLoadedImages();
		}};

	private void showLoadedImages()
	{
		photo = null;
		photo = new MyList<>();
		dialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {

				String searchResult = flickr.QueryFlickr(searchQ);

				ParseJSON(searchResult);

				MyList<PhotoURL> list = prepareData();
				for(int  i=0;i<list.listSize();i++)
					adapter.getPhotoURLMyList().add(list.get(i));

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						endlessRecyclerOnScrollListener.setLoading(false);
						dialog.dismiss();
					}
				});
			}
		}).start();
	}
    private void ParseJSON(String json){

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
				photo.add(site);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

