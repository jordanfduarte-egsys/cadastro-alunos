package ufpr.com.br.trabalhomobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ufpr.com.br.trabalhomobile.R;
import ufpr.com.br.trabalhomobile.helper.DataTable;
import ufpr.com.br.trabalhomobile.helper.DataTableModel;
import ufpr.com.br.trabalhomobile.model.Aluno;
import ufpr.com.br.trabalhomobile.service.Service;

public class List extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        Intent myIntent = getIntent();

        System.out.println("Preparando ... ");
        java.util.List<Aluno> alunos = new ArrayList<Aluno>();

        Service service = new Service();
        if (myIntent.hasExtra("matricula")) {
            String matricula = myIntent.getStringExtra("matricula");

            System.out.println("FIND ... " + matricula);
            Aluno aluno = service.getByCode(Integer.parseInt(matricula));
            TextView tvFindByCodeList = (TextView)findViewById(R.id.tvFindByCodeList);
            tvFindByCodeList.setText("Buscando por: #" + matricula);
            System.out.println("FIND ... " + aluno.getCpf());
            if (!aluno.getCpf().isEmpty()) {
                alunos.add(aluno);
            }
        } else {
            alunos = service.getAll();
        }

        System.out.println("Total de alunos ... " + alunos.size());

        DataTable dataTable = new DataTable(
                DataTableModel.getAllColumns(),
                DataTableModel.objectToDataModel(alunos)
        );

        dataTable.create(this, (TableLayout)findViewById(R.id.tlAppend));
        System.out.println("OK");

        if (service.hasError()) {
            CharSequence text = "Erro ao consultar o web service!";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
