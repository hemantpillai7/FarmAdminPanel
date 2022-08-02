package com.example.farmadminpanel.Fragment;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.farmadminpanel.AddProducts;
import com.example.farmadminpanel.CanceledOrderActivity;
import com.example.farmadminpanel.EditProduct;
import com.example.farmadminpanel.DeliveredListActivity;
import com.example.farmadminpanel.GardenServiceStatusActivity;
import com.example.farmadminpanel.HoneyServiceStatusActivity;
import com.example.farmadminpanel.OrderListActivity;
import com.example.farmadminpanel.ProcessingListActivity;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.ServiceRequestActivity;
import com.example.farmadminpanel.SoilServiceCompletedListActivity;
import com.example.farmadminpanel.SoilServiceProcessingListActivity;
import com.example.farmadminpanel.SoilServiceRequestListActivity;
import com.example.farmadminpanel.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
{
    FragmentHomeBinding binding;
    boolean isFABOpen = false;

    public HomeFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.floatAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeFABMenu();
                Intent i = new Intent(getActivity(), AddProducts.class);
                startActivity(i);

            }
        });

        binding.floatEditProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeFABMenu();
                Intent i = new Intent(getActivity(), EditProduct.class);
                startActivity(i);

            }
        });

        binding.floatAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeFABMenu();
                Fragment fragment = new Fragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,new CategoryFragment())
                        .addToBackStack(null).commit();

            }
        });

        binding.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), OrderListActivity.class);
                startActivity(i);
            }
        });

        binding.processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProcessingListActivity.class);
                startActivity(i);
            }
        });

        binding.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DeliveredListActivity.class);
                startActivity(i);
            }
        });

        binding.cancellist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CanceledOrderActivity.class);
                startActivity(i);
            }
        });


        binding.ServiceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ServiceRequestActivity.class);
                startActivity(i);
            }
        });

        binding.SoilRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SoilServiceRequestListActivity.class);
                startActivity(i);
            }
        });

        binding.GardenStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GardenServiceStatusActivity.class);
                startActivity(i);
            }
        });

        binding.HoneyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), HoneyServiceStatusActivity.class);
                startActivity(i);
            }
        });

        binding.SoilProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SoilServiceProcessingListActivity.class);
                startActivity(i);
            }
        });

        binding.SoilComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SoilServiceCompletedListActivity.class);
                startActivity(i);
            }
        });


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFABOpen) {
//                    linLayHeader.setBackgroundColor(getResources().getColor(R.color.trans_grey_color));
                    closeFABMenu();
                } else {
//                    linLayHeader.setBackgroundColor(getResources().getColor(R.color.white2));
                    showFABMenu();

                }
            }
        });

        return binding.getRoot();
    }

    private void showFABMenu()
    {
        isFABOpen = true;
        binding.fabLayout1.setVisibility(View.VISIBLE);
        binding.floatingActionButton.animate().rotationBy(180);
        binding.fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
    }

    private void closeFABMenu()
    {
        isFABOpen = false;

        binding.floatingActionButton.animate().rotation(0);
        binding.fabLayout1.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                if (!isFABOpen)
                {
                    binding.fabLayout1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        //binding.fabLayout1.setVisibility(View.GONE);
    }


}