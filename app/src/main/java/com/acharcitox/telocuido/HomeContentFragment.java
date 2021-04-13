package com.acharcitox.telocuido;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeContentFragment extends Fragment {

  private static final String TEXT = "text";

  public static com.acharcitox.telocuido.HomeContentFragment newInstance(String text) {
    com.acharcitox.telocuido.HomeContentFragment frag = new com.acharcitox.telocuido.HomeContentFragment();

    Bundle args = new Bundle();
    args.putString(TEXT, text);
    frag.setArguments(args);

    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
          Bundle savedInstanceState) {
    View layout = inflater.inflate(R.layout.home_fragment, container, false);

    if (getArguments() != null) {
      ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
    }

    return layout;
  }
}

