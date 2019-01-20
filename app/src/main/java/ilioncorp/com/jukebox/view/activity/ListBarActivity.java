package ilioncorp.com.jukebox.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.EstablishmentDAO;
import ilioncorp.com.jukebox.model.dao.MenuDAO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.dto.MenuVO;
import ilioncorp.com.jukebox.view.adapter.BarListAdapter;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

import static ilioncorp.com.jukebox.R.color.orange;

public class ListBarActivity extends GenericActivity implements Handler.Callback,View.OnClickListener,Runnable,SearchView.OnQueryTextListener {

    private BarListAdapter adapter;
    private ArrayList<EstablishmentVO> listItems;
    private Handler bridge;
    private android.support.v7.widget.RecyclerView listBarsRecicler;
    private Toolbar toolbar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bar);

        this.listBarsRecicler = findViewById(R.id.listBarsRecicler);
        this.listBarsRecicler.setHasFixedSize(true);
        this.listBarsRecicler.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(R.color.orange);
        toolbar.inflateMenu(R.menu.menu_search);
        setSupportActionBar(toolbar);
        showCharging("Loading");
        bridge = new Handler(this);
        EstablishmentDAO establishment = new EstablishmentDAO(bridge);
        establishment.getAllBars();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.Search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                adapter.setFilter(listItems);
                return true;
            }
        });
        return true;
    }
    private ArrayList<EstablishmentVO> filter(ArrayList<EstablishmentVO> list,String criterion){
        ArrayList<EstablishmentVO> listFilter=new ArrayList<>();
        try{
            criterion.toLowerCase();
            for (EstablishmentVO bar: list){
                    String nameBarFilter = bar.getName().toLowerCase();
                    String genderBarFilter = bar.getGenders().toLowerCase();
                    if(nameBarFilter.contains(criterion) || genderBarFilter.contains(criterion))
                        listFilter.add(bar);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listFilter;
    }

    @Override
    public boolean handleMessage(Message message) {
        listItems = (ArrayList<EstablishmentVO>) message.obj;
        adapter = new BarListAdapter(listItems,this);
        adapter.setOnClickListener(this);

        listBarsRecicler.setAdapter(adapter);
        hideCharging();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String criterion) {
        try{
            ArrayList<EstablishmentVO> listFilter = filter((ArrayList)listItems,criterion);
            adapter.setFilter(listFilter);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent pantalla = new Intent(this,BarActivity.class);
        EstablishmentVO establishment = listItems.get(listBarsRecicler.getChildAdapterPosition(view));
        pantalla.putExtra("establishment",establishment);
        startActivity(pantalla);
    }

    @Override
    public void run() {

    }
}
