package com.example.mikaela.lab2;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.text.TextWatcher;
import android.text.Editable;


public class MainActivity extends Activity{

    private ExpandListAdapter ExpAdapter;
    private ExpandableListView ExpandList;
    private ArrayList<Continent> continentList = new ArrayList<Continent>();

    int index = 0;
    boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        //get reference to the ExpandableListView
        ExpandList = (ExpandableListView) findViewById(R.id.myList);
        //create the adapter by passing the ArrayList data
        ExpAdapter = new ExpandListAdapter(MainActivity.this, continentList);
        //attach the adapter to the list
        ExpandList.setAdapter(ExpAdapter);

        //expand all Groups
        expandAll();

        EditText input = (EditText) findViewById(R.id.edittext);
        input.addTextChangedListener(watch);

        //listener for child row click
        ExpandList.setOnChildClickListener(myListItemClicked);
        //listener for group heading click
        ExpandList.setOnGroupClickListener(myListGroupClicked);

    }



    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence currentText, int start, int lengthBefore, int lengthAfter) {

            if(!isClicked)
            {
                EditText theTextView = (EditText) findViewById(R.id.edittext);
                String inputString = currentText.toString().toLowerCase();

                String continentString = "", countryString = "";
                boolean countryExists = false, continentExists = false;

                int lengthOfText = currentText.length();

                expandAll();

                theTextView.setBackgroundResource(0);

                if (lengthOfText < 1 || inputString.equals("/")) {

                    return;
                }

                //dela upp sträng
                if (inputString.contains("/") && lengthOfText > 1) {
                    String[] splitString = inputString.split("/");

                    // om det finns två "/", tex /Europa/Sv
                    if(splitString.length > 2){
                        continentString = splitString[1];
                        countryString = splitString[2];
                    }
                    else{
                        //om det finns ett "/"
                        // måste kollas om det är /Eur el Eur/sve
                        if(splitString[0].equals("")){
                            continentString = splitString[1];
                            if (splitString.length > 2) {
                                countryString = splitString[2];
                            }
                        }
                        else{
                            continentString = splitString[0];
                            if (splitString.length > 1) {
                                countryString = splitString[1];
                            }
                        }
                    }

                }
                else if(lengthOfText > 0){
                    continentString = inputString;
                }

                for(int i = 0; i < ExpAdapter.getGroupCount(); i++){

                    Continent continent = continentList.get(i);
                    if(continent.getName().toLowerCase().startsWith(continentString)) {
                        continentExists = true;

                        for (int j = 0; j < continent.getCountryList().size(); j++) {

                            Country country = continent.getCountryList().get(j);

                            if (country.getName().toLowerCase().equals(countryString)) {
                                index = ExpandList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(i, j));
                                ExpandList.setItemChecked(index, true);
                                countryExists = true;

                                collapseAll();
                                ExpandList.expandGroup(i);
                            }
                            else if(country.getName().toLowerCase().startsWith(countryString)){
                                countryExists = true;
                            }
                        }
                    }
                }

                if(!continentExists || !countryExists){
                    theTextView.setBackgroundResource(R.color.red);
                    ExpandList.setItemChecked(index, false);
                }
            }
            else isClicked = false;

        }
    };


    //expand all groups
    private void expandAll() {
        int count = ExpAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            ExpandList.expandGroup(i);
        }
    }

    //collapse all groups
    private void collapseAll() {
        int count = ExpAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            ExpandList.collapseGroup(i);
        }
    }

    private void loadData(){

        ArrayList<Country> countryList = new ArrayList<Country>();
        Country country = new Country("Sverige");
        countryList.add(country);
        country = new Country("Norge");
        countryList.add(country);
        country = new Country("Finland");
        countryList.add(country);
        country = new Country("Finland");
        countryList.add(country);

        Continent continent = new Continent("Europa",countryList);
        continentList.add(continent);

        countryList = new ArrayList<Country>();
        country = new Country("Kenya");
        countryList.add(country);
        country = new Country("Sydafrika");
        countryList.add(country);
        country = new Country("Uganda");
        countryList.add(country);

        continent = new Continent("Afrika",countryList);
        continentList.add(continent);

        countryList = new ArrayList<Country>();
        country = new Country("Brazilien");
        countryList.add(country);
        country = new Country("Chile");
        countryList.add(country);
        country = new Country("Nicaragua");
        countryList.add(country);

        continent = new Continent("Sydamerika",countryList);
        continentList.add(continent);

    }

    private OnChildClickListener myListItemClicked = new OnChildClickListener() {


        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            Continent continent = continentList.get(groupPosition);
            Country country =  continent.getCountryList().get(childPosition);

            EditText editText = (EditText)findViewById(R.id.edittext);
            editText.setText("/" + continent.getName() + "/" + country.getName());
            isClicked = true;

            index = ExpandList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
            ExpandList.setItemChecked(index, true);

            return true;
        }

    };

    private OnGroupClickListener myListGroupClicked =  new OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            return false;
        }

    };

}
