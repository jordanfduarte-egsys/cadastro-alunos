package ufpr.com.br.trabalhomobile.service;

import android.content.Context;
import android.os.StrictMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ufpr.com.br.trabalhomobile.model.Aluno;
import ufpr.com.br.trabalhomobile.model.Endereco;

/**
 * Serviço para realizar o CRUD
 */
public class Service {
    private String url;
    private boolean hasError = false;

    public static final String TYPE_JSON = "application/json";
    public static final int VALIDATION_ERROR_CODE = 417;
    public static final int TIMEOUT = 5000;

    public static enum Method {
        POST, PUT, DELETE, GET;
    }

    public Service() {
        // habilita o wi-fi, e acesso a rede
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // URL conforme o ambiente
        url = "http://172.20.139.0:8080/wsAndroid/webresources/alunos";
        //url = "http://localhost:8080/wsAndroid/webresources/alunos";
    }

    /**
     * Recupera todos os registros
     * @return List
     */
    public List<Aluno> getAll() {
        List<Aluno> alunos = new ArrayList<Aluno>();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", TYPE_JSON);

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    alunos = responseToList(urlConnection);
                    break;
                default:
                    throw new IOException(
                        "Erro ao chamar o web service. Codigo: "
                            + urlConnection.getResponseCode()
                            + urlConnection.getErrorStream()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            hasError = true;
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            hasError = true;
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            hasError = true;
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            hasError = true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return alunos;
    }

    /**
     * Recupera um aluno por matricula
     * @param matricula
     * @return Aluno
     */
    public Aluno getByCode(Integer matricula) {
        Aluno aluno = new Aluno();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(this.url + "/" + matricula);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", TYPE_JSON);

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    System.out.println("200   ddf");
                    aluno = responseToObject(urlConnection);
                    break;
                case VALIDATION_ERROR_CODE: // Nenhum aluno encontrado
                    System.out.println("417   ddf");
                    break;
                default: // Outro erro não validado
                    throw new IOException(
                        "Erro ao chamar o web service. Codigo: "
                            + urlConnection.getResponseCode()
                            + urlConnection.getErrorStream()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            hasError = true;
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            hasError = true;
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            hasError = true;
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            hasError = true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return aluno;
    }

    /**
     * Atualiza um aluno
     * @param context
     * @param aluno
     * @return boolean
     */
    public boolean update(Context context, Aluno aluno) {
        boolean isValid = false;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setRequestMethod(Method.PUT.toString());
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", TYPE_JSON);
            urlConnection.setRequestProperty("Accept", TYPE_JSON);
            OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
            osw.write(aluno.toJSON());
            osw.flush();
            osw.close();

            System.out.println("JOSN : \n\n" + aluno.toJSON() + "\n\n\n");
            System.out.println("STATUS: " + urlConnection.getResponseCode());
            System.out.println("500   aluno não encontrado " + urlConnection.getInputStream());
            System.out.println("500   aluno não encontrado " + urlConnection.getErrorStream());

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    System.out.println("200   OK alterado");
                    isValid = true;
                    break;
                case VALIDATION_ERROR_CODE: // Nenhum aluno encontrado
                    System.out.println("417   aluno não encontrado " + urlConnection.getInputStream());
                    break;
                default: // Outro erro não validado
                    System.out.println("500   aluno não encontrado " + urlConnection.getInputStream());
                    System.out.println("500   aluno não encontrado " + urlConnection.getErrorStream());
                    System.out.println("500   aluno não encontrado " + urlConnection.getResponseMessage());

                    throw new IOException(
                            "Erro ao chamar o web service. Codigo: "
                                    + urlConnection.getResponseCode()
                                    + urlConnection.getErrorStream()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            hasError = true;
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            hasError = true;
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            hasError = true;
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            hasError = true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return isValid;
    }

    /**
     * Cadastra um aluno
     * @param context
     * @param aluno
     * @return boolean
     */
    public boolean save(Context context, Aluno aluno) {
        boolean isValid = false;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setRequestMethod(Method.POST.toString());
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", TYPE_JSON);
            urlConnection.setRequestProperty("Accept", TYPE_JSON);
            OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
            osw.write(aluno.toJSON());
            osw.flush();
            osw.close();

            System.out.println("JOSN : \n\n" + aluno.toJSON() + "\n\n\n");
            System.out.println("STATUS: " + urlConnection.getResponseCode());
            System.out.println("STREAM: " + urlConnection.getInputStream());
            System.out.println("ERROR STREAM: " + urlConnection.getErrorStream());

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    System.out.println("200   OK criado");
                    isValid = true;
                    break;
                case VALIDATION_ERROR_CODE: // Erro de validação
                    System.out.println("417   erro de validação form " + urlConnection.getInputStream());
                    break;
                default: // Outro erro não validado
                    System.out.println("500   outro erro " + urlConnection.getInputStream());
                    System.out.println("500   outro erro " + urlConnection.getErrorStream());
                    System.out.println("500   outro erro " + urlConnection.getResponseMessage());

                    throw new IOException(
                        "Erro ao chamar o web service. Codigo: "
                            + urlConnection.getResponseCode()
                            + urlConnection.getErrorStream()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            hasError = true;
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            hasError = true;
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            hasError = true;
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            hasError = true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return isValid;
    }

    /**
     * Deleta um aluno
     * @param context
     * @param matricula
     * @return boolean
     */
    public boolean delete(Context context, int matricula) {
        boolean isValid = false;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(this.url + "/" + matricula);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setRequestMethod(Method.DELETE.toString());
            urlConnection.setRequestProperty("Content-Type", TYPE_JSON);
            urlConnection.setRequestProperty("Accept", TYPE_JSON);

            System.out.println("STATUS: " + urlConnection.getResponseCode());
            System.out.println("STREAM: " + urlConnection.getInputStream());
            System.out.println("ERROR STREAM: " + urlConnection.getErrorStream());

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    System.out.println("200   OK removido");
                    isValid = true;
                    break;
                case VALIDATION_ERROR_CODE: // Erro de validação
                    System.out.println("417   aluno não encontrado " + urlConnection.getInputStream());
                    break;
                default: // Outro erro não validado
                    System.out.println("500   outro erro " + urlConnection.getInputStream());
                    System.out.println("500   outro erro " + urlConnection.getErrorStream());
                    System.out.println("500   outro erro " + urlConnection.getResponseMessage());

                    throw new IOException(
                        "Erro ao chamar o web service. Codigo: "
                            + urlConnection.getResponseCode()
                            + urlConnection.getErrorStream()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            hasError = true;
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            hasError = true;
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            hasError = true;
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            hasError = true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return isValid;
    }

    /**
     * Pega o retornon e converte em um array
     * @param urlConnection
     * @return List
     * @throws IOException
     */
    private List<Aluno> responseToList(HttpURLConnection urlConnection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        List<Aluno> alunos = new ArrayList<Aluno>();
        StringBuilder sb = new StringBuilder();
        String response = null;

        for (String line; (line = br.readLine()) != null; ) {
            sb.append(line + "\n");
        }

        try {
            response = sb.toString();

            if (response != null) {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Aluno aluno;
                    aluno = pupulate(new JSONObject(jsonArray.get(i).toString()));
                    alunos.add(aluno);
                }
            } else {
                throw new Exception("Erro ao receber os dados");
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return alunos;
    }

    /**
     * Pega o retorno e converte em um objeto aluno
     * @param urlConnection
     * @return Aluno
     * @throws IOException
     */
    private Aluno responseToObject(HttpURLConnection urlConnection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        Aluno aluno = new Aluno();

        StringBuilder sb = new StringBuilder();
        String response = null;

        for (String line; (line = br.readLine()) != null; ) {
            sb.append(line + "\n");
        }

        try {
            response = sb.toString();

            if (response != null) {
                aluno = pupulate(new JSONObject(response));
            } else {
                throw new Exception("Erro ao receber os dados");
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return aluno;
    }

    /**
     * Método utilizado para popular um aluno, conforme um vertice do array
     * @param jsonObject
     * @return Aluno
     * @throws JSONException
     * @throws ParseException
     */
    private Aluno pupulate(JSONObject jsonObject) throws JSONException, ParseException {
        Aluno aluno = new Aluno();
        Endereco endereco = new Endereco();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dtaNascimento = df.parse(jsonObject.get("dtaNascimento").toString());

        aluno.setCpf(jsonObject.get("cpf").toString());
        aluno.setDtaNascimento(dtaNascimento);
        aluno.setId(Long.parseLong(jsonObject.get("id").toString()));
        aluno.setNome(jsonObject.get("nome").toString());

        JSONObject jsonObjectEndereco = new JSONObject(jsonObject.get("endereco").toString());

        endereco.setBairro(jsonObjectEndereco.get("bairro").toString());
        endereco.setComplemento(jsonObjectEndereco.get("complemento").toString());
        endereco.setId(Long.parseLong(jsonObjectEndereco.get("id").toString()));
        endereco.setLogradouro(jsonObjectEndereco.get("logradouro").toString());
        endereco.setNumero(jsonObjectEndereco.get("numero").toString());
        endereco.setCep(jsonObjectEndereco.get("cep").toString());
        endereco.setCidade(jsonObjectEndereco.get("cidade").toString());
        endereco.setEstado(jsonObjectEndereco.get("estado").toString());

        aluno.setEndereco(endereco);

        return aluno;
    }

    /**
     * Se tiver erro
     * @return boolean
     */
    public boolean hasError() {
        return this.hasError;
    }
}
