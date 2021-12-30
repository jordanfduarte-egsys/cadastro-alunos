package ufpr.com.br.trabalhomobile.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ufpr.com.br.trabalhomobile.activity.Register;
import ufpr.com.br.trabalhomobile.activity.Info;
import ufpr.com.br.trabalhomobile.service.Service;

import static android.view.View.TEXT_ALIGNMENT_VIEW_END;

/**
 * Helper para construção do datatable de listagem
 */
public class DataTable {
    private ArrayList<String> columns = new ArrayList<String>();
    private List<DataTableModel> data = new ArrayList<DataTableModel>();
    private TableLayout t1;

    public static final int SPINNER_VIEW = 1;
    public static final int SPINNER_REMOVE = 2;
    public static final int SPINNER_EDIT = 3;

    /**
     * @param columns
     * @param data
     */
    public DataTable(ArrayList<String> columns, List<DataTableModel> data) {
        this.columns = columns;
        this.data = data;
    }

    /**
     * Cria-se o datatable
     * @param content
     * @param tableDefault
     */
    public void create(Context content, TableLayout tableDefault) {
        t1 = tableDefault;

        // tem dados na listagem
        if (data.size() > 0) {
            TableRow tRow = new TableRow(content);
            TableLayout.LayoutParams layoutParamsRow = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            );
            tRow.setLayoutParams(layoutParamsRow);
            tRow.setGravity(TableRow.TEXT_ALIGNMENT_CENTER);

            // monta o cabeçalho do datatable
            for (String column : columns) {
                TextView tv = createCustomTextView(content);
                tv.setText(column);
                tRow.addView(tv);
            }
            t1.addView(tRow);

            // ações do registro
            List<String> spinnerArray = new ArrayList<String>();
            spinnerArray.add("Selecione");
            spinnerArray.add("Visualizar");
            spinnerArray.add("Remove");
            spinnerArray.add("Editar");

            // monta os registros
            for (DataTableModel model : data) {
                TableRow tRowColumn = new TableRow(content);

                TableLayout.LayoutParams layoutParamsColumn = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                );
                tRowColumn.setLayoutParams(layoutParamsColumn);
                tRowColumn.setGravity(TableRow.TEXT_ALIGNMENT_CENTER);

                // coluna cpf
                TextView tvCpf = createCustomTextView(content);
                tvCpf.setText(model.getCpf());

                // coluna matricula
                TextView tvCode = createCustomTextView(content);
                tvCode.setText(String.valueOf(model.getMatricula()));

                // coluna nome
                TextView tvNome = createCustomTextView(content);
                tvNome.setText(model.getNome());

                // coluna ação
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        content,
                        android.R.layout.simple_spinner_item,
                        spinnerArray
                );
                Spinner spinner = createCustomSpinner(content);
                spinner.setId(model.getMatricula().intValue());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // atribui os campos a linha
                tRowColumn.addView(tvCode);
                tRowColumn.addView(tvCpf);
                tRowColumn.addView(tvNome);
                tRowColumn.addView(spinner);

                // cria o evento no spinner
                onChangeSpinner(spinner);

                // adiciona para o layout a linha
                t1.addView(tRowColumn);
            }
        } else {
            // nenhum registro encontrado
            // apresenta mensagem de erro
            TableRow tRowColumn = new TableRow(content);

            TableLayout.LayoutParams layoutParamsColumn = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            );
            tRowColumn.setLayoutParams(layoutParamsColumn);
            tRowColumn.setGravity(TableRow.TEXT_ALIGNMENT_CENTER);

            TextView message = createCustomTextView(content);
            message.setText("Nenhum registro encontrado");

            tRowColumn.addView(message);
            t1.addView(tRowColumn);
        }
    }

    /**
     * Cria oevento no spinner
     * @param spinner
     */
    private void onChangeSpinner(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parentView, final View selectedItemView, int myPosition, long myID) {
                Intent intent;
                System.out.println("VIEW : => " + myPosition + "ID: " + parentView.getId());
                switch (myPosition) {
                    // Ao visualizar o registro
                    case SPINNER_VIEW:
                        intent = new Intent(selectedItemView.getContext(), Info.class);
                        intent.putExtra("matricula", parentView.getId());
                        selectedItemView.getContext().startActivity(intent);
                        parentView.setSelection(0);
                        break;
                    // Ao remover o registro
                    case SPINNER_REMOVE:
                        AlertDialog.Builder builder = new AlertDialog.Builder(selectedItemView.getContext());
                        builder.setMessage(String.format("Deseja remover o aluno #%d ?", parentView.getId()));

                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Service service = new Service();
                                CharSequence text = "ERRO ao remover o aluno!";
                                if (service.delete(selectedItemView.getContext(), parentView.getId())) {
                                    dialog.dismiss();

                                    DataTable dataTable = new DataTable(
                                        DataTableModel.getAllColumns(),
                                        DataTableModel.objectToDataModel(service.getAll())
                                    );

                                    t1.removeAllViews();
                                    dataTable.create(
                                        selectedItemView.getContext(),
                                        t1
                                    );
                                    text = "Aluno REMOVIDO com sucesso!";
                                }

                                Toast toast = Toast.makeText(
                                    selectedItemView.getContext().getApplicationContext(),
                                    text,
                                    Toast.LENGTH_SHORT
                                );

                                toast.show();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            parentView.setSelection(0);
                            dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        break;
                    // Ao editar o registro
                    case SPINNER_EDIT:
                        intent = new Intent(selectedItemView.getContext(), Register.class);
                        intent.putExtra("matricula", parentView.getId());
                        selectedItemView.getContext().startActivity(intent);
                        parentView.setSelection(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    /**
     * Cria um campo de texto padrão
     * @param content
     * @return
     */
    private AppCompatTextView createCustomTextView(Context content) {
        AppCompatTextView cel = new android.support.v7.widget.AppCompatTextView(content) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(2);
                getLocalVisibleRect(rect);
                canvas.drawRect(rect, paint);
            }
        };
        cel.setTextSize(18);
        cel.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
        cel.setPadding(6, 4, 6, 4);

        return cel;
    }

    /**
     * Cria um spinner padrão
     * @param content
     * @return
     */
    private AppCompatSpinner createCustomSpinner(Context content) {
        AppCompatSpinner spinner = new android.support.v7.widget.AppCompatSpinner(content) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(2);
                getLocalVisibleRect(rect);
                canvas.drawRect(rect, paint);
            }
        };

        spinner.setPadding(0, 7, 35, 5);
        //spinner.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return spinner;
    }
}
