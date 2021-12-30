package ufpr.com.br.trabalhomobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import ufpr.com.br.trabalhomobile.R;
import ufpr.com.br.trabalhomobile.model.Aluno;
import ufpr.com.br.trabalhomobile.model.Endereco;
import ufpr.com.br.trabalhomobile.service.Service;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

/**
 * Activity para cadastro e edição de aluno
 */
public class Register extends AppCompatActivity implements View.OnClickListener  {

    private EditText etNomeRegister;
    private EditText edCpfRegister;
    private EditText edDtaNascimentoRegister;
    private EditText etLogradouroRegister;
    private EditText etNumeroRegister;
    private EditText etComplementoRegister;
    private EditText etBairroRegister;
    private EditText etCepRegister;
    private EditText etCidadeRegister;
    private EditText etEstadoRegister;
    private Button btSalvar;
    private Aluno aluno;
    private AwesomeValidation awesomeValidation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent myIntent = getIntent();

        // Instancia os campos de edição
        etNomeRegister = (EditText)findViewById(R.id.etNomeRegister);
        edCpfRegister = (EditText)findViewById(R.id.edCpfRegister);
        edDtaNascimentoRegister = (EditText)findViewById(R.id.edDtaNascimentoRegister);
        etLogradouroRegister = (EditText)findViewById(R.id.etLogradouroRegister);
        etNumeroRegister = (EditText)findViewById(R.id.etNumeroRegister);
        etComplementoRegister = (EditText)findViewById(R.id.etComplementoRegister);
        etBairroRegister = (EditText)findViewById(R.id.etBairroRegister);
        etCepRegister = (EditText)findViewById(R.id.tvCepRegister);
        etCidadeRegister = (EditText)findViewById(R.id.tvCidadeRegister);
        etEstadoRegister = (EditText)findViewById(R.id.tvEstadoRegister);
        btSalvar = (Button)findViewById(R.id.btSalvarRegister);

        // se tiver o parametro de matrícula é edição
        if (myIntent.hasExtra("matricula")) {
            int matricula = myIntent.getIntExtra("matricula", 0);

            // busca o aluno pelo o ID
            Service service = new Service();
            aluno = service.getByCode(matricula);

            // altera o titulo para ficar visivel que a ação é edição
            System.out.println("FIND ... " + aluno.getCpf());
            TextView tvInfoTitle = (TextView)findViewById(R.id.tvInfoTitle);
            tvInfoTitle.setText("Editar aluno #" + matricula);

            // popula os dados do aluno
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            etNomeRegister.setText(aluno.getNome());
            edCpfRegister.setText(aluno.getCpf());
            edDtaNascimentoRegister.setText(df.format(aluno.getDtaNascimento()));
            etLogradouroRegister.setText(aluno.getEndereco().getLogradouro());
            etNumeroRegister.setText(aluno.getEndereco().getNumero());
            etComplementoRegister.setText(aluno.getEndereco().getComplemento());
            etBairroRegister.setText(aluno.getEndereco().getBairro());
            etCepRegister.setText(aluno.getEndereco().getCep());
            etCidadeRegister.setText(aluno.getEndereco().getCidade());
            etEstadoRegister.setText(aluno.getEndereco().getEstado());
        } else {
            // no caso de cadastro de aluno, cria-se apenas os objetos vazio
            aluno = new Aluno();
            aluno.setEndereco(new Endereco());
        }

        // validador de campos
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // configuração do validador
        // cada campo contem um regex, que é validado ao submeter o form
        awesomeValidation.addValidation(
                this,
                R.id.etNomeRegister,
                "[A-Z][a-z]* [A-Z][a-z]*",
                R.string.nameerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.edCpfRegister,
                "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})",
                R.string.cpferror
        );
        awesomeValidation.addValidation(
                this,
                R.id.edDtaNascimentoRegister,
                "^(?:(?:31(\\/)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$",
                R.string.dtanascimentoerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.etLogradouroRegister,
                "[a-zA-Z 0-9]+",
                R.string.logradouroerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.etNumeroRegister,
                "[0-9]+",
                R.string.numeroerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.etBairroRegister,
                "[a-zA-Z 0-9]+",
                R.string.bairroerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.tvCepRegister,
                "^[0-9]{2}[0-9]{3}-[0-9]{3}$",
                R.string.ceperror
        );
        awesomeValidation.addValidation(
                this,
                R.id.tvCidadeRegister,
                "[a-zA-Z ]+",
                R.string.cidadeerror
        );
        awesomeValidation.addValidation(
                this,
                R.id.tvEstadoRegister,
                "[a-zA-Z ]+",
                R.string.estadoerror
        );

        // atribui um evento ao click de submit
        btSalvar.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();

        btSalvar.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        btSalvar.setEnabled(false);

        // popula os dados preenchidos
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        aluno.setCpf(edCpfRegister.getText().toString());
        aluno.setNome(etNomeRegister.getText().toString());
        aluno.getEndereco().setLogradouro(etLogradouroRegister.getText().toString());
        aluno.getEndereco().setNumero(etNumeroRegister.getText().toString());
        aluno.getEndereco().setComplemento(etComplementoRegister.getText().toString());
        aluno.getEndereco().setBairro(etBairroRegister.getText().toString());
        aluno.getEndereco().setCep(etCepRegister.getText().toString());
        aluno.getEndereco().setCidade(etCidadeRegister.getText().toString());
        aluno.getEndereco().setEstado(etEstadoRegister.getText().toString());

        // validação da data de nascimento
        try {
            aluno.setDtaNascimento(df.parse(edDtaNascimentoRegister.getText().toString()));
        } catch (ParseException e) {
            CharSequence text = "Data informada inválida. Formato aceito dd/MM/yyyy";
            Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }

        if (awesomeValidation.validate()) {
            Service service = new Service();

            // se for edição
            if (aluno.getId().intValue() > 0) {
                CharSequence text = "ERRO ao ALTERAR o cadastro!";
                if (service.update(view.getContext(), aluno)) {
                    this.finish();
                    text = "Aluno ALTERADO com sucesso!";
                }

                // apresenta mensagem
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // no cadastro
                CharSequence text = "ERRO ao CADASTRAR!";
                if (service.save(view.getContext(), aluno)) {
                    this.finish();
                    text = "Aluno CRIADO com sucesso!";
                }

                // apresenta mensagem
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        // habilita novamente o botão
        btSalvar.setEnabled(true);
    }
}
