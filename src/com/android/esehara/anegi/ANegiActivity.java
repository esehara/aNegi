package com.android.esehara.anegi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.Runnable;
import java.util.Random;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.view.View;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.provider.MediaStore.Images;

public class ANegiActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView negi_darui;
	private ScrollView negi_gero;
	private int number_negi;
	private Context con;
	private Bitmap negi_image;
	private Dialog yourDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Random rnd = new Random();
        number_negi = rnd.nextInt(1190) + 1;
        //------------------------ make Dialog
        yourDialog = new Dialog(this);
        LayoutInflater inflater =  (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.your_dialog, (ViewGroup)findViewById(R.id.your_dialog_root_element));
        yourDialog.setContentView(layout);
        yourDialog.setTitle("飛べ");
        //------------------------
        Button yourDialogButton = (Button)layout.findViewById(R.id.your_dialog_button);
        SeekBar yourDialogSeekBar = (SeekBar)layout.findViewById(R.id.your_dialog_seekbar);
        yourDialogSeekBar.setMax(1190);
        yourDialogSeekBar.setProgress(number_negi);

        yourDialogSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
            	number_negi = progress;
            }	
        });

        yourDialogButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		try{
        		yourDialog.dismiss();
                loadimage("http://negineesan.fc2web.com/negi" + String.valueOf(number_negi) + ".jpg");
        		}catch(Exception e){
        			AlertDialog.Builder ad = new AlertDialog.Builder(con);
        			ad.setTitle("Error");
        			ad.setMessage(e.getClass().getName() + ':' + e.getMessage());
        			ad.create();
        			ad.show();
        		}
        	}
        });
        
        con = this;
        negi_darui = (ImageView) findViewById(R.id.image_view);
        negi_gero = (ScrollView) findViewById(R.id.scroll_view);
        loadimage("http://negineesan.fc2web.com/negi" + String.valueOf(number_negi) + ".jpg");
    }

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		super.onKeyDown(keyCode, event);
		boolean ret = false;
		switch(keyCode){
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (number_negi < 1191){
				number_negi ++;
				ret = true;				
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (number_negi > 1){
				number_negi --;
				ret = true;
			}
			break;
		}
				
		if (ret){
	        loadimage("http://negineesan.fc2web.com/negi" + String.valueOf(number_negi) + ".jpg");			
		}
			
		return ret;
	}

	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0,0,0,"飛ぶ");
		menu.add(0,1,1,"保存");
		menu.add(0,2,2,"ランダム");
		menu.add(1,3,3,"フェースブック");
		menu.add(1,4,4,"る");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		int iid=item.getItemId();
		switch(iid){
		case 0:
			yourDialog.show();
			break;
		
		case 1:
			String status = Environment.getExternalStorageState();
			File fout;
			if(!status.equals(Environment.MEDIA_MOUNTED)){
    			AlertDialog.Builder ad = new AlertDialog.Builder(con);
    			ad.setTitle("Error");
    			ad.setMessage("SDカードがありません。");
    			ad.create();
    			ad.show();    	
				break;
			}else{
				fout = new File("/sdcard/aNegi/");
				if(!fout.exists()){
					fout.mkdirs();
				}
			}
			String fname = fout.getAbsolutePath() + "/negi" + String.valueOf(number_negi) + ".png";
			try{
				FileOutputStream out = new FileOutputStream(fname);
				negi_image.compress(CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				ContentResolver contentresolver = getContentResolver();
				ContentValues contentvalues = new ContentValues();
				contentvalues.put(Images.Media.MIME_TYPE, "image/png");
				contentvalues.put(Images.Media.DATA,fname);
				contentvalues.put(Images.Media.TITLE, "ねぎ姉さん" + String.valueOf(number_negi) + "話");
				contentvalues.put(Images.Media.DISPLAY_NAME, "ねぎ姉さん" + String.valueOf(number_negi) + "話");
				contentresolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentvalues);
				
			}catch(Exception e){
				AlertDialog.Builder ad = new AlertDialog.Builder(con);
				ad.setTitle("Error");
				ad.setMessage(e.getClass().getName() + ':' + e.getMessage());
				ad.create();
				ad.show();
				break;				
			}
			break;
		
		case 2:
			Random rnd = new Random();
			number_negi = rnd.nextInt(1190) + 1;
	        loadimage("http://negineesan.fc2web.com/negi" + String.valueOf(number_negi) + ".jpg");						
			break;
			
		case 3:
			break;
		case 4:
			
			break;
		default:
			break;
		}
	
		return true;
	}
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	private String get_url;
	private ProgressDialog negi_d;
	public void loadimage(String str){
			get_url = str;
			negi_d = new ProgressDialog(con);
			negi_d.setMessage("画像を読み込み中です");
			negi_d.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			negi_d.setCancelable(false);
			negi_d.show();
			(new Thread(get_picture)).start();
	}

	private Runnable get_picture = new Runnable(){
		@Override
		public void run(){
			try{
				URL url= new URL(get_url);
				HttpURLConnection httpconnect = (HttpURLConnection) url.openConnection();
				httpconnect.setDoInput(true);
				httpconnect.connect();
				InputStream input = new BufferedInputStream(httpconnect.getInputStream(),IO_BUFFER_SIZE);
				ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
				BufferedOutputStream output = new BufferedOutputStream(dataStream,IO_BUFFER_SIZE);
				byte[] b = new byte[IO_BUFFER_SIZE];
				int read;
				while ((read = input.read(b)) != -1){
					output.write(b,0,read);
				}
				output.flush();
				byte[] data = dataStream.toByteArray();
				negi_image = BitmapFactory.decodeByteArray(data,0,data.length);
				negi_d.dismiss();
				if (negi_image == null){
    			AlertDialog.Builder ad = new AlertDialog.Builder(con);
    			ad.setTitle("Error");
    			ad.setMessage("画像が取得できませんでした。");
    			ad.create();
    			ad.show();    			    			
				}
		}catch(Exception e){
			negi_d.dismiss();
			AlertDialog.Builder ad = new AlertDialog.Builder(con);
			ad.setTitle("Error");
			ad.setMessage(e.getClass().getName() + ':' + e.getMessage());
			ad.create();
			ad.show();
			negi_image = null;
		}
		handler.sendEmptyMessage(0);
		return;
		}
	};
	
	private final Handler handler = new Handler(){
		public void handleMessage(Message msg){
			negi_darui.setImageBitmap(negi_image);
			negi_gero.fullScroll(ScrollView.FOCUS_UP);
		}
	};
	
}

