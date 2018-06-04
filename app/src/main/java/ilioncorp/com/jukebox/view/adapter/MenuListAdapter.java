package ilioncorp.com.jukebox.view.adapter;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;

import ilioncorp.com.jukebox.model.dto.MenuVO;

@SuppressLint("ValidFragment")
public class MenuListAdapter extends Fragment {
    ArrayList<MenuVO> listitems;
    RecyclerView MyRecyclerView;


    public MenuListAdapter(ArrayList<MenuVO> menuList) {
        listitems = new ArrayList<>();
        listitems = menuList;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_menu, container, false);
        MyRecyclerView =  view.findViewById(R.id.listMenu);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new adaptardorMenu(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        return view;
    }
    public class adaptardorMenu extends RecyclerView.Adapter<MyViewHolder>{
        private ArrayList<MenuVO> list;

        public adaptardorMenu(ArrayList<MenuVO> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Glide.with(getContext()).load(list.get(position).getImage()).placeholder(R.drawable.error)
                    .into(holder.imageItem);
            holder.nameItem.setText(list.get(position).getName());
            holder.priceItem.setText(String.valueOf(list.get(position).getPrice()));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageItem;
        TextView nameItem;
        TextView priceItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.ivImageMenuItem);
            nameItem = itemView.findViewById(R.id.tvNameMenuItem);
            priceItem = itemView.findViewById(R.id.tvPriceMenuItem);

        }
    }


}
