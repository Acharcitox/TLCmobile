package com.acharcitox.telocuido;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context context;

    public SlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.slide_screen, container, false);

        ImageView logo = view.findViewById(R.id.logo);
        ImageView indicador1 = view.findViewById(R.id.ims1);
        ImageView indicador2 = view.findViewById(R.id.ims2);
        ImageView indicador3 = view.findViewById(R.id.ims3);

        TextView titulo = view.findViewById(R.id.titulo);

        ImageView previo = view.findViewById(R.id.previo);
        ImageView siguiente = view.findViewById(R.id.siguiente);

        Button btnComenzar = view.findViewById(R.id.btnComenzar);
        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnboardActivity.viewPager.setCurrentItem(position+1);
            }
        });
        previo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnboardActivity.viewPager.setCurrentItem(position-1);
            }
        });

        switch (position){
            case 0:
                logo.setImageResource(R.drawable.ic_city_driver_cuate);
                indicador1.setImageResource(R.drawable.seleccionado);
                indicador2.setImageResource(R.drawable.no_seleccionado);
                indicador3.setImageResource(R.drawable.no_seleccionado);

                titulo.setText("¿Buscas Estacionar?");
                previo.setVisibility(View.VISIBLE);
                siguiente.setVisibility(View.GONE);
                break;
            case 1:
                logo.setImageResource(R.drawable.ic_paper_map_cuate);
                indicador1.setImageResource(R.drawable.no_seleccionado);
                indicador2.setImageResource(R.drawable.seleccionado);
                indicador3.setImageResource(R.drawable.no_seleccionado);

                titulo.setText("Un cuidacoches te ayudará");
                previo.setVisibility(View.VISIBLE);
                siguiente.setVisibility(View.VISIBLE);
                break;
            case 2:
                logo.setImageResource(R.drawable.ic_online_review_rafiki);
                indicador1.setImageResource(R.drawable.no_seleccionado);
                indicador2.setImageResource(R.drawable.no_seleccionado);
                indicador3.setImageResource(R.drawable.seleccionado);

                titulo.setText("Encuentra un lugar\nCalifica a tu cuidacoches");
                previo.setVisibility(View.VISIBLE);
                siguiente.setVisibility(View.GONE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
