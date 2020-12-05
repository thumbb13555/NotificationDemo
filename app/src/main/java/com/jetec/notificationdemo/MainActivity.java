package com.jetec.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    private String CHANNEL_ID = "Coder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        /**初始化介面控件與點擊事件*/
        Button btDefault,btCustom;
        btDefault = findViewById(R.id.button_DefaultNotification);
        btCustom = findViewById(R.id.button_CustomNotification);
        btDefault.setOnClickListener(onDefaultClick);
        btCustom.setOnClickListener(onCustomClick);

    }
    /**點選"系統預設通知"*/
    private View.OnClickListener onDefaultClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /**建置通知欄位的內容*/
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_accessible_forward_24)
                    .setContentTitle("哈囉你好！")
                    .setContentText("跟你打個招呼啊～")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);

            /**發出通知*/
            NotificationManagerCompat notificationManagerCompat
                    = NotificationManagerCompat.from(MainActivity.this);
            notificationManagerCompat.notify(1,builder.build());
        }
    };
    /**點選"客製化通知"*/
    private View.OnClickListener onCustomClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /**建立要嵌入在通知裡的介面*/
            RemoteViews view = new RemoteViews(getPackageName(),R.layout.custom_notification);

            /**初始化Intent，攔截點擊事件*/
            Intent intent = new Intent(MainActivity.this,NotificationReceiver.class);

            /**設置通知內"Hi"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Hi)*/
            intent.setAction("Hi");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this
                    ,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            /**設置通知內"Close"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Close)*/
            intent.setAction("Close");
            PendingIntent close = PendingIntent.getBroadcast(MainActivity.this
                    ,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            /**設置通知內的控件要做的事*/
            /*設置標題*/
            view.setTextViewText(R.id.textView_Title,"哈囉你好！");
            /*設置圖片*/
            view.setImageViewResource(
                    R.id.imageView_Icon,R.drawable.ic_baseline_directions_bike_24);

            /*設置"Hi"按鈕點擊事件(綁pendingIntent)*/
            view.setOnClickPendingIntent(R.id.button_Noti_Hi,pendingIntent);
            /*設置"Close"按鈕點擊事件(綁close)*/
            view.setOnClickPendingIntent(R.id.button_Noti_Close,close);

            /**建置通知欄位的內容*/
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                    .setContent(view)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
            NotificationManagerCompat notificationManagerCompat
                    = NotificationManagerCompat.from(MainActivity.this);
            /**發出通知*/
            notificationManagerCompat.notify(1,builder.build());
        }
    };

}