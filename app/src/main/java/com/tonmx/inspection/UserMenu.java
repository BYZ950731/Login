package com.tonmx.inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserMenu extends PopMenu {
    public UserMenu(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_user, null);
        return view;
    }

    @Override
    protected ListView findListView(View view) {
        return (ListView) view.findViewById(R.id.menu_listview);
    }

    @Override
    protected ArrayAdapter<Item> onCreateAdapter(Context context, ArrayList<Item> itemList) {
        return new ArrayAdapter<Item>(context, R.layout.item_menu_user, itemList);
    }
}
