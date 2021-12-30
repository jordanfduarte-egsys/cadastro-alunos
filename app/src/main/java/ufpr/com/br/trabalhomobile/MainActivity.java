package ufpr.com.br.trabalhomobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ufpr.com.br.trabalhomobile.activity.FindCode;
import ufpr.com.br.trabalhomobile.activity.List;
import ufpr.com.br.trabalhomobile.activity.Register;

/**
 * Tela inicial do APP
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btFindAll;
    private Button btFindByCode;
    private Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btFindAll = (Button)findViewById(R.id.btFIndAll);
        btFindByCode = (Button)findViewById(R.id.btFindByCode);
        btAdd = (Button)findViewById(R.id.btAddNew);

        // Eventos do botão
        btFindAll.setOnClickListener(this);
        btFindByCode.setOnClickListener(this);
        btAdd.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();

        btFindAll.setEnabled(true);
        btFindByCode.setEnabled(true);
        btAdd.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btFIndAll:
                btFindAll.setEnabled(false);
                intent = new Intent(view.getContext(), List.class);
                break;
            case R.id.btFindByCode:
                btFindByCode.setEnabled(false);
                intent = new Intent(view.getContext(), FindCode.class);
                break;
            case R.id.btAddNew:
                btAdd.setEnabled(false);
                intent = new Intent(view.getContext(), Register.class);
                break;
        }

        // inicia outra tela
        view.getContext().startActivity(intent);
    }
}
