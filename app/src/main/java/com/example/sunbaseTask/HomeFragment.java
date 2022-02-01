package com.example.sunbaseTask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunbaseTask.adapter.ImageAdapter;
import com.example.sunbaseTask.api.ApiUtilities;
import com.example.sunbaseTask.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ImageModel>  list;
    private GridLayoutManager manager;
    private ImageAdapter imageAdapter;

    private int page=1;
    private int pageSize=30;
    private boolean loading;
    private boolean lastPage;

    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);



        recyclerView=view.findViewById(R.id.home_recycler_view);
        list=new ArrayList<>();
        imageAdapter=new ImageAdapter(getContext(), list);
        manager= new GridLayoutManager(getContext(),3);

        dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();


        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem=manager.getChildCount();
                int totalItem=manager.getItemCount();
                int firstVisibleItemPos=manager.findFirstVisibleItemPosition();

                //we reach end of page then the data is loaded
                if(!loading && !lastPage){
                    if((visibleItem+firstVisibleItemPos>=totalItem)
                    && firstVisibleItemPos >=0 &&
                    totalItem>=pageSize){
                        page++;
                        getData();
                    }
                }
            }
        });

        return view;
    }

    private void getData() {
        loading=true;
        ApiUtilities.getApiInterface().getImages(page, pageSize)
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if(response.body()!=null){
                            list.addAll(response.body());
                            imageAdapter.notifyDataSetChanged();
                            loading=false;
                            dialog.dismiss();

                            if(list.size()>0) {
                                lastPage=list.size()<pageSize;
                            }else lastPage=true;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Error loading data "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

//without pagination only 30 images will be loaded. Therefore we implement pagination