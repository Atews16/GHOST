/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.os.Handler;
public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private SimpleDictionary simpleDictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private Button challenge,restart;
    private TextView ghostText,status;
    private RadioButton easy,hard;
    private String ProbableWord;
    private int turn;
    private String word;
    private int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
      try
      {
          InputStream inputStream=assetManager.open("words.txt");
          dictionary=new SimpleDictionary(inputStream);
      }catch (IOException io){}

        easy=(RadioButton)findViewById(R.id.easy);
        hard=(RadioButton)findViewById(R.id.hard);
        challenge=(Button)findViewById(R.id.challenge);
        restart=(Button)findViewById(R.id.restart);
        ghostText=(TextView)findViewById(R.id.ghostText);
        status=(TextView)findViewById(R.id.gameStatus);
        ghostText.setText("SELECT MODE TO BEGIN");
        challenge.setEnabled(false);
        restart.setEnabled(false);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenge.setEnabled(true);
                restart.setEnabled(true);
                ghostText.setText("");
                mode=0;
                easy.setEnabled(false);
                hard.setEnabled(false);
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ghostText, InputMethodManager.SHOW_IMPLICIT);
                onStart(null);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenge.setEnabled(true);
                restart.setEnabled(true);
                ghostText.setText("");
                mode=1;
                easy.setEnabled(false);
                hard.setEnabled(false);
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ghostText, InputMethodManager.SHOW_IMPLICIT);
                onStart(null);
            }
        });


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ghostText.setText("");
                challenge.setEnabled(true);
                restart.setEnabled(false);
                ghostText.setText("SELECT MODE TO BEGIN");
                status.setText("Game Begins again");
                easy.setChecked(false);
                easy.setEnabled(true);
                hard.setChecked(false);
                hard.setEnabled(true);
            }
        });
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=ghostText.getText().toString();
                String Userword=dictionary.getAnyWordStartingWith(word);
                if(word.equals(Userword))
                {
                    Toast.makeText(getBaseContext(),"You Win!!",Toast.LENGTH_SHORT).show();
                    status.setText("USER WINS!");
                }
                else {
                    ghostText.setText(Userword);
                    Toast.makeText(getBaseContext(),"Computer Wins!!",Toast.LENGTH_SHORT).show();
                    status.setText("COMPUTER WINS!!");
                    challenge.setEnabled(false);
                }

            }
        });
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
       /* if(easy.isChecked())
        {
            challenge.setEnabled(true);
            restart.setEnabled(true);
            mode=0;
        }
        if(hard.isChecked())
        {
            challenge.setEnabled(true);
            restart.setEnabled(true);
            mode=1;
        }*/

        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
            turn=0;
        } else {
            label.setText(COMPUTER_TURN);
            status.setText("Computer's Turn");
            turn=1;
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        status.setText("Computer's Turn");
        TextView label = (TextView) findViewById(R.id.gameStatus);

        word=ghostText.getText().toString();
        if(word=="")
            System.out.println("null");
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setText("Computer's Turn");
                if(word.equals(""))
                {
                    char initial='a';
                    int ran=random.nextInt(25);
                    initial+=ran;
                    ghostText.setText(""+initial);
                    status.setText("Your Turn");
                    word=ghostText.getText().toString();
                }

                else {
                    System.out.println("-------------->"+word);
                    String Computerword="";
                    if(mode==0)
                        Computerword=dictionary.getAnyWordStartingWith(word);
                    if(mode==1)
                     Computerword=dictionary.getGoodWordStartingWith(word,turn);
                    System.out.println(Computerword);
                    if (Computerword.equals("noword")) {
                        challenge.setEnabled(false);
                        status.setText("Computer Wins!!");
                        Toast.makeText(getBaseContext(), "Computer Wins!\n No such word", Toast.LENGTH_SHORT).show();

                    } else if (Computerword.equals(word)) {
                        challenge.setEnabled(true);
                        status.setText("Computer Wins!!");
                        Toast.makeText(getBaseContext(), "Computer Wins!\nYou ended the game", Toast.LENGTH_SHORT).show();
                    } else {
                            ProbableWord=Computerword;
                            ghostText.setText(Computerword.substring(0, word.length() + 1));
                            status.setText("Your Turn");
                    }
                }
            }
        },1000);
//        String Computerword=simpleDictionary.getAnyWordStartingWith(word);
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char KeyPressed=(char)event.getUnicodeChar();
        if(KeyPressed>='a'&&KeyPressed<='z')
        {
            String Text=ghostText.getText().toString();
            Text+=""+KeyPressed;
            ghostText.setText(Text);
            computerTurn();

        }
        else
        {
            Toast.makeText(getBaseContext(),"Invalid Character",Toast.LENGTH_SHORT).show();

        }
        return super.onKeyUp(keyCode, event);

    }
}
