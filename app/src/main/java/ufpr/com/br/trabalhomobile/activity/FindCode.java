package ufpr.com.br.trabalhomobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ufpr.com.br.trabalhomobile.R;

/**
 * Activity para filtrar um aluno por matrícula
 */
public class FindCode extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_code);

        Button btFindCode = (Button)findViewById(R.id.btFindCodeA);
        btFindCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText code = (EditText)findViewById(R.id.etFindCode);

        // Valida se o valor informado é um inteiro
        Integer value = 0;
        try {
            value = Integer.parseInt(code.getText().toString());
        } catch (NumberFormatException e) {
            code.setText("");
            System.out.println("VALOR INVALIDO");
        }

        // Valida o campo
        if (code.getText().toString().isEmpty() || value <= 0) {
            CharSequence text = "Valor informado inválido. Tente novamente";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(view.getContext(), List.class);

            intent.putExtra("matricula", code.getText().toString());
            System.out.println("PARAM ... " + code.getText().toString());
            view.getContext().startActivity(intent);
        }
    }
}
