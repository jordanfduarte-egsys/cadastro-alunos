package ufpr.com.br.trabalhomobile.helper;

import java.util.ArrayList;
import java.util.List;
import ufpr.com.br.trabalhomobile.model.Aluno;

/**
 * Classe que popula dados de registros no datatable
 */
public class DataTableModel {
    private String cpf;
    private String nome;
    private Long matricula;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    /**
     * converte um arraylist de aluno para arraylist de dados do datatable
     * @param alunos
     * @return
     */
    public static List<DataTableModel> objectToDataModel(List<Aluno> alunos) {
        List<DataTableModel> dataModel = new ArrayList<DataTableModel>();

        for (Aluno aluno : alunos) {
            DataTableModel dataTableModel = new DataTableModel();
            dataTableModel.setCpf(aluno.getCpf()
                    .replace(".", "")
                    .replace("-", "")
            );
            dataTableModel.setMatricula(aluno.getId());
            dataTableModel.setNome((aluno.getNome().split(" "))[0]);

            dataModel.add(dataTableModel);
        }

        return dataModel;
    }

    /**
     * Recupera as colunas do datatable
     * @return
     */
    public static ArrayList<String> getAllColumns() {
        ArrayList<String> ref = new ArrayList<String>();
        ref.add("#");
        ref.add("CPF");
        ref.add("Nome");
        ref.add("Ação");

        return ref;
    }
}
