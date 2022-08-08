package com.example.disha.ViewDetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.disha.AddPlace.data.DAOPlaceData;
import com.example.disha.AddPlace.data.PlaceData;
import com.example.disha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class FragmentShowImageList extends Fragment {

    GridView gallery;
    PlaceData data;
    ArrayList<Uri> list;
    public FragmentShowImageList(PlaceData data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show_image_list, container, false);
        gallery = root.findViewById(R.id.gridView);
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Getting Images");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        DAOPlaceData dao = new DAOPlaceData();
        list = new ArrayList<>();
        StorageReference ref = dao.getStorageReference().child(data.getPlaceName() + "/Display");
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference file : listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(uri -> {
                        list.add(uri);
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            CustomAdapter adapter = new CustomAdapter(list, getContext());
                            gallery.setAdapter(adapter);
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Error Occurred"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error Occurred"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                ImageFragment img = new ImageFragment();
                Bundle b = new Bundle();
                b.putString("Photo", list.get(position).toString());
                img.setArguments(b);
                fragmentTransaction.replace(R.id.photo_fragment_container, img).commit();
            }
        });
        return root;
    }
    public class CustomAdapter extends BaseAdapter {
        private ArrayList<Uri> list;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(ArrayList<Uri> list, Context context) {
            this.list = list;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.row_items, parent, false);
            }

            ImageView img = convertView.findViewById(R.id.imageView);
            Glide.with(convertView).load(list.get(position)).into(img);

            return convertView;
        }
    }
}