package com.example.loginactivity.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.loginactivity.ExtraActivities.ExploreCategory;
import com.example.loginactivity.R;

public class FragmentExplore extends Fragment {

    GridLayout catsGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_explore, container, false);

        catsGrid = root.findViewById(R.id.category_grid_layout);
        int childCount = catsGrid.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            CardView cardView = (CardView) catsGrid.getChildAt(i);
            final int temp = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (temp) {
                        case 0:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Outdoors & Adventure"));
//                            category = "Outdoors & Adventure";
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Technology"));
//                            category = "Technology";
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Health & Wellness"));
//                            category = "Health & Wellness";
                            break;
                        case 3:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Sports & Fitness"));
//                            category = "Sports & Fitness";
                            break;
                        case 4:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Learning"));
//                            category = "Learning";
                            break;
                        case 5:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Food & Drink"));
//                            category = "Food & Drink";
                            break;
                        case 6:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Language & Culture"));
//                            category = "Language & Culture";
                            break;
                        case 7:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Music"));
//                            category = "Music";
                            break;
                        case 8:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Film"));
//                            category = "Film";
                            break;
                        case 9:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Book Clubs"));
//                            category = "Book Clubs";
                            break;
                        case 10:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Dance"));
//                            category = "Dance";
                            break;
                        case 11:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Health & Wellness"));
//                            category = "Fashion";
                            break;
                        case 12:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Social"));
//                            category = "Social";
                            break;
                        case 13:
                            startActivity(new Intent(getActivity(), ExploreCategory.class).putExtra("evType","Career & Business"));
//                            category = "Career & Business";
                            break;
                    }
                }

            });

        }

        return root;
    }

}
