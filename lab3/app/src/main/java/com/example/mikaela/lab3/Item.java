package com.example.mikaela.lab3;


import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;

class Item extends View{
     String name;
     Paint items = new Paint();

     public Item(Context context, String currentName) {
          super(context);
          name = currentName;
     }

     @Override
     protected void onDraw(Canvas canvas){

          items.setColor(Color.BLACK);
          items.setTextSize(35);

          if(name != null) {
               canvas.drawText(name, 40, 50, items);
          }
          else {
               canvas.drawText("", 40, 50, items);
          }

     }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        //Sets the dimensions for the item.
        setMeasuredDimension(300,80);
    }
}
