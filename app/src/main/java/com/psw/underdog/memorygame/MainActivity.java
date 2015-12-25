package com.psw.underdog.memorygame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity{

    // 초기화 영역
    private Button button;
    private ArrayList<Integer> mNumberList = new ArrayList<Integer>();
    final int MAX_COUNT = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        //Select a specific button to bundle it with the action you want
        button = (Button) findViewById(R.id.btnMemory);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showMemoryNumber();
            }

        });

    }

    private void showMemoryNumber() {
        // 기존 메모리 정보를 지운다
        mNumberList.clear();

        // MAX_COUNT만큼 저장한다.
        for(int i = 0 ; i < MAX_COUNT; i++){
            int nNumber = giveMeANumber();
            mNumberList.add(nNumber);
        }

        playAnimation();

    }

    /**
     *  지금은 이해하기 힘들 것임.
     *  Thread와 Timer내에서 UI를 처리하는
     *  교육용 예제임.
     * */
    private void playAnimation() {

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            int nIndx = 0;

            @Override
            public void run() {
                if (nIndx > mNumberList.size() - 1) {
                    cancel();
                    return;
                }

                // Android를 경악하게 만드는 것 중에 하나..
                // Timer나 Thread 내에서 UI를 액세스할 경우, 이런 식의 처리를 해주어야 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (nIndx > mNumberList.size() - 1) return;

                        // TextView에 문자를 찍는다.
                        displayNumberToText(nIndx);
                        nIndx++;
                    }
                });


            }
        }, 0, 1000);  // 1초 단위로...

    }

    /**
     * random 함수로 값을 가져온다.
     *  (*)값 이후 상수가 0~부터 값-1까지 이다.
     * */
    private int giveMeANumber() {
        return (int) (Math.random() * 10);
    }

    /**
     * 화면에 번호를 출력한다..
     * */
    public void displayNumberToText(int nNumber){
        int n = mNumberList.get(nNumber);
        TextView txtView = (TextView)findViewById(R.id.txtNumber);
        txtView.setText(">" + n + "<");
    }

    public void OnPress(View v){
        Button btn = (Button)v;
        String str = btn.getText().toString();
        int nNumber = Integer.valueOf(str);

        CheckMemoryNumber(nNumber);
    }

    private void CheckMemoryNumber(int n) {

        // 더이상 값이 없다면...
        int nSize = mNumberList.size();
        if (nSize < 1){
            // Complete
            ShowMessage("완료했습니다. memory를 눌러 다시 시작해주세요...");
            return;
        }

        int nValue = mNumberList.get(0);
        if(nValue == n){

            if(nSize > 1){
                ShowMessage(n + " 값이 맞습니다. 다음을 맞추어주세요");
            } else {
                ShowMessage("완료했습니다. memory를 눌러 다시 시작해주세요...");
            }
            mNumberList.remove(0);
        }
    }

    // Message를 출력한다.
    private void ShowMessage(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}
