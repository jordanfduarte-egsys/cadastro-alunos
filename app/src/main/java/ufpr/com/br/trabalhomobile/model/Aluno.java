package ufpr.com.br.trabalhomobile.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model de aluno
 */
public class Aluno implements Serializable {
    private Long id;

    private String cpf = new String();
    private String nome;
    private Date dtaNascimento;
    private Endereco endereco;

    public Aluno() {
        id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the dtaNascimento
     */
    public Date getDtaNascimento() {
        return dtaNascimento;
    }

    /**
     * @param dtaNascimento the dtaNascimento to set
     */
    public void setDtaNascimento(Date dtaNascimento) {
        this.dtaNascimento = dtaNascimento;
    }

    /**
     * @return the endereco
     */
    public Endereco getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aluno)) {
            return false;
        }
        Aluno other = (Aluno) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  this.getClass() + "[ id=" + getId() + " ]";
    }

    /**
     * Converte os dados da classe em uma string JSON
     * @return
     */
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectEndereco = new JSONObject();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            jsonObject.put("id", getId());
            jsonObject.put("nome", getNome());
            jsonObject.put("dtaNascimento", df.format(getDtaNascimento()));
            jsonObject.put("cpf", getCpf());

            jsonObjectEndereco.put("bairro", getEndereco().getBairro());
            jsonObjectEndereco.put("complemento", getEndereco().getComplemento());
            jsonObjectEndereco.put("id", getEndereco().getId());
            jsonObjectEndereco.put("logradouro", getEndereco().getLogradouro());
            jsonObjectEndereco.put("numero", getEndereco().getNumero());
            jsonObjectEndereco.put("cep", getEndereco().getCep());
            jsonObjectEndereco.put("cidade", getEndereco().getCidade());
            jsonObjectEndereco.put("estado", getEndereco().getEstado());
            jsonObject.put("endereco", jsonObjectEndereco);

            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
