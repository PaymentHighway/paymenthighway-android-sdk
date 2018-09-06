package io.paymenthighway.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DemoMainActivity extends AppCompatActivity {

    enum Action {

        ADD_CARD("Add credit card"),
        SETTINGS("Settings");

        private String description;

        Action(String description) {
            this.description=description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo_main);

        final ListView listview = (ListView) findViewById(R.id.recipe_list_view);

        final ArrayList<String> list = new ArrayList<String>();
        for (Action action : Action.values()) {
            list.add(action.getDescription());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                System.out.println("click on " + item + " position " + position);
                Action action = Action.values()[position];
                System.out.println("action " + action.getDescription());
                switch (action) {
                    case ADD_CARD:
                        Intent intent = new Intent(view.getContext(), AddCardActivity.class);
                        startActivity(intent);
                        break;
                    case SETTINGS:
                        System.out.println("Settings to be implemented");
                        break;

                }
            }

        });
    }

}