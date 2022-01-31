package com.example.internshalaproject1;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.internshalaproject1.adapter.ImageAdapter;
import com.example.internshalaproject1.api.ApiUtilities;
import com.example.internshalaproject1.model.ImageModel;
import com.example.internshalaproject1.model.SearchModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ImageModel> list;
    private Button search;
    private EditText editText;

    private int page=1;
    private int pageSize=30;
    private boolean loading;
    private boolean lastPage;
    private String st;
    private ProgressDialog dialog;

    private GridLayoutManager manager;
    private ImageAdapter imageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String network = getArguments().getString("msg");
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=view.findViewById(R.id.search_recycler_view);
        editText=view.findViewById(R.id.edit_text);
        search=view.findViewById(R.id.btn_search);


        if(network.equals("true")){
            search.setEnabled(true);
        }else{
            search.setEnabled(false);
        }

        list=new ArrayList<>();
        imageAdapter=new ImageAdapter(getContext(), list);
        manager= new GridLayoutManager(getContext(),3);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 st=editText.getText().toString();

                dialog=new ProgressDialog(getContext());
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();

                if(!st.equals(null)){
                    getData(st);
                }else{
                    dialog.dismiss();
                    Toast.makeText(getContext(), "No keyword entered", Toast.LENGTH_SHORT).show();

                }
            }
        });



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
                    if((visibleItem+firstVisibleItemPos>totalItem)
                            && firstVisibleItemPos >=0 &&
                            totalItem>=pageSize){
                        page++;
                        getData(st);
                    }
                }
            }
        });
        
        return view;
    }

    private void getData(String keyword) {

        loading=true;
        ApiUtilities.getApiInterface().searchImages(keyword)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        list.clear();
                        list.addAll(response.body().getResults());
                        imageAdapter.notifyDataSetChanged();
                        loading=false;
                        dialog.dismiss();

                        if(list.size()>0){
                            lastPage=list.size()<pageSize;
                        }else lastPage=true;
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Failed to load data "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}