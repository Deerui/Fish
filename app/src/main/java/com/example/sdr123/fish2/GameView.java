package com.example.sdr123.fish2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sdr123 on 2016/11/18.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable,View.OnTouchListener{

    private Bitmap myfish1;//自己
    private Bitmap myfish2;
    private Bitmap myfish3;
    private Bitmap myfish4;
    private Bitmap myfish5;
    private Bitmap myfish6;
    private Bitmap myfish7;
    private Bitmap myfish8;
    private Bitmap enemyfish;//敌人
    private Bitmap enemy1;
    private Bitmap enemy2;
    private Bitmap enemy3;
    private Bitmap enemy4;
    private Bitmap bg;//背景
    private Bitmap erjihuancun;//二级缓存
    private int display_w;//屏幕宽和高
    private int display_h;
    private ArrayList<GameImage> gameImages=new ArrayList();
    private SurfaceHolder holder;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);//事件注册


    }
    private void init(){
        //加载照片
        myfish1= BitmapFactory.decodeResource(getResources(),R.drawable.myfish1);
        myfish2= BitmapFactory.decodeResource(getResources(),R.drawable.myfish2);
        myfish3= BitmapFactory.decodeResource(getResources(),R.drawable.myfish3);
        myfish4= BitmapFactory.decodeResource(getResources(),R.drawable.myfish4);
        myfish4= BitmapFactory.decodeResource(getResources(),R.drawable.myfish5);
        myfish4= BitmapFactory.decodeResource(getResources(),R.drawable.myfish6);
        myfish4= BitmapFactory.decodeResource(getResources(),R.drawable.myfish7);
        myfish4= BitmapFactory.decodeResource(getResources(),R.drawable.myfish8);
        enemyfish= BitmapFactory.decodeResource(getResources(),R.drawable.enemyfish);
        enemy1= BitmapFactory.decodeResource(getResources(),R.drawable.enemy1);
        enemy2= BitmapFactory.decodeResource(getResources(),R.drawable.enemy2);
        enemy3= BitmapFactory.decodeResource(getResources(),R.drawable.enemy3);
        enemy4= BitmapFactory.decodeResource(getResources(),R.drawable.enemy4);
        bg= BitmapFactory.decodeResource(getResources(),R.drawable.bg);

        //生成二级缓存照片
        erjihuancun=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        gameImages.add(new BgImage(bg));//先加入背景照片
        gameImages.add(new FishImage(myfish1));
        gameImages.add(new EnemyImage(enemy1));
    }
    FishImage selectFish;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_DOWN){//手接近屏幕
            for (GameImage game:gameImages){
                if(game instanceof FishImage){

                    FishImage fish=(FishImage)game;

                    if(fish.getX()<event.getX()&&
                            fish.getY()<event.getY()&&
                            fish.getX()+fish.getWidth()>event.getX()&&
                            fish.getY()+fish.getHeight()>event.getY()){
                        selectFish=fish;
                    }else {
                        selectFish=null;
                    }
                    break;
                }
            }
        }else if (event.getAction()==MotionEvent.ACTION_MOVE){

            if (selectFish!=null){
                selectFish.setX((int) event.getX()-selectFish.getWidth()/2);
                selectFish.setY((int) event.getY()-selectFish.getHeight()/2);
            }
        }else if (event.getAction()==MotionEvent.ACTION_UP){
            selectFish=null;
        }
        return true;
    }

    private  interface GameImage{
        public Bitmap getBitmap();
        public int getX();
        public int getY();
    }

    private  class EnemyImage implements GameImage{
        private Bitmap enemy=null;

        private List<Bitmap>bitmaps=new ArrayList<Bitmap>();
        private int x;
        private int y;
        public EnemyImage(Bitmap enemy){
            this.enemy=enemy;
            bitmaps.add(enemy1);
            bitmaps.add(enemy2);
            bitmaps.add(enemy3);
            bitmaps.add(enemy4);

            x=-enemy.getWidth();
            Random ran =new Random();
            y=ran.nextInt(display_h-(enemy.getHeight()));
        }
        private int index=0;
        //        private int num=0;
        @Override
        public Bitmap getBitmap() {
            Bitmap bitmap=bitmaps.get(index);
//            if (num==7) {  //限制鱼的游动速度
            index++;
            if (index == bitmaps.size()) {
                index = 0;
            }
//                num=0;
//            }
            x+=10;
//            num++;
            if(x>display_w){
                gameImages.remove(this);
            }
            return bitmap;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }
    private  class FishImage implements GameImage{

        private  Bitmap myfish;
        private int x;
        private int y;
        private int width;
        private int height;
        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
        private  List<Bitmap> bitmaps=new ArrayList<Bitmap>();
        private FishImage(Bitmap myfish) {
            this.myfish = myfish;
            bitmaps.add(myfish1);
            bitmaps.add(myfish4);
            bitmaps.add(myfish2);
            bitmaps.add(myfish3);

            //得到鱼的宽和高
            width=myfish.getWidth();
            height=myfish.getHeight();

            x=(display_w - myfish.getWidth()/4)/2;
            y=display_h/2-myfish.getHeight();
        }

        private int index=0;
//        private int num=0;
        @Override
        public Bitmap getBitmap() {
            Bitmap bitmap=bitmaps.get(index);
//            if (num==7) {  //限制鱼的游动速度
                index++;
                if (index == bitmaps.size()) {
                    index = 0;
                }
//                num=0;
//            }
//            num++;
            return bitmap;
        }
//        public boolean select(int x,int y){
//
//
//        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }


    }
    //负责背景图片处理
    private class BgImage implements GameImage{
        private Bitmap bg;

        private BgImage(Bitmap bg){
            this.bg=bg;
            newBitmap=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        }

        private  Bitmap newBitmap=null;
        private int width=0;

        public  Bitmap getBitmap(){
            Paint p=new Paint();
            Canvas canvas=new Canvas(newBitmap);

            canvas.drawBitmap(bg,
                    new Rect(0,0,bg.getWidth(),bg.getHeight()),
                    new Rect(width,0,display_w+width,display_h),p);
            canvas.drawBitmap(bg,
                    new Rect(0,0,bg.getWidth(),bg.getHeight()),
                    new Rect(-display_w+width,0,width,display_h),p);
            width++;
            if (width==display_w){
                width=0;
            }
            return newBitmap;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

    }

    private boolean state=false;
    //绘画中心
    @Override
    public void run() {
        Paint p1=new Paint();
        int enemy_num=0;
        try{
            while(state){
                Canvas newCanvas=new Canvas(erjihuancun);
                for(GameImage image :(List<GameImage>)gameImages.clone()){
                    newCanvas.drawBitmap(image.getBitmap(),
                            image.getX(),image.getY(),p1);
                }

                if(enemy_num==50){
                    enemy_num=0;
                    gameImages.add(new EnemyImage(enemy1));
                }
                enemy_num++;
                Canvas canvas=holder.lockCanvas();
                canvas.drawBitmap(erjihuancun,0,0,p1);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        display_w=width;
        display_h=height;
        init();
        this.holder=holder;
        state=true;
        new Thread(this).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        state=false;
    }


}
