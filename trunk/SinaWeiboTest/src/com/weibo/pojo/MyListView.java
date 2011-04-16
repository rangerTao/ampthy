package com.weibo.pojo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyListView extends LinearLayout implements OnGestureListener {  
    private GestureDetector mGestureDetector;  
    private ListView mListView;  
      
    public MyListView(Context context) {  
        super(context);  
        mGestureDetector = new GestureDetector(this);  
        mListView = new ListView(context);  
        String[] items = createStrings();  
        mListView.setAdapter(new ArrayAdapter<String>(context,  
                android.R.layout.simple_list_item_single_choice, items));  
        mListView.setCacheColorHint(Color.TRANSPARENT);  
        mListView.setScrollBarStyle(SCROLLBARS_OUTSIDE_INSET);  
        mListView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                Log.i("info", "click");  
            }  
        });  
        this.addView(mListView, new LinearLayout.LayoutParams(350, LayoutParams.FILL_PARENT));  
    }  
      
    public boolean onDown(MotionEvent arg0) {  
        return false;  
    }  
    
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
            float velocityY) {  
        return true;  
    }  
    
    public void onLongPress(MotionEvent e) {  
        //empty      
    }  
 
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,  
            float distanceY) {  
        int scrollWidth = mListView.getWidth() - this.getWidth();  
        if ((this.getScrollX() >= 0) && (this.getScrollX() <= scrollWidth) && (scrollWidth > 0)) {  
            int moveX = (int)distanceX;  
            if (((moveX + this.getScrollX()) >= 0) && ((Math.abs(moveX) + Math.abs(this.getScrollX())) <= scrollWidth)) {  
                this.scrollBy(moveX, 0);  
            }  
            else {  
                if (distanceX >= 0) {  
                    this.scrollBy(scrollWidth - Math.max(Math.abs(moveX), Math.abs(this.getScrollX())), 0);  
                }  
                else {  
                    this.scrollBy(-Math.min(Math.abs(moveX), Math.abs(this.getScrollX())), 0);  
                }  
            }  
        }  
        return true;  
    }  

    public void onShowPress(MotionEvent e) {  
        //empty  
    }  
 
    public boolean onSingleTapUp(MotionEvent e) {  
        return false;  
    }  
      
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev){  
        mGestureDetector.onTouchEvent(ev);  
        super.dispatchTouchEvent(ev);  
        return true;  
    }  
      
    private String[] createStrings() {  
        return new String[] {  
                "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",  
                "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",  
                "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",  
                "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",  
                "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",  
                "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",  
                "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",  
                "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",  
                "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",  
                "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",  
                "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",  
                "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",  
                "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",  
                "Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon", "Boule Du Roves",  
                "Boulette d'Avesnes", "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",  
                "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",  
                "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",  
                "Brin", "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",  
                "Briquette de Brebis", "Briquette du Forez", "Broccio", "Broccio Demi-Affine",  
                "Brousse du Rove", "Bruder Basil", "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",  
                "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase", "Button (Innes)",  
                "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta"};  
    } 
}