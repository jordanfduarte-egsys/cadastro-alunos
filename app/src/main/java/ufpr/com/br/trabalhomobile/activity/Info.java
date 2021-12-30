package ufpr.com.br.trabalhomobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import ufpr.com.br.trabalhomobile.R;
import ufpr.com.br.trabalhomobile.model.Aluno;
import ufpr.com.br.trabalhomobile.service.Service;

/**
 * Activity para mostrar informação do aluno
 */
public class Info extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Intent myIntent = getIntent();

        // Captura o parâmetro da matrícula
        if (myIntent.hasExtra("matricula")) {
            int matricula = myIntent.getIntExtra("matricula", 0);
            Service service = new Service();

            Aluno aluno = service.getByCode(matricula);
            System.out.println("FIND ... " + aluno.getCpf());

            // Instancia as labels para mostrar as informações
            TextView tvNome = (TextView)findViewById(R.id.tvNome);
            TextView tvCpf = (TextView)findViewById(R.id.tvCpf);
            TextView tvDtaNascimento = (TextView)findViewById(R.id.tvDtaNacimento);
            TextView tvLogradouro = (TextView)findViewById(R.id.tvLogradouro);
            TextView tvNumero = (TextView)findViewById(R.id.tvNumero);
            TextView tvComplemento = (TextView)findViewById(R.id.tvComplemento);
            TextView tvBairro = (TextView)findViewById(R.id.tvBairro);

            TextView tvCep = (TextView)findViewById(R.id.tvCep);
            TextView tvCidade = (TextView)findViewById(R.id.tvCidade);
            TextView tvEstado = (TextView)findViewById(R.id.tvEstado);
            Button btEdit = (Button)findViewById(R.id.btEditInfo);

            // Formata a data
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            tvNome.setText(aluno.getNome());
            tvCpf.setText(aluno.getCpf());
            tvDtaNascimento.setText(df.format(aluno.getDtaNascimento()));
            tvLogradouro.setText(aluno.getEndereco().getLogradouro());
            tvNumero.setText(aluno.getEndereco().getNumero());
            tvComplemento.setText(!aluno.getEndereco().getComplemento().isEmpty()
                ? aluno.getEndereco().getComplemento()
                : "-"
            );
            tvBairro.setText(aluno.getEndereco().getBairro());
            tvCep.setText(aluno.getEndereco().getCep());
            tvCidade.setText(aluno.getEndereco().getCidade());
            tvEstado.setText(aluno.getEndereco().getEstado());

            // evento de click no botão de editar
            btEdit.setOnClickListener(this);
        } else {
            this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent myIntent = getIntent();
        Intent intent = new Intent(view.getContext(), Register.class);
        intent.putExtra("matricula",  myIntent.getIntExtra("matricula", 0));
        view.getContext().startActivity(intent);
    }
}
